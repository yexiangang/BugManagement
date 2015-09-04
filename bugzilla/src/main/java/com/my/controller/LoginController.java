package com.my.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.my.domain.User;
import com.my.service.RoleService;
import com.my.service.UserService;
import com.my.util.Constant;

@Controller
public class LoginController
{

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;

	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public String showLogin()
	{
		return "login";
	}

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request, HttpSession session)
	{
		logout(request, session);
		session = request.getSession();
		
		ModelAndView mav = new ModelAndView("login");

		String name = request.getParameter("username");
		String password = request.getParameter("password");
		User user = new User();
		user.setName(name);
		
		if (!userService.isNameRegistered(name))
		{
			mav.addObject("user_not_exist", true);
		}
		else if (!userService.login(name, password))
		{
			mav.addObject("wrong_password", true);
		}
		else if (userService.isUserUnderAuditing(name))
		{
			mav.addObject("user_under_auditing", true);
		}
		else
		{
			user = userService.findByExample(user).get(0);
			session.setAttribute(Constant.ATTRIBUTE_USER, user);
			@SuppressWarnings("unchecked")
			Map<User, HttpSession> userMap = (Map<User, HttpSession>) session.getServletContext().getAttribute(Constant.ATTRIBUTE_USER_MAP);
			userMap.put(user, session);
			
			String[] authorities = roleService.getAuthorities(user.getRole());
			for (String authority : authorities)
			{
				session.setAttribute(authority, true);
			}
			
			mav.setViewName("welcome");
		}

		return mav;
	}
	
	@RequestMapping(value="/logout.do")
	public ModelAndView logout(HttpServletRequest request, HttpSession session)
	{
		@SuppressWarnings("unchecked")
		Map<User, HttpSession> userMap = (Map<User, HttpSession>) session.getServletContext().getAttribute(Constant.ATTRIBUTE_USER_MAP);
		User user = (User) session.getAttribute(Constant.ATTRIBUTE_USER);
		if (userMap.containsKey(user))
		{
			userMap.get(user).invalidate();
			userMap.remove(user);
		}
		
		ModelAndView mav = new ModelAndView("login");
		return mav;
	}
	
}
