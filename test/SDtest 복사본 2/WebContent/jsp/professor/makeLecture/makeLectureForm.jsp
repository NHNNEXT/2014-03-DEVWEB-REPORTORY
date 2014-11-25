<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	[Repotory] 교수님께서 강의를 등록하는 페이지입니다.<br>
	<br/>
	MAIN > Professor > MakeLecture<br>
	<br/>
	<br/>
	<form action='/MakeLectureServlet' method=POST>
	강의명: <input type="text" name="lectureSubject"><br/><br/><br/>
	설명  : <textarea rows=10 cols=100 name="lectureDiscription"></textarea><br>
	<input type="submit" name="registerSubmit" value="강의생성"><br>
</form>
</body>
</html>