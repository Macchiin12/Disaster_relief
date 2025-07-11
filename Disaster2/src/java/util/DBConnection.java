package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String JDBC_URL = "jdbc:derby://localhost:1527/Career";
    private static final String USERNAME = "app";
    private static final String PASSWORD = "app";
    private static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";

    public static Connection createConnection() throws SQLException {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new SQLException("Database error: Failed to establish database connection.");
        }
        return conn;
    }
}