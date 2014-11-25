<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action='/RegisterServlet' method=POST>
	Email주소: <input type="text" name="registerEmail"><br>
	Password: <input type="password" name="registerPassword"><br>
	성함: <input type="text" name="registerName"><br>
	<input type="submit" name="registerSubmit" value="가입"><br>
</form>
</body>
</html>