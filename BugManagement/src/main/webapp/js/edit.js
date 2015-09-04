function checksss() {
	var name = document.getElementById("tittle");
	var btn = document.getElementById("submit");
	var tip = document.getElementById("tip");
	if (name.value=="") {
		btn.disabled=true;
		tip.innerHTML = "bug标题不能为空";
		return true;
	}else{
		btn.disabled=false;
		tip.innerHTML = "";
	}
}