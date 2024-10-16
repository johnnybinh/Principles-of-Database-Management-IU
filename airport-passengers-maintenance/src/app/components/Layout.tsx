import React from 'react';
import Link from 'next/link';
import Image from 'next/image';

const Layout = ({ children }: { children: React.ReactNode }) => {
    return (
        <div style={styles.container}>
            <div style={styles.backgroundImageContainer}>
                <Image
                    src="/images/airport-background.jpg"
                    alt="Airport Background"
                    layout="fill"
                    objectFit="cover"
                    quality={100}
                    priority
                />
            </div>
            <nav style={styles.nav}>
                <div style={styles.websiteName}>Booking</div>
                <ul style={styles.ul}>
                    <li style={styles.li}><Link href="/" style={styles.link}>Home</Link></li>
                    <li style={styles.li}><Link href="/data" style={styles.link}>Data</Link></li>
                    <li style={styles.li}><Link href="/view" style={styles.link}>View</Link></li>
                    <li style={styles.li}><Link href="/flight" style={styles.link}>Flight</Link></li>
                </ul>
                <button style={styles.flightButton}>Flight Now</button>
            </nav>
            <main style={styles.main}>{children}</main>
        </div>
    );
};

const styles = {
    container: {
        position: 'relative' as 'relative',
        minHeight: '100vh',
        padding: '1rem',
        zIndex: 1,
        fontSize: '18px', // Base font size increased
    },
    backgroundImageContainer: {
        position: 'fixed' as 'fixed',
        top: 0,
        left: 0,
        width: '100%',
        height: '100%',
        zIndex: -1,
    },
    nav: {
        backgroundColor: 'rgba(51, 51, 51, 0.8)',
        padding: '0.75rem 1.5rem', // Reduced padding
        borderRadius: '15px', // Slightly reduced border radius
        backdropFilter: 'blur(10px)',
        WebkitBackdropFilter: 'blur(10px)',
        boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)', // Reduced shadow
        margin: '0.5rem 0', // Reduced margin
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
    },
    websiteName: {
        color: '#FFFFFFF',
        fontSize: '2.2rem', // Increased from 1.5rem
        fontWeight: 'bold',
        fontFamily: "'Inria Serif', serif",
    },
    ul: {
        listStyle: 'none',
        margin: 0,
        padding: 0,
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
    },
    li: {
        margin: '0 1rem', // Slightly reduced margin
    },
    link: {
        color: '#FFFFFFF',
        textDecoration: 'none',
        fontSize: '1.5rem', // Increased from 1rem
        padding: '0.4rem 0.8rem', // Reduced padding
        borderRadius: '8px', // Slightly reduced border radius
        transition: 'background-color 0.3s ease',
    },
    flightButton: {
        backgroundColor: '#E41C1C',
        color: '#FFFFFFF',
        border: 'none',
        padding: '0.4rem 0.8rem', // Reduced padding
        borderRadius: '8px', // Slightly reduced border radius
        fontSize: '1.5rem', // Increased from 1rem
        fontWeight: 'bold',
        cursor: 'pointer',
        transition: 'background-color 0.3s ease',
    },
    main: {
        flex: 1,
        width: '100%',
        maxWidth: '1200px',
        padding: '1.5rem', // Increased from 1rem
        margin: '1rem auto 0', // Added top margin
        backgroundColor: 'rgba(255, 255, 255, 0.8)',
        borderRadius: '20px',
        backdropFilter: 'blur(5px)',
        WebkitBackdropFilter: 'blur(5px)',
    },
};

export default Layout;
