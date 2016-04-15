package com.project.editmsg;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class EditMsg
 */
@WebServlet({ "/EditMsg", "/editmsg" })
public class EditMsg extends HttpServlet {
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

		ServletInputStream in = request.getInputStream();
		byte[] nameLenghtBytes = new byte[2];

		for (int i = 0; i < 2; i++) {// 读取文件名长度部分2字节
			int b = in.read();
			System.out.println("b:" + Integer.toHexString(b));
			if (b >= 0) {
				nameLenghtBytes[i] = (byte) (b);
			}
		}
		// 还原文件名长度
		short nameLenght = 0;
		for (int i = 0; i < 2; i++) {
			nameLenght |= nameLenghtBytes[i] << (8 * i);
		}

		System.out.println("nameLenght:" + nameLenght); // 观察数据的值
		if (nameLenght <= 0 || nameLenght > 512) {
			return;
		}
		byte[] fileNameBytes = new byte[nameLenght];

		int readed = 0;
		while (readed < nameLenght) {
			int r = in.read(fileNameBytes, readed, nameLenght - readed);
			if (r <= 0) {
				break;
			}
			readed += r;
			System.out.println("fileNameBytes readed:" + readed); // 观察数据的值
		}
		String fileName = new String(fileNameBytes, "UTF-8"); // 把文件名从字节转换到字符串
		long time = System.currentTimeMillis();
		String rootPath = getServletContext().getRealPath("");
		String fileServerPath = "/msgpic/";
		// 创建文件
		File localFile = new File(rootPath + fileServerPath);
		localFile.mkdirs();// 创建目录
		fileServerPath = fileServerPath + fileName;
		localFile = new File(rootPath + fileServerPath);
		localFile.createNewFile(); // 创建文件
		System.out.println(localFile.getAbsolutePath());

		BufferedOutputStream fileOut = new BufferedOutputStream(
				new FileOutputStream(localFile));

		byte[] datas = new byte[1024];
		while ((readed = in.read(datas, 0, datas.length)) > 0) {
			System.out.println("datas readed:" + readed); // 观察数据的值
			// 保存文件到磁盘
			fileOut.write(datas, 0, readed);
		}

		fileOut.close();
		in.close();
		System.out.println("文件保存成功：" + localFile.getAbsolutePath());

		// 给客户端返回结果信息
		PicArgClass returnPicArg = new PicArgClass();
		returnPicArg.act = "msgpic";
		returnPicArg.resultCode = 1;
		returnPicArg.picName = fileName;
		returnPicArg.picPath = fileServerPath;
		/*
		 * HTTP应答主体
		 */
		Gson gson = new Gson();
		PrintWriter out = response.getWriter();
		out.write(gson.toJson(returnPicArg));
		out.flush();
		out.close();
		in.close();

	}
}
