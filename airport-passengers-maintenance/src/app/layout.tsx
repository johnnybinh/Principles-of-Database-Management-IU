import type { Metadata } from "next";
import "./globals.css";
import Layout from "./components/Layout";

export const metadata: Metadata = {
  title: "Booking Flights",
  description: "Booking Flights",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta charSet="UTF-8" />
      </head>
      <body>
        <Layout>
          {children}
        </Layout>
      </body>
    </html>
  );
}
