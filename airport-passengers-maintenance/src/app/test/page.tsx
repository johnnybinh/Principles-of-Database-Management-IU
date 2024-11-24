import React from 'react';

interface Test {
  id: number;
  name: string;
}

export default async function Flights() {
  try {
    const res = await fetch('http://localhost:8080/api/test'); // Spring Boot API URL
    if (!res.ok) {
      console.error('Fetch error:', res.status, res.statusText);
      throw new Error('Network response was not ok');
    }
    const data = await res.json();
    console.log('Fetched data:', data); // Log the fetched data
    const tests: Test[] = Array.isArray(data) ? data : [];

    return (
      <div>
        <h1>Flight List</h1>
        <ul>
          {tests.map((test) => (
            <li key={test.id}>
              {test.name}
            </li>
          ))}
        </ul>
      </div>
    );
  } catch (error) {
    console.error('Fetch error:', error);
    return <div>Error loading flights</div>;
  }
}