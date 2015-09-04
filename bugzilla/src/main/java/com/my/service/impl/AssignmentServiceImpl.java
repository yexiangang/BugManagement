package com.my.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.my.dao.AssignmentDao;
import com.my.domain.Assignment;
import com.my.service.AssignmentService;

public class AssignmentServiceImpl implements AssignmentService
{

	@Autowired
	private AssignmentDao assignmentDao;
	
	public void save(Assignment assignment)
	{
		assignmentDao.save(assignment);
	}

}
