package com.my.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.my.domain.Member;
import com.my.domain.Project;
import com.my.domain.User;
import com.my.service.ProjectService;
import com.my.util.Constant;
import com.my.util.Page;
import com.my.util.StringParseUtil;

@Controller
public class ProjectManageController {

	@Autowired
	private ProjectService projectService;
	

	@RequestMapping(value = "project-manage.do")
	public ModelAndView projectManage(HttpServletRequest request, HttpSession session) {
		if (session.getAttribute("project_manage") == null) {
			return new ModelAndView("main");
		}
		ModelAndView mav = new ModelAndView("project_manage");


		User currentUser = (User) session.getAttribute("online_user");
		if(currentUser == null) {
			mav.addObject("project_manage_result_flag", "您已下线，请重新登录");
			mav.addObject("project_manage_result", false);
			return mav;
		}else if(currentUser.getRole().equals(Constant.ROLE_QA) || currentUser.getRole().equals(Constant.ROLE_DEVELOPER)){
			mav.addObject("project_manage_result_flag", "您无权访问该页面");
			mav.addObject("project_manage_result", false);
			return mav;
		}
		String curUserName = currentUser.getName();
		String curPageStr = request.getParameter("page");
		String projectName = request.getParameter("projectname");

		String project_manage_result = null;
		Boolean project_manage_result_flag = true;
		try {
			project_manage_result = (String) session.getAttribute("project_manage_result");
			project_manage_result_flag = (Boolean) session.getAttribute("project_manage_result_flag");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.setAttribute("project_manage_result", null);
		session.setAttribute("project_manage_result_flag", null);
		
		int curPage = StringParseUtil.parse2Integer(curPageStr, 1);
		
		Page<Project> page = projectService.getPage(curUserName, projectName, curPage);
		List<User> leaders = projectService.getLeader(curUserName);
		List<User> developerANDQSs = projectService.findAllQSAndDeveloper();

		mav.addObject("project_manage_result_flag", project_manage_result_flag);
		mav.addObject("project_manage_result", project_manage_result);
		mav.addObject("members", developerANDQSs);
		mav.addObject("leaders", leaders);
		mav.addObject("page", page);
		mav.addObject("project_status_created", Constant.PROJECT_STATUS_CREATED);
		mav.addObject("project_status_close", Constant.PROJECT_STATUS_CLOSE);
		mav.addObject("project_status_developing", Constant.PROJECT_STATUS_DEVELOPING);
		
		
		
		return mav;

	}



	@RequestMapping(value = "project-manage.do", params = "delete=删除")
	public ModelAndView projectManageDelete(HttpServletRequest request, HttpSession session) {
		if (session.getAttribute("project_manage") == null) {
			return new ModelAndView("main");
		}

		String projectId = request.getParameter("projectId");
		int id = Integer.parseInt(projectId);
		Project project = projectService.findProjectById(id);
		
		boolean flag = false;
		
		flag = projectService.deleteMember(project);
		
		if(!flag){
			session.setAttribute("project_manage_result", "删除失败(ID:" + id +")");
			session.setAttribute("project_manage_result_flag", false);
			return projectManage(request, session);
		}
		
		flag = flag && projectService.deleteProjectById(id);
		
		if(flag){
			session.setAttribute("project_manage_result", "删除成功(ID:" + id +")");
			session.setAttribute("project_manage_result_flag", true);
		}else {
			session.setAttribute("project_manage_result", "删除失败(ID:" + id +")");
			session.setAttribute("project_manage_result_flag", false);
		}

		return projectManage(request, session);

	}

	@RequestMapping(value = "project-manage.do", params = "close=关闭")
	public ModelAndView editProject(HttpServletRequest request, HttpSession session) {
		if (session.getAttribute("project_manage") == null) {
			return new ModelAndView("main");
		}
		
		String id = request.getParameter("projectId");
		int projectId = Integer.parseInt(id);
		
		if(projectService.updateProjectStatusWithId(projectId, Constant.PROJECT_STATUS_CLOSE)){
			session.setAttribute("project_manage_result", "关闭成功(ID:" + id +")");
			session.setAttribute("project_manage_result_flag", true);
		}else {
			session.setAttribute("project_manage_result", "关闭失败(ID:" + id +")");
			session.setAttribute("project_manage_result_flag", false);
		}

		return projectManage(request, session);

	}
	
	@RequestMapping(value = "project-manage.do", params = "activate=激活")
	public ModelAndView activateProject(HttpServletRequest request, HttpSession session) {
		if (session.getAttribute("project_manage") == null) {
			return new ModelAndView("main");
		}
		
		
		String id = request.getParameter("projectId");
		int projectId = Integer.parseInt(id);
		
		Project project = projectService.findProjectById(projectId);
		if(project == null) {
			session.setAttribute("project_manage_result", "激活失败(ID:" + id +")");
			session.setAttribute("project_manage_result_flag", false);
			return projectManage(request, session);
		}
		Set<Member> members = projectService.findMembers(project);
		boolean flagDeveloper = false;
		boolean flagQA = false;
		for(Member member : members){
			if(member.getUser().getRole().equals(Constant.ROLE_DEVELOPER))
				flagDeveloper = true;
			if(member.getUser().getRole().equals(Constant.ROLE_QA))
				flagDeveloper = true;
		}
		
		if(!(flagDeveloper || flagQA)){
			session.setAttribute("project_manage_result", "激活失败，项目(ID:" + id +")中尚未添加QA或Developer");
			session.setAttribute("project_manage_result_flag", false);
			return projectManage(request, session);
		}
		
		if(projectService.updateProjectStatusWithId(projectId, Constant.PROJECT_STATUS_DEVELOPING)){
			session.setAttribute("project_manage_result", "激活成功(ID:" + id +")");
			session.setAttribute("project_manage_result_flag", true);
		}else {
			session.setAttribute("project_manage_result", "激活失败(ID:" + id +")");
			session.setAttribute("project_manage_result_flag", false);
		}
		
		return projectManage(request, session);

	}
	
	@RequestMapping(value = "edit-project.do", params = "edit=提交")
	public ModelAndView addMember(HttpServletRequest request, HttpSession session) {
		if (session.getAttribute("project_manage") == null) {
			return new ModelAndView("main");
		}
		List<User> developerANDQAs = projectService.findAllQSAndDeveloper();
		
		String id = request.getParameter("project_id").trim();
		
		
		
		String projectName = request.getParameter("project_name").trim();
		
		String sprint = request.getParameter("sprint").trim();
		String desc = request.getParameter("description");
		int projectId = Integer.parseInt(id);
		Project project = projectService.findProjectById(projectId);
		
		if(projectName == null || projectName == ""){
			session.setAttribute("project_manage_result", "编辑失败，(ID:" + id +")项目名称不能为空");
			session.setAttribute("project_manage_result_flag", false);
			return projectManage(request, session);
		}
		if(!projectName.equals(project.getName())){
			if(projectService.isNameExists(projectName)){
				session.setAttribute("project_manage_result", "编辑失败，(ID:" + id +")项目名已存在");
				session.setAttribute("project_manage_result_flag", false);
				return projectManage(request, session);
			}
		}
		if(desc == null || desc == ""){
			session.setAttribute("project_manage_result", "编辑失败，(ID:" + id +")项目描述不能为空");
			session.setAttribute("project_manage_result_flag", false);
			return projectManage(request, session);
		}
		if (sprint == null || sprint == "") {
			session.setAttribute("project_manage_result", "编辑失败，(ID:" + id +")项目阶段不能为空");
			session.setAttribute("project_manage_result_flag", false);
			return projectManage(request, session);
		}
		
		
		
		if(project.getProjectStatus().equals(Constant.PROJECT_STATUS_CREATED)){
			String teamleaderName = request.getParameter("teamleader");
			User teamleader = projectService.getUserByName(teamleaderName);
			project.setUserByLeader(teamleader);
		}
		
		if(project.getProjectStatus().equals(Constant.PROJECT_STATUS_CLOSE)){
			session.setAttribute("project_manage_result", "编辑失败，(ID:" + id +")项目处于已关闭状态");
			session.setAttribute("project_manage_result_flag", false);
			return projectManage(request, session);
		}
		
		
		project.setSprint(sprint);
		project.setName(projectName);
		project.setDescription(desc);
		
		boolean flag = false;
		flag = projectService.update(project);
		if(flag == false){
			session.setAttribute("project_manage_result", "编辑失败(ID:" + id +")");
			session.setAttribute("project_manage_result_flag", false);
			return projectManage(request, session);
		}
		
		List<User> choosedQADevelopers = new ArrayList<User>();
		
		for(User user : developerANDQAs){
			String result = request.getParameter("member" + user.getUserId());
			if(result != null){
				choosedQADevelopers.add(user);
			}
			
		}
		
		
		//当项目处于developing状态时，只能添加项目成员
		if(project.getProjectStatus().equals(Constant.PROJECT_STATUS_DEVELOPING)){
			StringBuilder stringBuilder = new StringBuilder();
			
			for(User user : choosedQADevelopers) {
				stringBuilder.append(user.getName() + " ");
			}
			
			flag = flag && projectService.addMemberAtDeveloping(choosedQADevelopers, project);
			if(flag){
				if(choosedQADevelopers.isEmpty() || choosedQADevelopers == null){
					session.setAttribute("project_manage_result", "编辑成功(ID:" + id +")");
				}else {
					session.setAttribute("project_manage_result", "编辑成功，(ID:" + id +")成功添加用户" + stringBuilder.toString());
				}
				session.setAttribute("project_manage_result_flag", true);
			}else {
				session.setAttribute("project_manage_result", "编辑失败(ID:" + id +")");
				session.setAttribute("project_manage_result_flag", false);
			}
			return projectManage(request, session);
		}
		
		flag = flag && (projectService.addMember(choosedQADevelopers, project));
		
		if(flag == false){
			session.setAttribute("project_manage_result", "编辑失败(ID:" + id +")");
			session.setAttribute("project_manage_result_flag", false);
		}else {
			session.setAttribute("project_manage_result", "编辑成功(ID:" + id +")");
			session.setAttribute("project_manage_result_flag", true);
		}
		
		
		
		return projectManage(request, session);

	}
	
	@RequestMapping(value="edit-project.do", params = "edit2=编辑")
	public ModelAndView editProject2(HttpServletRequest request, HttpSession session){
		
		if (session.getAttribute("project_manage") == null) {
			return new ModelAndView("main");
		}
		
		String id = request.getParameter("project_id");
		int projectId = Integer.parseInt(id);
		String projectName = request.getParameter("project_name");
		String desc = request.getParameter("description");
		String projectSprint = request.getParameter("sprint");
		Project project = projectService.findProjectById(projectId);
		if(project == null){
			session.setAttribute("project_manage_result", "编辑失败，项目(ID:" + id +")不存在");
			session.setAttribute("project_manage_result_flag", false);
		}
		
		if(projectName == null || projectName == ""){
			session.setAttribute("project_manage_result", "编辑失败，(ID:" + id +")项目名称不能为空");
			session.setAttribute("project_manage_result_flag", false);
			return projectManage(request, session);
		}
		if(!projectName.equals(project.getName())){
			if(projectService.isNameExists(projectName)){
				session.setAttribute("project_manage_result", "编辑失败，(ID:" + id +")项目名已存在");
				session.setAttribute("project_manage_result_flag", false);
				return projectManage(request, session);
			}
		}
		if(desc == null || desc == ""){
			session.setAttribute("project_manage_result", "编辑失败，(ID:" + id +")项目描述不能为空");
			session.setAttribute("project_manage_result_flag", false);
			return projectManage(request, session);
		}
		if (projectSprint == null || projectSprint == "") {
			session.setAttribute("project_manage_result", "编辑失败，(ID:" + id +")项目阶段不能为空");
			session.setAttribute("project_manage_result_flag", false);
			return projectManage(request, session);
		}
		
		
		
		
		project.setName(projectName);
		project.setSprint(projectSprint);
		project.setDescription(desc);
		
		if(projectService.update(project)){
			session.setAttribute("project_manage_result", "编辑成功(ID:" + id +")");
			session.setAttribute("project_manage_result_flag", true);
		}else {
			session.setAttribute("project_manage_result", "编辑失败(ID:" + id +")");
			session.setAttribute("project_manage_result_flag", false);
		}
		
		return projectManage(request, session);
		
	}
	
	@RequestMapping(value = "showMembers.do")
	public @ResponseBody String showMembers(HttpServletRequest request, HttpSession session, HttpServletResponse response)
					throws IOException{
		
		
		String projectId = request.getParameter("projectId");
		int id = Integer.parseInt(projectId);
		Project project = projectService.findProjectById(id);
		if(project == null)
			return null;
		StringBuilder sBuilder = new StringBuilder();
		List<User> developerANDQAs = projectService.findAllQSAndDeveloper();
		for(User user : developerANDQAs) {
			sBuilder.append(user.getUserId() + " ");
		}
		
		sBuilder.append(",");
		
		Set<Member> members = projectService.findMembers(project);
		
		
		if(members.size() == 0)
			return null;
		
		
		for(Member member : members){
			sBuilder.append(member.getUser().getUserId() + " ");
		}
		
		
		return sBuilder.toString();
		
	}
	
	@RequestMapping(value = "showMembersNOTCreated.do")
	public @ResponseBody String showMembersNOTCreated(HttpServletRequest request, HttpSession session, HttpServletResponse response)
			throws IOException{
		
		
		String projectId = request.getParameter("projectId");
		int id = Integer.parseInt(projectId);
		Project project = projectService.findProjectById(id);
		if(project == null)
			return null;
		StringBuilder sBuilder = new StringBuilder();
		
		Set<Member> members = projectService.findMembers(project);
		
		
		if(members.size() == 0)
			return null;
		
		
		for(Member member : members){
			sBuilder.append(member.getUser().getUserId() + " ");
		}
		
		
		return sBuilder.toString();
		
	}
	@RequestMapping(value = "showMembersToString.do")
	public @ResponseBody String showMembersToString(HttpServletRequest request, HttpSession session, HttpServletResponse response)
			throws IOException{
		
		
		String projectId = request.getParameter("projectId");
		int id = StringParseUtil.parse2Integer(projectId, -1);
		Project project = projectService.findProjectById(id);
		if(project == null)
			return null;
		Set<Member> members = projectService.findMembers(project);
		if(members.isEmpty()){
			return null;
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		
		int flag = 1;
		for(Member member : members) {
			User user = member.getUser();
			stringBuilder.append(user.getName() + " ");
			if(flag % 5 == 0){
				stringBuilder.append("<br/>");
			}
			flag++;
		}
		
		return stringBuilder.toString();
		
	}

}
