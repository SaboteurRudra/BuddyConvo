
package BuddyConvo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/conversations")
public class MessagesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        List<Map<String, Object>> conversations = new ArrayList<>();

        String sql =
            "SELECT u.id AS peer_id, u.name, u.username, " +
            "m.message, m.message_type, m.sent_at, m.sender_id " +
            "FROM users u " +
            "JOIN messages m ON m.id = (" +
            " SELECT m2.id FROM messages m2 " +
            " WHERE (m2.sender_id = ? AND m2.receiver_id = u.id) " +
            " OR (m2.sender_id = u.id AND m2.receiver_id = ?) " +
            " ORDER BY m2.sent_at DESC, m2.id DESC LIMIT 1" +
            ") " +
            "WHERE u.id <> ? " +
            "AND EXISTS (SELECT 1 FROM messages mx " +
            " WHERE (mx.sender_id = ? AND mx.receiver_id = u.id) " +
            " OR (mx.sender_id = u.id AND mx.receiver_id = ?)) " +
            "ORDER BY m.sent_at DESC, m.id DESC";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                throw new SQLException("Database connection unavailable");
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, currentUser.getId());
                ps.setInt(2, currentUser.getId());
                ps.setInt(3, currentUser.getId());
                ps.setInt(4, currentUser.getId());
                ps.setInt(5, currentUser.getId());

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> item = new HashMap<>();

                        item.put("peerId", rs.getInt("peer_id"));
                        item.put("name", rs.getString("name"));
                        item.put("username", rs.getString("username"));
                        item.put("message", rs.getString("message"));
                        item.put("messageType",
                                 rs.getString("message_type"));
                        item.put("sentAt", rs.getTimestamp("sent_at"));
                        item.put("senderId", rs.getInt("sender_id"));

                        conversations.add(item);
                    }
                }
            }

            request.setAttribute("conversations", conversations);
            request.getRequestDispatcher("/messages.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Unable to load conversations", e);
        }
    }
}
