import React, { useRef, useEffect, useState } from 'react';
import * as d3 from 'd3';

interface DataPoint {
  date: string;
  value: string;
}

export default function Chart({
  width = 800,
  height = 400,
  margin_top = 20,
  margin_right = 20,
  margin_bottom = 30,
  margin_left = 40
}) {
  const gx = useRef<SVGGElement>(null);
  const gy = useRef<SVGGElement>(null);
  const [data, setData] = useState<DataPoint[]>([]);

  // 2. Modify useEffect to use sample data first and debug CSV loading
  useEffect(() => {
    // Debug CSV loading
    d3.csv("./dataset/test.csv", d3.autoType)
      .then((prased_data: d3.DSVParsedArray<object>) => {
        console.log("Raw CSV data:", prased_data); // Check what's being loaded
        
        if (!prased_data.length) {
          console.error("CSV data is empty");
          return;
        }
  
        const formatted_data: DataPoint[] = prased_data.map(d => {
          console.log("Processing row:", d); // Check each row
          return {
            date: d.date,
            value: d.value
          };
        });
        
        console.log("Formatted data:", formatted_data);
        // Comment this for now to keep sample data
        setData(formatted_data);
      })
      .catch(error => {
        console.error("Error loading CSV:", error);
      });
  }, []);

  const x = d3.scaleLinear()
    .domain([0, data.length - 1])
    .range([margin_left, width - margin_right]);

  const y = d3.scaleLinear()
    .domain([0, d3.max(data, d => +d.value) || 0])
    .range([height - margin_bottom, margin_top]);

  const line = d3.line<DataPoint>()
    .x((d, i) => x(i))
    .y(d => y(+d.value));

  useEffect(() => {
    if (gx.current) {
      d3.select(gx.current).call(d3.axisBottom(x)).attr("color", "black");
    }
  }, [gx, x]);

  useEffect(() => {
    if (gy.current) {
      d3.select(gy.current).call(d3.axisLeft(y)).attr("color", "black");
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
      <g ref={gx} transform={`translate(0,${height - margin_bottom})`} />
      <g ref={gy} transform={`translate(${margin_left},0)`} />
    </svg>
  );
}