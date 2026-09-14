<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Friends - BuddyConvo</title>

<style>
body{
    margin:0;
    font-family:Arial;
    background:#0b0f19;
    color:white;
    padding:40px
}

.box{
    max-width:600px;
    margin:auto;
    background:#111827;
    padding:30px;
    border-radius:15px
}

h1{
    color:#6c63ff
}

input{
    width:70%;
    padding:13px;
    border:0;
    border-radius:20px;
    background:#1f2937;
    color:white
}

button{
    padding:13px 20px;
    border:0;
    border-radius:20px;
    background:#6c63ff;
    color:white;
    cursor:pointer
}

.back{
    display:inline-block;
    margin-top:25px;
    color:#9ca3af;
    text-decoration:none
}
</style>
</head>

<body>

<div class="box">

<h1>👥 Friends</h1>

<p>Find a BuddyConvo user and add them as a friend.</p>

<form action="addFriend" method="post">

<input
    type="text"
    name="username"
    placeholder="Enter username"
    required>

<button type="submit">ADD FRIEND</button>

</form>

<br>

<a class="back" href="home.jsp">← Back to Home</a>

</div>

</body>
</html>