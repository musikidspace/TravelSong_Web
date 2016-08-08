package com.lg.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lg.bean.User;
import com.lg.dao.JDBCManager;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		ArrayList<User> users;
		String usercode = request.getParameter("usercode");
		String userpassword = request.getParameter("userpassword");
		String sql = "select usercode, username, userphone, useremail, usergender, userhead,"
				+ " userlvl, userbirthday, userlocation, userdesc, userplatform, userpermission"
				+ " from user where userstatus <> 0 and userpassword = ?"
				+ " and (usercode = ? or userphone = ? or useremail = ?)";
		Connection conn = JDBCManager.getInstance().getConncetion();
		try {
			PreparedStatement psm = conn.prepareStatement(sql);
			psm.setString(1, userpassword);
			psm.setString(2, usercode);
			psm.setString(3, usercode);
			psm.setString(4, usercode);
			
			ResultSet rs = psm.executeQuery();
			users = new ArrayList<>();
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
			pw.append("{\"data\":{\"msg:\"")
			.append(e.getMessage())
			.append("\"},\"success\":false,\"sts\":\"login\"}");
			return;
		}
		if(!users.isEmpty()){
			//生成cookie
			String cookieString = UUID.randomUUID().toString();
			Cookie cookie = new Cookie("verify", cookieString);
			response.addCookie(cookie);
			//将cookie保存到数据库
			String dsql = "delete from cookies where usercode = '" + usercode +"'";
			String isql = "insert into cookies (usercode, cookieverify) values ('" + usercode + "', '" + cookieString + "')";
			try {
				conn.setAutoCommit(false);
				Statement sm = conn.createStatement();
				sm.addBatch(dsql);
				sm.addBatch(isql);
				sm.executeBatch();
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			pw.append("{\"data\":{")
			.append("\"usercode\":\"")
			.append(users.get(0).getUsercode())
			.append("\",\"username\":\"")
			.append(users.get(0).getUsername())
			.append("\",\"userphone\":\"")
			.append(users.get(0).getUserphone())
			.append("\",\"useremail\":\"")
			.append(users.get(0).getUseremail())
			.append("\",\"usergender\":")
			.append(users.get(0).getUsergender() + "")
			.append(",\"userhead\":\"")
			.append(users.get(0).getUserhead())
			.append("\",\"userlvl\":")
			.append(users.get(0).getUserlvl() + "")
			.append(",\"userbirthday\":\"")
			.append(users.get(0).getUserbirthday())
			.append("\",\"userlocation\":\"")
			.append(users.get(0).getUserlocation())
			.append("\",\"userdesc\":\"")
			.append(users.get(0).getUserdesc())
			.append("\",\"userplatform\":\"")
			.append(users.get(0).getUserplatform())
			.append("\",\"userpermission\":")
			.append(users.get(0).getUserpermission() + "}")
			.append(",\"success\":true,\"sts\":\"login\"}");
		} else {
			pw.append("{\"data\":{")
			.append("\"msg\":\"null")
			.append("\"},\"success\":false,\"sts\":\"login\"}");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
