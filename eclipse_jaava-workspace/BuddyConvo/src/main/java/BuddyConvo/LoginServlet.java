package BuddyConvo;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserConnectivity userConnectivity = new UserConnectivity();

        boolean validUser = userConnectivity.loginUser(username, password);

        if (validUser) {

            HttpSession session = request.getSession();
            session.setAttribute("user", username);

            response.sendRedirect("home.jsp");

        } else {
            response.sendRedirect("login.jsp?error=Invalid%20username%20or%20password");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect("login.jsp");
    }
}
