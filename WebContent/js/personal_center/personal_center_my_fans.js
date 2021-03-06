/**
 * 
 */
$(function(){
	listMyFansByPage(1);
});
function listMyFansByPage(pageIndex){
	var xhr=new XMLHttpRequest();
	var formData=new FormData();
	formData.append("myAttentionVO.pageIndex",pageIndex);
	xhr.open("POST","/wlmtxt/Works/Works_listMyAttentionVO");
	xhr.send(formData);
	xhr.onreadystatechange=function(){
		if(xhr.readyState==4 && xhr.status==200){
			var list_myFans=JSON.parse(xhr.responseText);
			for(var i=0;i<list_myFans.followDTO.length;i++){
				var list_myFans='<li class="user_list">';
				list_myFans+='<div class="user_list_img_div">';
				list_myFans+='<img class="user_img" src="/wlmtxt/Works/Works_getImg?imgName=' +list_myFans.followDTO[i].works.works_cover +'"/>';
				list_myFans+='</div>';
				list_myFans+='<div class="user_info">';
				list_myFans+='<div class="user_name">'+list_myFans.followDTO[i].user.user_username+'</div>';
				list_myFans+='<div class="user_operateing" onclick="withFocus(this.id)" id="'+list_myFans.followDTO[i].user.user_id+'">+ 关注</div>';
				list_myFans+='<div class="user_grjj">'+list_myFans.followDTO[i].user.user_bio+'</div>';
				list_myFans+='</div>';
				list_myFans+='</li>';
				$("#list_myFans").append(list_myFans);
				
			}
		}
	}
}

/*+关注*/
function withFocus(focus_user_id){
	console.log("focus_user_id:"+focus_user_id);
	var xhrhp=new XMLHttpRequest();
	xhrhp.open("POST","/wlmtxt/Works/Works_deleteMyWorks");
	var formData=new FormData();
	formData.append("accept_works.works_id",focus_user_id);
	xhrhp.send(formData);
	xhrhp.onreadystatechange=function(){
		if(xhrhp.readyState==4&&xhrhp.status==200){
			if(xhrhp.responseText=="1"){
				toastr.success("+关注成功！");
				listMyFansByPage(1);
			}else{
				toastr.error("+关注失败！");
				return false;
			}
		}
	}
}

//全部关注
function withFocusAll(){
	var xhrhttp=new XMLHttpRequest();
	xhrhttp.open("POST","/wlmtxt/Works/Works_deleteAllMyWorks");
	xhrhttp.send(null);
	xhrhttp.onreadystatechange=function(){
		if(xhrhttp.readyState==4&&xhrhttp.status==200){
			if(xhrhttp.responseText=="1"){
				toastr.success("全部关注成功！");
				listMyFansByPage(1);
			}else{
				toastr.error("全部关注失败！");
				return false;
			}
		}
	}
}
