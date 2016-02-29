package com.qq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Servlet implementation class IbotInfo
 */
public class IbotInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public IbotInfo() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String str="please use post method";
		response.getOutputStream().write(str.toString().getBytes("UTF-8"));  				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
				sender=req.getParameter("sender");
				receiver=req.getParameter("receiver"); 
				sendtime=req.getParameter("sendtime");
				subject=req.getParameter("subject");
				body=req.getParameter("body");
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
		    System.out.println("json"+json);
		    int j1 = json.indexOf("sender");
		    int j2 = json.indexOf("receiver");
		    int j3 = json.indexOf("sendtime");
		    int j4 = json.indexOf("subject");
		    int j5 = json.indexOf("body");
		    int i=0;
		    if(j1!=-1 && j2!=-1)
		    {
		    	sender=json.substring(j1+7, j2-1);
		    }
		    if(j2!=-1 && j3!=-1)
		    {
		    	receiver=json.substring(j2+9, j3-1);
		    }
		    if(j3!=-1 && j4!=-1)
		    {
		    	sendtime=json.substring(j3+9, j4-1);
		    }
		    if(j4!=-1 && j5!=-1)
		    {
		    	subject=json.substring(j4+8, j5-1);
		    }
		    body=json.substring(j5+5,json.length()-1);
		}
				
		email.setSender(sender);
		email.setReceiver(receiver);
		email.setSendTime(sendtime);
		email.setEmailSubject(subject);
		email.setEmailBody(body);
		email.setEmailAttach(attachTemp);
		
		MailTxtInput(Raw_filename,email);
		
		try {
			botResult=GetKeyPart(Raw_filename,botResult);
		} catch (UnirestException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			botResult=GetOtherPart(Raw_filename,botResult);
		} catch (UnirestException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			iBotOutput(botResult,response);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String readJSONString(HttpServletRequest request) throws IOException
	{
		 BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
	        String line = null;
	        StringBuilder sb = new StringBuilder();
	        while((line = br.readLine())!=null){
	            sb.append(line);
	        }
	        // 将资料解码
	        String reqBody = sb.toString();
	        return URLDecoder.decode(reqBody, "UTF_8");
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
	public String Txt2Abstract(String infileName,String SENTIMENT_URL) throws IOException, UnirestException 
	{
		File file = new File(infileName);
		String result = "";
		if(file.isFile() && file.exists())
		{ 
            InputStreamReader read = new InputStreamReader (new FileInputStream(file),"utf-8");
            BufferedReader in = new  BufferedReader(read);   
            String line; 
            while((line = in.readLine())!=null) 
            {   
            	result += line;
            }    
            read.close();
        }
		
		String body = "[\""+result+"\"]";    
		
		System.out.println("body:"+body);
		
		HttpResponse <JsonNode> jsonResponse = Unirest.post(SENTIMENT_URL)
			.header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("X-Token", "5mR6aTnx.4451.Gx2Jt_BBGdFE")
            .body(body)
            .asJson();
			
		String finalStr = jsonResponse.getBody().toString();
		//Unirest.shutdown();			
        return finalStr;
	}
	/**
	 * 
	 * @param bot
	 * @throws UnirestException 
	 * @throws IOException 
	 */
	public  BotResult GetKeyPart(String infileName,BotResult bot) throws IOException, UnirestException
	{
		String SENTIMENT_URL="http://api.bosonnlp.com/keywords/analysis";
		String AnalyseLine = Txt2Abstract(infileName,SENTIMENT_URL);
		
		AnalyseLine=AnalyseLine.replace("[","").replace("]","");	
		String[] all=AnalyseLine.split(",");
		
		ArrayList<String> bizArea=new ArrayList<String> ();
		if(all.length>=4)
		{
			bizArea.add(all[1].replace("\"", ""));
			bizArea.add(all[3].replace("\"", ""));
		}
		else if(all.length>=2&&all.length<4)
		{
			bizArea.add(all[1].replace("\"", ""));
		}
		else
		{
			bizArea.add("null");
		}
		bot.setBizArea(bizArea);
		return bot;
				
	}
	public BotResult GetOtherPart(String infileName,BotResult bot) throws IOException, UnirestException, JSONException
	{
		String SENTIMENT_URL="http://api.bosonnlp.com/ner/analysis";
		String AnalyseLine = Txt2Abstract(infileName,SENTIMENT_URL);
		
		JSONArray jsonArray = new JSONArray(AnalyseLine);
		int iSize = jsonArray.length();
		String entity=null;
		String word=null;
		
		for (int i = 0; i < iSize; i++) 
		{
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			word = jsonObj.get("word").toString();
			entity = jsonObj.get("entity").toString();
		}
		
		entity = entity.replace("[", "").replace("]", "").replace("\"", "");
		String[] all_entity = entity.split(",");
		
		
		word = word.replace("[", "").replace("]", "").replace("\"", "");
		String[] all_word = word.split(",");
		
		int locationFlag=0,cnameFlag=0,pronameFlag=0;
		int temp=0;
		ArrayList<String> founderName=new ArrayList<String> ();
		
		for(int i=0;i<all_entity.length;i++)
		{
			if(all_entity[i].equals("location")&&locationFlag==0)
			{
				locationFlag=1;
				temp= Integer.parseInt(all_entity[i-2]);
				bot.setLocation(all_word[temp]);
			}
			else if(all_entity[i].equals("person_name"))
			{
				temp= Integer.parseInt(all_entity[i-2]);
				founderName.add(all_word[temp]);	
			}
			else if(all_entity[i].equals("company_name")&&cnameFlag==0)
			{
				cnameFlag=1;
				temp= Integer.parseInt(all_entity[i-2]);
				bot.setCompanyName(all_word[temp]);
			}
			else if(all_entity[i].equals("product_name")&&pronameFlag==0)
			{
				pronameFlag=1;
				temp= Integer.parseInt(all_entity[i-2]);
				bot.setProjectName(all_word[temp]);
			}
			
		}
		
		int financeLimit=30000;
		String tranStock="20%";
		bot.setFounderName(founderName);
		bot.setFinanceLimit(financeLimit);
		bot.setTranStock(tranStock);
		return bot;
	}
	
	public void iBotOutput(BotResult bot,HttpServletResponse response) throws JSONException
	{
		try {
		
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
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

