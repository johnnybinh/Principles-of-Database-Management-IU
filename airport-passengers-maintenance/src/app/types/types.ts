// The data types used in the application
export interface HeatMapData {
  entity: string;
  week: number;
  day: string;
  flights: number;
  day_2019: string;
  flights_2019_reference: number;
  percent_vs_2019_daily: number;
  day_previous_year: string;
  flights_previous_year: number;
}
export interface HeatMapProps {
  width?: string; // Width in rem
  height?: string; // Height in rem
  margin_top?: number;
  margin_right?: number; 
  margin_bottom?: number; 
  margin_left?: number; 
}

// Data types of the API response onflight
export interface FlightSchedule {
  scheduleID: number;
  statusID: Status;
  departureDate: string;
  arrivalDate: string;
  departure: string;
  arrival: string;
}

export interface FlightScheduleSearch {
  from: string;
  to: string;
  departure: string;
  arrival: string;
}

// Define the Status interface
export interface Status {
  statusID: number;
  status: string;
}