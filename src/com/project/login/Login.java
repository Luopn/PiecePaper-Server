package com.project.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.project.register.ArgClass;
import com.project.utils.DbUtil;

/**
 * Servlet implementation class Login
 */
@WebServlet({ "/Login", "/login" })
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final int SUCCESS = 1;
	private static final int FAIL = 0;
	ArgClass returnArg = new ArgClass();
	Gson gson = new Gson();
	Connection conn = DbUtil.getConn();
	BufferedReader br;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		br = request.getReader();
		ArgClass arg = gson.fromJson(br, ArgClass.class);
		ResultSet resultSet;
		if (arg != null && arg.act.equalsIgnoreCase("login") && conn != null) {
			String name = arg.name;
			String passcode = arg.passcode;
			String sql = "select id,name,passcode from UserInfo";
			try {
				PreparedStatement statement = conn.prepareStatement(sql);
				resultSet = statement.executeQuery();
				ArgClass returnArg = new ArgClass();
				PrintWriter out = response.getWriter();
				while (resultSet.next()) {
					if (resultSet.getString(2).equals(name) & resultSet.getString(3).equals(passcode)) {
						System.out.println(resultSet.getString(2));
						System.out.println(resultSet.getString(3));
						returnArg.act = "login";
						returnArg.userid = resultSet.getInt(1);
						returnArg.resultCode = SUCCESS;
						break;
					} else {
						returnArg.act = "login";
						returnArg.resultCode = FAIL;
					}
				}
				String returnJson = gson.toJson(returnArg);
				out.write(returnJson);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("连接数据库失败");
		}
	}
		
}
