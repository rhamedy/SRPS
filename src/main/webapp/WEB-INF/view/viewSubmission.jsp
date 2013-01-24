<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html xmlns="http://www.w3c.org/1999/xhtml">
	<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<title> Upload a file </title> 
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
		#submissionView{
				border: 1px solid black; 
				padding: 5px 5px 5px 5px;
				margin-top: 10px; 
		}
	</style>
	<body>
		<h1>Content of the submission is as follow; </h1>
		<br /><br />
		<div id ="main">
			<div id="submissionView">
				${htmlContent}
			</div>
		</div>
	</body>
</html>