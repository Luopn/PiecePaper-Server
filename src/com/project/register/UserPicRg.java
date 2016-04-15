package com.project.register;

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
 * Servlet implementation class UserPicRg
 */
@WebServlet({ "/UserPicRg", "/userpicrg" })
public class UserPicRg extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String userpic;
	
	Gson gson = new Gson();
	
    public UserPicRg() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//�����ļ�����
		
		ServletInputStream in = request.getInputStream();
		byte[] nameLenghtBytes = new byte[2];

		for(int i=0;i<2;i++){//��ȡ�ļ������Ȳ���2�ֽ�
			int b = in.read();
			System.out.println("b:"+Integer.toHexString(b));
			if(b>=0){
				nameLenghtBytes[i] = (byte)(b);
			}
		}
		//��ԭ�ļ�������
		short nameLenght = 0;
		for(int i=0;i<2;i++){
			nameLenght |= nameLenghtBytes[i] << (8*i);
		}
		
		System.out.println("nameLenght:"+nameLenght); //�۲����ݵ�ֵ
		if(nameLenght<=0 || nameLenght>512){
			return;
		}
		byte[] fileNameBytes = new byte[nameLenght];
		
		int readed = 0;
		while(readed<nameLenght){
			int r = in.read(fileNameBytes, readed, nameLenght-readed);
			if(r<=0){
				break;
			}
			readed += r;
			System.out.println("fileNameBytes readed:"+readed); //�۲����ݵ�ֵ
		}
		String fileName = new String(fileNameBytes,"UTF-8"); //���ļ������ֽ�ת�����ַ���
		
		long time = System.currentTimeMillis();
		String rootPath = request.getRealPath("/");
		String fileServerPath = "userimgs/userid/"+time+"/";
		//�����ļ�
		File localFile = new File(rootPath+fileServerPath); 
		localFile.mkdirs();//����Ŀ¼
		fileServerPath += fileName;
		localFile = new File(rootPath+fileServerPath);
		localFile.createNewFile(); //�����ļ�
		
		BufferedOutputStream fileOut = new BufferedOutputStream(
					new FileOutputStream(localFile));
		
		byte[] datas = new byte[1024];
		while((readed = in.read(datas, 0, datas.length))>0){
			fileOut.write(datas, 0, readed);
		}
		
		fileOut.close();
		in.close();
		userpic = localFile.getAbsolutePath();
		System.out.println("�ļ�����ɹ���"+userpic);
		
		//���ͻ��˷��ؽ����Ϣ

		ArgClass returnArg = new ArgClass();
		returnArg.act = "up";
		returnArg.resultCode = 1;
		returnArg.name = fileName;
		
		/*
		 * HTTPӦ������
		 */
		PrintWriter out = response.getWriter();
		out.write(gson.toJson(returnArg));
		out.flush();
		out.close();
		
		
	}

}
