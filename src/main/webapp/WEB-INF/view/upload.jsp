<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html xmlns="http://www.w3c.org/1999/xhtml">
	<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<title> Upload a file </title> 
	</head>
    <script src="/SRPS/resources/js/scripts.js" type="text/javascript"></script>
	<style>
		#main { 
				//border: 1px solid black; 
				padding: 5px 5px 5px 5px; 
		}
		#uploadWidget{
				//border: 1px solid black; 
				padding: 5px 5px 5px 5px;
				margin-top: 10px; 
		}
	</style>
	<body>
	<div id="main">
		<div id="uploadWidget" align="center">
			<h3>Upload Blank Survey Form (XML file & MapperFile)</h3><br><br>
			<form method="post" action="http://localhost:8080/SRPS/upload" enctype="multipart/form-data">
				<label for="multipartFile">File 1</label>
				<input type="file" id="multipartFile" name="multipartFile" /><br><br>
				<label for="multipartFile2">File 2</label>
				<input type="file" id="multipartFile2" name="multipartFile2" /><br><br>
				<label for="username">Select owner</label>
				<select name="username" id="username">
					<option value="everyone" selected>everyone</option>
					<c:forEach items="${users}" var="user">
						<option value="${user}">${user}</option>
					</c:forEach> 
				</select><br><br>
				<input type="submit" value="upload" name="upload"/>
				<br><br>
				<a href="javascript:gotoHomepage()">Homepage</a><br>
				<a href="javascript:logMeOut()">Logout</a>
			//</form>
		</div>
	</div>
	</body>
</html>