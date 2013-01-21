<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html xmlns="http://www.w3c.org/1999/xhtml">
	<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<title> Profile (User) </title> 
	</head>
	<style>
		#main { 
				border: 1px solid black; 
				padding: 5px 5px 5px 5px; 
		}
		#userDetails{
				border: 1px solid black; 
				padding: 5px 5px 5px 5px;
				margin-top: 10px; 
		}
		#userForms { 
				border: 1px solid black; 
				padding: 5px 5px 5px 5px;
				margin-top: 10px; 
		}
		#submissions { 
				border: 1px solid black; 
				padding: 5px 5px 5px 5px;
				margin-top: 10px; 
		}
		#footer { 
				border: 1px solid black; 
				padding: 5px 5px 5px 5px;
				margin-top: 10px; 
		}  
	</style>
	<script type="text/javascript">
		
	</script>
	<body>
		<div id="main">
				<div id="userDetails">
					<p> User details will go here </p>
					
					<label for="username">Username</label>
					<input type="text" value="mock@gmail.com" name="email" id="email" disabled /><br />
					
					<label for="firstName">First name </label>
					<input type="text" value="Rafal" name="firstName" id="firstName" disabled /><br />
					
					<label for="lastName">Last name </label>
					<input type="text" value="Fareen" name="lastName" id="lastName" disabled /><br />
					
					<label for="dateOfBirth">Date of birth</label>
					<input type="text" value="01-01-1986" name="dateOfBirth" id="dateOfBirth" disabled /><br /><br />
					
					<button type="button" onclick="javascript:alert('edit')">Enable Edit</button>
					<button type="button" onclick="javascript:alert('update')" disabled>Update</button><br /><br />
					
					<p id="updateStatus"></p>
				</div>
				<div id="submissions">
					<p> this is where list of user submissions go </p>
					<table border="1"> 
						<thead>
							<tr>
								<th>submission id</th>
								<th>form name</th>
								<th>username</th>
								<th>has image</th>
								<th>has audio</th>
								<th>has geopoint</th>
								<th>submission date</th>
								<th>actions</th>
							</tr>
						</thead>
						</tbody>
							<c:forEach items="${submissions}" var="s">
								<tr>
									<td>${s.submissionId}</td>
									<td>${s.formName}</td>
									<td>${s.username}</td>
									<td>${s.image}</td>
									<td>${s.audio}</td>
									<td>${s.geopoint}</td>
									<td>${s.submissionDate}</td>
									<td><a href=""> view </a> | <a href=""> edit </a> | <a href=""> export </a> | <a href=""> export </a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div id="userForms">
				</div> 
				<div id="footer">
					<p> information about footer </p>
				</div>
		</div>
	</body>
</html>