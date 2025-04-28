import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class AirlineBookingUI extends JFrame {
    private ReservationSystem reservationSystem;
    private Passenger currentPassenger;
    private JComboBox<String> flightDropdown;
    private JTextArea outputArea;

    private JButton addFlightButton;
    private JButton bookButton;
    private JButton viewButton;
    private JButton cancelButton;
    private JButton logoutButton;
    private JButton loginSwitchButton;
    private JLabel statusLabel;
    private static final String PASSENGER_DATA_FILE = "passengers.dat";
    private static final String STAFF_DATA_FILE = "staff.dat";
    private List<Passenger> passengers = DataManager.loadPassengers(PASSENGER_DATA_FILE);
    private List<AirlineStaff> staffMembers = DataManager.loadStaff(STAFF_DATA_FILE);



    public AirlineBookingUI() {
        reservationSystem = new ReservationSystem();
        currentPassenger = null;
        reservationSystem.setCurrentUser(null);

        // Dummy flights
        reservationSystem.addFlight(new Flight("F101", "Delhi", "Mumbai", "10:00 AM", 5));
        reservationSystem.addFlight(new Flight("F102", "Mumbai", "Bangalore", "3:00 PM", 3));
        reservationSystem.addFlight(new Flight("F103", "Hyderabad", "Mysore" ,"2:15 PM", 19));
        reservationSystem.addFlight(new Flight("F104", "OOty", "Prayagraj" ,"2:45 PM", 46));
        reservationSystem.addFlight(new Flight("F105", "Hyderabad", "Mumbai" ,"12:15 AM", 1));
        reservationSystem.addFlight(new Flight("F106", "Indore", "Delhi" ,"9:35 PM", 24));
        reservationSystem.addFlight(new Flight("F107", "Hyderabad", "Mysore" ,"2:15 PM", 19));

        setTitle("Airline Booking System");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // UI components
        JLabel flightLabel = new JLabel("Select Flight:");
        flightDropdown = new JComboBox<>();
        updateFlightDropdown();

        bookButton = new JButton("Book Ticket");
        viewButton = new JButton("View Reservations");
        cancelButton = new JButton("Cancel Reservation");
        addFlightButton = new JButton("Add Flight ✈️");
        JButton ViewFlightButton = new JButton("View Flights ");
        loginSwitchButton = new JButton("Login / Register 👤");
        logoutButton = new JButton("Logout 🔓");
        JButton managePassengersButton = new JButton("Manage Passengers 👥");
        JButton manageFlightPassengersButton = new JButton("Manage Flight Passengers ✈️");
        JButton managePassengerButton = new JButton("Manage Flight Passengers");

        outputArea = new JTextArea(15, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 2, 10, 10));
        topPanel.add(flightLabel);
        topPanel.add(flightDropdown);
        topPanel.add(bookButton);
        topPanel.add(viewButton);
        topPanel.add(cancelButton);
        topPanel.add(addFlightButton);
        topPanel.add(ViewFlightButton);
        topPanel.add(loginSwitchButton);
        topPanel.add(logoutButton);
        topPanel.add(managePassengersButton);
        topPanel.add(manageFlightPassengersButton);
        topPanel.add(managePassengerButton);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);

        statusLabel = new JLabel("🚪 Not Logged In");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        mainPanel.add(statusLabel, BorderLayout.SOUTH);


        // Actions
        bookButton.addActionListener(e -> bookTicket());
        viewButton.addActionListener(e -> viewReservations());
        cancelButton.addActionListener(e -> cancelReservation());
        addFlightButton.addActionListener(e -> addFlightUI());
        ViewFlightButton.addActionListener(e -> viewAllFlights());
        loginSwitchButton.addActionListener(e -> loginOrRegisterUI());
        logoutButton.addActionListener(e -> logout());
        managePassengersButton.addActionListener(e -> openPassengerManagementUI());
        manageFlightPassengersButton.addActionListener(e -> openFlightPassengerManager());
        managePassengerButton.addActionListener(e -> showPassengersForFlight());

        bookButton.setToolTipText("Book a flight (Passenger only)");
        addFlightButton.setToolTipText("Add a new flight (Staff only)");

        updateUIBasedOnUserRole();  // This will hide/show buttons based on currentUser

    }

    private void updateUIBasedOnUserRole() {
        User user = reservationSystem.getCurrentUser();

        boolean isLoggedIn = user != null;
        boolean isPassenger = user instanceof Passenger;
        boolean isStaff = user instanceof AirlineStaff;

        bookButton.setVisible(isPassenger);
        viewButton.setVisible(isPassenger);
        cancelButton.setVisible(isPassenger);
        addFlightButton.setVisible(isStaff);

        logoutButton.setVisible(isLoggedIn);
        loginSwitchButton.setVisible(!isLoggedIn);

        if (isLoggedIn) {
            String role = isPassenger ? "Passenger" : "Staff";
            statusLabel.setText("👤 Logged in as: " + user.getName() + " (" + role + ")");
        } else {
            statusLabel.setText("🚪 Not Logged In");
        }
    }



    private void updateFlightDropdown() {
        flightDropdown.removeAllItems();
        for (Flight flight : reservationSystem.getFlights()) {
            String display = flight.getFlightId() + " | " + flight.getSource() + " → " +
                    flight.getDestination() + " @ " + flight.getDepartureTime() +
                    " [" + flight.getAvailableSeats() + " seats]";
            flightDropdown.addItem(display);
        }
    }

    private void bookTicket() {
        if (!isPassengerLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Login or register as passenger to book a flight. ", "Access Denied", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ✅ Get the current logged-in user and cast it to Passenger
        Passenger passenger = (Passenger) reservationSystem.getCurrentUser();

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

        // ✅ Use the correct Passenger object here
        Reservation res = reservationSystem.makeReservation(passenger, selectedFlight.getFlightId());
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
        if (!isPassengerLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Login or register as passenger to cancel a flight.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            return;
        }
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
        DataManager.savePassengers(passengers, PASSENGER_DATA_FILE);
    }

    
    private void addFlightUI() {
        if (!isStaffLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Login as Airline Staff to add flight.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField flightNoField = new JTextField();
        JTextField sourceField = new JTextField();
        JTextField destinationField = new JTextField();
        JTextField timeField = new JTextField();
        JTextField seatsField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Flight Number:"));
        panel.add(flightNoField);
        panel.add(new JLabel("Source:"));
        panel.add(sourceField);
        panel.add(new JLabel("Destination:"));
        panel.add(destinationField);
        panel.add(new JLabel("Departure Time:"));
        panel.add(timeField);
        panel.add(new JLabel("Available Seats:"));
        panel.add(seatsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Flight", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String flightId = flightNoField.getText().trim();
            String source = sourceField.getText().trim();
            String destination = destinationField.getText().trim();
            String departureTime = timeField.getText().trim();
            String seatsText = seatsField.getText().trim();

            if (flightId.isEmpty() || source.isEmpty() || destination.isEmpty() || seatsText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int availableSeats = Integer.parseInt(seatsText);
                Flight flight = new Flight(flightId, source, destination, departureTime, availableSeats);
                reservationSystem.addFlight(flight);
                outputArea.append("✅ Flight added: " + flightId + " (" + source + " → " + destination + ")\n");
                updateFlightDropdown(); // Optional: refresh dropdowns
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Available Seats must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewAllFlights() {
        List<Flight> allFlights = reservationSystem.getAllFlights();

        if (allFlights.isEmpty()) {
            outputArea.append("No flights available.\n");
            return;
        }

        outputArea.append("✈️ Available Flights:\n");
        for (Flight flight : allFlights) {
            outputArea.append("• " + flight.getFlightId() + " (" 
                + flight.getSource() + " → " + flight.getDestination() 
                + ") - Seats: " + flight.getAvailableSeats() + "\n");
        }
    }

    private void loginOrRegisterUI() {
        String[] options = {"Login", "Register"};
        int action = JOptionPane.showOptionDialog(this, "Choose action:", "Login/Register",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (action == 0) {
            loginUI();
        } else if (action == 1) {
            registerUI();
        }
    }

    private void loginUI() {
        String[] roles = {"Passenger", "Airline Staff"};
        String role = (String) JOptionPane.showInputDialog(this, "Select Role:",
                "Login As", JOptionPane.PLAIN_MESSAGE, null, roles, roles[0]);

        String name = JOptionPane.showInputDialog(this, "Enter your name:");
        String email = JOptionPane.showInputDialog(this, "Enter your email:");
        JPasswordField passwordField = new JPasswordField();
        int result = JOptionPane.showConfirmDialog(this, passwordField, "Enter your password:", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());
        }


        if (name == null || name.trim().isEmpty()) return;

        User user = null;

        if ("Passenger".equals(role)) {
            for (Passenger p : reservationSystem.getPassengers()) {
                if (p.getName().equalsIgnoreCase(name.trim())) {
                    user = p;
                    break;
                }
            }
        } else {
            for (AirlineStaff s : reservationSystem.getStaffMembers()) {
                if (s.getName().equalsIgnoreCase(name.trim())) {
                    user = s;
                    break;
                }
            }
        }

        if (user != null) {
            reservationSystem.setCurrentUser(user);
            outputArea.append("🔐 Logged in as " + user.getName() + " (" + role + ")\n");
            updateUIBasedOnUserRole();
        } else {
            JOptionPane.showMessageDialog(this, "User not found. Try registering.");
        }
    }

    private void registerUI() {
        String[] roles = {"Passenger", "Airline Staff"};
        String role = (String) JOptionPane.showInputDialog(this, "Select Role:",
                "Register As", JOptionPane.PLAIN_MESSAGE, null, roles, roles[0]);

        String name = JOptionPane.showInputDialog(this, "Enter your name:");
        String email = JOptionPane.showInputDialog(this, "Enter your email:");
        String phone = JOptionPane.showInputDialog(this, "Enter your phone:");
        String password = JOptionPane.showInputDialog(this, "Enter your password:");

        if (name == null || email == null || phone == null || password == null ||
            name.trim().isEmpty() || email.trim().isEmpty() || phone.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        if ("Passenger".equals(role)) {
            Passenger p = new Passenger(name.trim(), email.trim(), phone.trim(), password.trim());
            passengers.add(p);
            DataManager.savePassengers(passengers, PASSENGER_DATA_FILE);
            reservationSystem.registerPassenger(p);
            reservationSystem.setCurrentUser(p);

        } else {
            AirlineStaff staff = new AirlineStaff(name.trim(), email.trim(), phone.trim(), password.trim(),role.trim());
            staffMembers.add(staff);
            DataManager.saveStaff(staffMembers, STAFF_DATA_FILE);
            reservationSystem.registerStaff(staff);
            reservationSystem.setCurrentUser(staff);

        }

        outputArea.append("🆕 Registered & logged in as: " + name + " (" + role + ")\n");
        updateUIBasedOnUserRole();
    }

    private boolean isStaffLoggedIn() {
        User user = reservationSystem.getCurrentUser();
        return user instanceof AirlineStaff;
    }

    private boolean isPassengerLoggedIn() {
        User user = reservationSystem.getCurrentUser();
        return user instanceof Passenger;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(AirlineBookingUI::new);
    }

    private void logout() {
        User currentUser = reservationSystem.getCurrentUser();
        if (currentUser != null) {
            outputArea.append("👋 Logged out: " + currentUser.getName() + "\n");
            reservationSystem.setCurrentUser(null);
            updateUIBasedOnUserRole();
        } else {
            outputArea.append("⚠️ No user is currently logged in.\n");
        }
    }

    private void openPassengerManagementUI() {
        if (!isStaffLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Only airline staff can manage passenger data.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Passenger> passengers = reservationSystem.getAllPassengers();
        if (passengers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No registered passengers to display.");
            return;
        }

        String[] names = passengers.stream().map(Passenger::getName).toArray(String[]::new);

        String selectedName = (String) JOptionPane.showInputDialog(
                this,
                "Select a passenger to manage:",
                "Manage Passenger",
                JOptionPane.PLAIN_MESSAGE,
                null,
                names,
                names[0]);

        if (selectedName != null) {
            Passenger p = reservationSystem.findPassengerByName(selectedName);
            if (p != null) {
                showPassengerEditDialog(p);
            }
        }
    }

    private void showPassengerEditDialog(Passenger passenger) {
        JTextField nameField = new JTextField(passenger.getName());
        JTextField emailField = new JTextField(passenger.getEmail());
        JTextField phoneField = new JTextField(passenger.getPhone());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Passenger Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            passenger.setName(nameField.getText().trim());
            passenger.setEmail(emailField.getText().trim());
            passenger.setPhone(phoneField.getText().trim());
            outputArea.append("✏️ Updated passenger: " + passenger.getName() + "\n");
        }
    }
    private void openFlightPassengerManager() {
        if (!isStaffLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Access restricted to staff only.", "Unauthorized", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Flight> flights = reservationSystem.getAllFlights();
        if (flights.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No flights available.");
            return;
        }

        String[] flightNames = flights.stream()
            .map(f -> f.getFlightId() + " - " + f.getSource() + " to " + f.getDestination())
            .toArray(String[]::new);

        String selectedFlightInfo = (String) JOptionPane.showInputDialog(
                this,
                "Select a flight:",
                "Flight List",
                JOptionPane.PLAIN_MESSAGE,
                null,
                flightNames,
                flightNames[0]);

        if (selectedFlightInfo != null) {
            String flightId = selectedFlightInfo.split(" - ")[0];
            List<Passenger> passengersOnFlight = reservationSystem.getPassengersByFlight(flightId);

            if (passengersOnFlight.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No passengers booked on this flight.");
            } else {
                showFlightPassengersDialog(passengersOnFlight);
            }
        }
    }

    private void showFlightPassengersDialog(List<Passenger> passengerList) {
        StringBuilder info = new StringBuilder("👥 Passengers on this flight:\n\n");
        for (Passenger p : passengerList) {
            info.append("Name: ").append(p.getName()).append("\n");
            info.append("Email: ").append(p.getEmail()).append("\n");
            info.append("Phone: ").append(p.getPhone()).append("\n");
            info.append("-----------------------------\n");
        }

        JTextArea textArea = new JTextArea(info.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Passenger List", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showPassengersForFlight() {
        if (!isStaffLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Only airline staff can view this.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Flight> allFlights = reservationSystem.getAllFlights();
        if (allFlights.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No flights available.");
            return;
        }

        String[] flightOptions = allFlights.stream()
            .map(f -> f.getFlightId() + " | " + f.getSource() + " → " + f.getDestination())
            .toArray(String[]::new);

        String selectedFlight = (String) JOptionPane.showInputDialog(
            this,
            "Select flight to view booked passengers:",
            "Flight Selection",
            JOptionPane.PLAIN_MESSAGE,
            null,
            flightOptions,
            flightOptions[0]
        );

        if (selectedFlight != null) {
            String flightId = selectedFlight.split(" \\| ")[0].trim();
            List<Passenger> passengers = reservationSystem.getPassengersByFlight(flightId);

            if (passengers.isEmpty()) {
                outputArea.append("👥 No passengers found for flight " + flightId + "\n");
            } else {
                String[] passengerOptions = passengers.stream()
                    .map(p -> p.getName() + " (" + p.getEmail() + ")")
                    .toArray(String[]::new);

                String selectedPassenger = (String) JOptionPane.showInputDialog(
                    this,
                    "Select passenger to edit:",
                    "Passenger Selection",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    passengerOptions,
                    passengerOptions[0]
                );

                if (selectedPassenger != null) {
                    int index = -1;
                    for (int i = 0; i < passengerOptions.length; i++) {
                        if (passengerOptions[i].equals(selectedPassenger)) {
                            index = i;
                            break;
                        }
                    }

                    if (index != -1) {
                        Passenger passenger = passengers.get(index);
                        editPassengerDetails(passenger);
                    }
                }
            }
        }
    }
    private void editPassengerDetails(Passenger passenger) {
        JTextField nameField = new JTextField(passenger.getName());
        JTextField emailField = new JTextField(passenger.getEmail());
        JTextField phoneField = new JTextField(passenger.getPhone());


        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);

        int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Edit Passenger Details",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            passenger.setName(nameField.getText().trim());
            passenger.setEmail(emailField.getText().trim());
            passenger.setPhone(phoneField.getText().trim());

            outputArea.append("✅ Passenger details updated successfully.\n");
        } else {
            outputArea.append("✏️ Edit cancelled.\n");
        }
        DataManager.savePassengers(passengers, PASSENGER_DATA_FILE);
    }
    
}