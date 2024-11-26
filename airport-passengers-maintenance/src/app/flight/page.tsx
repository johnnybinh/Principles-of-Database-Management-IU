"use client";

import { Button } from "@/components/ui/button"

import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
  } from "@/components/ui/table"

import { useEffect, useState } from "react";

export default function Flight() {

    // State variables for input values
    const [fromLocation, setFromLocation] = useState("");
    const [toLocation, setToLocation] = useState("");
    const [departureDate, setDepartureDate] = useState("");
    const [arrivalDate, setArrivalDate] = useState("");
    const [extractedData, setExtractedData] = useState<{ from: string; to: string; departure: string; arrival: string } | null>(null);

    const table_header = ["Time Start", "Departure", "Port", "Duration", "Time End", "Destination", "Port"];

    // Handle form submission
    const handleSearch = () => {
        const data = {
            from: fromLocation,
            to: toLocation,
            departure: departureDate,
            arrival: arrivalDate
        };

        setExtractedData(data);
        console.log("Extracted Data:", data);
        // Add your API call or data processing logic here
    };

    return (
        <div className="flex justify-center h-screen">
            <div className="w-full max-w-6xl p-6 bg-opacity-80" style={{ marginTop: '100px' }}>
                <div className="mb-6">
                    <div className="flex space-x-4 mb-4">
                        <div className="flex flex-col w-1/2">
                            <label htmlFor="from-departure" className="text-white text-2xl font-bold italic mb-2">
                                From:
                            </label>
                            <input
                                id="from-departure"
                                type="text"
                                value={fromLocation}
                                onChange={(e) => setFromLocation(e.target.value)}
                                className="w-full px-4 py-2 text-black rounded-lg focus:outline-none bg-white/50 border-2 border-black focus:ring-2 focus:ring-blue-500"
                                placeholder="Cam Ranh (CXR)"
                            />
                        </div>
                        <div className="flex flex-col w-1/2">
                            <label htmlFor="to-arrival" className="text-white text-2xl font-bold italic mb-2">
                                To:
                            </label>
                            <input
                                id="to-arrival"
                                type="text"
                                value={toLocation}
                                onChange={(e) => setToLocation(e.target.value)}
                                className="w-full px-4 py-2 text-black rounded-lg focus:outline-none bg-white/50 border-2 border-black focus:ring-2 focus:ring-blue-500"
                                placeholder="Dubai (DXB)"
                            />
                        </div>
                        <div className="flex flex-col w-1/2">
                            <label htmlFor="date-departure" className="text-white text-2xl font-bold italic mb-2">
                                Date of Departure:
                            </label>
                            <input
                                id="date-departure"
                                type="date"
                                value={departureDate}
                                onChange={(e) => setDepartureDate(e.target.value)}
                                className="w-full px-4 py-2 text-black rounded-lg focus:outline-none bg-white/50 border-2 border-black focus:ring-2 focus:ring-blue-500"
                                placeholder="10/10/2024"
                            />
                        </div>
                        <div className="flex flex-col w-1/2">
                            <label htmlFor="date-arrival" className="text-white text-2xl font-bold italic mb-2">
                                Date of Arrival:
                            </label>
                            <input
                                id="date-arrival"
                                type="date"
                                value={arrivalDate}
                                onChange={(e) => setArrivalDate(e.target.value)}
                                className="w-full px-4 py-2 text-black rounded-lg focus:outline-none bg-white/50 border-2 border-black focus:ring-2 focus:ring-blue-500"
                                placeholder="10/10/2024"
                            />
                        </div>
                    </div>
                    <div className="flex justify-end">
                        <Button 
                            onClick={handleSearch}
                            className="w-32 bg-black text-white font-bold text-xl py-3 px-8 rounded-lg shadow-md hover:bg-gray-800 focus:outline-none transform hover:scale-105 transition-transform duration-200">
                            
                            View
                        </Button>
                    </div>
                </div>
                <Table>
                    <TableHeader>
                        <TableRow>
                            {table_header.map((header, index) => (
                                <TableHead key={index} className="px-6 py-3 text-center text-lg font-bold italic text-black uppercase tracking-wider bg-white/50">
                                    {header}
                                </TableHead>
                            ))}
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        <TableRow className="bg-white/50 text-center text-base text-black uppercase tracking-wider hover:bg-white/70 transition-colors duration-200">
                            <TableCell className="px-6 py-4">6:00 10/10/2024</TableCell>
                            <TableCell className="px-6 py-4">Cam Ranh (CXR)</TableCell>
                            <TableCell className="px-6 py-4">Port 3</TableCell>
                            <TableCell className="px-6 py-4">6:00</TableCell>
                            <TableCell className="px-6 py-4">12:00 10/10/2024</TableCell>
                            <TableCell className="px-6 py-4">Dubai (DXB)</TableCell>
                            <TableCell className="px-6 py-4">Port 4</TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </div>
        </div>
    );
}