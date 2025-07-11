package servlet;

import bean.DistributionProgressBean; // Import the new DistributionProgressBean
import dao.DistributionProgressDao;   // Import the new DistributionProgressDao

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

@WebServlet("/DistributionProgressServlet")
public class DistributionProgressServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve parameters, matching new field names
            // Note: Distribution ID is auto-generated on INSERT, but used for UPDATE
            String distributionIdStr = request.getParameter("distributionId"); // Can be null for new records
            String beneficiaryId = request.getParameter("beneficiaryId");
            String supplyItem = request.getParameter("supplyItem");
            String distributionDate = request.getParameter("distributionDate");
            String quantityDistributedStr = request.getParameter("quantityDistributed");
            String status = request.getParameter("status");
            String remarks = request.getParameter("remarks"); // Renamed from notes

            int quantityDistributed = 0;
            try {
                quantityDistributed = Integer.parseInt(quantityDistributedStr);
            } catch (NumberFormatException e) {
                System.err.println("Invalid quantity distributed format: " + quantityDistributedStr);
                response.sendRedirect("trackProgress.jsp?status=invalid_quantity");
                return;
            }

            DistributionProgressDao progressDao = new DistributionProgressDao();
            DistributionProgressBean progressBean = new DistributionProgressBean();
            progressBean.setBeneficiaryId(beneficiaryId);
            progressBean.setSupplyItem(supplyItem);
            progressBean.setDistributionDate(distributionDate);
            progressBean.setQuantityDistributed(quantityDistributed);
            progressBean.setStatus(status);
            progressBean.setRemarks(remarks);

            boolean isUpdate = (distributionIdStr != null && !distributionIdStr.isEmpty());

            if (isUpdate) {
                int distributionId = Integer.parseInt(distributionIdStr);
                progressBean.setDistributionId(distributionId);
                // Update existing record
                progressDao.updateProgress(progressBean);
                response.sendRedirect("progressRecordsList.jsp?status=updated");
            } else {
                // Insert new record
                progressDao.addProgress(progressBean);
                response.sendRedirect("progressRecordsList.jsp?status=success");
            }
        } catch (NumberFormatException e) {
            System.err.println("Number format error in DistributionProgressServlet: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("trackProgress.jsp?status=invalid_input");
        } catch (SQLException e) {
            System.err.println("Database error in DistributionProgressServlet: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("trackProgress.jsp?status=sql_error");
        } catch (Exception e) {
            System.err.println("An unexpected error occurred in DistributionProgressServlet: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("trackProgress.jsp?status=unknown_error");
        }
    }
}