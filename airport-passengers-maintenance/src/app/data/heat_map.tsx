import React, { useRef, useEffect, useState } from 'react';
import * as d3 from 'd3';

interface DataImport {
    entity: string;
    week: number;
    day: string;
    flights: number;
    day_2019: string;
    flights_2019: number;
    vs_2019: number;
    day_previous_year: string;
    flights_previous_year: number;
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

    // useEffect(() => {
    //     d3.csv("./dataset/test.csv", d3.autoType)
    //         .then((prased_data: d3.DSVParsedArray<d3.DSVRowString<any>>) => {
    //             setData(prased_data);
    //         }
    // }, []);

    return (
        <svg width={width} height={height}>


        </svg>
    );
};