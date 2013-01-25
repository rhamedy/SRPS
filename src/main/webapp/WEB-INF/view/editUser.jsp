<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html xmlns="http://www.w3c.org/1999/xhtml">
	<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<title> Edit user </title> 
	</head>
	<script src="/SRPS/resources/js/jquery-1.6.1.min.js" type="text/javascript"></script>
    <script src="/SRPS/resources/js/jquery-ui-1.7.3.custom.min.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/SRPS/resources/css/ui-lightness/jquery-ui-1.7.3.custom.css" type="text/css"/>
    <script src="/SRPS/resources/js/datePicker.js" type="text/javascript"></script>
    <script src="/SRPS/resources/js/scripts.js" type="text/javascript"></script>
	<style>
		#main { 
				//border: 1px solid black; 
				padding: 5px 5px 5px 5px; 
		}
		#userDiv{
				//border: 1px solid black; 
				padding: 5px 5px 5px 5px;
				margin-top: 10px; 
		}
	</style>
	<script></script>
	<body>
		<div id="main">
			<div id="userDiv" align="center">
				<h3> Update user details </h3><br>
				
				<label for="username">Username</label>
				<input type="text" value="${user.email}" name="email" id="email" disabled /><br>
					
				<label for="firstName">First name </label>
				<input type="text" value="${user.firstName}" name="firstName" id="firstName"/><br>
					
				<label for="lastName">Last name </label>
				<input type="text" value="${user.lastName}" name="lastName" id="lastName"/><br>
					
				<label for="dateOfBirth">Date of birth</label>
				<input type="text" value="${user.dateOfBirth}" name="dateOfBirth" id="dateOfBirth"/><br><br>
				
				<label for="disabled">Account Status</label><br>
				<c:choose>
					<c:when test="${user.disabled}">
						<input type="radio" name="disabled" id="disabled" value='True' checked>Disable</input><br>
						<input type="radio" name="disabled" id="disabled" value='False'>Enable</input><br>
					</c:when>
					<c:otherwise>
						<input type="radio" name="disabled" id="disabled" value='True'>Disable</input><br>
						<input type="radio" name="disabled" id="disabled" value='False' checked>Enable</input><br>
					</c:otherwise>
				</c:choose><br>
				
				<label for="role">Roles</label><br>
				<c:choose>
					<c:when test="${role=='Admin'}">
						<input type="radio" name="role" id="role" value='Admin' checked>Admin</input><br>
						<input type="radio" name="role" id="role" value='User'>User</input><br>
					</c:when>
					<c:otherwise>
						<input type="radio" name="role" id="role" value='Admin'>Admin</input><br>
						<input type="radio" name="role" id="role" value='User' checked>User</input><br>
					</c:otherwise>
				</c:choose>
				<br /><br />
				<button type="button" id="updateButton" onclick="javascript:validateUserInput1()">Update</button><br><br>
				
				<p id="updateStatus"></p>
				<br><br><a href="javascript:gotoHomepage()"> Homepage </a>
			</div>
		</div> 
	</body>
</html>
	