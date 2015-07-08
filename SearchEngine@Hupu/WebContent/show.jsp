<%@page import="ustc.edu.cn.version.textrank.Page"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Result Page</title>
<link rel="stylesheet" type="text/css" href="css/css2.css">
</head>
<body aign="left" background="image/1.jpg">
	
	<form action="searchEngineView" method="get">
	<div>
		<img src="${pageContext.request.contextPath }/image/hp_logo_nba.png" alt="hupu" align="top" border="0">
	</div>
	<br/>
	<br/>
	<div style="padding:0,0,0,0">
		<input type="text" name="user_input" style="width:300px;height:30px">
		<input type="submit" value="搜索" style="width:200px;height:40px;font-size: 25px">
	</div>
	</form>
	
	<hr>
	
	<% 
		List<Page> pages = (List<Page>)request.getAttribute("list");
		Page[] pageArray = (Page[])pages.toArray();
 	%>
	<div>
		<%
		for(int i=1;i<=pageArray.length;i++)
		{		
			out.println("<h4><a href=" + pageArray[i-1].url + ">" + pageArray[i-1].title +  "</a>" + "<br/></h4>");
			if(pageArray[i-1].content.length() > 100){
				out.println("<p>" + pageArray[i-1].content.substring(0,100 ) + "</p><br/>");
			}else{
				out.println("<p>" + pageArray[i-1].content + "</p><br/>");
			}
		}
		%>
	</div>
</body>
</html>
