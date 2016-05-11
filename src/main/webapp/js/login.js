$(document).ready(function(){
	$("#login-button").click(function(){
		document.getElementsByClassName("cover-page")[0].style.visibility="visible";	
		var username = document.getElementById("name").value;
		var password = document.getElementById("password").value;
		var datatosend = "{\"NAME\":\""+username+"\","+"\"PASSWORD\":\""+password+"\"}";
		
		$.ajax({
			type:"post",//方式是post或是get，与后台人员协商，一般为get
			url:'LoginSuccess',//处理的后台action的URL
			data:datatosend,
			dataType:"json",//不用管
			success:function(data)
			{
				if(data.STATE=="SUCCESS")
				{
					if(data.DATA=="success")
					{
//						alert("Connect success!");
						document.getElementsByClassName("cover-page")[0].getElementsByTagName("img")[0].style.visibility="hidden";
						document.getElementsByClassName("cover-page")[0].getElementsByTagName("p")[0].innerHTML="连接成功";
						document.getElementsByClassName("cover-page")[0].getElementsByTagName("p")[0].style.marginTop="-10px";
						setTimeout(function(){
							var hrefStr=makeurl(username);
							window.location.href=hrefStr;//跳转到新页面
						},1000);
					}
					else
					{
//						alert("Connect fail!");
						document.getElementsByClassName("cover-page")[0].getElementsByTagName("img")[0].style.visibility="hidden";
						document.getElementsByClassName("cover-page")[0].getElementsByTagName("p")[0].innerHTML="连接失败";
						document.getElementsByClassName("cover-page")[0].getElementsByTagName("p")[0].style.marginTop="-10px";
						setTimeout(function(){
							document.getElementsByClassName("cover-page")[0].style.visibility="hidden";
						},2000);
					}
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown) {//这是错误处理，会在console口输出一些状态号帮助我们查看错误原因
				console.log(XMLHttpRequest.status);
				console.log(XMLHttpRequest.readyState);
				console.log(textStatus);
		    }
		});
	});
	$("#name").blur(function(){
	    var username = document.getElementById("name").value;
		var begin_index = username.indexOf("@");
		var end_index = username.indexOf(".");
		if(begin_index == -1||end_index == -1||begin_index > end_index)
		{
			document.getElementById("email_flag").setAttribute("src","img/valid.png");
			document.getElementsByClassName("login-button")[0].disabled=true;
			document.getElementsByClassName("login-button")[0].className="disable-login-button";
		}
		else
		{
			document.getElementById("email_flag").setAttribute("src","img/right.png");
			document.getElementsByClassName("disable-login-button")[0].disabled=false;
			document.getElementsByClassName("disable-login-button")[0].className="login-button";
		}
  });
});
function makeurl(name){
	var b = new Base64();  
    var str = b.encode(name);  
	var encode=encodeURI(str);//encodeURI() 函数可把字符串作为 URI 进行编码。
	var url="mailbox.jsp";
	var encode=encodeURI(url + "?value=" + encode);//这里我把值名取名为value，可以取名为其他你想要的名字，看网址就能看出是什么意思了。
	return encode;
}