package BuddyConvo;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.*;

@WebServlet("/addFriend")
public class FriendServlet extends HttpServlet {

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");

        HttpSession session = request.getSession();

        User currentUser =
                (User) session.getAttribute("user");

        if (currentUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String sql =
            "SELECT id FROM users WHERE username = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {

                response.getWriter().println(
                    "User not found! <br><br>" +
                    "<a href='friends.jsp'>Go Back</a>"
                );

                return;
            }

            int friendId = rs.getInt("id");

            if (friendId == currentUser.getId()) {

                response.getWriter().println(
                    "You cannot add yourself! <br><br>" +
                    "<a href='friends.jsp'>Go Back</a>"
                );

                return;
            }

            String insert =
                "INSERT INTO friends " +
                "(user_id, friend_id) VALUES (?, ?)";

            try (PreparedStatement add =
                    con.prepareStatement(insert)) {

                add.setInt(1, currentUser.getId());
                add.setInt(2, friendId);

                add.executeUpdate();
            }

            response.getWriter().println(
                "<h2>Friend added successfully! 🎉</h2>" +
                "<a href='home.jsp'>Go to Home</a>"
            );

        } catch (SQLIntegrityConstraintViolationException e) {

            response.getWriter().println(
                "<h2>This user is already your friend.</h2>" +
                "<a href='friends.jsp'>Go Back</a>"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                "Error adding friend.<br>" +
                e.getMessage()
            );
        }
    }
}