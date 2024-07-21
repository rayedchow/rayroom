import Navbar from "@/components/Navbar";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";

export default function Analyze() {

	const router = useRouter();
	const [patient, setPatient] = useState(null);
	const [testData, setTestData] = useState<any>(null);

	useEffect(() => {
		if(!router.isReady) return;
		const patientID: any = router.query.id;
		const patientStorage = localStorage.getItem("patients");
		if(!patientStorage) return;
		const patientData = JSON.parse(patientStorage);
		setPatient(patientData[patientID]);
		const testData = localStorage.getItem(`patient-${router.query.id}`);
		if(!testData) return;
		if(!router.query.testID) return;
		const testID: any = router.query.testID;
		setTestData(JSON.parse(testData)[testID]);
	}, [router.isReady]);

	if(!testData || !patient) return;
	return (
		<main className="flex flex-col">
			<div className="mt-12 flex flex-col gap-10 justify-center items-center text-center">
				<div className="flex flex-col gap-4 jusify-center items-center text-center">
					<div className="text-4xl text-red-400 font-semibold">Analyzing Fracture Detection Exam: {patient['name']}</div>
					<div className="text-3xl text-slate-300 italic">{new Date(testData.date).toDateString()} - {new Date(testData.date).toLocaleTimeString()}</div>
				</div>
				<div className="w-[60%] h-[60vh] bg-slate-500 ring-1 ring-slate-200 flex justify-center items-center text-slate-100 text-4xl rounded-2xl cursor-pointer font-semibold">
					<div className="w-full h-full flex justify-center items-center">
						<img src={`data:image/jpeg;base64,${testData.result_img}`} className="max-w-[95%] max-h-[95%] w-[95%] h-[95%] object-contain" />
					</div>
				</div>
			</div>
		</main>
	)
}