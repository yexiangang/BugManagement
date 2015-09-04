<%@ page language="java" contentType="text/html; charset=UTF-8"
	isELIgnored="false" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="${ pageContext.request.contextPath }/css/div.css" type="text/css"
	media="screen" />
<link rel="stylesheet"
	href="${ pageContext.request.contextPath }/css/bootstrap-combined.min.css"
	type="text/css" media="screen" />
<script src="${ pageContext.request.contextPath }/js/bugzilla.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="${ pageContext.request.contextPath }/js/jquery-1.11.3.js"></script>
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
	function showDiv(id, name, tl, sprint, status, desc, members) {
		if (status == "closed") {
			alert("该项目已被关闭，不能编辑！");
			return;
		}
		document.getElementById('selectProjectId').innerHTML = id;
		document.getElementById('selectProjectId2').value = id;
		document.getElementById('selectProjectName').value = name;
		document.getElementById('description').value = desc;
		document.getElementById('sprintMydiv').value = sprint;
		document.getElementById('selectProjectTL').value = tl;

		if(status == 'developing'){
			document.getElementById('selectProjectTL').disabled = true;
		}

		$("input[type=checkbox]").each(function() {
			this.checked = false
		});

		$.ajax({
			data : "projectId=" + id,
			type : "POST",
			url : "showMembers.do",
			success : function(data) {
				var ss = data.split(",");
				var ss0 = ss[0].split(" ");
				var ss1 = ss[1].split(" ");

				for (var i = 0, l = ss0.length; i < l - 1; i++) {
					var s = "isChoosed" + ss0[i];
					document.getElementById(s).disabled = false;
				}
				
				for (var i = 0, l = ss1.length; i < l - 1; i++) {
					var s = "isChoosed" + ss1[i];
					document.getElementById(s).checked = true;
					if(status == 'developing'){
						document.getElementById(s).disabled = true;
					}
				}
			},
			error : function(data) {
				alert("error : " + data);

			}
		});


		
		document.getElementById('popDiv').style.display = 'block';
		document.getElementById('bg').style.display = 'block';
	}
	
	function showDiv2(id, name, tl, sprint, status, desc) {
		if (status == "closed") {
			alert("该项目已被关闭，不能编辑！");
			return;
		}
		document.getElementById('selectProjectIdEdit2').innerText = id;
		document.getElementById('selectProjectIdEdit22').value = id;
		document.getElementById('selectProjectNameEdit2').value = name;
		document.getElementById('descriptionEdit2').value = desc;
		document.getElementById('sprintMydiv2').value = sprint;
		document.getElementById('selectProjectTLEdit2').innerText = tl;


		$.ajax({
			data : "projectId=" + id,
			type : "POST",
			url : "showMembersToString.do",
			success : function(data) {
				document.getElementById('membersEdit2').innerHTML = data;
			},
			error : function(data) {
				alert("error : " + data);

			}
		});
		
		document.getElementById('popDiv2').style.display = 'block';
		document.getElementById('bg').style.display = 'block';
	}


	function closeDiv2() {
		document.getElementById('popDiv2').style.display = 'none';
		document.getElementById('bg').style.display = 'none';
		}
	
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
<title>Manage Project</title>
</head>
<body>
	<div class="container">
		<c:import url="title.jsp"></c:import>
		<div class="row">
			<c:import url="menu.jsp"></c:import>
			<div class="span8">

				<div id="search">
					<form name="form" method="post" action="project-manage.do">
						<h2>查找项目</h2>
						<p>项目名称:</p>
						<input type="text" name="projectname"
							value="${ param.projectname }" /> <input type="hidden"
							name="page" value="${ param.page }" /> <input
							class="btn btn-primary" type="submit" name="search" value="查找" />
					</form>
				</div>
				<div id="all_project">
					<c:if test="${ project_manage_result_flag }">
						<p style="color: green;">${ project_manage_result }</p>
					</c:if>
					<c:if test="${ not project_manage_result_flag }">
						<p style="color: red;">${ project_manage_result }</p>
					</c:if>
					<p>所有项目</p>
					<table class="table table-striped">
						<tr id="title">
							<th>项目ID</th>
							<th>项目名称</th>
							<th>项目主管</th>
							<th>项目阶段</th>
							<th>项目状态</th>
							<th colspan="3">项目操作</th>
							<th></th>
							<th></th>
						</tr>
						<c:forEach var="project" items="${ page.curItems }">
							<tr id="content">
								<td>${ project.projectId }</td>
								<td>${ project.name }</td>
								<td>${ project.userByLeader.name }</td>
								<td>${ project.sprint }</td>
								<td><c:if test="${ project.projectStatus eq project_status_created}">未启动</c:if>
									<c:if test="${ project.projectStatus eq project_status_developing}">开发中</c:if>
									<c:if test="${ project.projectStatus eq project_status_close}">已关闭</c:if>
								</td>
								<td colspan="3">
									<c:if test="${ project.projectStatus eq project_status_created || project.projectStatus eq project_status_developing}">
										<button class="btn btn-primary" href="#popDiv"
											onclick="showDiv('${ project.projectId }','${ project.name }','${ project.userByLeader.name }',
											'${ project.sprint }','${ project.projectStatus }','${ project.description }','${ members }')"
											style="display: block; cursor: pointer" >编辑</button>
									</c:if>
									<c:if test="${ project.projectStatus eq project_status_close}">
										<button class="btn btn-primary" style="display: block; cursor: pointer" disabled="disabled" >编辑</button>
									</c:if>
								</td>
								<td>
									<form action="project-manage.do" method="post">
										<input type="hidden" name="projectname" value="${ param.projectname }" /> 
										<input type="hidden" name="projectId" value="${ project.projectId }" /> 
										<input type="hidden" name="page" value="${ param.page }" />
										<c:if test="${ project.projectStatus eq project_status_developing}">
											<input class="btn btn-primary" type="submit" name="close"
												value="关闭" />
										</c:if>
										<c:if test="${ project.projectStatus eq project_status_close or project.projectStatus eq project_status_created }">
											<input class="btn btn-primary" type="submit" name="activate"
												value="激活" />
										</c:if>
									</form>
								</td>
								<td>
									<form action="project-manage.do" method="post">
										<input type="hidden" name="projectname" value="${ param.projectname }" /> 
										<input type="hidden" name="projectId" value="${ project.projectId }" /> 
										<input type="hidden" name="page" value="${ param.page }" />
										<input class="btn btn-primary" type="submit" name="delete"
										<c:if test="${ project.projectStatus eq project_status_developing or project.projectStatus eq project_status_close }"> disabled="disabled"</c:if>
											value="删除" />
									</form>
								</td>
							</tr>
						</c:forEach>
					</table>
					<div align="center">
						<c:url var="url" value="project-manage.do" scope="page">
							<c:param name="projectname" value="${ param.projectname }"></c:param>
						</c:url>

						<p>共有${ page.itemCount }条记录</p>
						<c:if test="${ page.curPage > 1 }">
							<a class="n" href="${ url }&page=1">首页</a>
							<a class="n" href="${ url }&page=${ page.curPage - 1 }">上页</a>
						</c:if>

						<c:forEach items="${ page.showPageIndices }" var="index">
							<a href="${ url }&page=${ index }">${ index }</a>
						</c:forEach>

						<c:if test="${ page.curPage < page.pageCount }">
							<a class="n" href="${ url }&page=${ page.curPage + 1 }">下页</a>
							<a class="n" href="${ url }&page=${ page.pageCount }">尾页</a>
						</c:if>
					</div>
				</div>

				<div id="popDiv" class="mydiv" style="display: none;">
					<div class="dialog-title">修改项目</div>
					<div class="dialog-content">
					<form action="edit-project.do" method="post">
					<table class="table">
						
							<tr>
								<td  width=150px>项目ID</td>
								<td >
									<p id="selectProjectId"></p>
									<input id="selectProjectId2" type="hidden" name="project_id" />
								</td>
							</tr>
							<tr>
								<td>项目名称</td>
								<td>
								<input id="selectProjectName" type="text" name="project_name" />
								</td>
							</tr>
							<tr>
								<td>Team Leader</td>
								<td>
								<select id="selectProjectTL" name="teamleader">
									<c:forEach var="leader" items="${ leaders }">
										<option value="${ leader.name }">${ leader.name }</option>
									</c:forEach>
								</select>
								</td>
							</tr>
							<tr>
								<td>项目描述</td>
								<td>
								<textarea id="description" cols="30" name="description"></textarea>
								</td>
							</tr>
							<tr>
								<td>添加人员</td>
								<td>
									<c:forEach varStatus="status" var="member" items="${ members }">
										<input class="membercheckbox" type="checkbox"
											id="isChoosed${member.userId }"
											name="member${ member.userId }" />${ member.role }:${ member.name }
											<br/>
									</c:forEach>
								</td>
							</tr>
							<tr>
								<td>项目阶段</td>
								<td>
								<input id="sprintMydiv" type="text" name="sprint" />
								</td>
							</tr>
							</table>
								<br/>
								<input type="hidden" name="projectname"
									value="${ param.projectname }" />
								<input type="hidden" name="page" value="${ param.page }" />
								<input class="btn btn-primary" type="submit" name="edit"
									value="提交" />
						</form>
						<a href="#" onclick="closeDiv()"
							style="display: block; cursor: pointer">点击关闭页面</a>
					</div>
				</div>
				
				
				
				
				
				<div id="popDiv2" class="mydiv" style="display: none;">
					<div class="dialog-title">修改项目</div>
					<div class="dialog-content">
					<form action="edit-project.do" method="post">
					<table class="table">
						
							<tr>
								<td  width=150px>项目ID</td>
								<td >
									<p id="selectProjectIdEdit2"></p>
									<input id="selectProjectIdEdit22" type="hidden" name="project_id" />
								</td>
							</tr>
							<tr>
								<td>项目名称</td>
								<td>
								<input id="selectProjectNameEdit2" type="text" name="project_name" />
								</td>
							</tr>
							<tr>
								<td>Team Leader</td>
								<td>
									<p id="selectProjectTLEdit2"></p>
								</td>
							</tr>
							<tr>
								<td>项目描述</td>
								<td>
								<textarea id="descriptionEdit2" cols="30" name="description"></textarea>
								</td>
							</tr>
							<tr>
								<td>项目人员</td>
								<td id="membersEdit2">
								</td>
							</tr>
							<tr>
								<td>项目阶段</td>
								<td>
									<input id="sprintMydiv2" type="text" name="sprint" />
								</td>
							</tr>
							</table>
								<br/>
								<input type="hidden" name="projectname"
									value="${ param.projectname }" />
								<input type="hidden" name="page" value="${ param.page }" />
								<input class="btn btn-primary" type="submit" name="edit2"
									value="编辑" />
						</form>
						<a href="#" onclick="closeDiv2()"
							style="display: block; cursor: pointer">点击关闭页面</a>
					</div>
				</div>
				
	
				
				
				
				
			</div>
		</div>
	</div>
</body>
</html>