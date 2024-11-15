import React, { useEffect, useRef } from 'react';
import * as d3 from 'd3';

const FlightInYearChart: React.FC = () => {
  const chartRef = useRef<SVGSVGElement | null>(null);

  useEffect(() => {
    if (chartRef.current) {
      const data = [
        { year: 2015, flights: 200 },
        { year: 2016, flights: 300 },
        { year: 2017, flights: 400 },
        { year: 2018, flights: 500 },
        { year: 2019, flights: 600 },
      ];

      const svg = d3.select(chartRef.current);
      const margin = { top: 20, right: 30, bottom: 40, left: 40 };
      const width = 800 - margin.left - margin.right;
      const height = 400 - margin.top - margin.bottom;

      const x = d3.scaleBand()
        .domain(data.map(d => d.year.toString()))
        .range([0, width])
        .padding(0.1);

      const y = d3.scaleLinear()
        .domain([0, d3.max(data, d => d.flights)!])
        .nice()
        .range([height, 0]);

      const chart = svg.append('g')
        .attr('transform', `translate(${margin.left},${margin.top})`);

      chart.append('g')
        .attr('class', 'x-axis')
        .attr('transform', `translate(0,${height})`)
        .call(d3.axisBottom(x));

      chart.append('g')
        .attr('class', 'y-axis')
        .call(d3.axisLeft(y));

      chart.selectAll('.bar')
        .data(data)
        .enter().append('rect')
        .attr('class', 'bar')
        .attr('x', d => x(d.year.toString())!)
        .attr('y', d => y(d.flights))
        .attr('width', x.bandwidth())
        .attr('height', d => height - y(d.flights))
        .attr('fill', 'steelblue');
    }
  }, []);

  return (
    <svg ref={chartRef} width="800" height="400"></svg>
  );
};

export default FlightInYearChart;