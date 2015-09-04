package com.my.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthorityFilter implements Filter
{

	public void init(FilterConfig filterConfig) throws ServletException
	{
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
																								throws IOException,
																								ServletException
	{
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		HttpSession session = servletRequest.getSession();
		String[] uri = servletRequest.getRequestURI().split("/");
		if (uri.length > 2)
		{
			String mapping = uri[2];
						
			if (mapping.equals("user-audit.do") || mapping.equals("user-manage.do"))
			{
				if (session.getAttribute("user_manage") == null)
				{
					servletResponse.sendRedirect("login.do");
					return;
				}
				
			}
			else if (mapping.equals("bug-add.do"))
			{
				if (session.getAttribute("bug_manage_add") == null)
				{
					servletResponse.sendRedirect("login.do");
					return;
				}
			}
		}
		
		chain.doFilter(request, response);
	}

	public void destroy()
	{
	}

}
