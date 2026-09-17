
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

class PassengerService {
    private ArrayList<Passenger> passengers = new ArrayList<>();
    
    public boolean addPassenger(Passenger passenger) {
        if (searchPassenger(passenger.getId()) != null) {
            System.out.println("Passenger ID already exists.");
            return false;
        }
        passengers.add(passenger);
        System.out.println("Passenger Registered Successfully.");
        return true;
    }
    
    public Passenger searchPassenger(int id) {
        for (Passenger p : passengers) {
            if (p.getId() == id) return p;
        }
        return null;
    }
    
    public void viewPassengers() {
        if (passengers.isEmpty()) {
            System.out.println("No Passengers Found.");
            return;
        }
        System.out.println("\n----- PASSENGER LIST -----");
        for (Passenger p : passengers) System.out.println(p);
    }
    
    public int totalPassengers() { return passengers.size(); }
}

class TrainService {
    private ArrayList<Train> trains = new ArrayList<>();
    public void addTrain(Train train) { trains.add(train); }
    public Train searchTrain(int trainNo) {
        for (Train t : trains) {
            if (t.getTrainNo() == trainNo) return t;
        }
        return null;
    }
    public void viewTrains() {
        if (trains.isEmpty()) {
            System.out.println("No Trains Found.");
            return;
        }
        System.out.println("\n----- TRAIN LIST -----");
        for (Train t : trains) System.out.println(t);
    }
    public int totalTrains() { return trains.size(); }
}

class BookingService {
    private ArrayList<Booking> bookings = new ArrayList<>();
    private Queue<WaitingRequest> waitingList = new LinkedList<>();

    public synchronized void bookTicket(Passenger passenger, Train train) {
        int seatNumber = train.reserveSeat();
        if (seatNumber == -1) {
            waitingList.add(new WaitingRequest(passenger, train));
            System.out.println(passenger.getName() + " added to Waiting List for " + train.getTrainName() + ".");
            return;
        }
        String pnr = generatePNR();
        Booking booking = new Booking(pnr, passenger, train, seatNumber, BookingStatus.CONFIRMED);
        bookings.add(booking);
        System.out.println("\nBooking Successful!");
        System.out.println(booking);
    }

    private String generatePNR() {
        return "PNR" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    public void viewBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No Bookings Found.");
            return;
        }
        System.out.println("\n----- BOOKING LIST -----");
        for (Booking booking : bookings) System.out.println(booking);
    }

    public synchronized void cancelBooking(String pnr) {
        for (Booking booking : bookings) {
            if (booking.getBookingId().equalsIgnoreCase(pnr)) {
                if (booking.getStatus() == BookingStatus.CANCELLED) {
                    System.out.println("This booking is already cancelled.");
                    return;
                }
                booking.setStatus(BookingStatus.CANCELLED);
                Train train = booking.getTrain();
                train.releaseSeat(booking.getSeatNumber());
                System.out.println("Booking Cancelled Successfully.");
                System.out.println("Released Seat: " + booking.getSeatNumber());
                promoteWaitingPassenger(train);
                return;
            }
        }
        System.out.println("Booking Not Found.");
    }

    private void promoteWaitingPassenger(Train train) {
        WaitingRequest selectedRequest = null;
        for (WaitingRequest request : waitingList) {
            if (request.getTrain().getTrainNo() == train.getTrainNo()) {
                selectedRequest = request;
                break;
            }
        }
        if (selectedRequest == null) return;
        waitingList.remove(selectedRequest);
        Passenger passenger = selectedRequest.getPassenger();
        int seatNumber = train.reserveSeat();
        if (seatNumber == -1) {
            waitingList.add(selectedRequest);
            return;
        }
        String pnr = generatePNR();
        Booking booking = new Booking(pnr, passenger, train, seatNumber, BookingStatus.CONFIRMED);
        bookings.add(booking);
        System.out.println("\nWaiting List Passenger Promoted!");
        System.out.println(booking);
    }

    public void viewWaitingList() {
        if (waitingList.isEmpty()) {
            System.out.println("Waiting List is Empty.");
            return;
        }
        System.out.println("\n----- WAITING LIST -----");
        int position = 1;
        for (WaitingRequest request : waitingList) {
            System.out.println(position + ". " + request.getPassenger().getName() + " | Train: " + request.getTrain().getTrainName());
            position++;
        }
    }

    public void showReport() {
        int confirmed = 0;
        int cancelled = 0;
        for (Booking booking : bookings) {
            if (booking.getStatus() == BookingStatus.CONFIRMED) confirmed++;
            else if (booking.getStatus() == BookingStatus.CANCELLED) cancelled++;
        }
        System.out.println("\n========== SYSTEM REPORT ==========");
        System.out.println("Total Booking Records: " + bookings.size());
        System.out.println("Confirmed Bookings: " + confirmed);
        System.out.println("Cancelled Bookings: " + cancelled);
        System.out.println("Waiting List Count: " + waitingList.size());
        System.out.println("===================================");
    }

    public int totalBookings() {
        int count = 0;
        for (Booking booking : bookings) {
            if (booking.getStatus() == BookingStatus.CONFIRMED) count++;
        }
        return count;
    }
}

class BookingThread extends Thread {
    private BookingService bookingService;
    private Passenger passenger;
    private Train train;
    public BookingThread(BookingService bookingService, Passenger passenger, Train train) {
        this.bookingService = bookingService;
        this.passenger = passenger;
        this.train = train;
    }
    @Override
    public void run() {
        bookingService.bookTicket(passenger, train);
    }
}
