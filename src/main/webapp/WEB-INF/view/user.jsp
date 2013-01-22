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
		function enableEdit() { 
			document.getElementById("firstName").style.disabled = false; 
			document.getElementById("lastName").style.disabled = false; 
			document.getElementById("dateOfBirth").style.disabled = false; 
			document.getElementById("updateButton").style.disabled = false; 
			document.getElementById("editButton").style.disabled = true; 
		}
		
		function validateUserInput() {
				var firstName = document.getElementById("firstName").value; 
				var lastName = document.getElementById("lastName").value; 
				var date = document.getElementById("dateOfBirth").value; 
				var email = document.gtElementById(("email")>value;
				
				if(firstName.length < 2 && lastName.length < 2) { 
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
	<body>
		<div id="main">
				<div id="userDetails">
					<p> User details will go here </p>
					
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
									<td><a href=""> view </a> | <a href=""> edit </a> | <a href=""> delete </a> | <a href=""> export </a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div id="userForms">
					<p> list of forms belonging to user </p>
					<table border="1">
						<thead>
							<tr>
								<th>form name</th>
								<th>action</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${forms}" var="f">
								<tr>
									<td value="${f.key}">${f.value}</td>
									<td><a href="">View</a> | <a href="">Delete</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div> 
				<div id="footer">
					<p> information about footer </p>
				</div>
		</div>
	</body>
</html>