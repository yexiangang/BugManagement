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
			var regex = new RegExp("sdmenu_" + encodeURIComponent(this.menu.id)
					+ "=([01]+)");
			var match = regex.exec(document.cookie);
			if (match) {
				var states = match[1].split("");
				for (var i = 0; i < states.length; i++)
					this.submenus[i].className = (states[i] == 0 ? "collapsed"
							: "");
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
		var moveBy = Math.round(this.speed
				* submenu.getElementsByTagName("a").length);
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
				if (this.submenus[i] != submenu
						&& this.submenus[i].className != "collapsed")
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
			document.cookie = "sdmenu_" + encodeURIComponent(this.menu.id)
					+ "=" + states.join("") + "; expires=" + d.toGMTString()
					+ "; path=/";
		}
	};
</script>
<script type="text/javascript">
	var myMenu;
	window.onload = function() {
		myMenu = new SDMenu("my_menu");
		myMenu.init();
	};

	function check(obj) {
		var tip = document.getElementById("tip");
		if (obj.name.value.length < 6 || obj.name.value.length > 15) {
			tip.innerHTML = "用户名为6-15个字符";
		}
		else if (obj.password.value.length < 6 || obj.name.value.length > 15) {
			tip.innerHTML = "密码为6-15个字符";
		}
		else if (checkEmail(obj.email.value)) {
			tip.innerHTML = "邮箱无效";
		}
		else {
			return true;
		}
		return false;
	}	

	function checkEmail(email) {
		var sReg   =   /[_a-zA-Z\d\-\.]+@[_a-zA-Z\d\-]+(\.[_a-zA-Z\d\-]+)+$/;   
		return !sReg.test(email);   
	}
</script>
<title>添加用户</title>
</head>

<body>
	<div class="container">
		<c:import url="title.jsp"></c:import>
		<div class="row">
			<div class="span2">
				<div id="my_menu" class="sdmenu">
					<c:import url="menu.jsp"></c:import>
				</div>
			</div>
			<div class="span8">
				<div id="link">
					<h2>添加用户</h2>

				</div>
				<div id="add_user">
					<form method="post" action="user-manage.do"
						onsubmit="return check(this)">

						<p>新用户名</p>
						<input name="name" type="text" value="${ param.name }" />
						<p>用户密码</p>
						<input name="password" type="password" />
						<p>用户邮箱</p>
						<input name="email" type="text" value="${ param.email }" />
						<p>身份权限</p>
						<select name="role">
							<option value="team_leader">TeamLeader</option>
							<option value="QA">QA</option>
							<option value="developer">Developer</option>
						</select> <br />
						<button class="btn btn-primary" type="submit" name="action"
							value="add">确定</button>

					</form>

					<c:choose>
						<c:when test="${ not empty register_error }">
							<p style="color: red;">注册信息有误</p>
						</c:when>
						<c:when test="${ not empty name_registered }">
							<p style="color: red;">用户名已注册</p>
						</c:when>
						<c:when test="${ not empty email_registered }">
							<p style="color: red;">邮箱已注册</p>
						</c:when>
						<c:when test="${ not empty role_error }">
							<p style="color: red;">角色设置不能为admin</p>
						</c:when>
						<c:when test="${ not empty success }">
							<p style="color: green;">添加成功！</p>
						</c:when>
					</c:choose>

					<p style="color: red;" id="tip"></p>

				</div>
			</div>
		</div>

	</div>
</body>

</html>