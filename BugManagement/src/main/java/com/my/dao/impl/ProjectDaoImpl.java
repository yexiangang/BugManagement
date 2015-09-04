package com.my.dao.impl;


import java.util.List;

import com.my.dao.ProjectDao;
import com.my.domain.Project;

public class ProjectDaoImpl extends CommonDaoImpl<Project> implements ProjectDao {
	
	
	public List<Project> findProjectByName(String project_name)
	{
		if(project_name==null)
		{
			System.out.println("(ProjectDaoImpl)project_name is null");
			return null;
		}
		String hql="FROM Project project  WHERE project.name LIKE ? ";
		project_name="%"+project_name+"%";
		List<Project> projectList = this.findByHql(hql, project_name);
		if(projectList==null||projectList.isEmpty()) 
		{
			System.out.println("ProjectDaoImpl_Cannot find Project");
			return null;
		}
		return projectList;
		
	}

}
