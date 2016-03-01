# ibot

a bot that helps people to summary document in different way.

##### 版本
0.1.0
##### 访问地址
http://111.207.243.70:8838/SimpleRobot/GetInfo

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
      <td>346786495@qq.com</td>
      <td>发送邮箱的地址</td>
   </tr>
   <tr>
      <td>receiver</td>
      <td>String</td>
      <td>是</td>
      <td>wnbupt@qq.com</td>
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
      <td>测试demo</td>
      <td>邮件主题</td>
   </tr>
   <tr>
      <td>body</td>
      <td>String</td>
      <td>是</td>
      <td>快塞给我一封邮件吧！</td>
      <td>邮件正文</td>
   </tr>
   <tr>
      <td>uploadFiles</td>
      <td>String数组</td>
      <td>是</td>
      <td>{"D:\\ibotest\\test.ppt","D:\\ibotest\\test.doc"}</td>
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
      <td>int</td>
      <td>公司需要资金支持的额度</td>
   </tr>
   <tr>
      <td>equity</td>
      <td>string</td>
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
	curl -H "Content-Type:multipart/form-data" -F uploadFiles=@D:\ibotest\test_email.pdf -F "sender=Betty Wang<wnbupt0916@gmail.com>" -F "receiver=bp <bp@pre-angel.com>" -F "sendtime=2015-12-16 10:43" -F "subject=垃圾分类信息化社区平台" -F "body=您好，谢谢您的帮助与支持！" http://111.207.243.70:8838/SimpleRobot/GetInfo
	
##### 返回示例	
	{
	    "city": "北京",
	    "startup": "农夫之家",
	    "company": "北京市农业科技有限公司",
	    "founders": [
	        "张三",
	        "李四",
	        "王五"
	    ],
	    "money": 30000,
	    "equity": "20%",
	    "industries": [
	        "农业",
	        "科技"
	    ]
	}


######无附件上传
	curl -H "Content-Type:application/json" -X POST -d '{"sender":"Betty Wang<wnbupt0916@gmail.com>","receiver":"bp <bp@pre-angel.com>","sendtime":"2015-12-16 10:43","subject":"全球领先的采购批发平台","body":"阿里巴巴集团是以马云为首的18人，于1999年在中国杭州创立，阿里巴巴(1688.com)是全球企业间(B2B)电子商务的著名品牌,为数千万网商提供海量商机信息和便捷安全的在线交易市场,也是商人们以商会友、真实互动的社区平台。"}' http://111.207.243.70:8838/SimpleRobot/GetInfo 

##### 返回示例	
{
    "city": "中国",
    "startup": "Betty",
    "company": "阿里巴巴",
    "founders": [
        "@",
        "马云"
    ],
    "money": 30000,
    "equity": "20%",
    "industries": [
        "附件",
        "发件人"
    ]
}














