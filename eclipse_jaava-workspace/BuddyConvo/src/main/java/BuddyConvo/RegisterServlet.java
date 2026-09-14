package BuddyConvo;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("REGISTER DATA:");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Username: " + username);

        UserConnectivity connecti = new UserConnectivity();

        boolean success =
                connecti.register(name, email, username, password);

        if (success) {
            System.out.println("REGISTRATION SUCCESSFUL");
            response.sendRedirect("login.jsp");
        } else {
            System.out.println("REGISTRATION FAILED");
            response.getWriter().println(
                "Registration failed. Check Eclipse Console."
            );
        }
    }
}