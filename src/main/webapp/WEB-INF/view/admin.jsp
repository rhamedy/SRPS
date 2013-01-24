<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html xmlns="http://www.w3c.org/1999/xhtml">
	<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<title> Profile (Admin) </title> 
	</head>
	<script src="/SRPS/resources/js/jquery-1.6.1.min.js" type="text/javascript"></script>
    <script src="/SRPS/resources/js/jquery-ui-1.7.3.custom.min.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/SRPS/resources/css/ui-lightness/jquery-ui-1.7.3.custom.css" type="text/css"/>
    <script src="/SRPS/resources/js/datePicker.js" type="text/javascript"></script>
    <script src="/SRPS/resources/js/scripts.js" type="text/javascript"></script>
	<style>
		#main { 
				border: 1px solid black; 
				padding: 5px 5px 5px 5px; 
		}
		#adminDetails{
				border: 1px solid black; 
				padding: 5px 5px 5px 5px;
				margin-top: 10px; 
		}
		#users { 
				border: 1px solid black; 
				padding: 5px 5px 5px 5px;
				margin-top: 10px; 
		}
		#submissions { 
				border: 1px solid black; 
				padding: 5px 5px 5px 5px;
				margin-top: 10px; 
		}
		#forms { 
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
		function enableEdit() { 
			document.getElementById("firstName").disabled = false; 
			document.getElementById("lastName").disabled = false; 
			document.getElementById("dateOfBirth").disabled = false; 
			document.getElementById("updateButton").disabled = false; 
			document.getElementById("editButton").disabled = true; 
		}
		
		function validateUserInput() {
			var firstName = document.getElementById("firstName").value; 
			var lastName = document.getElementById("lastName").value; 
			var date = document.getElementById("dateOfBirth").value; 
			var email = document.getElementById("email").value; 
			var parsedDate; 
				
			if(firstName.length < 2 || lastName.length < 2) { 
				alert("The inputed values are too small in size."); 
			} else { 
				updateUser("firstName=" + firstName + "&lastName=" + lastName + "&dateOfBirth=" + date + "&email=" + email); 
			}
		}
		
		function updateUser(data) {
				var request;
				if (window.XMLHttpRequest)
 				{
 					// code for IE7+, Firefox, Chrome, Opera, Safari
  					request=new XMLHttpRequest();
  				}
				else {
					// code for IE6, IE5
  					request=new ActiveXObject("Microsoft.XMLHTTP");
  				}
				request.onreadystatechange=function(){
  					if (request.readyState==4 && request.status==200){
    					document.getElementById("updateStatus").innerHTML = request.getResponseHeader("Msg"); 
    					document.getElementById("updateStatus").style.color = "green"; 
    					document.getElementById("firstName").disabled = true; 
    					document.getElementById("lastName").disabled = true; 
    					document.getElementById("dateOfBirth").disabled = true;
    					document.getElementById("updateButton").disabled = true; 
    					document.getElementById("editButton").disabled = false;
    				} else if(request.readyState==4 && request.status==302) { 
    					document.getElementById("updateStatus").innerHTML = request.getResponseHeader("Msg"); 
    					document.getElementById("updateStatus").style.color = "red"; 
    				}
 				}
				request.open("POST","/SRPS/user/update",true);
				request.setRequestHeader("Content-type", "application/x-www-form-urlencoded"); 
				request.send(data);
			}
			
		</script>
		
	</script>
	<body>
		<div id="main">
			<div id="adminDetails">
				<p> admin details goes here. </p>
				
				<label for="username">Username</label>
				<input type="text" value="${user.email}" name="email" id="email" disabled /><br />
					
				<label for="firstName">First name </label>
				<input type="text" value="${user.firstName}" name="firstName" id="firstName" disabled /><br />
					
				<label for="lastName">Last name </label>
				<input type="text" value="${user.lastName}" name="lastName" id="lastName" disabled /><br />
					
				<label for="dateOfBirth">Date of birth</label>
				<input type="text" value="${user.dateOfBirth}" name="dateOfBirth" id="dateOfBirth" disabled /><br /><br />
					
				<button type="button" id="editButton" onclick="javascript:enableEdit()">Enable Edit</button>
				<button type="button" id="updateButton" onclick="javascript:validateUserInput()" disabled>Update</button><br /><br />
					
				<p id="updateStatus"></p>
			</div>
			<div id="users">
				<p> list of users goes here. </p>
				
				<table border="1">
					<thead>
						<tr>
							<th>username</th>
							<th>first name</th>
							<th>last name</th>
							<th>date of birth</th>
							<th>disabled</th>
							<th>actions</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${users}" var="u">
							<tr id="${u.email}">
								<td>${u.email}</td>
								<td>${u.firstName}</td>
								<td>${u.lastName}</td>
								<td>${u.dateOfBirth}</td>
								<td>${u.disabled}</td>
								<td><a href="javascript:userEditWindow('${u.email}')">Update</a>|<a href="javascript:deleteUser('${u.email}')">delete</a>|<a href="javascript:resetPassword('${u.email}')">reset password</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table> 
			</div>
			<div id="forms">
				<p> list of forms goes here. </p>
				
				<table border="1">
					<thead>
						<tr>
							<th>form name</th>
							<th>action</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${forms}" var="f">
							<tr id="${f.key}">
								<td value="${f.key}">${f.value}</td>
								<td><a href="javascript:deleteForm('${f.key}')">delete</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div id="submissions">
				<p> list of submissions goes here. </p>
				
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
								<tr id="${s.submissionId}">
									<td>${s.submissionId}</td>
									<td>${s.formName}</td>
									<td>${s.username}</td>
									<td>${s.image}</td>
									<td>${s.audio}</td>
									<td>${s.geopoint}</td>
									<td>${s.submissionDate}</td>
									<td><a href="javascript:viewSubmission('${s.submissionId}')"> view </a> | <a href="javascript:deleteSubmission('${s.submissionId}')"> delete </a> | <a href=""> export </a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
			</div>
			<div id="footer">
				<p> footer goes here </p>
			</div>
		</div>
	</body>
</html>