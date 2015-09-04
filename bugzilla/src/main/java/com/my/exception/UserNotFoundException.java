package com.my.exception;

public class UserNotFoundException extends RuntimeException
{

	public UserNotFoundException()
	{
		super("user name not found");
	}

	public UserNotFoundException(String message)
	{
		super(message);
	}
}
