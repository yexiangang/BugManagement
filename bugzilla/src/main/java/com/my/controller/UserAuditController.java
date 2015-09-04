package com.my.controller;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.my.domain.User;
import com.my.service.UserService;
import com.my.util.Constant;
import com.my.util.Page;
import com.my.util.StringParseUtil;

@Controller
public class UserAuditController
{

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/user-audit.do")
	public ModelAndView list(HttpServletRequest request, HttpSession session)
	{
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String startTimeStr = request.getParameter("start_time");
		String endTimeStr = request.getParameter("end_time");
		if (startTimeStr != null) startTimeStr += " 00:00:00";
		if (endTimeStr != null) endTimeStr += " 00:00:00";
		String curPageStr = request.getParameter("page");
		
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		Timestamp startTime = StringParseUtil.parse2Timestamp(startTimeStr, null);
		Timestamp endTime = StringParseUtil.parse2Timestamp(endTimeStr, null);
		Integer curPage = StringParseUtil.parse2Integer(curPageStr, 1);
		Page<User> page = userService.searchUser(user, startTime, endTime, curPage, Constant.PAGE_SIZE, true);
		
		ModelAndView mav = new ModelAndView("user_audit");
		mav.addObject("page", page);
		return mav;
	}

	@RequestMapping(value = "/user-audit.do", params = "action=unpass")
	public ModelAndView deleteAndList(HttpServletRequest request, HttpSession session)
	{
		String userId = request.getParameter("userId");
		Integer id = Integer.parseInt(userId);
		if (userService.isUserUnderAuditing(id))
		{
			if (!userService.deleteUserByID(id))
			{
				ModelAndView mav = list(request, session);
				mav.addObject("unpass_failed", true);
			}
		}

		ModelAndView mav = list(request, session);
		mav.addObject("unpass_succuss", true);
		return mav;
	}

	@RequestMapping(value = "/user-audit.do", params = "action=pass")
	public ModelAndView passAndList(HttpServletRequest request, HttpSession session)
	{
		String userId = request.getParameter("userId");
		Integer id = StringParseUtil.parse2Integer(userId, -1);
		if (userService.isUserUnderAuditing(id))
		{
			String role = request.getParameter("role");
			if (Constant.ROLE_ADMIN.equals(role) || Constant.ROLE_TEAM_LEADER.equals(role) || Constant.ROLE_QA.equals(role)
					|| Constant.ROLE_DEVELOPER.equals(role))
			{
				userService.changeRole(id, role);
				ModelAndView mav = list(request, session);
				mav.addObject("pass_success", true);
				return mav;
			}
		}
		ModelAndView mav = list(request, session);
		mav.addObject("pass_failed", true);
		return mav;
	}

}
