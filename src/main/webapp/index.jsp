<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>首页</title>
		<link rel="icon" href="favicon.ico" type="image/x-icon" />
		<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" href="css/index.css" />
		<link rel="stylesheet" href="css/footer.css" />
	</head>
	<body>
		<header>
			<a href="#"><span id="logo">iBot</span></a>
			<ul class="subtitle clearfix">
				<li><a href="#" class="active-page">首页</a></li>
				<li><a href="showResult.jsp">演示</a></li>
				<li><a href="workProject.jsp">开发者</a></li>
				<li>关于</li>
				<li><a href="login.jsp">进入我的邮箱</a></li>
			</ul>
			<div class="bigname clearfix">
				<p id="Chinese-title">智能邮件处理服务系统</p>
				<p id="English-title">summary information & reply Email automaticlly</p>
				<div class="choose-button clearfix">
					<a href="showResult.jsp"><input type="button" value="查看演示"/></a>
					<a href="workProject.jsp"><input type="button" value="开发者工具"/></a>
				</div>				
			</div>
		</header>
		<div class="all-container clearfix">
			<div class="row-one clearfix">
				<div class="wrap-one">
					<span class="icon"></span>
					<h2>领域探索</h2>
					<p>iBot专注于邮件智能处理，通过抽取邮件关键信息，一目了然地将结构化数据展示给用户，并进行邮件的自动回复。</p>
				</div>
				<div class="wrap-two">
					<span class="icon"></span>
					<h2>目标用户</h2>
					<p>每天在打开创业者的融资需求邮件、下载附件、阅读商业计划书上耗费大量时间，迫切希望邮件处理智能化的投资人。</p>					
				</div>
			</div>
			<div class="row-two clearfix">
				<div class="wrap-three">
					<span class="icon"></span>
					<h2>核心技术</h2>
					<p>支持多种附件格式的文本规格化、大数据语料训练的NLP语义识别、模拟人脑进行分析学习的神经网络。</p>
				</div>
				<div class="wrap-four">
					<span class="icon"></span>
					<h2>即刻可用</h2>
					<p>对于工程开发者，建议直接使用开放邮箱处理API。对于代码小白，推荐登录邮箱体验智能批量处理。</p>
				</div>
			</div>			
		</div>
		<footer>
			<p>iBot智能邮件处理服务系统</p>
			<p>Copyright 2016 Cinderella. All Rights Reserved.</p>
			<p>通讯地址：北京市海淀区西土城10号北京邮电大学</p>
		</footer>
	</body>
</html>
