# ibot

a bot that helps people to summary document in different way.

##### 版本
0.1.0
##### 访问地址
http://111.207.243.70:8088/IbotInfo/GetInfo

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

##### 返回示例
{"city":"北京","startup":"农夫之家","company":"北京市农业科技有限公司","founders":["张三","李四","王五"],"money":30000,"equity":"20%","industries":["农业","科技"]}
##### curl示例
######有附件上传
	curl -H "Content-Type:multipart/form-data" -F uploadFiles=@D:\ibotest\test.ppt -F uploadFiles=@D:\ibotest\test.doc -F "sender=346786495@qq.com" -F "receiver=wnbupt@qq.com" -F "sendtime=2015-12-16 10:43" -F "subject=测试demo" -F "body=快塞给我一封邮件吧" http://localhost:8080/IbotInfo/GetInfo
######无附件上传
	curl -H "Content-Type:application/json" -X POST -d {"sender":"346786495@qq.com","receiver":"wnbupt@qq.com","sendtime":"2015-12-16 10:43","subject":"测试demo","body":"快塞给我一封邮件吧！"} http://localhost:8080/IbotInfo/GetInfo
##### curl返回示例	
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
















