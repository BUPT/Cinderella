package com.qq;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.mysql.jdbc.Statement;
import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * Servlet implementation class LoginSuccess
 */
@WebServlet("/LoginSuccess")
public class LoginSuccess extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String url="jdbc:mysql://111.207.243.70:3306/emailinfo?useUnicode=true&characterEncoding=utf-8";
	private static final String user="root";
	private static final String password="123456";  
	private static final String separator = File.separator;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginSuccess() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");		
		String formstr = readJSONString(request);
		JSONObject a = new JSONObject(formstr);
		String username = (String) a.get("NAME");	
		String userPassword = (String) a.get("PASSWORD");
		
		try {
			receive(username,userPassword,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
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
	public static void receive(String username, String userPassword, HttpServletResponse response) throws Exception {
		// 准备连接服务器的会话信息
		
		int begin_index = username.indexOf("@");
		int end_index = username.indexOf(".");
		String email_flag = username.substring(begin_index+1,end_index);
			
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "pop3");		// 协议				
		String email_host = "";
		String email_port = "";
		if(email_flag.equals("sina"))
		{
			email_host = "pop.sina.com";
			email_port = "110";
		}
		else if(email_flag.equals("163")) 
		{
			email_host = "pop.163.com";	// pop3服务器
			email_port = "110";
		}
		else if(email_flag.equals("126"))
		{
			email_host = "pop.126.com";
			email_port = "110";
		}
		else if(email_flag.equals("gmail"))
		{
			email_host = "pop.gmail.com";
			email_port = "995";
			props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		}
		props.setProperty("mail.pop3.port", email_port);	
		props.setProperty("mail.pop3.host", email_host);	// pop3服务器

		// 创建Session实例对象
		Session session = Session.getInstance(props);
		Store store = session.getStore("pop3");
		try{
			store.connect(username,userPassword);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			JSONObject json = new JSONObject();
			json.put("STATE", "SUCCESS");
			json.put("DATA", "fail");	
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json; charset=UTF-8");			
			response.getOutputStream().write(json.toString().getBytes("UTF-8"));		
		}

		if(store.isConnected())
		{
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);	//打开收件箱
			System.out.println("邮件总数: " + folder.getMessageCount());
			
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection conn = DriverManager.getConnection(url,user,password);
			Statement statement = (Statement) conn.createStatement();				
			
			String sql = "select * from client;";
			ResultSet rSet = statement.executeQuery(sql);
			int count = 0;//表示数据库中已有用户数
			int currentLen = 0;//表示该用户上次登录时的邮件数目
			String tableName = "";
			String resultName = "";
			boolean flag = false;//判断用户是否存在
			
			while (rSet.next()) 
			{
				count ++;
				if (rSet.getString("UserID").equals(username))
				{
					flag = true;
					tableName = rSet.getString("EmailInfoName");
					resultName = rSet.getString("EmailResultName");
				}
			}
			
			//用户不存在，添加两张新表，并且在client表中插入记录
			if (!flag) 
			{
				tableName = "email"+count+"";
				resultName = "result"+count+"";
				sql = "insert into client values("+"\""+username+"\""+","+"\""+tableName+"\""+","+"\""+resultName+"\""+");";					
				statement.execute(sql);
								
				//判断email表是否存在
				sql = "SELECT table_name FROM information_schema.TABLES WHERE table_name ='"+tableName+"';";
				rSet = statement.executeQuery(sql);
				if(!rSet.next())
				{
					sql = "create table "+tableName+"(emailID int not null auto_increment primary key,"
							+ "sender varchar(255) not null,"
							+ "subject varchar(255) not null,"
							+ "sendtime varchar(255) not null,"
							+ "isAttach varchar(255) not null );";
					statement.execute(sql);
				}
				
				//判断result表是否存在
				sql = "SELECT table_name FROM information_schema.TABLES WHERE table_name ='"+resultName+""+"';";
				rSet = statement.executeQuery(sql);
				if(!rSet.next())
				{
					sql = "create table "+resultName+"(emailID int not null auto_increment primary key,"
							+ "location varchar(255),"
							+ "companyName varchar(255),"
							+ "projectName varchar(255),"
							+ "money varchar(255),"
							+ "equity varchar(255),"
							+ "founders varchar(255),"
							+ "area varchar(255));";
					statement.execute(sql);
				}				
				//在client表中插入记录				
				currentLen = 0;				
			}
			else
			{
				sql = "select * from "+tableName+";";					
				rSet = statement.executeQuery(sql);
				while (rSet.next()) 
				{
					currentLen ++;
				}
				
			}						
			Message[] messages = folder.getMessages();
			parseMessage(currentLen,tableName,resultName,response,folder,store,conn,messages);			
			//释放资源				
		}
		else
		{
			JSONObject json = new JSONObject();
			json.put("STATE", "SUCCESS");
			json.put("DATA", "fail");	
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json; charset=UTF-8");			
			response.getOutputStream().write(json.toString().getBytes("UTF-8"));		
		}
		
	}
	
	/**
	 * 解析邮件
	 * @param resultName 
	 * @param tableName 
	 * @param currentLen 
	 * @param response 
	 * @param store 
	 * @param folder
	 * @param conn
	 * @param messages 要解析的邮件列表
	 * @throws Exception 
	 */
	public static void parseMessage(int currentLen, String tableName, String resultName, HttpServletResponse response, Folder folder, Store store, Connection conn, Message ...messages) throws MessagingException, IOException, Exception {
		if (messages == null || messages.length < 1) 
			throw new MessagingException("未找到要解析的邮件!");
		
		/**
		 * 创建所有需要使用的文件
		 * folderName：文件存储所需要的总目录
		 * DownloadFolderName：存放下载附件的文件夹
		 * attachFolderName：存放附件txt转化结果的文件夹
		 * MaterialFolderName：存放语料处理结果的文件夹
		 */
		
		Statement statement = (Statement) conn.createStatement();
//		String folderName = "D:\\Javamail";	
		String folderName = "/home/test/test/jczz/Javamail";
		
		File foldername=new File(folderName);
		if(!foldername.exists() && !foldername.isDirectory())
		{
			foldername.mkdir();
		}		
		// 解析所有邮件
		System.out.println("currentlen:"+currentLen);
		for (int i = currentLen, count = messages.length; i < count; i++) 
		{
			String emailID = "NULL";
			String sender = "NULL";
			String subject = "NULL";
			String isAttach= "NULL";
			String sendtime = "NULL";
			String result = "NULL";
			
			MimeMessage msg = (MimeMessage) messages[i];		
			System.out.println("------------------解析第" + msg.getMessageNumber() + "封邮件-------------------- ");
			
			//解析获取到合理的邮件主题名作为文件名EmailtxtName（删除其中的空格和“：”）
			//EmailtxtName的内容是本邮件的所有信息（邮件主题、发件人、收件人、发送时间、是否包含附件、附件内容、正文内容）
			String Esubject = getSubject(msg).replace(" ", "").replace(":", "");
			String MaterialFolderName=folderName+separator+getSentID(msg, null);
			foldername=new File(MaterialFolderName);
			if(!foldername.exists() && !foldername.isDirectory())
			{
				foldername.mkdir();
			}				
			
			String EmailtxtName = MaterialFolderName+separator+"All_Info.txt";
			File f = new File(EmailtxtName);//新建一个文件对象		
			if(!f.exists())
			{
				try
				{
					f.createNewFile();
				}
				catch (IOException e)
				{      
				    e.printStackTrace();    
				}    
			}
			
			try 
			{
				emailID = msg.getMessageNumber()+"";
				System.out.println("emailID:"+emailID);
				sender = getFrom(msg);
				subject = Esubject;
				sendtime = getSentDate(msg, null);
				boolean isContainerAttachment = isContainAttachment(msg);
				BotResult bot = new BotResult();
				
				if(isContainerAttachment)
				{
					isAttach="true";
					String sql = "insert into "+tableName+" values("+"\""+emailID+"\""+","+"\""+sender+"\""+","+"\""+subject.replace("\"", "").replace("'", "")+"\""+","+"\""+sendtime+"\""+","+"\""+isAttach+"\""+");";
					statement.execute(sql);
					
					FileWriter fw=new FileWriter(f);//新建一个FileWriter			
					//AttachtxtName是下载附件txt内容的文件路径，并且要传给saveAttachment函数
					String AttachtxtName = MaterialFolderName + separator + "Attachment.txt";
					saveAttachment(msg, MaterialFolderName + separator,AttachtxtName); //保存附件
					
					InputStreamReader read = new InputStreamReader(
		                    new FileInputStream(AttachtxtName));
					BufferedReader bufferedReader = new BufferedReader(read);
	                String lineTxt = null;
	                while((lineTxt = bufferedReader.readLine()) != null){
	                	 lineTxt += "\r\n";//the key of chunks
	                     fw.write(lineTxt);
	                }	                
	                fw.write("\r\n");
	                read.close();	    
	                
//	                                        删除存储下载附件txt内容的文件
	                File tmpfile=new File(AttachtxtName);
	                if(tmpfile.exists())
	                {
	                	tmpfile.delete();
	                }   
	                fw.close();	                
	                bot = DealNLP(EmailtxtName, bot);
	                
	                String sql_result = "insert into "+resultName+" values("+"\""+emailID+"\""+","+"\""+bot.getLocation()+"\""+","+"\""+bot.getCompanyName()+"\""+","+"\""+bot.getProjectName()+"\""+","+"\""+bot.getFinanceLimit()+"\""+","+"\""+bot.getTranStock()+"\""+","+"\""+bot.getFounderName()+"\""+","+"\""+bot.getBizArea()+"\""+");";
					statement.execute(sql_result);
				}
					
				else
				{
					isAttach="false";	
					String sql = "insert into "+tableName+" values("+"\""+emailID+"\""+","+"\""+sender+"\""+","+"\""+subject.replace("\"", "").replace("'", "")+"\""+","+"\""+sendtime+"\""+","+"\""+isAttach+"\""+");";
					statement.execute(sql);
					
					FileWriter fw=new FileWriter(f);//新建一个FileWriter
					fw.write("邮件主题: " + subject+"\r\n");//将字符串写入到指定的路径下的文件中
					fw.write("发件人: " + sender +"\r\n");
					fw.write("收件人：" + getReceiveAddress(msg, null)+"\r\n");
					fw.write("发送时间：" + sendtime +"\r\n");
					StringBuffer content = new StringBuffer(1024);
					getMailTextContent(msg, content);
					fw.write("邮件正文：" + content);	
					fw.close();
					bot = DealNLP(EmailtxtName, bot);
					
					String sql_result = "insert into "+resultName+" values("+"\""+emailID+"\""+","+"\""+bot.getLocation()+"\""+","+"\""+bot.getCompanyName()+"\""+","+"\""+bot.getProjectName()+"\""+","+"\""+bot.getFinanceLimit()+"\""+","+"\""+bot.getTranStock()+"\""+","+"\""+bot.getFounderName()+"\""+","+"\""+bot.getBizArea()+"\""+");";
					statement.execute(sql_result);
	             
				}
				JSONStringer stringer = new JSONStringer();  
				try {
					 stringer.object().key("city").value(bot.getLocation()).  
				        key("startup").value(bot.getProjectName()).  
				        key("company").value(bot.getCompanyName()).  
				        key("founders").value(bot.getFounderName()).
				        key("money").value(bot.getFinanceLimit()).
				        key("equity").value(bot.getTranStock()).
				        key("industries").value(bot.getBizArea()).endObject(); 
				}
								
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				
			} 
			catch (IOException e) 
			{
		   // TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		conn.close();
		folder.close(true);
		store.close();	
		
		JSONObject json = new JSONObject();
		json.put("STATE", "SUCCESS");
		json.put("DATA", "success");	
		response.reset();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json; charset=UTF-8");			
		response.getOutputStream().write(json.toString().getBytes("UTF-8"));
	}
	
	/**
	 * 获得邮件主题
	 * @param msg 邮件内容
	 * @return 解码后的邮件主题
	 */
	public static String getSubject(MimeMessage msg) throws UnsupportedEncodingException, MessagingException {
		return MimeUtility.decodeText(msg.getSubject());
	}
	
	/**
	 * 获得邮件发件人
	 * @param msg 邮件内容
	 * @return 姓名 <Email地址>
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException 
	 */
	public static String getFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
		String from = "";
		Address[] froms = msg.getFrom();
		if (froms.length < 1)
			throw new MessagingException("没有发件人!");
		
		InternetAddress address = (InternetAddress) froms[0];
		String person = address.getPersonal();
		if (person != null) {
			person = MimeUtility.decodeText(person) + " ";
		} else {
			person = "";
		}
		from = person + "<" + address.getAddress() + ">";
		
		return from;
	}
	
	/**
	 * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人
	 * <p>Message.RecipientType.TO  收件人</p>
	 * <p>Message.RecipientType.CC  抄送</p>
	 * <p>Message.RecipientType.BCC 密送</p>
	 * @param msg 邮件内容
	 * @param type 收件人类型
	 * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
	 * @throws MessagingException
	 */
	public static String getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException {
		StringBuffer receiveAddress = new StringBuffer();
		Address[] addresss = null;
		if (type == null) {
			addresss = msg.getAllRecipients();
		} else {
			addresss = msg.getRecipients(type);
		}
		
		if (addresss == null || addresss.length < 1)
			throw new MessagingException("没有收件人!");
		for (Address address : addresss) {
			InternetAddress internetAddress = (InternetAddress)address;
			receiveAddress.append(internetAddress.toUnicodeString()).append(",");
		}
		
		receiveAddress.deleteCharAt(receiveAddress.length()-1);	//删除最后一个逗号
		
		return receiveAddress.toString();
	}
	
	/**
	 * 获得邮件发送时间
	 * @param msg 邮件内容
	 * @return yyyy年mm月dd日 星期X HH:mm
	 * @throws MessagingException
	 */
	public static String getSentDate(MimeMessage msg, String pattern) throws MessagingException {
		Date receivedDate = msg.getSentDate();
		if (receivedDate == null)
			return "";
		
		if (pattern == null || "".equals(pattern))
			pattern = "yyyy年MM月dd日 HH:mm ";
		
		return new SimpleDateFormat(pattern).format(receivedDate);
	}
	
	public static String getSentID(MimeMessage msg, String pattern) throws MessagingException {
		Date receivedDate = msg.getSentDate();
		if (receivedDate == null)
			return "";
		
		if (pattern == null || "".equals(pattern))
			pattern = "yyyyMMddHHmmss";
		
		return new SimpleDateFormat(pattern).format(receivedDate);
	}
	/**
	 * 判断邮件中是否包含附件
	 * @param msg 邮件内容
	 * @return 邮件中存在附件返回true，不存在返回false
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static boolean isContainAttachment(Part part) throws MessagingException, IOException {
		boolean flag = false;
		if (part.isMimeType("multipart/*")) {
			MimeMultipart multipart = (MimeMultipart) part.getContent();
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				String disp = bodyPart.getDisposition();
				if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
					flag = true;
				} else if (bodyPart.isMimeType("multipart/*")) {
					flag = isContainAttachment(bodyPart);
				} else {
					String contentType = bodyPart.getContentType();
					if (contentType.indexOf("application") != -1) {
						flag = true;
					}  
					
					if (contentType.indexOf("name") != -1) {
						flag = true;
					} 
				}
				
				if (flag) break;
			}
		} else if (part.isMimeType("message/rfc822")) {
			flag = isContainAttachment((Part)part.getContent());
		}
		return flag;
	}
	
	/**
	 * 判断邮件是否已读
	 * @param msg 邮件内容
	 * @return 如果邮件已读返回true,否则返回false
	 * @throws MessagingException 
	 */
	public static boolean isSeen(MimeMessage msg) throws MessagingException {
		return msg.getFlags().contains(Flags.Flag.SEEN);
	}
		
	/**
	 * 获得邮件文本内容
	 * @param part 邮件体
	 * @param content 存储邮件文本内容的字符串
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
		//如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
		boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;	
		//如果连html内容都要取得话，可以把isMimeType("text/plain")改成isMimeType("text/*")
		if (part.isMimeType("text/plain") && !isContainTextAttach) {
			content.append(part.getContent().toString());
		} else if (part.isMimeType("message/rfc822")) {	
			getMailTextContent((Part)part.getContent(),content);
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				getMailTextContent(bodyPart,content);
			}
		}
	}	
	/**
	 * 保存附件
	 * @param part 邮件中多个组合体中的其中一个组合体
	 * @param destDir  附件保存目录
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void saveAttachment(Part part, String destDir,String TikaPlace) throws UnsupportedEncodingException, MessagingException,
			FileNotFoundException, IOException {
		if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();	//复杂体邮件
			//复杂体邮件包含多个邮件体
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				//获得复杂体邮件中其中一个邮件体
				BodyPart bodyPart = multipart.getBodyPart(i);				
				
				//某一个邮件体也有可能是由多个邮件体组成的复杂体
				String disp = bodyPart.getDisposition();
				if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) 
				{
					InputStream is = bodyPart.getInputStream();
					saveFile(is, destDir, decodeText(bodyPart.getFileName()),TikaPlace);
				} 
				else if (bodyPart.isMimeType("multipart/*")) 
				{
					saveAttachment(bodyPart,destDir,TikaPlace);
				} 
				else 
				{
					String contentType = bodyPart.getContentType();
					if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
						saveFile(bodyPart.getInputStream(), destDir, decodeText(bodyPart.getFileName()),TikaPlace);
					}
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			saveAttachment((Part) part.getContent(),destDir,TikaPlace);
		}
	}
	
	/**
	 * 读取输入流中的数据保存至指定目录
	 * @param is 输入流
	 * @param fileName 文件名
	 * @param destDir 文件存储目录
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void saveFile(InputStream is, String destDir, String fileName,String Tikaplace)
			throws FileNotFoundException, IOException {
		BufferedInputStream bis = new BufferedInputStream(is);
		fileName.replace(" ", "");
		fileName.replace(":", "");
		File savePlace =new File(destDir + fileName);
		if(!savePlace.exists())      
		{    
			 try {    
				 savePlace.createNewFile();    
			    } catch (IOException e) {    			        
			        e.printStackTrace();    
			    }    
		}
		
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(savePlace));
		int len = -1;
		while ((len = bis.read()) != -1) {
			bos.write(len);
			bos.flush();
		}
		bos.close();
		bis.close();
		Tika tika = new Tika();
		File f_append=new File(Tikaplace);//新建一个TXT文件来存储附件的文本内容	
		if(!f_append.exists())
		{
			f_append.createNewFile();
		}
		try 
		{
			File attachment=new File(destDir + fileName); 
		    FileOutputStream fw = new FileOutputStream(f_append,true);//true表示在文件末尾追加  		   
		    OutputStreamWriter bw = new OutputStreamWriter(fw, "UTF-8");
		    String temp="附件:"+fileName+"\r\n"+tika.parseToString(attachment)+"\r\n";		    
		    bw.write(temp); 
		    bw.close();
		} 
		catch (TikaException e) 
		{
			e.printStackTrace();
		}
	}		
	
	/**
	 * 文本解码
	 * @param encodeText 解码MimeUtility.encodeText(String text)方法编码后的文本
	 * @return 解码后的文本
	 * @throws UnsupportedEncodingException
	 */
	public static String decodeText(String encodeText) throws UnsupportedEncodingException {
		if (encodeText == null || "".equals(encodeText)) {
			return "";
		} else {
			return MimeUtility.decodeText(encodeText);
		}
	}
	
	public static BotResult DealNLP(String filename,BotResult bot) throws IOException, InterruptedException{
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
		byte[] temp = sInput.getBytes("UTF-8");
		sInput = new String(temp,"UTF-8");		
		System.out.println(sInput);
        String[] inPythonArgs = new String[]{
                "python2",//windows下python执行路径
                "/home/test/test/AIMail_release/api.py",//python工程入口函数
                 sInput,
        };
        Process process = Runtime.getRuntime().exec(inPythonArgs, null, new File("/home/test/test/AIMail_release"));
        
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
        String line;
        String result = "";
        while ((line = in.readLine())!= null){
            System.out.println("line:"+line);
            result += line;
        }
        in.close();
        process.waitFor();
        
        JSONObject jsonobj = new JSONObject(result);  

        if(jsonobj.has("city")){
	  		  bot.setLocation(jsonobj.getString("city"));
	  	}
	    if(jsonobj.has("money")){
	  		  bot.setFinanceLimit(jsonobj.getString("money"));
	  	}
  	    if(jsonobj.has("company")){
  	    	  bot.setCompanyName(jsonobj.getString("company"));
  	    }
  	    if(jsonobj.has("startup")){
  	    	  bot.setProjectName(jsonobj.getString("startup"));
  	    }
  	    if(jsonobj.has("equity")){
  	    	  bot.setTranStock(jsonobj.getString("equity"));
  	    }
  	    if(jsonobj.has("industry")){
	    	bot.setBizArea(jsonobj.getString("industry"));
	    }
  	    if(jsonobj.has("founders")){
  	    	JSONArray jsonArray = new JSONArray();
  	    	jsonArray = jsonobj.getJSONArray("founders");
  	    	Set<String> set = new HashSet<String>();
  	    	for(int i=0;i<jsonArray.length();i++)
  	    	{
  	    		set.add(jsonArray.getString(i));
  	    	}
  	    	bot.setFounderName(set);
  	    }  	    
  	    
		return bot;
	}
	
}
