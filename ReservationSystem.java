import java.util.*;

public class ReservationSystem {
    private List<Flight> flights = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    public Flight findFlightById(String flightId) {
        for (Flight f : flights) {
            if (f.getFlightId().equals(flightId)) {
                return f;
            }
        }
        return null;
    }

    public Reservation makeReservation(Passenger passenger, String flightId) {
        Flight flight = findFlightById(flightId);
        if (flight != null && flight.bookSeat()) {
            Reservation reservation = new Reservation(passenger, flight);
            reservations.add(reservation);
            return reservation;
        }
        return null;
    }

    public void cancelReservation(String reservationId) {
        Iterator<Reservation> iterator = reservations.iterator();
        while (iterator.hasNext()) {
            Reservation res = iterator.next();
            if (res.getReservationId().equals(reservationId)) {
                res.getFlight().cancelSeat();
                iterator.remove();
                System.out.println("Reservation cancelled: " + reservationId);
                return;
            }
        }
        System.out.println("Reservation ID not found.");
    }

    public void listReservations() {
        for (Reservation res : reservations) {
            res.displayReservation();
        }
    }

    public void listFlights() {
        for (Flight flight : flights) {
            flight.displayFlightInfo();
        }
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public List<Flight> getFlights() {
        return flights;
    }
}
