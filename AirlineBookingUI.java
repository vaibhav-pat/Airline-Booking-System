import java.awt.*;
import java.util.Map;
import javax.swing.*;

public class AirlineBookingUI extends JFrame {
    private ReservationSystem reservationSystem;
    private Passenger currentPassenger;
    private JComboBox<String> flightDropdown;
    private JTextArea outputArea;

    public AirlineBookingUI() {
        reservationSystem = new ReservationSystem();
        currentPassenger = new Passenger("P001", "Vaibhav", "X12345");

        // Dummy flights
        reservationSystem.addFlight(new Flight("F101", "Delhi", "Mumbai", "10:00 AM", 5));
        reservationSystem.addFlight(new Flight("F102", "Mumbai", "Bangalore", "3:00 PM", 3));

        setTitle("Airline Booking System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // UI components
        JLabel flightLabel = new JLabel("Select Flight:");
        flightDropdown = new JComboBox<>();
        updateFlightDropdown();

        JButton bookButton = new JButton("Book Ticket");
        JButton viewButton = new JButton("View Reservations");
        JButton cancelButton = new JButton("Cancel Reservation");

        outputArea = new JTextArea(15, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Layout
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 2, 10, 10));
        topPanel.add(flightLabel);
        topPanel.add(flightDropdown);
        topPanel.add(bookButton);
        topPanel.add(viewButton);
        topPanel.add(cancelButton);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);

        // Actions
        bookButton.addActionListener(e -> bookTicket());
        viewButton.addActionListener(e -> viewReservations());
        cancelButton.addActionListener(e -> cancelReservation());
    }

    private void updateFlightDropdown() {
        flightDropdown.removeAllItems();
        for (Flight flight : reservationSystem.getFlights()) {
            String display = flight.getFlightId() + " | " + flight.getOrigin() + " → " +
                    flight.getDestination() + " @ " + flight.getDepartureTime() +
                    " [" + flight.getAvailableSeats() + " seats]";
            flightDropdown.addItem(display);
        }
    }

    private void bookTicket() {
        int selectedIndex = flightDropdown.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "No flight selected!");
            return;
        }
        Flight selectedFlight = reservationSystem.getFlights().get(selectedIndex);

        if (selectedFlight.getAvailableSeats() <= 0) {
            JOptionPane.showMessageDialog(this, "Sorry, no seats available!");
            return;
        }

        Reservation res = reservationSystem.makeReservation(currentPassenger, selectedFlight.getFlightId());
        if (res != null) {
            outputArea.append("✅ Booking Confirmed: " + res.getReservationId() + "\n");
            updateFlightDropdown();
        } else {
            JOptionPane.showMessageDialog(this, "Booking failed!");
        }
    }

    private void viewReservations() {
        Map<String, String> summaryMap = reservationSystem.getReservationSummaryMap();

        if (summaryMap.isEmpty()) {
            outputArea.append("No reservations found.\n");
            return;
        }

        outputArea.append("📋 Reservations:\n");
        for (String summary : summaryMap.values()) {
            outputArea.append("• " + summary + "\n");
        }
    }

    private void cancelReservation() {
        Map<String, String> summaryMap = reservationSystem.getReservationSummaryMap();

        if (summaryMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No reservations available to cancel.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Get the summaries (values) to show in the dropdown
        Object[] options = summaryMap.values().toArray();

        // Show selection dialog
        String selectedSummary = (String) JOptionPane.showInputDialog(
            this,
            "Select a reservation to cancel:",
            "Cancel Reservation",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        );

        if (selectedSummary != null) {
            // Find reservationId by value (reverse map)
            String selectedId = summaryMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(selectedSummary))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

            if (selectedId != null && reservationSystem.cancelReservation(selectedId)) {
                outputArea.append("❌ Reservation " + selectedSummary + " cancelled.\n");
                updateFlightDropdown(); // Update UI if needed
            } else {
                JOptionPane.showMessageDialog(this, "Failed to cancel reservation.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AirlineBookingUI::new);
    }
}
