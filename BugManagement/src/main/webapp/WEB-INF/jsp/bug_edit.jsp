<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
	src="${ pageContext.request.contextPath }/js/edit.js"></script>
<script type="text/javascript"
	src="${ pageContext.request.contextPath }/js/bootstrap-datetimepicker.zh-CN.js"
	charset="UTF-8"></script>
<script type="text/javascript">
	function projectChange(obj) {
		var form = document.form1;
		form.projectId.value = obj.options[obj.selectedIndex].value;
		form.submit();
	}
</script>

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
<script type="text/javascript">
	function assignSubmit(){
		var value=document.getElementById("user_select").value;
		if(value.length>0)
		{	
			document.getElementById("output").innerHTML="";
		}
		else
		{
			 alert("未选中分配对象");
		}
	}
	function changeText()
	{
		document.getElementById("userSelect").value=document.getElementById("user_select").value;
	}
</script>
</head>
<body>

	<div class="container">
	<c:import url="title.jsp"></c:import>
	<div class="row">
		<c:import url="menu.jsp"></c:import>
		<div class="span8">
		
		
		<!-- the changes of Bug Status -->
		<c:if test="${bug.project.projectStatus == 'developing'}">
			<form name="assignForm"  action="assign.do"  method="post">
					<input type="hidden" name="bugId" value="${ bug.bugId }" />
					<input type="hidden" id="userSelect" type="hidden"  name="userSelect" />
					<table class="table table-hover">
					<td>
					<c:if test="${bug.bugStatus=='testing'}" >
						<p>Bug完成测试，请选择结果：</p>
						
						<button name="assign"  value="test_pass"
							class="btn btn-primary" type="submit">测试通过
							
						</button>
						<button name="assign"  value="test_unpass"
							class="btn btn-primary" type="submit">测试未通过</button>
							
					</c:if>
						
					<c:if test="${bug.bugStatus!='testing' && bug.bugStatus!='closed'}">
						<p>
						<c:if test="${bug.bugStatus=='undistributed'}">
								Bug未分配，需分配人员(下方选择人员)，请点击：
							</c:if>
							<c:if test="${bug.bugStatus=='distributed'}">
								Bug已分配给你，需开发，请点击：
							</c:if>
							<c:if test="${bug.bugStatus=='developing'}">
								Bug开发完成，需提交测试(下方选择人员)，请点击：
							</c:if>
							<c:if test="${bug.bugStatus=='developed'}">
								Bug开发完成，需测试，请点击：
							</c:if>
							<c:if test="${bug.bugStatus=='test_passed'}">
								Bug已解决，需关闭，请点击：
							</c:if>
							<c:if test="${bug.bugStatus=='test_unpassed'}">
								Bug测试未通过，需分配开发(下方选择人员)，请点击：
							</c:if>
						</p>
						<button id="assign"  name="assign"  value="done" onclick="assignSubmit()"
							class="btn btn-primary" type="submit">
							<c:if test="${bug.bugStatus=='undistributed'}">
								分配
							</c:if>
							<c:if test="${bug.bugStatus=='distributed'}">
								开发
							</c:if>
							<c:if test="${bug.bugStatus=='developing'}">
								提交测试
							</c:if>
							<c:if test="${bug.bugStatus=='developed'}">
								测试
							</c:if>
							<c:if test="${bug.bugStatus=='test_passed'}">
								关闭
							</c:if>
							<c:if test="${bug.bugStatus=='test_unpassed'}">
								分配
							</c:if>
						</button>
					</c:if>
					</td>
						</table>
					</form>
			</c:if>		
					
			
			<form name="edit" action="edited.do" onsubmit="return edit()" method="post" enctype="multipart/form-data">
				<table class="table table-hover">
					<tbody>
						<c:if test="${ bug.bugStatus=='undistributed'  || bug.bugStatus=='developing'  ||  bug.bugStatus=='test_unpassed' }">
							<tr>
								<td>
								分配给项目人员：
								</td>
								<td>
								<select name="user_select" onchange="changeText()" id="user_select"
									>
									<option value="">请选择</option>
									<c:forEach var="member" items="${members}">
										<option value="${member.user.name }">${member.user.role} ：${member.user.name }</option>
										
									</c:forEach>
								</select>
								<!-- 
								<c:if test="${ bug.bugStatus=='undistributed'  || bug.bugStatus=='developing'  ||  bug.bugStatus=='test_unpassed' }">
									<p style="color: red;"  id="output"></p>
								</c:if>
								 -->
								</td>
							</tr>
						</c:if>	
						<tr>
							<td>
								bug编号
							</td>
							<td>
								${bug.bugId}
							</td>
						</tr>
						<tr>
							<td>
								bug标题
							</td>
								<c:choose>
	  									<c:when test="${role==4 || bug.bugStatus=='closed'}">
	  										<td>${ bug.name }</td>
	  									</c:when>
								    	<c:otherwise>
					        				<td>
					        					<textarea   name="tittle" id="tittle" cols="30" rows="1" onblur="checksss()">${bug.name}</textarea>
					    						<p id="tip" style="color: red;"></p>
					    					</td>
								    	</c:otherwise>
								</c:choose>
						</tr>
						<tr >
							<td>
								bug描述
							</td>
							<td>
								<c:choose>
   										<c:when test="${role==4 || bug.bugStatus=='closed'}">
  											${ bug.description}
								    	</c:when>
								    	<c:otherwise>
								        	<p>bug描述</p>
											<textarea class="comment" name="description" cols="30" rows="5">${ bug.description}</textarea>
											<br />

											<p>图片上传(bmp,png,jpg格式)</p>
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
											<br/>
								    	</c:otherwise>
								</c:choose>
								<c:forEach var="url" items="${urls}">
									<img src="${url}" height="100" width="100"/>
								</c:forEach>
								
							</td>
						</tr>
						<tr>
							<td>
								bug创建者
							</td>
							<td>
								${bug.user.name}
							</td>
						</tr>
						<tr>
							<td>
								所在项目
							</td>
							<td>
								${bug.project.name}
							</td>
						</tr>
						<tr>
							<td>
								创建时间
							</td>
							<td>
								${bug.createDate}
							</td>
						</tr>
						<tr>
							<td>
								bug状态
							</td>
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
									href="<c:url value="edit.do?bugId=${id}" >
									<c:param name="ids" value="${ param.ids }" />
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
				<c:if test="${ empty page.itemCount }"><p style="color: red;">目前无人评论</p></c:if>
				<c:if test="${ page.itemCount >0}"><p>共有${ page.itemCount }条记录</p></c:if>
				<c:if test="${ page.curPage > 1 }">
					<a
						href="<c:url value="edit.do?bugId=${bug.bugId}" >
						<c:param name="page" value="${ page.curPage - 1 }" />
						<c:param name="ids" value="${ param.ids }" />
						<c:param name="refer_id" value="${ param.refer_id }" />
					</c:url>">上一页</a>
				</c:if>
				<a>${ page.curPage }</a>
				<c:if test="${ page.curPage < page.pageCount }">
					<a
						href="<c:url value="edit.do?bugId=${bug.bugId}" >
						<c:param name="page" value="${ page.curPage + 1 }" />
						<c:param name="ids" value="${ param.ids }" />
						<c:param name="refer_id" value="${ param.refer_id }" />
					</c:url>">下一页</a>
				</c:if>
				<c:if test="${not empty page.pageCount}"><a>共${ page.pageCount }页</a></c:if>
					<fieldset>
						<input type="hidden" name="ids" value="${ param.ids }" /> <input
							type="hidden" name="refer_id"
							value="${ refer_comment.commentId }" />
						<legend>发表评论</legend>
						<c:if test="${ not empty refer_comment }">
							<p class="has-warning">引用： ${ refer_comment.user.name } 发表于
								${ refer_comment.pubTime }</p>
							<p class="has-warning">${ refer_comment.content }</p>
							<a href="edit.do?bugId=${id}&ids=${ param.ids }&page=${param.page }">取消引用</a>
						</c:if>
						<label>在此输入评论</label><textarea class="comment" placeholder="输入评论" name="content" ></textarea> <span class="help-block">评论不要超过50字</span>
						<input class="btn btn-primary" type="submit" name="submit" id="submit" value="提交"/>
						<input class="btn btn-primary"
						type=button value="返回"  onClick ="javascript:location.href ='javascript:window.history.back(-1);'"/>
						<p style="color: red;">
						<c:if test="${param.tip==2}">
						bug修改成功，未进行评论
						</c:if>
						<c:if test="${param.tip==3}">
						bug未进行修改和评论
						</c:if>
						<c:if test="${param.tip==4}">
						bug已修改，评论成功
						</c:if>
						<c:if test="${param.tip==5}">
						bug未修改，评论成功
						</c:if>
						
						</p>
					</fieldset>
					</form>
					
					
			</div>
		</div>
	</div>


</body>
</html>
