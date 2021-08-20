<%@page import="com.gethangry.sergey.testwebapp.WebAppUtil"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>(TEST) Application Insights Logging</title>
</head>
<body>
Release timestamp: <%= WebAppUtil.getReleaseTimestamp() %>
<p>
Code load date: <%= WebAppUtil.LOAD_DATE %>
<p>
Testing Application Insights 1... 2... 3...
<%
WebAppUtil.doStuff();
%>
</body>
</html>