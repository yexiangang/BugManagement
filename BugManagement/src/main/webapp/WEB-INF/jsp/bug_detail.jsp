<%@ page language="java" contentType="text/html; charset=UTF-8"
	isELIgnored="false" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Bug Detail</title>
<link rel="stylesheet"
	href="${ pageContext.request.contextPath }/css/div.css" type="text/css"
	media="screen" />


<script type="text/javascript" src="${ pageContext.request.contextPath }/js/bugzilla.js"></script>

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
	<div class="container">
		<c:import url="title.jsp"></c:import>
		<div class="row">
			<div class="span2">
			<div id="my_menu" class="sdmenu">
			<c:import url="menu.jsp"></c:import>
			</div>
			</div>
			<div class="span8">
				<table class="table table-striped">
					<tbody>
						<tr>
							<td>bugID</td>
							<td>${ bug.bugId }</td>
						</tr>
						<tr>
							<td>bug标题</td>
							<td>${ bug.name }</td>
						</tr>
						<tr>
							<td>bug描述</td>
							<td>${ bug.description }</td>
						</tr>
						<tr>
							<td>分配给</td>
							<td><c:forEach var="assign" items="${ bug.assignments }">
									<c:if test="${ empty assign.endTime }">
									${ assign.userByAssignedUserId.name }
									</c:if>
								</c:forEach></td>
						</tr>
						<tr>
							<td>bug创建者</td>
							<td>${ bug.user.name }</td>
						</tr>
						<tr>
							<td>所在项目</td>
							<td>${ bug.project.name }</td>
						</tr>
						<tr>
							<td>创建时间</td>
							<td>${ bug.createDate }</td>
						</tr>
					</tbody>
				</table>
				<table class="table">
					<thead>
						<tr>
							<th>评论列表</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="comment" items="${ page.curItems }">
							<tr class="error">
								<td>${ comment.user.name }发表于${ comment.pubTime }</td>
								<td>${ comment.user.role }</td>
								<td><a
									href="<c:url value="bug-detail.do" >
									<c:param name="id" value="${ param.id }" />
									<c:param name="refer_id" value="${ comment.commentId }" />
									<c:param name="page" value="${ param.page }" />
								</c:url>">引用</a></td>
							</tr>
							<tr>
								<td><c:if test="${ not empty comment.referComment }">
										<p style="background-color: #d9edf7;">引用： ${ comment.referComment.user.name }
											发表于 ${ comment.referComment.pubTime }: ${ comment.referComment.content }
										</p>
									</c:if> ${ comment.content }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<p>共有${ page.itemCount }条记录</p>

				<c:if test="${ page.curPage > 1 }">
					<a
						href="<c:url value="bug-detail.do">
						<c:param name="page" value="${ page.curPage - 1 }" />
						<c:param name="id" value="${ param.id }" />
						<c:param name="refer_id" value="${ param.refer_id }" />
					</c:url>">上一页</a>
				</c:if>
				<a>${ page.curPage }</a>
				<c:if test="${ page.curPage < page.pageCount }">
					<a
						href="<c:url value="bug-detail.do">
						<c:param name="page" value="${ page.curPage + 1 }" />
						<c:param name="id" value="${ param.id }" />
						<c:param name="refer_id" value="${ param.refer_id }" />
					</c:url>">下一页</a>
				</c:if>
				<a>共${ page.pageCount }页</a>

				<form action="comment.do">
					<fieldset>
						<input type="hidden" name="id" value="${ param.id }" /> <input
							type="hidden" name="refer_id"
							value="${ refer_comment.commentId }" />
						<legend>发表评论</legend>
						<c:if test="${ not empty refer_comment }">
							<p class="has-warning">引用： ${ refer_comment.user.name } 发表于
								${ refer_comment.pubTime }</p>
							<p class="has-warning">${ refer_comment.content }</p>
							<a href="bug-detail.do?id=${ param.id }&page=${param.page }">取消引用</a>
						</c:if>
						<label>在此输入评论</label><textarea class="comment" placeholder="输入评论" type="text"
							name="content" ></textarea> <span class="help-block">评论不要超过50字</span>
						<button class="btn btn-primary" type="submit" name="submit" value="submit">提交</button>
						<input class="btn btn-primary"
						type=button value="返回"  onClick ="javascript:location.href ='javascript:window.history.back(-1);'"/>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</body>
</html>