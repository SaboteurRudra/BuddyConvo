package BuddyConvo;
import BuddyConvo.User;
import java.sql.*;

public class UserConnectivity {
	
	public boolean register(String name, String email,
            String username, String password) {

String sql = "INSERT INTO users (name, email, username, password) " +
     "VALUES (?, ?, ?, ?)";

try (Connection con = DBConnection.getConnection();
PreparedStatement ps = con.prepareStatement(sql)) {

ps.setString(1, name);
ps.setString(2, email);
ps.setString(3, username);
ps.setString(4, password);

ps.executeUpdate();

return true;

} catch (Exception e) {
System.out.println("REGISTRATION ERROR:");
e.printStackTrace();
return false;
}
}
	public User login(String username,
			          String password) {
		String sql = "SELECT * from users where username =? AND password =?";
		try (Connection con = DBConnection.getConnection();
			 PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return new User(
						rs.getInt("id"),
						rs.getString("username"),
						rs.getString("password"),
						rs.getString("status")
				);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	public void updateStatus(int userId,
			                 String status) {
		String sql = "UPDATE users SET status=? WHERE id=?";
		try(Connection con = DBConnection.getConnection();
			PreparedStatement ps = con.prepareStatement(sql)){
		   ps.setString(1, status);
		   ps.setInt(2, userId);
		   ps.executeUpdate();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
