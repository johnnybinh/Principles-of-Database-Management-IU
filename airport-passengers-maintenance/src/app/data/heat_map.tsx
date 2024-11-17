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
  const [season, set_season] = useState<string>("Spring");
  const [country, set_country] = useState<string>("All");

  const handle_year_change = (e: React.ChangeEvent<HTMLSelectElement>) => {
    set_year(e.target.value);
  };

  const handle_season_change = (e: React.ChangeEvent<HTMLSelectElement>) => {
    set_season(e.target.value);
  };

  const handle_country_change = (e: React.ChangeEvent<HTMLSelectElement>) => {
    set_country(e.target.value);
  };

  // Define season to week range mapping
  const season_week_map: { [key: string]: [number, number] } = {
    'Spring': [1, 13],
    'Summer': [14, 26],
    'Fall': [27, 39],
    'Winter': [40, 52]
  };

  // Define country group mapping with duplicates removed
  const country_map: { [key: string]: string[] } = {
    'A_B': Array.from(new Set(['Austria', 'Armenia', 'Belgium', 'Bosnia-Herzegovina', 'Bulgaria'])),
    'C_F': Array.from(new Set(['Croatia', 'Cyprus', 'Czech Republic', 'Denmark', 'Estonia', 'Finland', 'France'])),
    'G_I': Array.from(new Set(['Georgia', 'Germany', 'Greece', 'Hungary', 'Ireland', 'Israel', 'Italy'])),
    'L_M': Array.from(new Set(['Latvia', 'Lithuania', 'Luxembourg', 'Malta', 'Moldova', 'Morocco'])),
    'N_R': Array.from(new Set(['Netherlands', 'North Macedonia', 'Norway', 'Poland', 'Portugal', 'Romania'])),
    'S': Array.from(new Set(['Serbia & Montenegro', 'Slovakia', 'Slovenia', 'Spain', 'Sweden', 'Switzerland'])),
    'T_Z': Array.from(new Set(['Turkey', 'Ukraine', 'United Kingdom', '-Total Network Manager Area']))
  };

  // Initialize tooltip once when component mounts
  const tooltipRef = useRef<HTMLElement | null>(null);

  useEffect(() => {
    // Create the tooltip div and append to body
    const tooltip = d3.select('body').append('div')
      .attr('class', 'tooltip')
      .style('position', 'absolute')
      .style('padding', '0.5rem')
      .style('background', 'rgba(0, 0, 0, 0.7)')
      .style('color', 'white')
      .style('border-radius', '4px')
      .style('pointer-events', 'none')
      .style('opacity', 0)
      .style('font-size', '0.875rem')
      .style('z-index', '10'); // Ensure tooltip is above other elements

    tooltipRef.current = tooltip.node();

    // Cleanup tooltip on component unmount
    return () => {
      tooltip.remove();
    };
  }, []);

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
          flights_2019_reference: +d['Flights 2019 (Reference)'], // Ensure this matches your CSV exactly
          percent_vs_2019_daily: +d['% vs 2019 (Daily)'],
          day_previous_year: d['Day Previous Year'],
          flights_previous_year: +d['Flights Previous Year']
        })) as HeatMapData[];

        // Aggregate data: sum flights per entity per week
        const aggregated_data = Array.from(
          d3.rollup(
            parsed_data,
            v => ({
              flights: d3.sum(v, d => d.flights),
              flights_2019_reference: d3.sum(v, d => d.flights_2019_reference)
            }),
            d => d.entity,
            d => d.week
          ),
          ([entity, weeksMap]) => {
            return Array.from(weeksMap, ([week, sums]) => ({
              entity,
              week,
              flights: sums.flights,
              flights_2019_reference: sums.flights_2019_reference,
              day: '',
              day_2019: '',
              percent_vs_2019_daily: 0,
              day_previous_year: '',
              flights_previous_year: 0
            }));
          }
        ).flat();

        console.log(`Loaded and aggregated CSV for year ${year}:`, aggregated_data);
        set_data(aggregated_data);
      } catch (error) {
        console.error(`Error loading CSV for year ${year}:`, error);
      }
    };

    fetch_data();
  }, [year]);

  useEffect(() => {
    if (!heatmap_ref.current || data.length === 0) return;

    const [start_week, end_week] = season_week_map[season] || [1, 52];

    // Filter data based on selected season's week range
    let filtered_data = data.filter(d => d.week >= start_week && d.week <= end_week);

    // Further filter data based on selected country group
    if (country !== 'All') {
      const selected_countries = country_map[country];
      filtered_data = filtered_data.filter(d => selected_countries.includes(d.entity));
    }

    if (filtered_data.length === 0) {
      console.warn(`No data available for ${season} in year ${year} and selected country group.`);
      d3.select(heatmap_ref.current).selectAll('*').remove();
      return;
    }

    // Clear previous heatmap
    d3.select(heatmap_ref.current).selectAll('*').remove();

    const svg = d3.select(heatmap_ref.current);
    
    // Convert rem to pixels for SVG dimensions (assuming 1rem = 16px)
    const font_size = 16;
    const svg_width = parseFloat(width) * font_size;
    const svg_height = parseFloat(height) * font_size;

    svg.attr('width', svg_width).attr('height', svg_height);

    const entities = Array.from(new Set(filtered_data.map(d => d.entity)));
    // console.log('Entities:', entities);
    const weeks = Array.from(new Set(filtered_data.map(d => d.week))).sort((a, b) => a - b);

    const x = d3.scaleBand()
      .domain(weeks.map(String))
      .range([margin_left, svg_width - margin_right])
      .padding(0.05);

    const y = d3.scaleBand()
      .domain(entities)
      .range([margin_top, svg_height - margin_bottom])
      .padding(0.05);

    // Define diverging color scale based on the ratio between flights and flights_2019_reference
    const ratios = filtered_data.map(d => d.flights / d.flights_2019_reference);
    const max_ratio = d3.max(ratios) || 1;
    const min_ratio = d3.min(ratios) || 1;

    const color = d3.scaleDiverging<string>()
      .interpolator(d3.interpolateRdBu)
      .domain([min_ratio, 1, max_ratio]);

    // Access the tooltip
    const tooltip = d3.select(tooltipRef.current);

    // Define mouse event handlers
    const handle_mouseover = (event: MouseEvent, d: HeatMapData) => {
      
      tooltip.transition().duration(200).style('opacity', 1);
      tooltip.html(`
        <div><strong>${d.entity}</strong></div>
        <div>Week: ${d.week}</div>
        <div>Flights: ${d.flights}</div>
        <div>Flights 2019: ${d.flights_2019_reference}</div>
        <div>Ratio: ${(d.flights / d.flights_2019_reference).toFixed(3)}</div>
      `)
        .style('left', `${event.pageX + 10}px`)
        .style('top', `${event.pageY + 10}px`);
      console.log('Mouseover:', d);
    };

    const handle_mousemove = (event: MouseEvent) => {
      tooltip
        .style('left', `${event.pageX + 10}px`)
        .style('top', `${event.pageY + 10}px`);
    };

    const handle_mouseleave = () => {
      tooltip.transition().duration(200).style('opacity', 0);
    };  

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
      .data(filtered_data, (d: any) => `${d.entity}:${d.week}`)
      .enter()
      .append('rect')
      .attr('x', d => x(String(d.week))!)
      .attr('y', d => y(d.entity)!)
      .attr('width', x.bandwidth())
      .attr('height', y.bandwidth())
      .style('fill', d => color(d.flights / d.flights_2019_reference))
      .style('stroke', 'white')
      // Tooltip event handlers
      .on('mouseover', handle_mouseover)
      .on('mousemove', handle_mousemove)
      .on('mouseleave', handle_mouseleave);

  }, [data, season, country, width, height, margin_top, margin_right, margin_bottom, margin_left, year]);

  return (
    <div>
      <div className="mb-4 flex items-center">
        <label htmlFor="year-selection" className="mr-2 text-black">Year of data:</label>
        <select
          id="year-selection"
          value={year}
          onChange={handle_year_change}
          className="p-2 border border-gray-300 rounded text-black mr-4"
        >
          <option value="2020">2020</option>
          <option value="2021">2021</option>
          <option value="2022">2022</option>
          <option value="2023">2023</option>
        </select>

        <label htmlFor="season-selection" className="mr-2 text-black">Season:</label>
        <select
          id="season-selection"
          value={season}
          onChange={handle_season_change}
          className="p-2 border border-gray-300 rounded text-black mr-4"
        >
          <option value="Spring">Spring (Weeks 1-13)</option>
          <option value="Summer">Summer (Weeks 14-26)</option>
          <option value="Fall">Fall (Weeks 27-39)</option>
          <option value="Winter">Winter (Weeks 40-52)</option>
        </select>

        <label htmlFor="country-selection" className="mr-2 text-black">Country group:</label>
        <select
          id="country-selection"
          value={country}
          onChange={handle_country_change}
          className="p-2 border border-gray-300 rounded text-black"
        >
          <option value="All">All</option>
          {Object.keys(country_map).map(group => (
            <option key={group} value={group}>
              {group} ({country_map[group].length} countries)
            </option>
          ))}
        </select>
      </div>

      {(() => {
        const [start_week, end_week] = season_week_map[season] || [1, 52];

        let filtered_data = data.filter(d => d.week >= start_week && d.week <= end_week);

        if (country !== 'All') {
          const selected_countries = country_map[country];
          filtered_data = filtered_data.filter(d => selected_countries.includes(d.entity));
        }

        if (filtered_data.length === 0) {
          return (
            <p className="text-red-500">No data available for the selected season and country group.</p>
          );
        }
        return <svg ref={heatmap_ref} width="100%" height="auto"></svg>;
      })()}
    </div>
  );
}