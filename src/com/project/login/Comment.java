package com.project.login;

import java.io.BufferedReader;
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

@WebServlet({ "/Comment", "/comment" })
public class Comment extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Gson gson = new Gson();
	Connection conn = DbUtil.getConn();
	BufferedReader br;
	private static final int SUCCESS = 1;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// response.getWriter().append("Served at:
		// ").append(request.getContextPath());
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		br = request.getReader();
		int result;
		CommentInfo argComment = gson.fromJson(br, CommentInfo.class);
		CommentInfo returnArg = new CommentInfo();
		if (argComment != null && argComment.act.equalsIgnoreCase("sendComment") && conn != null) {
			String comment = argComment.comment;
			int userid = argComment.userid;
			int msgid = argComment.msgid;
			System.out.println(userid);
			System.out.println(msgid);
//			String sql = "insert into comments(id,msgid,userid,regdate,mycomment) "
//					+ "values(seq_comments_id.nextVal,?,?,?,?)";
			String sql = "insert into comments(msgid,comments,userid,regdate) "
					+ "values(?,?,?,?)";
			try {
				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setInt(1, msgid);
				statement.setString(2, comment);
				statement.setInt(3, userid);
				statement.setString(4, String.valueOf(System.currentTimeMillis()));
				result = statement.executeUpdate();
				if (result > 0) {
					returnArg.act = "sendComment";
					returnArg.resultCode = SUCCESS;
					String returnJson = gson.toJson(returnArg);
					PrintWriter out = response.getWriter();
					out.write(returnJson);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("连接数据库失败");
		}
	}

	class CommentInfo {
		String act;
		int userid, msgid, resultCode;
		String comment;
	}

}
