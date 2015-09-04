function validate() {
	var name = document.getElementById("name");
	var pwd = document.getElementById("password");
	var pwd2 = document.getElementById("password2");
	var email = document.getElementById("email");

	var tip = document.getElementById("tip");
	var submit = document.getElementById("submit");
	
	if (name.value.length < 6 || name.value.length > 15) {
		tip.innerHTML = "用户名无效";
		submit.disabled = true;
		return;
	}
	
	if (pwd.value.length < 6 || pwd.value.length > 15) {
		tip.innerHTML = "密码无效";
		submit.disabled = true;
		return;
	}
	
	if (pwd2.value != pwd.value) {
		tip.innerHTML = "密码不一致";
		submit.disabled = true;
		return;
	}
	
	if (checkEmail(email.value)) {
		tip.innerHTML = "邮箱无效";
		submit.disabled = true;
		return;
	}
	
	tip.innerHTML = "";
	submit.disabled = false;
}

function checkEmail(email) {
	var sReg   =   /[_a-zA-Z\d\-\.]+@[_a-zA-Z\d\-]+(\.[_a-zA-Z\d\-]+)+$/;   
	return !sReg.test(email);   
}
