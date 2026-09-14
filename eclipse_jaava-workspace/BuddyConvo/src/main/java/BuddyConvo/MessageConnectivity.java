package BuddyConvo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageConnectivity {
	public void sendMessage(Message msg) {
		String sql = "INSERT INTO MESSAGES "+
	                 "(sender_id, receiver_id,message,message_type,unlock_time)"+
				     "VALUES(?,?,?,?,?)";
		try (Connection con = DBConnection.getConnection();
			 PreparedStatement ps = con.prepareStatement(sql)){
			ps.setInt(1, msg.getSenderId());
			ps.setInt(2, msg.getReceiverId());
			ps.setString(3, msg.getMessage());
			ps.setString(4, msg.getMessageType());
			
			if (msg.getUnlockTime() != null) {
				ps.setTimestamp(
						5,
						Timestamp.valueOf(msg.getUnlockTime())
				);
			}else {
				ps.setTimestamp(5, null);
			}
			ps.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public List<Message> getMessages(int user1, int user2){
		List<Message>list = new ArrayList<>();
		String sql = "SELECT FROM messages "+
		             "WHERE (sender_id=? AND receiver_id=?) "+
				     "OR (sender_id=? AND receiver_id=?) "+
		             "ORDER BY sent_time";
		try (Connection con = DBConnection.getConnection();
			 PreparedStatement ps = con.prepareStatement(sql)){
			ps.setInt(1, user1);
			ps.setInt(2, user2);
			ps.setInt(3, user2);
			ps.setInt(4, user1);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Message m = new Message();
				m.setId(rs.getInt("id"));
				list.add(m);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
