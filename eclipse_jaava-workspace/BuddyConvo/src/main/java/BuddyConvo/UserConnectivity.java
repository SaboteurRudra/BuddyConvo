public User login(String username, String password) {

    String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, username);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            User user = new User();

            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setStatus(rs.getString("status"));

            return user;
        }

    } catch (Exception e) {

        System.out.println("LOGIN ERROR:");
        e.printStackTrace();
    }

    return null;
}

		
