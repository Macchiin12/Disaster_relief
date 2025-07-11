<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Disaster Relief - Login</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            background: linear-gradient(to right, #4facfe 0%, #00f2fe 100%); /* Blue gradient */
            color: #333;
        }
        .login-container {
            background-color: #ffffff;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
            text-align: center;
            width: 100%;
            max-width: 400px;
            box-sizing: border-box;
        }
        h1 {
            color: #007bff;
            margin-bottom: 25px;
            font-size: 28px;
            font-weight: 700;
        }
        .form-group {
            margin-bottom: 20px;
            text-align: left;
        }
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #555;
        }
        input[type="text"],
        input[type="password"] {
            width: calc(100% - 22px); /* Account for padding and border */
            padding: 12px;
            border: 1px solid #ced4da;
            border-radius: 8px;
            font-size: 16px;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }
        input[type="text"]:focus,
        input[type="password"]:focus {
            border-color: #007bff;
            box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
            outline: none;
        }
        button {
            width: 100%;
            padding: 14px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 18px;
            font-weight: 600;
            cursor: pointer;
            transition: background-color 0.3s ease, transform 0.2s ease;
            box-shadow: 0 4px 10px rgba(0, 123, 255, 0.3);
        }
        button:hover {
            background-color: #0056b3;
            transform: translateY(-2px);
        }
        .signup-link {
            margin-top: 20px;
            font-size: 15px;
            color: #666;
        }
        .signup-link a {
            color: #007bff;
            text-decoration: none;
            font-weight: 600;
        }
        .signup-link a:hover {
            text-decoration: underline;
        }
        .message {
            color: green;
            margin-top: 15px;
            font-weight: 500;
        }
        .message.error {
            color: red;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <h1>Disaster Relief Login</h1>
        <%
            String logoutStatus = request.getParameter("logout");
            if ("true".equals(logoutStatus)) {
                out.println("<p class='message'>You have been logged out successfully.</p>");
            }
            String loginError = request.getParameter("status");
            if ("error".equals(loginError)) {
                 out.println("<p class='message error'>Invalid username or password. Please try again.</p>");
            }
        %>
        <form action="LoginServlet" method="post">
            <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit">Login</button>
        </form>
        <div class="signup-link">
            Don't have an account? <a href="signup.jsp">Sign Up Here</a>
        </div>
    </div>
</body>
</html>