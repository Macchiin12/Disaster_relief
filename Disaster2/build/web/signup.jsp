<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Disaster Relief - Sign Up</title>
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
            background: linear-gradient(to right, #4facfe 0%, #00f2fe 100%);
            color: #333;
        }
        .signup-container {
            background-color: #ffffff;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
            text-align: center;
            width: 100%;
            max-width: 450px;
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
        input[type="email"],
        input[type="password"] {
            width: calc(100% - 22px);
            padding: 12px;
            border: 1px solid #ced4da;
            border-radius: 8px;
            font-size: 16px;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }
        input[type="text"]:focus,
        input[type="email"]:focus,
        input[type="password"]:focus {
            border-color: #007bff;
            box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
            outline: none;
        }
        button {
            width: 100%;
            padding: 14px;
            background-color: #28a745; /* Green for signup */
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 18px;
            font-weight: 600;
            cursor: pointer;
            transition: background-color 0.3s ease, transform 0.2s ease;
            box-shadow: 0 4px 10px rgba(40, 167, 69, 0.3);
        }
        button:hover {
            background-color: #218838;
            transform: translateY(-2px);
        }
        .login-link {
            margin-top: 20px;
            font-size: 15px;
            color: #666;
        }
        .login-link a {
            color: #007bff;
            text-decoration: none;
            font-weight: 600;
        }
        .login-link a:hover {
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
    <div class="signup-container">
        <h1>Sign Up for Disaster Relief System</h1>
        <%
            String status = request.getParameter("status");
            if ("invalid_input".equals(status)) {
                out.println("<p class='message error'>All fields are required.</p>");
            } else if ("password_mismatch".equals(status)) {
                out.println("<p class='message error'>Passwords do not match.</p>");
            } else if ("user_exists".equals(status)) {
                out.println("<p class='message error'>User with this email already exists.</p>");
            } else if ("sql_error".equals(status)) {
                out.println("<p class='message error'>A database error occurred during signup. Please try again later.</p>");
            } else if ("unknown_error".equals(status)) {
                out.println("<p class='message error'>An unexpected error occurred. Please try again later.</p>");
            }
        %>
        <form action="SignupServlet" method="post">
            <div class="form-group">
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="form-group">
                <label for="confirmPassword">Confirm Password:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
            </div>
            <button type="submit">Sign Up</button>
        </form>
        <div class="login-link">
            Already have an account? <a href="index.jsp">Login Here</a>
        </div>
    </div>
</body>
</html>