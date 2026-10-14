
package BuddyConvo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {

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
        int receiverId;

        try {
            receiverId = Integer.parseInt(request.getParameter("receiverId"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/friends");
            return;
        }

        if (receiverId == currentUser.getId()) {
            response.sendRedirect(request.getContextPath() + "/friends");
            return;
        }

        List<Map<String, Object>> messages = new ArrayList<>();

        String friendSql =
            "SELECT id, name, username FROM users WHERE id = ?";

        String relationshipSql =
            "SELECT 1 FROM friends WHERE " +
            "(user_id = ? AND friend_id = ?) OR " +
            "(user_id = ? AND friend_id = ?) LIMIT 1";

        String messageSql =
            "SELECT id, sender_id, receiver_id, message, " +
            "message_type, unlock_time, sent_at, " +
            "CASE WHEN message_type = 'TIME_CAPSULE' " +
            "AND unlock_time > NOW() AND sender_id <> ? " +
            "THEN '🔒 Time Capsule (locked)' ELSE message END AS display_message " +
            "FROM messages " +
            "WHERE (sender_id = ? AND receiver_id = ?) " +
            "OR (sender_id = ? AND receiver_id = ?) " +
            "ORDER BY sent_at ASC, id ASC";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                throw new SQLException("Database connection unavailable");
            }

            // Check that the selected account exists.
            try (PreparedStatement ps = con.prepareStatement(friendSql)) {
                ps.setInt(1, receiverId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        response.sendRedirect(
                            request.getContextPath() + "/friends");
                        return;
                    }

                    request.setAttribute("friendName", rs.getString("name"));
                    request.setAttribute("friendUsername",
                                         rs.getString("username"));
                }
            }

            // Only allow conversations between friends.
            try (PreparedStatement ps =
                     con.prepareStatement(relationshipSql)) {

                ps.setInt(1, currentUser.getId());
                ps.setInt(2, receiverId);
                ps.setInt(3, receiverId);
                ps.setInt(4, currentUser.getId());

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        response.sendRedirect(
                            request.getContextPath() + "/friends");
                        return;
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(messageSql)) {
                ps.setInt(1, currentUser.getId());
                ps.setInt(2, currentUser.getId());
                ps.setInt(3, receiverId);
                ps.setInt(4, receiverId);
                ps.setInt(5, currentUser.getId());

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> item = new HashMap<>();

                        item.put("id", rs.getInt("id"));
                        item.put("senderId", rs.getInt("sender_id"));
                        item.put("message",
                                  rs.getString("display_message"));
                        item.put("messageType",
                                  rs.getString("message_type"));
                        item.put("sentAt", rs.getTimestamp("sent_at"));
                        item.put("unlockTime",
                                  rs.getTimestamp("unlock_time"));

                        messages.add(item);
                    }
                }
            }

            request.setAttribute("messages", messages);
            request.setAttribute("receiverId", receiverId);

            request.getRequestDispatcher("/chat.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Unable to load conversation", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        String receiverText = request.getParameter("receiverId");
        String message = request.getParameter("message");

        int receiverId;

        try {
            receiverId = Integer.parseInt(receiverText);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/friends");
            return;
        }

        if (receiverId == currentUser.getId()
                || message == null || message.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath()
                    + "/chat?receiverId=" + receiverId);
            return;
        }

        String relationshipSql =
            "SELECT 1 FROM friends WHERE " +
            "(user_id = ? AND friend_id = ?) OR " +
            "(user_id = ? AND friend_id = ?) LIMIT 1";

        String insertSql =
            "INSERT INTO messages " +
            "(sender_id, receiver_id, message, message_type) " +
            "VALUES (?, ?, ?, 'TEXT')";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                throw new SQLException("Database connection unavailable");
            }

            try (PreparedStatement ps =
                     con.prepareStatement(relationshipSql)) {

                ps.setInt(1, currentUser.getId());
                ps.setInt(2, receiverId);
                ps.setInt(3, receiverId);
                ps.setInt(4, currentUser.getId());

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        response.sendRedirect(
                            request.getContextPath() + "/friends");
                        return;
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setInt(1, currentUser.getId());
                ps.setInt(2, receiverId);
                ps.setString(3, message.trim());
                ps.executeUpdate();
            }

            response.sendRedirect(request.getContextPath()
                    + "/chat?receiverId=" + receiverId);

        } catch (SQLException e) {
            throw new ServletException("Unable to send message", e);
        }
    }
}
