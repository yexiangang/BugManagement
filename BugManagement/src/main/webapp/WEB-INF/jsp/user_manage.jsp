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

<style>
a {
	text-decoration: none
}

div.sdmenu {
	width: 150px;
	font-family: Arial, sans-serif;
	font-size: 12px;
	padding-bottom: 10px;
	background: url(images/bottom.gif) no-repeat right bottom;
	color: #fff;
}

div.sdmenu div {
	background: url(images/title2.gif) repeat-x;
	overflow: hidden;
}

div.sdmenu div:first-child {
	background: url(images/toptitle1.gif) no-repeat;
}

div.sdmenu div.collapsed {
	height: 25px;
}

div.sdmenu div spanlist {
	display: block;
	height: 15px;
	line-height: 15px;
	overflow: hidden;
	padding: 5px 25px;
	font-weight: bold;
	color: #ffffff;
	background: url(images/expanded.gif) no-repeat 10px center;
	cursor: pointer;
	border-bottom: 1px solid #ddd;
}

div.sdmenu div.collapsed spanlist {
	background-image: url(images/collapsed.gif);
}

div.sdmenu div a {
	padding: 5px 10px;
	background: #46A3FF;
	display: block;
	border-bottom: 1px solid #ddd;
	color: #fff;
}

div.sdmenu div a.current {
	background: #46A3FF url(images/linkarrow.gif) no-repeat right center;
}

div.sdmenu div a:hover {
	background: #46A3FF url(images/linkarrow.gif) no-repeat right center;
	color: #fff;
	text-decoration: none;
}

#popDiv td {
	height: 50px;
	text-align: center;
}

#popDiv th {
	text-align: center;
	padding-right: 20px;
}

#popDiv td * {
	text-align: center;
}
</style>
<script type="text/javascript">
function SDMenu(id) {
	if (!document.getElementById || !document.getElementsByTagName)
		return false;
	this.menu = document.getElementById(id);
	this.submenus = this.menu.getElementsByTagName("div");
	this.remember = true;
	this.speed = 3;
	this.markCurrent = true;
	this.oneSmOnly = false;
}
SDMenu.prototype.init = function() {
	var mainInstance = this;
	for (var i = 0; i < this.submenus.length; i++)
		this.submenus[i].getElementsByTagName("spanlist")[0].onclick = function() {
			mainInstance.toggleMenu(this.parentNode);
		};
	if (this.markCurrent) {
		var links = this.menu.getElementsByTagName("a");
		for (var i = 0; i < links.length; i++)
			if (links[i].href == document.location.href) {
				links[i].className = "current";
				break;
			}
	}
	if (this.remember) {
		var regex = new RegExp("sdmenu_" + encodeURIComponent(this.menu.id) + "=([01]+)");
		var match = regex.exec(document.cookie);
		if (match) {
			var states = match[1].split("");
			for (var i = 0; i < states.length; i++)
				this.submenus[i].className = (states[i] == 0 ? "collapsed" : "");
		}
	}
};
SDMenu.prototype.toggleMenu = function(submenu) {
	if (submenu.className == "collapsed")
		this.expandMenu(submenu);
	else
		this.collapseMenu(submenu);
};
SDMenu.prototype.expandMenu = function(submenu) {
	var fullHeight = submenu.getElementsByTagName("spanlist")[0].offsetHeight;
	var links = submenu.getElementsByTagName("a");
	for (var i = 0; i < links.length; i++)
		fullHeight += links[i].offsetHeight;
	var moveBy = Math.round(this.speed * links.length);
	
	var mainInstance = this;
	var intId = setInterval(function() {
		var curHeight = submenu.offsetHeight;
		var newHeight = curHeight + moveBy;
		if (newHeight < fullHeight)
			submenu.style.height = newHeight + "px";
		else {
			clearInterval(intId);
			submenu.style.height = "";
			submenu.className = "";
			mainInstance.memorize();
		}
	}, 30);
	this.collapseOthers(submenu);
};
SDMenu.prototype.collapseMenu = function(submenu) {
	var minHeight = submenu.getElementsByTagName("spanlist")[0].offsetHeight;
	var moveBy = Math.round(this.speed * submenu.getElementsByTagName("a").length);
	var mainInstance = this;
	var intId = setInterval(function() {
		var curHeight = submenu.offsetHeight;
		var newHeight = curHeight - moveBy;
		if (newHeight > minHeight)
			submenu.style.height = newHeight + "px";
		else {
			clearInterval(intId);
			submenu.style.height = "";
			submenu.className = "collapsed";
			mainInstance.memorize();
		}
	}, 30);
};
SDMenu.prototype.collapseOthers = function(submenu) {
	if (this.oneSmOnly) {
		for (var i = 0; i < this.submenus.length; i++)
			if (this.submenus[i] != submenu && this.submenus[i].className != "collapsed")
				this.collapseMenu(this.submenus[i]);
	}
};
SDMenu.prototype.expandAll = function() {
	var oldOneSmOnly = this.oneSmOnly;
	this.oneSmOnly = false;
	for (var i = 0; i < this.submenus.length; i++)
		if (this.submenus[i].className == "collapsed")
			this.expandMenu(this.submenus[i]);
	this.oneSmOnly = oldOneSmOnly;
};
SDMenu.prototype.collapseAll = function() {
	for (var i = 0; i < this.submenus.length; i++)
		if (this.submenus[i].className != "collapsed")
			this.collapseMenu(this.submenus[i]);
};
SDMenu.prototype.memorize = function() {
	if (this.remember) {
		var states = new Array();
		for (var i = 0; i < this.submenus.length; i++)
			states.push(this.submenus[i].className == "collapsed" ? 0 : 1);
		var d = new Date();
		d.setTime(d.getTime() + (30 * 24 * 60 * 60 * 1000));
		document.cookie = "sdmenu_" + encodeURIComponent(this.menu.id) + "=" + states.join("") + "; expires=" + d.toGMTString() + "; path=/";
	}
};
</script>
<script type="text/javascript">
var myMenu;
window.onload = function() {
	myMenu = new SDMenu("my_menu");
	myMenu.init();
};

</script>
<title>管理用户</title>
</head>
<body>
	<div class="container">
		<c:import url="title.jsp"></c:import>
		<div class="row">
			<c:import url="menu.jsp"></c:import>
			<div class="span8">
				<div id="link">
					<h2>管理用户</h2>
				</div>

				<script type="text/javascript">
					function pass(id) {
						var role = document.getElementById("select" + id).value;
						var form = document.passForm;
						form.userId.value = id;
						form.role.value = role;
						form.action.value = "modify";
						form.submit();
					}

					function unpass(id) {
						var form = document.passForm;
						form.userId.value = id;
						form.action.value = "delete";
						form.submit(); 
					}

					function showDiv(id) {

						$.ajax({
							   type: "GET",
							   url: "user-manage.do",
							   data: "action=detail&userId=" + id,
							   dataType: "json",
							   success: function(data){
								   var map = eval(data);
								   var form = document.getElementById("hidden_form");
								   form.userId.value = map.userId;
								   document.getElementById("hidden_id").innerHTML = map.userId;
								   document.getElementById("hidden_name").innerHTML = map.name;
								   document.getElementById("hidden_time").innerHTML = map.registerTime;
								   form.email.value = map.email;
								   form.role.value = map.role;
								   document.getElementById('popDiv').style.display = 'block';
								   document.getElementById('bg').style.display = 'block'; 
							   },
						   	   error: function(data){
						   			var tip = document.getElementById("hidden_tip");
						   			tip.innerHTML = "用户信息加载失败";
							   }
							});
						
					}

					function submit() {
						var form = document.getElementById("hidden_form");
						var tip = document.getElementById("hidden_tip");
						var password = form.password.value;
						if (password.length > 0 && (password.length < 6 || password.length > 15)) {
							tip.innerHTML = "密码长度为6-15个字符，不填写则不修改";
						} else if (checkEmail(form.email.value)) {
							tip.innerHTML = "邮箱格式不正确";
						} else {
							form.submit();
						}
					}

					function checkEmail(email) {
						var sReg   =   /[_a-zA-Z\d\-\.]+@[_a-zA-Z\d\-]+(\.[_a-zA-Z\d\-]+)+$/;   
						return !sReg.test(email);   
					}
					
					function closeDiv() {
						document.getElementById('popDiv').style.display = 'none';
						document.getElementById('bg').style.display = 'none';
					}
				</script>

				<div id="change_user">

					<form name="form2" method="post" action="user-manage.do">
						<h2>查找</h2>
						<p>用户名:</p>
						<input type="text" name="name" value="${ param.name }" />
						<button class="btn btn-primary" type="submit" name="search">查找</button>
					</form>


					<p align="center" style="color: green;">
						<c:if test="${ not empty modify_success }">修改成功（用户ID：${ param.userId }）</c:if>
						<c:if test="${ not empty delete_success }">删除成功（用户ID：${ param.userId }）</c:if>
					</p>

					<p align="center" style="color: red;">
						<c:if test="${ not empty modify_failed }">修改失败（用户ID：${ param.userId }），用户有相关联的信息</c:if>
						<c:if test="${ not empty delete_failed }">删除失败（用户ID：${ param.userId }），用户有相关联的信息</c:if>
					</p>

					<div id="popDiv" class="mydiv" style="display: none;">
						<div class="dialog-title">修改用户信息</div>
						<div class="dialog-content" align="center">
							<p id="hidden_tip" style="color: red;"></p>
							<form action="user-manage.do" id="hidden_form">
								<input type="hidden" value="" name="userId" /> <input
									type="hidden" value="modify" name="action" /> <input
									type="hidden" name="name" value="${ param.name }" /> <input
									type="hidden" name="page" value="${ param.page }" />
								<table>
									<tr>
										<th>用户&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ID</th>
										<td id="hidden_id"></td>
									</tr>
									<tr>
										<th>用户名称</th>
										<td id="hidden_name"></td>
									</tr>
									<tr>
										<th>注册时间</th>
										<td id="hidden_time"></td>
									</tr>
									<tr>
										<th>密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码</th>
										<td><input type="password" name="password" /></td>
									</tr>
									<tr>
										<th>邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱</th>
										<td><input type="text" name="email" /></td>
									</tr>
									<tr>
										<th>角&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;色</th>
										<td><select name="role">
												<option value="team_leader">TeamLeader</option>
												<option value="QA">QA</option>
												<option value="developer">Developer</option>
										</select></td>
									</tr>
									<tr>
										<td colspan="2"><a class="btn btn-primary"
											onclick="submit()">确定</a> <a class="btn btn-primary"
											onclick="closeDiv()">取消</a></td>
									</tr>
								</table>
							</form>

						</div>
					</div>

					<table class="table table-striped">
						<tr>
							<th>用户ID</th>
							<th>用户名</th>
							<th>用户邮箱</th>
							<th>注册时间</th>
							<th>身份</th>
							<th colspan="3">修改操作</th>
						</tr>

						<c:forEach var="user" items="${ page.curItems }">
							<tr>
								<td>${ user.userId }</td>
								<td>${ user.name }</td>
								<td>${ user.email }</td>
								<td>${ user.registerTime }</td>
								<td><c:choose>
										<c:when test="${ user.role == 'admin' }">Admin</c:when>
										<c:when test="${ user.role == 'developer' }">Developer</c:when>
										<c:when test="${ user.role == 'team_leader' }">TeamLeader</c:when>
										<c:when test="${ user.role == 'QA' }">QA</c:when>
									</c:choose></td>
								<td>
									<button class="btn btn-primary" type="submit" name="pass"
										onclick="showDiv(${ user.userId})"
										<c:if test="${ user.role == 'admin' }">disabled="disabled"</c:if>>修改
									</button>
								</td>
								<td>
									<button class="btn btn-primary" type="submit" name="not_pass"
										onclick="unpass(${ user.userId})"
										<c:if test="${ user.role == 'admin' }">disabled="disabled"</c:if>>删除</button>
								</td>

							</tr>
						</c:forEach>
					</table>
					<div align="center">
						<c:url var="url" value="user-manage.do" scope="page">
							<c:param name="name" value="${ param.name }"></c:param>
						</c:url>

						<p>共有${ page.itemCount }条记录</p>
						<c:if test="${ page.curPage > 1 }">
							<a href="${ url }&page=1">首页</a>
							<a class="n" href="${ url }&page=${ page.curPage - 1 }">上页</a>
						</c:if>
						<c:if test="${ page.curPage == 1 }">首页&nbsp;上页</c:if>

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


	<form action="user-manage.do" name="passForm" method="get">
		<input type="hidden" name="userId" value="" /><input type="hidden"
			name="role" value="" /> <input type="hidden" name="action" value="" />
		<input type="hidden" name="name" value="${ param.name }" /> <input
			type="hidden" name="page" value="${ param.page }" />
	</form>
</body>

</html>