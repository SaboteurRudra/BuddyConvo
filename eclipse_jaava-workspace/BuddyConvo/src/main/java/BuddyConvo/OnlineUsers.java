package BuddyConvo;
import java.util.HashMap;
import java.util.Map;

public class OnlineUsers {
	private static Map<Integer, String> users = new HashMap<>();
	public static void addUser(int id, String username) {
		users.put(id, username);
	}
	public static void removeUser(int id) {
		users.remove(id);
	}
	public static boolean isOnline(int id) {
		return users.containsKey(id);
	}
	public static Map<Integer, String> getUsers(){
		return users;
	}
}
