package dao;

import bean.BeneficiaryBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

// DAO for handling database operations related to beneficiaries.
public class BeneficiaryDao {

    private static final String JDBC_URL = "jdbc:derby://localhost:1527/Career;user=app;password=app";
    private static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";

    public BeneficiaryDao() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Derby JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Registers a new beneficiary in the database.
     *
     * @param beneficiaryBean The BeneficiaryBean containing beneficiary details.
     * @return true if registration is successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean registerBeneficiary(BeneficiaryBean beneficiaryBean) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            String sql = "INSERT INTO APP.BENEFICIARIES (BENEFICIARY_ID, BENEFICIARY_NAME, IC_NUMBER, ADDRESS, CONTACT_NUMBER, DATE_OF_BIRTH) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, beneficiaryBean.getBeneficiaryId());
            pstmt.setString(2, beneficiaryBean.getBeneficiaryName());
            pstmt.setString(3, beneficiaryBean.getIcNumber());
            pstmt.setString(4, beneficiaryBean.getAddress());
            pstmt.setString(5, beneficiaryBean.getContactNumber());

            // Convert String dateOfBirth to java.sql.Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                java.util.Date parsedDate = dateFormat.parse(beneficiaryBean.getDateOfBirth());
                pstmt.setDate(6, new java.sql.Date(parsedDate.getTime()));
            } catch (ParseException e) {
                System.err.println("Error parsing Date of Birth: " + beneficiaryBean.getDateOfBirth());
                e.printStackTrace();
                return false; // Indicate failure due to date parsing error
            }

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
     * Retrieves a beneficiary's details by their ID.
     *
     * @param beneficiaryId The ID of the beneficiary to retrieve.
     * @return A BeneficiaryBean object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public BeneficiaryBean getBeneficiaryById(String beneficiaryId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BeneficiaryBean beneficiary = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            String sql = "SELECT BENEFICIARY_ID, BENEFICIARY_NAME, IC_NUMBER, ADDRESS, CONTACT_NUMBER, DATE_OF_BIRTH FROM APP.BENEFICIARIES WHERE BENEFICIARY_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, beneficiaryId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                beneficiary = new BeneficiaryBean();
                beneficiary.setBeneficiaryId(rs.getString("BENEFICIARY_ID"));
                beneficiary.setBeneficiaryName(rs.getString("BENEFICIARY_NAME"));
                beneficiary.setIcNumber(rs.getString("IC_NUMBER"));
                beneficiary.setAddress(rs.getString("ADDRESS"));
                beneficiary.setContactNumber(rs.getString("CONTACT_NUMBER"));
                // Format Date to String for the bean
                if (rs.getDate("DATE_OF_BIRTH") != null) {
                    beneficiary.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").format(rs.getDate("DATE_OF_BIRTH")));
                }
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return beneficiary;
    }

    /**
     * Retrieves all beneficiaries from the database.
     *
     * @return A list of BeneficiaryBean objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<BeneficiaryBean> getAllBeneficiaries() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BeneficiaryBean> beneficiaries = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            String sql = "SELECT BENEFICIARY_ID, BENEFICIARY_NAME, IC_NUMBER, ADDRESS, CONTACT_NUMBER, DATE_OF_BIRTH FROM APP.BENEFICIARIES";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                BeneficiaryBean beneficiary = new BeneficiaryBean();
                beneficiary.setBeneficiaryId(rs.getString("BENEFICIARY_ID"));
                beneficiary.setBeneficiaryName(rs.getString("BENEFICIARY_NAME"));
                beneficiary.setIcNumber(rs.getString("IC_NUMBER"));
                beneficiary.setAddress(rs.getString("ADDRESS"));
                beneficiary.setContactNumber(rs.getString("CONTACT_NUMBER"));
                if (rs.getDate("DATE_OF_BIRTH") != null) {
                    beneficiary.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").format(rs.getDate("DATE_OF_BIRTH")));
                }
                beneficiaries.add(beneficiary);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return beneficiaries;
    }

    /**
     * Updates an existing beneficiary's details in the database.
     *
     * @param beneficiaryBean The BeneficiaryBean with updated details.
     * @return true if the update was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean updateBeneficiary(BeneficiaryBean beneficiaryBean) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            // Note: Beneficiary Name, IC Number, and Date of Birth are readonly in the JSP.
            // The update query should reflect what can actually be changed by the user.
            // If these fields are truly immutable after registration, remove them from the UPDATE statement.
            // Assuming for now they can be updated if the bean provides new values,
            // but if they are fixed, the SQL should be adjusted to only update mutable fields.
            // For this fix, we'll keep them in the update statement as per your original DAO structure,
            // but the servlet should ensure they are populated correctly from the original record if readonly.
            String sql = "UPDATE APP.BENEFICIARIES SET BENEFICIARY_NAME = ?, IC_NUMBER = ?, ADDRESS = ?, CONTACT_NUMBER = ?, DATE_OF_BIRTH = ? WHERE BENEFICIARY_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, beneficiaryBean.getBeneficiaryName());
            pstmt.setString(2, beneficiaryBean.getIcNumber());
            pstmt.setString(3, beneficiaryBean.getAddress());
            pstmt.setString(4, beneficiaryBean.getContactNumber());

            // Convert String dateOfBirth to java.sql.Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                java.util.Date parsedDate = dateFormat.parse(beneficiaryBean.getDateOfBirth());
                pstmt.setDate(5, new java.sql.Date(parsedDate.getTime()));
            } catch (ParseException e) {
                System.err.println("Error parsing Date of Birth for update: " + beneficiaryBean.getDateOfBirth());
                e.printStackTrace();
                return false; // Indicate failure
            }
            pstmt.setString(6, beneficiaryBean.getBeneficiaryId());

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
     * Deletes a beneficiary from the database by their ID.
     *
     * @param beneficiaryId The ID of the beneficiary to delete.
     * @return true if deletion was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean deleteBeneficiary(String beneficiaryId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            String sql = "DELETE FROM APP.BENEFICIARIES WHERE BENEFICIARY_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, beneficiaryId);

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
     * Checks if an IC number is already taken by a *different* beneficiary.
     * This is used during update operations to prevent unique constraint violations.
     *
     * @param icNumber The IC number to check.
     * @param currentBeneficiaryId The ID of the beneficiary currently being updated (to exclude from the check).
     * @return true if the IC number is taken by another beneficiary, false otherwise.
     */
    public boolean isIcNumberTakenByOtherBeneficiary(String icNumber, String currentBeneficiaryId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            // Check if any beneficiary *other than the current one* has this IC number
            String sql = "SELECT COUNT(*) FROM APP.BENEFICIARIES WHERE IC_NUMBER = ? AND BENEFICIARY_ID <> ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, icNumber);
            pstmt.setString(2, currentBeneficiaryId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    exists = true;
                }
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return exists;
    }

    /**
     * Checks if an IC number already exists in the database for any beneficiary.
     * This is typically used during new registration.
     *
     * @param icNumber The IC number to check.
     * @return true if the IC number exists, false otherwise.
     */
    public boolean isIcNumberExists(String icNumber) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            String sql = "SELECT COUNT(*) FROM APP.BENEFICIARIES WHERE IC_NUMBER = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, icNumber);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    exists = true;
                }
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return exists;
    }
}