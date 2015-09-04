<%@ page language="java" contentType="text/html; charset=UTF-8"
	isELIgnored="false" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="${ pageContext.request.contextPath }/css/bootstrap.min.css">
<link rel="stylesheet"
	href="${ pageContext.request.contextPath }/css/div.css" type="text/css"
	media="screen" />
<link
	href="${ pageContext.request.contextPath }/css/bootstrap-combined.min.css"
	rel="stylesheet">
<title>My bug</title>
</head>
<body>
	<div id="list">
		<ul id="showlist">
			<c:if test="${ not empty user_manage }">
				<li onmouseover="show('showlist_1');"
					onmouseout="hide('showlist_1')"><a href="#">人员管理</a>
					<ul id="showlist_1">
						<li><a href="user-audit.do">审核账户</a></li>
						<li><a href="user-manage.do">管理用户</a></li>
					</ul></li>
			</c:if>
			<c:if test="${ not empty project_manage }">
				<li onmouseover="show('showlist_2');"
					onmouseout="hide('showlist_2')"><a href="#">项目管理</a>
					<ul id="showlist_2">
						<li><a href="#">创建项目</a></li>
						<li><a href="#">管理项目</a></li>
					</ul></li>
			</c:if>
			<c:if test="${ not empty bug_manage }">
				<li onmouseover="show('showlist_3');"
					onmouseout="hide('showlist_3')"><a href="#">BUG管理</a>
					<ul id="showlist_4">
						<li><a href="mybug.do">我的BUG</a></li>
						<li><a href="#">创建BUG</a></li>
						<li><a href="#">查询BUG</a></li>
					</ul></li>
			</c:if>
			<li><a href="#">报表统计</a></li>
		</ul>
	</div>
	<div id="search">
		<form method="get" action="bugsearch.do">
			<h2>查询bug：</h2>
			<p>bug名称：</p>
			<input name="bug_name" type="text" value="${ param.bug_name }" />
			<p>所在项目名称：</p>
			<input name="bug_project" type="text" value="${ param.bug_project }" />
			<p>分配者名称：</p>
			<input name="bug_assignby" type="text" value="${ param.bug_assignby }" />
			<p>被分配者名称：</p>
			<input name="bug_assignto" type="text" value="${ param.bug_assignto }" />
			<p>bug状态</p>
			<select name="bug_status">
				<option value="">-</option>
				<option value="undistributed"
					<c:if test="${ param.bug_status == 'undistributed' }">selected="selected"</c:if>>Undistributed</option>
				<option value="distributed"
					<c:if test="${ param.bug_status == 'distributed' }">selected="selected"</c:if>>Distributed</option>
				<option value="developing"
					<c:if test="${ param.bug_status == 'developing' }">selected="selected"</c:if>>Developing</option>
				<option value="testing"
					<c:if test="${ param.bug_status == 'testing' }">selected="selected"</c:if>>Testing</option>
				<option value="test_passed"
					<c:if test="${ param.bug_status == 'test_passed' }">selected="selected"</c:if>>Test_passed</option>
				<option value="test_unpassed"
					<c:if test="${ param.bug_status == 'test_unpassed' }">selected="selected"</c:if>>Test_unpassed</option>
				<option value="closed"
					<c:if test="${ param.bug_status == 'closed' }">selected="selected"</c:if>>Closed</option>
			</select>
			<p>bug严重性</p>
			<select name="severity">
				<option value="">-</option>
				<option value="1"
					<c:if test="${ param.severity == '1' }">selected="selected"</c:if>>1</option>
				<option value="2"
					<c:if test="${ param.severity == '2' }">selected="selected"</c:if>>2</option>
				<option value="3"
					<c:if test="${ param.severity == '3' }">selected="selected"</c:if>>3</option>
				<option value="4"
					<c:if test="${ param.severity == '4' }">selected="selected"</c:if>>4</option>
			</select>
			<p>bug阶段</p>
			<select name="sprint">
				<option value="">-</option>
				<option value="1"
					<c:if test="${ param.sprint == '1' }">selected="selected"</c:if>>1</option>
				<option value="2"
					<c:if test="${ param.sprint == '2' }">selected="selected"</c:if>>2</option>
				<option value="3"
					<c:if test="${ param.sprint == '3' }">selected="selected"</c:if>>3</option>
				<option value="4"
					<c:if test="${ param.sprint == '4' }">selected="selected"</c:if>>4</option>
			</select>
			<p>bug标签</p>
			<input name="bug_tag" type="text" value="${ param.bug_tag }" />
			<p>创建者名称</p>
			<input name="creator" type="text" value="${ param.creator }" />
			<input type="reset" value="reset" /> <input name="submit" type="submit"
				value="submit" />
		</form>
	</div>
	<div>
		<table>
			<tr>
				<th>bugID</th>
				<th>bug名称</th>
				<th>bug描述</th>
				<th>bug状态</th>
				<th>bug严重性</th>
				<th>bug阶段</th>
				<th>bug标签</th>
				<th>创建日期</th>
				<th>所在项目ID</th>
				<th>创建者ID</th>
				<th>相关操作</th>
			</tr>
			<c:forEach var="bug" items="${ page_two.curItems }">
				<tr class="warning">
					<td>${ bug.bugId }</td>
					<td>${ bug.name }</td>
					<td>${ bug.description }</td>
					<td>${ bug.bugStatus }</td>
					<td>${ bug.severity }</td>
					<td>${ bug.sprint }</td>
					<td>${ bug.tag }</td>
					<td>${ bug.createDate }</td>
					<td>${ bug.project.projectId }</td>
					<td>${ bug.user.userId }</td>
					<td>
						<a href="#">编辑</a> 
						<a href="#">评论</a>
					</td>
				</tr>
			</c:forEach>
		</table>
		<p>共有${ page.itemCount }条记录</p>

		<c:if test="${ page.curPage > 1 }">
			<a
				href="<c:url value="bugsearch.do">
				<c:param name="page" value="${ page_two.curPage - 1 }" />
				<c:param name="bug_id" value="${ param.bug_id }" />
				<c:param name="bug_name" value="${ param.bug_name }" />
				<c:param name="bug_status" value="${ param.bug_status }" />
				<c:param name="severity" value="${ param.severity }" />
				<c:param name="sprint" value="${ param.sprint }" />
				<c:param name="bug_tag" value="${ param.bug_tag }" />
				<c:param name="start_date" value="${ param.start_date }" />
				<c:param name="end_date" value="${ param.end_date }" />
				<c:param name="project_id" value="${ param.project_id }" />
				<c:param name="creator_id" value="${ param.creator_id }" />
			</c:url>">上一页</a>
		</c:if>
		<a>${ page_two.curPage }</a>
		<c:if test="${ page_two.curPage < page_two.pageCount }">
			<a
				href="<c:url value="bugsearch.do">
				<c:param name="page" value="${ page_two.curPage + 1 }" />
				<c:param name="bug_id" value="${ param.bug_id }" />
				<c:param name="bug_name" value="${ param.bug_name }" />
				<c:param name="bug_status" value="${ param.bug_status }" />
				<c:param name="severity" value="${ param.severity }" />
				<c:param name="sprint" value="${ param.sprint }" />
				<c:param name="bug_tag" value="${ param.bug_tag }" />
				<c:param name="start_date" value="${ param.start_date }" />
				<c:param name="end_date" value="${ param.end_date }" />
				<c:param name="project_id" value="${ param.project_id }" />
				<c:param name="creator_id" value="${ param.creator_id }" />
			</c:url>">下一页</a>
		</c:if>
		<a>共${ page_two.pageCount }页</a>

	</div>
</body>
</html>