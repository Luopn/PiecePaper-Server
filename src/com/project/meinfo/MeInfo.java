package com.project.meinfo;

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
 * Servlet implementation class MeInfo
 */
@WebServlet({ "/MeInfo", "/meinfo" })
public class MeInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int SUCCESS = 1;
	ArgClassForMe returnArg = new ArgClassForMe();
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
		ArgClassForMe arg = gson.fromJson(request.getReader(),
				ArgClassForMe.class);
		ResultSet resultSet;
		if (arg != null && conn != null) {
			int userId = arg.userId;
			String sql = "select name,userpic from UserInfo where id=?";
			try {
				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setInt(1, userId);
				resultSet = statement.executeQuery();
				PrintWriter out = response.getWriter();
				while (resultSet.next()) {
					returnArg.name = resultSet.getString(1);
					System.out.println("returnArg.name"+returnArg.name);
					returnArg.headPic = resultSet.getString(2);
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
}
