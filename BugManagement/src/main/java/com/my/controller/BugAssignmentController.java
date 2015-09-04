package com.my.controller;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.my.domain.Bug;
import com.my.domain.User;
import com.my.service.BugService;
import com.my.util.Constant;
import com.my.util.Page;

@Controller
public class BugAssignmentController {

	@Autowired 
	BugService bugService;
	
	@RequestMapping(value="assign.do" ,  method = RequestMethod.POST)
	private void ChangeBugStatus(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) 
			throws IOException 
	{
		User onlineUser=(User) session.getAttribute(Constant.ATTRIBUTE_USER);
		String userToStr=request.getParameter("userSelect");
//		System.out.println("***********Winston************");
//		System.out.println(userToStr);
//		System.out.println("***********************");
		String bugIdStr=(String)request.getParameter("bugId");
		int bugId=Integer.parseInt(bugIdStr);
		Bug bug=bugService.findById(bugId);
		String bugStatus=bug.getBugStatus();
		
		
		if(bugStatus.equals(Constant.BUG_INIT)||bugStatus.equals(Constant.BUG_DEVELOPING)||bugStatus.equals(Constant.BUG_TESTUNPASSED))
		{
			if(userToStr!=null && userToStr.isEmpty())
			{	
				response.sendRedirect("edit.do?bugId="+bugIdStr);
//				return new ModelAndView("bug_edit");
//				request.getServletContext("/")
			}
			else
			{
				if(bugStatus.equals(Constant.BUG_INIT))
				{
					bugService.updateStatus(bugId, Constant.BUG_ASSIGN);
					bugService.addEndTime(bugId);
					bugService.assignTo(onlineUser,userToStr , Constant.BUG_ASSIGN, bugId);
				}
				else if(bugStatus.equals(Constant.BUG_DEVELOPING))
				{
					bugService.updateStatus(bugId , Constant.BUG_DEVELOPED);
					bugService.addEndTime(bugId);
					bugService.assignTo(onlineUser,userToStr , Constant.BUG_DEVELOPED, bugId);
				}
				else if( bugStatus.equals(Constant.BUG_TESTUNPASSED))
				{
					bugService.updateStatus(bugId, Constant.BUG_ASSIGN);
					bugService.addEndTime(bugId);
					bugService.assignTo(onlineUser,userToStr , Constant.BUG_ASSIGN, bugId);
				}
				response.sendRedirect("mybug.do");
			}
		}
		else
		{
			if(bugStatus.equals(Constant.BUG_ASSIGN))
			{
				bugService.updateStatus(bugId, Constant.BUG_DEVELOPING);
			}
			else if(bugStatus.equals(Constant.BUG_DEVELOPED))
			{
				bugService.updateStatus(bugId, Constant.BUG_TESTING);
			}
			else if(bugStatus.equals(Constant.BUG_TESTING))
			{
				String test_result=request.getParameter("assign");
				if(test_result.equals("test_pass"))
				{
					bugService.updateStatus(bugId, Constant.BUG_TESTPASSED);
				}
				else
				{
					bugService.updateStatus(bugId, Constant.BUG_TESTUNPASSED);
				}
			}
			else if(bugStatus.equals(Constant.BUG_TESTPASSED))
			{
				bugService.updateStatus(bugId, Constant.BUG_END);
				bugService.addEndTime(bugId);
			}
			response.sendRedirect("edit.do?bugId="+bugIdStr);
//			return new ModelAndView("bug_edit");
		}
		
//		return new ModelAndView("bug_edit");	
		
	}

//		@RequestMapping(value="edited.do" , params="assign=test_pass")
//		public void TestPass(HttpServletRequest request, HttpServletResponse response, HttpSession session) 
//		throws IOException 
//		{
//			String bugIdStr=(String)  request.getParameter("bugId");
//			int bugId=Integer.parseInt(bugIdStr);
//			Bug bug=bugService.findById(bugId);
//			String bugStatus=bug.getBugStatus();
//			String projectName=bug.getProject().getName();
//			String creatorName=bug.getUser().getName();
//			bugService.updateStatus(bugId, bugStatus, Constant.BUG_TESTPASSED);
//			response.sendRedirect("edit.do?"+"bugId="+bugIdStr+"&projectName="+projectName+"&userName="+creatorName);
//		}
//		@RequestMapping(value="edited.do" , params="assign=test_unpass")
//		public void TestUNPass(HttpServletRequest request, HttpServletResponse response, HttpSession session) 
//		throws IOException 
//		{
//			String bugIdStr=(String)  request.getParameter("bugId");
//			int bugId=Integer.parseInt(bugIdStr);
//			Bug bug=bugService.findById(bugId);
//			String bugStatus=bug.getBugStatus();
//			String projectName=bug.getProject().getName();
//			String creatorName=bug.getUser().getName();
//			bugService.updateStatus(bugId, bugStatus, Constant.BUG_TESTUNPASSED);
//			response.sendRedirect("edit.do?"+"bugId="+bugIdStr+"&projectName="+projectName+"&userName="+creatorName);
//		}
}
