package com.my.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.my.dao.CommentDao;
import com.my.dao.UserDao;
import com.my.domain.Comment;
import com.my.domain.User;
import com.my.service.CommentService;
import com.my.util.Page;

public class CommentServiceImpl implements CommentService
{

	@Autowired
	private CommentDao commentDao;
	
	@Autowired
	private UserDao userDao;
	
	public Comment findById(Integer id)
	{
		return commentDao.findById(id);
	}

	public void save(Comment comment)
	{
		commentDao.save(comment);
	}

	public Page<Comment> findByBugId(Integer id, Integer curPage, Integer pageSize)
	{
		String CountQuery = "SELECT COUNT(*) FROM comment WHERE bug_id = " + id;
		StringBuilder resultQueryBuilder = new StringBuilder("SELECT * FROM comment WHERE bug_id = ");
		resultQueryBuilder.append(id);
		resultQueryBuilder.append(" ORDER BY pub_time DESC");
		
		Long count = (Long) commentDao.query(CountQuery).get(0).get("COUNT(*)");
		if (count == 0)
		{
			return null;
		}
		Page<Comment> page = new Page<Comment>(curPage, pageSize, count);
		resultQueryBuilder.append(" Limit ");
		resultQueryBuilder.append(page.getFromIndex());
		resultQueryBuilder.append(",");
		resultQueryBuilder.append(page.getCurItemCount());
		String resultQuery = resultQueryBuilder.toString();
		System.out.println(resultQuery);
		
		List<Comment> curItems = new ArrayList<Comment>(page.getCurItemCount());
		List<Map<String, Object>> result = commentDao.query(resultQuery);
		for (Map<String, Object> entity : result)
		{
			Comment comment = new Comment();
			comment.setCommentId((Integer) entity.get("comment_id"));
			comment.setUser(userDao.findById(User.class, (Integer) entity.get("user_id")));
			comment.setContent((String) entity.get("content"));
			Integer referId = (Integer) entity.get("refer_id");
			if (referId != null)
			{
				comment.setReferComment(commentDao.findById(referId));
			}
			comment.setPubTime((Timestamp) entity.get("pub_time"));
			curItems.add(comment);
		}
		page.setCurItems(curItems);
		
		return page;
	}

}
