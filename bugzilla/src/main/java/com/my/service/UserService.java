package com.my.service;

import java.sql.Timestamp;
import java.util.List;

import com.my.domain.User;
import com.my.util.Page;

public interface UserService
{
	public boolean register(User user);
	
	public boolean isNameRegistered(String name);
	
	public boolean isEmailRegistered(String email);
	
	public boolean login(String name, String password);
	
	public List<User> getAuditingUsers();
	
	public List<User> getAuditingUsers(int start, int length);
	
	public Integer getAuditingUserCount();
	
	public boolean deleteUser(User user);
	
	public boolean deleteUserByID(Integer id);
	
	public boolean isUserUnderAuditing(Integer id);
	
	public boolean isUserUnderAuditing(String name);
	
	public void changeRole(Integer id, String role);
	
	public List<User> findByExample(User user);
	
	public User findById(Integer id);

	public Page<User> searchUser(User user, Timestamp startTime, Timestamp endTime, Integer curPage, Integer pageSize, boolean isAudit);
	
	public void update(User user);

}
