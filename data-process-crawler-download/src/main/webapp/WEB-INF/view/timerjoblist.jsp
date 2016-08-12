<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<html>
<body>
	<div>
		<p>所有的任务列表</p>
		<table width="100%" border="2">
			<tr>
				<td>编号</td>
				<td align="center">Id</td>
				<td align="center">JobId</td>
				<td align="center">延时</td>
				<td align="center">抓取的Url</td>
				<td align="center">扩展Url的Xpath</td>
				<td align="center">通知MqName</td>
				<td align="center">是否扩展</td>
				<td align="center">创建时间</td>
				<td align="center">修改时间</td>
				<td align="center">任务状态</td>
				<td align="center">任务描述</td>
			</tr>
			<c:forEach items="${lists}" var="item" varStatus="vs">
				<tr>
					<td><s:property value="${vs.index+1}" /></td>
					<td align="center">${item.id}</td>
					<td align="center">${item.jobid}</td>
					<td align="center">${item.delay}</td>
					<td align="center">${item.url}</td>
					<td align="center">${item.xpath}</td>
					<td align="center">${item.noticequeuename}</td>
					<td align="center">${item.expendstype}</td>
					<td align="center">${item.ctime}</td>
					<td align="center">${item.mtime}</td>
					<td align="center">${item.jobstatus}</td>
					<td align="center">${item.jobdesc}</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>
