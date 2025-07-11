<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, java.util.*, bean.BeneficiaryBean, dao.BeneficiaryDao" %>
<%
    // Check if user is logged in, otherwise redirect to login page
    if (session.getAttribute("user") == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    // Changed from new ArrayList<>() to new ArrayList<BeneficiaryBean>()
    List<BeneficiaryBean> beneficiaries = new ArrayList<BeneficiaryBean>();
    BeneficiaryDao beneficiaryDao = new BeneficiaryDao();
    try {
        beneficiaries = beneficiaryDao.getAllBeneficiaries();
    } catch (SQLException e) {
        e.printStackTrace();
        request.setAttribute("errorMessage", "Error loading beneficiaries: " + e.getMessage());
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Disaster Relief - Request Supplies</title>
    <link href="[https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap](https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap)" rel="stylesheet">
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            background-color: #f4f6f9;
            color: #333;
        }
        .header {
            background-color: #007bff;
            color: white;
            padding: 15px 20px;
            text-align: center;
            font-size: 22px;
            font-weight: 600;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        .main-content {
            display: flex;
            flex: 1;
        }
        .sidebar {
            background-color: #343a40;
            color: white;
            width: 250px;
            display: flex;
            flex-direction: column;
            padding: 20px;
            box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
        }
        .sidebar h2 {
            font-size: 18px;
            text-align: center;
            margin-bottom: 20px;
            font-weight: 600;
            text-transform: uppercase;
            color: #e0e0e0;
        }
        .sidebar a {
            text-decoration: none;
            color: white;
            padding: 12px 15px;
            margin-bottom: 10px;
            border-radius: 8px;
            transition: background-color 0.3s ease, transform 0.2s ease;
            font-weight: 500;
            display: block;
            text-align: center;
        }
        .sidebar a:hover {
            background-color: #495057;
            transform: translateY(-2px);
        }
        .content-area {
            flex: 1;
            padding: 40px;
            display: flex;
            justify-content: center;
            align-items: flex-start;
        }
        .form-card {
            background-color: #ffffff;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 600px;
            box-sizing: border-box;
        }
        .form-card h1 {
            color: #007bff;
            margin-bottom: 25px;
            font-size: 28px;
            font-weight: 700;
            text-align: center;
        }
        .form-group {
            margin-bottom: 20px;
            text-align: left;
        }
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #555;
        }
        input[type="text"],
        input[type="date"],
        input[type="number"],
        select {
            width: calc(100% - 22px);
            padding: 12px;
            border: 1px solid #ced4da;
            border-radius: 8px;
            font-size: 16px;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }
        input[type="text"]:focus,
        input[type="date"]:focus,
        input[type="number"]:focus,
        select:focus {
            border-color: #007bff;
            box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
            outline: none;
        }
        textarea {
            width: calc(100% - 22px);
            padding: 12px;
            border: 1px solid #ced4da;
            border-radius: 8px;
            font-size: 16px;
            min-height: 80px;
            resize: vertical;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }
        textarea:focus {
            border-color: #007bff;
            box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
            outline: none;
        }
        button {
            width: 100%;
            padding: 14px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 18px;
            font-weight: 600;
            cursor: pointer;
            transition: background-color 0.3s ease, transform 0.2s ease;
            box-shadow: 0 4px 10px rgba(40, 167, 69, 0.3);
        }
        button:hover {
            background-color: #218838;
            transform: translateY(-2px);
        }
        .message {
            color: red;
            margin-top: 15px;
            font-weight: 500;
            text-align: center;
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            .main-content {
                flex-direction: column;
            }
            .sidebar {
                width: 100%;
                flex-direction: row;
                justify-content: space-around;
                min-height: auto;
                padding: 10px;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            }
            .sidebar h2 {
                display: none;
            }
            .content-area {
                padding: 20px;
            }
            .form-card {
                padding: 25px;
            }
        }
    </style>
</head>
<body>
    <div class="header">
        Disaster Relief Supply Management System
    </div>

    <div class="main-content">
        <div class="sidebar">
            <h2>Menu</h2>
            <a href="registerBeneficiary.jsp">Register Beneficiary</a>
            <a href="beneficiaryInformation.jsp">Beneficiary Information</a>
            <a href="requestSupply.jsp">Request Supplies</a>
            <a href="supplyRequestList.jsp">Supply Request List</a>
            <a href="trackProgress.jsp">Track Distribution</a>
            <a href="progressRecordsList.jsp">Distribution Records</a>
            <a href="LogoutServlet">Logout</a>
        </div>

        <div class="content-area">
            <div class="form-card">
                <h1>Request Supplies</h1>
                <%
                    String status = request.getParameter("status");
                    if ("invalid_quantity".equals(status)) {
                        out.println("<p class='message'>Invalid quantity provided.</p>");
                    } else if ("sql_error".equals(status)) {
                        out.println("<p class='message'>A database error occurred. Please try again later.</p>");
                    } else if ("unknown_error".equals(status)) {
                        out.println("<p class='message'>An unexpected error occurred. Please try again later.</p>");
                    } else if ("error".equals(status)) {
                        out.println("<p class='message'>Failed to submit supply request. Please check inputs.</p>");
                    }
                    String errorMessage = (String) request.getAttribute("errorMessage");
                    if (errorMessage != null) {
                        out.println("<p class='message'>" + errorMessage + "</p>");
                    }
                %>
                <form action="SupplyRequestServlet" method="post">
                    <div class="form-group">
                        <label for="requestId">Request ID:</label>
                        <input type="text" id="requestId" name="requestId" required>
                    </div>
                    <div class="form-group">
                        <label for="beneficiaryId">Beneficiary:</label>
                        <select id="beneficiaryId" name="beneficiaryId" required>
                            <option value="">-- Select Beneficiary --</option>
                            <%
                                if (beneficiaries != null && !beneficiaries.isEmpty()) {
                                    // Iteration is fine for Java 1.5+
                                    for (BeneficiaryBean b : beneficiaries) {
                            %>
                                <option value="<%= b.getBeneficiaryId() %>"><%= b.getBeneficiaryName() %> (<%= b.getBeneficiaryId() %>)</option>
                            <%
                                    }
                                } else {
                                    out.println("<option value='' disabled>No beneficiaries registered</option>");
                                }
                            %>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="requestDate">Request Date:</label>
                        <input type="date" id="requestDate" name="requestDate" required>
                    </div>
                    <div class="form-group">
                        <label for="supplyItem">Supply Item:</label>
                        <input type="text" id="supplyItem" name="supplyItem" placeholder="e.g., Water Bottles, Food Packs, Tents" required>
                    </div>
                    <div class="form-group">
                        <label for="quantity">Quantity:</label>
                        <input type="number" id="quantity" name="quantity" min="1" required>
                    </div>
                     <div class="form-group">
                        <label for="status">Status:</label>
                        <select id="status" name="status" required>
                            <option value="Pending">Pending</option>
                            <option value="Approved">Approved</option>
                            <option value="Rejected">Rejected</option>
                            <option value="Fulfilled">Fulfilled</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="notes">Notes:</label>
                        <textarea id="notes" name="notes" rows="3"></textarea>
                    </div>
                    <button type="submit">Submit Request</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>