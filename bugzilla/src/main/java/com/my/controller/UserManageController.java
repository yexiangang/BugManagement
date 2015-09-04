package com.my.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.my.domain.User;
import com.my.service.UserService;
import com.my.util.Constant;
import com.my.util.Page;
import com.my.util.StringParseUtil;

@Controller
public class UserManageController
{

	@Autowired
	private UserService userService;

	@RequestMapping(value = "user-manage.do")
	public ModelAndView manage(HttpServletRequest request, HttpSession session)
	{

		String name = request.getParameter("name");
		String curPageStr = request.getParameter("page");

		User user = new User();
		user.setName(name);
		Integer curPage = StringParseUtil.parse2Integer(curPageStr, 1);
		Page<User> page = userService.searchUser(user, null, null, curPage, Constant.PAGE_SIZE, false);

		ModelAndView mav = new ModelAndView("user_manage");
		mav.addObject("page", page);
		return mav;
	}

	@RequestMapping(value = "user-manage.do", params = "action=delete")
	public ModelAndView delete(HttpServletRequest request, HttpSession session)
	{
		String userId = request.getParameter("userId");
		Integer id = StringParseUtil.parse2Integer(userId, -1);
		User user = userService.findById(id);
		
		if (user != null && user.getRole() != null && !Constant.ROLE_ADMIN.equals(user.getRole()))
		{
			boolean success = userService.deleteUserByID(id); 
			String info = success ? "delete_success" : "delete_failed";
			ModelAndView mav = manage(request, session);
			mav.addObject(info, true);
			return mav;
		}

		ModelAndView mav = manage(request, session);
		mav.addObject("bad_request", true);
		return mav;
	}

	@RequestMapping(value = "user-manage.do", params = "action=modify")
	public ModelAndView modify(HttpServletRequest request, HttpSession session)
	{
		String userId = request.getParameter("userId");
		Integer id = StringParseUtil.parse2Integer(userId, -1);
		User user = userService.findById(id);
		if (user != null && user.getRole() != null && !Constant.ROLE_ADMIN.equals(user.getRole()))
		{
			String role = request.getParameter("role");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			user.setRole(role);
			user.setEmail(email);
			if (password != null && !password.isEmpty())
			{
				user.setPassword(password);
			}
			if (user.isRegistrable())
			{
				userService.update(user);
				ModelAndView mav = manage(request, session);
				mav.addObject("modify_success", true);
				return mav;
			}
		}

		ModelAndView mav = manage(request, session);
		mav.addObject("modify_failed", true);
		return mav;
	}

	@RequestMapping(value = "user-manage.do", params = "action=add", method = RequestMethod.GET)
	public ModelAndView showAdd()
	{
		return new ModelAndView("user_add");
	}
	
	@RequestMapping(value = "user-manage.do", params = "action=add", method = RequestMethod.POST)
	public ModelAndView add(HttpServletRequest request)
	{
		ModelAndView mav = new ModelAndView("user_add");

		User user = new User();
		try
		{
			BeanUtils.populate(user, request.getParameterMap());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return mav;
		}
		
		user.setUserId(null);
		user.setRegisterTime(new Timestamp(System.currentTimeMillis()));
		if (!user.isRegistrable())
		{
			mav.addObject("register_error", true);
		}
		else if (userService.isNameRegistered(user.getName()))
		{
			mav.addObject("name_registered", true);
		}
		else if (userService.isEmailRegistered(user.getEmail()))
		{
			mav.addObject("email_registered", true);
		}
		else if (Constant.ROLE_ADMIN.equals(user.getRole()))
		{
			mav.addObject("role_error", true);
		}
		else
		{
			if (userService.register(user))
			{
				mav.addObject("success", true);
			}
		}
		
		return mav;
	}
	
	@RequestMapping(value = "user-manage.do", params = "action=detail")
	public void user(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException
	{
		Integer id = StringParseUtil.parse2Integer(request.getParameter("userId"), null);
		User user = userService.findById(id);
		if (user != null)
		{
			JSONObject map = new JSONObject();
			map.put("userId", user.getUserId());
			map.put("name", user.getName());
			map.put("registerTime", user.getRegisterTime());
			map.put("email", user.getEmail());
			map.put("role", user.getRole());
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(map.toString());
		}
	}

}
