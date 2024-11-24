"use client";
import React, { useEffect, useState } from 'react';

interface Test {
  id: number;
  name: string;
}

const Flights: React.FC = () => {
  const [tests, setTests] = useState<Test[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchTests = async () => {
      try {
        const res = await fetch('http://localhost:8080/api/test', {
          headers: {
            'Accept': 'application/json',
          },
        });

        if (!res.ok) {
          throw new Error(`Network response was not ok: ${res.status} ${res.statusText}`);
        }

        const data: Test[] = await res.json();
        setTests(data);
      } catch (err: any) {
        console.error('Fetch error:', err);
        setError(err.message);
      }
    };

    fetchTests();
  }, []);

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (tests.length === 0) {
    return <div>No Flights Found</div>;
  }

  return (
    <div className="container">
      <h1>Flight List ({tests.length} items)</h1>
      <ul>
        {tests.map((test) => (
          <li key={test.id} className="flight-item">
            #{test.id} - {test.name}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Flights;

// Testing the docker
// docker exec -it backend-mysql-1 mysql -u root -p
// USE pdmdb;
// SELECT * FROM Test;