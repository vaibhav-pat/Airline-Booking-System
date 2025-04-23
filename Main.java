import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ReservationSystem system = new ReservationSystem();
        Scanner sc = new Scanner(System.in);

        // Add some flights
        system.addFlight(new Flight("F101", "Delhi", "Mumbai", "10:00 AM", 5));
        system.addFlight(new Flight("F102", "Mumbai", "Bangalore", "3:00 PM", 3));

        // Add a passenger
        Passenger passenger = new Passenger("P001", "Vaibhav", "X12345");

        while (true) {
            System.out.println("\n1. View Flights\n2. Make Reservation\n3. Cancel Reservation\n4. View Reservations\n5. Exit");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    system.listFlights();
                    break;
                case 2:
                    System.out.print("Enter Flight ID: ");
                    String fid = sc.nextLine();
                    Reservation res = system.makeReservation(passenger, fid);
                    if (res != null) {
                        System.out.println("Reservation successful!");
                        res.displayReservation();
                    } else {
                        System.out.println("Reservation failed.");
                    }
                    break;
                case 3:
                    System.out.print("Enter Reservation ID to cancel: ");
                    String rid = sc.nextLine();
                    system.cancelReservation(rid);
                    break;
                case 4:
                    system.listReservations();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
