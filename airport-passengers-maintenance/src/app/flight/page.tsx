"use client";

import { FlightSchedule } from '../types/types';

import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
  } from "@/components/ui/table"
import { Button } from "@/components/ui/button";

import { useEffect, useState } from "react";
//import axios from 'axios';

export default function Flight() {
    const table_header = ["Departure Date", "Arrival Date", "Departure", "Arrival", "Duration", "Status"];

    const [flightSchedule, setFlightSchedule] = useState<FlightSchedule[]>([]);
    const [error, setError] = useState<string | null>(null);

    const [departureDate, setDepartureDate] = useState<string>('');
    const [arrivalDate, setArrivalDate] = useState<string>('');
    const [departure, setDeparture] = useState<string>('');
    const [arrival, setArrival] = useState<string>('');
  
    const handleClick = () => {
        const fetchData = async () => {
            try {
                const params = new URLSearchParams({
                    ...(departure && { departure }),
                    ...(arrival && { arrival }),
                    ...(departureDate && { departureDate }),
                    ...(arrivalDate && { arrivalDate })
                });

                const res = await fetch(`http://localhost:8080/api/flight_schedules?${params}`, {
                    headers: {
                        'Accept': 'application/json',
                    },
                });

                if (!res.ok) {
                    throw new Error(`Network response was not ok: ${res.status} ${res.statusText}`);
                }

                const data: FlightSchedule[] = await res.json();
                setFlightSchedule(data);
            } catch (err: any) {
                console.error('Fetch error:', err);
                setError(err.message);
            }
        };
        
        fetchData();
    }

    const formatDate = (dateString: string) => {
        const date = new Date(dateString);
        return date.toLocaleString('en-GB', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const getStatusColor = (status: string) => {
        switch (status.toLowerCase()) {
          case 'on time':
            return 'text-green-500';
          case 'cancelled':
            return 'text-red-600';
          case 'delayed':
            return 'text-amber-400';
          default:
            return 'text-black';
        }
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
                                onChange={(e) => setDeparture(e.target.value)}
                                value={departure}
                                className="w-full px-4 py-2 text-black rounded-lg focus:outline-none bg-white/50 border-2 border-black focus:ring-2 focus:ring-blue-500 placeholder:text-black"
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
                                onChange={(e) => setArrival(e.target.value)}
                                value={arrival}
                                className="w-full px-4 py-2 text-black rounded-lg focus:outline-none bg-white/50 border-2 border-black focus:ring-2 focus:ring-blue-500 placeholder:text-black"
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
                                onChange={(e) => setDepartureDate(e.target.value)}
                                value={departureDate}
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
                                onChange={(e) => setArrivalDate(e.target.value)}
                                value={arrivalDate}
                                className="w-full px-4 py-2 text-black rounded-lg focus:outline-none bg-white/50 border-2 border-black focus:ring-2 focus:ring-blue-500"
                                placeholder="10/10/2024"
                            />
                        </div>
                    </div>
                    <div className="flex justify-end">
                        <Button 
                            onClick={handleClick}
                            className="w-32 bg-black text-white font-bold text-xl py-3 px-8 rounded-lg shadow-md hover:bg-gray-800 focus:outline-none transform hover:scale-105 transition-transform duration-200">            
                            View
                        </Button>
                    </div>
                </div>
                <Table>
                    <TableHeader>
                        <TableRow>
                            {table_header.map((header, index) => (
                            <TableHead
                                key={index}
                                className="px-6 py-3 text-center text-lg font-bold italic text-black uppercase tracking-wider bg-white/50"
                            >
                                {header}
                            </TableHead>
                            ))}
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {flightSchedule.map((item, index) => (
                            <TableRow key={index} className="bg-white/50 text-center text-base text-black uppercase tracking-wider hover:bg-white/70 transition-colors duration-200">
                                <TableCell className="px-6 py-4 text-center">{formatDate(item.departureDate)}</TableCell>
                                <TableCell className="px-6 py-4 text-center">{formatDate(item.arrivalDate)}</TableCell>
                                <TableCell className="px-6 py-4 text-center">{item.departure}</TableCell>
                                <TableCell className="px-6 py-4 text-center">{item.arrival}</TableCell>
                                <TableCell className="px-6 py-4 text-center">{item.flightDuration} hrs</TableCell>
                                <TableCell className={`px-6 py-4 text-center ${getStatusColor(item.statusID.status)}`}>
                                    {item.statusID.status}
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </div>
        </div>
    );
}
