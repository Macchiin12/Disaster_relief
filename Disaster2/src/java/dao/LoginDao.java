package dao;

import bean.LoginBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// DAO for handling database operations related to user login.
public class LoginDao {

    // JDBC URL for Derby database. Ensure this matches your setup.
    private static final String JDBC_URL = "jdbc:derby://localhost:1527/Career;user=app;password=app";
    private static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";

    public LoginDao() {
        // Load the Derby JDBC driver when the DAO is instantiated.
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Derby JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Validates user login credentials against the database.
     *
     * @param username The username provided by the user.
     * @param password The password provided by the user.
     * @return true if credentials are valid, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean validateLogin(String username, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean isValid = false;

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            // SQL query to check username and password in the LOGIN table.
            String sql = "SELECT USERNAME, PASSWORD FROM APP.LOGIN WHERE USERNAME = ? AND PASSWORD = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            // If a record is found, credentials are valid.
            isValid = rs.next();

        } finally {
            // Close resources
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return isValid;
    }

    /**
     * Registers a new user in the database.
     *
     * @param loginBean The LoginBean containing user details.
     * @return true if registration is successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean registerUser(LoginBean loginBean) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            // SQL INSERT statement for the LOGIN table.
            String sql = "INSERT INTO APP.LOGIN (USERNAME, EMAIL, PASSWORD) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loginBean.getUsername());
            pstmt.setString(2, loginBean.getEmail());
            pstmt.setString(3, loginBean.getPassword());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
            }
        } finally {
            // Close resources
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return success;
    }

    /**
     * Checks if a user with the given email already exists.
     *
     * @param email The email to check.
     * @return true if a user with the email exists, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean isUserExists(String email) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            String sql = "SELECT COUNT(*) FROM APP.LOGIN WHERE EMAIL = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
        } finally {
            // Close resources
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return exists;
    }
}
