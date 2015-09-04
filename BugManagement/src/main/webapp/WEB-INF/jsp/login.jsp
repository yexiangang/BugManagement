<%@ page language="java" contentType="text/html; charset=UTF-8"
	isELIgnored="false" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
<link rel="stylesheet" href="${ pageContext.request.contextPath }/css/div.css" type="text/css" media="screen" />
<link href="${ pageContext.request.contextPath }/css/bootstrap-combined.min.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="span12">
				<div class="page-header">
					<h1 align="center">
						Bugzilla <small>缺陷追踪系统</small>
					</h1>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="span4"></div>
			<div class="span4">
				<form class="form-horizontal" action="login.do" method="post">
					<div class="control-group">
						<label class="control-label" for="inputUsername">用户名</label>
						<div class="controls">
							<input id="inputUsername" name="username" type="text" placeholder="Username" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="inputPassword">密码</label>
						<div class="controls">
							<input id="inputPassword" name="password" type="password" placeholder="Password" />
						</div>
					</div>
					<div class="control-group">
						<div class="controls">
							<button class="btn btn-primary" type="submit">登陆</button>
							&nbsp;&nbsp; <a class="btn btn-primary" href="register.do">注册</a>
						</div>
					</div>
					<div class="control-group">
						<div class="controls">
							<p style="color: red;">
								<c:if test="${ not empty user_not_exist }">用户名不存在</c:if>
								<c:if test="${ not empty user_under_auditing }">该用户正在审核中</c:if>
								<c:if test="${ not empty wrong_password }">密码错误</c:if>
							</p>
						</div>
					</div>
				</form>
			</div>
			<div class="span4"></div>
		</div>
	</div>
</body>
</html>