package com.my.dao.impl;

import java.util.Iterator;
import java.util.List;

import com.my.dao.UserDao;
import com.my.domain.User;

public class UserDaoImpl extends CommonDaoImpl<User>implements UserDao {

	public  List<User>  findUserByName(String user_name)
	{
		if(user_name==null)
		{
			System.out.println("(UserDaoImpl)user_name is null");
			return null;
		}
		String hql="FROM User user WHERE user.name LIKE ?";
		String find_name="%"+user_name+"%";
		List<User> userList=this.findByHql(hql, find_name);
		if(!userList.isEmpty() && userList!=null)
		{
			return userList;
			
		}
		else
		{
			return null;
		}
	
	}
}
