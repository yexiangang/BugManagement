package com.my.service.impl;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.my.service.RoleService;
import com.my.util.Constant;

public class RoleServiceImpl implements RoleService
{
	
	private Properties properties;
	
	public RoleServiceImpl()
	{
		try
		{
			properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(Constant.PROPERTIES_FILE_ROLE));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public String[] getAuthorities(String role)
	{
		String property = (String) properties.get(role + ".authorities");
		return property.split(";");
	}

}
