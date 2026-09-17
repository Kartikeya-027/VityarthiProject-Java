import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.UUID;

public class RailwayReservationSystem {

    // =========================
    // ENUMS
    // =========================

    enum Gender {
        MALE, FEMALE, OTHER
    }

    enum BookingStatus {
        CONFIRMED, CANCELLED
    }

    // =========================
    // PASSENGER CLASS
    // =========================

    static class Passenger {

        private int id;
        private String name;
        private int age;
        private Gender gender;
        private String phone;

        public Passenger(int id, String name, int age,
                         Gender gender, String phone) {

            this.id = id;
            this.name = name;
            this.age = age;
            this.gender = gender;
            this.phone = phone;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "ID: " + id
                    + " | Name: " + name
                    + " | Age: " + age
                    + " | Gender: " + gender
                    + " | Phone: " + phone;
        }
    }

    // =========================
    // TRAIN CLASS
    // =========================

    static class Train {

        private int trainNo;
        private String trainName;
        private String source;
        private String destination;
        private int totalSeats;

        // true means seat is occupied
        private boolean[] occupiedSeats;

        public Train(int trainNo,
                     String trainName,
                     String source,
                     String destination,
                     int totalSeats) {

            this.trainNo = trainNo;
            this.trainName = trainName;
            this.source = source;
            this.destination = destination;
            this.totalSeats = totalSeats;
            this.occupiedSeats = new boolean[totalSeats];
        }

        public int getTrainNo() {
            return trainNo;
        }

        public String getTrainName() {
            return trainName;
        }

        public int getTotalSeats() {
            return totalSeats;
        }

        // Find and reserve the first available seat
        public int reserveSeat() {

            for (int i = 0; i < occupiedSeats.length; i++) {

                if (!occupiedSeats[i]) {

                    occupiedSeats[i] = true;

                    // Seat numbers start from 1
                    return i + 1;
                }
            }

            return -1;
        }

        // Release a seat
        public void releaseSeat(int seatNumber) {

            if (seatNumber >= 1
                    && seatNumber <= totalSeats) {

                occupiedSeats[seatNumber - 1] = false;
            }
        }

        public int getAvailableSeats() {

            int count = 0;

            for (boolean occupied : occupiedSeats) {

                if (!occupied) {
                    count++;
                }
            }

            return count;
        }

        @Override
        public String toString() {

            return trainNo
                    + " | " + trainName
                    + " | " + source
                    + " -> " + destination
                    + " | Available Seats: "
                    + getAvailableSeats()
                    + "/" + totalSeats;
        }
    }

    // =========================
    // BOOKING CLASS
    // =========================

    static class Booking {

        private String bookingId;
        private Passenger passenger;
        private Train train;
        private int seatNumber;
        private BookingStatus status;

        public Booking(String bookingId,
                       Passenger passenger,
                       Train train,
                       int seatNumber,
                       BookingStatus status) {

            this.bookingId = bookingId;
            this.passenger = passenger;
            this.train = train;
            this.seatNumber = seatNumber;
            this.status = status;
        }

        public String getBookingId() {
            return bookingId;
        }

        public Passenger getPassenger() {
            return passenger;
        }

        public Train getTrain() {
            return train;
        }

        public int getSeatNumber() {
            return seatNumber;
        }

        public BookingStatus getStatus() {
            return status;
        }

        public void setStatus(BookingStatus status) {
            this.status = status;
        }

        @Override
        public String toString() {

            return "PNR: " + bookingId
                    + " | Passenger: "
                    + passenger.getName()
                    + " | Train: "
                    + train.getTrainName()
                    + " | Seat: "
                    + seatNumber
                    + " | Status: "
                    + status;
        }
    }

    // =========================
    // WAITING REQUEST CLASS
    // =========================

    static class WaitingRequest {

        private Passenger passenger;
        private Train train;

        public WaitingRequest(Passenger passenger,
                              Train train) {

            this.passenger = passenger;
            this.train = train;
        }

        public Passenger getPassenger() {
            return passenger;
        }

        public Train getTrain() {
            return train;
        }
    }

    // =========================
    // PASSENGER SERVICE
    // =========================

    static class PassengerService {

        private ArrayList<Passenger> passengers =
                new ArrayList<>();

        public boolean addPassenger(Passenger passenger) {

            // Check duplicate ID
            if (searchPassenger(passenger.getId()) != null) {

                System.out.println(
                        "Passenger ID already exists.");

                return false;
            }

            passengers.add(passenger);

            System.out.println(
                    "Passenger Registered Successfully.");

            return true;
        }

        public Passenger searchPassenger(int id) {

            for (Passenger p : passengers) {

                if (p.getId() == id) {
                    return p;
                }
            }

            return null;
        }

        public void viewPassengers() {

            if (passengers.isEmpty()) {

                System.out.println(
                        "No Passengers Found.");

                return;
            }

            System.out.println(
                    "\n----- PASSENGER LIST -----");

            for (Passenger p : passengers) {

                System.out.println(p);
            }
        }

        public int totalPassengers() {
            return passengers.size();
        }
    }

    // =========================
    // TRAIN SERVICE
    // =========================

    static class TrainService {

        private ArrayList<Train> trains =
                new ArrayList<>();

        public void addTrain(Train train) {

            trains.add(train);
        }

        public Train searchTrain(int trainNo) {

            for (Train t : trains) {

                if (t.getTrainNo() == trainNo) {
                    return t;
                }
            }

            return null;
        }

        public void viewTrains() {

            if (trains.isEmpty()) {

                System.out.println(
                        "No Trains Found.");

                return;
            }

            System.out.println(
                    "\n----- TRAIN LIST -----");

            for (Train t : trains) {

                System.out.println(t);
            }
        }

        public int totalTrains() {
            return trains.size();
        }
    }

    // =========================
    // BOOKING SERVICE
    // =========================

    static class BookingService {

        private ArrayList<Booking> bookings =
                new ArrayList<>();

        private Queue<WaitingRequest> waitingList =
                new LinkedList<>();

        /*
         * synchronized prevents two threads
         * from booking the same seat simultaneously.
         */
        public synchronized void bookTicket(
                Passenger passenger,
                Train train) {

            int seatNumber = train.reserveSeat();

            // No seat available
            if (seatNumber == -1) {

                waitingList.add(
                        new WaitingRequest(
                                passenger,
                                train));

                System.out.println(
                        passenger.getName()
                                + " added to Waiting List for "
                                + train.getTrainName()
                                + ".");

                return;
            }

            String pnr = generatePNR();

            Booking booking =
                    new Booking(
                            pnr,
                            passenger,
                            train,
                            seatNumber,
                            BookingStatus.CONFIRMED);

            bookings.add(booking);

            System.out.println(
                    "\nBooking Successful!");

            System.out.println(booking);
        }

        // Generate a short unique PNR
        private String generatePNR() {

            return "PNR"
                    + UUID.randomUUID()
                    .toString()
                    .substring(0, 6)
                    .toUpperCase();
        }

        // =========================
        // VIEW BOOKINGS
        // =========================

        public void viewBookings() {

            if (bookings.isEmpty()) {

                System.out.println(
                        "No Bookings Found.");

                return;
            }

            System.out.println(
                    "\n----- BOOKING LIST -----");

            for (Booking booking : bookings) {

                System.out.println(booking);
            }
        }

        // =========================
        // CANCEL BOOKING
        // =========================

        public synchronized void cancelBooking(
                String pnr) {

            for (Booking booking : bookings) {

                if (booking.getBookingId()
                        .equalsIgnoreCase(pnr)) {

                    // Already cancelled
                    if (booking.getStatus()
                            == BookingStatus.CANCELLED) {

                        System.out.println(
                                "This booking is already cancelled.");

                        return;
                    }

                    booking.setStatus(
                            BookingStatus.CANCELLED);

                    Train train =
                            booking.getTrain();

                    // Release the cancelled seat
                    train.releaseSeat(
                            booking.getSeatNumber());

                    System.out.println(
                            "Booking Cancelled Successfully.");

                    System.out.println(
                            "Released Seat: "
                                    + booking.getSeatNumber());

                    /*
                     * Promote the first waiting passenger
                     * belonging to the same train.
                     */
                    promoteWaitingPassenger(train);

                    return;
                }
            }

            System.out.println(
                    "Booking Not Found.");
        }

        // =========================
        // WAITING LIST PROMOTION
        // =========================

        private void promoteWaitingPassenger(
                Train train) {

            WaitingRequest selectedRequest = null;

            for (WaitingRequest request : waitingList) {

                if (request.getTrain()
                        .getTrainNo()
                        == train.getTrainNo()) {

                    selectedRequest = request;

                    break;
                }
            }

            if (selectedRequest == null) {
                return;
            }

            waitingList.remove(selectedRequest);

            Passenger passenger =
                    selectedRequest.getPassenger();

            int seatNumber =
                    train.reserveSeat();

            if (seatNumber == -1) {

                // Safety check
                waitingList.add(selectedRequest);

                return;
            }

            String pnr = generatePNR();

            Booking booking =
                    new Booking(
                            pnr,
                            passenger,
                            train,
                            seatNumber,
                            BookingStatus.CONFIRMED);

            bookings.add(booking);

            System.out.println(
                    "\nWaiting List Passenger Promoted!");

            System.out.println(booking);
        }

        // =========================
        // VIEW WAITING LIST
        // =========================

        public void viewWaitingList() {

            if (waitingList.isEmpty()) {

                System.out.println(
                        "Waiting List is Empty.");

                return;
            }

            System.out.println(
                    "\n----- WAITING LIST -----");

            int position = 1;

            for (WaitingRequest request : waitingList) {

                System.out.println(
                        position
                                + ". "
                                + request
                                .getPassenger()
                                .getName()
                                + " | Train: "
                                + request
                                .getTrain()
                                .getTrainName());

                position++;
            }
        }

        // =========================
        // REPORT
        // =========================

        public void showReport() {

            int confirmed = 0;
            int cancelled = 0;

            for (Booking booking : bookings) {

                if (booking.getStatus()
                        == BookingStatus.CONFIRMED) {

                    confirmed++;

                } else if (booking.getStatus()
                        == BookingStatus.CANCELLED) {

                    cancelled++;
                }
            }

            System.out.println(
                    "\n========== SYSTEM REPORT ==========");

            System.out.println(
                    "Total Booking Records: "
                            + bookings.size());

            System.out.println(
                    "Confirmed Bookings: "
                            + confirmed);

            System.out.println(
                    "Cancelled Bookings: "
                            + cancelled);

            System.out.println(
                    "Waiting List Count: "
                            + waitingList.size());

            System.out.println(
                    "===================================");
        }

        public int totalBookings() {

            int count = 0;

            for (Booking booking : bookings) {

                if (booking.getStatus()
                        == BookingStatus.CONFIRMED) {

                    count++;
                }
            }

            return count;
        }
    }

    // =========================
    // BOOKING THREAD
    // =========================

    static class BookingThread extends Thread {

        private BookingService bookingService;
        private Passenger passenger;
        private Train train;

        public BookingThread(
                BookingService bookingService,
                Passenger passenger,
                Train train) {

            this.bookingService =
                    bookingService;

            this.passenger =
                    passenger;

            this.train =
                    train;
        }

        @Override
        public void run() {

            bookingService.bookTicket(
                    passenger,
                    train);
        }
    }

    // =========================
    // INPUT METHODS
    // =========================

    public static int readInt(
            Scanner sc,
            String message) {

        while (true) {

            System.out.print(message);

            try {

                return Integer.parseInt(
                        sc.nextLine());

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number.");
            }
        }
    }

    public static String readString(
            Scanner sc,
            String message) {

        while (true) {

            System.out.print(message);

            String input =
                    sc.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println(
                    "Input cannot be empty.");
        }
    }

    // =========================
    // MAIN METHOD
    // =========================

    public static void main(String[] args) {

        Scanner sc =
                new Scanner(System.in);

        PassengerService passengerService =
                new PassengerService();

        TrainService trainService =
                new TrainService();

        BookingService bookingService =
                new BookingService();

        // =========================
        // DEFAULT TRAINS
        // =========================

        trainService.addTrain(
                new Train(
                        101,
                        "Rajdhani Express",
                        "Delhi",
                        "Mumbai",
                        5));

        trainService.addTrain(
                new Train(
                        102,
                        "Shatabdi Express",
                        "Bhopal",
                        "Delhi",
                        5));

        trainService.addTrain(
                new Train(
                        103,
                        "Vande Bharat",
                        "Delhi",
                        "Bhopal",
                        5));

        // =========================
        // MAIN MENU
        // =========================

        while (true) {

            System.out.println(
                    "\n==========================================");

            System.out.println(
                    "     SMART RAILWAY RESERVATION SYSTEM");

            System.out.println(
                    "==========================================");

            System.out.println(
                    "1. Register Passenger");

            System.out.println(
                    "2. View Passengers");

            System.out.println(
                    "3. View Trains");

            System.out.println(
                    "4. Book Ticket");

            System.out.println(
                    "5. View Bookings");

            System.out.println(
                    "6. Cancel Booking");

            System.out.println(
                    "7. View Waiting List");

            System.out.println(
                    "8. View Reports");

            System.out.println(
                    "9. Concurrent Booking Demo");

            System.out.println(
                    "0. Exit");

            System.out.println(
                    "==========================================");

            int choice =
                    readInt(
                            sc,
                            "Enter Choice: ");

            switch (choice) {

                // =========================
                // REGISTER PASSENGER
                // =========================

                case 1:

                    System.out.println(
                            "\n----- REGISTER PASSENGER -----");

                    int id =
                            readInt(
                                    sc,
                                    "Passenger ID: ");

                    String name =
                            readString(
                                    sc,
                                    "Name: ");

                    int age =
                            readInt(
                                    sc,
                                    "Age: ");

                    if (age <= 0
                            || age > 120) {

                        System.out.println(
                                "Invalid age.");

                        break;
                    }

                    String genderInput =
                            readString(
                                    sc,
                                    "Gender (M/F/O): ");

                    Gender gender;

                    if (genderInput
                            .equalsIgnoreCase("M")) {

                        gender = Gender.MALE;

                    } else if (genderInput
                            .equalsIgnoreCase("F")) {

                        gender = Gender.FEMALE;

                    } else if (genderInput
                            .equalsIgnoreCase("O")) {

                        gender = Gender.OTHER;

                    } else {

                        System.out.println(
                                "Invalid gender.");

                        break;
                    }

                    String phone =
                            readString(
                                    sc,
                                    "Phone Number: ");

                    if (!phone.matches("\\d{10}")) {

                        System.out.println(
                                "Phone number must contain exactly 10 digits.");

                        break;
                    }

                    Passenger passenger =
                            new Passenger(
                                    id,
                                    name,
                                    age,
                                    gender,
                                    phone);

                    passengerService
                            .addPassenger(
                                    passenger);

                    break;

                // =========================
                // VIEW PASSENGERS
                // =========================

                case 2:

                    passengerService
                            .viewPassengers();

                    break;

                // =========================
                // VIEW TRAINS
                // =========================

                case 3:

                    trainService
                            .viewTrains();

                    break;

                // =========================
                // BOOK TICKET
                // =========================

                case 4:

                    System.out.println(
                            "\n----- BOOK TICKET -----");

                    int passengerId =
                            readInt(
                                    sc,
                                    "Passenger ID: ");

                    Passenger p =
                            passengerService
                                    .searchPassenger(
                                            passengerId);

                    if (p == null) {

                        System.out.println(
                                "Passenger Not Found.");

                        break;
                    }

                    int trainNo =
                            readInt(
                                    sc,
                                    "Train Number: ");

                    Train train =
                            trainService
                                    .searchTrain(
                                            trainNo);

                    if (train == null) {

                        System.out.println(
                                "Train Not Found.");

                        break;
                    }

                    bookingService
                            .bookTicket(
                                    p,
                                    train);

                    break;

                // =========================
                // VIEW BOOKINGS
                // =========================

                case 5:

                    bookingService
                            .viewBookings();

                    break;

                // =========================
                // CANCEL BOOKING
                // =========================

                case 6:

                    String pnr =
                            readString(
                                    sc,
                                    "Enter PNR: ");

                    bookingService
                            .cancelBooking(
                                    pnr);

                    break;

                // =========================
                // WAITING LIST
                // =========================

                case 7:

                    bookingService
                            .viewWaitingList();

                    break;

                // =========================
                // REPORTS
                // =========================

                case 8:

                    System.out.println(
                            "\n========== REPORTS ==========");

                    System.out.println(
                            "Total Passengers: "
                                    + passengerService
                                    .totalPassengers());

                    System.out.println(
                            "Total Trains: "
                                    + trainService
                                    .totalTrains());

                    System.out.println(
                            "Active Confirmed Bookings: "
                                    + bookingService
                                    .totalBookings());

                    bookingService
                            .showReport();

                    break;

                // =========================
                // CONCURRENT BOOKING
                // =========================

                case 9:

                    System.out.println(
                            "\n===== CONCURRENT BOOKING DEMO =====");

                    /*
                     * A train with only ONE seat.
                     * Two passengers will try to
                     * book it at the same time.
                     */

                    Train demoTrain =
                            new Train(
                                    500,
                                    "Concurrent Express",
                                    "A",
                                    "B",
                                    1);

                    Passenger p1 =
                            new Passenger(
                                    100,
                                    "Aman",
                                    20,
                                    Gender.MALE,
                                    "1111111111");

                    Passenger p2 =
                            new Passenger(
                                    101,
                                    "Riya",
                                    21,
                                    Gender.FEMALE,
                                    "2222222222");

                    BookingThread t1 =
                            new BookingThread(
                                    bookingService,
                                    p1,
                                    demoTrain);

                    BookingThread t2 =
                            new BookingThread(
                                    bookingService,
                                    p2,
                                    demoTrain);

                    System.out.println(
                            "Two passengers are trying to book "
                                    + "the same train...");

                    t1.start();
                    t2.start();

                    try {

                        t1.join();
                        t2.join();

                    } catch (InterruptedException e) {

                        Thread.currentThread()
                                .interrupt();

                        System.out.println(
                                "Thread was interrupted.");
                    }

                    System.out.println(
                            "Concurrent Booking Demo Completed.");

                    break;

                // =========================
                // EXIT
                // =========================

                case 0:

                    System.out.println(
                            "\nThank you for using "
                                    + "Smart Railway Reservation System!");

                    sc.close();

                    return;

                // =========================
                // INVALID CHOICE
                // =========================

                default:

                    System.out.println(
                            "Invalid Choice. "
                                    + "Please try again.");
            }
        }
    }
}