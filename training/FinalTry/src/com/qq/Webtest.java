package com.qq;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.json.JSONStringer;

/**
 * Servlet implementation class Webtest
 */
@WebServlet("/Webtest")
public class Webtest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Webtest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO Auto-generated method stub
		EmailInput email=new EmailInput();
		BotResult botResult=new BotResult();
		String Raw_filename="D:\\iBotest\\bizplan1.txt";
		String Abstract_filename="D:\\iBotest\\abstract.txt";
		
		String str=request.getQueryString();
		str=URLDecoder.decode(str, "UTF-8");
		System.out.println(str);
		//out.println(str);
		String temp[] =str.split("&");
		for(String word : temp)
	    {
			
			String temp1[] =word.split("=");	
			for(int i=0;i<temp1.length;i++)
			{
				if(temp1[i].equals("sender"))
				{
					email.setSender(temp1[i+1]);
				}
				else if(temp1[i].equals("reciever"))
				{
					email.setReciever(temp1[i+1]);
				}
				else if(temp1[i].equals("sendtime"))
				{
					email.setSendTime(temp1[i+1]);
				}
				else if(temp1[i].equals("subject"))
				{
					email.setEmailSubject(temp1[i+1]);
				}
				else if(temp1[i].equals("body"))
				{
					email.setEmailBody(temp1[i+1]);
				}
				else if(temp1[i].equals("attachment"))
				{
					String temp2[] =temp1[i+1].split("\\|");	
					email.setEmailAttach(temp2);
				}
			}
	    }
			
		MailTxtInput(Raw_filename,email);		
		
		Txt2Abstract(Raw_filename,Abstract_filename);		

		botResult=Abstract2Meta(Abstract_filename);

		botResult=Meta2Subness(botResult);
		
		iBotOutput(botResult,response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.println("use post!!!");
		doGet(request, response); 
		
	}
	public void MailTxtInput(String fileName,EmailInput email)
	{
		String sendInfo=email.getSender();
		String recieveInfo=email.getReciever();
		String timeInfo=email.getSendTime();
		String subjectInfo=email.getEmailSubject();
		String bodyInfo=email.getEmailBody();
		String[] appendInfo=email.getEmailAttach();
		Tika tika = new Tika();
		String text = "";
		try 
		{
			for(int i=0;i<appendInfo.length;i++)
			{
				text = text+"\r\n"+"附件"+(i+1)+"内容:"+tika.parseToString(new File(appendInfo[i]));
			}		
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (TikaException e) 
		{
			e.printStackTrace();
		}
		try
		{
			String filename=fileName;	
			File file = new File(filename);
		    if(!file.exists()) 
		    {
			    file.createNewFile();
		    }
		    FileOutputStream fw = new FileOutputStream(file.getAbsoluteFile());
		    //BufferedWriter bw = new BufferedWriter(fw);
		    //BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fw, "UTF-8")); 
		    OutputStreamWriter bw = new OutputStreamWriter(fw, "UTF-8");
		    bw.write("发件人："+sendInfo+"\r\n");
		    bw.write("收件人："+recieveInfo+"\r\n"); 
		    bw.write("发件时间："+timeInfo+"\r\n");
		    bw.write("邮件主题："+subjectInfo+"\r\n");
		    bw.write("邮件正文："+bodyInfo+"\r\n");
		    bw.write("附件内容："+"\r\n"+text);
		    bw.close();
	
		    System.out.println("Done");  
		} 
		catch(IOException e) 
		{
			e.printStackTrace();
		}			
	}
//	//*********************邮件附件信息写入txt***********************//
//	public void MailAppendInput(String fileName,String appendInfo)
//	{
//		String filename=fileName;
//		System.out.println(filename);
//		Tika tika = new Tika();
//		String text = null;
//		//String filePath=appendInfo;  
//       
//		try 
//		{
//			text = tika.parseToString(new File(appendInfo));
//		} 
//		catch (IOException e) 
//		{
//			e.printStackTrace();
//		} 
//		catch (TikaException e) 
//		{
//			e.printStackTrace();
//		}
//		try
//		{	
//			File file = new File(filename);
//		    if(!file.exists()) 
//		    {
//			    file.createNewFile();
//		    }
//		    FileWriter fw = new FileWriter(file.getAbsoluteFile());
//		    BufferedWriter bw = new BufferedWriter(fw);
//		    bw.write(text);
//		    bw.close();
//		} 
//		catch(IOException e) 
//		{
//			e.printStackTrace();
//		}			
//	}
	public void Txt2Abstract(String infileName,String outfilename) 
	{
		//摘要获取模块
		//输入为txt语料地址和摘要存储地址，无返回值
	}
	public BotResult Abstract2Meta(String filename)
	{
		//关键属性获取模块
		//输入为摘要存放地址，返回值为BotResult对象
		
		String location="北京";
		int financeLimit=30000;
		String tranStock="20%";
		String projectName="农夫之家";
		String companyName="北京市农业科技有限公司";
		String founderName="张三";
		String bizArea="农业";
		
		BotResult bot=new BotResult();
		bot.setLocation(location);
		bot.setFinanceLimit(financeLimit);
		bot.setTranStock(tranStock);
		bot.setProjectName(projectName);
		bot.setCompanyName(companyName);
		bot.setFounderName(founderName);
		bot.setBizArea(bizArea);
		bot.setSubness(0);
		
		return bot;
	}
	public BotResult Meta2Subness(BotResult bot)
	{
		//置信度获取模块
		//输入为BotResult对象，输出为置信度subness
		
		double subness=0.5;
		bot.setSubness(subness);
		return bot;
	}
	public void iBotOutput(BotResult bot,HttpServletResponse response)
	{
		response.setCharacterEncoding("UTF-8");
		//response.setContentType("text/plain;charset=utf-8");
		//response.setContentType("application/json;charset=utf-8");
		response.setContentType("text/json; charset=UTF-8"); 
		JSONStringer stringer = new JSONStringer();  

		try {
			//PrintWriter out = response.getWriter();
			//stringer.array();
		    stringer.object().key("地点").value(bot.getLocation()).  
	        key("项目名称").value(bot.getProjectName()).  
	        key("公司名称").value(bot.getCompanyName()).  
	        key("成立者").value(bot.getFounderName()).
	        key("融资额度").value(bot.getFounderName()).
	        key("出让股权").value(bot.getTranStock()).
	        key("行业").value(bot.getBizArea()).
	        key("置信程度").value(bot.getSubness()).endObject(); 
		    //stringer.endArray();
			System.out.println("---------------输出ing---------------------");
//			out.println(":"+bot.getLocation());
//			out.println("项目名称:"+bot.getProjectName());
//			out.println("公司名称:"+bot.getCompanyName());
//			out.println("成立者:"+bot.getFounderName());
//			out.println("融资额度:"+bot.getFinanceLimit());
//			out.println("出让股权:"+bot.getTranStock());
//			out.println("行业:"+bot.getBizArea());
//			out.println("置信程度:"+bot.getSubness());
//			out.println("------------------------------------");
//			out.flush();
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));  			
			//out.write(stringer.toString());
			
	//		out.println(stringer.toString().getBytes("UTF-8"));
		//	out.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
