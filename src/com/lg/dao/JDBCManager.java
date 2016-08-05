package com.lg.dao;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class JDBCManager {
	private static JDBCManager sJDBCManager;

	private JDBCManager() {
	}

	/**
	 * 单例模式
	 * @return
	 */
	public static JDBCManager getInstance() {
		if (sJDBCManager == null) {
			synchronized (JDBCManager.class) {
				if (sJDBCManager == null) {
					sJDBCManager = new JDBCManager();
				}
			}
		}
		return sJDBCManager;
	}

	/**
	 * 使用tomcat的DBCP 
	 * @return 数据库连接
	 */
	public Connection getConncetion() {
		Connection conn = null;
		try {
			InitialContext context = new InitialContext();
			DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/travelsong_pool");
			conn = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
