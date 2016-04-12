# Cinderella

Cinderella(a woman whose merits were not been recognized but who then achieves sudden success and recognition) is a bot that does paper works in order to helps people to summary document in different way.

##### 版本
0.1.0
##### 访问地址
http://111.207.243.70:8838/IbotNLP/GetInfo

##### 访问方式  
POST

##### 请求参数
<table class="table table-bordered table-striped table-condensed">
   <tr>
      <td>参数名</td>
      <td>数据类型</td>
      <td>是否必须</td>
      <td>示例值</td>
      <td>描述</td>
   </tr>
   <tr>
      <td>sender</td>
      <td>String</td>
      <td>是</td>
      <td>Betty Wang <wnbupt0916@gmail.com></td>
      <td>发送邮箱的地址</td>
   </tr>
   <tr>
      <td>receiver</td>
      <td>String</td>
      <td>是</td>
      <td>bp <bp@pre-angel.com></td>
      <td>接收邮箱的地址</td>
   </tr>
   <tr>
      <td>sendtime</td>
      <td>String</td>
      <td>是</td>
      <td>2015-12-16 10:43</td>
      <td>邮件的发送时间</td>
   </tr>
   <tr>
      <td>subject</td>
      <td>String</td>
      <td>是</td>
      <td>全球领先的采购批发平台——阿里巴巴</td>
      <td>邮件主题</td>
   </tr>
   <tr>
      <td>body</td>
      <td>String</td>
      <td>是</td>
      <td>阿里巴巴集团是以马云为首的18人，于1999年在中国杭州创立，阿里巴巴(1688.com)是全球企业间(B2B)电子商务的著名品牌,为数千万网商提供海量商机信息和便捷安全的在线交易市场,也是商人们以商会友、真实互动的社区平台。</td>
      <td>邮件正文</td>
   </tr>
   <tr>
      <td>uploadFiles</td>
      <td>String数组</td>
      <td>是</td>
      <td>{"D:\\ibotest\\还你我一片绿色商业计划书.pdf"}</td>
      <td>附件在本地的路径，可以上传多个附件</td>
   </tr>
</table>

##### 返回参数 
<table class="table table-bordered table-striped table-condensed">
   <tr>
      <td>参数名</td>
      <td>数据类型</td>
      <td>描述</td>
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
      <td>industries</td>
      <td>String数组</td>
      <td>公司所属领域</td>
   </tr>
</table>

##### curl示例
######有附件上传
	curl -H "Content-Type:multipart/form-data" -F uploadFiles=@D:\ibotest\还你我一片绿色商业计划书.pdf -F "sender=Betty Wang <wnbupt0916@gmail.com>" -F "receiver=bp <bp@pre-angel.com>" -F "sendtime=2015-12-16 10:43" -F "subject=还你我一片绿色——项目融资需求" -F "body=您好，谢谢观看！有意请联系本人" http://111.207.243.70:8838/SimpleRobot/GetInfo
	
##### 返回示例	
	{
	    "city": "none",
	    "startup": "还你我一片绿色",
	    "company": "none",
	    "founders": [
	        "王永",
		"北信科",
		"王雁茂"
	    ],
	    "money": "100万",
	    "equity": "10%",
	    "industries": [
	       "废品",
	       "废品交易",
	       "循环经济",
	       "垃圾减量补贴",
	       "垃圾分类",
	       "互联网+",
	       "回收员",
	       "积分商城",
	       "互联网+环保",
	       "分类"
	    ]
	}

######无附件上传
	curl -H "Content-Type:application/json" -X POST -d '{"sender":"Betty Wang<wnbupt0916@gmail.com>","receiver":"bp <bp@pre-angel.com>","sendtime":"2015-12-16 10:43","subject":"全球领先的采购批发平台","body":"阿里巴巴集团是以马云为首的18人，于1999年在中国杭州创立，阿里巴巴(1688.com)是全球企业间(B2B)电子商务的著名品牌,为数千万网商提供海量商机信息和便捷安全的在线交易市场,也是商人们以商会友、真实互动的社区平台。"}' http://111.207.243.70:8838/SimpleRobot/GetInfo 

##### 返回示例	
	{
	    "city": "none",
	    "startup": "none",
	    "company": "none",
	    "founders": [
	        "马云"
	    ],
	    "money": "none",
	    "equity": "none",
	    "industries": [
	        "全球",
	        "阿里巴巴"
	    ]
	}














