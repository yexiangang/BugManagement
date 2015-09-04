package com.my.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;

import com.my.domain.Assignment;
import com.my.domain.Bug;
import com.my.domain.Comment;
import com.my.domain.Member;
import com.my.domain.Project;
import com.my.domain.UploadFile;
import com.my.domain.User;
import com.my.service.BugService;
import com.my.service.CommentService;
import com.my.service.FileSuffixService;
import com.my.service.ProjectService;
import com.my.service.UploadFileService;
import com.my.service.UserService;
import com.my.util.Constant;
import com.my.util.IOUtil;
import com.my.util.Page;
import com.sun.net.httpserver.HttpsParameters;

@Controller
public class MyBugController {

	@Autowired
	private BugService bugService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private UserService userService;
	@Autowired
	private UploadFileService uploadFileService;
	@Autowired
	private FileSuffixService fileSuffixService;
	@Autowired
	CommentService commentService;
	
	@RequestMapping(value="mybug.do")
	private ModelAndView getMyBug(HttpServletRequest request, HttpSession session){
		
		if (session.getAttribute("bug_manage") == null) {
			return new ModelAndView("main");
		}
		
		
		ModelAndView modelAndView =new ModelAndView("mybug");
		User user=(User)session.getAttribute("online_user");
		System.out.println(user.getUserId());
		List<Object[]> show = new ArrayList<Object[]>();
		List<Object> list = bugService.show(user);
		for (Object object : list) {
			show.add((Object[])object);
		}
		System.out.println(show.size());
		String page = request.getParameter("page");
		int curPage = (page == null) ? 1 : Integer.parseInt(page);
		Page<Object[]> pages = new Page<Object[]>(curPage, Constant.PAGE_SIZE, show);
		int total_project = (int) pages.getItemCount();
		int total_page = pages.getPageCount();
		List<Object[]> bugs=pages.getCurItems();
		modelAndView.addObject("bugs", bugs);
		modelAndView.addObject("role", "developer");
		modelAndView.addObject("current_page", curPage);
		modelAndView.addObject("page", pages);
		return modelAndView;
	}
	
	
	@RequestMapping(value="edit.do",params="delect=删除")
	private ModelAndView delectMyBug(HttpServletRequest request, HttpSession session){
		
		String id = request.getParameter("bugId");
		Bug bug = bugService.findById(Integer.parseInt(id));
		String  hql ="from UploadFile where bug.bugId=?";
		String[] str={id};
		List<Object> findByHql = uploadFileService.findByHql(hql, str);
		for (Object object : findByHql) {
			uploadFileService.delect((UploadFile) object);
		}
		boolean del = bugService.delete(bug);
		if(del){
			request.setAttribute("tip", "删除成功");
		}else{
			request.setAttribute("tip", "删除失败");
		}
		return getMyBug(request, session);
	}
	
	
	@RequestMapping(value="edit.do")
	private ModelAndView getEditBug(HttpServletRequest request, HttpSession session){
		
		if (session.getAttribute("bug_manage") == null) {
			return new ModelAndView("main");
		}
		String id = request.getParameter("bugId");
		User usera=(User)session.getAttribute("online_user");
		String userRole =usera.getRole();
		
		if(userRole.equals(Constant.ROLE_DEVELOPER)){
			
			request.setAttribute("role", 4);
		}else{
			request.setAttribute("role", 1);
		}
		int bugId = Integer.parseInt(id);
		Bug bug = bugService.findById(bugId);
		
		Project project =projectService.findProjectById(bug.getProject().getProjectId());
		@SuppressWarnings("unchecked")
		Set<Member> members = project.getMembers();
		User leader = project.getUserByLeader();
		Member member = new Member();
		member.setUser(leader);
		members.add(member);

		ModelAndView modelAndView =new ModelAndView("bug_edit");
		session.setAttribute("bugId", bug.getBugId());
		request.setAttribute("bug", bug);
		
		String hql2 ="select savePath,isImage from UploadFile where bug.bugId=?";
		String[] str2={id};
		List<Object> findByHql = uploadFileService.findByHql(hql2, str2);
		//String contextPath = request.getServletContext().getRealPath("/WEB-INF/");
		List<String> urls =new ArrayList<String>();
		if(findByHql.size()!=0){
			for (Object object : findByHql) {
				Object[] ob=(Object[]) object;
				if((Boolean)ob[1]==true){
					urls.add("/WEB-INF/upload/"+ob[0].toString());
				}
			}
		}
		
		modelAndView.addObject("urls", urls);
//		System.out.println("method#####################################################");
//		for (String ob : urls) {
//			System.out.println(ob);
//		}
//		System.out.println(findByHql.size()+"  " +contextPath);
//		System.out.println("method#####################################################");
		String idStr = id;
		String referIdStr = request.getParameter("refer_id");
		String curPageStr = request.getParameter("page");
		Integer ids = (idStr == null || idStr.isEmpty()) ? null : Integer.parseInt(idStr);
		Integer curPage = (curPageStr == null || curPageStr.isEmpty()) ? 1 : Integer.parseInt(curPageStr);
		
		if (ids == null)
		{
			
			return null;
		}
		
		if (referIdStr != null && !referIdStr.isEmpty())
		{
			int referId = Integer.parseInt(referIdStr);
			modelAndView.addObject("refer_comment", commentService.findById(referId));
		}
		
		Bug bugs = bugService.findById(ids);
		modelAndView.addObject("bug", bugs);
		modelAndView.addObject("members", members);
		Page<Comment> page = commentService.findByBugId(ids, curPage, Constant.PAGE_SIZE);
		if (page != null)
		{
			modelAndView.addObject("page", page);
		}
		
		return modelAndView;
	}	
	
	

	
	
	@RequestMapping(value="edited.do",method = RequestMethod.POST)
	private ModelAndView postEidtBug(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		
		if (session.getAttribute("bug_manage") == null) {
			return new ModelAndView("main");
		}
		int bugId = (Integer) session.getAttribute("bugId");
		String name =null;
		String description=null;
		String projdectName=null;
		String creatorName=null;
		int message=0;
		Map<String, String> bugMap = new HashMap<String, String>();
		DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
		ServletFileUpload servletFileUpload=null;
		Bug bug =bugService.findById(bugId);
		
		try {
			String contextPath = request.getServletContext().getRealPath("/WEB-INF/");
			String uploadPath = "upload/";
			String tmpPath = "tmp/";
			
			fileItemFactory.setSizeThreshold(1024 * 1024);
			fileItemFactory.setRepository(new File(contextPath + tmpPath));
			servletFileUpload = new ServletFileUpload(fileItemFactory);
			List<FileItem> items = servletFileUpload.parseRequest(request);
			
			for (FileItem item : items)
			{
				if (item.isFormField())
				{	
					bugMap.put(item.getFieldName(), item.getString("utf-8"));
					System.out.println(item.getFieldName()+"   "+item.getString("utf-8"));
				}
			}
			name = bugMap.get("tittle");
			description =bugMap.get("description");
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
			
			if(!(description==null||description.trim().equals(""))){
				
				bug.setDescription(description);
			}
			
			if(!(name==null||name.trim().equals(""))){
				bug.setName(name);
			}
			bugService.updata(bug);
			message= 1;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		String bugIdStr=null;
	
		try {
			bugIdStr = session.getAttribute("bugId") + "";
			String content = bugMap.get("content");
			String referIdStr = bugMap.get("refer_id");
			projdectName = bugMap.get("projdectName");
			creatorName = bugMap.get("creatorName");
			if (bugIdStr == null || bugIdStr.isEmpty() || content == null || content.isEmpty() || content.equals("")) {
				if (message == 1) {
					message = 2;
				} else {
					message = 3;
				}

			} else {
				Comment comment = new Comment();
				comment.setBug(bug);
				if (referIdStr != null && !referIdStr.isEmpty()) {
					Comment referComment = new Comment();
					referComment.setCommentId(Integer.parseInt(referIdStr));
					comment.setReferComment(referComment);
				}
				comment.setPubTime(new Timestamp(System.currentTimeMillis()));
				comment.setContent(content);
				comment.setUser((User) session.getAttribute("online_user"));
				commentService.save(comment);
				if (message == 1) {
					message = 4;
				} else {
					message = 5;
				}
			} 
		} finally {
			try {
				response.sendRedirect("edit.do?bugId="+bugIdStr+"&projectName="+projdectName+"&userName="+creatorName+"&tip="+message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
