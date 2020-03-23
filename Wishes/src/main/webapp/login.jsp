<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body><h1>LogIn</h1>

	${SPRING_SECURITY_LAST_EXCEPTION.message}
	<form action="login" method='POST' >
		<table>
		<tr>
		<td>UserName:</td>
		<td><input type="text" name="username"> <br></td>
		</tr>
		<tr>
		<td>Password:</td>
		<td><input type="password" name="password"> <br></td>
		</tr>
		<tr>
		<td><input name="submit" type="submit" value="submit"> <br></td>
		
		</tr>
		</table>
		</form>
		<form action="/signup">
    <input type="submit" value="SignUp" />
</form>

</body>
</html>