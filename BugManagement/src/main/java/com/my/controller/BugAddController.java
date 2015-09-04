package com.my.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.my.domain.Assignment;
import com.my.domain.Bug;
import com.my.domain.Member;
import com.my.domain.Project;
import com.my.domain.UploadFile;
import com.my.domain.User;
import com.my.service.AssignmentService;
import com.my.service.BugService;
import com.my.service.FileSuffixService;
import com.my.service.ProjectService;
import com.my.service.UploadFileService;
import com.my.util.Constant;
import com.my.util.IOUtil;
import com.my.util.StringParseUtil;

@Controller
public class BugAddController
{
	@Autowired
	private BugService bugService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private UploadFileService uploadFileService;

	@Autowired
	private FileSuffixService fileSuffixService;

	@Autowired
	private AssignmentService assignmentService;

	@RequestMapping(value = "bug-add.do", method = RequestMethod.GET)
	public ModelAndView showAdd(HttpServletRequest request, HttpSession session)
	{
		ModelAndView mav = new ModelAndView("bug_add");

		User user = (User) session.getAttribute(Constant.ATTRIBUTE_USER);
		
		int[] projectIDs = projectService.getProjectIDs(user);
		Project[] projects = new Project[projectIDs.length];
		for (int i = 0; i < projects.length; ++i)
		{
			projects[i] = projectService.findProjectById(projectIDs[i]);
		}
		Arrays.sort(projects, new Comparator<Project>()
		{

			public int compare(Project o1, Project o2)
			{
				return o1.getProjectId() - o2.getProjectId();
			}
		});
		mav.addObject("projects", projects);
		
		Integer projectId = StringParseUtil.parse2Integer(request.getParameter("projectId"), null);
		if (projectId != null)
		{
			Project project = projectService.findProjectById(projectId);
			@SuppressWarnings("unchecked")
			Set<Member> members = project.getMembers();
			User leader = project.getUserByLeader();
			Member member = new Member();
			member.setUser(leader);
			members.add(member);

			mav.addObject("members", members);
		}

		return mav;
	}

	@RequestMapping(value = "bug-add.do", method = RequestMethod.POST)
	public ModelAndView add(HttpServletRequest request, HttpSession session)
	{
		try
		{
			String contextPath = request.getServletContext().getRealPath("/WEB-INF/");
			String uploadPath = "upload/";
			String tmpPath = "tmp/";

			DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
			fileItemFactory.setSizeThreshold(1024 * 1024);
			fileItemFactory.setRepository(new File(contextPath + tmpPath));
			ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
			List<FileItem> items = servletFileUpload.parseRequest(request);

			Map<String, String> bugMap = new HashMap<String, String>();
			for (FileItem item : items)
			{
				if (item.isFormField())
				{
					bugMap.put(item.getFieldName(), item.getString("utf-8"));
				}
			}
			Bug bug = new Bug();
			BeanUtils.populate(bug, bugMap);
			bug.setBugId(null);
			bug.setBugStatus(Constant.BUG_INIT);
			bug.setCreateDate(new Timestamp(System.currentTimeMillis()));
			User user = (User) session.getAttribute(Constant.ATTRIBUTE_USER);
			bug.setUser(user);
			Project project = new Project();
			Integer projectId = StringParseUtil.parse2Integer(bugMap.get("projectId"), null);
			project.setProjectId(projectId);
			bug.setProject(project);
			bug.setSprint(projectService.findProjectById(projectId).getSprint());
			bugService.save(bug);

			Integer assignedUserId = StringParseUtil.parse2Integer(bugMap.get("assignedUserId"), null);
			if (assignedUserId != null)
			{
				bug.setBugStatus(Constant.BUG_ASSIGN);
				User assignedUser = new User();
				assignedUser.setUserId(assignedUserId);
				Assignment assignment = new Assignment(bug, assignedUser, user, Constant.BUG_ASSIGN,
						new Timestamp(System.currentTimeMillis()));
				assignmentService.save(assignment);
			}

			for (FileItem item : items)
			{
				if (!item.isFormField() && !item.getName().isEmpty())
				{
					int type = fileSuffixService.getFileType(item.getName());
					if (type == FileSuffixService.TYPE_NONE) continue;

					int hashCode = item.hashCode();
					int d1 = hashCode & 0xff;
					int d2 = hashCode >> 8 & 0xff;
					String filePath = contextPath + uploadPath + d1 + "/" + d2;
					File file = new File(filePath);
					if (!file.isDirectory())
					{
						file.mkdirs();
					}
					String fileName = UUID.randomUUID().toString()
							+ item.getName().substring(item.getName().lastIndexOf("."));
					File saveFile = new File(file, fileName);
					System.out.println(saveFile.getAbsolutePath());

					InputStream is = item.getInputStream();
					FileOutputStream os = new FileOutputStream(saveFile);
					IOUtil.in2out(is, os);
					IOUtil.close(is, os);
					item.delete();

					UploadFile uf = new UploadFile();
					uf.setBug(bug);
					uf.setSavePath(d1 + "/" + d2 + "/" + fileName);
					uf.setIsImage(type == FileSuffixService.TYPE_IMAGE);
					uploadFileService.save(uf);

				}
			}

			ModelAndView mav = showAdd(request, session);
			mav.addObject("success", true);
			return mav;
		}
		catch (Exception e)
		{
			e.printStackTrace();
//			throw new RuntimeException(e);
			ModelAndView mav = showAdd(request, session);
			mav.addObject("failed", true);
			return mav;
		}

	}

}
