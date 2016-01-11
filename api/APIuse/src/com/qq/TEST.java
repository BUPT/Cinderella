package com.qq;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TEST {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String httpUrl = "http://localhost:8080/FinalTry/Webtest";
		String sendInfo="346786495@qq.com";
		String recieveInfo="bbb@qq.com";
		String timeInfo="2015-12-16-10:43";
		String subjectInfo="HelloWorld!";
		String bodyInfo="balalalalal";
		String[] appendInfo={"D:/ibotest/test.ppt","D:/ibotest/test.doc"};
		String httpArg = "sender="+sendInfo+"&reciever="+recieveInfo+"&sendtime="+timeInfo+"&subject="+subjectInfo+"&body="+bodyInfo+"&attachment=";
		int i;
		for(i=0;i<appendInfo.length-1;i++)
		{
			httpArg=httpArg+appendInfo[i]+'|';
		}
		httpArg=httpArg+appendInfo[i];
		TEST test=new TEST();
		String jsonResult = test.request(httpUrl, httpArg);
		System.out.println(jsonResult);
	}
	
	public static String request(String httpUrl,String httpArg) 
	{
	    BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
		httpUrl = httpUrl + "?" + httpArg;
	    System.out.println(httpUrl);
	    try 
	    {
	        URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setRequestMethod("POST");
	        // ����apikey��HTTP header
	        connection.connect();	      
	        java.io.InputStream is = connection.getInputStream();
	        //InputStream is = (InputStream) connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	        	//System.out.println(strRead);
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }
	        reader.close();
	        
	        result = sbf.toString();
	    } 
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}
	
}
