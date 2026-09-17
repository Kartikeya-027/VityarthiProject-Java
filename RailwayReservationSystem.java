
import java.util.Scanner;

public class RailwayReservationSystem {

    public static int readInt(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public static String readString(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Input cannot be empty.");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PassengerService passengerService = new PassengerService();
        TrainService trainService = new TrainService();
        BookingService bookingService = new BookingService();

        trainService.addTrain(new Train(101, "Rajdhani Express", "Delhi", "Mumbai", 5));
        trainService.addTrain(new Train(102, "Shatabdi Express", "Bhopal", "Delhi", 5));
        trainService.addTrain(new Train(103, "Vande Bharat", "Delhi", "Bhopal", 5));

        while (true) {
            System.out.println("\n==========================================");
            System.out.println("     SMART RAILWAY RESERVATION SYSTEM");
            System.out.println("==========================================");
            System.out.println("1. Register Passenger");
            System.out.println("2. View Passengers");
            System.out.println("3. View Trains");
            System.out.println("4. Book Ticket");
            System.out.println("5. View Bookings");
            System.out.println("6. Cancel Booking");
            System.out.println("7. View Waiting List");
            System.out.println("8. View Reports");
            System.out.println("9. Concurrent Booking Demo");
            System.out.println("0. Exit");
            System.out.println("==========================================");

            int choice = readInt(sc, "Enter Choice: ");

            switch (choice) {
                case 1:
                    System.out.println("\n----- REGISTER PASSENGER -----");
                    int id = readInt(sc, "Passenger ID: ");
                    String name = readString(sc, "Name: ");
                    int age = readInt(sc, "Age: ");
                    if (age <= 0 || age > 120) {
                        System.out.println("Invalid age.");
                        break;
                    }
                    String genderInput = readString(sc, "Gender (M/F/O): ");
                    Gender gender;
                    if (genderInput.equalsIgnoreCase("M")) gender = Gender.MALE;
                    else if (genderInput.equalsIgnoreCase("F")) gender = Gender.FEMALE;
                    else if (genderInput.equalsIgnoreCase("O")) gender = Gender.OTHER;
                    else {
                        System.out.println("Invalid gender.");
                        break;
                    }
                    String phone = readString(sc, "Phone Number: ");
                    if (!phone.matches("\\d{10}")) {
                        System.out.println("Phone number must contain exactly 10 digits.");
                        break;
                    }
                    Passenger passenger = new Passenger(id, name, age, gender, phone);
                    passengerService.addPassenger(passenger);
                    break;
                case 2:
                    passengerService.viewPassengers();
                    break;
                case 3:
                    trainService.viewTrains();
                    break;
                case 4:
                    System.out.println("\n----- BOOK TICKET -----");
                    int passengerId = readInt(sc, "Passenger ID: ");
                    Passenger p = passengerService.searchPassenger(passengerId);
                    if (p == null) {
                        System.out.println("Passenger Not Found.");
                        break;
                    }
                    int trainNo = readInt(sc, "Train Number: ");
                    Train train = trainService.searchTrain(trainNo);
                    if (train == null) {
                        System.out.println("Train Not Found.");
                        break;
                    }
                    bookingService.bookTicket(p, train);
                    break;
                case 5:
                    bookingService.viewBookings();
                    break;
                case 6:
                    String pnr = readString(sc, "Enter PNR: ");
                    bookingService.cancelBooking(pnr);
                    break;
                case 7:
                    bookingService.viewWaitingList();
                    break;
                case 8:
                    System.out.println("\n========== REPORTS ==========");
                    System.out.println("Total Passengers: " + passengerService.totalPassengers());
                    System.out.println("Total Trains: " + trainService.totalTrains());
                    System.out.println("Active Confirmed Bookings: " + bookingService.totalBookings());
                    bookingService.showReport();
                    break;
                case 9:
                    System.out.println("\n===== CONCURRENT BOOKING DEMO =====");
                    Train demoTrain = new Train(500, "Concurrent Express", "A", "B", 1);
                    Passenger p1 = new Passenger(100, "Aman", 20, Gender.MALE, "1111111111");
                    Passenger p2 = new Passenger(101, "Riya", 21, Gender.FEMALE, "2222222222");
                    BookingThread t1 = new BookingThread(bookingService, p1, demoTrain);
                    BookingThread t2 = new BookingThread(bookingService, p2, demoTrain);
                    System.out.println("Two passengers are trying to book the same train...");
                    t1.start();
                    t2.start();
                    try {
                        t1.join();
                        t2.join();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Thread was interrupted.");
                    }
                    System.out.println("Concurrent Booking Demo Completed.");
                    break;
                case 0:
                    System.out.println("\nThank you for using Smart Railway Reservation System!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid Choice. Please try again.");
            }
        }
    }
}
