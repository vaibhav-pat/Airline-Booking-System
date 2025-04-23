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

    public boolean cancelReservation(String reservationId) {
        Reservation reservationToCancel = null;
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId().equals(reservationId)) {
                reservationToCancel = reservation;
                break;
            }
        }

        if (reservationToCancel != null) {
            reservations.remove(reservationToCancel);
            return true; // Successfully cancelled
        } else {
            return false; // Reservation not found
        }
    }

    public List<String> getAllReservationIds() {
        List<String> ids = new ArrayList<>();
        for (Reservation res : reservations) {
            ids.add(res.getReservationId());
        }
        return ids;
    }

    public void listReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            for (Reservation res : reservations) {
                System.out.println("Reservation ID: " + res.getReservationId()
                    + ", Passenger: " + res.getPassenger().getName()
                    + ", Flight: " + res.getFlight().getFlightId());
            }
        }
    }

    public List<String> getAllReservationSummaries() {
        List<String> summaries = new ArrayList<>();
        for (Reservation res : reservations) {
            String flightInfo = res.getFlight().getFlightId() + 
                " (" + res.getFlight().getSource() + " → " + res.getFlight().getDestination() + ")";
            summaries.add(flightInfo);
        }
        return summaries;
    }

    public Map<String, String> getReservationSummaryMap() {
        Map<String, String> map = new LinkedHashMap<>(); // Keeps insertion order
        for (Reservation res : reservations) {
            String summary = res.getFlight().getFlightId() + 
                " (" + res.getFlight().getSource() + " → " + res.getFlight().getDestination() + ")";
            map.put(res.getReservationId(), summary);
        }
        return map;
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
