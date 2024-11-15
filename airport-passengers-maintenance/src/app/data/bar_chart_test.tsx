import React, { useEffect, useState } from "react";
import * as d3 from "d3";

interface DataEntry {
  name: string;
  value: number;
  rank: number;
}

const BarChart: React.FC = () => {
  const [dataset, setDataset] = useState<DataEntry[]>([]);
  const [year, setYear] = useState("2020");
  const [sortOrder, setSortOrder] = useState("ascending");
  const [topN, setTopN] = useState(10);

  useEffect(() => {
    loadData(year);
  }, [year]);

  useEffect(() => {
    renderChart();
  }, [dataset, sortOrder, topN]);

  const loadData = async (selectedYear: string) => {
    try {
      const data = await d3.csv(`csv${selectedYear}.csv`, d => ({
        name: d.name,
        value: +d.value,
        rank: +d.rank
      }));
      setDataset(data as DataEntry[]);
    } catch (error) {
      console.error("Error loading data:", error);
    }
  };

  const handleYearChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setYear(e.target.value);
  };

  const handleSortOrderChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSortOrder(e.target.value);
  };

  const handleTopNChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setTopN(parseInt(e.target.value, 10));
  };

  const renderChart = () => {
    // Clear previous chart
    d3.select("#barChart").selectAll("*").remove();

    const sortedData = [...dataset]
      .sort((a, b) => (sortOrder === "ascending" ? a.value - b.value : b.value - a.value))
      .slice(0, topN);

    const margin = { top: 20, right: 30, bottom: 40, left: 90 };
    const width = 700 - margin.left - margin.right;
    const height = 500 - margin.top - margin.bottom;

    const svg = d3
      .select("#barChart")
      .append("svg")
      .attr("width", width + margin.left + margin.right)
      .attr("height", height + margin.top + margin.bottom)
      .append("g")
      .attr("transform", `translate(${margin.left},${margin.top})`);

    const x = d3
      .scaleLinear()
      .domain([0, d3.max(sortedData, d => d.value) || 0])
      .range([0, width]);

    const y = d3
      .scaleBand()
      .domain(sortedData.map(d => d.name))
      .range([0, height])
      .padding(0.1);

    svg
      .append("g")
      .selectAll("rect")
      .data(sortedData)
      .join("rect")
      .attr("x", 0)
      .attr("y", d => y(d.name)!)
      .attr("width", d => x(d.value))
      .attr("height", y.bandwidth())
      .attr("fill", (d, i) => d3.interpolateBlues(i / sortedData.length))
      .on("mouseover", function () {
        d3.select(this).attr("fill", "#6baed6");
      })
      .on("mouseout", function (event, d) {
        d3.select(this).attr("fill", (d, i) => d3.interpolateBlues(i / sortedData.length));
      });

    svg
      .append("g")
      .attr("class", "x-axis")
      .attr("transform", `translate(0,${height})`)
      .call(d3.axisBottom(x).ticks(5));

    svg
      .append("g")
      .attr("class", "y-axis")
      .call(d3.axisLeft(y));

    svg
      .append("text")
      .attr("text-anchor", "end")
      .attr("x", width)
      .attr("y", height + margin.bottom - 5)
      .text("Value");

    svg
      .append("text")
      .attr("text-anchor", "end")
      .attr("transform", "rotate(-90)")
      .attr("y", -margin.left + 20)
      .attr("x", -margin.top)
      .text("Name");
  };

  return (
    <div>
      <h2>{`Top ${topN} Data of ${year}`}</h2>
      <div>
        <label>
          Choose Dataset:
          <select value={year} onChange={handleYearChange}>
            <option value="2020">2020</option>
            <option value="2021">2021</option>
            <option value="2022">2022</option>
            <option value="2023">2023</option>
          </select>
        </label>

        <label>
          Sort Order:
          <select value={sortOrder} onChange={handleSortOrderChange}>
            <option value="ascending">Ascending</option>
            <option value="descending">Descending</option>
          </select>
        </label>

        <label>
          Show Top:
          <select value={topN} onChange={handleTopNChange}>
            <option value="10">Top 10</option>
            <option value="20">Top 20</option>
          </select>
        </label>
      </div>

      <div id="barChart" />
    </div>
  );
};

export default BarChart;
