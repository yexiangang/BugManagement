package com.my.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.my.service.FileSuffixService;
import com.my.util.Constant;

public class FileSuffixServiceImpl implements FileSuffixService
{

	private Properties properties;

	public FileSuffixServiceImpl()
	{
		try
		{
			properties = PropertiesLoaderUtils
					.loadProperties(new ClassPathResource(Constant.PROPERTIES_FILE_SUFFIX));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public int getFileType(String fileName)
	{
		String[] imgSuffix = properties.getProperty("image.suffix").split(";");
		String[] archSuffix = properties.getProperty("archive.suffix").split(";");

		List<String> list = null;
		String suffix = fileName.substring(fileName.lastIndexOf('.') + 1);
		if ((list = Arrays.asList(imgSuffix)) != null && list.contains(suffix))
		{
			return TYPE_IMAGE;
		}
		if ((list = Arrays.asList(archSuffix)) != null && list.contains(suffix))
		{
			return TYPE_ARCHIVE;
		}
		return TYPE_NONE;
	}
	

}
