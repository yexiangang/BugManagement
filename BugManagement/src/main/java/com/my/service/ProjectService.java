
package com.my.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.my.domain.Bug;
import com.my.domain.Member;
import com.my.domain.Project;
import com.my.domain.User;
import com.my.util.Page;

public interface ProjectService {
	public boolean save(Project project);

	public boolean addMember(Member member);
	
	public boolean deleteMember(Project project);
	
	public boolean addMember(String memberString, int projectId);

	public boolean addMember(List<User> users, Project project);
	
	public boolean addMemberAtDeveloping(String memberString, int projectId);

	public boolean addMemberAtDeveloping(List<User> users, Project project);
	
	public boolean update(Project project);
	
	public boolean update(int projectId, String projectName, User leader);
	
	public List<Map<String, Object>> findMembersName(int projectId);
	
	public Set<Member> findMembers(Project project);
	
	public List<User> findAllQSAndDeveloper();
	
	public Set<Bug> findBugs(Project project);
	
	public String findQAs(int projectId);
	
	public String findDevelopers(int projectId);

	public boolean updateProjectStatus(Project project, String status);
	
	public boolean updateProjectStatusWithId(int projectId, String status);

	public boolean delete(Project project);

	public Project findProjectById(int id);

	public boolean deleteProjectById(int id);

	public User getUserByName(String name);

	public List<User> getLeader(String name);

	public List<Project> findByExample(Project example);

	public List<Project> findByCurrentUserName(String name);

	public List<Project> findByCurrentUserName(String userName, String projectName);

	public Page<Project> getPage(String userName, int page);

	public Page<Project> getPage(String userName, String projectName, int page);

	public int[] getProjectIDs(User user);
	
	public int[] getLeadedProjectIDs(int leaderID);
	
	public boolean isNameExists(String name);
}
