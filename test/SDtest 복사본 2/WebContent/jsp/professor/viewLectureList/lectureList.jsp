<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="entity.Lecture" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	[Repotory] 교수님의 강의 목록입니다.<br/>
	<br/>
	<a href="/jsp">MAIN</a> > Professor > Lecture<br/>
	<br/>
	<br/>
	<%ArrayList<Lecture> lectureList=(ArrayList<Lecture>)session.getAttribute("lectureList");
	%>
	<%=lectureList.toString() %>

</body>
</html>