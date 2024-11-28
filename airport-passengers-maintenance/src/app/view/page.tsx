import { Button } from "@/components/ui/button"

import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
  } from "@/components/ui/table"
  

export default function View() {
    const table_header = ["Time Start", "Departure", "Port", "Duration", "Time End", "Destination", "Port"]
     
    return (
        <div className="flex justify-center h-screen">
            <div className="w-full max-w-6xl p-3 bg-opacity-80" style={{ marginTop: '100px' }}>
                <div className="mb-4">
                    <label htmlFor="email" className="block text-white text-2xl font-bold italic mb-2">
                        Enter your email:
                    </label>
                    <div className="flex">
                        <input
                            id="email"
                            type="email"
                            placeholder="example@gmail.com"
                            className="w-2/5 px-4 py-1 text-white rounded-l-lg rounded-r-lg focus:outline-none bg-white/30 border-2 border-black focus:ring-2 focus:ring-blue-500 mr-2.5 placeholder:text-white placeholder:italic placeholder:font-bold"
                        />
                        <Button className="bg-black text-white font-bold py-4 px-6 rounded-lg shadow-md hover:bg-gray-800 focus:outline-none transform hover:scale-105 transition-transform duration-200">
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

                        </TableRow>
                    </TableBody>
                </Table>
            </div>
        </div>
    );
}
