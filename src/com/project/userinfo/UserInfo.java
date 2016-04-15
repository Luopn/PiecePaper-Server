package com.project.userinfo;

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
import com.project.utils.DbUtil;

/**
 * Servlet implementation class UserInfo
 */
@WebServlet({ "/UserInfo", "/userinfo" })
public class UserInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final int SUCCESS = 1;
	ArgClass returnArg = new ArgClass();
	Gson gson = new Gson();
	Connection conn = DbUtil.getConn();

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		ArgClass arg = gson.fromJson(request.getReader(), ArgClass.class);
		ResultSet resultSet;
		if (arg != null && conn != null) {
			int userid = arg.userid;
			String sql = "select name,userpic,regdate from UserInfo where id=?";
			try {
				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setInt(1, userid);
				resultSet = statement.executeQuery();
				ArgClass returnArg = new ArgClass();
				PrintWriter out = response.getWriter();
				while (resultSet.next()) {
					returnArg.userName = resultSet.getString(1);
					returnArg.headPic = resultSet.getString(2);
					returnArg.regdate = resultSet.getString(3);
					returnArg.resultCode = SUCCESS;
					break;
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

	class ArgClass {
		int userid, resultCode;
		String headPic, userName, regdate;
	}

}
