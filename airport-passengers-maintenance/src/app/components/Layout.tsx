import React from 'react';
import Link from 'next/link';
import Image from 'next/image';
import airportBackground from '../images/airport-background.jpg'

const Layout = ({ children }: { children: React.ReactNode }) => {
    return (
        <div style={styles.container}>
            <div style={styles.backgroundImageContainer}>
                <Image
                    src={airportBackground}
                    alt="Airport Background"
                    quality={100}
                    style={{objectFit: "cover"}}
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
                    <li style={styles.li}><Link href="/employee" style={styles.link}></Link></li>
                </ul>
                <Link href="/flight" passHref>
                    <button style={styles.flightButton}>Flight Now</button>
                </Link>
            </nav>

            <main>{children}</main>
        </div>
    );
};

const styles = {
    container: {
        position: 'relative' as 'relative',
        minHeight: '100vh',
        padding: '1rem',
        zIndex: 1,
        fontSize: '18px',
        color: '#FFFFFF',
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
        backdropFilter: 'blur(15px)',
        WebkitBackdropFilter: 'blur(15px)',
        boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)', // Reduced shadow
        margin: '0.5rem 0', // Reduced margin
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        position: 'sticky' as 'sticky',
    },
    websiteName: {
        color: '#FFFFFF',
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
        color: '#FFFFFF',
        textDecoration: 'none',
        fontSize: '1.5rem', // Increased from 1rem
        padding: '0.4rem 0.8rem', // Reduced padding
        borderRadius: '8px', // Slightly reduced border radius
        transition: 'background-color 0.3s ease',
    },
    flightButton: {
        backgroundColor: '#E41C1C',
        color: '#FFFFFF',
        border: 'none',
        padding: '0.4rem 0.8rem', // Reduced padding
        borderRadius: '8px', // Slightly reduced border radius
        fontSize: '1.5rem', // Increased from 1rem
        fontWeight: 'bold',
        cursor: 'pointer',
        transition: 'background-color 0.3s ease',
    },
    // main: {
    //     flex: 1,
    //     width: '100%',
    //     maxWidth: '1200px',
    //     padding: '1.5rem', // Increased from 1rem
    //     margin: '1rem auto 0', // Added top margin
    //     backgroundColor: 'rgba(255, 255, 255, 0.8)',
    //     borderRadius: '20px',
    //     backdropFilter: 'blur(5px)',
    //     WebkitBackdropFilter: 'blur(5px)',
    //     color: '#000000',
    // },
    centerContent: {
        display: 'flex',
        flexDirection: 'column' as 'column',
        justifyContent: 'center',
        alignItems: 'center',
        height: 'calc(100vh - 100px)', // Adjust this value based on your nav height
        textAlign: 'center',
    },
    getFlightButton: {
        backgroundColor: '#E41C1C',
        color: '#FFFFFF',
        border: 'none',
        padding: '0.6rem 1.2rem',
        borderRadius: '8px',
        fontSize: '1.5rem',
        fontWeight: 'bold',
        cursor: 'pointer',
        marginTop: '2rem',
        transition: 'background-color 0.3s ease',
    },
};

export default Layout;
