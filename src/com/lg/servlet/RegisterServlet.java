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
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		String user = request.getParameter("user");
		String userpe = request.getParameter("usercode");
		String userpassword = request.getParameter("userpassword");
		PreparedStatement psm;
		ResultSet rs;
		boolean phoneFlag = true;
		if (user == null || userpe == null || userpassword == null) {
			pw.append("{\"data\":{\"msg\":\"").append("param is null")
					.append("\"},\"success\":false,\"sts\":\"register\"}");
			return;
		}
		if (userpe.contains("@")) {
			phoneFlag = false;
		}

		String qsql = "select 1 from user where usercode = ?";
		Connection conn = JDBCManager.getInstance().getConncetion();
		try {
			// 判断用户名是否已存在
			psm = conn.prepareStatement(qsql);
			psm.setString(1, user);
			rs = psm.executeQuery();
			if (rs.next()) {
				pw.append("{\"data\":{\"msg\":\"").append("has the same usercode")
						.append("\"},\"success\":false,\"sts\":\"register\"}");
				return;
			}
			// 判断用户手机号或邮箱是否已存
			if (phoneFlag) {
				qsql = "select 1 from user where userphone = ?";
				psm = conn.prepareStatement(qsql);
				psm.setString(1, userpe);
				rs = psm.executeQuery();
				if (rs.next()) {
					pw.append("{\"data\":{\"msg\":\"").append("has the same userphone")
							.append("\"},\"success\":false,\"sts\":\"register\"}");
					return;
				}
			} else {
				qsql = "select 1 from user where useremail = ?";
				psm = conn.prepareStatement(qsql);
				psm.setString(1, userpe);
				rs = psm.executeQuery();
				if (rs.next()) {
					pw.append("{\"data\":{\"msg\":\"").append("has the same useremail")
							.append("\"},\"success\":false,\"sts\":\"register\"}");
					return;
				}
			}

			if (phoneFlag) {
				qsql = "insert into user (usercode, userphone, userpassword) values (?, ? ,?)";
			} else {
				qsql = "insert into user (usercode, useremail, userpassword) values (?, ? ,?)";
			}
			psm = conn.prepareStatement(qsql);
			psm.setString(1, user);
			psm.setString(2, userpe);
			psm.setString(3, userpassword);
			int result = psm.executeUpdate();
			if (result == 0) {
				pw.append("{\"data\":{\"msg\":\"").append("register failed")
						.append("\"},\"success\":false,\"sts\":\"register\"}");
				return;
			}
		} catch (Exception e) {
			pw.append("{\"data\":{\"msg\":\"").append(e.getMessage())
					.append("\"},\"success\":false,\"sts\":\"register\"}");
			return;
		}
		// 生成cookie
		String cookieString = UUID.randomUUID().toString();
		Cookie cookie = new Cookie("verify", cookieString);
		response.addCookie(cookie);
		// 将cookie保存到数据库
		String dsql = "delete from cookies where usercode = '" + user + "'";
		String isql = "insert into cookies (usercode, cookieverify) values ('" + user + "', '" + cookieString + "')";
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
		pw.append("{\"data\":{").append("\"usercode\":\"").append(user);
		if (userpe.contains("@")) {
			pw.append("\",\"useremail\":\"").append(userpe);
		} else {
			pw.append("\",\"userphone\":\"").append(userpe);
		}
		pw.append("\"}").append(",\"success\":true,\"sts\":\"register\"}");
		try {
			psm.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
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
