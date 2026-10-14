
<%@ page import="java.util.List" %>
<%@ page import="BuddyConvo.User" %>
<%@ page import="java.net.URLEncoder" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<User> friends =
        (List<User>) request.getAttribute("friends");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Friends - BuddyConvo</title>
<style>
body {
    margin: 0;
    padding: 25px;
    background: #080b12;
    color: #f0f0ff;
    font-family: Arial, sans-serif;
}
.box {
    max-width: 650px;
    margin: auto;
    background: #171b2b;
    padding: 24px;
    border-radius: 16px;
}
h1 { color: #7865ff; }
.friend {
    padding: 16px;
    margin: 12px 0;
    background: #252b3b;
    border-radius: 12px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 12px;
}
a {
    color: #aaa0ff;
    text-decoration: none;
}
button {
    background: #6c63ff;
    color: white;
    border: 0;
    padding: 10px 14px;
    border-radius: 8px;
}
input {
    padding: 12px;
    border-radius: 8px;
    border: 1px solid #394156;
    background: #252b3b;
    color: white;
}
</style>
</head>
<body>
<div class="box">
    <h1>👥 Your Friends</h1>
    <p>Choose a friend to continue your conversation.</p>

    <form action="addFriend" method="post">
        <input name="username"
               placeholder="Enter a BuddyConvo username"
               required>
        <button type="submit">Add Friend</button>
    </form>

    <hr style="border-color:#394156;margin:24px 0">

    <%
        if (friends == null || friends.isEmpty()) {
    %>
        <p>You haven't added any friends yet.</p>
    <%
        } else {
            for (User friend : friends) {
    %>
        <div class="friend">
            <div>
                <strong><%= friend.getName() %></strong>
                <br>
                <small>@<%= friend.getUsername() %></small>
            </div>
            <a href="chat?receiverId=<%= friend.getId() %>">
                Open Chat →
            </a>
        </div>
    <%
            }
        }
    %>

    <p><a href="home.jsp">← Back to Home</a></p>
</div>
</body>
</html>
