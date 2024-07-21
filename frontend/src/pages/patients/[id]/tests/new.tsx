import { ChangeEvent, LegacyRef, MutableRefObject, useEffect, useRef, useState } from "react"
import { useRouter } from "next/router";
import Link from "next/link";
import axios from "axios";

const CLASS_NAMES = ['', 'elbow positive', 'fingers positive', 'forearm fracture', 'humerus fracture', 'humerus', 'shoulder fracture', 'wrist positive']

export default function addPatient() {

	const router = useRouter();
	const [id, setId] = useState(null);
	const [patient, setPatient] = useState(null);
	const [uploadedImage, setUploadedImage] = useState<string | ArrayBuffer | null>(null);
	const [loading, setLoading] = useState(false);
	const [tests, setTests] = useState<any>([]);
	const fileInput = useRef<any>(null);

	useEffect(() => {
		if(!router.isReady) return;
		const patientID: any = router.query.id;
		setId(patientID);
		const patientStorage = localStorage.getItem("patients");
		if(!patientStorage) return;
		const patientData = JSON.parse(patientStorage);
		setPatient(patientData[patientID]);
		const testData = localStorage.getItem(`patient-${patientID}`);
		if(!testData) return;
		setTests(JSON.parse(testData));
	}, [router.isReady]);

	const selectImage = (e: ChangeEvent<HTMLInputElement>) => {
		if(!e.target.files) return;
		const fileReader = new FileReader();
		fileReader.addEventListener("load", () => {
			setUploadedImage(fileReader.result);
		});
		fileReader.readAsDataURL(e.target.files[0])
	}

	const runExam = () => {
		if(!uploadedImage) return;
		// console.log(uploadedImage)
		setLoading(true);
		axios.post("http://localhost:5000/fracture_model", { imgdata: uploadedImage }).then((res) => {

			const predictions = [];
			for(let i = 0; i < res.data.scores.length; i++) {
				predictions.push({
					type: CLASS_NAMES[res.data.classes[i]],
					percentage: res.data.scores[i]*100
				});
			}

			tests.push({
				result_img: res.data.img,
				date: Date.now(),
				predictions
			});
			localStorage.setItem(`patient-${id}`, JSON.stringify(tests));

			router.push(`/patients/${id}/tests/${tests.length-1}/analyze`);
		});
	}

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