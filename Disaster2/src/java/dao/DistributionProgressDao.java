package dao;

import bean.DistributionProgressBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

// DAO for handling database operations related to distribution progress.
public class DistributionProgressDao {

    private static final String JDBC_URL = "jdbc:derby://localhost:1527/Career;user=app;password=app";
    private static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";

    public DistributionProgressDao() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Derby JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Adds a new distribution progress record.
     *
     * @param progressBean The DistributionProgressBean containing progress details.
     * @return true if addition is successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean addProgress(DistributionProgressBean progressBean) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            // DISTRIBUTION_ID is auto-generated, so we don't include it in the INSERT statement.
            String sql = "INSERT INTO APP.DISTRIBUTION_PROGRESS (BENEFICIARY_ID, SUPPLY_ITEM, DISTRIBUTION_DATE, QUANTITY_DISTRIBUTED, STATUS, REMARKS) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, progressBean.getBeneficiaryId());
            pstmt.setString(2, progressBean.getSupplyItem());

            // Convert String distributionDate to java.sql.Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                java.util.Date parsedDate = dateFormat.parse(progressBean.getDistributionDate());
                pstmt.setDate(3, new java.sql.Date(parsedDate.getTime()));
            } catch (ParseException e) {
                System.err.println("Error parsing Distribution Date: " + progressBean.getDistributionDate());
                e.printStackTrace();
                return false; // Indicate failure
            }

            pstmt.setInt(4, progressBean.getQuantityDistributed());
            pstmt.setString(5, progressBean.getStatus());
            pstmt.setString(6, progressBean.getRemarks());

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
     * Updates an existing distribution progress record.
     *
     * @param progressBean The DistributionProgressBean with updated details.
     * @return true if the update was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean updateProgress(DistributionProgressBean progressBean) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            String sql = "UPDATE APP.DISTRIBUTION_PROGRESS SET BENEFICIARY_ID = ?, SUPPLY_ITEM = ?, DISTRIBUTION_DATE = ?, QUANTITY_DISTRIBUTED = ?, STATUS = ?, REMARKS = ? WHERE DISTRIBUTION_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, progressBean.getBeneficiaryId());
            pstmt.setString(2, progressBean.getSupplyItem());

            // Convert String distributionDate to java.sql.Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                java.util.Date parsedDate = dateFormat.parse(progressBean.getDistributionDate());
                pstmt.setDate(3, new java.sql.Date(parsedDate.getTime()));
            } catch (ParseException e) {
                System.err.println("Error parsing Distribution Date for update: " + progressBean.getDistributionDate());
                e.printStackTrace();
                return false; // Indicate failure
            }

            pstmt.setInt(4, progressBean.getQuantityDistributed());
            pstmt.setString(5, progressBean.getStatus());
            pstmt.setString(6, progressBean.getRemarks());
            pstmt.setInt(7, progressBean.getDistributionId()); // Set ID for WHERE clause

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
     * Checks if a distribution record for a specific beneficiary ID exists.
     * Note: This method might need refinement depending on what "exists" means for progress.
     * If you want to check for *any* progress for a beneficiary, this is fine.
     * If you want to check for a specific progress record by a unique ID, you'd use getProgressById.
     *
     * @param beneficiaryId The ID of the beneficiary to check.
     * @return true if a record for the beneficiary exists, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean isBeneficiaryProgressExists(String beneficiaryId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            String sql = "SELECT COUNT(*) FROM APP.DISTRIBUTION_PROGRESS WHERE BENEFICIARY_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, beneficiaryId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return exists;
    }

    /**
     * Retrieves a single distribution progress record by its Distribution ID.
     *
     * @param distributionId The unique ID of the distribution record.
     * @return A DistributionProgressBean object if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public DistributionProgressBean getProgressById(int distributionId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DistributionProgressBean progress = null;

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            String sql = "SELECT DISTRIBUTION_ID, BENEFICIARY_ID, SUPPLY_ITEM, DISTRIBUTION_DATE, QUANTITY_DISTRIBUTED, STATUS, REMARKS FROM APP.DISTRIBUTION_PROGRESS WHERE DISTRIBUTION_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, distributionId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                progress = new DistributionProgressBean();
                progress.setDistributionId(rs.getInt("DISTRIBUTION_ID"));
                progress.setBeneficiaryId(rs.getString("BENEFICIARY_ID"));
                progress.setSupplyItem(rs.getString("SUPPLY_ITEM"));
                if (rs.getDate("DISTRIBUTION_DATE") != null) {
                    progress.setDistributionDate(new SimpleDateFormat("yyyy-MM-dd").format(rs.getDate("DISTRIBUTION_DATE")));
                }
                progress.setQuantityDistributed(rs.getInt("QUANTITY_DISTRIBUTED"));
                progress.setStatus(rs.getString("STATUS"));
                progress.setRemarks(rs.getString("REMARKS"));
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return progress;
    }

    /**
     * Retrieves all distribution progress records.
     *
     * @return A list of DistributionProgressBean objects.
     * @throws SQLException if a database access error occurs.
     */
    public List<DistributionProgressBean> getAllProgressRecords() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<DistributionProgressBean> progressRecords = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            String sql = "SELECT DISTRIBUTION_ID, BENEFICIARY_ID, SUPPLY_ITEM, DISTRIBUTION_DATE, QUANTITY_DISTRIBUTED, STATUS, REMARKS FROM APP.DISTRIBUTION_PROGRESS";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                DistributionProgressBean progress = new DistributionProgressBean();
                progress.setDistributionId(rs.getInt("DISTRIBUTION_ID"));
                progress.setBeneficiaryId(rs.getString("BENEFICIARY_ID"));
                progress.setSupplyItem(rs.getString("SUPPLY_ITEM"));
                if (rs.getDate("DISTRIBUTION_DATE") != null) {
                    progress.setDistributionDate(new SimpleDateFormat("yyyy-MM-dd").format(rs.getDate("DISTRIBUTION_DATE")));
                }
                progress.setQuantityDistributed(rs.getInt("QUANTITY_DISTRIBUTED"));
                progress.setStatus(rs.getString("STATUS"));
                progress.setRemarks(rs.getString("REMARKS"));
                progressRecords.add(progress);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return progressRecords;
    }

    /**
     * Deletes a distribution progress record by its ID.
     *
     * @param distributionId The ID of the distribution record to delete.
     * @return true if deletion was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean deleteProgress(int distributionId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            conn = DriverManager.getConnection(JDBC_URL);
            String sql = "DELETE FROM APP.DISTRIBUTION_PROGRESS WHERE DISTRIBUTION_ID = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, distributionId);

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
