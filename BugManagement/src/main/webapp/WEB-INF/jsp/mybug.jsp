<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>My bug</title>
<link rel="stylesheet"
	href="${ pageContext.request.contextPath }/css/div.css" type="text/css"
	media="screen" />
<link
	href="${ pageContext.request.contextPath }/css/bootstrap-combined.min.css"
	rel="stylesheet">
<style>
a { text-decoration: none} 
div.sdmenu{
	width:150px;
	font-family:Arial, sans-serif;
	font-size:12px;
	padding-bottom:10px;
	background:url(images/bottom.gif) no-repeat  right bottom;color:#fff;
}
div.sdmenu div{
	background:url(images/title2.gif) repeat-x;
	overflow:hidden;
}
div.sdmenu div:first-child{
	background:url(images/toptitle1.gif) no-repeat;
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
	background:url(images/expanded.gif) no-repeat 10px center;
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
	background:#46A3FF url(images/linkarrow.gif) no-repeat right center;
}
div.sdmenu div a:hover{
	background:#46A3FF url(images/linkarrow.gif) no-repeat right center;
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

<div id="list" class="container">
	<c:import url="title.jsp"></c:import>
	<div class="row">
		<div class="span2">
			<div id="my_menu" class="sdmenu">
			<c:import url="menu.jsp"></c:import>
			</div>
			</div>
		<div class="span8" id="mybug" >
			<table class="table table-striped">
				<thead>
						<tr>
							<th width="100px">
								我的bug<p style="color: red;">${tip}</p>
							</th>
						</tr>
				</thead>
				<tbody>
					<tr class="success">
						<th>bug编号</th>
						<th>bug名称</th>
						<th width="100px">项目名称</th>
						<th>创建者</th>
						<th>分配者</th>
						<th width="100px">被分配者</th>
						<th width="100px">项目阶段</th>
						<th width="100px">bug状态</th>
						<th width="100px">严重性</th>
						<th>创建时间</th>
						<th>相关操作</th>
					</tr>
					<c:forEach var="bug" items="${bugs}">
						<tr>
							<td>${ bug[0] }</td>
	
							<td>
								<a href="<c:url value="edit.do?bugId=${bug[0]}&projectName=${bug[2]}&userName=${bug[3]}"></c:url>">${ bug[1] }</a>
							</td>
							<td>${ bug[2] }</td>
							<td>${ bug[3] }</td>
							<td>${ bug[4] }</td>
							<td>${ bug[5] }</td>
							<td>${ bug[6] }</td>
							 <td>
								<c:if test="${bug[7]=='undistributed'}">
								未分配
								</c:if>
								<c:if test="${bug[7]=='distributed'}">
								已分配
								</c:if>
								<c:if test="${bug[7]=='developing'}">
								开发中
								</c:if>
								<c:if test="${bug[7]=='developed'}">
								开发完成
								</c:if>
								<c:if test="${bug[7]=='testing'}">
								测试中
								</c:if>
								<c:if test="${bug[7]=='test_passed'}">
								测试通过
								</c:if>
								<c:if test="${bug[7]=='test_unpassed'}">
								测试未通过
								</c:if>
								<c:if test="${bug[7]=='closed'}">
								已关闭
								</c:if>
							</td>	
							<td>
							<c:if test="${bug[8]=='1'}">
							一般
							</c:if>
							<c:if test="${bug[8]=='2'}">
							中等
							</c:if>
							<c:if test="${bug[8]=='3'}">
							严重
							</c:if>
							<c:if test="${bug[8]=='4'}">
							致命
							</c:if>
							</td>
							<td>${ bug[9] }</td>
							<td width="200px">
								 <form  action="edit.do"  method="post">
									<input type="hidden" name="bugId" value="${ bug[0]}" />
									<input type="hidden" name="projectName" value="${bug[2]}" />
									<input type="hidden" name="userName" value="${bug[3]}" />

										<input class="btn btn-primary" type="submit" value="编辑"/>
									<c:if test="${ bug[7]=='undistributed'}">
										<input class="btn btn-primary" type="submit" name="delect" value="删除"/>
									</c:if>
								</form>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div align="center">
			<p>共有${ page.itemCount }条记录</p>
	
			<c:if test="${ page.curPage > 1 }">
				<a href="<c:url value="mybug.do">
					<c:param name="page" value="${ page.curPage - 1 }" />
				</c:url>">上一页</a>
			</c:if>
			<a>${ page.curPage }</a>
			<c:if test="${ page.curPage < page.pageCount }">
				<a href="<c:url value="mybug.do">
							<c:param name="page" value="${ page.curPage + 1 }" />
	
						</c:url>">下一页</a>
			</c:if>
			<a>共${ page.pageCount }页</a>	
			</div>		
		</div>
	</div>
</div>
</body>
</html>