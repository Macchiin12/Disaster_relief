package servlet;

import bean.SupplyRequestBean; // Import the new SupplyRequestBean
import dao.SupplyRequestDao;   // Import the new SupplyRequestDao

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException; // Import SQLException

@WebServlet("/SupplyRequestServlet")
public class SupplyRequestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve parameters from the request, matching new field names
        String requestId = request.getParameter("requestId"); // New field
        String beneficiaryId = request.getParameter("beneficiaryId");
        String requestDate = request.getParameter("requestDate");
        String supplyItem = request.getParameter("supplyItem");
        String quantityStr = request.getParameter("quantity"); // Read as String, then parse
        String status = request.getParameter("status"); // New field
        String notes = request.getParameter("notes");

        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            System.err.println("Invalid quantity format: " + quantityStr);
            response.sendRedirect("requestSupply.jsp?status=invalid_quantity");
            return;
        }

        SupplyRequestBean supplyRequest = new SupplyRequestBean();
        supplyRequest.setRequestId(requestId);
        supplyRequest.setBeneficiaryId(beneficiaryId);
        supplyRequest.setRequestDate(requestDate);
        supplyRequest.setSupplyItem(supplyItem);
        supplyRequest.setQuantity(quantity);
        supplyRequest.setStatus(status);
        supplyRequest.setNotes(notes);

        SupplyRequestDao dao = new SupplyRequestDao();
        boolean isScheduled = false; // Renamed from isScheduled to reflect supply request
        try {
            isScheduled = dao.scheduleSupplyRequest(supplyRequest); // Use new DAO method

            if (isScheduled) {
                response.sendRedirect("supplyRequestList.jsp?status=success"); // Redirect to list page
            } else {
                response.sendRedirect("requestSupply.jsp?status=error"); // Redirect back to form
            }
        } catch (SQLException e) {
            System.err.println("Database error during supply request: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("requestSupply.jsp?status=sql_error");
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during supply request: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("requestSupply.jsp?status=unknown_error");
        }
    }
}