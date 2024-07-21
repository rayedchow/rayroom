import Link from "next/link";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import { FaPlus } from "react-icons/fa6";

export default function Patient() {
	const router = useRouter();
	const [id, setId] = useState(null);
	const [patient, setPatient] = useState(null);
	const [tests, setTests] = useState([]);

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

	return (
		<main className="flex flex-col">
			<div className="mt-12 flex flex-col">
				{(patient && <>
				<div className="flex justify-center text-center text-4xl font-semibold text-rose-400">ER Patient: {patient['name']}</div>
				<div className="grid grid-cols-2 mt-16 gap-10 w-full">
					{tests.map((test: any, i: any) => (
						<Link className="min-h-72 flex flex-col justify-center items-center ring-2 ring-slate-200 rounded-xl bg-slate-800 cursor-pointer gap-2 text-center" href={`/patients/${id}/tests/${i}/analyze`}>
							<div className="text-3xl font-semibold mt-3">X-Ray Imaging</div>
							<div className="flex flex-col items-center text-center my-2 text-red-400 font-bold">
								<div className="text-2xl text-red-500">Result</div>
								<div className="text-2xl max-w-[80%]">{test.prediction}</div>
							</div>
							<div className="text-lg">{new Date(test.date).toDateString()}</div>
							<div className="text-lg my-3 italic text-red-700">( click to analyze examination )</div>
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