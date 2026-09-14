package BuddyConvo;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/messages")
public class MessagesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print("[]");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        String receiverText = request.getParameter("receiverId");

        if (receiverText == null) {
            response.getWriter().print("[]");
            return;
        }

        int receiverId = Integer.parseInt(receiverText);

        String sql =
                "SELECT id, sender_id, receiver_id, message, " +
                "message_type, unlock_time, sent_at " +
                "FROM messages " +
                "WHERE (sender_id = ? AND receiver_id = ?) " +
                "OR (sender_id = ? AND receiver_id = ?) " +
                "ORDER BY sent_at ASC";

        StringBuilder json = new StringBuilder("[");
        boolean first = true;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (con == null) {
                response.getWriter().print("[]");
                return;
            }

            ps.setInt(1, currentUser.getId());
            ps.setInt(2, receiverId);
            ps.setInt(3, receiverId);
            ps.setInt(4, currentUser.getId());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                if (!first) {
                    json.append(",");
                }

                first = false;

                String message = rs.getString("message")
                        .replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n");

                String type = rs.getString("message_type");

                String unlock = rs.getString("unlock_time");

                json.append("{")
                    .append("\"id\":").append(rs.getInt("id")).append(",")
                    .append("\"senderId\":").append(rs.getInt("sender_id")).append(",")
                    .append("\"receiverId\":").append(rs.getInt("receiver_id")).append(",")
                    .append("\"message\":\"").append(message).append("\",")
                    .append("\"type\":\"").append(type).append("\",")
                    .append("\"unlockTime\":\"")
                    .append(unlock == null ? "" : unlock)
                    .append("\",")
                    .append("\"sentAt\":\"")
                    .append(rs.getString("sent_at"))
                    .append("\"")
                    .append("}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        json.append("]");

        response.getWriter().print(json);
    }
}
