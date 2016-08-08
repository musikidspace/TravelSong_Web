package com.lg.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.lg.dao.JDBCManager;

/**
 * Servlet Filter implementation class CookiesCheckingFilter
 */
@WebFilter("/CookiesCheckingFilter")
public class CookieCheckingFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public CookieCheckingFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		PrintWriter pw = response.getWriter();
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String cookie = httpRequest.getHeader("Cookie");
		// 如果头文件中没有cookie，需重新登录
		if (cookie == null || cookie.equals("null") || cookie.equals("")) {
			pw.append("{\"data\":{\"msg\":\"cookie is null\"},\"success\":false,\"sts\":\"checking\"}");
			return;
		}
		String cookieverify = null;
		String usercode = httpRequest.getParameter("usercode");
		try {
			String sql = "select cookieverify from cookies where usercode = ?";
			Connection conn = JDBCManager.getInstance().getConncetion();
			if (conn != null) {
				PreparedStatement psm = conn.prepareStatement(sql);
				psm.setString(1, usercode);
				ResultSet rs = psm.executeQuery();
				while (rs.next()) {
					cookieverify = rs.getString(1);
				}
				rs.close();
				psm.close();
			}
		} catch (Exception e) {
			pw.append("{\"data\":{\"msg\":\"")
			.append(e.getMessage())
			.append("\"},\"success\":false,\"sts\":\"checking\"}");
			return;
		}
		// 本地有，数据库中没有cookie信息，已退出登录（未清除本地cookie）
		if (cookieverify == null || cookieverify.equals("null") || cookieverify.equals("")) {
			pw.append("{\"data\":{\"msg\":\"remote cookie is null\"},\"success\":false,\"sts\":\"checking\"}");
			return;
		}
		if (!cookie.equals(cookieverify)) {
			pw.append("{\"data\":{\"msg\":\"the two cookies are different\"},\"success\":false,\"sts\":\"checking\"}");
			return;
		}
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
