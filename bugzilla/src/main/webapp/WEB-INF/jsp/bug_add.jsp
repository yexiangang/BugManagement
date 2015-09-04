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
<script type="text/javascript">
	function projectChange(obj) {
		var form = document.getElementById("create_bug");
		form.method = "get";
		form.submit();
	}

	function create() {
		var form = document.getElementById("create_bug");
		form.method = "post";
		return true;
	}
</script>
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
</script>
<title>添加用户</title>
</head>

<body>

	<div class="container">
		<c:import url="title.jsp"></c:import>

		<div class="row">
			<c:import url="menu.jsp"></c:import>
			<div class="span8">
				<script type="text/javascript">
					function create(obj) {
						var tip = document.getElementById("tip");
						if (obj.name.value == "") {
							tip.innerHTML = "名称不能为空";
						} else if (obj.projectId.value == "") {
							tip.innerHTML = "项目ID不能为空";
						} else if (obj.description.value == "") {
							tip.innerHTML = "项目描述不能为空";
						} else {
							return true;
						}
						return false;
					}
				</script>
				<div id="link">
					<h2>创建bug</h2>

				</div>

				<p id="tip" style="color: red;">
					<c:if test="${ not empty failed }">
						添加失败,请填写完整信息
					</c:if>
				</p>
				<p style="color: green;">
					<c:if test="${ not empty success }">
						添加成功
					</c:if>
				</p>

				<form id="create_bug" onsubmit="return create(this)"
					enctype="multipart/form-data" action="bug-add.do" method="post">
					<div class="row">
						<div class="span3">
							<label>bug名称<small>(不超过20字)</small></label> <input name="name"
								type="text" value="${ param.name }" />
						</div>
						<div class="span3">
							<p>严重性</p>
							<select name="severity"><option value="1">一般</option>
								<option value="2">中等</option>
								<option value="3">严重</option>
								<option value="4">致命</option></select>
						</div>
					</div>
					<div class="row">
						<div class="span3">
							<p>所在项目</p>
							<select name="projectId" onchange="projectChange(this)">
								<option value="">请选择项目</option>
								<c:forEach items="${ projects }" var="project">
									<option value="${ project.projectId }"
										<c:if test="${ project.projectId == param.projectId }">selected="selected"</c:if>>${ project.name }</option>

								</c:forEach>
							</select>
						</div>
						<div class="span3">
							<p>分配给</p>
							<select name="assignedUserId"><option>（可选择）</option>
								<c:if test="${ not empty members }">
									<c:forEach var="member" items="${ members }">
										<option value="${ member.user.userId }">${ member.user.name }</option>
									</c:forEach>
								</c:if>
							</select>
						</div>
					</div>
					<p>bug描述</p>
					<textarea class="comment" placeholder="在此输入描述..."
						name="description" cols="30" rows="5">${ param.description }</textarea>
					<br />
					<p>图片上传(bmp,png,jpg格式,最多五张)</p>
					<div id="images">
						<div>
							<input type="file" name="file1"
								accept="image/bmp,image/png,image/jpeg" />
						</div>
						<div>
							<input type="file" name="file2"
								accept="image/bmp,image/png,image/jpeg" />
						</div>
						<div>
							<input type="file" name="file3"
								accept="image/bmp,image/png,image/jpeg" />
						</div>
						<div>
							<input type="file" name="file4"
								accept="image/bmp,image/png,image/jpeg" />
						</div>
						<div>
							<input type="file" name="file5"
								accept="image/bmp,image/png,image/jpeg" />
						</div>
					</div>
					<br />
					<p>附件上传(doc,ppt,txt,zip,pdf格式)</p>
					<div id="attach">
						<input type="file" name="file6"
							accept="application/msword,application/vnd.ms-powerpoint,text/plain,application/zip,application/pdf" />
					</div>
					<br />
					<button class="btn btn-primary" type="submit">创建</button>
				</form>

			</div>
		</div>
	</div>
</body>

</html>