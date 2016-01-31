package com.qq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONObject; 
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import org.json.*;
import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Servlet implementation class GetInfo
 */
public class GetInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String str="please use post method";
		response.getOutputStream().write(str.toString().getBytes("UTF-8"));  		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		
		EmailInput email=new EmailInput();
		BotResult botResult=new BotResult();
		String Raw_filename="D:\\iBotest\\bizplan1.txt";
		String Abstract_filename="D:\\iBotest\\abstract.txt";
		
		String sender=null;
		String receiver=null; 
        String sendtime=null;
        String subject=null;
        String body=null;         
        ArrayList<String> attachTemp = new ArrayList<String> ();
         
		if(request.getHeader("Content-Type").indexOf("multipart/form-data")!=-1)
		{
			
	        String realPath = getServletContext().getRealPath("/") + "RecFile";
			System.out.println(realPath);
			File fileupload = new File(realPath);
			if(!fileupload.exists()){
				fileupload.mkdir();
			}
			SmartUpload su=new SmartUpload();
			su.initialize(getServletConfig(), request, response);
			com.jspsmart.upload.Request req = su.getRequest();
			JSONObject obj = null;
			String revdata=null;
			try 
			{
				su.upload();
				try
				{
					revdata=req.getParameter("DATA");				
			        System.out.println(revdata);
			        obj = new JSONObject(revdata);
			        sender=obj.getString("sender");
			        receiver=obj.getString("receiver"); 
			        sendtime=obj.getString("sendtime");
			        subject=obj.getString("subject");
			        body=obj.getString("body");   
				}catch(JSONException e)
				{
					e.printStackTrace();
				}
				int count=su.save(realPath);
				for(int i=0;i<su.getFiles().getCount();i++)
				{
					com.jspsmart.upload.File tempFile=su.getFiles().getFile(i);
					System.out.println(realPath+"\\"+tempFile.getFileName());
					attachTemp.add(realPath+"\\"+tempFile.getFileName());
				}
				System.out.println("count:"+count);
			} 
			catch (SmartUploadException e) {
				e.printStackTrace();
			}				        
		}
		
		else if(request.getHeader("Content-Type").indexOf("application/json")!=-1)
		{
			    String json = readJSONString(request);	
			    System.out.println(json);
		        JSONObject jsonObject = null;
		        try {
		            jsonObject = new JSONObject(json);
		            sender=jsonObject.getString("sender");
		            receiver=jsonObject.getString("receiver"); 
			        sendtime=jsonObject.getString("sendtime");
			        subject=jsonObject.getString("subject");
			        body=jsonObject.getString("body");   
		        }
		        catch (JSONException e) {
		            e.printStackTrace();
		        }
		}
				
		email.setSender(sender);
		email.setReceiver(receiver);
		email.setSendTime(sendtime);
		email.setEmailSubject(subject);
		email.setEmailBody(body);
		email.setEmailAttach(attachTemp);
		
		MailTxtInput(Raw_filename,email);
		
		Txt2Abstract(Raw_filename,Abstract_filename);		

		botResult=Abstract2Meta(Abstract_filename);
		
		iBotOutput(botResult,response);
	}
	public String readJSONString(HttpServletRequest request)
	{
        StringBuffer json = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while((line = reader.readLine()) != null) {
                json.append(line);
            }
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
        return json.toString();
    }
	public void MailTxtInput(String fileName,EmailInput email)
	{
		String sendInfo=email.getSender();
		String receiveInfo=email.getReceiver();
		String timeInfo=email.getSendTime();
		String subjectInfo=email.getEmailSubject();
		String bodyInfo=email.getEmailBody();
		ArrayList<String> appendInfo = email.getEmailAttach();
		Tika tika = new Tika();
		String text = "";
		if(appendInfo.isEmpty())
		{
			text="您没有指定任何附件";
			System.out.println(text);
		}
		else
		{
			try 
			{	
				for(int i=0;i<appendInfo.size();i++)
				{
					File file=new File(appendInfo.get(i));
					if(!file.exists())  
					{
						text = text+"您指定的第"+(i+1)+"个附件不存在!\n";
						System.out.println(text);
					}
					else
					{
						text = text+"\r\n"+"\r\n"+"附件"+(i+1)+"内容:"+"\r\n"+tika.parseToString(file);
					}					
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
		    OutputStreamWriter bw = new OutputStreamWriter(fw, "UTF-8");
		    bw.write("发件人："+sendInfo+"\r\n");
		    bw.write("收件人："+receiveInfo+"\r\n"); 
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
		String[] founder={"张三","李四","王五"};
		String[] area={"农业","科技"};
		ArrayList<String> founderName=new ArrayList<String> ();
		ArrayList<String> bizArea=new ArrayList<String> ();
		
		BotResult bot=new BotResult();
		bot.setLocation(location);
		bot.setFinanceLimit(financeLimit);
		bot.setTranStock(tranStock);
		bot.setProjectName(projectName);
		bot.setCompanyName(companyName);
		for(int i=0;i<founder.length;i++)
		{
			founderName.add(founder[i]);
		}
		for(int i=0;i<area.length;i++)
		{
			bizArea.add(area[i]);
		}
		bot.setFounderName(founderName);
		bot.setBizArea(bizArea);
		
		return bot;
	}
	public void iBotOutput(BotResult bot,HttpServletResponse response)
	{
		response.reset();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json; charset=UTF-8"); 
		JSONStringer stringer = new JSONStringer();  

		try {
			 stringer.object().key("city").value(bot.getLocation()).  
		        key("startup").value(bot.getProjectName()).  
		        key("company").value(bot.getCompanyName()).  
		        key("founders").value(bot.getFounderName()).
		        key("money").value(bot.getFinanceLimit()).
		        key("equity").value(bot.getTranStock()).
		        key("industries").value(bot.getBizArea()).endObject(); 
				System.out.println("---------------输出ing---------------------");
			response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));  			
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
