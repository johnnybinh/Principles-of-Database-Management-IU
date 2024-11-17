"use client";
import Chart from "./chart";
import HeatMap from "./heat_map";

export default function Data() {
  return (
    <div className="flex flex-col items-center p-20 mt-8"> {/*<!-- Add padding: p-20 and margin top: mt-8 -->*/}  
      <h1 className="text-3xl font-bold mb-8">European Aviation Traffic</h1>
      <div className="w-full max-w-6xl bg-neutral-50 rounded-lg shadow-lg p-6 flex justify-center items-center">
        <HeatMap/>
      </div>
      {/* <div className="flex justify-center h-screen">
        <HeatMap/>
      </div> */}
    </div>
  );
}
