<%@ page language="java" contentType="text/html; charset=UTF-8"
	isELIgnored="false" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<link rel="stylesheet" href="div.css" type="text/css" media="screen"/>
<link href="${ pageContext.request.contextPath }/css/bootstrap-combined.min.css" rel="stylesheet">
<script type="text/javascript" src="${ pageContext.request.contextPath }/js/bugzilla.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>创建项目</title>
<style>
a { text-decoration: none} 
div.sdmenu{
	width:150px;
	font-family:Arial, sans-serif;
	font-size:12px;
	padding-bottom:10px;
	background:url(${ pageContext.request.contextPath }/images/bottom.gif) no-repeat  right bottom;color:#fff;
}
div.sdmenu div{
	background:url(${ pageContext.request.contextPath }/images/title2.gif) repeat-x;
	overflow:hidden;
}
div.sdmenu div:first-child{
	background:url(${ pageContext.request.contextPath }/images/toptitle1.gif) no-repeat;
}
div.sdmenu div.collapsed{
	height:25px;
}
div.sdmenu div spanlist{
	display:block;
	height:15px;
	line-height:15px;
	overflow:hidden;
	padding:5px 25px;
	font-weight:bold;
	color:#ffffff;
	background:url(${ pageContext.request.contextPath }/images/expanded.gif) no-repeat 10px center;
	cursor:pointer;
	border-bottom:1px solid #ddd;

}
div.sdmenu div.collapsed spanlist{
	background-image:url(images/collapsed.gif);
}
div.sdmenu div a{
	padding:5px 10px;
	background:#46A3FF;
	display:block;
	border-bottom:1px solid #ddd;
	color:#fff;
}
div.sdmenu div a.current{
	background:#46A3FF url(${ pageContext.request.contextPath }/images/linkarrow.gif) no-repeat right center;
}
div.sdmenu div a:hover{
	background:#46A3FF url(${ pageContext.request.contextPath }/images/linkarrow.gif) no-repeat right center;
	color:#fff;
	text-decoration:none;
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

</head>
<body>
<div class="container">
<c:import url="title.jsp"></c:import>
<div class="row">
<c:import url="menu.jsp"></c:import>
<div class="span8">
		<form name="createProject" method="post" action="project-create.do" onsubmit="return checkCreateProject()">
			<h3>创建项目</h3>
			<c:choose>
				<c:when test="${ not empty name_exists }">
					<p style="color: red;">创建失败，项目名已存在！</p>
				</c:when>
				<c:when test="${ not empty is_logout }">
					<p style="color: red;">您已下线，请重新登录</p>
				</c:when>
				<c:when test="${ not empty no_authority }">
					<p style="color: red;">您无权访问</p>
				</c:when>
				<c:when test="${ not empty create_error }">
					<p style="color: red;">信息有误</p>
				</c:when>
				<c:when test="${ not empty name_error }">
					<p style="color: red;">项目名不能为空</p>
				</c:when>
				<c:when test="${ not empty describe_error }">
					<p style="color: red;">描述不能为空</p>
				</c:when>
				<c:when test="${ not empty create_suc }">
					<p style="color: green;">创建成功</p>
				</c:when>
			</c:choose>
				<p>项目名称:</p>
	   	            <input type="text" name="project_name"/>                
	   	        <p>Team Leader:</p>              
	   	            <select id="TL" name="TeamLeader">
						<c:forEach var="leader" items="${ leaders }">
							<option value="${ leader.name }">${ leader.name }</option>
						</c:forEach>
					</select>
				<p>项目描述:</p>              
	   	            <textarea class= " comment" name="describe" cols="30" rows="5"></textarea>
				<br/>
	   	    <button class="btn btn-primary" type="submit" >创建</button>
	   	    
	   	</form>
	</div>
	</div>
	</div>
</body>
</html>