package com.lg.api.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.lg.api.UserAPI;
import com.lg.api.comm.BodyWrapper;
import com.lg.api.comm.EasemobRestAPI;
import com.lg.api.comm.HTTPMethod;
import com.lg.api.comm.HeaderHelper;
import com.lg.api.comm.HeaderWrapper;
import com.lg.api.comm.ResponseWrapper;
import com.lg.api.comm.body.IMUserBody;
import com.lg.bean.ResultMsg;
import com.lg.bean.User;
import com.lg.dao.JDBCManager;

public class UserAPIImpl extends EasemobRestAPI implements UserAPI {
	private static final String ROOT_URI = "/users";
	
	@Override
	public String getResourceRootURI() {
		return ROOT_URI;
	}
	
	@Override
	public Object createNewUserSingle(boolean phoneFlag, Object payload) {
		User user = (User) payload;
		ResultMsg result = new ResultMsg();
		String sql;
		PreparedStatement psm = null;
		try {
			Connection conn = JDBCManager.getInstance().getConncetion();
			if (phoneFlag) {
				sql = "insert into user (usercode, userphone, userpassword) values (?, ? ,?)";
				psm = conn.prepareStatement(sql);
				psm.setString(2, user.getUserphone());
			} else {
				sql = "insert into user (usercode, useremail, userpassword) values (?, ? ,?)";
				psm = conn.prepareStatement(sql);
				psm.setString(2, user.getUseremail());
			}
			psm.setString(1, user.getUsercode());
			psm.setString(3, user.getUserpassword());
			int qresult = psm.executeUpdate();
			if (qresult == 0) {
				result.setMsg("register failed");
				result.setSuccess(false);
				result.setSts("register");
				try {
					if(psm != null){
						psm.close();
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			result.setMsg(e.getMessage());
			result.setSuccess(false);
			result.setSts("register");
			try {
				if(psm != null){
					psm.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return result;
		}
		
		result.setData(user);
		result.setSuccess(true);
		result.setSts("register");
		//用户环信注册
		IMUserBody userBody = new IMUserBody(user.getUsercode(), user.getUserpassword());
		String url = getContext().getSeriveURL() + getResourceRootURI();
		BodyWrapper body = (BodyWrapper) userBody;
		HeaderWrapper header = HeaderHelper.getDefaultHeaderWithToken();
		ResponseWrapper responseWrapper = getInvoker().sendRequest(HTTPMethod.METHOD_POST, url, header, body, null);
		System.out.println(responseWrapper.toString());
		try {
			if(psm != null){
				psm.close();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return result;
	}

	@Override
	public Object getUsersByUserCode(String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getUsersByUserCodeAndPassword(String userCode, String userPassword) {
		ResultMsg result = new ResultMsg();
		ArrayList<User> users = new ArrayList<>();
		String usercode = userCode;
		String userpassword = userPassword;
		PreparedStatement psm = null;
		ResultSet rs = null;
		String sql = "select usercode, username, userphone, useremail, usergender, userhead,"
				+ " userlvl, userbirthday, userlocation, userdesc, userplatform, userpermission"
				+ " from user where userstatus <> 0 and userpassword = ? collate utf8_bin"
				+ " and (usercode = ? collate utf8_bin or userphone = ? or useremail = ? collate utf8_bin)";
		Connection conn = JDBCManager.getInstance().getConncetion();
		try {
			psm = conn.prepareStatement(sql);
			psm.setString(1, userpassword);
			psm.setString(2, usercode);
			psm.setString(3, usercode);
			psm.setString(4, usercode);
			
			rs = psm.executeQuery();
			User user;
			while (rs.next()) {
				user = new User();
				user.setUsercode(rs.getString(1));
				user.setUsername(rs.getString(2));
				user.setUserphone(rs.getString(3));
				user.setUseremail(rs.getString(4));
				user.setUsergender(rs.getInt(5));
				user.setUserhead(rs.getString(6));
				user.setUserlvl(rs.getInt(7));
				user.setUserbirthday(rs.getString(8));
				user.setUserlocation(rs.getString(9));
				user.setUserdesc(rs.getString(10));
				user.setUserplatform(rs.getString(11));
				user.setUserpermission(rs.getInt(12));
				users.add(user);
			}
		} catch (Exception e) {
			result.setData(null);
			result.setMsg(e.getMessage());
			result.setSuccess(false);
			result.setSts("login");
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
			return result;
		}
		if(!users.isEmpty()){
			result.setData(users.get(0));
			result.setMsg(null);
			result.setSuccess(true);
			result.setSts("login");
		} else {
			result.setData(null);
			result.setMsg("null");
			result.setSuccess(false);
			result.setSts("login");
		}
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
		return result;
	}

	@Override
	public Object getUsersBatch(Long limit, String cursor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object deleteUserByUserCode(String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object modifyUserPassword(String userCode, Object payload) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object modifyUserInfo(String userName, Object payload) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object addFriendSingle(String userCode, String friendCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object deleteFriendSingle(String userCode, String friendCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getFriends(String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getBlackList(String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object addToBlackList(String userCode, Object payload) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object removeFromBlackList(String userCode, String blackListCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object deactivateUser(String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object activateUser(String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object disconnectUser(String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getUserAllChatGroups(String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
