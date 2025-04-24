public class AirlineStaff extends User {
    private String role;

    public AirlineStaff(String name, String email, String phone , String password, String role) {
        super(name,email,phone,password);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public void displayInfo() {
        System.out.println("Staff ID: " + getUserId());
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Phone: " + getPhone());
        System.out.println("Role: " + role);
    }
}
