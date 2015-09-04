package com.my.exception;

public class RegisterException extends RuntimeException
{

	public RegisterException()
	{
		super("Register info error");
	}
	
	public RegisterException(String message)
	{
		super(message);
	}
	
}
