"use client";
import React, { useRef, useEffect, useState } from 'react';
import * as d3 from 'd3';

interface DataPoint {
  date: string;
  value: string;
}

export default function Chart({
  width = 800,
  height = 400,
  marginTop = 20,
  marginRight = 20,
  marginBottom = 30,
  marginLeft = 40
}) {
  const gx = useRef<SVGGElement>(null);
  const gy = useRef<SVGGElement>(null);
  const [data, setData] = useState<DataPoint[]>([]);

  // 1. Add sample data at the top of component
const sampleData: DataPoint[] = [
    { date: "2023-01", value: "10" },
    { date: "2023-02", value: "20" },
    { date: "2023-03", value: "15" },
    { date: "2023-04", value: "25" },
    { date: "2023-05", value: "30" }
  ];
  
  // 2. Modify useEffect to use sample data first and debug CSV loading
  useEffect(() => {
    // Start with sample data to verify chart works
    setData(sampleData);
  
    // Debug CSV loading
    d3.csv("../public/dataset/mock.csv", d3.autoType)
      .then((parsedData: d3.DSVParsedArray<d3.DSVRowString<string>>) => {
        console.log("Raw CSV data:", parsedData); // Check what's being loaded
        
        if (!parsedData.length) {
          console.error("CSV data is empty");
          return;
        }
  
        const formattedData: DataPoint[] = parsedData.map(d => {
          console.log("Processing row:", d); // Check each row
          return {
            date: d.date,
            value: d.value
          };
        });
        
        console.log("Formatted data:", formattedData);
        // Comment this for now to keep sample data
        // setData(formattedData);
      })
      .catch(error => {
        console.error("Error loading CSV:", error);
      });
  }, []);

  const x = d3.scaleLinear()
    .domain([0, data.length - 1])
    .range([marginLeft, width - marginRight]);

  const y = d3.scaleLinear()
    .domain([0, d3.max(data, d => +d.value) || 0])
    .range([height - marginBottom, marginTop]);

  const line = d3.line<DataPoint>()
    .x((d, i) => x(i))
    .y(d => y(+d.value));

  useEffect(() => {
    if (gx.current) {
      d3.select(gx.current).call(d3.axisBottom(x));
    }
  }, [gx, x]);

  useEffect(() => {
    if (gy.current) {
      d3.select(gy.current).call(d3.axisLeft(y));
    }
  }, [gy, y]);

  return (
    <svg width={width} height={height}>
      <g>
        <path
          fill="none"
          stroke="steelblue"
          strokeWidth="1.5"
          d={line(data) || ""}
        />
      </g>
      <g ref={gx} transform={`translate(0,${height - marginBottom})`} />
      <g ref={gy} transform={`translate(${marginLeft},0)`} />
    </svg>
  );
}