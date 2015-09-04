<%@ page language="java" contentType="text/html; charset=UTF-8"
	isELIgnored="false" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="${ pageContext.request.contextPath }/css/div.css" type="text/css"
	media="screen" />
<link rel="stylesheet"
	href="${ pageContext.request.contextPath }/css/bootstrap-datetimepicker.min.css"
	type="text/css" media="screen" />
<link
	href="${ pageContext.request.contextPath }/css/bootstrap-combined.min.css"
	rel="stylesheet">

<script type="text/javascript"
	src="${ pageContext.request.contextPath }/js/jquery-1.8.3.min.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="${ pageContext.request.contextPath }/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="${ pageContext.request.contextPath }/js/bootstrap-datetimepicker.js"
	charset="UTF-8"></script>
<script type="text/javascript"
	src="${ pageContext.request.contextPath }/js/bootstrap-datetimepicker.zh-CN.js"
	charset="UTF-8"></script>
	<script type="text/javascript" src="${ pageContext.request.contextPath }/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">

		function pass(id)
		{
			var role = document.getElementById("select" + id).value;
			var form = document.passForm;
			form.userId.value = id;
			form.role.value = role;
			form.action.value = "pass";
			form.submit();
		}

		function unpass(id)
		{
			var form = document.passForm
			form.userId.value = id;
			form.action.value = "unpass";
			form.submit();
		}
		
</script>

<title>审核用户</title>
</head>
<body>
	<div class="container">
		<c:import url="title.jsp"></c:import>
		<div class="row">
			<c:import url="menu.jsp"></c:import>
			<div class="span8">
				<div id="search">
					<form method="get" action="user-audit.do">
						<h2>查找</h2>
						<table class="table">
							<tr>
								<th>用户名：</th>
								<th>邮箱：</th>
								<th>从：</th>
								<th>到：</th>
							</tr>
							<tr>
								<td><input type="text" name="name" value="${ param.name }" /></td>
								<td><input type="text" name="email"
									value="${ param.email }" /></td>
								<td><input class="date" type="text" name="start_time" onfocus="WdatePicker()"
									value="${ param.start_time }" /></td>
								<td><input class="date" type="text" name="end_time" onfocus="WdatePicker()"
									value="${ param.end_time }" /> </td>
								<td>
						</table>
						<br />
						<button class="btn btn-primary" type="submit" name="search">查找</button>
						<br />
					</form>
				</div>
				<div id="showresult">
					<h2>查找结果</h2>
					<p style="color: green;" align="center" >
						<c:if test="${ not empty pass_success }">已通过</c:if>
						<c:if test="${ not empty unpass_succuss }">已拒绝</c:if>
					</p>
					<p style="color: red;"  align="center" >
						<c:if test="${ not empty pass_failed }">通过失败,请选择角色</c:if>
						<c:if test="${ not empty unpass_failed }">拒绝失败</c:if>
					</p>
					<table class="table table-striped">
						<tr>
							<th>用户ID</th>
							<th>用户名</th>
							<th>用户邮箱</th>
							<th>注册时间</th>
							<th>身份</th>
							<th>审核操作</th>
							<th></th>
						</tr>
						<c:forEach var="user" items="${ page.curItems }">
							<tr>
								<td>${ user.userId }</td>
								<td>${ user.name }</td>
								<td>${ user.email }</td>
								<td>${ user.registerTime }</td>
								<td><select class="input-medium" id="select${ user.userId }">
										<option value="no">请选择角色</option>
										<option value="admin">Admin</option>
										<option value="team_leader">TeamLeader</option>
										<option value="QA">QA</option>
										<option value="developer">Developer</option>
								</select></td>
								<td>
									<button type="submit" name="pass" class="btn btn-primary"
										onclick="pass(${ user.userId })">通过</button>
								</td>
								<td>
									<button type="submit" name="not_pass" class="btn btn-primary"
										onclick="unpass(${ user.userId})">拒绝</button>
								</td>
							</tr>
						</c:forEach>

					</table>


					<c:url var="url" value="user-audit.do" scope="page">
						<c:param name="name" value="${ param.name }"></c:param>
						<c:param name="email" value="${ param.email }"></c:param>
						<c:param name="start_time" value="${ param.start_time }"></c:param>
						<c:param name="end_time" value="${ param.end_time }"></c:param>
					</c:url>

					<div align="center">
						<p>共有${ page.itemCount }条记录</p>
						
						<c:if test="${ page.curPage > 1 }">
							<a class="n" href="${ url }&page=1">首页</a>
							<a class="n" href="${ url }&page=${ page.curPage - 1 }">上页</a>
						</c:if>
						<c:if test="${ page.curPage == 1 }">
							首页&nbsp;上页
						</c:if>

						<c:forEach items="${ page.showPageIndices }" var="index">
							<c:if test="${ index == page.curPage }">
								${ index }
							</c:if>
							<c:if test="${ index != page.curPage }">
								<a href="${ url }&page=${ index }">${ index }</a>
							</c:if>
						</c:forEach>

						<c:if test="${ page.curPage < page.pageCount }">
							<a class="n" href="${ url }&page=${ page.curPage + 1 }">下页</a>
							<a class="n" href="${ url }&page=${ page.pageCount }">尾页</a>
						</c:if>
						<c:if test="${ page.curPage >= page.pageCount }">
							下页&nbsp;尾页
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>

	<form action="user-audit.do" name="passForm" action="post">
		<input type="hidden" name="userId" value="" /><input type="hidden"
			name="role" value="" /> <input type="hidden" name="action" value="" />
		<input type="hidden" name="name" value="${ param.name }"> <input
			type="hidden" name="email" value="${ param.email }" /> <input
			type="hidden" name="start_time" value="${ param.start_time }" /> <input
			type="hidden" name="end_time" value="${ param.end_time }" /> <input
			type="hidden" name="page" value="${ param.page }" />
	</form>
</body>

</html>