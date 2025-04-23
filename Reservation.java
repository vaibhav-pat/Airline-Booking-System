public class Reservation {
    private static int idCounter = 1;
    private final String reservationId;
    private final Passenger passenger;
    private final Flight flight;

    public Reservation(Passenger passenger, Flight flight) {
        this.reservationId = "R" + idCounter++;
        this.passenger = passenger;
        this.flight = flight;
    }

    public Flight getFlight() {
        return flight;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId + ", Passenger: " + passenger.getName() +
                ", Flight: " + flight.getFlightId());
    }
}
