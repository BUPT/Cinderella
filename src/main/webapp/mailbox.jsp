<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>我的邮箱</title>
		<link rel="stylesheet" href="css/mailbox.css" />
		<link rel="stylesheet" href="css/header.css" />
		<link rel="stylesheet" href="css/footer.css" />
		<script type="text/javascript" src="js/jquery-1.11.0.js" ></script>
		<script type="text/javascript" src="js/mailbox.js" ></script>
		<script type="text/javascript" src="js/base64.js" ></script>
	</head>
	<body>
		<header>
			<a href="index.jsp"><span id="logo">iBot</span><span id="email_user"></span></a>
			<ul class="subtitle clearfix">
				<li><a href="index.jsp">首页</a></li>
				<li><a href="showResult.jsp">演示</a></li>
				<li><a href="workProject.jsp">开发者</a></li>
				<li>关于</li>
				<li><a href="index.jsp">注销</a></li>
			</ul>
		</header>
		<div class="all-container">
			<img src="img/colors.png" id="first_img"/>
			<span class="top_span">尊敬的iBot用户您好，您共有</span><span id="email_num"></span><span class="top_span">封邮件</span>
			<div class="head-wrap">	
				<input type="checkbox" id="chooseAll"/>
				<span id="sender">发件人</span>
				<span id="subject">主题</span>
				<span id="time">时间</span>
				<span id="attach">有无附件</span>
			</div>
			
			<!--  <div class="email_wrap">
				<input type="checkbox"/>
				<p class="sender_list">wnbupt0916@gmail.com</p>
				<p class="subject_list">(#415148153) Gmail 转发确认 - 从 wnbupt0916@gmail.com 接收邮件</p>
				<p class="time_list">2016年4月26日 17:57</p>
				<p class="attach_list"><img src="img/retina.png" class="attach_img"/></p>
			</div>-->			
			
		</div>
		<div class="page-container clearfix">
			<input type="button" id="startwork" value="确认抽取"/>
			<div class="pagenum">
				<div class="left-box"><span><</span></div>
				<span id="current_page">1</span><span>/</span><span id="all_page"></span>
				<div class="right-box"><span>></span></div>				
			</div>
		</div>
		<footer>
			<p>iBot智能邮件处理服务系统</p>
			<p>Copyright 2016 Cinderella. All Rights Reserved.</p>
			<p>通讯地址：北京市海淀区西土城10号北京邮电大学</p>
		</footer>
	</body>
</html>

