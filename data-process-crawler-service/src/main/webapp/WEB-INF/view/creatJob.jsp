<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Creat Job</title>
</head>
<body>
	<h2 align="center">Job配置</h2>
	<form id="creatJob" name="creatJob" method="post" action="add">
		<table align="center">
			<tr>
				<td width="30%" align="right">JobName:</td>
				<td width="50%" align="left"><input type="text" name="jobName" /></td>
			</tr>
			<tr>
				<td width="30%" align="right">Url列表:</td>
				<td width="50%" align="left"><input type="text" name="urlList" /></td>
			</tr>
			<tr>
				<td width="30%" align="right">最大抓取深度:</td>
				<td width="50%" align="left"><input type="text" name="maxDeep" /></td>
			</tr>
			<tr>
				<td width="30%" align="right">最大抓取量:</td>
				<td width="50%" align="left"><input type="text" name="maxSize" /></td>
			</tr>
			<tr>
			<td width="30%" align="right">JobConfig:</td>
			<td width="50%" align="left">
				<table align="left" border="2">
					
					<tr>
						<td width="10%" align="right">baseUrl:</td>
						<td width="80%" align="left"><input type="text"
							name="baseUrl" /></td>
					</tr>
					<tr>
						<td width="10%" align="right">crawler类型:</td>
						<td width="80%" align="left"><input type="text"
							name="crawlerType" /></td>
					</tr>
					<tr>
						<td width="10%" align="right">extendDeep:</td>
						<td width="80%" align="left"><input type="text"
							name="extendDeep" /></td>
					</tr>
					<tr>
						<td width="10%" align="right">threadNum:</td>
						<td width="80%" align="left"><input type="text"
							name="threadNum" /></td>
					</tr>
					<tr>
						<td width="10%" align="right">mongoCollectionName:</td>
						<td width="80%" align="left"><input type="text"
							name="mongoCollectionName" /></td>
					</tr>
					<tr>
						<td width="10%" align="right">sleepMills:</td>
						<td width="80%" align="left"><input type="text"
							name="sleepMills" /></td>
					</tr>
					<tr>
						<td width="10%" align="right">mqName:</td>
						<td width="80%" align="left"><input type="text" name="mqName" /></td>
					</tr>
					<tr>
						<td width="10%" align="right">torrentUrl:</td>
						<td width="80%" align="left"><input type="text"
							name="torrentUrl" /></td>
					</tr>
					<tr>
						<td width="10%" align="right">maxDomain:</td>
						<td width="80%" align="left"><input type="text"
							name="maxDomain" /></td>
					</tr>
				</table>
				</td>
			</tr>

			<tr>
			<td width="30%" align="right">可变字段配置:</td>
			<td width="50%" align="left">
				<table align="left" border="2">
					
					<tr>
						<td width="10%" align="right">startPoint:</td>
						<td width="20%" align="left"><input type="text"
							name="startPoint" /></td>
					</tr>
					<tr>
						<td width="10%" align="right">endPoint:</td>
						<td width="20%" align="left"><input type="text"
							name="endPoint" /></td>
					</tr>
					<tr>
						<td width="10%" align="right">fieldName:</td>
						<td width="20%" align="left"><input type="text"
							name="fieldName" /></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr align="center">
				<td width="30%"></td>
				<td width="50%"><input type="submit" value="提交" />&nbsp;&nbsp;&nbsp;<input
					type="reset" value="重置" /></td>
			</tr>

		</table>
	</form>
</body>
</html>
