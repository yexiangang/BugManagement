package com.my.service;

public interface FileSuffixService
{
	public static final int	TYPE_IMAGE		= 0;
	public static final int	TYPE_ARCHIVE	= 1;
	public static final int	TYPE_NONE		= 2;
	
	public int getFileType(String fileName);
}
