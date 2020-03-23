<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
 	 
 	 <a href="/logout"> logout </a>
 	 
 	 <h1>Upload a link</h1>

	${SPRING_SECURITY_LAST_EXCEPTION.message}
	<form action="link" method='POST' >
		<table>
		<tr>
		<td>Amazon Link:</td>
		<td><input type="text" name="link"> <br></td>
		</tr>
		<tr>
		<td><input name="submit" type="submit" value="submit"> <br></td>
		</tr>
		</table>
		</form>
 	 
 	 
 
 	 
 	 
 	 
 	 
 	 
 	 
 	 
</body>
</html>