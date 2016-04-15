package com.project.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtil {
	static Connection conn = null;

	public static Connection getConn() {
		if (conn == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager
						.getConnection("jdbc:mysql://localhost:3306/lplf320?"
								+ "user=lplf320&password=778179&useUnicode=true&characterEncoding=UTF8");
//				Class.forName("oracle.jdbc.driver.OracleDriver");
//				conn = DriverManager.getConnection(
//						"jdbc:oracle:thin:@//localhost:1521/orcl", "lplf320",
//						"778179");
			} catch (Exception e) {
				System.out.println("数据库连接异常");
				e.printStackTrace();
			}
		}
		return conn;
	}
}
