import Link from "next/link";
import { useEffect, useState } from "react";
import { FaPlus } from "react-icons/fa6";

export default function Home() {

  const [patients, setPatients] = useState([]);

  useEffect(() => {
    const patientStorage = localStorage.getItem("patients");
    if(patientStorage) {
      setPatients(JSON.parse(patientStorage));
    }
  }, []);

  return (
    <main className="flex flex-col min-h-[90vh]">
      <div className="mt-12 flex flex-col items-center justify-between flex-grow">
        <div className="flex justify-center text-center text-5xl font-medium text-red-400">Patient Database</div>
        <div className="flex flex-col items-center gap-8 w-full">
          {patients.map((patient: any, i) => (
            <Link href={`/patients/${i}`} className="w-[60%] h-32 rounded-xl flex justify-center items-center border-2 border-slate-200 cursor-pointer text-3xl text-rose-500 font-medium text-center bg-slate-800">{patient['name']}</Link>
          ))}
          <Link className="w-[60%] h-32 rounded-xl flex justify-center items-center border-2 border-slate-200 cursor-pointer bg-slate-900" href="/patients/add"><FaPlus size={65} color={"#f43f5e"} /></Link>
        </div>
				<div className="flex w-full justify-center items-center text-center">
					<div className="py-5 px-10 flex justify-center items-center rounded-lg bg-rose-500 text-3xl font-semibold text-slate-800 ring-2 ring-slate-200 cursor-pointer" onClick={() => {localStorage.clear(); window.location.reload();}}>Clear Data</div>
				</div>
      </div>
    </main>
  )
}
