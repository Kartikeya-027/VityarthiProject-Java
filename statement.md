# Project Statement

## Problem Statement
Traditional railway booking systems often struggle with race conditions when multiple users attempt to book the last available seat simultaneously, leading to double-booking and data inconsistency. Furthermore, managing cancellations and automatically updating waitlists requires seamless queuing mechanisms. There is a need for a lightweight, thread-safe reservation system that can efficiently handle these operations in real-time.

## Scope of the Project
The project focuses on the core backend logic of a railway ticketing system. It encompasses passenger registration, train management, secure ticket booking, cancellation processing, and automated waitlist resolution. It operates as a console-based application in a simulated environment, bypassing the need for a persistent database or graphical user interface, to strictly demonstrate data structures and multithreading concepts.

## Target Users
* **Passengers/Travelers:** Individuals looking to book or cancel train tickets quickly.
* **Railway Administrators:** Staff members who need to view train capacities, passenger manifests, and system reports.
* **Computer Science Students/Educators:** Individuals studying concurrent programming and Java collections.

## High-Level Features
1. **Thread-Safe Transactions:** Synchronized core methods to prevent race conditions during booking.
2. **Automated Waitlist Promotion:** Queue-based waitlist that instantly upgrades passengers when a confirmed seat is relinquished.
3. **Dynamic Resource Tracking:** Real-time calculation and display of available versus occupied seats for all active trains.
4. **Comprehensive System Reporting:** Real-time analytics on confirmed, cancelled, and waitlisted requests.
