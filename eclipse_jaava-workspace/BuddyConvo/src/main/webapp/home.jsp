<%@ page import="BuddyConvo.User" %>
<%
User currentUser = (User) session.getAttribute("user");
if (currentUser == null) {
    response.sendRedirect("login.jsp");
    return;
}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>BuddyConvo</title>

<style>
*{box-sizing:border-box}
body{margin:0;font-family:Arial,sans-serif;background:#080b12;color:#fff;height:100vh}
.app{display:flex;height:100vh}
.sidebar{width:240px;background:#101521;border-right:1px solid #252b38;padding:22px 14px}
.logo{font-size:25px;font-weight:bold;margin:5px 10px 30px}
.logo span{color:#7c5cff}
.menu{display:block;padding:13px;margin:7px 0;border-radius:12px;text-decoration:none;color:#cbd1dc}
.menu:hover{background:#1d2432;color:#fff}
.logout{color:#ff7185}
.main{flex:1;display:flex;flex-direction:column}
.header{height:70px;display:flex;align-items:center;justify-content:space-between;padding:0 25px;background:#0d111b;border-bottom:1px solid #252b38}
.header-title{font-size:21px;font-weight:bold}
.status{color:#58e6a5;font-size:13px}
.chat{flex:1;overflow-y:auto;padding:25px}
.welcome{text-align:center;margin-top:90px;color:#9299a8}
.welcome h2{color:#fff}
.composer{padding:16px;background:#0d111b;border-top:1px solid #252b38}
.composer form{display:flex;gap:9px}
.message-input{flex:1;background:#171d29;border:1px solid #2b3342;border-radius:13px;padding:13px;color:#fff;outline:none}
.btn{border:0;border-radius:12px;padding:0 17px;cursor:pointer;font-weight:bold}
.send{background:#7c5cff;color:#fff}
.voice,.capsule{background:#202838;color:#fff}
.capsule{color:#c6b8ff}
.light{background:#f4f6fa;color:#171a22}
.light .sidebar,.light .header,.light .composer{background:#fff}
.light .menu{color:#343945}
.light .menu:hover{background:#eef0f5}
.light .message-input{background:#f0f2f6;color:#111}
.light .welcome h2{color:#171a22}

@media(max-width:700px){
.sidebar{width:65px;padding:15px 7px}
.logo{font-size:0;text-align:center;margin:5px 0 25px}
.logo:after{content:"BC";font-size:20px;color:#7c5cff}
.menu{font-size:0;text-align:center;padding:14px 5px}
.menu::first-letter{font-size:21px}
.header{padding:0 15px}
.chat{padding:15px}
.composer{padding:10px}
.message-input{min-width:0}
.btn{padding:0 12px}
.btn.send{font-size:0}
.btn.send:after{content:"➤";font-size:17px}
}

</style>
</head>

<body>

<div class="app" id="app">

<aside class="sidebar">

<div class="logo">Buddy<span>Convo</span></div>

<a class="menu" href="home.jsp">🏠 Home</a>
<a class="menu" href="home.jsp">💬 Messages</a>
<a class="menu" href="home.jsp">👥 Friends</a>
<a class="menu" href="home.jsp">🔐 Time Capsule</a>

<a class="menu" href="#" onclick="toggleTheme();return false">
🌙 Theme
</a>

<a class="menu logout" href="logout">🚪 Logout</a>

</aside>

<main class="main">

<header class="header">

<div class="header-title">BuddyConvo</div>

<div class="status">● Online</div>

</header>

<section class="chat" id="chat">

<div class="welcome">

<h2>Welcome, <%= currentUser.getName() %> 👋</h2>

<p>Select a friend to start chatting.</p>

</div>

</section>

<section class="composer">

<form action="chat" method="post">

<input type="hidden" name="receiverId" value="1">

<input
class="message-input"
type="text"
name="message"
placeholder="Type a message..."
required>

<button
class="btn capsule"
type="button"
onclick="timeCapsule()">🔐</button>

<button
class="btn voice"
type="button"
onclick="speakMessage()">🎙️</button>

<button
class="btn send"
type="submit">Send</button>

</form>

</section>

</main>
</div>

<script>

function toggleTheme(){
document.body.classList.toggle("light");

localStorage.setItem(
"theme",
document.body.classList.contains("light")?"light":"dark"
);
}

if(localStorage.getItem("theme")==="light"){
document.body.classList.add("light");
}

function speakMessage(){

const input=document.querySelector(".message-input");
const text=input.value.trim();

if(!text){
alert("Type a message first.");
return;
}

const data=new URLSearchParams();
data.append("text",text);

fetch("voice",{
method:"POST",
headers:{
"Content-Type":
"application/x-www-form-urlencoded"
},
body:data
})
.then(response=>{
if(!response.ok)
throw new Error("Voice failed");
return response.blob();
})
.then(blob=>{
const audio=new Audio(
URL.createObjectURL(blob)
);
audio.play();
})
.catch(error=>{
console.error(error);
alert("AI voice is not configured yet.");
});
}

function timeCapsule(){

const input=document.querySelector(".message-input");
const message=input.value.trim();

if(!message){
alert("Type your Time Capsule message first.");
return;
}

const unlock=prompt(
"Enter unlock date and time:\nYYYY-MM-DD HH:MM"
);

if(!unlock)return;

const form=document.createElement("form");
form.method="POST";
form.action="chat";

const receiver=document.createElement("input");
receiver.name="receiverId";
receiver.value="1";

const msg=document.createElement("input");
msg.name="message";
msg.value=message;

const unlockInput=document.createElement("input");
unlockInput.name="unlockTime";
unlockInput.value=unlock.replace(" ","T");

form.append(receiver,msg,unlockInput);
document.body.appendChild(form);
form.submit();
}
</script>

</body>
</html>
