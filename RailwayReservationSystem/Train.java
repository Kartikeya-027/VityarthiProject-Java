
public class Train {
    private int trainNo;
    private String trainName;
    private String source;
    private String destination;
    private int totalSeats;
    private boolean[] occupiedSeats;

    public Train(int trainNo, String trainName, String source, String destination, int totalSeats) {
        this.trainNo = trainNo;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.occupiedSeats = new boolean[totalSeats];
    }
    public int getTrainNo() { return trainNo; }
    public String getTrainName() { return trainName; }
    public int getTotalSeats() { return totalSeats; }
    
    public int reserveSeat() {
        for (int i = 0; i < occupiedSeats.length; i++) {
            if (!occupiedSeats[i]) {
                occupiedSeats[i] = true;
                return i + 1;
            }
        }
        return -1;
    }
    
    public void releaseSeat(int seatNumber) {
        if (seatNumber >= 1 && seatNumber <= totalSeats) {
            occupiedSeats[seatNumber - 1] = false;
        }
    }
    
    public int getAvailableSeats() {
        int count = 0;
        for (boolean occupied : occupiedSeats) {
            if (!occupied) count++;
        }
        return count;
    }
    
    @Override
    public String toString() {
        return trainNo + " | " + trainName + " | " + source + " -> " + destination + " | Available Seats: " + getAvailableSeats() + "/" + totalSeats;
    }
}
