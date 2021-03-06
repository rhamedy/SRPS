<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html xmlns="http://www.w3c.org/1999/xhtml">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<title> Survey Retrieval and Processing System </title> 
		<style>
			#main { 
				//border: 1px solid black; 
				padding: 5px 5px 5px 5px; 
			}
			
			#about { 
				//border: 1px solid black; 
				padding: 5px 5px 5px 5px;
				margin-top: 10px; 
			}
			
			#submissions { 
				//border: 1px solid black; 
				padding: 5px 5px 5px 5px;
				margin-top: 10px; 
			}
			
			#statistics { 
				//border: 1px solid black; 
				padding: 5px 5px 5px 5px;
				margin-top: 10px; 
			}
			
			#account { 
				border: 1px solid black; 
				padding: 5px 5px 5px 5px;
				margin-top: 10px; 
			} 
			
			 
		</style>
		<script type="text/javascript"> 
		
			function validateUserInput() {
				var firstName = document.getElementById("firstName").value; 
				var lastName = document.getElementById("lastName").value; 
				var email = document.getElementById("email").value; 
				
				if(firstName.length < 2 || lastName.length < 2 || email.length < 5) { 
					alert("The inputed values are too small in size."); 
				} else { 
					if(email.indexOf("@") == -1 || (email.substring(email.indexOf("@"),email.length)).indexOf(".") == -1) { 
						alert("invalid email address."); 
					} else { 
						createNewUser("firstName=" + firstName + "&lastName=" + lastName + "&email=" + email); 
					}
				}
			}
			
			function createNewUser(data) {
			
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
    					document.getElementById("accountStatus").innerHTML = request.getResponseHeader("Msg"); 
    					document.getElementById("accountStatus").style.color = "green"; 
    					document.getElementById("firstName").value = ""; 
    					document.getElementById("lastName").value = ""; 
    					document.getElementById("email").value = "";
    				} else if(request.readyState==4 && request.status==302) { 
    					document.getElementById("accountStatus").innerHTML = request.getResponseHeader("Msg"); 
    					document.getElementById("accountStatus").style.color = "red"; 
    				}
 				}
				request.open("POST","/SRPS/public/account",true);
				request.setRequestHeader("Content-type", "application/x-www-form-urlencoded"); 
				request.send(data);
			}
		</script>
	</head>
	<body>
		<div id="main">
			<div id="about">
				<h2> Survey Retrieval and Processing System </h2>
				<br>
				<h4> Information about Web application </h4><br>
				<p> This web application provide web services to the an open source mobile application </p>
				<p> which is used to download surveys within this system, fill the surveys in smartphone</p>
				<p> and submit the surveys back to the system. </p>
				<br>
			</div> 
			<div id="submissions" alighn="center">
				<h4> all those submissions that are available for public users </h4><br>
				
				<table border="1" cellspacing="0"> 
					<thead>
						<tr>
							<th>submission id</th>
							<th>form name</th>
							<th>username</th>
							<th>has image</th>
							<th>has audio</th>
							<th>has geopoint</th>
							<th>submission date</th>
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
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div> 
			<div id="account">
				<h4> this is where account request and login controls goes. </h4>
				
				<label for="firstName"> First name </label>
				<input type="text" name="firstName" id="firstName"></input><br>
				
				<label for="lastName"> Last name </label>
				<input type="text" name="lastName" id="lastName"></input><br>
				
				<label for="email"> Email </label>
				<input type="text" name="email" id="email"></input><br><br>
				<p id="accountStatus"></p>
				
				<button type="button" onclick="javascript:validateUserInput()"> Submit </button>
			</div>
		</div>
	</body>
</html>