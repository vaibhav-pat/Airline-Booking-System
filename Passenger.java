public class Passenger extends User {
    private String passportNumber;

    public Passenger(String userId, String name, String passportNumber) {
        super(userId, name);
        this.passportNumber = passportNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    @Override
    public void displayInfo() {
        System.out.println("Passenger: " + name + ", ID: " + userId + ", Passport: " + passportNumber);
    }
}
