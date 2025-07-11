<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, java.util.*, bean.SupplyRequestBean, dao.SupplyRequestDao" %>
<%
    // Check if user is logged in, otherwise redirect to login page
    if (session.getAttribute("user") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Disaster Relief - Supply Request List</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
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
        .data-card {
            background-color: #ffffff;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 1000px;
            box-sizing: border-box;
            text-align: center;
        }
        .data-card h1 {
            color: #007bff;
            margin-bottom: 25px;
            font-size: 28px;
            font-weight: 700;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        th, td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #007bff;
            color: white;
            text-transform: uppercase;
            font-weight: 600;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        tr:hover {
            background-color: #e9ecef;
        }
        .actions {
            display: flex;
            gap: 10px;
            justify-content: center;
            align-items: center;
        }
        .actions a {
            text-decoration: none;
            color: white;
            background-color: #007bff;
            padding: 8px 12px;
            border-radius: 5px;
            font-size: 14px;
            transition: 0.3s;
            display: inline-block;
            margin: 2px;
        }
        .actions a:hover {
            background-color: #0056b3;
        }
        .delete-btn {
            background-color: #dc3545 !important;
            color: white !important;
        }
        .delete-btn:hover {
            background-color: #a71d2a !important;
        }
        .no-records {
            text-align: center;
            padding: 20px;
            color: #777;
            font-style: italic;
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
            .data-card {
                padding: 25px;
            }
            table, thead, tbody, th, td, tr {
                display: block;
            }
            thead tr {
                position: absolute;
                top: -9999px;
                left: -9999px;
            }
            tr {
                border: 1px solid #ccc;
                margin-bottom: 10px;
                border-radius: 8px;
                overflow: hidden;
            }
            td {
                border: none;
                border-bottom: 1px solid #eee;
                position: relative;
                padding-left: 50%;
                text-align: right;
            }
            td:before {
                position: absolute;
                top: 0;
                left: 6px;
                width: 45%;
                padding-right: 10px;
                white-space: nowrap;
                text-align: left;
                font-weight: bold;
                color: #555;
            }
            td:nth-of-type(1):before { content: "Request ID"; }
            td:nth-of-type(2):before { content: "Beneficiary ID"; }
            td:nth-of-type(3):before { content: "Request Date"; }
            td:nth-of-type(4):before { content: "Supply Item"; }
            td:nth-of-type(5):before { content: "Quantity"; }
            td:nth-of-type(6):before { content: "Status"; }
            td:nth-of-type(7):before { content: "Notes"; }
            td:nth-of-type(8):before { content: "Actions"; }
            .actions {
                justify-content: flex-end;
                padding-right: 6px;
            }
        }
    </style>
    <script>
        function confirmDelete(requestId) {
            if (confirm("Are you sure you want to delete request ID: " + requestId + "?")) {
                window.location.href = "supplyRequestList.jsp?action=delete&id=" + requestId;
            }
        }
    </script>
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
            <div class="data-card">
                <h1>Supply Request List</h1>
                <%
                    SupplyRequestDao supplyRequestDao = new SupplyRequestDao();
                    // Changed from new ArrayList<>() to new ArrayList<SupplyRequestBean>()
                    List<SupplyRequestBean> requests = new ArrayList<SupplyRequestBean>();
                    String statusMessage = request.getParameter("status");

                    if ("success".equals(statusMessage)) {
                        out.println("<p style='color: green; font-weight: bold;'>Supply request submitted successfully!</p>");
                    } else if ("deleted".equals(statusMessage)) {
                        out.println("<p style='color: green; font-weight: bold;'>Supply request deleted successfully!</p>");
                    } else if ("error".equals(statusMessage)) {
                        out.println("<p style='color: red; font-weight: bold;'>An error occurred. Please try again.</p>");
                    } else if ("sql_error".equals(statusMessage)) {
                        out.println("<p style='color: red; font-weight: bold;'>A database error occurred. Please try again later.</p>");
                    } else if ("invalid_quantity".equals(statusMessage)) {
                        out.println("<p style='color: red; font-weight: bold;'>Invalid quantity for supply request.</p>");
                    }

                    try {
                        // Handle delete operation if requested
                        if ("delete".equals(request.getParameter("action"))) {
                            String idToDelete = request.getParameter("id");
                            if (idToDelete != null && !idToDelete.isEmpty()) {
                                boolean deleted = supplyRequestDao.deleteSupplyRequest(idToDelete);
                                if (deleted) {
                                    response.sendRedirect("supplyRequestList.jsp?status=deleted");
                                    return;
                                } else {
                                    response.sendRedirect("supplyRequestList.jsp?status=error");
                                    return;
                                }
                            }
                        }

                        // Retrieve all supply requests
                        requests = supplyRequestDao.getAllSupplyRequests();

                        if (requests.isEmpty()) {
                            out.println("<p class='no-records'>No supply requests found.</p>");
                        } else {
                %>
                <table>
                    <thead>
                        <tr>
                            <th>Request ID</th>
                            <th>Beneficiary ID</th>
                            <th>Request Date</th>
                            <th>Supply Item</th>
                            <th>Quantity</th>
                            <th>Status</th>
                            <th>Notes</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            for (SupplyRequestBean req : requests) {
                        %>
                        <tr>
                            <td><%= req.getRequestId() %></td>
                            <td><%= req.getBeneficiaryId() %></td>
                            <td><%= req.getRequestDate() %></td>
                            <td><%= req.getSupplyItem() %></td>
                            <td><%= req.getQuantity() %></td>
                            <td><%= req.getStatus() %></td>
                            <td><%= req.getNotes() %></td>
                            <td class="actions">
                                <%-- No direct edit for supply requests in this simplified version, only delete --%>
                                <%-- If you need edit, you'd create editSupplyRequest.jsp and a corresponding servlet --%>
                                <a href="javascript:void(0);" class="delete-btn" onclick="confirmDelete('<%= req.getRequestId() %>');">Delete</a>
                            </td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
                <%
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        out.println("<p class='no-records' style='color:red;'>Database error: " + e.getMessage() + "</p>");
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.println("<p class='no-records' style='color:red;'>An unexpected error occurred: " + e.getMessage() + "</p>");
                    }
                %>
            </div>
        </div>
    </div>
</body>
</html>
