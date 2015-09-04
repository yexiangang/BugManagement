package com.my.service.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.my.dao.BugDao;
import com.my.dao.MemberDao;
import com.my.dao.ProjectDao;
import com.my.dao.UserDao;
import com.my.domain.Bug;
import com.my.domain.Member;
import com.my.domain.MemberId;
import com.my.domain.Project;
import com.my.domain.User;
import com.my.exception.UserNotFoundException;
import com.my.service.ProjectService;
import com.my.util.Constant;
import com.my.util.Page;

public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectDao projectDao;
	
	@Autowired
	private MemberDao memberDao;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private BugDao bugDao;

	public boolean save(Project project) {
		// TODO Auto-generated method stub
		if(project.getName() == "" || project.getName() == null || project.getDescription() == "" || project.getDescription() == null){
			return false;
		}
		
		projectDao.save(project);

		return true;
	}

	public User getUserByName(String name) {
		// TODO Auto-generated method stub
		User creator = new User();
		creator.setName(name);

		List<User> users = userDao.findByExample(creator);

		if (users.size() == 0) {
			throw new UserNotFoundException("The creator not found!");
		} else {
			return users.get(0);
		}

	}

	public List<Project> findByExample(Project example) {
		// TODO Auto-generated method stub
		return projectDao.findByExample(example);
	}

	public List<Project> findByCurrentUserName(String userName) {
		List<Project> projects = new ArrayList<Project>();

		User currentUser = getUserByName(userName);
		String sql;
		if(currentUser.getRole().equals(Constant.ROLE_ADMIN)){
			sql = "select * from project order by create_date desc";
		}else if (currentUser.getRole().equals(Constant.ROLE_TEAM_LEADER)) {
			sql = "select * from project where leader=" + currentUser.getUserId() + " order by create_date desc";
		}else {
			return projects;
		}
		
		List<Map<String, Object>> list = projectDao.query(sql);
		
		for(Map<String, Object> map : list){
			Project project = new Project();
			project.setProjectId((Integer) map.get("project_id"));
			project.setUserByCreator(userDao.findById(User.class, (Integer) map.get("creator")));
			project.setUserByLeader(userDao.findById(User.class, (Integer) map.get("leader")));
			project.setName((String) map.get("name"));
			project.setDescription((String) map.get("description"));
			project.setProjectStatus((String) map.get("project_status"));
			project.setSprint((String) map.get("sprint"));
			project.setCreateDate((Date) map.get("create_date"));
			project.setMembers(findMembers(project));
			project.setBugs(findBugs(project));
			
			projects.add(project);
		}
		
		

		return projects;
	}

	public List<Project> findByCurrentUserName(String userName, String projectName) {

		List<Project> projects = new ArrayList<Project>();

		User currentUser = getUserByName(userName);
		String sql;
		if(currentUser.getRole().equals(Constant.ROLE_ADMIN)){
			sql = "select * from project where name like '%" + projectName +"%'  order by create_date desc";
		}else if (currentUser.getRole().equals(Constant.ROLE_TEAM_LEADER)) {
			sql = "select * from project where leader = " + currentUser.getUserId() + "  AND  name like '%" + projectName +"%'  order by create_date desc";
		}else {
			return projects;
		}
		
		List<Map<String, Object>> list = projectDao.query(sql);
		
		for(Map<String, Object> map : list){
			Project project = new Project();
			project.setProjectId((Integer) map.get("project_id"));
			project.setUserByCreator(userDao.findById(User.class, (Integer) map.get("creator")));
			project.setUserByLeader(userDao.findById(User.class, (Integer) map.get("leader")));
			project.setName((String) map.get("name"));
			project.setDescription((String) map.get("description"));
			project.setProjectStatus((String) map.get("project_status"));
			project.setSprint((String) map.get("sprint"));
			project.setCreateDate((Date) map.get("create_date"));
			project.setMembers(findMembers(project));
			project.setBugs(findBugs(project));
			
			projects.add(project);
		}

		return projects;
	}

	public Page<Project> getPage(String userName, int curPage) {
		Page<Project> page = new Page<Project>(curPage, Constant.PAGE_SIZE, findByCurrentUserName(userName));

		return page;
	}

	public Page<Project> getPage(String userName, String projectName, int curPage) {
		Page<Project> page;

		if (projectName == "" || projectName == null ) {

			page = new Page<Project>(curPage, Constant.PAGE_SIZE, findByCurrentUserName(userName));

		} else {

			page = new Page<Project>(curPage, Constant.PAGE_SIZE, findByCurrentUserName(userName, projectName));
		}

		return page;
	}

	public boolean delete(Project project) {
		// TODO Auto-generated method stub
		projectDao.delete(project);
		return true;
	}

	public Project findProjectById(int id) {
		// TODO Auto-generated method stub

		return projectDao.findById(Project.class, id);
	}

	public boolean deleteProjectById(int id) {
		Project project = findProjectById(id);
		if (project.getProjectStatus().equals(Constant.PROJECT_STATUS_CREATED)) {
			return delete(project);
		} else {
			return false;
		}

	}

	public List<User> getLeader(String name) {
		
		List<User> users = new ArrayList<User>();
		if(name == null){
			return users;
		}
		
		User user = getUserByName(name);
		
		
		if (user.getRole().equals(Constant.ROLE_ADMIN)) {
			User example = new User();
			example.setRole(Constant.ROLE_TEAM_LEADER);
			users = userDao.findByExample(example);
		} else if (user.getRole().equals(Constant.ROLE_TEAM_LEADER)) {
			users.add(user);
		} else {

		}
		
		return users;
	}

	public int[] getProjectIDs(User user)
	{
		if (Constant.ROLE_ADMIN.equals(user.getRole()))
		{
			List<Map<String, Object>> list = projectDao.query("SELECT project_id FROM project");
			int[] projectIDs = new int[list.size()];
			for (int i = 0; i < list.size(); ++i)
			{
				projectIDs[i] = (Integer) list.get(i).get("project_id");
			}
			return projectIDs;
		}
		else
		{
			List<Map<String, Object>> list = projectDao.query("SELECT project_id FROM member WHERE user_id = " + user.getUserId());
			List<Map<String, Object>> list2 = projectDao.query("SELECT project_id FROM project WHERE leader = " + user.getUserId());
			
			int[] projectIDs = new int[list.size() + list2.size()];
			int j = 0;
			for (int i = 0; i < list.size(); ++i)
			{
				projectIDs[j++] = (Integer) list.get(i).get("project_id");
			}
			for (int i = 0; i < list2.size(); ++i)
			{
				projectIDs[j++] = (Integer) list2.get(i).get("project_id");
			}
			return projectIDs;
		}
	}

	
	public int[] getLeadedProjectIDs(int leaderID)
	{
		List<Map<String,Object>> list = projectDao.query("SELECT * FROM project WHERE leader = " + leaderID);
		int[] projectIDs = new int[list.size()];
		for (int i = 0; i < list.size(); i++)
		{
			projectIDs[i] = (Integer) list.get(i).get("project_id");
		}
		return projectIDs;
	}

	public boolean addMember(Member member) {
		
		memberDao.save(member);
		
		return true;
	}

	public boolean update(Project project) {
		projectDao.update(project);
		return true;
	}

	public boolean updateProjectStatus(Project project, String status) {
		
		project.setProjectStatus(status);
		
		return update(project);
		
	}

	public boolean updateProjectStatusWithId(int projectId, String status) {
		String sql = "update project set project_status = '" + status + "' where project_id = " + projectId;
		
		projectDao.execute(sql);
		
		return true;
		
	}

	public boolean addMember(String memberString, int projectId) {
		String[] members = memberString.split(",");
		
		for(String member : members){
			User u = getUserByName(member.trim());
			if(u != null && !u.getRole().equals(Constant.ROLE_ADMIN) && !u.getRole().equals(Constant.ROLE_TEAM_LEADER)){
				String sql = "insert into member values(" + u.getUserId() + "," + projectId + ",now())";
				try {
					projectDao.execute(sql);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				return false;
			}
		}
		
		
		return true;
	}

	public List<Map<String, Object>> findMembersName(int projectId) {
		String sql = "select name,role from user,member where user.user_id=member.user_id AND project_id=" + projectId;
		List<Map<String, Object>> members = projectDao.query(sql);
		
		
		return members;
	}

	public String findQAs(int projectId) {
		
		List<Map<String, Object>> members = findMembersName(projectId);
		
		StringBuilder QAs = new StringBuilder();
		
		for(Map<String, Object> member : members){
			if(member.get("role").equals(Constant.ROLE_QA)){
				QAs.append((String) member.get("name") + ",");
			}
		}
		QAs.deleteCharAt(QAs.length() - 1);
		return QAs.toString();
	}

	public String findDevelopers(int projectId) {
		
		List<Map<String, Object>> members = findMembersName(projectId);
		
		StringBuilder QAs = new StringBuilder();
		
		for(Map<String, Object> member : members){
			if(member.get("role").equals(Constant.ROLE_DEVELOPER)){
				QAs.append((String) member.get("name") + ",");
			}
		}
		QAs.deleteCharAt(QAs.length() - 1);
		return QAs.toString();
	}

	public boolean update(int projectId, String projectName, User leader) {
		
		if(projectName == "" || projectName == null || leader == null){
			return false;
		}
		
		String sql = "update project set name='" + projectName + "',leader=" + leader.getUserId() + " where project_id = " + projectId;
		
		projectDao.execute(sql);
		
		return true;
	}

	public Set<Member> findMembers(Project project) {
		
		String sql = "select * from member where project_id=" + project.getProjectId();
		
		List<Map<String, Object>> list = projectDao.query(sql);
		
		Set<Member> members = new HashSet<Member>();
		
		for(Map<String, Object> map : list) {
			MemberId memberId = new MemberId();
			int userId = (Integer) map.get("user_id");
			memberId.setProjectId(project.getProjectId());
			memberId.setUserId(userId);
			Member member = new Member();
			member.setId(memberId);
			member.setJoinTime((Date) map.get("join_time"));
			member.setProject(project);
			member.setUser(userDao.findById(User.class, userId));
			members.add(member);
		}
		
		return members;
	}

	public Set<Bug> findBugs(Project project) {
		
		Bug example = new Bug();
		
		example.setProject(project);
		
		List<Bug> bList = bugDao.findByExample(example);
		
		Set<Bug> bugs = new HashSet<Bug>(bList);
		
		return bugs;
	}

	public List<User> findAllQSAndDeveloper() {
		User example = new User();
		List<User> users = new ArrayList<User>();
		example.setRole(Constant.ROLE_QA);
		users.addAll(userDao.findByExample(example));
		example.setRole(Constant.ROLE_DEVELOPER);
		users.addAll(userDao.findByExample(example));
		
		return users;
	}

	public boolean addMember(List<User> users, Project project) {
		
		List<Integer> choosedUserIds = new ArrayList<Integer>();
		for(User user : users){
			choosedUserIds.add(user.getUserId());
		}
		
		List<Integer> alreadyExists = new ArrayList<Integer>();
		String sql = "select user_id from member where project_id = " + project.getProjectId();
		List<Map<String, Object>> list = projectDao.query(sql);
		for(Map<String, Object> map : list){
			alreadyExists.add((Integer) map.get("user_id"));
		}
		
		List<Integer> unchange = new ArrayList<Integer>();
		
		for(Integer i : choosedUserIds){
			if(alreadyExists.contains(i)){
				unchange.add(i);
			}
		}
		
		choosedUserIds.removeAll(unchange);
		alreadyExists.removeAll(unchange);
		
		for(Integer i : choosedUserIds){
			sql = "insert into member values(" + i + "," + project.getProjectId() + ",now())";
			try {
				projectDao.execute(sql);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(Integer i : alreadyExists){
			sql = "delete from member where project_id = " + project.getProjectId() + " AND user_id = " + i;
			try {
				projectDao.execute(sql);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return true;
	}

	public boolean deleteMember(Project project) {
		if(project == null){
			return false;
		}
		
		String sql = "delete from member where project_id = " + project.getProjectId();
		try {
			projectDao.execute(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

	public boolean addMemberAtDeveloping(String memberString, int projectId) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addMemberAtDeveloping(List<User> users, Project project) {
		int projectId = project.getProjectId();
		String sql;
		
		for(User user : users){
			sql = "insert into member values(" + user.getUserId() + "," + projectId + ",now())";
			if(user != null && !user.getRole().equals(Constant.ROLE_ADMIN) && !user.getRole().equals(Constant.ROLE_TEAM_LEADER)){
				try {
					projectDao.execute(sql);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				return false;
			}
		}
		return true;
	}

	public boolean isNameExists(String name)
	{
		List<Map<String,Object>> list = projectDao.query("SELECT * FROM project WHERE name = ?", new Object[] {name}, new int[] {Types.VARCHAR});
		return !list.isEmpty();
	}

}
