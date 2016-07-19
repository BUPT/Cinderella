package com.qq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.synth.SynthSeparatorUI;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;


/**
 * Servlet implementation class GetInfo
 */
@WebServlet("/GetInfo")
public class GetInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GetInfo() {
      
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.reset();
		response.setContentType("text/html; charset=UTF-8");
		String str = "please use post method";
		response.getOutputStream().write(str.getBytes("UTF-8"));			
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	    request.setCharacterEncoding("utf-8");	
	    
		String separator = File.separator;		
		String materialPath = getServletContext().getRealPath(separator) + "TXTFile";
		String realPath = getServletContext().getRealPath(separator) + "RecFile";		
		File file = new File(materialPath);
		if(!file.exists()){
			file.mkdir();
		}
		file = new File(realPath);
		if(!file.exists()){
			file.mkdir();
		}
		String filename = materialPath+separator+"bizplan.txt";
		file = new File(filename);
		if(!file.exists())
		{
			file.createNewFile();
		}
		
        EmailInput email = new EmailInput(); 
        System.out.println("Content-Type:"+request.getHeader("Content-Type"));
		
        if(request.getHeader("Content-Type").indexOf("multipart/form-data")!=-1)
		{   						
			email = RevMultiData(email,request,response,realPath);				
		}
		else if(request.getHeader("Content-Type").indexOf("application/json")!=-1)
		{	
			email = RevJsonData(email,request);		    
		}     
		else
		{
			email.setEmailBody("CINDERELLA-FALSE");
		}
		try 
		{
			MailTxtInput(filename,email);
			
			//Get HTTP Headers
			Map<String, String> map = new HashMap<String, String>();
		    Enumeration headerNames = request.getHeaderNames();
		    while (headerNames.hasMoreElements()) 
		    {
		        String key = (String) headerNames.nextElement();
		        String value = request.getHeader(key);
		        map.put(key, value);
		    }
		    
		    //into the CINDERELLA test unit
			if(email.getEmailBody().equals("CINDERELLA"))
		    {
				File fileTemp = new File(filename);
				String sInput = "";
				if(fileTemp.isFile() && fileTemp.exists())
				{ 
		            InputStreamReader read = new InputStreamReader (new FileInputStream(fileTemp),"utf-8");
		            BufferedReader in = new  BufferedReader(read);   
		            String line; 
		            while((line = in.readLine())!=null) 
		            {   
		            	line += "\r\n";
		            	sInput += line;
		            }    
		            read.close();
		        }					
				iBot_Test_Output(sInput,response,map);	
	        }
			
			//use the wrong API Content type
			else if(email.getEmailBody().equals("CINDERELLA-FALSE"))
			{
				String sInput = "Please use the right Content Type!";
				iBot_Test_Output(sInput,response,map);
			}
			
			//the right way
	        else
	        {
	        	try 
	        	{
	    			String botResult = DealNLP(filename);
	    			iBotOutput(botResult,response);
	    		} 
	        	catch (InterruptedException e) 
	        	{
	    			e.printStackTrace();
	    		}	   		
	        }
		} 
		catch (TikaException e) {
			e.printStackTrace();
		}			
	}
	
	public EmailInput RevMultiData(EmailInput email,HttpServletRequest request, HttpServletResponse response,String realPath) throws IOException{
		
		String sender = "";
		String receiver = ""; 
        String sendtime = "";
        String subject = "";
        String body = "";         
        ArrayList<String> attachTemp = new ArrayList<String> ();
 
        int sizeThreshold=1024*1024; //缓存区大小
     
        File repository = new File(realPath); //缓存区目录
        long sizeMax = 1024 * 1024 * 10;//设置文件的大小为10M
        
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        diskFileItemFactory.setRepository(repository);
        diskFileItemFactory.setSizeThreshold(sizeThreshold);
        ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
        servletFileUpload.setSizeMax(sizeMax);
        System.out.println("File Path:"+realPath);
        List<FileItem> fileItems = null;
        try{
            fileItems = servletFileUpload.parseRequest(request);
            for(FileItem fileItem:fileItems)
            {
            	if(fileItem.isFormField())
            	{
            		String xmString = new String(fileItem.getFieldName().getBytes("UTF-8"));  
            		String xmlUTF8 = URLDecoder.decode(xmString, "UTF-8");  
            		String json = xmlUTF8+":"+fileItem.getString("UTF-8");
//            		System.out.println(json);
            		JSONObject jsonobj = new JSONObject(json);	
            	    if(jsonobj.has("sender")){
            	    	sender = jsonobj.getString("sender");
            	    }
            	    if(jsonobj.has("receiver")){
            	    	receiver = jsonobj.getString("receiver");
            	    }
            	    if(jsonobj.has("sendtime")){
            	    	sendtime = jsonobj.getString("sendtime");
            	    }
            	    if(jsonobj.has("subject")){
            	    	subject = jsonobj.getString("subject");
            	    }
            	    if(jsonobj.has("body")){
            	    	body = jsonobj.getString("body");
            	    }	
            	} 
            	else 
            	{
            		String filePath = fileItem.getName();         
                    if(filePath==null || filePath.trim().length()==0)
                        continue;
                    String fileName=filePath.substring(filePath.lastIndexOf(File.separator)+1);
                    
                    try {
                    	if(!fileName.equals(URLDecoder.decode(fileName,"UTF-8")))
                        {
                        	fileName = URLDecoder.decode(fileName,"UTF-8");   
                        }
    					fileItem.write(new File(realPath+File.separator+fileName));
    					attachTemp.add(realPath+File.separator+fileName);
    				} catch (Exception e) {
    					e.printStackTrace();
    				}               
				}             
            }
        }
        catch(FileUploadException e1){
            e1.printStackTrace();
        }        
		email.setSender(sender);
		email.setReceiver(receiver);
		email.setSendTime(sendtime);
		email.setEmailSubject(subject);
		email.setEmailBody(body);
		email.setEmailAttach(attachTemp);
		return email;
	}
	public EmailInput RevJsonData(EmailInput email,HttpServletRequest request) throws IOException{
	
		String sender = "";
		String receiver = ""; 
        String sendtime = "";
        String subject = "";
        String body = "";         
        ArrayList<String> attachTemp = new ArrayList<String> ();
        
		String json = readJSONString(request);
	    JSONObject jsonobj = new JSONObject(json);	
	    if(jsonobj.has("sender")){
	    	sender = jsonobj.getString("sender");
	    }
	    if(jsonobj.has("receiver")){
	    	receiver = jsonobj.getString("receiver");
	    }
	    if(jsonobj.has("sendtime")){
	    	sendtime = jsonobj.getString("sendtime");
	    }
	    if(jsonobj.has("subject")){
	    	subject = jsonobj.getString("subject");
	    }
	    if(jsonobj.has("body")){
	    	body = jsonobj.getString("body");
	    }	
	    email.setSender(sender);
		email.setReceiver(receiver);
		email.setSendTime(sendtime);
		email.setEmailSubject(subject);
		email.setEmailBody(body);
		email.setEmailAttach(attachTemp);
	    return email;
	}
	
	public String readJSONString(HttpServletRequest request) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine())!=null){
            sb.append(line);
        }
        String reqBody = sb.toString();
//      System.out.println(reqBody);
	    return reqBody;
    }
	
	public void MailTxtInput(String fileName,EmailInput email) throws IOException, TikaException
	{
		String sendInfo = email.getSender();
		String receiveInfo = email.getReceiver();
		String timeInfo = email.getSendTime();
		String subjectInfo = email.getEmailSubject();
		String bodyInfo = email.getEmailBody();
		ArrayList<String> appendInfo = email.getEmailAttach();
		Tika tika = new Tika();
		String text = "";
		if(appendInfo.isEmpty())
		{
			text = "您没有指定任何附件";
		}
		else
		{				
			for(int i = 0;i<appendInfo.size();i++)
			{
				File file = new File(appendInfo.get(i));
				int fileTag = appendInfo.get(i).lastIndexOf(File.separator);				
				String filename = appendInfo.get(i).substring(fileTag + 1,appendInfo.get(i).length());
				if(!file.exists())  
				{
					text = text+"您指定的第"+(i+1)+"个附件不存在!\n";
				}
				else
				{
					text = text+"附件:"+filename+"\r\n"+tika.parseToString(file);
					file.delete();
				}					
			}
		}
//		System.out.println(fileName);		
		File file = new File(fileName);
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
	    bw.write(text);
	    bw.close();

	    System.out.println("Done");  					
	}
	
	public String DealNLP(String filename) throws IOException, InterruptedException{
		String fileName = filename;
		File fileTemp = new File(fileName);
		String sInput = "";
		if(fileTemp.isFile() && fileTemp.exists())
		{ 
            InputStreamReader read = new InputStreamReader (new FileInputStream(fileTemp),"utf-8");
            BufferedReader in = new  BufferedReader(read);   
            String line; 
            while((line = in.readLine())!=null) 
            {   
            	line += "\r\n";
            	sInput += line;
            }    
            read.close();
        }	
//		System.out.println(sInput);
		
		byte[] temp = sInput.getBytes("UTF-8");
		sInput = new String(temp,"UTF-8");		
		String url = "http://111.207.243.70:8839/ibot/api/GetInfo";
   	 	HttpClient httpclient = new HttpClient();    	 
   	 	HttpMethod method = postMethod(url,sInput);
   	 	httpclient.executeMethod(method);
   	 	String finalStr = method.getResponseBodyAsString();
   	 	System.out.println(finalStr);
		return finalStr;

	}
	public HttpMethod postMethod(String url,String sInput) throws IOException{
        PostMethod post = new PostMethod(url);
        post.setRequestHeader("Accept", "application/json");
        post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        post.setRequestBody(sInput);
        post.releaseConnection();
        return post;
    }	

	public void iBotOutput(String botResult,HttpServletResponse response) throws JSONException
	{
		try {		
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json; charset=UTF-8");
			response.setHeader("Access-Control-Allow-Origin", "*");			
			System.out.println("---------------输出ing---------------");
			response.getOutputStream().write(botResult.getBytes("UTF-8"));			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void iBot_Test_Output(String botResult,HttpServletResponse response, Map<String, String> map) throws JSONException
	{
		try {		
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json; charset=UTF-8");
			response.setHeader("Access-Control-Allow-Origin", "*");			
			System.out.println("-----------------进入彩蛋-----------------");
			JSONStringer stringer = new JSONStringer();
			try 
			{
				stringer.object().key("headers").value(map).key("content").value(botResult).endObject(); 
				response.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
			} 
			catch (IOException e) {
				e.printStackTrace();
			}			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
