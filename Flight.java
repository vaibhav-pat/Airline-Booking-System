public class Flight {
    private String flightId;
    private String origin;
    private String destination;
    private String departureTime;
    private int capacity;
    private int availableSeats;

    public Flight(String flightId, String origin, String destination, String departureTime, int capacity) {
        this.flightId = flightId;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.capacity = capacity;
        this.availableSeats = capacity;
    }

    public String getFlightId() {
        return flightId;
    }

    public boolean bookSeat() {
        if (availableSeats > 0) {
            availableSeats--;
            return true;
        }
        return false;
    }

    public void cancelSeat() {
        if (availableSeats < capacity) {
            availableSeats++;
        }
    }

    public void displayFlightInfo() {
        System.out.println("Flight: " + flightId + ", From: " + origin + ", To: " + destination +
                ", Departure: " + departureTime + ", Available Seats: " + availableSeats);
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getAvailableSeats() {
    return availableSeats;
    }

}
