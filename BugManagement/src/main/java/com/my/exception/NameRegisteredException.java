package com.my.exception;

public class NameRegisteredException extends RegisterException
{
	public NameRegisteredException()
	{
		super("The name has been registered");
	}
	
	public NameRegisteredException(String message)
	{
		super(message);
	}
}
