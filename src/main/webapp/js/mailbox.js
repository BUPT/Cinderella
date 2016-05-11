$(document).ready(function(){
	var username=decodeURI(getQueryString("value"));//获取传过来的值，并解码
	var b = new Base64(); 
	username = b.decode(username);
    document.getElementById("email_user").innerHTML = username;
	sendPost(username);
});
function sendPost(username){
	var pagenum = document.getElementById("current_page").innerHTML;
	var datatosend = "{\"PAGE\":\""+pagenum+"\",\"USER\":\""+username+"\"}";
	console.log(datatosend);
	$.ajax({
		type:"post",//方式是post或是get，与后台人员协商，一般为get
		url:'GetEmailList',//处理的后台action的URL
		data:datatosend,
		dataType:"json",//不用管
		success:function(data)
		{
			if(data.STATE=="SUCCESS")
			{
				document.getElementById('email_num').innerHTML = data.NUMBER;
				document.getElementById('all_page').innerHTML = Math.floor(data.NUMBER/20)+1;
				for(var i=0;i<data.DATA.length;i++)
				{			
					var outdiv = document.createElement("div");
					outdiv.className = "email_wrap";
					outdiv.setAttribute("data-id",data.DATA[i].id);
					document.getElementsByClassName("all-container")[0].appendChild(outdiv);
					var inputbox = document.createElement("input");
					inputbox.type="checkbox";
					var pnode1 = document.createElement("p");
					pnode1.className="sender_list";
					pnode1.innerHTML = cutstr(data.DATA[i].sender,20);
					var pnode2 = document.createElement("p");
					pnode2.className="subject_list";
					pnode2.innerHTML = cutstr(data.DATA[i].subject,60);
					var pnode3 = document.createElement("p");
					pnode3.className="time_list";
					pnode3.innerHTML = data.DATA[i].sendtime;
					var pnode4 = document.createElement("p");
					pnode4.className="attach_list";
					if(data.DATA[i].Attachment == "true")
					{
						var imgnode = document.createElement("img");
						imgnode.setAttribute("src","img/retina.png");
						imgnode.className="attach_img";
						pnode4.appendChild(imgnode);
					}				
					outdiv.appendChild(inputbox);
					outdiv.appendChild(pnode1);
					outdiv.appendChild(pnode2);
					outdiv.appendChild(pnode3);
					outdiv.appendChild(pnode4);
				}
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {//这是错误处理，会在console口输出一些状态号帮助我们查看错误原因
			console.log(XMLHttpRequest.status);
			console.log(XMLHttpRequest.readyState);
			console.log(textStatus);
	    }
	});
}
function cutstr(str, len) {
    var str_length = 0;
    var str_len = 0;
    str_cut = new String();
    str_len = str.length;
    for (var i = 0; i < str_len; i++) {
        a = str.charAt(i);
        str_length++;
        if (escape(a).length > 4) {
            //中文字符的长度经编码之后大于4
            str_length++;
        }
        str_cut = str_cut.concat(a);
        if (str_length >= len) {
            str_cut = str_cut.concat("...");
            return str_cut;
        }
    }
    //如果给定字符串小于指定长度，则返回源字符串；
    if (str_length < len) {
        return str;
    }
}
$(document).ready(function(){
	$(".left-box").eq(0).click(function(){
		var current_page = document.getElementById("current_page").innerHTML;
		if(current_page == 1)
		{
			alert("this is the first page");
		}
		else
		{
			document.getElementById("current_page").innerHTML = current_page-1;
			$(".email_wrap").remove();
			var username = document.getElementById("email_user").innerHTML;
			sendPost(username);
		}
	});
	
	$(".right-box").eq(0).click(function(){
		var current_page = parseInt(document.getElementById("current_page").innerHTML);
		var all_page = parseInt(document.getElementById("all_page").innerHTML);
		if(current_page == all_page)
		{
			alert("this is the last page");
		}
		else
		{
			document.getElementById("current_page").innerHTML = current_page+1;
			$(".email_wrap").remove();
			var username = document.getElementById("email_user").innerHTML;
			sendPost(username);
		}
	});
	
	$("#startwork").click(function(){
		var sum = document.getElementsByClassName("email_wrap").length;
		var pagenum = parseInt(document.getElementById("current_page").innerHTML)-1;
		var username = document.getElementById("email_user").innerHTML;
		var count="";
		for(var i=0;i<sum;i++)
		{
			if(document.getElementsByClassName("email_wrap")[i].getElementsByTagName("input")[0].checked)
			{
				count=count+document.getElementsByClassName("email_wrap")[i].getAttribute("data-id")+",";
			}
		}
		var hrefStr=makeurl(count,username);
		window.location.href=hrefStr;//跳转到新页面
	});
	
	$("#chooseAll").click(function(){
		if(document.getElementById("chooseAll").checked)
		{
			$(".email_wrap input[type='checkbox']").each(function(){
				this.checked=true;
		    });
		}
		else
		{
			$(".email_wrap input[type='checkbox']").each(function(){
				this.checked=false;
		    });		
		}
	});
	
});
function makeurl(name,user){
	var b = new Base64();  
    var name_str = b.encode(name.substring(0, name.length-1));
    var user_str = b.encode(user);
	var encode1 = encodeURI(name_str);//encodeURI() 函数可把字符串作为 URI 进行编码。
	var encode2 = encodeURI(user_str);
	var url="mailResult.jsp";
	encode = encodeURI(url + "?value=" + encode1 +"&user="+ encode2);//这里我把值名取名为value，可以取名为其他你想要的名字，看网址就能看出是什么意思了。
	return encode;
}
function getQueryString(name) 
{
	//匹配这样的类似参数,但是他只取name参数部分 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	//这里是开始匹配，找到了返回对应url值，没找到返回null。
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}