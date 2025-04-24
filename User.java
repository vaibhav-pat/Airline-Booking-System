import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class User {
    protected String userId;
    protected String name;
    protected String email;
    protected String phone;
    protected String password;

    public User(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = hashPassword(password);
        this.userId = generateUserId();
    }

    private String generateUserId() {
        return "U" + System.currentTimeMillis(); 
    }

    public boolean checkPassword(String input) {
        return password.equals(hashPassword(input));
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 hashing failed", e);
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public abstract void displayInfo();
}
