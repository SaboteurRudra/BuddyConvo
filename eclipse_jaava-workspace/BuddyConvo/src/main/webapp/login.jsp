<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>BuddyConvo - Login</title>

<style>

* {
    box-sizing: border-box;
}

body {
    margin: 0;
    font-family: Arial, sans-serif;
    background: #0b0f19;
    color: white;
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
}

.container {
    width: 420px;
    background: #111827;
    padding: 35px;
    border-radius: 20px;
    box-shadow: 0 0 35px rgba(108, 99, 255, 0.25);
}

.logo {
    text-align: center;
    font-size: 34px;
    font-weight: bold;
    color: #6c63ff;
    margin-bottom: 5px;
}

.subtitle {
    text-align: center;
    color: #9ca3af;
    margin-bottom: 30px;
}

h2 {
    text-align: center;
}

input {
    width: 100%;
    padding: 13px;
    margin-top: 8px;
    margin-bottom: 15px;
    border: 1px solid #374151;
    border-radius: 10px;
    background: #1f2937;
    color: white;
    outline: none;
}

input:focus {
    border-color: #6c63ff;
}

button {
    width: 100%;
    padding: 13px;
    border: none;
    border-radius: 10px;
    background: #6c63ff;
    color: white;
    font-size: 15px;
    font-weight: bold;
    cursor: pointer;
}

button:hover {
    background: #584ff0;
}

hr {
    border: none;
    border-top: 1px solid #374151;
    margin: 30px 0;
}

label {
    color: #d1d5db;
    font-size: 14px;
}

</style>

</head>

<body>

<div class="container">

    <div class="logo">
        BuddyConvo
    </div>

    <div class="subtitle">
        Connect. Chat. Share.
    </div>


    <h2>Login</h2>

    <form action="login" method="post">

        <label>Username</label>

        <input
            type="text"
            name="username"
            required
        >

        <label>Password</label>

        <input
            type="password"
            name="password"
            required
        >

        <button type="submit">
            LOGIN
        </button>

    </form>


    <hr>


    <h2>Create Account</h2>

    <form action="register" method="post">

        <label>Full Name</label>

        <input
            type="text"
            name="name"
            required
        >

        <label>Email</label>

        <input
            type="email"
            name="email"
            required
        >

        <label>Username</label>
        <input
            type="text"
            name="username"
            required
        >
        <label>Password</label>
        <input
            type="password"
            name="password"
            required
        >
        <button type="submit">
            CREATE ACCOUNT
        </button>
    </form>
</div>
</body>
</html>