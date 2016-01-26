package com.qq;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.omg.CORBA.portable.OutputStream;

import sun.net.www.URLConnection;
import sun.net.www.http.HttpClient;

import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;

public class PostRequest {
	public static void main(String[] args) throws Exception {
		/**
		 * POST请求待发送的参数
		 * sender：发件人
		 * reciever：收件人
		 * sendtime：发件时间
		 * subject：件主题
		 * body：邮件内容
		 * uploadFiles：本地附件的路径
		 */
		String sender = "346786495@qq.com";
		String reciever = "wnbupt@qq.com";
		String sendtime = "2015-12-16 10:43";
		String subject = "测试demo";
		String body = "快塞给我一封邮件吧！";
		String[] uploadFiles = {"D:\\ibotest\\test.ppt","D:\\ibotest\\test.doc"};
		/**
		 *  对传入的参数进行utf-8编码
		 */
		sender = URLEncoder.encode(sender, "UTF-8");
		reciever = URLEncoder.encode(reciever, "UTF-8");
		sendtime = URLEncoder.encode(sendtime, "UTF-8");
		subject = URLEncoder.encode(subject, "UTF-8");
		body = URLEncoder.encode(body, "UTF-8");
		/**
		 *  准备上传附件的文件流写入
		 */
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		/**
		 * 开始建立连接
		 */
		String actionUrl = "http://111.207.243.70:8088/IbotInfo/GetInfo";
		try {
			URL url = new URL(actionUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			con.addRequestProperty("sender", sender);
			con.addRequestProperty("reciever", reciever);
			con.addRequestProperty("sendtime", sendtime);
			con.addRequestProperty("subject", subject);
			con.addRequestProperty("body", body);
			/**
			 * 将上传附件写入文件流
			 */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			for (int i = 0; i < uploadFiles.length; i++) 
			{
				String uploadFile = uploadFiles[i];
				String filename = uploadFile.substring(uploadFile.lastIndexOf("//") + 1);
				ds.writeBytes(twoHyphens + boundary + end);
				ds.writeBytes("Content-Disposition: form-data; " + "name=\"file" + i + "\";" + "filename=\"" + filename
						+ "\"" + end);
				ds.writeBytes(end);
				FileInputStream fStream = new FileInputStream(uploadFile);
				int bufferSize = 1024;
				byte[] buffer = new byte[bufferSize];
				int length = -1;
				while ((length = fStream.read(buffer)) != -1) 
				{
					ds.write(buffer, 0, length);
				}
				ds.writeBytes(end);
				/* close streams */
				fStream.close();				
			}			
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			ds.flush();
			ds.close();			
			/**
			 * 接收来自服务器的消息
			 */
			DataInputStream in = new DataInputStream(con.getInputStream());
			BufferedReader reader = null;
			StringBuffer sbf = new StringBuffer();
			reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			in.close();
			System.out.println(sbf);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
