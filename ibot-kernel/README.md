# iBot

商业计划书邮件智能抽取系统

## 功能说明

- 支持从商业计划书文本中抽取以下信息：
	- 项目名称
	- 公司名称
	- 公司所在地
	- 融资额
	- 融资股权占比
	- 团队成员

- 支持识别商业计划书所属行业类别：
	- 电子商务
	- 社交网络
	- 其他

## 部署说明

### 环境依赖

- 系统要求：Windows/Linux/Mac OS
- Python版本: 2.7.X
- Python依赖：
	- numpy
	- SciPy
	- sklearn
	- jieba
	- pynlpir
	- Flask

	
### 启动服务

直接运行命令`python start.py`启动Flask框架服务


### 配置文件

修改`ibot.conf`配置相关功能


## 接口说明

- 服务器状态测试

	| 类别   | 说明  |
	| --------- | ----------------------------|
	| 接口 | http://host:port/ibot/|
	| 请求方式| HTTP|

- 信息接口测试

	| 类别   | 说明  |
	| --------- | ----------------------------|
	| 接口 | http://host:port/ibot/api/test/|
	| 请求方式| POST|
	| 请求类型| TEXT(text/plain)|

- 信息抽取接口

	| 类别   | 说明  |
	| --------- | ----------------------------|
	| 接口 | http://host:port/ibot/api/GetInfo/|
	| 请求方式| POST|
	| 请求类型| TEXT(text/plain)|