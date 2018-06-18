package com.wlmtxt.User.action;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.wlmtxt.User.service.UserService;
import com.wlmtxt.domain.DO.wlmtxt_user;

import util.JavaMail;
import util.JsonUtils;
import util.TeamUtil;
import util.md5;

public class UserAction extends ActionSupport {

	private UserService userService;
	private wlmtxt_user accpet_user;
	private String new_password;

	public String getNew_password() {
		return new_password;
	}

	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}

	public wlmtxt_user getAccpet_user() {
		return accpet_user;
	}

	public void setAccpet_user(wlmtxt_user accpet_user) {
		this.accpet_user = accpet_user;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * 登录<br />
	 * 1-成功<br />
	 * 2-失败
	 * @throws IOException 
	 */
	public void loign() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter pw = response.getWriter();
		wlmtxt_user loginResult = userService.login(accpet_user);
		if (loginResult != null) {
			ActionContext.getContext().getSession().put("loginResult", loginResult);
			pw.write("1");
		} else {
			pw.write("2");
		}
	}
	
	/**
	 * 退出登录,清空session<br>
	 * 1-成功<br>
	 * 2-失败<br>
	 * @throws IOException 
	 */
	public String logout() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter pw = response.getWriter();
		ActionContext.getContext().getSession().remove("loginResult");
		pw.write("1");
		return "skipToIndexPage";
	}
	
	/**
	 * 1-验证邮件发送成功<br />
	 * 2-验证邮件发送失败
	 * @throws IOException 
	 */
	public void sendRegisterMail() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		String href = "http://localhost:8080/wlmtxt/User/User_skipActivatePage?accpet_user.user_mail="+accpet_user.getUser_mail()+"&accpet_user.user_password="+md5.GetMD5Code(accpet_user.getUser_password())+"&accpet_user.user_username="+accpet_user.getUser_username();
//		String href = "http://localhost:8080/wlmtxt/User/User_skipActivatePage";
		//邮件内容
		String mailcontent = "<p><a href="+href+">register</a></p>";
		PrintWriter pw = response.getWriter();
		try {
			JavaMail.sendMail(mailcontent, accpet_user.getUser_mail());
			pw.write("1");
		} catch (Exception e) {
			pw.write("2");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return 跳转到激活页面
	 */
	public String skipActivatePage() {
		return "skipActivatePage";
	}
	
	/**
	 * 确认激活按钮
	 * <li>1-邮件激活成功
	 * <li>2-邮件激活失败
	 * @throws IOException 
	 */
	public void registerUser() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		String registerResult = userService.saveUser(accpet_user);
		PrintWriter pw =  response.getWriter();
		if ("1".equals(registerResult)) {
			pw.write("1");
		} else {
			//页面显示激活链接失效,重新注册
			pw.write("2");
		}
	}
	
	/**
	 * 
	 * 验证邮箱是否已注册<br>
	 * 1-未注册过<br />
	 * 2-注册过<br />
	 * @throws IOException 
	 */
	public void mailRegisted() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		wlmtxt_user registedUser= userService.mailRegisted(accpet_user);
		PrintWriter pw = response.getWriter();
		if (registedUser == null) {
			pw.write("1");
		} else {
			pw.write("2");
		}
	}
	
	/**
	 * 判断是否已经登录<br>
	 * 
	 * @throws IOException 
	 */
	public void isLogin() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		wlmtxt_user user = (wlmtxt_user) ActionContext.getContext().getSession().get("loginResult");
		PrintWriter pw = response.getWriter();
		if (user != null) {
			pw.write(JsonUtils.toJson(user));
		} else {
			pw.write("2");
		}
	}
	
	/**
	 * 修改个人资料<br>
	 * 
	 * 接收用户显式输入框的所有参数<br>
	 * 
	 * 1-修改失败<br>
	 * 2-修改成功
	 * @throws IOException 
	 * 
	 */
	public void modifyPersonalData() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter pw = response.getWriter();
		String user = userService.modifyPersonalData(accpet_user);
		if ("1".equals(user)) {
			pw.write("1");
		} else {
			pw.write("2");
		}
		
	}
	
	/**
	 * 登录后修改密码<br>
	 * 
	 * 接收参数，新密码:new_password<br>
	 * 
	 * 1-修改成功<br>
	 * 2-修改失败<br>
	 * 3-原密码错误，修改失败<br>
	 * @throws IOException 
	 */
	public void modifyPassword() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter pw = response.getWriter();
		wlmtxt_user user = (wlmtxt_user) ActionContext.getContext().getSession().get("loginResult");
		if (accpet_user.getUser_password().equals(user.getUser_password())) {
			user.setUser_password(new_password);
			String result = userService.modifyPassword(user);
			if ("1".equals(result)) {
				pw.write("1");
			} else {
				pw.write("2");
			}
		} else {
			pw.write("3");
		}
	}
	
	
	public String skipToModifyPasswordPage() {
		return "skipToModifyPasswordPage";
	}
	
	/**
	 * 得到用户头像
	 * @throws IOException 
	 * TODO 未测试
	 */
	public void getUserAvatar() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		wlmtxt_user user = (wlmtxt_user) ActionContext.getContext().getSession().get("loginResult");
		//头像后缀名
		String suffix = user.getUser_avatar().substring((user.getUser_avatar().indexOf(".")+1));
		//头像位置
		String filename = "c:/wlmtxt/avatar/" + user.getUser_avatar();
		if ("png".equals(suffix)) {
			response.setContentType("image/png");
		} else if ("jpg".equals(suffix)) {
			response.setContentType("image/jpeg");
		} else if ("jpeg".equals(suffix)) {
			response.setContentType("image/jpeg");
		} else if ("gif".equals(suffix)) {
			response.setContentType("image/gif");
		} else if ("bmp".equals(suffix)) {
			response.setContentType("image/bmp");
		} else if ("tiff".equals(suffix)) {
			response.setContentType("image/tiff");
		} else {
			response.setContentType("image/jpeg");
		}
		response.setHeader("Content-Disposition", "attachment;filename="+filename);
		OutputStream os = response.getOutputStream();
		InputStream is = new FileInputStream(filename);
		byte[] buffer = new byte[1024];
		int i = -1;
		while ((i = is.read(buffer)) != -1) {
		    os.write(buffer, 0, i);
		}
		os.flush();
		os.close();
		is.close();
	}
	/**
	 * 关注用户
	 * TODO
	 */
	public void 	followUser() {
		
	}
	
	public String skipToIndexPage() {
		return "skipToIndexPage";
	}
	/**
	 * 跳转到分类页
	 * @return
	 */
	public String skipToCategoryPage() {
		return "skipToCategoryPage";
	}
	/**
	 * 跳转到排行榜页面
	 * @return
	 */
	public String skipToRankPage() {
		return "skipToRankPage";
	}
	/**
	 * 跳转到发布作品页面
	 * @return
	 */
	public String skipToPublishWorksPage() {
		return "skipToPublishWorksPage";
	}
	/**
	 * 跳转到个人中心页面
	 * @return
	 *//*
	public String skipToPersonalCenterPage() {
		return "skipToMyDynamicPage";
	}*/
	/**
	 * 跳转到我的动态页面
	 * @return
	 */
	public String skipToMyDynamicPage() {
		return "skipToMyDynamicPage";
	}
	/**
	 * 跳转到个人资料页
	 * @return
	 */
	public String skipToPersonalDataPage() {
		return "skipToPersonalDataPage";
	}
	/**
	 * 跳转到我的关注页面
	 * @return
	 */
	public String skipToMyAttentionPage() {
		return "skipToMyAttentionPage";
	}
	/**
	 * 跳转到我的粉丝页面
	 * @return
	 */
	public String skipToMyFansPage() {
		return "skipToMyFansPage";
	}
	/**
	 *跳转到播放历史页面
	 * @return
	 */
	public String skipToWatchHistoryPage() {
		return "skipToWatchHistoryPage";
	}
	/**
	 *跳转到与我相关之我的收藏页面
	 * @return
	 */
	public String skipToRelationCollectionPage() {
		return "skipToRelationCollectionPage";
	}
	/**
	 *跳转到与我相关之我的点赞页面
	 * @return
	 */
	public String skipToRelationAppreciatesPage() {
		return "skipToRelationAppreciatesPage";
	}
	/**
	 *跳转到与我相关之我的评论页面
	 * @return
	 */
	public String skipToRelationCommentsPage() {
		return "skipToRelationCommentsPage";
	}
	/**
	 * 跳转到消息中心之点赞通知页
	 * @return
	 */
	public String skipToAppreciatesNoticePage() {
		return "skipToAppreciatesNoticePage";
	}
	/**
	 * 跳转到消息中心之收藏通知页
	 * @return
	 */
	public String skipToCollectionNoticePage() {
		return "skipToCollectionNoticePage";
	}
	/**
	 * 跳转到消息中心之评论通知页
	 * @return
	 */
	public String skipToCommentsNoticePage() {
		return "skipToCommentsNoticePage";
	}
	/**
	 * 跳转到消息中心之审核通知页
	 * @return
	 */
	public String skipToAuditNoticePage() {
		return "skipToAuditNoticePage";
	}
}
