package com.qq;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.security.PublicKey;
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

import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.sis.internal.jaxb.gml.TimePeriod;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.mysql.jdbc.Statement;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Servlet implementation class GetEmailNum
 */
@WebServlet("/GetEmailResult")
public class GetEmailResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String url="jdbc:mysql://111.207.243.70:3306/emailinfo?useUnicode=true&characterEncoding=utf-8";
	private static final String user="root";
	private static final String password="123456";  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetEmailResult() {
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
		String EmailList = (String) a.get("REQUEST");		
		String username = (String) a.get("USER");
		
		ArrayList<String> subject = new ArrayList<>();
		ArrayList<String> location = new ArrayList<>();
		ArrayList<String> companyName = new ArrayList<>();
		ArrayList<String> projectName = new ArrayList<>();
		ArrayList<String> money = new ArrayList<>();
		ArrayList<String> equity = new ArrayList<>();
		ArrayList<String> founders = new ArrayList<>();
		ArrayList<String> area = new ArrayList<>();
		
		String resultName = "";
		String emailName = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection conn = DriverManager.getConnection(url,user,password);
			Statement statement = (Statement) conn.createStatement();	
			String sql = "select * from client";
			ResultSet rSet = statement.executeQuery(sql);
			while(rSet.next())
			{
				if(rSet.getString("UserID").equals(username))
				{
					resultName = rSet.getString("EmailResultName");
					emailName = rSet.getString("EmailInfoName");
				}
			}
			String[] temp = EmailList.split(",");
			for(int i = 0;i < temp.length;i ++)
			{
				int index = Integer.parseInt(temp[i]);			
				sql = "select * from "+ resultName +" where emailID="+index+""+";";
				rSet = statement.executeQuery(sql);
				while (rSet.next()) {
					location.add(rSet.getString("location"));
					companyName.add(rSet.getString("companyName"));
					projectName.add(rSet.getString("projectName"));
					money.add(rSet.getString("money"));
					equity.add(rSet.getString("equity"));
					founders.add(rSet.getString("founders"));
					area.add(rSet.getString("area"));
				}
				String sql2 = "select * from "+emailName+" where emailID="+index+""+";";
				ResultSet rSet2 = statement.executeQuery(sql2);
				while (rSet2.next()) {
					subject.add(rSet2.getString("subject"));
				}
			}
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		JSONArray array = new JSONArray(); 
		//防止最后一页溢出
		
		for (int i = 0, count = location.size(); i < count; i++)
		{
			JSONObject obj = new JSONObject(); 
			obj.put("subject", subject.get(i));
	        obj.put("location", location.get(i));
	        obj.put("companyName", companyName.get(i));
	        obj.put("projectName", projectName.get(i));
	        obj.put("money", money.get(i));
	        obj.put("equity", equity.get(i));
	        obj.put("founders", founders.get(i));
	        obj.put("area", area.get(i));
	        array.put(obj);
		}		
		String separator = File.separator;		
		String downloadPath = "download"+separator+"result.xls";
		String realPath = getServletContext().getRealPath(separator)+downloadPath;
		
		try {
			GenerateExcel(realPath,subject,location,companyName,projectName,money,equity,founders,area);
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		JSONObject json = new JSONObject();
		json.put("STATE", "SUCCESS");
		json.put("DATA", array);
		json.put("LINK",downloadPath);
//		System.out.println(json.toString());	
		response.reset();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json; charset=UTF-8");			
		response.getOutputStream().write(json.toString().getBytes("UTF-8"));		
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
	public void GenerateExcel(String downloadURL, ArrayList<String> subject, ArrayList<String> location, ArrayList<String> companyName, ArrayList<String> projectName, ArrayList<String> money, ArrayList<String> equity, ArrayList<String> founders, ArrayList<String> area) throws IOException, RowsExceededException, WriteException
	{
		File file = new File(downloadURL);
		if(!file.exists())
		{
			file.createNewFile();
		}
		WritableWorkbook workbook= Workbook.createWorkbook(new File(downloadURL));   
        WritableSheet sheet = workbook.createSheet("First Sheet",0);
        //创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
        Label title = new Label(0,0,"邮件主题");
        sheet.addCell(title);
        title = new Label(1,0,"公司地点");
        sheet.addCell(title);
        title = new Label(2,0,"项目名称");
        sheet.addCell(title);
        title = new Label(3,0,"公司名称");
        sheet.addCell(title);
        title = new Label(4,0,"创始人");
        sheet.addCell(title);
        title = new Label(5,0,"融资金额");
        sheet.addCell(title);
        title = new Label(6,0,"换股比例");
        sheet.addCell(title);
        title = new Label(7,0,"所属领域");
        sheet.addCell(title);
        
        for(int row = 1;row <= subject.size();row ++)
        {        	
        	title = new Label(0,row,subject.get(row-1));
        	sheet.addCell(title);
        	title = new Label(1,row,location.get(row-1));
        	sheet.addCell(title);
        	title = new Label(2,row,projectName.get(row-1));
        	sheet.addCell(title);
        	title = new Label(3,row,companyName.get(row-1));
        	sheet.addCell(title);
        	title = new Label(4,row,founders.get(row-1));
        	sheet.addCell(title);
        	title = new Label(5,row,money.get(row-1));
        	sheet.addCell(title);
        	title = new Label(6,row,equity.get(row-1));
        	sheet.addCell(title);
        	title = new Label(7,row,area.get(row-1));
        	sheet.addCell(title);
        }
        
        workbook.write();
        workbook.close();
	}
}
