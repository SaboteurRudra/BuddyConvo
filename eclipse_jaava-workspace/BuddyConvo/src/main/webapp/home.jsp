<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>BuddyConvo</title>

<style>
*{box-sizing:border-box}

body{
    margin:0;
    font-family:Arial,sans-serif;
    background:#0b0f19;
    color:white
}

.topbar{
    height:60px;
    background:#111827;
    display:flex;
    align-items:center;
    padding:0 20px;
    border-bottom:1px solid #273449
}

.logo{
    font-size:24px;
    font-weight:bold;
    color:#6c63ff
}

.profile{
    margin-left:auto;
    color:#9ca3af
}

.container{
    display:flex;
    height:calc(100vh - 60px)
}

.sidebar{
    width:240px;
    background:#111827;
    border-right:1px solid #273449;
    padding:15px
}

.sidebar h2{
    margin-top:5px
}

.menu{
    display:block;
    padding:13px;
    margin:8px 0;
    border-radius:10px;
    background:#1f2937;
    color:white;
    text-decoration:none;
    cursor:pointer
}

.menu:hover{
    background:#6c63ff
}

.chat{
    flex:1;
    padding:30px
}

.card{
    background:#111827;
    padding:25px;
    border-radius:15px;
    max-width:600px
}

.card h1{
    color:#6c63ff
}

input{
    padding:12px;
    width:70%;
    border:0;
    border-radius:20px;
    background:#1f2937;
    color:white
}

button{
    padding:12px 20px;
    border:0;
    border-radius:20px;
    background:#6c63ff;
    color:white;
    cursor:pointer
}

button:hover{
    background:#584ff0
}
</style>
</head>

<body>

<div class="topbar">
    <div class="logo">BuddyConvo</div>
    <div class="profile">Welcome to BuddyConvo</div>
</div>

<div class="container">

<div class="sidebar">

<h2>BuddyConvo</h2>

<a class="menu" href="home.jsp">🏠 Home</a>

<a class="menu" href="messages.jsp">
💬 Messages
</a>

<a class="menu" href="friends.jsp">
👥 Friends
</a>

<a class="menu" href="voice.jsp">
🎤 Voice Messages
</a>

<a class="menu" href="timecapsule.jsp">
🔐 Time Capsule
</a>

<a class="menu" href="settings.jsp">
⚙ Settings
</a>

</div>

<div class="chat">

<div class="card">

<h1>Welcome to BuddyConvo 👋</h1>

<p>Your private space to chat, connect and share.</p>

<p>Use the menu on the left to explore BuddyConvo.</p>

<hr>

<h2>Quick Actions</h2>

<a href="friends.jsp">
<button>Add / Find Friends</button>
</a>

<a href="messages.jsp">
<button>Open Messages</button>
</a>

</div>

</div>

</div>

</body>
</html>