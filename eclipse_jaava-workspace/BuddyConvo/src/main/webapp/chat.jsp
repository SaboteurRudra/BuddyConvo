
<%@ page import="java.util.List,java.util.Map" %>
<%@ page import="BuddyConvo.User" %>
<%@ page import="java.sql.Timestamp" %>

<%!
    private String escapeHtml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
%>

<%
    User currentUser = (User) session.getAttribute("user");

    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Integer receiverId = (Integer) request.getAttribute("receiverId");

    if (receiverId == null) {
        response.sendRedirect("friends");
        return;
    }

    String friendName = (String) request.getAttribute("friendName");
    String friendUsername =
        (String) request.getAttribute("friendUsername");

    List<Map<String, Object>> messages =
        (List<Map<String, Object>>) request.getAttribute("messages");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Chat - BuddyConvo</title>
<style>
* { box-sizing: border-box; }
body {
    margin: 0;
    padding: 20px;
    background: #090d16;
    color: #f5f5ff;
    font-family: Arial, sans-serif;
}
.container {
    max-width: 760px;
    margin: auto;
    background: #141a29;
    border-radius: 16px;
    overflow: hidden;
}
.header {
    padding: 20px;
    background: #1e2638;
}
.header h2 { margin: 0 0 8px; color: #887bff; }
.header a { color: #c3bdff; text-decoration: none; }
.chat {
    padding: 18px;
    min-height: 300px;
    max-height: 55vh;
    overflow-y: auto;
}
.message {
    max-width: 82%;
    margin: 12px 0;
    padding: 12px 15px;
    border-radius: 14px;
    overflow-wrap: anywhere;
    white-space: pre-wrap;
}
.sent {
    margin-left: auto;
    background: #6559ee;
}
.received {
    margin-right: auto;
    background: #273247;
}
.time {
    display: block;
    font-size: 11px;
    opacity: .75;
    margin-top: 8px;
}
form {
    display: flex;
    gap: 10px;
    padding: 15px;
    background: #1e2638;
}
input {
    min-width: 0;
    flex: 1;
    padding: 13px;
    border-radius: 10px;
    border: 1px solid #39445b;
    background: #0f1522;
    color: white;
}
button {
    padding: 12px 18px;
    background: #6c63ff;
    border: 0;
    color: white;
    border-radius: 10px;
    font-weight: bold;
}
</style>
</head>
<body>
<div class="container">

    <div class="header">
        <h2><%= escapeHtml(friendName) %></h2>
        <p>@<%= escapeHtml(friendUsername) %></p>
        <a href="friends">← Back to Friends</a>
        &nbsp;·&nbsp;
        <a href="conversations">All Messages</a>
    </div>

    <div class="chat">
        <%
            if (messages == null || messages.isEmpty()) {
        %>
            <p>No previous messages. Say hello!</p>
        <%
            } else {
                for (Map<String, Object> item : messages) {
                    int senderId = (Integer) item.get("senderId");
                    boolean sent = senderId == currentUser.getId();

                    String content = (String) item.get("message");
                    Timestamp sentAt = (Timestamp) item.get("sentAt");
        %>
            <div class="message <%= sent ? "sent" : "received" %>">
                <%= escapeHtml(content) %>

                <span class="time">
                    <%= sentAt == null ? "" : sentAt.toString() %>
                </span>
            </div>
        <%
                }
            }
        %>
    </div>

    <form action="chat" method="post">
        <input type="hidden" name="receiverId"
               value="<%= receiverId %>">

        <input type="text" name="message"
               placeholder="Write a message..."
               maxlength="5000" required autocomplete="off">

        <button type="submit">Send</button>
    </form>

</div>
</body>
</html>
