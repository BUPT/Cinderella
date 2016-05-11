<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>有附件演示</title>
	<link rel="icon" href="favicon.ico" type="image/x-icon" />
		<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
	<link rel="stylesheet" href="css/showResult.css" />
	<link rel="stylesheet" href="css/header.css" />
	<link rel="stylesheet" href="css/footer.css" />
	<script type="text/javascript" src="js/jquery-1.11.0.js"></script>
	<script type="text/javascript" src="js/ajaxfileupload.js"></script>
	<script type="text/javascript" src="js/subFile.js"></script>
	
</head>
<body>
	<header>
		<a href="index.jsp"><span id="logo">iBot</span></a>
		<ul class="subtitle clearfix">
			<li><a href="index.jsp">首页</a></li>
			<li><a href="#" class="active-page">演示</a></li>
			<li><a href="workProject.jsp">开发者</a></li>
			<li>关于</li>
			<li><a href="login.jsp">进入我的邮箱</a></li>
		</ul>			
	</header>
	<div class="isAttach">
		<div class="isAttach-inner clearfix">					
			<p class="active"><a href="#">有附件上传</a></p>
			<p><a href="showResult2.jsp">无附件上传</a></p>				
		</div>				
	</div>
	<div class="all-container">
		
		<div class="form-container">
			<form method="post" class="bootstrap-frm" enctype="multipart/form-data">					
				<label>
					<span>发件人：</span>
					<input id="sender" type="text" name="sender" value="wnbupt0916@gmail.com"/>
				</label>
				<label>
					<span>收件人：</span>
					<input id="receiver" type="text" name="receiver" value="bp@pre-angel.com"/>
				</label>
				<label>
					<span>发件时间：</span>
					<input id="sendtime" type="text" name="sendtime" value="2015-12-16 10:43" />
				</label>
				<label>
					<span>邮件主题：</span>
					<input id="subject" type="text" name="subjest" value="还你我一片绿色——项目融资需求" />
				</label>
				<label>
					<span>邮件正文：</span>
					<textarea id="body" name="body">您好，谢谢观看！有意请联系本人</textarea>
				</label>
				<label>
					<span>邮件附件：</span>
					<input type="file" name="file1" id="uploadFile1"/>
				</label>
				<label class="clearfix">
					<span>&nbsp;</span>
					<input type="button" class="button" value="提交邮件" onclick="fun()"/>
				</label>				
			</form>
		</div>
		<div class="divide-line">				
			<hr class="dot-line-gray">
			<span class="title">分析结果</span>
		</div>
		<div class="table-container">
			<table class="altrowstable" id="alternatecolor">
				<tr class="evenrowcolor">
					<th class="first-column">参数名</th>
					<th>抽取结果</th>
				</tr>
				<tr class="oddrowcolor">
					<td class="first-column">公司地点</td>
					<td id="city">null</td>
				</tr>
				<tr class="evenrowcolor">
					<td class="first-column">项目名称</td>
					<td id="startup">null</td>
				</tr>
				<tr class="oddrowcolor">
					<td class="first-column">公司名称</td>
					<td id="company">null</td>
				</tr>
				<tr class="evenrowcolor">
					<td class="first-column">创始人</td>
					<td id="founders">null</td>
				</tr>
				<tr class="oddrowcolor">
					<td class="first-column">融资金额</td>
					<td id="money">null</td>
				</tr>
				<tr class="evenrowcolor">
					<td class="first-column">换股比例</td>
					<td id="equity">null</td>
				</tr>
				<tr class="oddrowcolor">
					<td class="first-column">所属领域</td>
					<td id="industries">null</td>
				</tr>
			</table>
		</div>
	</div>
	<footer>
		<p>iBot智能邮件处理服务系统</p>
		<p>Copyright 2016 Cinderella. All Rights Reserved.</p>
		<p>通讯地址：北京市海淀区西土城10号北京邮电大学</p>
	</footer>
</body>
</html>