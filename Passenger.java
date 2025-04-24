public class Passenger extends User {
    private String passportNumber;
    private String id;
    private String bookingHistory;

    public Passenger(String name, String email, String phone,String password) {
        super(name,email,phone ,password);
        this.passportNumber = passportNumber;
        this.bookingHistory = "";
    }

    public String getId() {
        return id;
    }

    public String getPassportNumber() {
        return passportNumber;
    }
    // get setter
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

   @Override
    public void displayInfo() {
        System.out.println("Passenger ID: " + getUserId());
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Phone: " + getPhone());
        System.out.println("Booking History: " + bookingHistory);
    }

    // Example method to update booking history
    public void addBooking(String bookingDetails) {
        this.bookingHistory += bookingDetails + "\n";
    }
}
