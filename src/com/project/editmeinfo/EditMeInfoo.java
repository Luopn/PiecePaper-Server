package com.project.editmeinfo;

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
import com.project.editmsg.MsgArgClass;
import com.project.utils.DbUtil;

/**
 * Servlet implementation class EditMsgInfoo
 */
@WebServlet({ "/EditMeInfoo", "/editmeinfoo" })
public class EditMeInfoo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int SUCCESS = 1;
	private Gson gson = new Gson();

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		EditMeArgClass arg = gson.fromJson(request.getReader(),
				EditMeArgClass.class);
		;
		Connection conn = DbUtil.getConn();
		int result = -1;
		if (arg != null && conn != null) {
			String picPath = arg.headPicPath;
			System.out.println(picPath);
			int userid = arg.userId;
			System.out.println(userid + "");
			String sql = "update userinfo set userpic=? where id=?";
			try {
				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setString(1, picPath);
				statement.setInt(2, userid);
				result = statement.executeUpdate();
				System.out.println("成功");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (result > 0) {
				MsgArgClass returnArg = new MsgArgClass();
				returnArg.resultCode = SUCCESS;
				returnArg.act = "editmeinfoo";
				String returnJson = gson.toJson(returnArg);
				PrintWriter pwout = response.getWriter();
				pwout.write(returnJson);
			}
		} else {
			System.out.println("连接数据库失败");
		}
	}

}
