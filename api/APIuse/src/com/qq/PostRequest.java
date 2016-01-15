package com.qq;

import java.io.BufferedReader;
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

public class PostRequest 
{
	public static void main(String[] args) throws Exception 
	{
		
		String pathUrl = "http://localhost:8080/IbotInfo/GetInfo";  
		String sendInfo="346786495@qq.com";
		String recieveInfo="wnbupt@qq.com";
		String timeInfo="2015-12-16-10:43";
		String subjectInfo="����demo������";
		String bodyInfo="������ʵҲ�ǿ��Ե��������";
		String[] appendInfo={"D:/ibotest/test.ppt","D:/ibotest/test.doc"};
		//String[] appendInfo={"null"};//û�и�����ʱ����null
		
		String sender=URLEncoder.encode(sendInfo,"UTF-8");  
        String reciever=URLEncoder.encode(recieveInfo,"UTF-8"); 
        String sendtime=URLEncoder.encode(timeInfo,"UTF-8"); 
        String subject=URLEncoder.encode(subjectInfo,"UTF-8"); 
        String body=URLEncoder.encode(bodyInfo,"UTF-8");   
        String sendMsg=("sender="+sender+"&reciever="+reciever+"&sendtime="+sendtime+"&subject="+subject+"&body="+body+"&attachment=");
        int i;
        for(i=0;i<appendInfo.length-1;i++)
        {
        	appendInfo[i]=URLEncoder.encode(appendInfo[i],"UTF-8");
        	sendMsg=sendMsg+appendInfo[i]+"|";
        }
        appendInfo[i]=URLEncoder.encode(appendInfo[i],"UTF-8");
    	sendMsg=sendMsg+appendInfo[i];	 
		//System.out.println(sendMsg);
		// ��������   
		URL url = new URL(pathUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);    // ���Է�������
        conn.setDoInput(true);    // ���Խ�������
        conn.setRequestMethod("POST");    // POST����
        conn.setRequestProperty
        ("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");       
        conn.connect();
        //    д���POST����  
        OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());       
        
		
        
        
        
               
        osw.write(sendMsg);
        osw.flush();
        osw.close();
        // ��ȡ��Ӧ����
        
        BufferedReader reader = null;
	    StringBuffer sbf = new StringBuffer();	
        java.io.InputStream is = conn.getInputStream();
        //InputStream is = (InputStream) connection.getInputStream();
        reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
        String strRead = null;
        while ((strRead = reader.readLine()) != null) {
        	//System.out.println(strRead);
            sbf.append(strRead);
            sbf.append("\r\n");
        }
        reader.close();
        System.out.println(sbf);
    }
}

