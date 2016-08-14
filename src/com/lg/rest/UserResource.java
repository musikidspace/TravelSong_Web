package com.lg.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.lg.api.UserAPI;
import com.lg.api.comm.ClientContext;
import com.lg.api.comm.EasemobRestAPIFactory;
import com.lg.api.comm.body.IMUserBody;
import com.lg.api.impl.UserAPIImpl;
import com.lg.bean.ResultMsg;
import com.lg.bean.User;
import com.lg.dao.JDBCManager;

@Path("/user")
public class UserResource {

	/**
	 * 通过用户号密码获取用户（登录）
	 * @return
	 */
	@GET
	@Path("/codepwd")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(value = MediaType.APPLICATION_JSON)
	public String login(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		Map<String, String> mp = null;
		String usercode = null;
		String userpassword = null;
		//POST方法无法直接通过request获取参数，通过is读取
		try {
	    	BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));  
	    	String ins = "";
	    	String line;
			while ((line = in.readLine()) != null){
				ins += line;
			}
			String[] params = ins.split("&");
			mp = new HashMap<>();
			for (String param : params) {
				if (param.split("=").length > 1) {
					mp.put(param.split("=")[0], param.split("=")[1]);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(mp != null){
			usercode = mp.get("usercode");
			userpassword = mp.get("userpassword");
		}
		ResultMsg result = new ResultMsg();
		UserAPI userApi = new UserAPIImpl();
		result = (ResultMsg) userApi.getUsersByUserCodeAndPassword(usercode, userpassword);
		User user = result.getData();
		Connection conn = JDBCManager.getInstance().getConncetion();
		if (user != null) {
			// 生成cookie
			String cookieString = UUID.randomUUID().toString();
			Cookie cookie = new Cookie("verify", cookieString);
			response.addCookie(cookie);
			// 将cookie保存到数据库
			String dsql = "delete from cookies where usercode = '" + usercode + "'";
			String isql = "insert into cookies (usercode, cookieverify) values ('" + usercode + "', '" + cookieString
					+ "')";
			try {
				conn.setAutoCommit(false);
				Statement sm = conn.createStatement();
				sm.addBatch(dsql);
				sm.addBatch(isql);
				sm.executeBatch();
				conn.commit();
				try {
					if(sm != null){
						sm.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Gson gson = new Gson();
		return gson.toJson(result);
	}
	
	/**
	 * 新增一个用户（注册）
	 * @return
	 */
	@POST
	@Path("/codepwd")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(value = MediaType.APPLICATION_JSON)
	public String register(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		Map<String, String> mp = null;
		ResultMsg result = new ResultMsg();
		//初始化环信context
		EasemobRestAPIFactory factory = ClientContext.getInstance().init(ClientContext.INIT_FROM_PROPERTIES).getAPIFactory();
		UserAPI userApi = (UserAPI)factory.newInstance(EasemobRestAPIFactory.USER_CLASS);
		Gson gson = new Gson();
		PreparedStatement psm = null;
		ResultSet rs = null;
		String userstr = null;
		String userpe = null;
		String userpassword = null;
		boolean phoneFlag = true;
		//POST方法无法直接通过request获取参数，通过is读取
		try {
	    	BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));  
	    	String ins = "";
	    	String line;
			while ((line = in.readLine()) != null){
				ins += line;
			}
			String[] params = ins.split("&");
			mp = new HashMap<>();
			for (String param : params) {
				if (param.split("=").length > 1) {
					mp.put(param.split("=")[0], param.split("=")[1]);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(mp != null){
			userstr = mp.get("user");
			userpe = mp.get("usercode");
			userpassword = mp.get("userpassword");
		}
		if (userstr == null || userpe == null || userpassword == null) {
			result.setMsg("param is null");
			result.setSuccess(false);
			result.setSts("register");
			return gson.toJson(result);
		}
		if (userpe.contains("@")) {
			phoneFlag = false;
		}
		//注册
		String qsql = "select 1 from user where usercode = ?";
		Connection conn = JDBCManager.getInstance().getConncetion();
		try {
			// 判断用户名是否已存在
			psm = conn.prepareStatement(qsql);
			psm.setString(1, userstr);
			rs = psm.executeQuery();
			if (rs.next()) {
				result.setMsg("has the same usercode");
				result.setSuccess(false);
				result.setSts("register");
				return gson.toJson(result);
			}
			// 判断用户手机号或邮箱是否已存
			if (phoneFlag) {
				qsql = "select 1 from user where userphone = ?";
				psm = conn.prepareStatement(qsql);
				psm.setString(1, userpe);
				rs = psm.executeQuery();
				if (rs.next()) {
					result.setMsg("has the same userphone");
					result.setSuccess(false);
					result.setSts("register");
					try {
						if(psm != null){
							psm.close();
						}
						if(rs != null){
							rs.close();
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					return gson.toJson(result);
				}
			} else {
				qsql = "select 1 from user where useremail = ?";
				psm = conn.prepareStatement(qsql);
				psm.setString(1, userpe);
				rs = psm.executeQuery();
				if (rs.next()) {
					result.setMsg("has the same useremail");
					result.setSuccess(false);
					result.setSts("register");
					try {
						if(psm != null){
							psm.close();
						}
						if(rs != null){
							rs.close();
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					return gson.toJson(result);
				}
			}
			User user = new User();
			user.setUsercode(userstr);
			if (phoneFlag) {
				user.setUserphone(userpe);
			}else {
				user.setUseremail(userpe);
			}
			user.setUserpassword(userpassword);
			//注册用户
			result = (ResultMsg) userApi.createNewUserSingle(phoneFlag, user);
		}catch (Exception e){
			e.printStackTrace();
			result.setMsg(e.getMessage());
			result.setSuccess(false);
			result.setSts("register");
			try {
				if(psm != null){
					psm.close();
				}
				if(rs != null){
					rs.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return gson.toJson(result);
		}
		//登录
		// 生成cookie
		String cookieStr = UUID.randomUUID().toString();
		Cookie cookie = new Cookie("verify", cookieStr);
		response.addCookie(cookie);
		// 将cookie保存到数据库
		String dsql = "delete from cookies where usercode = '" + userstr + "'";
		String isql = "insert into cookies (usercode, cookieverify) values ('" + userstr + "', '" + cookieStr + "')";
		try {
			conn.setAutoCommit(false);
			Statement sm = conn.createStatement();
			sm.addBatch(dsql);
			sm.addBatch(isql);
			sm.executeBatch();
			conn.commit();
			try {
				if(sm != null){
					sm.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return gson.toJson(result);
	}
}
