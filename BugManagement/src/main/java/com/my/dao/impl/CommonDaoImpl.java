package com.my.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import com.my.dao.CommonDao;

public class CommonDaoImpl<T> extends HibernateDaoSupport implements CommonDao<T>
{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate()
	{
		return jdbcTemplate;
	}

	@Autowired
	public void setHT(HibernateTemplate template)
	{
		super.setHibernateTemplate(template);
	}

	public void save(T instance)
	{
		getHibernateTemplate().save(instance);
	}

	public List<T> findByExample(T instance)
	{
		List<T> list = getHibernateTemplate().findByExample(instance);
		return list;
	}

	public T findById(Class<T> clazz, Integer id)
	{
		return getHibernateTemplate().get(clazz, id);
	}

	public void execute(String sql)
	{
		getJdbcTemplate().execute(sql);
	}

	public List<Map<String, Object>> query(String sql)
	{
		return getJdbcTemplate().queryForList(sql);
	}

	public List<Map<String, Object>> query(String sql, Object[] args,int[] argTypes)
	{
		return getJdbcTemplate().queryForList(sql, args, argTypes);
	}

	public void update(T instance)
	{
		getHibernateTemplate().update(instance);
	}

	public void delete(T instance)
	{
		getHibernateTemplate().delete(instance);
	}

	public <E> E queryForObject(String sql, Class<E> clazz)
	{
		return getJdbcTemplate().queryForObject(sql, clazz);
	}
	
	
	/*
	 * test:  String hql ="from User where userId=?";
	 * fromå�Žé�¢è·Ÿçš„æ˜¯ç±»å��ä¸�æ˜¯è¡¨å��,whereå�Žé�¢å�¯ä»¥æ˜¯æ•°æ�®åº“è¡¨çš„å±žæ€§å��ï¼Œä½†æœ€å�Žä½¿ç”¨ç±»ä¸­çš„å­—æ®µï¼Œå�¯ä»¥æ·»åŠ å¤šä¸ªé—®å�·ï¼Œ
	 * æ›¿ä»£é—®å�·çš„å†…å®¹ä¾�æ¬¡å­˜å…¥æ•°ç»„ä¸­ï¼Œå�„ç§�ç±»åž‹çš„æ•°æ�®è½¬ä¸ºStringå­˜å‚¨å�³å�¯ï¼›
	 * æ­¤æ–¹æ³•å�¯è¿›è¡Œå¤šè¡¨æŸ¥è¯¢
	 */
	public List<Object> findByHql(String hql,String []str){
		
		List<Object> list =null;
		Session session=null;
		Transaction transaction =null;
		try{
			session =getSessionFactory().openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery(hql);
			if(str!=null){
				for(int i=0;i<str.length;i++){
					query.setString(i, str[i]);
				}
			}
			list =query.list();
			transaction.commit();
		}catch(Exception e){
			e.printStackTrace();
			if(transaction!=null){
				transaction.rollback();
			}
		}finally {
			if(session!=null&&session.isOpen()){
				session.close();
			}
		}
		if(list==null){
			list=new ArrayList<Object>();
		}
		return list;
	}
	@SuppressWarnings("unchecked")
	public List<T> findByHql(String hql, Object value){
		return  (List<T>) getHibernateTemplate().find(hql, value);
		
	}
	public int update(String sql, Object[] args, int[] argTypes)
	{
		return getJdbcTemplate().update(sql, args, argTypes);
	}

	public int update(String sql)
	{
		return getJdbcTemplate().update(sql);
	}

}
