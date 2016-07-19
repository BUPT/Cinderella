# Cinderella

Cinderella is a bot that does paper works in order to helps people to summary document in different way.

`Cinderella - a woman whose merits were not been recognized but who then achieves sudden success and recognition`

## 版本
0.1.0

## 访问地址
http://111.207.243.70:8838/Cinderella/GetInfo

## 访问方式  
POST

## 请求参数
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

## 返回参数
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
      <td>industry</td>
      <td>String</td>
      <td>公司所属领域</td>
   </tr>
</table>

## curl示例
### 有附件上传
	curl -H "Content-Type:multipart/form-data" -F uploadFiles=@D:\ibotest\还你我一片绿色商业计划书.pdf -F "sender=Betty Wang <wnbupt0916@gmail.com>" -F "receiver=bp <bp@pre-angel.com>" -F "sendtime=2015-12-16 10:43" -F "subject=还你我一片绿色——项目融资需求" -F "body=您好，谢谢观看！有意请联系本人" http://111.207.243.70:8838/Cinderella/GetInfo

#### 返回示例
	{
	    "city": "北京",
	    "startup": "none",
	    "company": "none",
	    "founders": [
	    	"果上",
	        "王永刚",
	        "王雁茂",
	        "兰亚军",
	        "葛新权"
	    ],
	    "money": "100万",
	    "equity": "10%",
	    "industries": "电子商务"
	}

### 无附件上传
	curl -H "Content-Type:application/json" -X POST -d '{"sender":"Betty Wang<wnbupt0916@gmail.com>","receiver":"bp <bp@pre-angel.com>","sendtime":"2015-12-16 10:43","subject":"全球领先的采购批发平台","body":"阿里巴巴集团是以马云为首的18人，于1999年在中国杭州创立，阿里巴巴(1688.com)是全球企业间(B2B)电子商务的著名品牌,为数千万网商提供海量商机信息和便捷安全的在线交易市场,也是商人们以商会友、真实互动的社区平台。"}' http://111.207.243.70:8838/Cinderella/GetInfo

#### 返回示例
	{
	    "city": "杭州",
	    "startup": "none",
	    "company": "none",
	    "founders": [
	        "none"
	    ],
	    "money": "none",
	    "equity": "none",
	    "industries": "电子商务"
	}



---



## How to deploy

### 1. clone代码至本地

```shell
$ git clone git@github.com:AKAMobi/Cinderella.git
```

### 2. 安装配置项目依赖

#### 2.1 安装配置 JDK

```shell
$ sudo apt-get install openjdk-7-jdk
$ vim /etc/profile
```

在profile的最后添加以下语句：

```shell
export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
export JAVA_BIN=${JAVA_HOME}/bin
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:{JAVA_HOME}/lib:{JRE_HOME}/lib:${JRE_HOME}/lib/charsets.jar
export PATH={JAVA_HOME}/bin:{JRE_HOME}/bin:$PATH
```

执行如下命令使配置立即生效：

```shell
$ sudo source /etc/profile
```

#### 2.2 安装配置 Tomcat 7

下载tomcat安装包

```shell
$ cd /usr/local/src/
$ wget http://archive.apache.org/dist/tomcat/tomcat-7/v7.0.14/bin/apache-tomcat-7.0.14.tar.gz
```

配置 Tomcat

```shell
$ tar zxvf apache-tomcat-7.0.14.tar.gz
$ mv apache-tomcat-7.0.14 /usr/local/tomcat
$ cp -p /usr/local/tomcat/bin/catalina.sh /etc/init.d/tomcat
$ vim /etc/init.d/tomcat
```

在第二行（即`#!/bin/sh`下一行）加入以下内容：

```shell
JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
CATALINA_HOME=/usr/local/tomcat
```

修改权限：

```shell
$ chmod 755 /etc/init.d/tomcat
```

启动Tomcat：

```shell
$ service tomcat start
```

#### 2.3 安装配置 Python 依赖包

安装程序运行需要的依赖包`numpy`、`SciPy`、`sklearn`、`jieba`、`pynlpir`、`Flask`。

一般系统会自带Python，安装依赖包则是使用`pip`命令或`apt-get`命令，所有下载的依赖包最终会在`/usr/local/lib/python2.7/dist-packages`文件夹中。

```shell
$ pip install numpy
$ pip install Scipy
$ pip install sklearn
$ pip install jieba
$ pip install pynlpir
$ pip install Flask
```

* Tips： 如果使用`pip`命令安装出现问题可以换用`apt-get`命令。

### 3. 部署项目

#### 3.1 部署项目 Web 部分 

下载Maven

下载链接：http://maven.apache.org/download.cgi，选择最新版本的maven安装包，我安装时最新安装包是apache-maven-3.3.9-bin.tar.gz

解压安装

```shell
$  tar -zxvf apache-maven-3.3.9-bin.tar.gz
$  sudo mv apache-maven-3.3.9 /usr/local/
```

设置环境变量

```shell
$  vim /etc/profile
```

在文件最后添加以下内容

```shell
#set maven environment
M2_HOME=/usr/local/apache-maven-3.3.9
export M2_HOME
export MAVEN_OPTS="-Xms256m -Xmx512m"
export PATH=$M2_HOME/bin:$PATH
```

执行如下命令使配置立即生效，并检查maven是否安装成功：

```shell
$ sudo source /etc/profile
$ mvn -version
```

进入项目根目录后使用Maven打包项目

```shell
$ mvn war:war
```

将打包成功位于项目根目录`target`文件夹下的`Cinderella.war`放置在Tomcat的`webapps`目录下

```shell
$ cp source_dir /usr/local/tomcat/webapps
```

开启Tomcat服务器

```shell
$ cd /usr/local/tomcat/bin
$ ./startup.sh
```

在浏览器输入`http://your_ip_address:your-tomcat-port/Cinderella/GetInfo`

如果出现项目主页则说明配置成功。

#### 3.2 部署项目中 Python 接口部分

在`GetInfo.java`文件中实际是调用了python web接口

	sInput = new String(temp,"UTF-8");		
	String url = "http://111.207.243.70:8839/ibot/api/GetInfo";
   	HttpClient httpclient = new HttpClient();    	 
   	HttpMethod method = postMethod(url,sInput);

进入到项目根目录下的`ibot-kernel`文件夹，运行`start.py`文件

```shell
$ python start.py
```

* Tips：可修改`start.py`文件中的`port`变量值修改端口值

在浏览器输入`http://your_ip_address:your_port/ibot`

如果可以出现`welcome to ibot!`说明python web接口配置成功。
