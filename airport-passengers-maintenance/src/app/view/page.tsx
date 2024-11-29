'use client';
import { useState } from 'react';
import { Button } from "@/components/ui/button"
import { Ticket } from "../types/types";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

// Define interface for the selected ticket
interface SelectedTicket extends Ticket {}

const table_header = ["Departure Date", "Arrival Date", "Departure", "Arrival", "Duration", "Status", "Actions"];

export default function View() {
  const [email, setEmail] = useState('');
  const [tickets, setTickets] = useState<Ticket[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  
  // State for modal
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedTicket, setSelectedTicket] = useState<SelectedTicket | null>(null);

  const handleFetchTickets = async () => {
    if (!email) {
      setError('Please enter an email');
      return;
    }

    try {
      setLoading(true);
      setError(null);
      const response = await fetch(`http://localhost:8080/api/tickets/email/${encodeURIComponent(email)}`);
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const data: Ticket[] = await response.json();
      console.log('Fetched Tickets:', data); // Add this line
      setTickets(data);
    } catch (err) {
      setError('Failed to fetch tickets');
      console.error('Error:', err);
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateStr: string) => {
    if (!dateStr) return '-';
    return new Date(dateStr).toLocaleDateString();
  };

  const formatDateTime = (dateStr: string) => {
    if (!dateStr) return '-';
    const date = new Date(dateStr);
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

  const handleCancel = async (ticketID: string) => {
    if (!confirm('Are you sure you want to cancel this ticket?')) {
      return;
    }
  
    try {
      const response = await fetch(`http://localhost:8080/api/tickets/${ticketID}`, {
        method: 'DELETE',
      });
  
      if (response.ok) {
        // Remove ticket from state
        setTickets(tickets.filter(ticket => ticket.ticketID !== ticketID));
      } else {
        throw new Error('Failed to cancel ticket');
      }
    } catch (err) {
      console.error('Error cancelling ticket:', err);
      alert('Failed to cancel ticket');
    }
  };

  const handleDetail = (ticket: Ticket) => {
    setSelectedTicket(ticket);
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
    setSelectedTicket(null);
  };

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
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="example@gmail.com"
              className="w-2/5 px-4 py-1 text-white rounded-l-lg rounded-r-lg focus:outline-none bg-white/30 border-2 border-black focus:ring-2 focus:ring-blue-500 mr-2.5 placeholder:text-white placeholder:italic placeholder:font-bold"
            />
            <Button 
              onClick={handleFetchTickets}
              disabled={loading}
              className="bg-black text-white font-bold py-4 px-6 rounded-lg shadow-md hover:bg-gray-800 focus:outline-none transform hover:scale-105 transition-transform duration-200"
            >
              {loading ? 'Loading...' : 'View'}
            </Button>
          </div>
          {error && <p className="text-red-500 mt-2">{error}</p>}
        </div>

        {tickets.length > 0 && (
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
            {tickets.map((ticket, index) => (
                <TableRow key={index} className="bg-white/50 text-center text-base text-black uppercase tracking-wider hover:bg-white/70 transition-colors duration-200">
                  <TableCell className="px-6 py-4 text-center text-lg">
                    {formatDateTime(ticket.flightBase.flightSchedule.departureDate)}
                  </TableCell>
                  <TableCell className="px-6 py-4 text-center text-lg">
                    {formatDateTime(ticket.flightBase.flightSchedule.arrivalDate)}
                  </TableCell>
                  <TableCell className="px-6 py-4 text-center text-lg">
                    {ticket.flightBase.flightSchedule.departure}
                  </TableCell>
                  <TableCell className="px-6 py-4 text-center text-lg">
                    {ticket.flightBase.flightSchedule.arrival}
                  </TableCell>
                  <TableCell className="px-6 py-4 text-center text-lg">
                    {ticket.flightBase.flightSchedule.flightDuration} hrs
                  </TableCell>
                  <TableCell className={`px-6 py-4 text-center text-lg ${getStatusColor(ticket.flightBase.flightSchedule.flightStatus.status)}`}>
                    {ticket.flightBase.flightSchedule.flightStatus.status}
                  </TableCell>
                  <TableCell>
                    <div className="flex space-x-2 justify-center">
                      <Button
                        className="bg-black text-white font-bold py-2 px-4 rounded-lg shadow-md hover:bg-red-800 focus:outline-none transform hover:scale-105 transition-transform duration-200"
                        onClick={() => handleCancel(ticket.ticketID)} // Add your cancel handler
                      >
                        Cancel
                      </Button>
                      <Button
                        className="bg-black text-white font-bold py-2 px-4 rounded-lg shadow-md hover:bg-gray-800 focus:outline-none transform hover:scale-105 transition-transform duration-200"
                        onClick={() => handleDetail(ticket)} // Add your detail handler
                      >
                        Detail
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        )}
      </div>
      {/* Modal */}
      {isModalOpen && selectedTicket && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
          <div className="bg-gradient-to-br from-gray-100 to-gray-200 rounded-lg shadow-lg w-11/12 md:w-2/3 lg:w-1/2 p-6 relative">
            <button
              className="absolute top-4 right-4 bg-black text-white font-bold py-2 px-4 rounded-lg shadow-md hover:bg-gray-800 focus:outline-none transform hover:scale-105 transition-transform duration-200"
              onClick={closeModal}
            >
              ×
            </button>
            <h2 className="text-2xl font-bold mb-6 text-black border-b pb-3 text-center">Ticket Details</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-black">
              <div className="bg-white/90 p-4 rounded-lg shadow-md col-span-1 md:col-span-2">
                <h3 className="font-semibold text-lg mb-3 text-black">Passenger Information</h3>
                <div className="space-y-2">
                  <p className='text-black'><span className="font-medium">Ticket ID:</span> {selectedTicket.ticketID}</p>
                  <p className='text-black'><span className="font-medium">Passenger:</span> {selectedTicket.passenger.firstName} {selectedTicket.passenger.lastName}</p>
                  <p className='text-black'><span className="font-medium">Seat:</span> {selectedTicket.seat.seatNumber}</p>
                  <p className='text-black'><span className="font-medium">Baggage Weight:</span> {selectedTicket.baggageWeight} kg</p>
                  <p className='text-black'><span className="font-medium">Final Price:</span> ${selectedTicket.finalPrice}</p>
                </div>
              </div>

              <div className="bg-white/90 p-4 rounded-lg shadow-md col-span-1 md:col-span-2">
                <h3 className="font-semibold text-lg mb-3 text-black">Flight Information</h3>
                <div className="space-y-2">
                  <p className='text-black'><span className="font-medium">Flight ID:</span> {selectedTicket.flightBase.flightID}</p>
                  <p className='text-black'><span className="font-medium">Airline:</span> {selectedTicket.flightBase.airline.airlineName}</p>
                  <p className='text-black'><span className="font-medium">Gate:</span> {selectedTicket.flightBase.gate.gateNumber}</p>
                </div>
              </div>

              <div className="bg-white/90 p-4 rounded-lg shadow-md col-span-1 md:col-span-2">
                <h3 className="font-semibold text-lg mb-3 text-black">Schedule Details</h3>
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <p className='text-black'><span className="font-medium">Departure:</span> {selectedTicket.flightBase.flightSchedule.departure}</p>
                    <p className='text-black'><span className="font-medium">Departure Date:</span> {formatDate(selectedTicket.flightBase.flightSchedule.departureDate)}</p>
                  </div>
                  <div className="space-y-2">
                    <p className='text-black'><span className="font-medium">Arrival:</span> {selectedTicket.flightBase.flightSchedule.arrival}</p>
                    <p className='text-black'><span className="font-medium">Arrival Date:</span> {formatDate(selectedTicket.flightBase.flightSchedule.arrivalDate)}</p>
                  </div>
                  <div className="col-span-2 space-y-2">
                    <p className='text-black'><span className="font-medium">Duration:</span> {selectedTicket.flightBase.flightSchedule.flightDuration} Hours</p>
                    <p className='text-black'><span className="font-medium">Status:</span> 
                      <span className={`ml-2 px-2 py-1 rounded-full text-xl ${
                        getStatusColor(selectedTicket.flightBase.flightSchedule.flightStatus.status)
                      }`}>
                        {selectedTicket.flightBase.flightSchedule.flightStatus.status}
                      </span>
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
