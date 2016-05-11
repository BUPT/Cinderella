package com.qq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.mysql.jdbc.Statement;

/**
 * Servlet implementation class GetEmailNum
 */
@WebServlet("/GetEmailList")
public class GetEmailList extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String url="jdbc:mysql://111.207.243.70:3306/emailinfo?useUnicode=true&characterEncoding=utf-8";
	private static final String user="root";
	private static final String password="123456";  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetEmailList() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		String formstr = readJSONString(request);
		JSONObject a = new JSONObject(formstr);
		int num = Integer.parseInt((String) a.get("PAGE")) ;	
		String username = (String) a.get("USER");
		int startRecord = (num-1)*20;
		int endRecord = num*20;
		try 
		{
			ArrayList<String> subject = new ArrayList<>();
			ArrayList<String> sender = new ArrayList<>();
			ArrayList<String> sendtime = new ArrayList<>();
			ArrayList<String> emailID = new ArrayList<>();
			ArrayList<String> isAttach = new ArrayList<>();
			
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection conn = DriverManager.getConnection(url,user,password);
			Statement statement = (Statement) conn.createStatement();
			
			String emailName = "";
			String sql = "select * from client";
			ResultSet rSet = statement.executeQuery(sql);
			while(rSet.next())
			{
				if(rSet.getString("UserID").equals(username))
				{
					emailName = rSet.getString("EmailInfoName");
				}
			}
			sql = "select * from "+ emailName +";";
			rSet = statement.executeQuery(sql);
			int totalNum = 0;
			while (rSet.next()) {
				emailID.add(rSet.getString("emailID"));
				sender.add(rSet.getString("sender"));
				subject.add(rSet.getString("subject"));
				sendtime.add(rSet.getString("sendtime"));
				isAttach.add(rSet.getString("isAttach"));
				totalNum++;
			}
			JSONArray array = new JSONArray(); 
			//防止最后一页溢出
			if(endRecord>totalNum)
			{
				endRecord = totalNum;
			}
			for (int i = startRecord, count = endRecord; i < count; i++)
			{
				JSONObject obj = new JSONObject(); 
		        obj.put("sender", sender.get(i));
		        obj.put("subject", subject.get(i));
		        obj.put("sendtime", sendtime.get(i));
		        obj.put("id", emailID.get(i));
		        obj.put("Attachment", isAttach.get(i));
		        array.put(obj);
			}		
			JSONObject json = new JSONObject();
			json.put("STATE", "SUCCESS");
			json.put("NUMBER", totalNum);
			json.put("DATA", array);
			
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json; charset=UTF-8");			
			response.getOutputStream().write(json.toString().getBytes("UTF-8"));
		}
		catch (Exception e) {
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
}
