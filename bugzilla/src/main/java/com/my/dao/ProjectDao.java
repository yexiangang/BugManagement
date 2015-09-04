package com.my.dao;

import java.util.List;

import com.my.domain.Project;

public interface ProjectDao extends CommonDao<Project> {
	public List<Project> findProjectByName(String project_name);
}
