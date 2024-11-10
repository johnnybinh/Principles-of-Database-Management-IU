import React, { useRef, useEffect, useState } from 'react';
import * as d3 from 'd3';

interface DataImport {
    date: string;
    value: string;
}

export default function HeatMap({
    width = 800,
    height = 400,
    margin_top = 20,
    margin_right = 20,
    margin_bottom = 30,
    margin_left = 40
}) {
    const gx = useRef<SVGGElement>(null);
    const gy = useRef<SVGGElement>(null);
    const [data, setData] = useState<DataImport[]>([]);

    useEffect(() => {
        d3.csv("./dataset/test.csv", d3.autoType)
            .then((prased_data: d3.DSVParsedArray<d3.DSVRowString<any>>) => {
                console.log("Raw CSV data:", prased_data);

                if (!prased_data.length) {
                    console.error("CSV data is empty");
                    return;
                }

                const formatted_data: DataImport[] = prased_data.map(d => {
                    console.log("Processing row:", d);
                    return {
                        date: d.date,
                        value: d.value
                    };
                });

                console.log("Formatted data:", formatted_data);
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

    const line = d3.line<DataImport>()
        .x((d, i) => x(i))
        .y(d => y(+d.value));

    return (
        <svg width={width} height={height}>
            <g ref={gx} />
            <g ref={gy} />
            <path
                d={line(data) || ""}
                fill="none"
                stroke="black"
                strokeWidth="1.5"
            />
        </svg>
    );
};