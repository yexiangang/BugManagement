package com.my.domain;
// Generated Aug 25, 2015 11:15:05 AM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.my.util.Constant;

/**
 * User generated by hbm2java
 */
public class User implements java.io.Serializable {

	private Integer userId;
	private String name;
	private String password;
	private String email;
	private String role;
	private Date registerTime;
	private Set assignmentsForAssignedUserId = new HashSet(0);
	private Set members = new HashSet(0);
	private Set projectsForLeader = new HashSet(0);
	private Set bugs = new HashSet(0);
	private Set assignmentsForOperateUserId = new HashSet(0);
	private Set comments = new HashSet(0);
	private Set projectsForCreator = new HashSet(0);

	public User() {
	}

	public User(String name, String password, String email, Date registerTime) {
		this.name = name;
		this.password = password;
		this.email = email;
		this.registerTime = registerTime;
	}

	public User(String name, String password, String email, String role, Date registerTime,
			Set assignmentsForAssignedUserId, Set members, Set projectsForLeader, Set bugs,
			Set assignmentsForOperateUserId, Set comments, Set projectsForCreator) {
		this.name = name;
		this.password = password;
		this.email = email;
		this.role = role;
		this.registerTime = registerTime;
		this.assignmentsForAssignedUserId = assignmentsForAssignedUserId;
		this.members = members;
		this.projectsForLeader = projectsForLeader;
		this.bugs = bugs;
		this.assignmentsForOperateUserId = assignmentsForOperateUserId;
		this.comments = comments;
		this.projectsForCreator = projectsForCreator;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Date getRegisterTime() {
		return this.registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Set getAssignmentsForAssignedUserId() {
		return this.assignmentsForAssignedUserId;
	}

	public void setAssignmentsForAssignedUserId(Set assignmentsForAssignedUserId) {
		this.assignmentsForAssignedUserId = assignmentsForAssignedUserId;
	}

	public Set getMembers() {
		return this.members;
	}

	public void setMembers(Set members) {
		this.members = members;
	}

	public Set getProjectsForLeader() {
		return this.projectsForLeader;
	}

	public void setProjectsForLeader(Set projectsForLeader) {
		this.projectsForLeader = projectsForLeader;
	}

	public Set getBugs() {
		return this.bugs;
	}

	public void setBugs(Set bugs) {
		this.bugs = bugs;
	}

	public Set getAssignmentsForOperateUserId() {
		return this.assignmentsForOperateUserId;
	}

	public void setAssignmentsForOperateUserId(Set assignmentsForOperateUserId) {
		this.assignmentsForOperateUserId = assignmentsForOperateUserId;
	}

	public Set getComments() {
		return this.comments;
	}

	public void setComments(Set comments) {
		this.comments = comments;
	}

	public Set getProjectsForCreator() {
		return this.projectsForCreator;
	}

	public void setProjectsForCreator(Set projectsForCreator) {
		this.projectsForCreator = projectsForCreator;
	}

	public boolean isRegistrable()
	{
		return name != null && name.length() >= 6 && name.length() <= 15 && password != null && password.length() >= 6
				&& password.length() <= 15 && email != null
				&& email.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")
				&& (role == null || (role.equals(Constant.ROLE_DEVELOPER) || role.equals(Constant.ROLE_QA)
						|| role.equals(Constant.ROLE_TEAM_LEADER) || role.equals(Constant.ROLE_ADMIN)) && registerTime != null);
	}
	
	@Override
	public int hashCode()
	{
		return (userId == null) ? super.hashCode() : Integer.hashCode(userId);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof User)
		{
			User user = (User) obj;
			if (user.getUserId() != null && this.userId != null)
			{
				return user.getUserId().equals(this.userId);
			}
			if (user.getName() != null && this.name != null)
			{
				return user.getName().equals(this.name);
			}
		}
		return false;
	}
}