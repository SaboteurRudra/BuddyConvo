<%@ page import="BuddyConvo.User"%><%
User u=(User)session.getAttribute("user");
if(u==null){response.sendRedirect("login.jsp");return;}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>BuddyConvo</title>
<style>
*{box-sizing:border-box}body{margin:0;background:#080b12;color:#f0f0ff;font-family:Arial,sans-serif;display:flex;min-height:100vh}
aside{width:220px;background:#1e1e2d;padding:25px 15px}h1{color:#7850ff;text-align:center}
a,button{display:block;width:100%;padding:13px;margin:8px 0;border:0;border-radius:9px;background:#7870ff;color:white;text-decoration:none;cursor:pointer}
a{background:transparent}a:hover,button:hover{background:#7850ff}
main{flex:1;padding:30px;max-width:800px;margin:auto;width:100%}
header{display:flex;justify-content:space-between;border-bottom:1px solid #303045;padding-bottom:15px}
.online{color:#55e68a}.box{background:#1e1e2d;padding:20px;border-radius:15px;margin-top:25px}
input{width:100%;padding:14px;border:0;border-radius:9px;background:#303045;color:white;font-size:16px;margin-bottom:10px}
.actions{display:flex;gap:8px}.actions button{margin:0}.voice{background:#5940c8}.capsule{background:#404b70}
@media(max-width:600px){body{display:block}aside{width:100%;padding:10px}aside a{display:inline-block;width:auto;padding:8px}aside h1{margin:8px}.actions button{font-size:12px}main{padding:15px}}
</style>
</head>
<body>

<aside>
<h1>BuddyConvo</h1>
<a href="home.jsp">🏠 Home</a>
<a href="home.jsp">💬 Messages</a>
<a href="home.jsp">👥 Friends</a>
<a href="home.jsp">⏳ Time Capsule</a>
<button onclick="theme()">🌙 Theme</button>
<a href="logout">🚪 Logout</a>
</aside>

<main>
<header><b>BuddyConvo</b><span class="online">● Online</span></header>

<h2>Welcome, <%=u.getName()%> 👋</h2>

<div class="box">
<form action="chat" method="post">
<input type="hidden" name="receiverId" value="1">
<input class="message-input" name="message" placeholder="Write a message..." required>
<div class="actions">
<button type="button" class="capsule" onclick="timeCapsule()">⏳ Capsule</button>
<button type="button" class="voice" onclick="speakMessage()">🔊 Voice</button>
<button type="submit">Send ➤</button>
</div>
</form>
</div>
</main>

<script>
function theme(){
document.body.style.background=
document.body.style.background=="white"?"#080b12":"white";
document.body.style.color=
document.body.style.color=="black"?"#f0f0ff":"black";
}

function timeCapsule(){
let t=prompt("Unlock date/time (YYYY-MM-DD HH:MM):");
if(!t)return;
let m=document.querySelector(".message-input").value.trim();
if(!m)return alert("Type a message first.");
let f=document.createElement("form");
f.method="post";f.action="chat";
f.innerHTML='<input name="receiverId" value="1"><input name="message"><input name="unlockTime">';
f.querySelector("[name=message]").value=m;
f.querySelector("[name=unlockTime]").value=t.replace(" ","T");
document.body.appendChild(f);f.submit();
}

function speakMessage(){
const input=document.querySelector(".message-input"),text=input.value.trim();
if(!text){alert("Type a message first.");return;}
const data=new URLSearchParams();data.append("text",text);
fetch("voice",{method:"POST",headers:{"Content-Type":"application/x-www-form-urlencoded"},body:data})
.then(async r=>{
if(!r.ok)throw new Error("Server error "+r.status+": "+await r.text());
return r.blob();
})
.then(b=>{
const u=URL.createObjectURL(b),a=new Audio(u);
a.play();a.onended=()=>URL.revokeObjectURL(u);
})
.catch(e=>{console.error("AI VOICE ERROR:",e);alert("AI voice failed.\n\n"+e.message);});
}
</script>
</body>
</html>
