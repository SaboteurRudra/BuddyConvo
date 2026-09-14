package BuddyConvo;
import java.time.LocalDateTime;

public class TimeCapsule {
	private String message;
	private LocalDateTime unlockTime;
	
	public TimeCapsule(String message, LocalDateTime unlockTime) {
		this.message = message;
		this.unlockTime = unlockTime;
	}
	public boolean isUnlocked() {
		return LocalDateTime.now().isAfter(unlockTime);
	}
	public String getMessage() {
		if (isUnlocked()) {
			return message;
		}
		return "Message LOCKED...";
	}
	public LocalDateTime getUnlockTime() {
		return unlockTime;
	}
}
