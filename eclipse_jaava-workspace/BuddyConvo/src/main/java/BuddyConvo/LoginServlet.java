package BuddyConvo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("LOGIN ATTEMPT: " + username);

        UserConnectivity connectivity = new UserConnectivity();
        User user = connectivity.login(username, password);

        if (user != null) {

            System.out.println("LOGIN SUCCESSFUL: " + username);

            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);

            response.sendRedirect(
                request.getContextPath() + "/home.jsp"
            );

        } else {

            System.out.println("LOGIN FAILED: " + username);

            response.sendRedirect(
                request.getContextPath() + "/login.jsp?error=true"
            );
        }
    }
}

   
