<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>开发者</title>
	<link rel="icon" href="favicon.ico" type="image/x-icon" />
		<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
	<link rel="stylesheet" href="css/workProject.css" />
	<link rel="stylesheet" href="css/header.css" />
	<link rel="stylesheet" href="css/footer.css" />
	<script type="text/javascript" src="js/jquery-1.11.0.js" ></script>
	<script type="text/javascript" src="js/workPro.js" ></script>
</head>
<body>
	<header>
		<a href="index.jsp"><span id="logo">iBot</span></a>
		<ul class="subtitle clearfix">
			<li><a href="index.jsp">首页</a></li>
			<li><a href="showResult.jsp">演示</a></li>
			<li><a href="#" class="active-page">开发者</a></li>
			<li>关于</li>
			<li><a href="login.jsp">进入我的邮箱</a></li>
		</ul>
	</header>
	<div class="all-container clearfix">
		<div class="top-wrap">
			<h1>API文档</h1>
			<p>本智能邮箱处理系统暂时仅能够对投资者的商业计划书邮箱此特定类别进行处理。用户只需要将邮件的发件人、收件人、发件时间、邮件主题、邮件正文、邮件附件组织成HTTP请求，发送给本API即可得到处理结果。</p>
		</div>
		<div class="left-wrap">
			<h2><img src="img/star.png"/>URL</h2>
			<p>http://111.207.243.70:8838/Cinderella/GetInfo</p>
			<h2><img src="img/star.png"/>HTTP Method</h2>
			<p>POST</p>
			<h2><img src="img/star.png"/>HTTP Header</h2>
			<p>
				<span>有附件上传:</span>
				<small>Content-Type:multipart/form-data</small>
			</p>
			<p>
				<span>无附件上传:</span>
				<small>Content-Type:application/json</small>
			</p>
			<h2><img src="img/star.png"/>HTTP请求body</h2>
			<table class="altrowstable">
				<tr>
					<th class="first-column">参数名</th>
					<th class="second-column">数据类型</th>
					<th class="third-column">是否必须</th>
					<th class="fourth-column">示例值</th>
					<th class="fifth-column">描述</th>
				</tr>
				<tr>
					<td class="first-column">sender</td>
					<td class="second-column">String</td>
					<td class="third-column">是</td>
					<td class="fourth-column">wnbupt0916@gmail.com</td>
					<td class="fifth-column">发送邮箱的地址</td>
				</tr>
				<tr>
					<td class="first-column">receiver</td>
					<td class="second-column">String</td>
					<td class="third-column">是</td>
					<td class="fourth-column">bp@pre-angel.com</td>
					<td class="fifth-column">接收邮箱的地址</td>
				</tr>
				<tr>
					<td class="first-column">sendtime</td>
					<td class="second-column">String</td>
					<td class="third-column">是</td>
					<td class="fourth-column">2015-12-16 10:43</td>
					<td class="fifth-column">邮件的发送时间</td>
				</tr>
				<tr>
					<td class="first-column">subject</td>
					<td class="second-column">String</td>
					<td class="third-column">是</td>
					<td class="fourth-column">全球领先的采购批发平台——阿里巴巴</td>
					<td class="fifth-column">邮件主题</td>
				</tr>
				<tr>
					<td class="first-column">body</td>
					<td class="second-column">String</td>
					<td class="third-column">是</td>
					<td class="fourth-column">阿里巴巴集团是以马云为首的18人，于1999年在中国杭州创立，阿里巴巴(1688.com)是全球企业间(B2B)电子商务的著名品牌,为数千万网商提供海量商机信息和便捷安全的在线交易市场,也是商人们以商会友、真实互动的社区平台。</td>
					<td class="fifth-column">邮件正文</td>
				</tr>
				<tr>
					<td class="first-column">uploadFiles</td>
					<td class="second-column">String数组</td>
					<td class="third-column">是</td>
					<td class="fourth-column">{"D:\\ibotest\\还你我一片绿色商业计划书.pdf"}</td>
					<td class="fifth-column">附件在本地的路径，可以上传多个附件</td>
				</tr>
			</table>
			<h2><img src="img/star.png"/>HTTP返回body</h2>
			<table class="altrowstable" id="res-table">
				<tr>
					<th>参数名</th>
					<th>数据类型</th>
					<th>描述</th>
				</tr>
				<tr>
					<td>city</td>
					<td>String</td>
					<td>公司所处城市</td>
				</tr>
				<tr>
					<td>startup</td>
					<td>String</td>
					<td>项目名称</td>
				</tr>
				<tr>
					<td>company</td>
					<td>String</td>
					<td>公司名称</td>
				</tr>
				<tr>
					<td>founders</td>
					<td>String数组</td>
					<td>公司的成立者</td>
				</tr>
				<tr>
					<td>money</td>
					<td>String</td>
					<td>公司需要资金支持的额度</td>
				</tr>
				<tr>
					<td>equity</td>
					<td>String</td>
					<td>公司愿意付出的股权份额</td>
				</tr>
				<tr>
					<td>industry</td>
					<td>String</td>
					<td>公司所属领域</td>
				</tr>
			</table>
			<h2><img src="img/star.png"/>CURL实例</h2>
			<p>有附件上传 | Sample Code</p>
			<div class="code-one">
				<p>curl -H "Content-Type:multipart/form-data" -F uploadFiles=@D:\ibotest\还你我一片绿色商业计划书.pdf -F "sender=wnbupt0916@gmail.com" -F "receiver=bp@pre-angel.com" -F "sendtime=2015-12-16 10:43" -F "subject=还你我一片绿色——项目融资需求" -F "body=您好，谢谢观看！有意请联系本人" http://111.207.243.70:8838/Cinderella/GetInfo</p>
			</div>
			<p>Response</p>
			<img src="img/code-result-1.png" id="code-result-one"/>
			<p>无附件上传 | Sample Code</p>
			<div class="code-two">
				<p>curl -H "Content-Type:application/json" -X POST -d '{"sender":"wnbupt0916@gmail.com","receiver":"bp@pre-angel.com","sendtime":"2015-12-16 10:43","subject":"全球领先的采购批发平台","body":"阿里巴巴集团是以马云为首的18人，于1999年在中国杭州创立，阿里巴巴(1688.com)是全球企业间(B2B)电子商务的著名品牌,为数千万网商提供海量商机信息和便捷安全的在线交易市场,也是商人们以商会友、真实互动的社区平台。"}' http://111.207.243.70:8838/Cinderella/GetInfo </p>
			</div>
			<p>Response</p>
			<img src="img/code-result-2.png" id="code-result-two"/>
		</div>
		<div class="right-wrap">
			<div class="right-up">
				<h2>源码下载</h2>
				<a href="https://github.com/AKAMobi/Cinderella" target="_blank">Github-Cinderella</a>
			</div>
			<div class="right-down">
				<h2>特别感谢</h2>
				<a href="http://ictclas.nlpir.org/nlpir/" target="_blank">中科院分词系统</a>
				<a href="http://tika.apache.org/" target="_blank">Apache Tika</a>
				<a href="http://hanlp.linrunsoft.com/" target="_blank">HanLP</a>
			</div>
		</div>
		
		<div class="fdiv" id="backtop">
			<img src="img/moveup.png"/>
		</div>				
	</div>
	<footer>
		<p>iBot智能邮件处理服务系统</p>
		<p>Copyright 2016 Cinderella. All Rights Reserved.</p>
		<p>通讯地址：北京市海淀区西土城10号北京邮电大学</p>
	</footer>
</body>
</html>