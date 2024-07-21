import { ChangeEvent, useEffect, useRef, useState } from "react"
import { useRouter } from "next/router";
import axios from "axios";

// constant class names for types of bone fractures
// also used in backend model file for MetadataCatalog
const CLASS_NAMES = ['', 'elbow fracture', 'fingers fracture', 'forearm fracture', 'humerus fracture', 'humerus injury', 'shoulder fracture', 'wrist fracture']

export default function addPatient() {

	const router = useRouter();
	const [id, setId] = useState(null); // patient ID
	const [patient, setPatient] = useState(null); // patient data object; loaded from localStorage
	const [tests, setTests] = useState<any>([]); // patient tests data; loaded from localStorage

	const fileInput = useRef<any>(null); // react ref hook for HTML file input; mainly for styling reasons
	const [uploadedImage, setUploadedImage] = useState<string | ArrayBuffer | null>(null); // uploaded image from user
	const [loading, setLoading] = useState(false); // boolean for whether test results are loading

	useEffect(() => {
		if(!router.isReady) return;

		// uses NextJS dynamic page router for patientID
		const patientID: any = router.query.id;
		setId(patientID);

		// parsing patient JSON data from localStorage
		const patientStorage = localStorage.getItem("patients");
		if(!patientStorage) return;
		const patientData = JSON.parse(patientStorage);
		setPatient(patientData[patientID]);

		// parsing test JSON data from localStorage
		const testData = localStorage.getItem(`patient-${patientID}`);
		if(!testData) return;
		setTests(JSON.parse(testData));
	}, [router.isReady]);

	// used when user selects a file (for preview reasons)
	const selectImage = (e: ChangeEvent<HTMLInputElement>) => {
		if(!e.target.files) return;
		
		// getting base64 data of uploaded image and updating uploadedImage state
		const fileReader = new FileReader();
		fileReader.addEventListener("load", () => {
			setUploadedImage(fileReader.result);
		});
		fileReader.readAsDataURL(e.target.files[0])
	}

	const runExam = () => {
		if(!uploadedImage) return; // make sure image is uploaded
		setLoading(true);

		// communicating with backend Flask REST API and sending uploadedImage data
		axios.post("http://localhost:5000/fracture_model", { imgdata: uploadedImage }).then((res) => {

			const predictions: any = {}; // predictions with keys as class names (fracture types)
			let prediction_title = "no fractures"; // setting prediction title in case of no predictions listed
			let curr_highest_acc = 0; // highest confidence representation

			for(let i = 0; i < res.data.scores.length; i++) {
				const className = CLASS_NAMES[res.data.classes[i]];

				// updating prediction counts, highest percentages, and keeping the highest confidence representation and best prediction representation
				if(predictions[className]) {
					predictions[className].count++;
					predictions[className].percentage = Math.max(res.data.scores[i], predictions[className].percentage);
				} else {
					predictions[className] = {
						count: 1,
						percentage: res.data.scores[i]
					}
				} if(res.data.scores[i] > curr_highest_acc) {
					curr_highest_acc = res.data.scores[i];
					prediction_title = className;
				}
			}

			// adding new test to localStorage test data and updating localStorage
			tests.push({
				result_img: res.data.img,
				date: Date.now(),
				prediction_title,
				predictions
			});
			localStorage.setItem(`patient-${id}`, JSON.stringify(tests));

			router.push(`/patients/${id}/tests/${tests.length-1}/analyze`); // going to analyze page for newly tested x-ray screening
		});
	}

	// registering loader component by dynamic import
	useEffect(() => {
		async function getLoader() {
		  const { helix } = await import('ldrs')
		  helix.register()
		}
		getLoader()
	}, [])

	if(!patient) return <></>;
	return (
		<main className="flex flex-col min-h-[90vh] w-full">
			<div className="mt-12 flex flex-col items-center justify-between flex-grow w-full">
				<div className="flex justify-center text-center text-4xl font-medium text-red-400">New Fracture Detection: {patient['name']}</div>
				{(!loading) ? <>
					<div className="flex flex-col h-full justify-center items-center gap-5 w-full">
						{(uploadedImage && (typeof uploadedImage == 'string')) ? 
							<>
								<div className="w-[60%] h-[60vh] bg-slate-500 ring-1 ring-slate-200 flex justify-center items-center text-slate-100 text-4xl rounded-2xl cursor-pointer font-semibold" onClick={() => fileInput.current && fileInput.current.click()}>
									<div className="w-full h-full flex justify-center items-center">
										<img src={uploadedImage} className="max-w-[95%] max-h-[95%] w-[95%] h-[95%] object-contain" />
									</div>
								</div>
								<div className="text-rose-500 text-2xl italic text-medium">( click to upload a new imaging )</div>
							</>
							: 
							<div className="w-[60%] h-[60vh] bg-slate-500 ring-1 ring-slate-200 flex justify-center items-center text-slate-100 text-4xl rounded-2xl cursor-pointer font-semibold" onClick={() => fileInput.current && fileInput.current.click()}>
								Upload an X-Ray Imaging
							</div>
						}
					</div>
					<input 
						type="file"
						onChange={e => selectImage(e)}
						multiple={false}
						accept="image/*"
						ref={fileInput}
						className="hidden"
					/>
					<div className="py-5 px-10 flex justify-center items-center rounded-lg bg-rose-500 text-3xl font-semibold text-slate-800 ring-2 ring-slate-200 cursor-pointer" onClick={runExam}>Run Fracture Exams</div>
				</> : <div className="w-[60%] h-[70vh] mb-16 bg-slate-500 ring-1 ring-slate-200 flex justify-center items-center rounded-2xl cursor-pointer">
						<l-helix
							size="260"
							speed="1.5"
							color="#e11d48" 
						/>
					</div>
				}
			</div>
		</main>
	)
}