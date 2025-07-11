<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <title>Disaster Relief - Dashboard</title>
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
        .dashboard-main {
            flex: 1;
            padding: 40px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            text-align: center;
        }
        .welcome-message {
            font-size: 36px;
            color: #007bff;
            margin-bottom: 20px;
            font-weight: 700;
        }
        .dashboard-info {
            font-size: 18px;
            color: #555;
            line-height: 1.6;
            max-width: 700px;
            margin-bottom: 30px;
        }
        .dashboard-cards {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 25px;
            width: 100%;
            max-width: 900px;
        }
        .card {
            background-color: #ffffff;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
            text-align: center;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            cursor: pointer;
        }
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
        }
        .card h3 {
            color: #007bff;
            font-size: 22px;
            margin-bottom: 15px;
            font-weight: 600;
        }
        .card p {
            color: #666;
            font-size: 15px;
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
                display: none; /* Hide menu title on small screens */
            }
            .dashboard-main {
                padding: 20px;
            }
            .welcome-message {
                font-size: 28px;
            }
            .dashboard-info {
                font-size: 16px;
            }
            .dashboard-cards {
                grid-template-columns: 1fr; /* Stack cards on small screens */
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

        <div class="dashboard-main">
            <h1 class="welcome-message">Welcome, <%= session.getAttribute("user") %>!</h1>
            <p class="dashboard-info">
                This dashboard provides an overview of the Disaster Relief Supply Management System.
                You can manage beneficiaries, track supply requests, and monitor distribution progress.
            </p>

            <div class="dashboard-cards">
                <a href="registerBeneficiary.jsp" class="card">
                    <h3>Register Beneficiary</h3>
                    <p>Add new individuals or families requiring aid.</p>
                </a>
                <a href="beneficiaryInformation.jsp" class="card">
                    <h3>Beneficiary Information</h3>
                    <p>View, edit, or delete registered beneficiary details.</p>
                </a>
                <a href="requestSupply.jsp" class="card">
                    <h3>Request Supplies</h3>
                    <p>Submit new requests for essential relief supplies.</p>
                </a>
                <a href="supplyRequestList.jsp" class="card">
                    <h3>Supply Request List</h3>
                    <p>Track and manage all submitted supply requests.</p>
                </a>
                <a href="trackProgress.jsp" class="card">
                    <h3>Track Distribution</h3>
                    <p>Record and update the progress of supply distribution.</p>
                </a>
                <a href="progressRecordsList.jsp" class="card">
                    <h3>Distribution Records</h3>
                    <p>View all past and ongoing supply distribution records.</p>
                </a>
            </div>
        </div>
    </div>
</body>
</html>
