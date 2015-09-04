package com.my.service;

import java.sql.Timestamp;
import java.util.List;

import com.my.domain.Bug;
import com.my.domain.User;
import com.my.util.Page;

public interface BugService {
	public List<Bug> search(Bug bug,String creator,List<Integer> projectIdList,String assignTo, String  assignBy);
	public List<Integer>  findProjectIdList(String role,int user_id,String project_name);
	public Page<Bug>  getPage(List<Bug> bugList,int curPage,int pageSize);
	public void addEndTime(int bugId);
	public Page<Bug> searchByExample(Bug bug, 
								String bugAssigned,
								Timestamp startTime, 
								Timestamp endTime, 
								int curPage, 
								int pageSize, 
								boolean isAdmin, 
								int[] projectIDs);
	public void updateStatus(int bugId ,String  new_state);
	public List<Object> show(User user);
	
	public Page<Bug> findByExample(Bug bug, int curPage, int pageSize);
	
	public Bug findById(Integer id);
	
	public boolean save(Bug bug);
	public boolean delete(Bug bug);
	
	public List<Object> findByHql(String hql,String []str);
	public void assignTo(User from, String to, String bug_status,int bugId);
	public void updata(Bug bug);
}
