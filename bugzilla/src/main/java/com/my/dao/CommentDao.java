package com.my.dao;

import com.my.domain.Comment;

public interface CommentDao extends CommonDao<Comment>{

	public Comment findById(Integer id);
	
}
