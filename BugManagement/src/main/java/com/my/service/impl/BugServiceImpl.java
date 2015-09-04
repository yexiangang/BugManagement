package com.my.service.impl;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.my.dao.AssignmentDao;
import com.my.dao.BugDao;
import com.my.dao.CommentDao;
import com.my.dao.MemberDao;
import com.my.dao.MemberIdDao;
import com.my.dao.ProjectDao;
import com.my.dao.UserDao;
import com.my.domain.Assignment;
import com.my.domain.Bug;
import com.my.domain.Project;
import com.my.domain.User;
import com.my.service.BugService;
import com.my.util.Constant;
import com.my.util.Page;

public class BugServiceImpl implements BugService
{

	@Autowired
	private BugDao			bugDao;
	@Autowired
	private AssignmentDao	assignmentDao;
	@Autowired
	private CommentDao		commentDao;
	@Autowired
	private ProjectDao		projectDao;
	@Autowired
	private UserDao			userDao;
	@Autowired
	private MemberIdDao		memberIdDao;
	@Autowired
	private MemberDao		memberDao;

	
	
	public void updateStatus(int bugId ,String  new_state)
	{//a little problem,the assignment will be modified multiple rows;
		Bug bug = bugDao.findById(Bug.class, bugId);
		bug.setBugStatus(new_state);
		bugDao.update(bug);
		String sql="UPDATE assignment SET status=? WHERE bug_id=? AND (end_time IS NULL) ";
		Object[] args={new_state,new Integer(bugId)};
		int[] argTypes={Types.VARCHAR,Types.INTEGER};
		assignmentDao.update(sql, args, argTypes);
	}
	public List<User> findMember(int projectId)
	{
		String sql="SELECT user_id FROM member WHERE project_id=?";
		List<User> users=new ArrayList<User>();
		Object[] args={new Integer(projectId)};
		int[] argTypes={Types.INTEGER};
		List<Map<String,Object>> result=memberIdDao.query(sql, args, argTypes);
		for(Map<String,Object> entity:result)
		{
			Integer userId=(Integer) entity.get("user_id");
			User user=userDao.findById(User.class, userId);
//			if(user.getRole().equals(Constant.ROLE_DEVELOPER))
//			{
//				users.add(user);
//			}
			
		}
		return users;
	}
	public void addEndTime(int bugId)
	{
//	 	??? findByExample: I put the bug_ID,but the result of search  is all;
//		Assignment assignment=new Assignment();
//		Bug bug=new Bug();
//		bug.setBugId(bugId);
//		assignment.setBug(bug);
//	 	List<Assignment> assignmentList = assignmentDao.findByExample(assignment);
		System.out.println("**********Winston************");
		String hql="FROM Assignment assign WHERE assign.bug.bugId=? ";
	 	List<Assignment> assignmentList = assignmentDao.findByHql(hql, new Integer(bugId));
		Date end = new Date();
		for(Assignment entity: assignmentList)
		{
			System.out.println(entity.getAssignId());
			if(entity.getEndTime()==null)
			{
				entity.setEndTime(end);
				assignmentDao.update(entity);
			}
			
		}
		
	}
	public void assignTo(User from, String to, String bug_status,int bugId)
	{
		Assignment assignment = new Assignment();
		//change bug status
		List<User> userTo=userDao.findUserByName(to);
		Bug bug = bugDao.findById(Bug.class, bugId);
		bug.setBugStatus(bug_status);
		bugDao.update(bug);
		//insert assignment
		assignment.setUserByOperateUserId(from);
		assignment.setUserByAssignedUserId(userTo.get(0));
		assignment.setBug(bug);
		assignment.setStatus(bug_status);
		Date now = new Date();
		assignment.setBeginTime(now);
		assignment.setEndTime(null);
		assignmentDao.save(assignment);
	}


	// search2 by winston
	//*********************************************************************************
	public List<Integer> findProjectIdList(String role, int user_id, String project_name)
	{// 权限控制主要是在于只能查询规定的project.
		List<Integer> projectIdList = new ArrayList<Integer>();
		String sql;
		List<Map<String, Object>> result;
		if (Constant.ROLE_ADMIN.equals(role))
		{
			sql = "SELECT project_id FROM project";
			result = memberDao.query(sql);
		}
		else
		{
			sql = "SELECT project_id FROM member WHERE user_id = ?";
			Object[] args = new Object[]
			{ new Integer(user_id) };
			int[] argTypes = new int[]
			{ Types.INTEGER };
			result = memberDao.query(sql, args, argTypes);
		}
		if (result == null) return null;// not found
		for (Map<String, Object> entity : result)
		{
			Integer i = (Integer) entity.get("project_id");
			projectIdList.add(i);
		}
		if (project_name != null)
		{
			List<Project> project = projectDao.findProjectByName(project_name);

			if (projectIdList.contains(project.get(0).getProjectId()))
			{
				projectIdList.clear();
				projectIdList.add(new Integer(project.get(0).getProjectId()));
			}
			else
			{
				projectIdList.clear();
			}
		}
		return projectIdList;
	}

	public List<Bug> search(Bug bug, 
			String creator, 
			List<Integer> projectIdList, 
			String assignTo,
			String assignBy)
	{
		StringBuilder sql = new StringBuilder("SELECT DISTINCT  b.bug_id FROM bug AS b,assignment AS a WHERE 1=1 ");
		// StringBuilder sql_a=new StringBuilder("SELECT DISTINCT bug_id FROM
		// assignment WHERE");
		List<Bug> bugList = new ArrayList<Bug>();
		// Object[] args=new Object[Constant.SEARCH_NUM];
		List<Object> args = new ArrayList<Object>();
		int[] argsTypes = new int[Constant.SEARCH_NUM];
		int num = 0;
		if (bug.getName() != null)
		{
			sql.append(" AND b.name= ? ");
			args.add(bug.getName());
			argsTypes[num++] = Types.VARCHAR;
		}
		if (bug.getTag() != null)
		{
			sql.append(" AND b.tag= ?");
			args.add(bug.getTag());
			argsTypes[num++] = Types.VARCHAR;
		}
		if (creator != null)
		{
			User Creator = userDao.findUserByName(creator).get(0);
			if (Creator == null) return null;
			sql.append(" AND b.creator = ? ");
			args.add(Creator.getUserId());
			argsTypes[num++] = Types.INTEGER;
		}
		if (bug.getBugStatus() != null)
		{
			sql.append(" AND b.bug_status = ?");
			args.add(bug.getBugStatus());
			argsTypes[num++] = Types.VARCHAR;
		}
		if (bug.getSeverity() != null)
		{
			sql.append(" AND b.severity = ?");
			args.add(bug.getSeverity());
			argsTypes[num++] = Types.TINYINT;
		}
		if (bug.getSprint() != null)
		{
			sql.append(" AND b.sprint = ?");
			args.add(bug.getSprint());
			argsTypes[num++] = Types.TINYINT;
		}
		if (assignTo != null || assignBy != null) sql.append(" AND a.bug_id=b.bug_id ");
		if (assignTo != null)
		{
			User Assignto=userDao.findUserByName(creator).get(0);
			
			if (Assignto == null) return null;
			sql.append(" AND a.assigned_user_id = ?");
			args.add(Assignto.getUserId());
			argsTypes[num++] = Types.INTEGER;
		}
		if (assignBy != null)
		{
			User Assignby=userDao.findUserByName(creator).get(0);
			if (Assignby == null) return null;
			sql.append(" AND a.operate_user_id = ?");
			args.add(Assignby.getUserId());
			argsTypes[num++] = Types.INTEGER;
		}
		if (projectIdList == null)
			return null;
		else
		{
			sql.append(" AND b.project_id = ?");
			argsTypes[num++] = Types.INTEGER;
			int[] argTypes2 = new int[num];
			System.arraycopy(argsTypes, 0, argTypes2, 0, num);
			// test-code
			// for(Object entity:args)
			// {
			// System.out.println(entity.toString());
			// }
			// for(int entity:argTypes2)
			// {
			// System.out.println(Integer.toString(entity));
			// }

			for (Integer projectId : projectIdList)
			{
				args.add(projectId);
				List<Map<String, Object>> list = assignmentDao.query(sql.toString(), args.toArray(), argTypes2);

				if (list == null)
				{
					System.out.println("List_NULL");
					continue;
				}

				for (Map<String, Object> entity : list)
				{
					Integer bugId = (Integer) entity.get("bug_id");
					Bug bug1 = bugDao.findById(Bug.class, bugId);
					bugList.add(bug1);
				}
				args.remove(projectId);
			}
		}
		// test-code
		 System.out.println(sql.toString());
		 System.out.println(bug.getTag());
		 System.out.println(bug.getSeverity());
		 for(Integer projectId:projectIdList)
		 {
		 System.out.println(projectId);
		 }
		return bugList;
	}
	public Page<Bug> getPage(List<Bug> bugList, int curPage, int pageSize)
	{
		Page<Bug> bugPage = new Page<Bug>(curPage, pageSize, bugList);
		return bugPage;
	}

	public void create(Bug bug)
	{
		bugDao.save(bug);
	}

	public boolean delete(Bug bug)
	{
		Bug findById = bugDao.findById(Bug.class, bug.getBugId());
		String hql ="from Comment where bug.bugId=?";
		String str[]={bug.getBugId()+""};
		List<Object> list = findByHql(hql, str);
		if (findById != null && findById.getBugStatus().equals(Constant.BUG_INIT)&&list.size()==0)
		{
			bugDao.delete(findById);
			return true;
		}
		return false;
	}

	public List<Object> findByHql(String hql,String []str){
		
		return  bugDao.findByHql(hql, str);
	}
	
	public List<Object> show(User user)
	{
		
		List<Object> list=new ArrayList<Object>();
		List<Object> bugs=new ArrayList<Object>();
		List<Object> findByHql=null;
		String hql = null,hq=null;
		if (user.getRole().equals(Constant.ROLE_ADMIN))
		{
			hq="select bugId from Bug order by bugId DESC";
			bugs=bugDao.findByHql(hq, null);
			for (Object object : bugs) {
				hql = "select  bug.bugId,bug.name,bug.project.name,bug.user.name,userByOperateUserId.name,userByAssignedUserId.name,bug.project.sprint,bug.bugStatus,bug.severity,bug.createDate  from Assignment where bug.bugId = ? and bug.bugStatus !='undistributed' group by assignId order by assignId DESC ";
				String str[]={object.toString()};
				findByHql = bugDao.findByHql(hql, str);
				if(findByHql.size()>0){
					Object[] ob=(Object[]) findByHql.get(0);
					if(ob[7].equals(Constant.BUG_END)){
						ob[4]="null";
						ob[5]="null";
					}
					list.add(ob);
				}else{
					String hql2 = "select bugId,name,project.name,user.name,name,name,project.sprint,bugStatus,severity,createDate from Bug where bugStatus ='undistributed' and bugId=?";
					List<Object> li=bugDao.findByHql(hql2, str);
					if(li.size()>0){
						Object[] ob=(Object[])li.get(0);
						ob[4]="null";
						ob[5]="null";	
						list.add(ob);
					}
				}
			}
				
			
		}
		else if (user.getRole().equals(Constant.ROLE_TEAM_LEADER))
		{
			hq="select bugId from Bug where project.userByLeader.userId=? order by bugId DESC";
			String st[]={user.getUserId()+""};
			bugs=bugDao.findByHql(hq, st);
			for (Object object : bugs) {
				hql = "select  bug.bugId,bug.name,bug.project.name,bug.user.name,userByOperateUserId.name,userByAssignedUserId.name,bug.project.sprint,bug.bugStatus,bug.severity,bug.createDate from Assignment where bug.bugId = ? and bug.bugStatus !='undistributed' group by assignId order by assignId DESC";
				String str[]={object.toString()};
				findByHql=bugDao.findByHql(hql, str);
				if(findByHql.size()>0){
					Object[] ob=(Object[]) findByHql.get(0);
					if(ob[7].equals(Constant.BUG_END)){
						ob[4]="null";
						ob[5]="null";
					}
					list.add(ob);
				}else{
					String hql2 = "select bugId,name,project.name,user.name,name,name,project.sprint,bugStatus,severity,createDate from Bug where project.userByLeader.userId=? and bugStatus ='undistributed' and bugId=?";
					String str2[] ={ user.getUserId() + "",object.toString()};
					List<Object> li=bugDao.findByHql(hql2, str2);
					if(li.size()>0){
						Object[] ob=(Object[])li.get(0);
						ob[4]="null";
						ob[5]="null";	
						list.add(ob);
					}
				}
			}
		}
		else if (user.getRole().equals(Constant.ROLE_QA)||user.getRole().equals(Constant.ROLE_DEVELOPER))
		{

			hql="select bug.bugId,bug.name,bug.project.name,bug.user.name,userByOperateUserId.name,userByAssignedUserId.name,bug.project.sprint,bug.bugStatus,bug.severity,bug.createDate from Assignment where userByAssignedUserId.userId=? and bug.bugStatus in('developed','testing','test_passed','test_unpassed','distributed','developing')  and (endTime is NULL) order by bug.bugId DESC";
			String str[] ={ user.getUserId() + ""};
			String hql2 = "select bugId,name,project.name,user.name,name,name,project.sprint,bugStatus,severity,createDate from Bug where user.userId=? and bugStatus ='undistributed' order by bugId DESC";
			String str2[] ={ user.getUserId() + ""};
			List<Object> lis = bugDao.findByHql(hql, str);
			List<Object> li=bugDao.findByHql(hql2, str2);
			if(li.size()>0){
				for (Object object : li) {
					Object[] ob=(Object[])object;
					ob[4]="null";
					ob[5]="null";
				}
			}
			int j=0,k=0;
			Object[] ob1=null,ob2=null;
			if(li.size()==0||lis.size()==0){
				list.addAll(li);
				list.addAll(lis);
			}else{
				
				for(int i=0;i<lis.size()+li.size();i++){
					if(j<lis.size()){
						ob1=(Object[]) lis.get(j);
					}
					if(k<li.size()){
						ob2 =(Object[]) li.get(k);
					}
					
					if((Integer)ob1[0]>(Integer)ob2[0]&&j<lis.size()||k>=li.size()){
						list.add(ob1);
						j++;
					}else if((Integer)ob1[0]<(Integer)ob2[0]&&k<li.size()||j>=lis.size()){
						list.add(ob2);
						k++;
					}
				}
			}
			
		}
//		else if (user.getRole().equals(Constant.ROLE_DEVELOPER))
//		{
//
//			hql = "select bug.bugId,bug.name,bug.project.name,bug.user.name,userByOperateUserId.name,userByAssignedUserId.name,bug.project.sprint,bug.bugStatus,bug.severity,bug.createDate from Assignment where userByAssignedUserId.userId=? and (endTime is null) and bug.bugStatus in('distributed','developing') order by bug.bugId DESC";
//			//hql = "select bug.bugId,bug.name,bug.project.name,bug.user.name,userByOperateUserId.name,userByAssignedUserId.name,bug.project.sprint,bug.bugStatus,bug.severity,bug.createDate,assignId from Assignment where endTime is null and bug.bugStatus in('distributed','developing') group by assignId";
//			String str[] ={ user.getUserId() + "" };			
//			list = bugDao.findByHql(hql, str);
//		}
		return list;
		
//		List<Object> list=new ArrayList<Object>();
//		String hql = null;
//		if (user.getRole().equals(Constant.ROLE_ADMIN))
//		{
//			
//			hql = "select bug.bugId,bug.name,bug.project.name,bug.user.name,userByOperateUserId.name,userByAssignedUserId.name,bug.project.sprint,bug.bugStatus,bug.severity,bug.createDate,count(distinct bug.bugId) from Assignment where  bug.bugStatus != 'undistributed' group by bug.bugId order by bug.bugId DESC";
//			//hql="select bug.bugId,bug.name,bug.project.name,bug.user.name,userByOperateUserId.name,userByAssignedUserId.name,bug.project.sprint,bug.bugStatus,bug.severity,bug.createDate,count(distinct bug.bugId) from Assignment where  bug.bugStatus != 'undistributed' and endTime=null";
//			list = bugDao.findByHql(hql, null);
//			String hql2 = "select bugId,name,project.name,user.name,name,name,project.sprint,bugStatus,severity,createDate from Bug where bugStatus ='undistributed'";
//			List<Object> li=bugDao.findByHql(hql2, null);
//			if(list==null){
//				list = new ArrayList<Object>();
//			}
//			if(list.size()==0){
//				list = new ArrayList<Object>();
//			}
//			if(li.size()>0){
//				for (Object object : li) {
//					Object[] ob=(Object[])object;
//					ob[4]="null";
//					ob[5]="null";
//					list.add(ob);
//				}
//			}
//			
//		}
//		else if (user.getRole().equals(Constant.ROLE_TEAM_LEADER))
//		{
//
//			hql="select bug.bugId,bug.name,bug.project.name,bug.user.name,userByOperateUserId.name,userByAssignedUserId.name,bug.project.sprint,bug.bugStatus,bug.severity,bug.createDate ,count(distinct bug.bugId) from Assignment where  bug.bugStatus != 'undistributed'  and   bug.project.userByLeader.userId=? group by bug.bugId order by bug.bugId DESC";
//			//hql="select bug.bugId,bug.name,bug.project.name,bug.user.name,userByOperateUserId.name,userByAssignedUserId.name,bug.project.sprint,bug.bugStatus,bug.severity,bug.createDate from Assignment where  bug.bugStatus != 'undistributed' and  bug.project.userByLeader.userId=? and endTime=null";
//
//			String str[] ={ user.getUserId() + "" };
//			String hql2 = "select bugId,name,project.name,user.name,name,name,project.sprint,bugStatus,severity,createDate from Bug where project.userByLeader.userId=? and bugStatus ='undistributed'";
//			String str2[] ={ user.getUserId() + ""};
//			list = bugDao.findByHql(hql, str);
//			List<Object> li=bugDao.findByHql(hql2, str2);
//			if(list==null){
//				list = new ArrayList<Object>();
//			}
//			if(list.size()==0){
//				list = new ArrayList<Object>();
//			}
//			if(li.size()>0){
//				for (Object object : li) {
//					Object[] ob=(Object[])object;
//					ob[4]="null";
//					ob[5]="null";
//					list.add(ob);
//				}
//			}
//
//		}
//		else if (user.getRole().equals(Constant.ROLE_QA))
//		{
//
//			hql="select bug.bugId,bug.name,bug.project.name,bug.user.name,userByOperateUserId.name,userByAssignedUserId.name,bug.project.sprint,bug.bugStatus,bug.severity,bug.createDate,count(distinct bug.bugId) from Assignment where userByAssignedUserId=? and endTime=null and bug.bugStatus in('developed','testing','test_passed','test_unpassed') or  userByAssignedUserId.userId=? and endTime=null and bug.bugStatus in('distributed','developing') group by bug.bugId order by bug.bugId DESC";
//			//hql="select bug.bugId,bug.name,bug.project.name,bug.user.name,userByOperateUserId.name,userByAssignedUserId.name,bug.project.sprint,bug.bugStatus,bug.severity,bug.createDate,count(distinct bug.bugId) from Assignment where userByAssignedUserId=? and bug.bugStatus in('developed','testing','test_passed','test_unpassed') and endTime=null";
//			String str[] ={ user.getUserId() + "",user.getUserId() + ""};
//			String hql2 = "select bugId,name,project.name,user.name,name,name,project.sprint,bugStatus,severity,createDate from Bug where user.userId=? and bugStatus ='undistributed'";
//			String str2[] ={ user.getUserId() + ""};
//			list = bugDao.findByHql(hql, str);
//			List<Object> li=bugDao.findByHql(hql2, str2);
//			if(list==null){
//				list = new ArrayList<Object>();
//			}
//			if(list.size()==0){
//				list = new ArrayList<Object>();
//			}
//			if(li.size()>0){
//				for (Object object : li) {
//					Object[] ob=(Object[])object;
//					ob[4]="null";
//					ob[5]="null";
//					list.add(ob);
//				}
//			}
//			
//		}
//		else if (user.getRole().equals(Constant.ROLE_DEVELOPER))
//		{
//
//			hql = "select bug.bugId,bug.name,bug.project.name,bug.user.name,userByOperateUserId.name,userByAssignedUserId.name,bug.project.sprint,bug.bugStatus,bug.severity,bug.createDate,count(distinct bug.bugId) from Assignment where userByAssignedUserId.userId=? and endTime=null and bug.bugStatus in('distributed','developing')  group by bug.bugId order by bug.bugId DESC";
//			//hql="select bug.bugId,bug.name,bug.project.name,bug.user.name,userByOperateUserId.name,userByAssignedUserId.name,bug.project.sprint,bug.bugStatus,bug.severity,bug.createDate,count(distinct bug.bugId) from Assignment where userByAssignedUserId.userId=? and bug.bugStatus in('distributed','developing') and endTime=null or userByOperateUserId.userId=?";
//			String str[] ={ user.getUserId() + "" };			
//			list = bugDao.findByHql(hql, str);
//			if(list==null){
//				list = new ArrayList<Object>();
//			}
//			if(list.size()==0){
//				list = new ArrayList<Object>();
//			}
//		}
//		return list;
	}
	
	
	//formal search
	public Page<Bug> searchByExample(   Bug bug,
										String bugAssigned,
										Timestamp startTime,
										Timestamp endTime,
										int curPage,
										int pageSize,
										boolean isAdmin,
										int[] projectIDs)
	{
		StringBuilder countQuery = new StringBuilder("SELECT COUNT(*) FROM bug AS b  WHERE 1=1 ");
		StringBuilder resultQuery = new StringBuilder("SELECT * FROM bug AS b  WHERE 1=1 ");
		List<Object> args = new ArrayList<Object>();
		int[] argTypes = new int[10];
		int count = 0;
		if (!isAdmin)
		{
			countQuery.append("AND project_id in (");
			resultQuery.append("AND project_id in (");
			for (int i = 0; i < projectIDs.length; i++)
			{
				countQuery.append(projectIDs[i] + ",");
				resultQuery.append(projectIDs[i] + ",");
			}
			countQuery.append("-1) ");
			resultQuery.append("-1) ");
		}
		if (bug.getName() != null && !bug.getName().isEmpty())
		{
			countQuery.append("AND name LIKE ? ");
			resultQuery.append("AND name LIKE ? ");
			args.add("%" + bug.getName() + "%");
			argTypes[count++] = Types.VARCHAR;
		}
		if (bug.getBugStatus() != null && !bug.getBugStatus().isEmpty())
		{
			countQuery.append("AND bug_status = ? ");
			resultQuery.append("AND bug_status = ? ");
			args.add(bug.getBugStatus());
			argTypes[count++] = Types.VARCHAR;
		}
		if (bug.getSeverity() != null && bug.getSeverity() != 0)
		{
			countQuery.append("AND severity = ? ");
			resultQuery.append("AND severity = ? ");
			args.add(bug.getSeverity());
			argTypes[count++] = Types.INTEGER;
		}
		if (bug.getSprint() != null && !bug.getSprint().isEmpty())
		{
			countQuery.append("AND sprint LIKE ? ");
			resultQuery.append("AND sprint LIKE ? ");
			args.add("%"+bug.getSprint()+"%");
			argTypes[count++] = Types.VARCHAR;
		}
		if (bug.getTag() != null && !bug.getTag().isEmpty())
		{
			countQuery.append("AND tag LIKE ? ");
			resultQuery.append("AND tag LIKE ? ");
			args.add("%" + bug.getTag() + "%");
			argTypes[count++] = Types.VARCHAR;
		}
		if (bug.getProject().getName() != null && !bug.getProject().getName().isEmpty())
		{
			List<Project> projectList=projectDao.findProjectByName(bug.getProject().getName());
			if(projectList==null)
			{
				System.out.println("BugServiceImpl_searchByExample Cannot find project2");
			}
			countQuery.append("AND project_id in (");
			resultQuery.append("AND project_id in (");
			if(projectList != null)
			{
				for(Project entity:projectList)
				{
					countQuery.append(entity.getProjectId()+",");
					resultQuery.append(entity.getProjectId()+",");
				}
			}
			countQuery.append("-1) ");
			resultQuery.append("-1) ");
			
		}
		if (bug.getUser().getName() != null && !bug.getUser().getName().isEmpty())
		{
			List<User> user1=userDao.findUserByName(bug.getUser().getName());
			if(user1==null)
			{
				System.out.println("BugServiceImpl_searchByExample Cannot find user2");
			}
			countQuery.append("AND creator in ( ");
			resultQuery.append("AND creator in ( ");
			if(user1!=null)
			{
				for(User entity : user1)
				{
					countQuery.append(entity.getUserId().toString() + ",");
					resultQuery.append(entity.getUserId().toString() + ",");
				}
			}
			countQuery.append("-1) ");
			resultQuery.append("-1) ");
			
		}
		if (startTime != null)
		{
			countQuery.append("AND create_date >= ? ");
			resultQuery.append("AND create_date >= ? ");
			args.add(startTime);
			argTypes[count++] = Types.TIMESTAMP;
		}
		if (endTime != null)
		{
			countQuery.append("AND create_date <= ? ");
			resultQuery.append("AND create_date <= ? ");
			args.add(endTime);
			argTypes[count++] = Types.TIMESTAMP;
		}
		//add by winston
		if(bugAssigned!=null && !bugAssigned.isEmpty())
		{
			List<User> assignedUserList=userDao.findUserByName(bugAssigned);
			if(assignedUserList==null)
			{
				countQuery.append("AND bug_id = -1 ");
				resultQuery.append("AND bug_id = -1 ");
			}
			else
			{//效率较低
				countQuery.append("AND bug_id in ( SELECT  bug_id FROM assignment WHERE (end_time IS NULL) AND  assigned_user_id in ( ");
				resultQuery.append("AND bug_id in ( SELECT  bug_id FROM assignment WHERE (end_time IS NULL) AND  assigned_user_id in ( ");
				for(User entity : assignedUserList)
				{
					countQuery.append(entity.getUserId().toString()+",");
					resultQuery.append(entity.getUserId().toString()+",");
				}
				countQuery.append("-1) ) ");
				resultQuery.append("-1) ) ");
			}
		}
		int[] argTypes2 = new int[count];
		System.arraycopy(argTypes, 0, argTypes2, 0, count);

		System.out.println(countQuery.toString());
		List<Map<String, Object>> r1 = userDao.query(countQuery.toString(), args.toArray(), argTypes2);
		Long itemCount = (Long) r1.get(0).get("COUNT(*)");
		Page<Bug> bugPage = new Page<Bug>(curPage, pageSize, itemCount);

		resultQuery.append("LIMIT " + bugPage.getFromIndex() + "," + (bugPage.getCurItemCount()));
		System.out.println(resultQuery.toString());
		List<Map<String, Object>> r2 = userDao.query(resultQuery.toString(), args.toArray(), argTypes2);

		List<Bug> bugs = new ArrayList<Bug>(pageSize);
		for (Map<String, Object> entity : r2)
		{
			Bug bug2=bugDao.findById(Bug.class,(Integer) entity.get("bug_id") );
			bugs.add(bug2);
//			Bug bug2 = new Bug();
//			bug2.setBugId((Integer) entity.get("bug_id"));
//			User creator = new User();
//			creator.setUserId((Integer) entity.get("creator"));
//			bug2.setUser(creator);
//			Project project1 = new Project();
//			project1.setProjectId((Integer) entity.get("project_id"));
//			bug2.setProject(project1);
//			bug2.setName((String) entity.get("name"));
//			bug2.setDescription((String) entity.get("description"));
//			bug2.setBugStatus((String) entity.get("bug_status"));
//			bug2.setSprint(((Integer) entity.get("sprint")).byteValue());
//			bug2.setTag((String) entity.get("tag"));
//			bug2.setSeverity(((Integer) entity.get("severity")).byteValue());
//			bug2.setCreateDate((Timestamp) entity.get("create_date"));

		}
		bugPage.setCurItems(bugs);

		return bugPage;
	}

	public Page<Bug> findByExample(Bug bug, int curPage, int pageSize)
	{
		return null;
	}

	public Bug findById(Integer id)
	{
		return bugDao.findById(Bug.class, id);
	}

	public boolean save(Bug bug)
	{
		bugDao.save(bug);
		return true;
	}
	
	public void updata(Bug bug){
		bugDao.update(bug);
	}

}
