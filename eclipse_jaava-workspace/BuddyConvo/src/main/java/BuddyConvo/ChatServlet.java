package BuddyConvo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User sender = (User) session.getAttribute("user");

        String receiverIdText = request.getParameter("receiverId");
        String message = request.getParameter("message");
        String unlockTime = request.getParameter("unlockTime");
        if (receiverIdText == null ||
            message == null ||
            message.trim().isEmpty()) {

            response.sendRedirect("home.jsp?error=message");
            return;
        }
        try {

            int receiverId = Integer.parseInt(receiverIdText);

            String sql;

            if (unlockTime != null && !unlockTime.trim().isEmpty()) {

                sql = "INSERT INTO messages " +
                      "(sender_id, receiver_id, message, message_type, unlock_time) " +
                      "VALUES (?, ?, ?, 'TIME_CAPSULE', ?)";

            } else {

                sql = "INSERT INTO messages " +
                      "(sender_id, receiver_id, message, message_type) " +
                      "VALUES (?, ?, ?, 'TEXT')";
            }
            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                if (con == null) {
                    response.sendRedirect("home.jsp?error=database");
                    return;
                }

                ps.setInt(1, sender.getId());
                ps.setInt(2, receiverId);
                ps.setString(3, message);

                if (unlockTime != null && !unlockTime.trim().isEmpty()) {
                    ps.setString(4, unlockTime.replace("T", " "));
                }

                ps.executeUpdate();
            }

            response.sendRedirect(
                    "home.jsp?receiverId=" + receiverId
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    "home.jsp?error=send"
            );
        }
    }
}
