
package BuddyConvo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/friends")
public class FriendsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        List<User> friends = new ArrayList<>();

        String sql =
            "SELECT DISTINCT u.id, u.name, u.username " +
            "FROM friends f " +
            "JOIN users u ON u.id = " +
            "CASE WHEN f.user_id = ? THEN f.friend_id " +
            "ELSE f.user_id END " +
            "WHERE f.user_id = ? OR f.friend_id = ? " +
            "ORDER BY u.username";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                throw new SQLException("Database connection unavailable");
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, currentUser.getId());
                ps.setInt(2, currentUser.getId());
                ps.setInt(3, currentUser.getId());

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        User friend = new User();
                        friend.setId(rs.getInt("id"));
                        friend.setName(rs.getString("name"));
                        friend.setUsername(rs.getString("username"));
                        friends.add(friend);
                    }
                }
            }

            request.setAttribute("friends", friends);
            request.getRequestDispatcher("/friends.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Could not load friends", e);
        }
    }
}
