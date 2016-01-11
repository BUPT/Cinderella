package com.qq;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

/**
 * Servlet implementation class Demo1
 */
@WebServlet("/Demo1")
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
					email.setEmailAttach(temp1[i+1]);
				}
			}
	    }
		MailTxtInput(Raw_filename,email.getSender(),email.getReciever(),email.getSendTime(),email.getEmailSubject(),email.getEmailBody());		
		MailAppendInput(Raw_filename,email.getEmailAttach());
		
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
	public void MailTxtInput(String fileName,String sendInfo,String recieveInfo,String timeInfo,String subjectInfo,String bodyInfo)
	{
		try
		{
			String filename=fileName;	
			File file = new File(filename);
		    if(!file.exists()) 
		    {
			    file.createNewFile();
		    }
		    FileWriter fw = new FileWriter(file.getAbsoluteFile());
		    BufferedWriter bw = new BufferedWriter(fw);
		    bw.write(sendInfo);
		    bw.write(recieveInfo); 
		    bw.write(timeInfo);
		    bw.write(subjectInfo);
		    bw.write(bodyInfo);
		    bw.close();
	
		    System.out.println("Done");  
		} 
		catch(IOException e) 
		{
			e.printStackTrace();
		}			
	}
	//*********************閭欢闄勪欢淇℃伅鍐欏叆txt***********************//
	public void MailAppendInput(String fileName,String appendInfo)
	{
		String filename=fileName;
		System.out.println(filename);
		Tika tika = new Tika();
		String text = null;
		String filePath=appendInfo;  
       
		try 
		{
			text = tika.parseToString(new File(appendInfo));
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
			File file = new File(filename);
		    if(!file.exists()) 
		    {
			    file.createNewFile();
		    }
		    FileWriter fw = new FileWriter(file.getAbsoluteFile());
		    BufferedWriter bw = new BufferedWriter(fw);
		    bw.write(text);
		    bw.close();
		} 
		catch(IOException e) 
		{
			e.printStackTrace();
		}			
	}
	public void Txt2Abstract(String infileName,String outfilename) 
	{
		//摘要生成部分
		//传入的参数是所有信息的txt保存路径，和摘要的txt保存路径
	}
	public BotResult Abstract2Meta(String filename)
	{
		//关键值提取部分
		//传入的参数是摘要的txt保存路径，返回值是BotResult对象
		
		String location="北京";
		int financeLimit=30000;
		String tranStock="20%";
		String projectName="农夫山泉";
		String companyName="北京农业科技有限公司";
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
		//置信程度提取部分
		//传入的参数和返回值都是BotResult对象
		
		double subness=0.5;
		bot.setSubness(subness);
		return bot;
	}
	public void iBotOutput(BotResult bot,HttpServletResponse response)
	{
		response.setContentType("text/plain;charset=utf-8");
		try {
			PrintWriter out = response.getWriter();
			System.out.println("---------------输出ing---------------------");
			out.println("Location:"+bot.getLocation());
			out.println("Project:"+bot.getProjectName());
			out.println("Company name"+bot.getCompanyName());
			out.println("Founder:"+bot.getFounderName());
			out.println("FinanceLimit:"+bot.getFinanceLimit());
			out.println("TranStock:"+bot.getTranStock());
			out.println("BizArea:"+bot.getBizArea());
			out.println("Subness:"+bot.getSubness());
			out.println("------------------------------------");
			out.flush();
			out.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
