package com.my.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.my.domain.Bug;
import com.my.domain.Comment;
import com.my.domain.Project;
import com.my.domain.User;
import com.my.service.BugService;
import com.my.service.CommentService;
import com.my.service.ProjectService;
import com.my.util.Constant;
import com.my.util.Page;

@Controller
public class BugManageController {
	
	@Autowired
	BugService bugService;
	@Autowired
	ProjectService projectService;
	
	@Autowired
	CommentService commentService;
	
	@RequestMapping(value = "bugsearch.do")
	public ModelAndView searchBug(HttpServletRequest request, HttpSession session)
	{
		ModelAndView bugManagement=new ModelAndView("bug_manageTwo");
		String bug_name=request.getParameter("bug_name");
		String bug_project=request.getParameter("bug_project");
		String creator=request.getParameter("creator");
		String bug_assignby=request.getParameter("bug_assignby");
		String bug_assignto=request.getParameter("bug_assignto");
		String bugTag = request.getParameter("bug_tag");
		String sprint=request.getParameter("sprint");
		String bug_status=request.getParameter("bug_status");
		String bug_severity=request.getParameter("severity");
		String pageStr = request.getParameter("page");
		Integer curPage = (pageStr == null || pageStr.isEmpty()) ? 1 : Integer.parseInt(pageStr);
		if("".equals(bug_name))
		{
			bug_name=null;
		}
		if("".equals(bug_project)) 
		{
			bug_project=null;
		}
		if("".equals(creator)) 
		{
			creator=null;
		}
		if("".equals(bug_assignby)) 
		{
			bug_assignby=null;
		}
		if("".equals(bug_assignto)) 
		{
			bug_assignto=null;
		}
		if("".equals(bugTag))
		{
			bugTag=null;
		}
		if("".equals(sprint)) 
		{
			sprint=null;
		}
		if("".equals(bug_status)) 
		{
			bug_status=null;
		}
		if("".equals(bug_severity)) 
		{
			bug_severity=null;
		}
		//search different projects
		User user=(User) session.getAttribute("online_user");
		List<Integer> projectIdList=bugService.findProjectIdList(Constant.ROLE_ADMIN,2,bug_project);
		//List<Integer> projectIdList=bugService.findProjectIdList(user.getRole(),user.getUserId(),bug_project);
		//search bug_id_list
		Bug bug=new Bug();
		bug.setName(bug_name);
		bug.setTag(bugTag);
		bug.setBugStatus(bug_status);
		//Sprint is String
		//if(sprint!=null) bug.setSprint((byte)(Integer.parseInt(sprint)));
		if(bug_severity!=null) bug.setSeverity((byte)(Integer.parseInt(bug_severity)));
		List<Bug> bugList=bugService.search(bug,creator,projectIdList,bug_assignto,bug_assignby);
		if(bugList!=null)
		{
			Page<Bug> page = bugService.getPage(bugList, curPage, Constant.PAGE_SIZE);
			bugManagement.addObject("page_two", page);
		}
		//test-code
//		for(Bug entity:bugList)
//		{
//			bugAssigned="empty";
//		}
		return bugManagement;
	}
	@RequestMapping(value="tobugs.do")
	public ModelAndView tobugs(HttpServletRequest request, HttpSession session)
	{
		return new ModelAndView("bug_manage");
	}
	
	@RequestMapping(value = "bugs.do" )
	public ModelAndView bugs(HttpServletRequest request, HttpSession session)
	{
		ModelAndView mav = new ModelAndView("bug_manage");
		
		String bugName = request.getParameter("bug_name");
		String projectName=request.getParameter("project_name");
		String creator=request.getParameter("creator");
		String bugStatus = request.getParameter("bug_status");
		String severityStr = request.getParameter("severity");
		Byte severity = (severityStr == null || severityStr.isEmpty()) ? null : Byte.parseByte(severityStr);
		String sprint = request.getParameter("sprint");
		String bugTag = request.getParameter("bug_tag");
		String bugAssigned=request.getParameter("bug_assigned");
		
		
		String startDateStr = request.getParameter("start_date");
		Timestamp startDate= new Timestamp(System.currentTimeMillis());
		startDate = (startDateStr == null || startDateStr.isEmpty()) ? null : Timestamp.valueOf(startDateStr+" 00:00:00");
		String endDateStr = request.getParameter("end_date");
		Timestamp endDate=new Timestamp(System.currentTimeMillis());
		endDate = (endDateStr == null || endDateStr.isEmpty()) ? null : Timestamp.valueOf(endDateStr+" 23:59:59");
		
		String pageStr = request.getParameter("page");
		Integer curPage = (pageStr == null || pageStr.isEmpty()) ? 1 : Integer.parseInt(pageStr);
		
		Bug bug = new Bug();
		bug.setName(bugName);
		bug.setBugStatus(bugStatus);
		bug.setSeverity(severity);
		bug.setSprint(sprint);
		bug.setTag(bugTag);
		User user=new User();
		user.setName(creator);
		Project project =new Project();
		project.setName(projectName);
		bug.setProject(project);
		bug.setUser(user);
		
		User userOnline = (User) session.getAttribute(Constant.ATTRIBUTE_USER);
		boolean isAdmin = Constant.ROLE_ADMIN.equals(userOnline.getRole());
		boolean isLeader = Constant.ROLE_ADMIN.equals(userOnline.getRole());
		int[] projectIDs = null;
		if (!isAdmin)
		{
			if (isLeader)
			{
				int[] a = projectService.getLeadedProjectIDs(userOnline.getUserId());
				int[] b = projectService.getProjectIDs(userOnline);
				projectIDs = new int[a.length + b.length];
				System.arraycopy(a, 0, projectIDs, 0, a.length);
				System.arraycopy(b, 0, projectIDs, a.length, b.length);
			}
			else
			{
				projectIDs = projectService.getProjectIDs(userOnline);
			}
		}
		Page<Bug> page = bugService.searchByExample(bug, 
											bugAssigned,
											startDate, 
											endDate, 
											curPage, 
											Constant.PAGE_SIZE, 
											isAdmin, 
											projectIDs);
		
		mav.addObject("page", page);
//		if(bugAssigned.isEmpty())
//		{
//			bugAssigned="empty";
//		}
//		mav.addObject("bugAssigned",bugAssigned);
		return mav;
	}
	
	
	@RequestMapping(value = "bug-detail.do")
	public ModelAndView detail(HttpServletRequest request)
	{
		String idStr = request.getParameter("id");
		String referIdStr = request.getParameter("refer_id");
		String curPageStr = request.getParameter("page");
		Integer id = (idStr == null || idStr.isEmpty()) ? null : Integer.parseInt(idStr);
		Integer curPage = (curPageStr == null || curPageStr.isEmpty()) ? 1 : Integer.parseInt(curPageStr);
		ModelAndView mav = new ModelAndView("bug_detail");
		
		if (id == null)
		{
			return null;
		}
		
		if (referIdStr != null && !referIdStr.isEmpty())
		{
			int referId = Integer.parseInt(referIdStr);
			mav.addObject("refer_comment", commentService.findById(referId));
		}
		
		Bug bug = bugService.findById(id);
		mav.addObject("bug", bug);
		
		Page<Comment> page = commentService.findByBugId(id, curPage, Constant.PAGE_SIZE);
		if (page != null)
		{
			mav.addObject("page", page);
		}
		
		return mav;
	}
	
	@RequestMapping(value = "comment.do")
	public ModelAndView makeComment(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException
	{
		String bugIdStr = request.getParameter("id");
		String content = request.getParameter("content");
		String referIdStr = request.getParameter("refer_id");
		if (bugIdStr == null || bugIdStr.isEmpty())
		{
			return null;
		}
		
		Comment comment = new Comment();
		Bug bug = new Bug();
		bug.setBugId(Integer.parseInt(bugIdStr));
		comment.setBug(bug);
		if (referIdStr != null && !referIdStr.isEmpty())
		{
			Comment referComment = new Comment();
			referComment.setCommentId(Integer.parseInt(referIdStr));
			comment.setReferComment(referComment);
		}
		comment.setPubTime(new Timestamp(System.currentTimeMillis()));
		comment.setContent(content);
		comment.setUser((User) session.getAttribute("online_user"));
		
		commentService.save(comment);
		
		response.sendRedirect("bug-detail.do?id=" + bugIdStr);
		return null;
	}
	
	@RequestMapping(value = "add-bug.do", method = RequestMethod.GET)
	public ModelAndView addBugPage(HttpSession session)
	{
		User user = (User) session.getAttribute("online_user");
		int[] projectIDs = projectService.getProjectIDs(user);
		
		ModelAndView mav = new ModelAndView("addbug");
		mav.addObject("projectIDs", projectIDs);
		return mav;
	}
	
	@RequestMapping(value = "add-bug.do", method = RequestMethod.POST)
	public ModelAndView addBug(HttpServletRequest request, HttpSession session) throws UnsupportedEncodingException
	{
		String name = request.getParameter("name");
		String projectIdStr = request.getParameter("project_id");
		String severityStr = request.getParameter("severity");
		String description = request.getParameter("description");
		String tag = request.getParameter("tag");
		
		Integer projectId = (projectIdStr == null || projectIdStr.isEmpty()) ? null : Integer.parseInt(projectIdStr);
		Byte severity = (severityStr == null || severityStr.isEmpty()) ? null : Byte.parseByte(severityStr);
		
		ModelAndView mav = addBugPage(session);
		
		if (name != null && projectId != null && severity != null && description != null)
		{
			Project project = projectService.findProjectById(projectId);
			Bug bug = new Bug();
			bug.setProject(project);
			bug.setUser((User) session.getAttribute("online_user"));
			bug.setName(name);
			bug.setDescription(description);
			bug.setBugStatus(Constant.BUG_INIT);
//			bug.setSprint(project.getSprint());
			bug.setTag(tag);
			bug.setSeverity(severity);
			bug.setCreateDate(new Timestamp(System.currentTimeMillis()));
			if (bugService.save(bug))
			{
				mav.addObject("success", true);
			}
			else
			{
				mav.addObject("failed", true);
			}
		}
		else 
		{
			mav.addObject("failed", true);
		}
		
		return mav;
	}
	
}
