import Image from "next/image";
import Link from "next/link";

export default function Home() {
  return (
    <div className="flex flex-col items-center justify-center h-[calc(100vh-100px)] text-center">
      <h1 className="text-white text-8xl font-bold italic mb-8 drop-shadow-xl">
        Fly better, Fly with us
      </h1>
      <Link href="/flight" passHref>
        <button className="text-white text-4xl font-bold py-3 px-6 rounded-3xl border-2 border-white hover:bg-red-700 hover:border-red-300 transition-all duration-300">
          Get Flight
        </button>
      </Link>
    </div>
  );
}
