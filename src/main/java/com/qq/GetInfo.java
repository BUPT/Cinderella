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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.sun.jna.Library;
import com.sun.jna.Native;


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
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
		if(request.getHeader("Content-Type").indexOf("multipart/form-data")!=-1)
		{   						
			email = RevMultiData(email,request,response,realPath);				
		}
		else if(request.getHeader("Content-Type").indexOf("application/json")!=-1)
		{	
			email = RevJsonData(email,request);		    
		}		
		
		try 
		{
			MailTxtInput(filename,email);
		} 
		catch (TikaException e) {
			e.printStackTrace();
		}	
		
		BotResult botResult = new BotResult();
	    botResult = DealNLP(filename,botResult);
	    iBotOutput(botResult,response);
		
	}
	
	public EmailInput RevMultiData(EmailInput email,HttpServletRequest request, HttpServletResponse response,String realPath){
	
		String sender = null;
		String receiver = null; 
        String sendtime = null;
        String subject = null;
        String body = null;         
        ArrayList<String> attachTemp = new ArrayList<String> ();
 
        int sizeThreshold=1024*1024; //缓存区大小
     
        File repository = new File(realPath); //缓存区目录
        long sizeMax = 1024 * 1024 * 10;//设置文件的大小为10M
        
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        diskFileItemFactory.setRepository(repository);
        diskFileItemFactory.setSizeThreshold(sizeThreshold);
        ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
        servletFileUpload.setSizeMax(sizeMax);
        System.out.println(realPath);
        List<FileItem> fileItems = null;
        try{
            fileItems = servletFileUpload.parseRequest(request);
            for(FileItem fileItem:fileItems)
            {
                String filePath = fileItem.getName();         
                if(filePath==null || filePath.trim().length()==0)
                    continue;
                String fileName=filePath.substring(filePath.lastIndexOf(File.separator)+1);
                attachTemp.add(realPath+File.separator+fileName);
                try {
					fileItem.write(new File(realPath+File.separator+fileName));
				} catch (Exception e) {
					e.printStackTrace();
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
		
		String sender = null;
		String receiver = null; 
        String sendtime = null;
        String subject = null;
        String body = null;         
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
	    return reqBody;
    }
	
	public void MailTxtInput(String fileName,EmailInput email) throws IOException, TikaException
	{
//		String sendInfo = email.getSender();
//		String receiveInfo = email.getReceiver();
//		String timeInfo = email.getSendTime();
//		String subjectInfo = email.getEmailSubject();
//		String bodyInfo = email.getEmailBody();
		ArrayList<String> appendInfo = email.getEmailAttach();
		Tika tika = new Tika();
		String text = "";
		if(appendInfo.isEmpty())
		{
			text = "您没有指定任何附件";
			System.out.println(text);
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
					System.out.println(text);
				}
				else
				{
					text = text+"附件:"+filename+"\r\n"+tika.parseToString(file);
				}					
			}
		}
				
		String filename = fileName;	
		File file = new File(filename);
	    if(!file.exists()) 
	    {
		    file.createNewFile();
	    }
	    FileOutputStream fw = new FileOutputStream(file.getAbsoluteFile());
	    OutputStreamWriter bw = new OutputStreamWriter(fw, "UTF-8");
//	    bw.write("发件人："+sendInfo+"\r\n");
//	    bw.write("收件人："+receiveInfo+"\r\n"); 
//	    bw.write("发件时间："+timeInfo+"\r\n");
//	    bw.write("邮件主题："+subjectInfo+"\r\n");
//	    bw.write("邮件正文："+bodyInfo+"\r\n");
	    bw.write(text);
	    bw.close();

	    System.out.println("Done");  					
	}
	
	public BotResult DealNLP(String filename,BotResult bot) throws IOException{
//		String argu = "D:\\MyEclipse\\Workspaces\\NLPIR\\ICTCLAS2016";
		String argu = "/home/test/test/jczz/ICTCLAS2016";
		// String system_charset = "GBK";//GBK----0
		String system_charset = "UTF-8";
		int charset_type = 1;
		
		int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");
		String nativeBytes = null;

		if (0 == init_flag) {
			nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();
			System.err.println("初始化失败！fail reason is "+nativeBytes);
			return null;
		}

		String fileName = filename;
		File fileTemp = new File(fileName);
		String result = "";
		if(fileTemp.isFile() && fileTemp.exists())
		{ 
            InputStreamReader read = new InputStreamReader (new FileInputStream(fileTemp),"utf-8");
            BufferedReader in = new  BufferedReader(read);   
            String line; 
            while((line = in.readLine())!=null) 
            {   
            	result += line;
            }    
            read.close();
        }	
		String sInput = result.replace(" ","");
		
//		String nativeBytes = null;
		try {
			CLibrary.Instance.NLPIR_AddUserWord("有限公司 nt");
			CLibrary.Instance.NLPIR_AddUserWord("附件: n");
			CLibrary.Instance.NLPIR_AddUserWord("一点 n");
			CLibrary.Instance.NLPIR_AddUserWord(".pdf n");
			CLibrary.Instance.NLPIR_AddUserWord(".doc n");
			CLibrary.Instance.NLPIR_AddUserWord(".ppt n");
			CLibrary.Instance.NLPIR_AddUserWord(".pptx n");
			CLibrary.Instance.NLPIR_AddUserWord(".docx n");
			
			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);
//			System.out.println("分词结果为： " + nativeBytes);
			
			ArrayList<String> allWords = new ArrayList<String> ();
			ArrayList<String> allTags = new ArrayList<String> ();
			String[] temp = nativeBytes.split(" ");
			for(int i = 0;i < temp.length;i ++)
			{
				String[] wordtemp = temp[i].split("/");
				allWords.add(wordtemp[0]);
				allTags.add(wordtemp[1]);
			}
////			Pattern pattern = Pattern.compile("b*g"); 
////			System.out.println(pattern.matches(".*[融资|融].*", "融20百万"));  
			
			
			
			bot.setFounderName(getFounders(allWords,allTags));
			bot.setFinanceLimit(getMoney(allWords, allTags));
			bot.setTranStock(getEquity(allWords, allTags));
			bot.setCompanyName(getCompanyName(allWords, allTags));
			bot.setLocation(getLocation(allWords, allTags));
			bot.setProjectName(getProjectName(allWords, allTags));
			bot.setBizArea(getBizArea(sInput));
		
//			nativeByte = CLibrary.Instance.NLPIR_GetFileKeyWords("D:\\NLPIR\\feedback\\huawei\\5341\\5341\\产经广场\\2012\\5\\16766.txt", 10,false);
//			System.out.print("关键词提取结果是：" + nativeByte);

//			System.out.println(allWords.toString());
//			System.out.println("分词结果为： " + nativeBytes);			
//			CLibrary.Instance.NLPIR_AddUserWord("华玉米的产地来源 n");
//			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);
//			System.out.println("增加用户词典后分词结果为： " + nativeBytes);
//			
//			CLibrary.Instance.NLPIR_DelUsrWord("要求美方加强对输");
//			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);
//			System.out.println("删除用户词典后分词结果为： " + nativeBytes);
			
			CLibrary.Instance.NLPIR_Exit();
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}		
		return bot;
		
	}
	
	public interface CLibrary extends Library {
		// 定义并初始化接口的静态变量
//		CLibrary Instance = (CLibrary) Native.loadLibrary(
//				"D:\\MyEclipse\\Workspaces\\NLPIR\\ICTCLAS2016\\lib\\win64\\NLPIR", CLibrary.class);
		CLibrary Instance = (CLibrary) Native.loadLibrary(
				"/home/test/test/jczz/ICTCLAS2016/lib/linux64/libNLPIR.so", CLibrary.class);
		public int NLPIR_Init(String sDataPath, int encoding,
				String sLicenceCode);
				
		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);
		public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);
		public int NLPIR_AddUserWord(String sWord);//add by qp 2008.11.10
		public int NLPIR_DelUsrWord(String sWord);//add by qp 2008.11.10
		public String NLPIR_GetLastErrorMsg();
		public void NLPIR_Exit();
	}

	public static String transString(String aidString, String ori_encoding,
			String new_encoding) {
		try {
			return new String(aidString.getBytes(ori_encoding), new_encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Set<String> getBizArea(String sInput) throws IOException{
		int nCountKey = 0;
		String nativeByte = CLibrary.Instance.NLPIR_GetKeyWords(sInput, 10 ,false);
		System.out.print("关键词提取结果是：" + nativeByte);
		String[] nativeKey = nativeByte.split("#");
		Set<String> bizArea = new HashSet<String>();
		for(int i = 0 ; i < nativeKey.length ; i++)
		{
			bizArea.add(nativeKey[i]);
		}
//		String NKwordFile = "D:\\MyEclipse\\Workspaces\\NLPIR\\NKWords.txt";
		String NKwordFile = "/home/test/test/jczz/NKWords.txt";
		File fileTemp = new File(NKwordFile);
		String result = "";
		if(fileTemp.isFile() && fileTemp.exists())
		{ 
            InputStreamReader read = new InputStreamReader (new FileInputStream(fileTemp),"utf-8");
            BufferedReader in = new  BufferedReader(read);   
            String line; 
            while((line = in.readLine())!=null) 
            {   
            	result += line;
            }    
            read.close();
        }	
		String[] strTemp = result.split(" ");
		Set<String> NKwordSet = new HashSet<String>();
		ArrayList<String> listTemp = new ArrayList();
		
		for(int i = 0 ; i < strTemp.length ; i++)
		{
			NKwordSet.add(strTemp[i]);
		}		
//		System.out.println(NKwordSet.toString());
		for (String str : bizArea)
		{  
			if(!NKwordSet.add(str))
			{
				listTemp.add(str);
			}
		}		
		for(int i = 0 ; i < listTemp.size() ; i++)
		{
			if(bizArea.contains(listTemp.get(i)))
			{
				bizArea.remove(listTemp.get(i));
			}
		}
		return bizArea;
	}
	
	public static String getLocation(ArrayList<String> allWords,ArrayList<String> allTags){
		ArrayList<String> locaList = new ArrayList<String>();
		ArrayList<Integer> locaTags = new ArrayList<Integer>();
		for(int i = 0; i < allTags.size() ;i++)
		{
			if(allTags.get(i).equals("ns"))
			{
				locaList.add(allWords.get(i));
				locaTags.add(i);
			}
		}
		String[] flagStr = {"有限公司"};
		for(int i = 0; i<locaList.size() ;i++)
		{
			if(locaList.get(i).equals("中国")||!findNearWord(locaTags.get(i), allWords, 10, flagStr))
			{
				locaList.remove(i);
				locaTags.remove(i);
				i--;
			}
		}
		System.out.println("公司地点:"+locaList.toString());
		if(locaList.size()>0)
			return locaList.get(0);
		else
			return "none";
	}
	/**
	 * 公司名称
	 * @param allWords
	 * @param allTags
	 */
	public static String getCompanyName(ArrayList<String> allWords,ArrayList<String> allTags){
		ArrayList<String> companyList = new ArrayList<String>();
		ArrayList<Integer> companyTags = new ArrayList<Integer>();
		int index = 0;
		
		for(int i = 0; i < allTags.size() ;i++)
		{
			if(allWords.get(i).contains("有限公司"))
			{
				companyList.add(allWords.get(i));
				companyTags.add(i);
				index = i;
			}
		}
		System.out.println(companyList.toString());
		
		if(companyList.size() > 0)
		{
			String str = "";
			int count = 0;
			while(!allTags.get(index).equals("ns") && index > 0 && count <= 8)
			{
				if(allWords.get(index).equals("）")||allWords.get(index).equals(")"))
				{
					str = allWords.get(index) + str;
					index --;
					str = allWords.get(index) + str;
					index --;
					count += 2; 
				}
				str = allWords.get(index) + str;
				index --;
				count ++;
			}
			if(allTags.get(index).equals("ns"))
			{
				str = allWords.get(index) + str;
			}
			System.out.println("公司名称:"+str);
			return str;
		}
		else
		{
			return "none";
		}
	}
	/**
	 * 项目名称
	 * @param allWords
	 * @param allTags
	 */
	public static String getProjectName(ArrayList<String> allWords,ArrayList<String> allTags){
		int startFlag = 0,endFlag = 0;
		for(int i = 0 ;i < allWords.size(); i++)
		{
			if(allWords.get(i).equals("附件:"))
			{
				startFlag = i;
			}
			if(allWords.get(i).contains(".doc")||allWords.get(i).contains(".ppt")||allWords.get(i).contains(".docx")||allWords.get(i).contains(".pptx")||allWords.get(i).contains(".pdf"))
			{
				endFlag = i;
				break;
			}
		}
		String proName = "";
		for(int i = startFlag+1 ;i < endFlag; i++)
		{
			proName = proName + allWords.get(i);
		}	
		proName = proName.replace("商业","").replace("融资", "").replace("计划介绍", "").replace("概述", "").replace("计划书", "").replace("A轮", "").replace("融", "").replace("说明书","").replace("Pre-", "").replace("pre-", "");
		System.out.println("项目名称:"+ proName);
		if(proName.length() > 0)
			return proName;
		else
			return "none";
	}
	/**
	 * 公司创始人
	 * @param allWords
	 * @param allTags
	 */
	public static Set<String> getFounders(ArrayList<String> allWords,ArrayList<String> allTags){
		Set<String> founders = new HashSet<String>();
		for(int i = 0; i < allTags.size() ;i++)
		{
			if(allTags.get(i).equals("nr") && !allWords.get(i).contains("@") && !allWords.get(i).contains("万元"))
			{
				founders.add(allWords.get(i));
			}
		}
		System.out.println("公司创始人:"+founders.toString());
		return founders;
	}
	/**
	 * 公司换股比
	 * @param allWords
	 * @param allTags
	 */
	public static String getEquity(ArrayList<String> allWords,ArrayList<String> allTags){
		ArrayList<String> equityList = new ArrayList<String>();
		ArrayList<Integer> equityTags = new ArrayList<Integer>();
		for(int i = 0; i < allTags.size() ;i++)
		{
			if(allTags.get(i).equals("m"))
			{
				equityList.add(allWords.get(i));
				equityTags.add(i);
			}
		}
		Pattern p1 = Pattern.compile(".*%$");

		for(int i = 0; i < equityList.size(); i++)
		{			  
			Matcher m1 = p1.matcher(equityList.get(i));
			if(!m1.matches())
			{
				equityList.remove(i);
				equityTags.remove(i);
				i--;
			}
		}
		String [] flagStr = {"股权","换股","股","出让","股份","期权","占股"};
		for(int i = 0; i < equityTags.size(); i++)
		{			  
			if(!findNearWord(equityTags.get(i), allWords, 5, flagStr))
			{
				equityList.remove(i);
				equityTags.remove(i);
				i--;
			}
		}		
		System.out.println("股权换比:"+equityList.toString());
		System.out.println(equityList.toString());
		
		if(equityList.size()>0)
			return equityList.get(0);
		else
			return "none";
	}
	/**
	 * 公司需要的资金额度
	 * @param allWords
	 * @param allTags
	 */
	public static String getMoney(ArrayList<String> allWords,ArrayList<String> allTags){
		ArrayList<String> moneyList = new ArrayList<String>();
		ArrayList<Integer> moneyTags = new ArrayList<Integer>();
		for(int i = 0; i < allTags.size() ;i++)
		{
			if(allTags.get(i).equals("m"))
			{
				moneyList.add(allWords.get(i));
				moneyTags.add(i);
			}
		}

		Pattern p1 = Pattern.compile("[0-9]*(万|亿|百万)$");

		for(int i = 0; i < moneyList.size(); i++)
		{
			Matcher m1 = p1.matcher(moneyList.get(i));
			if(!m1.matches())
			{
				moneyList.remove(i);
				moneyTags.remove(i);
				i--;
			}
		}
		String [] flagStr = {"融资","融","轮","融资用途","销售","需求","投资","吸收","资金","增资"};
		for(int i = 0; i < moneyTags.size(); i++)
		{			  
			if(!findNearWord(moneyTags.get(i), allWords, 5, flagStr))
			{
				moneyList.remove(i);
				moneyTags.remove(i);
				i--;
			}
		}
		System.out.println("融资金额:"+moneyList.toString());
		System.out.println(moneyList.toString());
		if(moneyList.size()>0)
			return moneyList.get(0);
		else
			return "none";
	}
	
	public static boolean findNearWord(int pos,ArrayList<String> allWords,int len,String[] flagWord){
		int startPos = pos - len;
		int endPos = pos + len;
		if(startPos < 0){
			startPos = 0;
		}
		if(endPos > allWords.size()){
			endPos = allWords.size();
		}
		for(int index = 0 ; index < flagWord.length ; index ++)
		{
			for(int i = startPos ; i < endPos ; i++){
				if(allWords.get(i).equals(flagWord[index])||allWords.get(i).contains(flagWord[index]))
				{
					return true;
				}
			}
		}
		return false;		
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
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
