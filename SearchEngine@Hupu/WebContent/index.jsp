<%@page import="ustc.edu.cn.version.textrank.Page"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>HOME PAGE</title>
<link rel="stylesheet" type="text/css" href="css/css.css">
</head>
<body align="center" background="#ffffff">

	<form action="searchEngineView" method="get">
		<div>
			<img src="${pageContext.request.contextPath }/image/hp_logo_nba.png"
				alt="hupu" align="center" border="0">
		</div>
		<br /> <input type="text" name="user_input"
			style="width: 500px; height: 50px"> <input type="submit"
			value="搜索" style="width: 200px; height: 50px; font-size: 25px">
	</form>
</body>
</html>
