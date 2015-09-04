package com.my.domain;
// Generated Aug 25, 2015 11:15:05 AM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Project generated by hbm2java
 */
public class Project implements java.io.Serializable {

	private Integer projectId;
	private User userByLeader;
	private User userByCreator;
	private String name;
	private String description;
	private String projectStatus;
	private String sprint;
	private Date createDate;
	private Set members = new HashSet(0);
	private Set bugs = new HashSet(0);

	public Project() {
	}

	public Project(User userByLeader, User userByCreator, String name, String description, String projectStatus,
			String sprint, Date createDate) {
		this.userByLeader = userByLeader;
		this.userByCreator = userByCreator;
		this.name = name;
		this.description = description;
		this.projectStatus = projectStatus;
		this.sprint = sprint;
		this.createDate = createDate;
	}

	public Project(User userByLeader, User userByCreator, String name, String description, String projectStatus,
			String sprint, Date createDate, Set members, Set bugs) {
		this.userByLeader = userByLeader;
		this.userByCreator = userByCreator;
		this.name = name;
		this.description = description;
		this.projectStatus = projectStatus;
		this.sprint = sprint;
		this.createDate = createDate;
		this.members = members;
		this.bugs = bugs;
	}

	public Integer getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public User getUserByLeader() {
		return this.userByLeader;
	}

	public void setUserByLeader(User userByLeader) {
		this.userByLeader = userByLeader;
	}

	public User getUserByCreator() {
		return this.userByCreator;
	}

	public void setUserByCreator(User userByCreator) {
		this.userByCreator = userByCreator;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProjectStatus() {
		return this.projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public String getSprint() {
		return this.sprint;
	}

	public void setSprint(String sprint) {
		this.sprint = sprint;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Set getMembers() {
		return this.members;
	}

	public void setMembers(Set members) {
		this.members = members;
	}

	public Set getBugs() {
		return this.bugs;
	}

	public void setBugs(Set bugs) {
		this.bugs = bugs;
	}

}
