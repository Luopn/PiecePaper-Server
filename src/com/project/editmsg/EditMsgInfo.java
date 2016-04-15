package com.project.editmsg;

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
 * Servlet implementation class EditMsgInfo
 */
@WebServlet({ "/EditMsgInfo", "/editmsginfo" })
public class EditMsgInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");

		Gson gson = new Gson();
		MsgArgClass arg = gson.fromJson(request.getReader(), MsgArgClass.class);
		final int SUCCESS = 1;
		Connection conn = DbUtil.getConn();
		int result = -1;
		if (arg != null && arg.act.equalsIgnoreCase("msginfo") && conn != null) {
			String content = arg.msgcontent;
			System.out.println(content);
			String picPath = arg.picPath;
			System.out.println(picPath);
			int userid = arg.userid;
			System.out.println(userid + "");
			String regdate = String.valueOf(System.currentTimeMillis());
//			String sql = "insert into allmsg(id,msgcontent,userid,picpath,regdate) "
//					+ "values (seq_userinfo_id.nextVal,?,?,?,?)";
			String sql = "insert into allmsg(msgcontent,userid,picpath,regdate) "
					+ "values (?,?,?,?)";
			try {
				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setString(1, content);
				statement.setInt(2, userid);
				statement.setString(3, picPath);
				statement.setString(4, regdate);
				result = statement.executeUpdate();
				System.out.println("成功");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (result > 0) {
				MsgArgClass returnArg = new MsgArgClass();
				returnArg.act = "msgsend";
				returnArg.resultCode = SUCCESS;
				String returnJson = gson.toJson(returnArg);
				PrintWriter pwout = response.getWriter();
				pwout.write(returnJson);
			}
		} else {
			System.out.println("连接数据库失败");
		}
	}
	

}
