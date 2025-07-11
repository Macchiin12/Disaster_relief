package servlet;

import bean.LoginBean; // Using LoginBean for signup data
import dao.LoginDao;   // Using LoginDao for database operations

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get form data
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        LoginDao loginDao = new LoginDao(); // Instantiate the DAO

        try {
            // Validate required fields
            if (username == null || username.isEmpty() || email == null || email.isEmpty()
                    || password == null || password.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
                response.sendRedirect("signup.jsp?status=invalid_input");
                return;
            }

            // Check if passwords match
            if (!password.equals(confirmPassword)) {
                response.sendRedirect("signup.jsp?status=password_mismatch");
                return;
            }

            // Check if user already exists
            if (loginDao.isUserExists(email)) {
                response.sendRedirect("signup.jsp?status=user_exists");
                return;
            }

            // Create LoginBean and store user in the database
            LoginBean newUser = new LoginBean();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(password); // In a real app, hash this password!

            boolean success = loginDao.registerUser(newUser);

            if (success) {
                // Store email in session (optional, but consistent with previous logic)
                HttpSession session = request.getSession();
                session.setAttribute("userEmail", email); // You might want to use 'username' here for consistency with LoginServlet

                // Redirect to a success page or login page
                response.sendRedirect("index.jsp?status=signup_success"); // Redirect to login with success message
            } else {
                response.sendRedirect("signup.jsp?status=error"); // Generic error
            }

        } catch (SQLException ex) {
            System.err.println("Database error during signup: " + ex.getMessage());
            ex.printStackTrace();
            response.sendRedirect("signup.jsp?status=sql_error");
        } catch (Exception ex) {
            System.err.println("An unexpected error occurred during signup: " + ex.getMessage());
            ex.printStackTrace();
            response.sendRedirect("signup.jsp?status=unknown_error");
        } finally {
            out.close();
        }
    }
}