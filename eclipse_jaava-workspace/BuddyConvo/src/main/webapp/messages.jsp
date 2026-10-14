
<%@ page import="java.util.List,java.util.Map" %>
<%@ page import="BuddyConvo.User" %>
<%@ page import="java.sql.Timestamp" %>

<%!
    private String escapeMessageHtml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
%>

<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    User currentUser = (User) session.getAttribute("user");

    List<Map<String, Object>> conversations =
        (List<Map<String, Object>>) request.getAttribute("conversations");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Messages - BuddyConvo</title>
<style>
body {
    margin: 0;
    padding: 24px;
    background: #090d16;
    color: #f5f5ff;
    font-family: Arial, sans-serif;
}
.container {
    max-width: 760px;
    margin: auto;
}
h1 { color: #8174ff; }
.nav { margin-bottom: 24px; }
a { color: #c0b8ff; text-decoration: none; }
.item {
    display: block;
    padding: 18px;
    margin-bottom: 12px;
    border-radius: 14px;
    background: #171e2e;
    color: white;
}
.item:hover { background: #232c41; }
.name { font-weight: bold; font-size: 17px; }
.preview { margin-top: 8px; color: #c2c8d6; }
.date { margin-top: 8px; font-size: 12px; color: #939db2; }
</style>
</head>
<body>
<div class="container">

    <h1>💬 Messages</h1>

    <div class="nav">
        <a href="home.jsp">Home</a> ·
        <a href="friends">Friends</a> ·
        <a href="timecapsule">Time Capsule</a>
    </div>

    <%
        if (conversations == null || conversations.isEmpty()) {
    %>
        <div class="item">
            <p>No conversations yet.</p>
            <a href="friends">Choose a friend to start chatting →</a>
        </div>
    <%
        } else {
            for (Map<String, Object> item : conversations) {
                int peerId = (Integer) item.get("peerId");
                int senderId = (Integer) item.get("senderId");

                String name = (String) item.get("name");
                String username = (String) item.get("username");
                String preview = (String) item.get("message");
                String type = (String) item.get("messageType");
                Timestamp sentAt = (Timestamp) item.get("sentAt");

                if ("TIME_CAPSULE".equals(type)) {
                    preview = senderId == currentUser.getId()
                        ? "⏳ You sent a Time Capsule"
                        : "🔐 Time Capsule message";
                } else if (senderId == currentUser.getId()) {
                    preview = "You: " + preview;
                }
    %>

        <a class="item"
           href="chat?receiverId=<%= peerId %>">

            <div class="name">
                <%= escapeMessageHtml(name) %>
                <span style="font-weight:normal;color:#a6aec0">
                    @<%= escapeMessageHtml(username) %>
                </span>
            </div>

            <div class="preview">
                <%= escapeMessageHtml(preview) %>
            </div>

            <div class="date">
                <%= sentAt == null ? "" : sentAt.toString() %>
                &nbsp; · &nbsp; Open conversation →
            </div>
        </a>

    <%
            }
        }
    %>

</div>
</body>
</html>
