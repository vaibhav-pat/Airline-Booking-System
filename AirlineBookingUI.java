import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AirlineBookingUI extends JFrame {
    private ReservationSystem reservationSystem;
    private Passenger currentPassenger;

    private JTextArea outputArea;
    private JTextField inputField;

    public AirlineBookingUI() {
        reservationSystem = new ReservationSystem();
        currentPassenger = new Passenger("P001", "Vaibhav", "X12345");

        // Add some flights
        reservationSystem.addFlight(new Flight("F101", "Delhi", "Mumbai", "10:00 AM", 5));
        reservationSystem.addFlight(new Flight("F102", "Mumbai", "Bangalore", "3:00 PM", 3));

        setTitle("Airline Booking System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);
        add(scroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        JButton viewBtn = new JButton("View Flights");
        JButton bookBtn = new JButton("Book Flight");
        JButton cancelBtn = new JButton("Cancel Reservation");
        JButton resBtn = new JButton("View Reservations");
        JButton exitBtn = new JButton("Exit");

        buttonPanel.add(viewBtn);
        buttonPanel.add(bookBtn);
        buttonPanel.add(cancelBtn);
        buttonPanel.add(resBtn);
        buttonPanel.add(exitBtn);

        add(buttonPanel, BorderLayout.NORTH);

        inputField = new JTextField();
        add(inputField, BorderLayout.SOUTH);

        // Button Actions
        viewBtn.addActionListener(e -> {
            outputArea.setText("");
            for (Flight flight : reservationSystem.getFlights()) {
                outputArea.append(flight.getFlightId() + ": " + flight.getOrigin() + " → " +
                        flight.getDestination() + " at " + flight.getDepartureTime() +
                        " | Seats left: " + flight.getAvailableSeats() + "\n");
            }
        });

        bookBtn.addActionListener(e -> {
            String fid = JOptionPane.showInputDialog(this, "Enter Flight ID to Book:");
            Reservation res = reservationSystem.makeReservation(currentPassenger, fid);
            if (res != null) {
                outputArea.setText("✅ Booking Successful!\nReservation ID: " + res.getReservationId());
            } else {
                outputArea.setText("❌ Booking Failed! Flight may not exist or is full.");
            }
        });

        cancelBtn.addActionListener(e -> {
            String rid = JOptionPane.showInputDialog(this, "Enter Reservation ID to Cancel:");
            reservationSystem.cancelReservation(rid);
            outputArea.setText("Attempted to cancel reservation ID: " + rid);
        });

        resBtn.addActionListener(e -> {
            outputArea.setText("");
            for (Reservation res : reservationSystem.getReservations()) {
                outputArea.append("Reservation: " + res.getReservationId() + " - Flight: " +
                        res.getFlight().getFlightId() + " - Passenger: " + res.getPassenger().getName() + "\n");
            }
        });

        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AirlineBookingUI::new);
    }
}
