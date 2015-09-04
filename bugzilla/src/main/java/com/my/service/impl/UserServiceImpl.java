package com.my.service.impl;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.my.dao.UserDao;
import com.my.domain.User;
import com.my.service.UserService;
import com.my.util.Page;

public class UserServiceImpl implements UserService
{

	@Autowired
	private UserDao userDao;

	public boolean register(User user)
	{
		userDao.save(user);
		return true;
	}

	public boolean isNameRegistered(String name)
	{
		User example = new User();
		example.setName(name);
		List<User> list = userDao.findByExample(example);
		return !list.isEmpty();
	}

	public boolean isEmailRegistered(String email)
	{
		User example = new User();
		example.setEmail(email);
		List<User> list = userDao.findByExample(example);
		return !list.isEmpty();
	}

	public boolean login(String name, String password)
	{
		User user = new User();
		user.setName(name);
		user.setPassword(password);
		List<User> list = userDao.findByExample(user);
		return !list.isEmpty();
	}

	public List<User> getAuditingUsers()
	{
		List<User> users = new ArrayList<User>();
		
		List<Map<String,Object>> list = userDao.query("select * from user where role IS NULL");
		for (Map<String, Object> entity : list)
		{
			User user = new User();
			user.setUserId((Integer) entity.get("user_id"));
			user.setName((String) entity.get("name"));
			user.setEmail((String) entity.get("email"));
			user.setRegisterTime((Timestamp) entity.get("register_time"));
			users.add(user);
		}
		return users;
	}
	
	public Integer getAuditingUserCount()
	{
		String sql = "select count(*) as count from user where role IS NULL";
		Integer count = userDao.queryForObject(sql, Integer.class);
		return count;
	}
	
	public List<User> getAuditingUsers(int start, int length)
	{
		List<User> users = new ArrayList<User>();
		
		List<Map<String,Object>> list = userDao.query("select * from user where role IS NULL limit " + start +  "," + length);
		for (Map<String, Object> entity : list)
		{
			User user = new User();
			user.setUserId((Integer) entity.get("user_id"));
			user.setName((String) entity.get("name"));
			user.setEmail((String) entity.get("email"));
			user.setRegisterTime((Timestamp) entity.get("register_time"));
			users.add(user);
		}
		return users;
	}

	public boolean deleteUser(User user)
	{
		userDao.delete(user);
		return true;
	}

	public boolean deleteUserByID(Integer id)
	{
		try
		{
			int row = userDao.update("delete from user where user_id = " + id);
			return row > 0;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public boolean isUserUnderAuditing(Integer id)
	{
		List<Map<String,Object>> list = userDao.query("select * from user where user_id = " + id + " and role IS NULL");
		return !list.isEmpty();
	}
	
	public boolean isUserUnderAuditing(String name)
	{
		User user = new User();
		user.setName(name);
		user = userDao.findByExample(user).get(0);
		return isUserUnderAuditing(user.getUserId());
	}

	public void changeRole(Integer id, String role)
	{
		User user = userDao.findById(User.class, id);
		if (user != null)
		{
			user.setRole(role);
			userDao.update(user);
		}
	}

	public List<User> findByExample(User user)
	{
		return userDao.findByExample(user);
	}


	public User findById(Integer id)
	{
		return userDao.findById(User.class, id);
	}

	public void update(User user)
	{
		userDao.update(user);
	}

	public Page<User> searchUser(
											User user,
											Timestamp startTime,
											Timestamp endTime,
											Integer curPage,
											Integer pageSize,
											boolean isAudit)
	{
		StringBuilder queryBuilder = new StringBuilder();
		List<Object> args = new ArrayList<Object>();
		int[] argTypes = new int[6];
		int count = 0;
		
		queryBuilder.append("select * from user where ");
		queryBuilder.append(isAudit ? "role IS NULL " : "role IS NOT NULL ");
		if (user.getUserId() != null)
		{
			queryBuilder.append("and user_id = ? ");
			args.add(user.getUserId());
			argTypes[count++] = Types.INTEGER;
		}
		if (user.getName() != null && !user.getName().isEmpty())
		{
			queryBuilder.append("and name LIKE ? ");
			args.add("%" + user.getName() + "%");
			argTypes[count++] = Types.VARCHAR;
		}
		if (user.getEmail() != null && !user.getEmail().isEmpty())
		{
			queryBuilder.append("and email LIKE ? ");
			args.add("%" + user.getEmail() + "%");
			argTypes[count++] = Types.VARCHAR;
		}
		if (!isAudit && user.getRole() != null && !user.getRole().isEmpty())
		{
			queryBuilder.append("and role = ? ");
			args.add(user.getRole());
			argTypes[count++] = Types.VARCHAR;
		}
		if (startTime != null)
		{
			queryBuilder.append("and register_time >= ? ");
			args.add(startTime);
			argTypes[count++] = Types.TIMESTAMP;
		}
		
		if (endTime != null)
		{
			queryBuilder.append("and register_time <= ? ");
			args.add(endTime);
			argTypes[count++] = Types.TIMESTAMP;
		}
		
		String querySql = queryBuilder.toString();
		String countSql = querySql.replaceFirst("\\*", "COUNT(*)");
		
		int[] argTypes2 = new int[count];
		System.arraycopy(argTypes, 0, argTypes2, 0, count);

		System.out.println(countSql);
		Long itemCount = (Long) userDao.query(countSql, args.toArray(), argTypes2).get(0).get("COUNT(*)");
		Page<User> page = new Page<User>(curPage, pageSize, itemCount);
		
		queryBuilder.append("ORDER BY register_time ASC LIMIT " + page.getFromIndex() + "," + page.getCurItemCount());
		querySql = queryBuilder.toString();
		System.out.println(querySql);
		List<Map<String,Object>> list = userDao.query(querySql, args.toArray(), argTypes2);
		
		List<User> users = new ArrayList<User>();
		for (Map<String, Object> entity : list)
		{
			User u = new User();
			u.setUserId((Integer) entity.get("user_id"));
			u.setName((String) entity.get("name"));
			u.setEmail((String) entity.get("email"));
			u.setRole((String) entity.get("role"));
			u.setRegisterTime((Timestamp) entity.get("register_time"));
			users.add(u);
		}
		
		page.setCurItems(users);
		return page;
	}
	
}
