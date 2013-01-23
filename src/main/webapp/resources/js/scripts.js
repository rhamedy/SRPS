function getRequestObject() {
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
	return request; 
}

function deleteForm(formId) { 
	var request = getRequestObject();

	request.onreadystatechange=function(){
		if (request.readyState==4 && request.status==200) {
			var e = document.getElementById(formId); 
			e.parentNode.removeChild(e); 	
		} else if(request.readyState==4 && request.status==302) { 
			alert("deleting failed!!!!");
		}
	}
	request.open("GET","/SRPS/form/delete?id=" + formId,true);
	request.setRequestHeader("Content-type", "application/x-www-form-urlencoded"); 
	request.send();
}

function deleteSubmission(submissionId) { 
	var request = getRequestObject(); 
	
	request.onreadystatechange=function(){
		if (request.readyState==4 && request.status==200) {
			var e = document.getElementById(submissionId); 
			e.parentNode.removeChild(e); 	
		} else if(request.readyState==4 && request.status==302) { 
			alert("deletiion failed!!!!");
		}
	}
	request.open("POST","/SRPS/submission/delete",true);
	request.setRequestHeader("Content-type", "application/x-www-form-urlencoded"); 
	request.send("submissionId=" + submissionId);
}

function userEditWindow(username) { 
	window.location = "http://localhost:8080/SRPS/user/edit?email=" + username; 
}

function validateUserInput1() {
	alert("inside validateUserInput1()"); 
	var firstName = document.getElementById("firstName").value; 
	var lastName = document.getElementById("lastName").value; 
	var date = document.getElementById("dateOfBirth").value; 
	var email = document.getElementById("email").value;  
				
	if(firstName.length < 2 || lastName.length < 2) { 
		alert("The inputed values are too small in size."); 
	} else { 
		updateUserDetails("firstName=" + firstName + "&lastName=" + lastName + "&dateOfBirth=" + date + "&email=" + email); 
	}
}

function updateUserDetails(data) { 
	alert("inside updateUserDetails"); 
	var disabled; 
	var role; 
	
	if(document.getElementsByName('disabled')[0].checked) { 
		disabled = document.getElementsByName('disabled')[0].value; 	
	} else { 
		disabled = document.getElementsByName('disabled')[1].value; 	
	}

	if(document.getElementsByName('role')[0].checked) { 
		role = document.getElementsByName('role')[0].value; 	
	} else { 
		role = document.getElementsByName('role')[1].value; 	
	}
	
	data = data + "&disabled=" + disabled + "&role=" + role; 

	var request = getRequestObject(); 

	request.onreadystatechange=function(){
		if (request.readyState==4 && request.status==200) {
			document.getElementById("updateStatus").innerHTML = request.getResponseHeader("Msg"); 
    			document.getElementById("updateStatus").style.color = "green"; 	
		} else if(request.readyState==4 && request.status==302) { 
			document.getElementById("updateStatus").innerHTML = request.getResponseHeader("Msg"); 
    			document.getElementById("updateStatus").style.color = "red"; 
		}
	}

	request.open("POST","/SRPS/user/edit",true);
	request.setRequestHeader("Content-type", "application/x-www-form-urlencoded"); 
	request.send(data);	
}


