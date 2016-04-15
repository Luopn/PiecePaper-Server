package com.project.editmeinfo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.project.editmsg.MsgArgClass;
import com.project.utils.DbUtil;

/**
 * Servlet implementation class EditMeInfo
 */
@WebServlet({ "/EditMeInfo", "/editmeinfo" })
public class EditMeInfo extends HttpServlet {
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

		for (int i = 0; i < 2; i++) {// ��ȡ�ļ������Ȳ���2�ֽ�
			int b = in.read();
			System.out.println("b:" + Integer.toHexString(b));
			if (b >= 0) {
				nameLenghtBytes[i] = (byte) (b);
			}
		}
		// ��ԭ�ļ�������
		short nameLenght = 0;
		for (int i = 0; i < 2; i++) {
			nameLenght |= nameLenghtBytes[i] << (8 * i);
		}

		System.out.println("nameLenght:" + nameLenght); // �۲����ݵ�ֵ
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
			System.out.println("fileNameBytes readed:" + readed); // �۲����ݵ�ֵ
		}
		String fileName = new String(fileNameBytes, "UTF-8"); // ���ļ������ֽ�ת�����ַ���

		long time = System.currentTimeMillis();
		String rootPath = getServletContext().getRealPath("");
		String fileServerPath = "/images/userhead/";
		// �����ļ�
		File localFile = new File(rootPath + fileServerPath);
		localFile.mkdirs();// ����Ŀ¼
		fileServerPath = fileServerPath + fileName;
		localFile = new File(rootPath + fileServerPath);
		localFile.createNewFile(); // �����ļ�
		System.out.println(localFile.getAbsolutePath());

		BufferedOutputStream fileOut = new BufferedOutputStream(
				new FileOutputStream(localFile));

		byte[] datas = new byte[1024];
		while ((readed = in.read(datas, 0, datas.length)) > 0) {
			System.out.println("datas readed:" + readed); // �۲����ݵ�ֵ
			// �����ļ�������
			fileOut.write(datas, 0, readed);
		}

		fileOut.close();
		in.close();
		System.out.println("�ļ�����ɹ���" + localFile.getAbsolutePath());

		// ���ͻ��˷��ؽ����Ϣ
		EditMeArgClass returnPicArg = new EditMeArgClass();
		returnPicArg.resultCode = 1;
		returnPicArg.act = "editmeinfo";
		returnPicArg.headPicPath = fileServerPath;
		/*
		 * HTTPӦ������
		 */
		Gson gson = new Gson();
		PrintWriter out = response.getWriter();
		out.write(gson.toJson(returnPicArg));
		out.flush();
		out.close();
		in.close();
	}

}
