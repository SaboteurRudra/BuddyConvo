package BuddyConvo;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/chat")
public class ChatController {
	
	@GetMapping("/Hello")
	public String Hello() {
		return "BUDDY CHAT SERVER RUNNING";
	}
	@GetMapping("/Status")
	public String Status() {
		return "ONLINE";
	}
}
