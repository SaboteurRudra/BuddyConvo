package BuddyConvo;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserConnectivity uc = new UserConnectivity();

        boolean success = uc.register(name, email, username, password);

        if (success) {

            // Login the newly registered user
            User user = uc.login(username, password);

            if (user != null) {

                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                // Directly enter BuddyConvo
                response.sendRedirect(
                    request.getContextPath() + "/home.jsp"
                );

            } else {

                // Registration worked but automatic login failed
                response.sendRedirect(
                    request.getContextPath()
                    + "/login.jsp?error=Registration%20successful%20but%20login%20failed"
                );
            }

        } else {

            // Registration failed
            response.sendRedirect(
                request.getContextPath()
                + "/login.jsp?error=Registration%20failed"
            );
        }
    }
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        response.sendRedirect(
            request.getContextPath() + "/login.jsp"
        );
    }
}
