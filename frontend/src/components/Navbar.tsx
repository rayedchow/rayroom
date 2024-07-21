import Link from "next/link";

export default function Navbar() {
	return (
		<div className="flex justify-center items-center w-full h-24 bg-slate-800 border-b-[1px] border-b-white">
      <div className="flex justify-between items-center w-[60%] h-full">
        <Link className="text-4xl text-red-400 font-semibold cursor-pointer" href="/">RayRoom</Link>
        <div className="flex justify-end text-red-200 text-2xl font-bold w-[50%] gap-32">
          <Link className="cursor-pointer" href="/">Patients</Link>
          <Link className="cursor-pointer" href="/researchcenter">Research Center</Link>
        </div>
      </div>
    </div>
	)
}