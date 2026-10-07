package BuddyConvo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class UserConnectivity {
    public boolean register(String name, String email,
                            String username, String password) {
        String sql = "INSERT INTO users (name, email, username, password) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                System.out.println("DATABASE CONNECTION FAILED");
                return false;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, username);
                ps.setString(4, password);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("REGISTRATION SUCCESSFUL: " + username);
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("REGISTRATION ERROR:");
            e.printStackTrace();
        }
        return false;
    }
    public User login(String username, String password) {
        String sql = "SELECT * FROM users "
                   + "WHERE username = ? AND password = ?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                System.out.println("DATABASE CONNECTION FAILED");
                return null;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setName(rs.getString("name"));
                        user.setEmail(rs.getString("email"));
                        user.setUsername(rs.getString("username"));
                        user.setPassword(rs.getString("password"));
                        // Safely read status
                        try {
                            String status = rs.getString("status");
                            if (status == null || status.trim().isEmpty()) {
                                status = "offline";
                            }
                            user.setStatus(status);
                        } catch (Exception e) {
                            // If status column doesn't exist,
                            // use offline as default.
                            user.setStatus("offline");
                        }
                        System.out.println("LOGIN SUCCESSFUL: " + username);
                        return user;
                    }
                }
            }
            System.out.println("INVALID USERNAME OR PASSWORD");
        } catch (Exception e) {
            System.out.println("LOGIN ERROR:");
            e.printStackTrace();
        }
        return null;
    }
}
