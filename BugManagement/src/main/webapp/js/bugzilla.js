/**
 * 
 */
 
 
 function comment1(){

	if(form.comment.value== "")
	
		{
		alert("评论不能为空");
		form.comment.focus();
		return false;
		}
	
}

 function create(){

	if(create_bug.bug_name.value== "")
	
		{
		alert("bug名称不能为空");
		create_bug.bug_name.focus();
		return false;
		}
	if(create_bug.description.value== "")
	
		{
		alert("评论不能为空");
		create_bug.description.focus();
		return false;
		}
	
}
function check()
{
   if(form1.username.value=="")
   {
     alert("通行证用户名不能为空！");
     form1.username.focus();
     return false;
    }
    if(form1.password.value=="")
   {
     alert("登录密码不能为空！");
     form1.password.focus();
     return false;
    }
    if(form.projectname.value=="")
    {
      alert("项目名不能为空！");
      form.projectname.focus();
      return false;
     }
}

function checkManageProject()
{
    if(form.projectname.value=="")
    {
      alert("项目名不能为空！");
      form.projectname.focus();
      return false;
     }
}

function checkPersonManage()
{
   if(form1.add_new.value=="")
   {
     alert("新用户名不能为空！");
     form1.add_new.focus();
     return false;
    }
    if(form1.add_pw.value=="")
   {
     alert("登录密码不能为空！");
     form1.add_pw.focus();
     return false;
    }
    if(form1.add_mail.value=="")
    {
      alert("邮箱不能为空！");
      form1.add_mail.focus();
      return false;
     }
}
function checkPerson(){
	if(form2.username.value=="")
    {
      alert("用户名不能为空！");
      form2.username.focus();
      return false;
     }
}
function checkCreateProject()
{
   if(createProject.project_name.value=="")
   {
     alert("项目名不能为空！");
     createProject.project_name.focus();
     return false;
    }
    if(createProject.describe.value=="")
   {
     alert("描述不能为空！");
     createProject.describe.focus();
     return false;
    }
}


//var Itemindex = 0;
function showDiv() {
document.getElementById('popDiv').style.display = 'block';
document.getElementById('bg').style.display = 'block'; 
}
function closeDiv() {
document.getElementById('popDiv').style.display = 'none';
document.getElementById('bg').style.display = 'none';
}
