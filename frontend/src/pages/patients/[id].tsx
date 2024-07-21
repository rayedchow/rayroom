import Link from "next/link";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import { FaPlus } from "react-icons/fa6";

export default function Patient() {
	const router = useRouter();
	const [id, setId] = useState(null); // patient ID
	const [patient, setPatient] = useState(null); // patient data object; loaded from localStorage
	const [tests, setTests] = useState([]); // patient tests data; loaded from localStorage

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

	return (
		<main className="flex flex-col">
			<div className="mt-12 flex flex-col">
				{(patient && <>
				<div className="flex justify-center text-center text-4xl font-semibold text-rose-400">ER Patient: {patient['name']}</div>
				<div className="grid grid-cols-2 mt-16 gap-10 w-full">
					{tests.reverse().map((test: any, i: any) => (
						<Link className="min-h-72 flex flex-col justify-center items-center ring-2 ring-slate-200 rounded-xl bg-slate-800 cursor-pointer gap-4 text-center" href={`/patients/${id}/tests/${i}/analyze`}>
							<div className="text-slate-200 text-4xl font-semibold mt-3">X-Ray Imaging</div>
							<div className="text-red-400 font-bold text-2xl">{test.prediction_title}</div>
							<div className="text-slate-300 text-lg">{new Date(test.date).toDateString()}</div>
							<div className="text-lg my-3 italic text-red-300">( click to analyze examination )</div>
						</Link>
					))}
					<Link className="min-h-72 flex justify-center items-center ring-2 ring-slate-200 rounded-xl bg-slate-900 cursor-pointer" href={`/patients/${id}/tests/new`}>
						<FaPlus size={100} color={"#f43f5e"} />
					</Link>
				</div>
				</>)}
			</div>
		</main>
	)
}