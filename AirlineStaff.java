public class AirlineStaff extends User {
    private String role;

    public AirlineStaff(String userId, String name, String role) {
        super(userId, name);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public void displayInfo() {
        System.out.println("Staff: " + name + ", ID: " + userId + ", Role: " + role);
    }
}
