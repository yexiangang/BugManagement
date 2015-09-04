package com.my.listener;

import java.util.LinkedHashMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;

import com.my.domain.User;
import com.my.util.Constant;

public class MyServletContextListener implements ServletContextListener
{

	public void contextInitialized(ServletContextEvent sce)
	{
		sce.getServletContext().setAttribute(Constant.ATTRIBUTE_USER_MAP, new LinkedHashMap<User, HttpSession>());
	}

	public void contextDestroyed(ServletContextEvent sce)
	{
	}

}
