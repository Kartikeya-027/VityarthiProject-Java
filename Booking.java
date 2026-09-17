
enum BookingStatus {
    CONFIRMED, CANCELLED
}

class WaitingRequest {
    private Passenger passenger;
    private Train train;
    public WaitingRequest(Passenger passenger, Train train) {
        this.passenger = passenger;
        this.train = train;
    }
    public Passenger getPassenger() { return passenger; }
    public Train getTrain() { return train; }
}

public class Booking {
    private String bookingId;
    private Passenger passenger;
    private Train train;
    private int seatNumber;
    private BookingStatus status;

    public Booking(String bookingId, Passenger passenger, Train train, int seatNumber, BookingStatus status) {
        this.bookingId = bookingId;
        this.passenger = passenger;
        this.train = train;
        this.seatNumber = seatNumber;
        this.status = status;
    }
    public String getBookingId() { return bookingId; }
    public Passenger getPassenger() { return passenger; }
    public Train getTrain() { return train; }
    public int getSeatNumber() { return seatNumber; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    
    @Override
    public String toString() {
        return "PNR: " + bookingId + " | Passenger: " + passenger.getName() + " | Train: " + train.getTrainName() + " | Seat: " + seatNumber + " | Status: " + status;
    }
}
