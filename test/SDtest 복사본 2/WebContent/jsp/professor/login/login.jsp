<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	[Repotory] 교수님의 로그인 페이지입니다.<br/>
	<br/>
	MAIN > Professor > LogIn<br/>
	<br/>
	<br/>
	<form action='/LoginServlet' method=POST>
	이메일: <input type="text" name="loginEmail"><br/>
	패스워드: <input type="password" name="loginPassword"><br/>
	<input type="submit" name="loginSubmit" value="로그인"><br/>	
	</form>
</body>
</html>