package com.my.dao.impl;

import com.my.dao.CommentDao;
import com.my.domain.Comment;

public class CommentDaoImpl extends CommonDaoImpl<Comment> implements CommentDao{

	public Comment findById(Integer id)
	{
		return findById(Comment.class, id);
	}

}
