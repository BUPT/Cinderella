<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>批量抽取结果</title>
		<link rel="stylesheet" href="css/header.css" />
		<link rel="stylesheet" href="css/footer.css" />
		<link rel="stylesheet" href="css/mailResult.css" />
		<script type="text/javascript" src="js/jquery-1.11.0.js" ></script>
		<script type="text/javascript" src="js/mailResult.js" ></script>
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
		<div class="all-container clearfix">
			<!--  <div class="result_wrap">
				<img src="img/down.png" class="subject-flag"/>
				<p class="subject_title">邮件主题</p>
				<table class="altrowstable" id="alternatecolor">
					<tr class="evenrowcolor">
						<th class="first-column">参数名</th>
						<th>抽取结果</th>
					</tr>
					<tr class="oddrowcolor">
						<td class="first-column">公司地点</td>
						<td id="city">Text 1B</td>
					</tr>
					<tr class="evenrowcolor">
						<td class="first-column">项目名称</td>
						<td id="startup">Text 2B</td>
					</tr>
					<tr class="oddrowcolor">
						<td class="first-column">公司名称</td>
						<td id="company">Text 3B</td>
					</tr>
					<tr class="evenrowcolor">
						<td class="first-column">创始人</td>
						<td id="founders">Text 4B</td>
					</tr>
					<tr class="oddrowcolor">
						<td class="first-column">融资金额</td>
						<td id="money">Text 5B</td>
					</tr>
					<tr class="evenrowcolor">
						<td class="first-column">换股比例</td>
						<td id="equity">Text 5B</td>
					</tr>
					<tr class="oddrowcolor">
						<td class="first-column">所属领域</td>
						<td id="industries">Text 5B</td>
					</tr>
				</table>
			</div>-->			
		</div>
		<div class="all-container">
			<a href="" id="download"><input type="button" value="导出为excel表格" id="output"/></a>
		</div>	
		<footer>
			<p>iBot智能邮件处理服务系统</p>
			<p>Copyright 2016 Cinderella. All Rights Reserved.</p>
			<p>通讯地址：北京市海淀区西土城10号北京邮电大学</p>
		</footer>
	</body>
</html>
    