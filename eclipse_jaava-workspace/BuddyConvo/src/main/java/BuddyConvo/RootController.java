package BuddyConvo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String openLogin() {
        return "redirect:/login.jsp";
    }
}
