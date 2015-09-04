<%@ page language="java" contentType="text/html; charset=UTF-8"
	isELIgnored="false" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册</title>
<link rel="stylesheet"
	href="${ pageContext.request.contextPath }/css/div.css" type="text/css"
	media="screen" />
<link
	href="${ pageContext.request.contextPath }/css/bootstrap-combined.min.css"
	rel="stylesheet">
<script type="text/javascript"
	src="${ pageContext.request.contextPath }/js/my.js"></script>
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
				<form action="register.do" method="post">
					<fieldset>
						<legend>新用户注册</legend>
						<label>用户名</label> <input type="text" name="name" id="name"
							placeholder="Username" onKeyUp="validate()"
							value="${ param.name }" /> <label>密码</label> <input
							type="password" name="password" id="password"
							placeholder="Password" onblur="validate()" /> <label>确认密码</label>
						<input type="password" id="password2" name="password2"
							onblur="validate()" placeholder="confirm password" /> <label>邮箱</label>
						<input type="text" name="email" id="email" placeholder="Email"
							onKeyUp="validate()" /> <br />
						<button type="submit" id="submit" class="btn btn-primary"
							disabled="disabled">确定</button>
							<a href="login.do" style="margin-left: 40px">登录</a>
					</fieldset>
				</form>
				<p id="tip" style="color: red;">
					<c:if test="${ not empty name_registered }">
						用户名已注册
					</c:if>
					<c:if test="${ not empty email_registered }">
						邮箱已注册
					</c:if>
					<c:if test="${ not empty register_error }">
						注册失败
					</c:if>
					<c:if test="${ not empty success }">
						注册成功, 3秒后跳转到登录页面
					</c:if>
				</p>
			</div>
			<div class="span4"></div>
		</div>
	</div>
</body>
</html>