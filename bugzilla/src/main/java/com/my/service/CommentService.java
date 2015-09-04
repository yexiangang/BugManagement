package com.my.service;

import com.my.domain.Comment;
import com.my.util.Page;

public interface CommentService
{

	public Comment findById(Integer id);
	
	public void save(Comment comment);
	
	public Page<Comment> findByBugId(Integer id, Integer curPage, Integer pageSize);
}
