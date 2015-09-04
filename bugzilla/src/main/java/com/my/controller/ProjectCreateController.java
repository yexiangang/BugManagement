package com.my.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.my.domain.Project;
import com.my.domain.User;
import com.my.service.ProjectService;
import com.my.service.UserService;
import com.my.util.Constant;

@Controller
public class ProjectCreateController {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/project-create.do")
	public ModelAndView projectCreate(HttpServletRequest request, HttpSession httpSession) {

		ModelAndView mav = new ModelAndView("project_create");

		User creator = (User) httpSession.getAttribute("online_user");
		
		
		if(creator == null) {
			mav.addObject("is_logout", true);
			return mav;
		}else if(creator.getRole().equals(Constant.ROLE_QA) || creator.getRole().equals(Constant.ROLE_DEVELOPER)){
			mav.addObject("no_authority", true);
			return mav;
		}
		
		String name = request.getParameter("project_name");
		
		List<User> leaders = new ArrayList<User>();
		leaders = projectService.getLeader(creator.getName());
		
		mav.addObject("leaders", leaders);
		if(projectService.isNameExists(name)){
			mav.addObject("name_exists", true);
			return mav;
		}
		
		
		String teamLeaderName = request.getParameter("TeamLeader");
		
		
		User teamLeader = projectService.getUserByName(teamLeaderName);
		
		
		String describe = request.getParameter("describe");
		
		Project newProject = new Project();
		newProject.setUserByCreator(creator);
		newProject.setName(name);
		newProject.setDescription(describe);
		newProject.setCreateDate(new Timestamp(System.currentTimeMillis()));
		newProject.setUserByLeader(teamLeader);
		newProject.setSprint(Constant.PROJECT_DEFAULT_SPRINT);
		newProject.setProjectStatus(Constant.PROJECT_STATUS_CREATED);
		
		if(name.equals("") || name == null){
			mav.addObject("name_error", true);
		}else if (describe.equals("") || describe == null) {
			mav.addObject("describe_error", true);
		}else {
			if (!projectService.save(newProject)) {
				mav.addObject("create_error", true);
			}else {
				mav.addObject("create_suc", true);
			}
		}

		
		return mav;

	}

	@RequestMapping(value = "/project-create.do", method = RequestMethod.GET)
	public ModelAndView projectCreateGet(HttpServletRequest request, HttpSession httpSession) {

		ModelAndView mav = new ModelAndView("project_create");
		
		User creator = (User) httpSession.getAttribute("online_user");
		List<User> leaders = new ArrayList<User>();
		leaders = projectService.getLeader(creator.getName());
		
		mav.addObject("leaders", leaders);

		return mav;

	}
}
