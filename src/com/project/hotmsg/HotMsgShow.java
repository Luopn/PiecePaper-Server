package com.project.hotmsg;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.project.editmsg.MsgArgClass;
import com.project.utils.DbUtil;

/**
 * Servlet implementation class HotMsgShow
 */
@WebServlet({ "/HotMsgShow", "/hotmsgshow" })
public class HotMsgShow extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int SUCCESS = 1;
	MsgArgClass returnArg = new MsgArgClass();
	Gson gson = new Gson();
	Connection conn = DbUtil.getConn();
	ArrayList<MsgInfo> list;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		MsgArgClass arg = gson.fromJson(request.getReader(), MsgArgClass.class);
		ResultSet resultSet;
		list = new ArrayList<MsgInfo>();
		if (arg != null && arg.act.equalsIgnoreCase("hotmsg") && conn != null) {
//			String sql = "select * from(select a.*,rownum rn from "
//					+ "(select msgcontent,name,userpic,picpath,"
//					+ "AllMsg.regdate,userinfo.id as userid,AllMsg.id as msgid from AllMsg left join userinfo on "
//					+ "AllMsg.userid=userinfo.id  order by regdate desc)a where rownum<=?) "
//					+ "where rn>=?";
			String sql = "select msgcontent,name,userpic,picpath,"
					+ "AllMsg.regdate,userinfo.id as userid,AllMsg.id as msgid from AllMsg left join userinfo on "
					+ "AllMsg.userid=userinfo.id  order by regdate desc limit ?,?";
			try {
				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setInt(1, arg.min);
				statement.setInt(2, arg.max);
				resultSet = statement.executeQuery();
				MsgArgClass returnArg = new MsgArgClass();
				PrintWriter out = response.getWriter();
				while (resultSet.next()) {
					System.out.println(resultSet.getString(1));
					System.out.println(resultSet.getString(3));
					MsgInfo info = new MsgInfo();
					info.msgcontent = resultSet.getString(1);
					info.userName = resultSet.getString(2);
					info.userPic = resultSet.getString(3);
					info.picPath = resultSet.getString(4);
					info.regdate = resultSet.getString(5);
					info.userid = resultSet.getInt(6);
					info.msgid = resultSet.getInt(7);
					System.out.println(info.picPath);
					list.add(info);
				}
				returnArg.act = "hotmsg";
				returnArg.resultCode = SUCCESS;
				returnArg.list = list;
				list = null;

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
