import React, { useRef, useEffect, useState } from 'react';
import * as d3 from 'd3';
import { HeatMapData } from '../types/types';

interface HeatMapProps {
    width?: string; // Width in rem
    height?: string; // Height in rem
    margin_top?: number;
    margin_right?: number; 
    margin_bottom?: number; 
    margin_left?: number; 
}

export default function HeatMap({
  width = '50rem', 
  height = '25rem', 
  margin_top = 20,
  margin_right = 20,
  margin_bottom = 30,
  margin_left = 40
}: HeatMapProps) {
  const heatmap_ref = useRef<SVGSVGElement | null>(null);
  const [data, set_data] = useState<HeatMapData[]>([]);
  const [year, set_year] = useState<string>("2023");

  const handle_year_change = (e: React.ChangeEvent<HTMLSelectElement>) => {
    set_year(e.target.value);
  };

  useEffect(() => {
    const fetch_data = async () => {
      try {
        const file_path = `/dataset/csv${year}.csv`;
        const parsed_data = await d3.csv(file_path, (d) => ({
          entity: d['Entity'],
          week: +d['Week'],
          day: d['Day'],
          flights: +d['Flights'],
          day_2019: d['Day 2019'],
          flights_2019_reference: +d['Flights 2019 (Reference)'],
          percent_vs_2019_daily: +d['% vs 2019 (Daily)'],
          day_previous_year: d['Day Previous Year'],
          flights_previous_year: +d['Flights Previous Year']
        })) as HeatMapData[];
        console.log(`Loaded CSV for year ${year}:`, parsed_data);
        set_data(parsed_data);
      } catch (error) {
        console.error(`Error loading CSV for year ${year}:`, error);
      }
    };

    fetch_data();
  }, [year]);

  useEffect(() => {
    if (!heatmap_ref.current || data.length === 0) return;

    // Clear previous heatmap
    d3.select(heatmap_ref.current).selectAll('*').remove();

    const svg = d3.select(heatmap_ref.current);
    
    // Convert rem to pixels for SVG dimensions (assuming 1rem = 16px)
    const font_size = 16;
    const svg_width = parseFloat(width) * font_size;
    const svg_height = parseFloat(height) * font_size;

    svg.attr('width', svg_width).attr('height', svg_height);

    const entities = Array.from(new Set(data.map(d => d.entity)));
    console.log('Entities:', entities);
    const weeks = Array.from(new Set(data.map(d => d.week)));

    const x = d3.scaleBand()
      .domain(weeks.map(String))
      .range([margin_left, svg_width - margin_right])
      .padding(0.05);

    const y = d3.scaleBand()
      .domain(entities)
      .range([margin_top, svg_height - margin_bottom])
      .padding(0.05);

    const color = d3.scaleSequential(d3.interpolateBlues)
      .domain([0, d3.max(data, d => d.flights) || 0]);

    // X Axis
    svg.append('g')
      .attr('transform', `translate(0,${svg_height - margin_bottom})`)
      .call(d3.axisBottom(x))
      .selectAll("text")
      .attr("transform", "rotate(-45)")
      .style("text-anchor", "end")
      .style("fill", "black"); // Set text color to black

    // Y Axis
    svg.append('g')
      .attr('transform', `translate(${margin_left},0)`)
      .call(d3.axisLeft(y))
      .selectAll("text")
      .style("fill", "black"); // Set text color to black

    // Axis lines
    svg.selectAll(".domain, .tick line")
      .style("stroke", "black"); // Set axis lines to black

    // Heatmap cells
    svg.selectAll()
      .data(data, (d: any) => `${d.entity}:${d.week}`)
      .enter()
      .append('rect')
      .attr('x', d => x(String(d.week))!)
      .attr('y', d => y(d.entity)!)
      .attr('width', x.bandwidth())
      .attr('height', y.bandwidth())
      .style('fill', d => color(d.flights))
      .style('stroke', 'white');

  }, [data, width, height, margin_top, margin_right, margin_bottom, margin_left]);

  return (
    <div>
      <div className="mb-4">
        <label htmlFor="year-selection" className="mr-2">Choose the year of dataset:</label>
        <select
          id="year-selection"
          value={year}
          onChange={handle_year_change}
          className="p-2 border border-gray-300 rounded text-black"
        >
          <option value="2020">2020</option>
          <option value="2021">2021</option>
          <option value="2022">2022</option>
          <option value="2023">2023</option>
        </select>
      </div>
      <svg ref={heatmap_ref}></svg>
    </div>
  );
}