package com.my.domain;
// Generated Aug 25, 2015 11:15:05 AM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Bug generated by hbm2java
 */
public class Bug implements java.io.Serializable {

	private Integer bugId;
	private Project project;
	private User user;
	private String name;
	private String description;
	private String bugStatus;
	private String sprint;
	private String tag;
	private Byte severity;
	private Date createDate;
	private Set comments = new HashSet<Comment>(0);
	private Set assignments = new HashSet<Assignment>(0);
	private Set files = new HashSet<UploadFile>(0);

	public Bug() {
	}

	public Bug(Project project, User user, String name, String description, String bugStatus, String sprint,
			Byte severity, Date createDate) {
		this.project = project;
		this.user = user;
		this.name = name;
		this.description = description;
		this.bugStatus = bugStatus;
		this.sprint = sprint;
		this.severity = severity;
		this.createDate = createDate;
	}

	public Bug(Project project, User user, String name, String description, String bugStatus, String sprint, String tag,
			Byte severity, Date createDate, Set<Comment> comments, Set<Assignment> assignments, Set<UploadFile> files) {
		this.project = project;
		this.user = user;
		this.name = name;
		this.description = description;
		this.bugStatus = bugStatus;
		this.sprint = sprint;
		this.tag = tag;
		this.severity = severity;
		this.createDate = createDate;
		this.comments = comments;
		this.assignments = assignments;
		this.files = files;
	}

	public Integer getBugId() {
		return this.bugId;
	}

	public void setBugId(Integer bugId) {
		this.bugId = bugId;
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

	public String getBugStatus() {
		return this.bugStatus;
	}

	public void setBugStatus(String bugStatus) {
		this.bugStatus = bugStatus;
	}

	public String getSprint() {
		return this.sprint;
	}

	public void setSprint(String sprint) {
		this.sprint = sprint;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Byte getSeverity() {
		return this.severity;
	}

	public void setSeverity(Byte severity) {
		this.severity = severity;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Set<Comment> getComments() {
		return this.comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Set<Assignment> getAssignments() {
		return this.assignments;
	}

	public void setAssignments(Set<Assignment> assignments) {
		this.assignments = assignments;
	}

	public Set<UploadFile> getFiles() {
		return this.files;
	}

	public void setFiles(Set<UploadFile> files) {
		this.files = files;
	}

}
