package com.qq;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TEST {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String httpUrl = "http://localhost:8080/WebTest/Webtest";
		String httpArg = "sender=346786495@qq.com&reciever=bbb@qq.com&sendtime=2015-12-16-10:43&subject=hello&body=chinesecannot&attahment=D:\\ibotest\\test.ppt";
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
	        // ÃÓ»ÎapikeyµΩHTTP header
	        connection.connect();
	        java.io.InputStream is = connection.getInputStream();
	        //InputStream is = (InputStream) connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
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
