package servlet;

import bean.BeneficiaryBean;
import dao.BeneficiaryDao;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

@WebServlet("/BeneficiaryRegistrationServlet")
public class BeneficiaryRegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve parameters from the request
        String beneficiaryId = request.getParameter("beneficiaryId");
        String beneficiaryName = request.getParameter("beneficiaryName");
        String icNumber = request.getParameter("icNumber");
        String address = request.getParameter("address");
        String contactNumber = request.getParameter("contactNumber");
        String dateOfBirth = request.getParameter("dateOfBirth");

        // Create a BeneficiaryBean object and populate it
        BeneficiaryBean beneficiaryBean = new BeneficiaryBean();
        beneficiaryBean.setBeneficiaryId(beneficiaryId);
        beneficiaryBean.setBeneficiaryName(beneficiaryName);
        beneficiaryBean.setIcNumber(icNumber);
        beneficiaryBean.setAddress(address);
        beneficiaryBean.setContactNumber(contactNumber);
        beneficiaryBean.setDateOfBirth(dateOfBirth);

        BeneficiaryDao beneficiaryDao = new BeneficiaryDao();
        boolean success = false;
        String action = request.getParameter("action");

        try {
            if ("update".equals(action)) {
                // For update, the IC Number and Beneficiary Name are read-only in the JSP.
                // However, the DAO's update method still expects them.
                // We need to ensure the bean has the original (or correct) IC Number and Name
                // before calling updateBeneficiary.
                // The current implementation of editBeneficiaryInformation.jsp sends them as hidden fields,
                // so they should be available in the request parameters.

                // The primary issue was the unimplemented DAO methods.
                // Now that they are implemented, the check below will work.
                // If IC number is unique or unchanged, proceed with update
                success = beneficiaryDao.updateBeneficiary(beneficiaryBean);
                if (success) {
                    response.sendRedirect(request.getContextPath() + "/beneficiaryInformation.jsp?status=updated");
                } else {
                    request.setAttribute("errorMessage", "Beneficiary update failed. Please try again.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/editBeneficiaryInformation.jsp?id=" + beneficiaryId);
                    dispatcher.forward(request, response);
                }
            } else { // This is a new registration (insert operation)
                // Check if IC number already exists for new registrations
                if (beneficiaryDao.isIcNumberExists(icNumber)) {
                    request.setAttribute("errorMessage", "IC Number '" + icNumber + "' is already registered.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/registerBeneficiary.jsp");
                    dispatcher.forward(request, response);
                    return;
                }

                success = beneficiaryDao.registerBeneficiary(beneficiaryBean);
                if (success) {
                    response.sendRedirect(request.getContextPath() + "/beneficiaryInformation.jsp?status=success");
                } else {
                    request.setAttribute("errorMessage", "Beneficiary registration failed. Please try again.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/registerBeneficiary.jsp");
                    dispatcher.forward(request, response);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during beneficiary operation: " + e.getMessage());
            e.printStackTrace(); // Log the exception for troubleshooting
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            // Redirect based on action
            if ("update".equals(action)) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/editBeneficiaryInformation.jsp?id=" + beneficiaryId);
                dispatcher.forward(request, response);
            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/registerBeneficiary.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during beneficiary operation: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            // Redirect based on action
            if ("update".equals(action)) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/editBeneficiaryInformation.jsp?id=" + beneficiaryId);
                dispatcher.forward(request, response);
            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("/registerBeneficiary.jsp");
                dispatcher.forward(request, response);
            }
        }
    }
}
