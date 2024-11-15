import React, { useState, useEffect} from 'react';
import * as d3 from 'd3';

    interface DataPoint {
        Entity: string;
        Flights: number;
    }

    const BarChart: React.FC = () => {
        const [dataset, setDataset] = useState<DataPoint[]>([]);
        const [year, setYear] = useState("2020");
        const [sortOrder, setSortOrder] = useState("Ascending");
        const [topN, setTopN] = useState(10);

        useEffect(()=>{
            loadData(year);
        }, [year]);

        useEffect(() => {
            renderChart();
        }, [dataset, sortOrder, topN]);

        const loadData = async (selectedYear: string) => {
            try {
                const rawData = await d3.csv(`/dataset/csv${selectedYear}.csv`, (d: any) => ({
                    Entity: d.Entity as string,
                    Flights: +d.Flights,
                }));

                const filteredData = rawData.filter((d) => d.Entity && d.Entity !== "-Total Network Manager Area" && !isNaN(d.Flights));

                const aggreatedData = d3.rollups(
                    filteredData,
                    (group) => d3.sum(group, d => d.Flights), //Sum Flights of each entity
                    (d) => d.Entity
                ).map(([Entity, Flights]) => ({Entity, Flights})); // Convert back to array of objects

                setDataset(aggreatedData);
                
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
            setTopN(parseInt(e.target.value,10));
        };

        const renderChart = () => {
            // Clear previous chart
            d3.select("#barChart").selectAll("*").remove();
       
            const sortedData = [...dataset]
            .sort((a, b) =>
                sortOrder === "Descending" ? d3.descending(a.Flights, b.Flights) : d3.ascending(a.Flights, b.Flights)
            )
            .slice(0, topN);

            console.log("Sorted and sliced data for rendering:", sortedData);

            const margin = {top: 40, right: 20, bottom: 100, left: 60};
            const width = 1500 -margin.left-margin.right;
            const height = 600 -margin.top-margin.bottom;

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

            // Add Chart title
            svg.append("text")
            .attr("x", width / 2)
            .attr("y", margin.top/2 - 30)
            .attr("text-anchor", "middle")
            .attr("font-size", "16px")
            .attr("font-weight", "bold")
            .text(`Flights ${year} (Arr/Dep flights)`);

        };

    return(
        <div>
        <div className='mb-5'>
            <label>
                Choose Dataset:
                <select value={year} onChange={handleYearChange} className='text-black'>
                    <option value="2020">2020</option>
                    <option value="2021">2021</option>
                    <option value="2022">2022</option>
                    <option value="2023">2023</option>
                </select>
            </label>
            <label className='ml-5'>
                Sort Order:
                <select value={sortOrder} onChange={handleSortOrderChange} className='text-black'>
                    <option value="Ascending">Ascending</option>
                    <option value="Descending">Descending</option>
                </select>
            </label>
            <label className='ml-5'>
                Show Top:
                <select value={topN} onChange={handleTopNChange} className='text-black'>
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



    