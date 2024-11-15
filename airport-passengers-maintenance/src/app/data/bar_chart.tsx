import React, { useState, useEffect, useRef } from 'react';
import * as d3 from 'd3';

    interface DataPoint {
        Entity: string;
        Flights: number;
    }

    const BarChart: React.FC = () => {
        const [dataset, setDataset] = useState<DataPoint[]>([]);
        const [year, setYear] = useState("2020");
        const [sortOrder, setSortOrder] = useState("ascending");
        const [topN, setTopN] = useState(10);

        useEffect(()=>{
            loadData(year);
        }, [year]);

        useEffect(() => {
            renderChart();
        }, [dataset, sortOrder, topN]);

        const loadData = async (selectedYear: string) =>{
            try{
                const data = await d3.csv(`./dataset/csv${selectedYear}.csv`, d=> ({
                    Entity: d.Entity,
                    Flights: +d.Flights
                }));
            } catch (error){
                console.error("Error loading data: ",error);
            }
        };

        const handleYearChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
            setYear(e.target.value);
        };
        const handleSortOrderChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
            setSortOrder(e.target.value);
        };
        const handleTopNChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
            setTopN(parseInt(e.target.value,10));
        };

        const renderChart = () => {
            // Clear previous chart
            d3.select("#barChart").selectAll("*").remove();

            const sortedData = [...dataset]
                .sort((a,b) => (sortOrder === "ascending" ? a.Flights-b.Flights : b.Flights-a.Flights))
                .slice(0,topN);
            
            const margin = {top: 40, right: 20, bottom: 40, left: 60};
            const width = 1000 -margin.left-margin.right;
            const height = 500 -margin.top-margin.bottom;

            const svg = d3
                    .select("#barChart")
                    .append("svg")
                    .attr("width", width + margin.left + margin.right)
                    .attr("height", height+margin.top+margin.bottom)
                    .append("g")
                    .attr("transform", `translate(${margin.left},${margin.top})`);

            const x = d3
                    .scaleBand()
                    .domain(sortedData.map(d => d.Entity))
                    .range([0, width])
                    .padding(0.1);

            const y = d3
                    .scaleLinear()
                    .domain([0, d3.max(sortedData, d => d.Flights) || 0])
                    .nice()
                    .range([height, 0]);
            
            // Draw bars with transition
            svg 
                .append("g")
                .selectAll("rect")
                .data(sortedData)
                .join("rect")
                .attr("x", d=> x(d.Entity)!)
                .attr("y", height) // Start from the bottom for the transition
                .attr("width", x.bandwidth())
                .attr("height", 0) // Start width height 0 for transition
                .attr("fill", "#2171b5")
                .transition()
                .duration(800)
                .attr("y", d => y(d.Flights))
                .attr("height", d => height - y(d.Flights));

            // Add hover effect

            svg 
                .append("g")
                .attr("class", "x-axis")
                .attr("transform", `translate(0,${height})`)
                .call(d3.axisBottom(x))
                .selectAll("text")
                .attr("transform", "rotate(-45)")
                .style("text-anchor", "end");

            svg
                .append("g")
                .attr("class", "y-axis")
                .call(d3.axisLeft(y).ticks(10));

        };

    return(
        <div>
            <h2>{`Flights ${year} (Arr/Dep Flights)`}</h2>
        <div className='mb-5'>
            <label>
                Choose Dataset:
                <select value={year} onChange={handleYearChange}>
                    <option value="2020">2020</option>
                    <option value="2021">2021</option>
                    <option value="2022">2022</option>
                    <option value="2023">2023</option>
                </select>
            </label>
            <label className='ml-5'>
                Sort Order:
                <select value={sortOrder} onChange={handleSortOrderChange}>
                    <option value="Ascending">Ascending</option>
                    <option value="Descending">Descending</option>
                </select>
            </label>
            <label className='ml-5'>
                Show Top:
                <select value={topN} onChange={handleTopNChange}>
                    <option value={10}>Top 10</option>
                    <option value={20}>Top 20</option>
                </select>
            </label>
        </div>
        <div id="barChart" />
    </div>

    );
}
export default BarChart;



    