package BuddyConvo;

public class User {
	private int id;
	private String username;
	private String password;
	private String status;
	public User() {
	}
	public User(int id, String username, String password, String status) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getStatus() {
		return status;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
