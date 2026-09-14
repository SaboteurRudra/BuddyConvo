package BuddyConvo;
import java.time.LocalDateTime;

public class Message {
	private int id;
	private int senderId;
	private int receiverId;
	private String message;
	private String messageType;
	private LocalDateTime unlockTime;
	private LocalDateTime expireTime;
	private String status;
	
	public Message() {
	}
	public Message(int senderId,
			       int receiverId,
			       String message,
			       String messageType,
			       LocalDateTime unlockTime) {
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.message = message;
		this.messageType = messageType;
		this.unlockTime = unlockTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSenderId() {
		return senderId;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	public int getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String message) {
		this.messageType = messageType;
	}
	public LocalDateTime getUnlockTime() {
		return unlockTime;
	}
	public void setUnlockTime(LocalDateTime unlockTime) {
		this.unlockTime = unlockTime;
	}
}
