# Smart Railway Reservation System

## Project Overview
The Smart Railway Reservation System is a robust, console-based Java application designed to simulate real-world train ticketing processes. It allows users to register passengers, view available trains, book tickets, cancel reservations, and manage waitlists efficiently. A key feature of this system is its thread-safe architecture, which seamlessly handles concurrent booking attempts to prevent double-booking of seats.

## Features
* **Passenger Management:** Register passengers with unique IDs, names, ages, and contact details.
* **Train Scheduling:** Pre-loaded trains with configurable capacities, routes, and seat availability tracking.
* **Ticket Booking:** Reserve available seats and generate unique PNRs.
* **Waitlist Management:** Automatically queue passengers when trains are fully booked and promote them upon ticket cancellations.
* **Concurrency Handling:** Synchronized ticket booking and cancellation methods to handle multiple simultaneous booking threads securely.
* **Reporting:** View active confirmed bookings, waitlist queues, cancellations, and overall system statistics.

## Technologies Used
* **Language:** Java (Core Java)
* **Concepts:** Object-Oriented Programming (OOP), Multithreading, Synchronization, Enums, Collections Framework (ArrayList, LinkedList, Queue).

## Installation & Execution
1. Ensure you have the Java Development Kit (JDK) installed (Java 8 or higher).
2. Download or clone the repository containing `RailwayReservationSystem.java`.
3. Open a terminal or command prompt in the project directory.
4. Compile the source code:
   ```bash
   javac RailwayReservationSystem.java
   ```
5. Run the application:
   ```bash
   java RailwayReservationSystem
   ```

## Instructions for Testing
* **Basic Flow:** Start the application (Option 0 to exit). Register a passenger (Option 1). View trains (Option 3). Book a ticket (Option 4). View your booking (Option 5).
* **Waitlist Flow:** Book tickets on the same train until capacity is reached (e.g., 5 seats). Book a 6th ticket to see the user added to the waitlist. Cancel an earlier ticket (Option 6) to see the waitlisted user automatically promoted.
* **Concurrency Demo:** Select Option 9 from the main menu. This simulates two users (Aman and Riya) trying to book the single remaining seat on the "Concurrent Express" simultaneously. The system will successfully book one and waitlist the other, proving thread safety.
