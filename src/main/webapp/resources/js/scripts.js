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


