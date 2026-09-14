package BuddyConvo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("REGISTER ATTEMPT");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Username: " + username);

        UserConnectivity connectivity = new UserConnectivity();

        boolean success = connectivity.register(
                name,
                email,
                username,
                password
        );

        if (success) {

            System.out.println("REGISTRATION SUCCESSFUL");

            // Login the newly registered user automatically
            User user = connectivity.login(username, password);

            if (user != null) {

                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);

                response.sendRedirect(
                    request.getContextPath() + "/home.jsp"
                );

            } else {

                response.sendRedirect(
                    request.getContextPath() + "/login.jsp?error=loginfailed"
                );
            }

        } else {

            System.out.println("REGISTRATION FAILED");

            response.sendRedirect(
                request.getContextPath() + "/login.jsp?error=registerfailed"
            );
        }
    }
}
