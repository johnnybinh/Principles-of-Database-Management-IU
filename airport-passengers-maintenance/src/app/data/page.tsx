"use client";
import Chart from "./chart";
import HeatMap from "./heat_map";

export default function Data() {
  return (
    <div>
      <div>
        <Chart/>
      </div>
      <div className="">
        <HeatMap/>
      </div>
    </div>
  );
}
