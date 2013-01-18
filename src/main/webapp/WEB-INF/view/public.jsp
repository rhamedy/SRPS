<!DOCTYPE html>
<html xmlns="http://www.w3c.org/1999/xhtml">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<title> Survey Retrieval and Processing System </title> 
		<style>
			#main { 
				border: 1px solid black; 
				padding: 5px 5px 5px 5px; 
			}
			
			#about { 
				border: 1px solid black; 
				padding: 5px 5px 5px 5px;
				margin-top: 10px; 
			}
			
			#submissions { 
				border: 1px solid black; 
				padding: 5px 5px 5px 5px;
				margin-top: 10px; 
			}
			
			#statistics { 
				border: 1px solid black; 
				padding: 5px 5px 5px 5px;
				margin-top: 10px; 
			}
			
			#account { 
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
	</head>
	<body>
		<div id="main">
			<div id="about">
				<p> this is where information about website goes. </p>
			</div> 
			<div id="submissions">
				<p> this is where list of submissions with public visibility goes. </p>
				
				<table border="1"> 
					<thead>
						<tr>
							<th><submission id </th>
							<th>form name</th>
							<th>username</th>
							<th>has image</th>
							<th>has audio</th>
							<th><td>has geopoint</td></th>
							<th><td>submission date</td><th>
						</tr>
					</thead>
					</tbody>
						<tr>
							<td>
						</tr>
					</tbody>
				</table> 
				
				<select name="submissions">
					<option value="everyone" selected>everyone</option>
					<c:forEach items="${users}" var="user">
						<option value="${user}">${user}</option>
					</c:forEach> 
				</select>
			
			</div> 
			<div id="statistics">
				<p> this is where submissions statistics (charts) goes. </p>
			</div> 
			<div id="account">
				<p> this is where account request and login controls goes. </p>
			</div>
			<div id="footer">
				<p> this is where page footer goes. </p>
			</div>
		</div>
	</body>
</html>