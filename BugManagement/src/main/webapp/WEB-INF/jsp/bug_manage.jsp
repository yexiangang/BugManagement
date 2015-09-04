<%@ page language="java" contentType="text/html; charset=UTF-8"
	isELIgnored="false" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search bug</title>

<link rel="stylesheet" href="${ pageContext.request.contextPath }/css/div.css" type="text/css" media="screen"/>
<link href="${ pageContext.request.contextPath }/css/bootstrap-combined.min.css" rel="stylesheet">
<link rel="stylesheet" href="${ pageContext.request.contextPath }/css/bootstrap-datetimepicker.min.css" type="text/css" media="screen"/>

<script type="text/javascript" src="${ pageContext.request.contextPath }/js/jquery-1.8.3.min.js" charset="UTF-8"></script>
<script type="text/javascript" src="${ pageContext.request.contextPath }/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ pageContext.request.contextPath }/js/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script type="text/javascript" src="${ pageContext.request.contextPath }/js/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
<script type="text/javascript" src="${ pageContext.request.contextPath }/js/My97DatePicker/WdatePicker.js"></script>
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

function change_reset()
{
	document.getElementById("bug_name").value="";
	document.getElementById("project_name").value="";
	document.getElementById("creator").value="";
	document.getElementById("start_date").value="";
	document.getElementById("end_date").value="";
	document.getElementById("bug_assigned").value="";
	document.getElementById("bug_tag").value="";
	document.getElementById("sprint").value="";
	var options_1=document.getElementById("bug_status").options;
	for(i=0;i<options_1.length;i++)
	{
		if(options_1[i].value=="")
		{
			options_1[i].selected=true;
		}
	}
	var options_1=document.getElementById("severity").options;
	for(i=0;i<options_1.length;i++)
	{
		if(options_1[i].value=="")
		{
			options_1[i].selected=true;
		}
	}

}
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
			<div class="span8" id="search_bug">
				<div id="link">
					<h2>查询bug</h2>

				</div>
				<form class="form-search"  action="bugs.do">
					<table>
						<tbody>
							<tr>
								<th>bug名称:</th>
								<th>项目名称：</th>
								<th>创建者：</th>
								<th>从：</th>
								<th>到：</th>
							</tr>
							<tr>
								<td><input id="bug_name"
									
									name="bug_name" type="text" placeholder="在此输入bug名称" 
									value="${ param.bug_name }"
									/></td>
								<td><input   id="project_name"
									
									name="project_name" type="text" placeholder="在此输入项目名称" 
									value="${ param.project_name }"
									/></td>
								<td><input  id="creator"
									 name="creator" type="text"
									placeholder="在此输入创建者名称" 
									value="${param.creator}"
									/>
								</td>
								<td><input  id="start_date" 
									readonly="readonly"
									name="start_date" type="text" placeholder="在此输入起始时间" 
									value="${param.start_date}" onfocus="WdatePicker()"
									/>
									
								</td>
								<td>
								<input  id="end_date" 
									readonly="readonly"
									name="end_date" type="text"  placeholder="在此输入终止时间" 
									value="${param.end_date}" onfocus="WdatePicker()" 
									/>
									
								</td>
							</tr>
							<tr>
								<th>bug标签</th>
								<th>分配给</th>
								<th>项目阶段</th>
								<th>bug状态</th>
								<th>bug严重性</th>
							</tr>
							<tr>
							<td><input   id="bug_tag"
								
								name="bug_tag"  type="text" placeholder="在此输入bug标签" 
								value="${param.bug_tag}"
								/>
								</td>
								<td><input	  id="bug_assigned"
									
									name="bug_assigned" type="text" placeholder="在此输入被分配者名称" 
									value="${param.bug_assigned}"
									/></td>
								<td><input		id="sprint" 
									type="text"
										 placeholder="在此输入项目阶段" 
										 name="sprint"
										value="${param.sprint}">
								</td>
								<td><select		name="bug_status" 
										 name="bug_status">
										<option value="">请选择</option>
										<option value="undistributed"
										<c:if test="${ param.bug_status == 'undistributed' }">selected="selected"</c:if>
										>未分配</option>
										<option value="distributed"
										<c:if test="${ param.bug_status == 'distributed' }">selected="selected"</c:if>
										>已分配</option>
										<option value="developing"
										<c:if test="${ param.bug_status == 'developing' }">selected="selected"</c:if>
										>开发中</option>
										<option value="developed"
										<c:if test="${ param.bug_status == 'developed' }">selected="selected"</c:if>
										>开发完成</option>
										<option value="testing"
										<c:if test="${ param.bug_status == 'testing' }">selected="selected"</c:if>
										>测试中</option>
										<option value="test_passed"
										<c:if test="${ param.bug_status == 'test_passed' }">selected="selected"</c:if>
										>测试通过</option>
										<option value="test_unpassed"
										<c:if test="${ param.bug_status == 'test_unpassed' }">selected="selected"</c:if>
										>测试未通过</option>
										<option value="closed"
										<c:if test="${ param.bug_status == 'closed' }">selected="selected"</c:if>
										>测试关闭</option>
								</select></td>
								<td><select id="severity"
										 name="severity">
										<option value="">请选择</option>
										<option  value="1"
										<c:if test="${ param.severity == '1' }">selected="selected"</c:if>
										>一般
										</option>
										<option value="2"
										<c:if test="${ param.severity == '2' }">selected="selected"</c:if>
										>中等
										</option>
										<option value="3"
										<c:if test="${ param.severity == '3' }">selected="selected"</c:if>
										>严重
										</option>
										<option value="4"
										<c:if test="${ param.severity == '4' }">selected="selected"</c:if>
										>致命
										</option></select></td>
							</tr>
						</tbody>
					</table>
					<button class="btn btn-primary" type="submit">查找</button>
					<button class="btn btn-primary" type="button"  onclick="change_reset()">重置</button>
				</form>
					
				<table class="table table-striped">
					<thead>
						<tr>
							<th>查询结果</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>bugID</td>
							<td>bug名称</td>
							<td>项目名称</td>
							<td>创建者</td>
							<td>bug标签</td>
							<td>项目阶段</td>
							<td>bug状态</td>
							<td>bug严重性</td>
							<td>创建时间</td>
							<td>相关操作</td>
						</tr>
						<c:forEach var="bug" items="${ page.curItems }">
						<tr>
							<td>${ bug.bugId }</td>
							<td>
								<a href="<c:url value="bug-detail.do?id=${ bug.bugId }"></c:url>">${ bug.name }</a>
							</td>
							<td>${ bug.project.name }</td>
							<td>${ bug.user.name }</td>
							<td>${ bug.tag }</td>
							<td>${ bug.sprint }</td>
							<td>
							<c:if test="${bug.bugStatus=='undistributed'}">
								未分配
								</c:if>
								<c:if test="${bug.bugStatus=='distributed'}">
								已分配
								</c:if>
								<c:if test="${bug.bugStatus=='developing'}">
								开发中
								</c:if>
								<c:if test="${bug.bugStatus=='developed'}">
								开发完成
								</c:if>
								<c:if test="${bug.bugStatus=='testing'}">
								测试中
								</c:if>
								<c:if test="${bug.bugStatus=='test_passed'}">
								测试通过
								</c:if>
								<c:if test="${bug.bugStatus=='test_unpassed'}">
								测试未通过
								</c:if>
								<c:if test="${bug.bugStatus=='closed'}">
								已关闭
								</c:if>
							</td>
							<td>
							<c:if test="${bug.severity=='1'}">
							一般
							</c:if>
							<c:if test="${bug.severity=='2'}">
							中等
							</c:if>
							<c:if test="${bug.severity=='3'}">
							严重
							</c:if>
							<c:if test="${bug.severity=='4'}">
							致命
							</c:if>
							</td>
							<td>${ bug.createDate }</td>
							<td>
								<a class="btn btn-primary" href="bug-detail.do?id=${ bug.bugId }">查看</a>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
					<p>共有${ page.itemCount }条记录</p>
					
				<c:if test="${ page.curPage > 1 }">
				<a
					href="<c:url value="bugs.do">
					<c:param name="page" value="${ page.curPage - 1 }" />
					<c:param name="bug_name" value="${ param.bug_name }" />
					<c:param name="project_name" value="${ param.project_name }" />
					<c:param name="creator" value="${ param.creator }" />
					<c:param name="bug_status" value="${ param.bug_status }" />
					<c:param name="severity" value="${ param.severity }" />
					<c:param name="sprint" value="${ param.sprint }" />
					<c:param name="bug_tag" value="${ param.bug_tag }" />
					<c:param name="bug_assigned" value="${ param.bug_assigned }" />
					<c:param name="start_date" value="${ param.start_date }" />
					<c:param name="end_date" value="${ param.end_date }" />
					
				</c:url>">上一页</a>
			</c:if>
				<a>${ page.curPage }</a>
				<c:if test="${ page.curPage < page.pageCount }">
				<a
					href="<c:url value="bugs.do">
					<c:param name="page" value="${ page.curPage + 1 }" />
					<c:param name="bug_name" value="${ param.bug_name }" />
					<c:param name="project_name" value="${ param.project_name }" />
					<c:param name="creator" value="${ param.creator }" />
					<c:param name="bug_status" value="${ param.bug_status }" />
					<c:param name="severity" value="${ param.severity }" />
					<c:param name="sprint" value="${ param.sprint }" />
					<c:param name="bug_tag" value="${ param.bug_tag }" />
					<c:param name="bug_assigned" value="${ param.bug_assigned }" />
					<c:param name="start_date" value="${ param.start_date }" />
					<c:param name="end_date" value="${ param.end_date }" />
				</c:url>">下一页</a>
				</c:if>
				<a>共${ page.pageCount }页</a>
			</div>
		</div>
	</div>
</body>
</html>