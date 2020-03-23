<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm"%>
<%@taglib uri = "http://www.springframework.org/tags/form" prefix = "form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<style>
      .error {
         color: #ff0000;
      }

      .errorblock {
         color: #000;
         background-color: #ffEEEE;
         border: 3px solid #ff0000;
         padding: 8px;
         margin: 16px;
      }
   </style>





<body><h1>Sign Up</h1>
	<h2><spring:message code="lbl.page" text="SignUp" /></h2>
	${SPRING_SECURITY_LAST_EXCEPTION.message}
	<springForm:form action="signup" method='POST' modelAttribute = "signupform" >
	
	 <springForm:errors path = "*" cssClass = "errorblock" element = "div" />
	
	
	
		<table>
		<tr>
		<td>UserName:</td>
	 	
		<td><input type="text" name="username"> <br></td>
		<td><springForm:errors path = "username" cssClass = "error" /></td>
		</tr>
		<tr>
		<td>Password:</td>
		<td><input type="password" name="password1"> <br></td>
		<td><springForm:errors path = "password1" cssClass = "error" /></td>
		</tr>
		<tr>
		<td>Repeat Password:</td>
		<td><input type="password" name="password2"> <br></td>
		<td><springForm:errors path = "password2" cssClass = "error" /></td>
		</tr>
		<tr>
		<td><input name="submit" type="submit" value="submit"> <br></td>
		
		</tr>
		</table>
		</springForm:form>

</body>
</html>