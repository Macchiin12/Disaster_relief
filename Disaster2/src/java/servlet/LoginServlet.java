package servlet;

import dao.LoginDao; // Assuming you have this DAO
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession; // Import for session management
import javax.servlet.RequestDispatcher; // Import for forwarding requests

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        LoginDao loginDao = new LoginDao(); // Instantiate your LoginDao

        try {
            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                request.setAttribute("errorMessage", "Username and password are required.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp"); // Assuming login form is on index.jsp
                dispatcher.forward(request, response);
                return;
            }

            // Validate login using LoginDao
            if (loginDao.validateLogin(username, password)) {
                HttpSession session = request.getSession();
                session.setAttribute("user", username); // Store username in session

                response.sendRedirect(request.getContextPath() + "/dashboard.jsp"); // Redirect to dashboard.jsp
            } else {
                request.setAttribute("errorMessage", "Invalid username or password. Please try again.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp"); // Assuming login form is on index.jsp
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            System.err.println("Database error during login: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "A database error occurred. Please try again later.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp"); // Redirect to login page on database error
            dispatcher.forward(request, response);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during login: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred. Please try again.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp"); // Redirect to login page on unexpected error
            dispatcher.forward(request, response);
        }
    }
}
