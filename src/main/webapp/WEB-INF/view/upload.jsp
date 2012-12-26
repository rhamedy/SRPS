<html>
	<head>
		<title> Upload a file </title> 
	</head>
	<body>
		<h1>Please upload a file</h1>
		<form method="post" action="http://localhost:8080/SRPS/upload" enctype="multipart/form-data">
			<input type="file" name="multipartFile" />
			<input type="file" name="multipartFile2" />
			<input type="submit" name="upload"/>
		</form>
	</body>
</html>