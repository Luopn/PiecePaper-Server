package com.project.register;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.project.utils.DbUtil;

/**
 * 用户基本信息接收类
 */
@WebServlet({ "/Register", "/register" })
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int SUCCESS = 1;
	ArgClass returnArg = new ArgClass();
	Gson gson = new Gson();
	Connection conn = DbUtil.getConn();

	public Register() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		int result = -1;
		ArgClass arg = gson.fromJson(request.getReader(), ArgClass.class);
		if (arg != null && arg.act.equalsIgnoreCase("register") && conn != null) {
			String name = arg.name;
			System.out.println(arg.name);
			String passcode = arg.passcode;
			System.out.println(arg.passcode);
			String regdate = String.valueOf(System.currentTimeMillis());
//			String sql = "insert into UserInfo(id,name,passcode,userpic,regdate) "
//					+ "values (seq_userinfo_id.nextVal,?,?,?,?)";
			String sql = "insert into UserInfo(name,passcode,userpic,regdate) "
					+ "values (?,?,?,?)";
			try {
				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setString(1, name);
				statement.setString(2, passcode);
				statement.setString(3, "/images/userhead/head_default.png");
				statement.setString(4, regdate);
				result = statement.executeUpdate();
				System.out.println("成功");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (result > 0) {
				returnArg.act = "register";
				returnArg.resultCode = SUCCESS;
				String returnJson = gson.toJson(returnArg);
				PrintWriter out = response.getWriter();
				out.write(returnJson);
			}
		} else {
			System.out.println("连接数据库失败");
		}

	}
}
