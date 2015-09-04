package com.my.util;

public class Constant
{
	public static final String ROLE_ADMIN = "admin";
	public static final String ROLE_TEAM_LEADER = "team_leader";
	public static final String ROLE_QA = "QA";
	public static final String ROLE_DEVELOPER = "developer";
	
	//-----------------------bug_states-----------------------
	public static final String BUG_INIT="undistributed";//未分配
	public static final String BUG_ASSIGN="distributed";//分配
	public static final String BUG_DEVELOPING="developing";//开发中
	public static final String BUG_DEVELOPED="developed";//开发完成
	public static final String BUG_TESTING="testing";//测试中
	public static final String BUG_TESTPASSED="test_passed";//测试通过
	public static final String BUG_TESTUNPASSED="test_unpassed";//测试未通过
	public static final String BUG_END="closed";//测试关闭
	
	public static final String PROJECT_DEFAULT_SPRINT = "created";//紧急程度1
	
	
	public static final int BUG_SEVERITY_ONE = 1;//紧急程度1
	public static final int BUG_SEVERITY_TWO = 2;//2
	public static final int BUG_SEVERITY_THREE = 3;//3
	public static final int BUG_SEVERITY_FOUR = 4;//4
	
	public static final String PROJECT_STATUS_CREATED = "created";
	public static final String PROJECT_STATUS_DEVELOPING = "developing";
	public static final String PROJECT_STATUS_CLOSE = "closed";
	
	public static final String PROPERTIES_FILE_ROLE = "role.properties";
	public static final String PROPERTIES_FILE_SUFFIX = "suffix.properties";
	
	public static final int PAGE_SIZE = 10;
	public static final int SEARCH_NUM = 10;//搜索条件数量
	
	public static final String ATTRIBUTE_USER_MAP = "user_map";
	public static final String ATTRIBUTE_USER = "online_user";
	
}
