package com.my.dao;

import java.util.List;

import com.my.domain.User;

public interface UserDao extends CommonDao<User>
{
	public  List<User>  findUserByName(String user_name);
}
