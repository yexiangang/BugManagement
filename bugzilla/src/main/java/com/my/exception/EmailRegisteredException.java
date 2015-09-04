package com.my.exception;

public class EmailRegisteredException extends RegisterException
{
	public EmailRegisteredException()
	{
		super("The email has been registered");
	}
	
	public EmailRegisteredException(String message)
	{
		super(message);
	}
}
