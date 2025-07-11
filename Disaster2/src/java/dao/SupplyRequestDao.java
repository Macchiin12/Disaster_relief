package dao;

import bean.SupplyRequestBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

// DAO for handling database operations related to supply requests.
public class SupplyRequestDao {

    private static final String JDBC_URL = "jdbc:derby://localhost:1527/Career;user=app;password=app";
    private static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";

    public SupplyRequestDao() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Derby JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Schedules a new supply request in the database.
     *
     * @param requestBean The SupplyRequestBean containing request details.
     * @return true if scheduling is successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean scheduleSupplyRequest(SupplyRequestBean requestBean) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            String sql = "INSERT INTO APP.SUPPLY_REQUESTS (REQUEST_ID, BENEFICIARY_ID, REQUEST_DATE, SUPPLY_ITEM, QUANTITY, STATUS, NOTES) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, requestBean.getRequestId());
            pstmt.setString(2, requestBean.getBeneficiaryId());

            // Convert String requestDate to java.sql.Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                java.util.Date parsedDate = dateFormat.parse(requestBean.getRequestDate());
                pstmt.setDate(3, new java.sql.Date(parsedDate.getTime()));
            } catch (ParseException e) {
                System.err.println("Error parsing Request Date: " + requestBean.getRequestDate());
                e.printStackTrace();
                return false; // Indicate failure
            }

            pstmt.setString(4, requestBean.getSupplyItem());
            pstmt.setInt(5, requestBean.getQuantity());
            pstmt.setString(6, requestBean.getStatus());
            pstmt.setString(7, requestBean.getNotes());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
            }
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return success;
    }

    /**
     * Retrieves all supply requests from the database.
     *
     * @return A list of SupplyRequestBean objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<SupplyRequestBean> getAllSupplyRequests() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<SupplyRequestBean> requests = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            String sql = "SELECT REQUEST_ID, BENEFICIARY_ID, REQUEST_DATE, SUPPLY_ITEM, QUANTITY, STATUS, NOTES FROM APP.SUPPLY_REQUESTS";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                SupplyRequestBean request = new SupplyRequestBean();
                request.setRequestId(rs.getString("REQUEST_ID"));
                request.setBeneficiaryId(rs.getString("BENEFICIARY_ID"));
                if (rs.getDate("REQUEST_DATE") != null) {
                    request.setRequestDate(new SimpleDateFormat("yyyy-MM-dd").format(rs.getDate("REQUEST_DATE")));
                }
                request.setSupplyItem(rs.getString("SUPPLY_ITEM"));
                request.setQuantity(rs.getInt("QUANTITY"));
                request.setStatus(rs.getString("STATUS"));
                request.setNotes(rs.getString("NOTES"));
                requests.add(request);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return requests;
    }

    /**
     * Deletes a supply request from the database by its ID.
     *
     * @param requestId The ID of the request to delete.
     * @return true if deletion was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean deleteSupplyRequest(String requestId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            String sql = "DELETE FROM APP.SUPPLY_REQUESTS WHERE REQUEST_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, requestId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
            }
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return success;
    }
}