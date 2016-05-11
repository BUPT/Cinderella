<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>进入我的邮箱</title>
	<link rel="icon" href="favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
	<link rel="stylesheet" href="css/login.css" />
	<script type="text/javascript" src="js/jquery-1.11.0.js" ></script>
	<script type="text/javascript" src="js/login.js" ></script>
	<script type="text/javascript" src="js/base64.js" ></script>
</head>
<body>
	<img src="img/index_banner.jpg" id="all-cover"/>
	<header>
		<a href="index.jsp"><span id="logo">iBot</span></a>
		<ul class="subtitle clearfix">
			<li><a href="index.jsp">首页</a></li>
			<li><a href="showResult.jsp">演示</a></li>
			<li><a href="workProject.jsp">开发者</a></li>
			<li>关于</li>
			<li><a href="#" class="active-page">进入我的邮箱</a></li>
		</ul>
	</header>
	<div class="login-box">
		<h1>进入全新工作空间</h1>
		<form id="loginform">
			<label>
				<span>邮箱：</span>
				<input id="name" type="email" class="name" placeholder="请填入有效的邮箱地址"/>
				<img src="img/valid.png" id="email_flag"/>
			</label>
			<label>
				<span>密码：</span>
				<input id="password" type="password" class="password" />
			</label>
			<input type="button" class="disable-login-button" id="login-button" value="GO" />
		</form>	
		<div class="cover-page">
			<img src="img/loading.gif"/>
			<p>正在解析邮件，请耐心等候...</p>
		</div>	
	</div>
</body>
</html>