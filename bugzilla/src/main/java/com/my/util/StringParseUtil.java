package com.my.util;

import java.sql.Timestamp;

public class StringParseUtil
{
	public static Integer parse2Integer(String s, Integer defalut)
	{
		try
		{
			if (s == null || s.isEmpty())
			{
				return defalut;
			}
			Integer re = Integer.parseInt(s);
			return re;
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		return defalut;
	}
	
	public static Timestamp parse2Timestamp(String s, Timestamp defalut)
	{
		try
		{
			if (s == null || s.isEmpty())
			{
				return defalut;
			}
			Timestamp re = Timestamp.valueOf(s);
			return re;
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		return defalut;
	}
	
	public static Byte parse2Byte(String s, Byte defalut)
	{
		try
		{
			if (s == null || s.isEmpty())
			{
				return defalut;
			}
			Byte re = Byte.parseByte(s);
			return re;
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		return defalut;
	}
}
