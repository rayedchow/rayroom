import { useState } from "react"
import questionJson from '@/data/questions.json'
import Select from 'react-select'
import { useRouter } from "next/router";

const questionData: any = questionJson;

export default function addPatient() {

	const [userInputs, setUserInputs] = useState<any>({});
	const router = useRouter();

	const handleChange = (i: number, j: any) => {
		const tempUserInputs: any = {...userInputs};
		if(j.type == 'number') {
			tempUserInputs[questionData.general[i].name] = parseInt(j.value);
		} else {
			tempUserInputs[questionData.general[i].name] = j.value;
		}
		setUserInputs(tempUserInputs);
	}

	const addPatient = () => {
		if(Object.keys(userInputs).length != questionData.general.length) return;
		const patientStorage = localStorage.getItem("patients");
		if(patientStorage) {
			const patientData = JSON.parse(patientStorage);
			patientData.push(userInputs);
			localStorage.setItem("patients", JSON.stringify(patientData));
		} else {
			localStorage.setItem("patients", JSON.stringify([userInputs]));
		}
		router.push("/");
	}

	return (
		<main className="flex flex-col min-h-[90vh]">
			<div className="mt-12 flex flex-col items-center justify-between flex-grow">
			<div className="text-center text-4xl font-medium text-red-500">Add New Patient</div>
			<div className="mt-5 flex flex-col h-full w-full justify-center items-center">
				<div className="mt-8 w-[40%] min-h-72 bg-slate-800 rounded-lg ring-2 ring-slate-200">
					<div className="my-12 mx-5 flex flex-col gap-8">
						{questionData.general.map((q: any, i: any) => (
							<div className="flex flex-col items-center gap-1">
								<div className="text-xl font-medium text-center text-rose-400">{q.question}</div>
								{((q.type == "dropdown")) ? 
									<div className='self-center relative' style={{zIndex: (14-i)*50}}>
										<Select
											className='self-center drop-shadow-lg mt-2 w-72 bg-slate-300 rounded-md'
											options={q.options}
											onChange={(s) => handleChange(i, s)}
											theme={(theme) => ({
												...theme,
												colors: {
													...theme.colors,
													neutral0: "#cbd5e1",
													neutral50: "#111827"
												}
											})}
											isSearchable
										/>
									</div>
								:
									<>
										{(q.type == 'input') ? 
											<input type="text" onChange={(e) => handleChange(i, e.target)} className='self-center text-center w-48 p-2 rounded-md drop-shadow-lg mt-2 outline-none bg-slate-300' />
											: <input type="number" onChange={(e) => handleChange(i, e.target)} className='self-center text-center w-48 p-2 rounded-md drop-shadow-lg mt-2 outline-none bg-slate-300' />
										}
									</>
								}
							</div>
						))}
					</div>
				</div>
			</div>
			<div className="py-5 px-10 flex justify-center items-center rounded-lg bg-rose-500 text-3xl font-semibold text-slate-800 ring-2 ring-slate-200 cursor-pointer" onClick={addPatient}>Add Patient</div>
			</div>
		</main>
	)
}