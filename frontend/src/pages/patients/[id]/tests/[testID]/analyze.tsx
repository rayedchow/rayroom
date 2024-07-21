import Navbar from "@/components/Navbar";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import instructionData from '@/data/instruction.json';

const instruction: any = instructionData;

export default function Analyze() {

	const router = useRouter();
	const [patient, setPatient] = useState(null); // patient data object; loaded from localStorage
	const [testData, setTestData] = useState<any>(null); // test data for specific patient test; loaded from localStorage

	useEffect(() => {
		if(!router.isReady) return;

		// getting patient ID from nextJS page router
		const patientID: any = router.query.id;

		// parsing patient JSON data from localStorage
		const patientStorage = localStorage.getItem("patients");
		if(!patientStorage) return;
		const patientData = JSON.parse(patientStorage);
		setPatient(patientData[patientID]);

		// parsing patient tests JSON data from localStorage and indexing for specific test using testID
		const testData = localStorage.getItem(`patient-${router.query.id}`);
		if(!testData) return;
		if(!router.query.testID) return;
		const testID: any = router.query.testID;
		setTestData(JSON.parse(testData)[testID]);
	}, [router.isReady]);

	if(!testData || !patient) return;
	return (
		<main className="flex flex-col">
			<div className="mt-12 flex flex-col gap-16 justify-center items-center text-center">
				<div className="flex flex-col gap-4 jusify-center items-center text-center">
					<div className="text-4xl text-red-400 font-semibold">Analyzing Fracture Detection Exam: {patient['name']}</div>
					<div className="text-3xl text-slate-300 italic">{new Date(testData.date).toDateString()} - {new Date(testData.date).toLocaleTimeString()}</div>
				</div>
				<div className="w-full flex justify-center gap-10">
					<div className="w-[60%] h-[60vh] bg-slate-500 ring-1 ring-slate-200 flex justify-center items-center text-slate-100 text-4xl rounded-2xl cursor-pointer font-semibold">
						<div className="w-full h-full flex justify-center items-center">
							<img src={`data:image/jpeg;base64,${testData.result_img}`} className="max-w-[95%] max-h-[95%] w-[95%] h-[95%] object-contain" />
						</div>
					</div>
					<div className="flex flex-col gap-16 text-left max-w-[40%]">
						<div className="flex flex-col gap-1">
							<div className="text-red-400 font-bold text-lg capitalize">
								MOST ACCURATE DIAGNOSIS
							</div>
							<div className="text-slate-200 font-semibold text-6xl">
								{testData.prediction_title}
							</div>
						</div>
						<div className="flex flex-col gap-6">
						{Object.keys(testData.predictions).map((type: any, i: any) => (
							<div className="text-red-400 text-medium text-2xl" key={`prediction-${i}`}>
								<b>{testData.predictions[type].count}</b> potential point(s) of {type} at <b>{Math.round(testData.predictions[type].percentage*10000)/100}%</b>
							</div>
						))}
						</div>
						<div className="flex flex-col gap-4">
							<div className="text-slate-200 font-semibold text-4xl">ER Instruction</div>
							<div className="text-slate-300 text-2xl">{instruction[testData.prediction_title]}</div>
						</div>
					</div>
				</div>
			</div>
		</main>
	)
}