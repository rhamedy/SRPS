<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html xmlns="http://www.w3c.org/1999/xhtml">
	<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<title> Upload a file </title> 
	</head>
	<body>
		<h1>Please upload a file</h1>
		<form method="post" action="http://localhost:8080/SRPS/upload" enctype="multipart/form-data">
			<input type="file" name="multipartFile" />
			<input type="file" name="multipartFile2" />
			<select name="username">
				<option value="everyone" selected>everyone</option>
				<c:forEach items="${users}" var="user">
					<option value="${user}">${user}</option>
				</c:forEach> 
			</select>
			<input type="submit" name="upload"/>
		</form>
	</body>
</html>