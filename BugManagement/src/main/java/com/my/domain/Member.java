package com.my.domain;
// Generated Aug 25, 2015 11:15:05 AM by Hibernate Tools 4.3.1

import java.util.Date;

/**
 * Member generated by hbm2java
 */
public class Member implements java.io.Serializable {

	private MemberId id;
	private Project project;
	private User user;
	private Date joinTime;

	public Member() {
	}

	public Member(MemberId id, Project project, User user, Date joinTime) {
		this.id = id;
		this.project = project;
		this.user = user;
		this.joinTime = joinTime;
	}

	public MemberId getId() {
		return this.id;
	}

	public void setId(MemberId id) {
		this.id = id;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getJoinTime() {
		return this.joinTime;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}

}