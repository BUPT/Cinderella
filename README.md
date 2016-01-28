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
      <td>地点</td>
      <td>String</td>
      <td>公司所处城市</td>
   </tr>
   <tr>
      <td>项目名称</td>
      <td>String</td>
      <td>项目名称</td>
   </tr>
   <tr>
      <td>公司名称</td>
      <td>String</td>
      <td>公司名称</td>
   </tr>
   <tr>
      <td>成立者</td>
      <td>String</td>
      <td>公司的成立者</td>
   </tr>
   <tr>
      <td>融资额度</td>
      <td>int</td>
      <td>公司需要资金支持的额度</td>
   </tr>
   <tr>
      <td>出让股权</td>
      <td>string</td>
      <td>公司愿意付出的股权份额</td>
   </tr>
   <tr>
      <td>行业</td>
      <td>String</td>
      <td>公司所属领域</td>
   </tr>
   <tr>
      <td>置信程度</td>
      <td>double</td>
      <td>取值范围在0-1之间，取值越大表示本项目的投资价值越高</td>
   </tr>
</table>

##### 返回示例
{"地点":"北京","项目名称":"农夫之家","公司名称":"北京市农业科技有限公司","成立者":"张三","融资额度":30000,"出让股权":"20%","行业":"农业","置信程度":0.5}
##### curl示例
	curl -F uploadFiles=@D:\ibotest\test.ppt -F uploadFiles=@D:\ibotest\test.doc -H "sender:346786495@qq.com" -H "receiver:wnbupt@qq.com" -H "sendtime:2015-12-16 10:43" -H "subject:测试demo" -H "body:快塞给我一封邮件吧！" http://111.207.243.70:8088/IbotInfo/GetInfo
##### curl返回示例	
	{
	    "地点": "北京",
	    "项目名称": "农夫之家",
	    "公司名称": "北京市农业科技有限公司",
	    "成立者": "张三",
	    "融资额度": 30000,
	    "出让股权": "20%",
	    "行业": "农业",
	    "置信程度": 0.5
	}

















