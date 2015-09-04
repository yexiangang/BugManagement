package com.my.controller;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.my.domain.User;
import com.my.service.UserService;

@Controller
public class RegisterController
{

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/register.do", method = RequestMethod.POST)
	public ModelAndView register(HttpServletRequest request, HttpServletResponse response)
	{
		ModelAndView mav = new ModelAndView("register");

		String name = request.getParameter("name");
		String pwd = request.getParameter("password");
		String email = request.getParameter("email");
		
		User user = new User();
		user.setEmail(email);
		user.setName(name);
		user.setPassword(pwd);
		user.setRegisterTime(new Timestamp(System.currentTimeMillis()));

		if (!user.isRegistrable())
		{
			mav.addObject("register_error", true);
		}
		else if (userService.isNameRegistered(name))
		{
			mav.addObject("name_registered", true);
		}
		else if (userService.isEmailRegistered(email))
		{
			mav.addObject("email_registered", true);
		}
		else
		{
			if (userService.register(user))
			{
				mav.addObject("success", true);
				response.setHeader("Refresh","3;URL=" + request.getRequestURL().toString().replaceFirst("register.do", "login.do"));
			}
		}

		return mav;
	}

	@RequestMapping(value = "/register.do", method = RequestMethod.GET)
	public String registerGet(HttpServletRequest request)
	{
		return "register";
	}

}
