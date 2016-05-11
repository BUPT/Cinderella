$(document).ready(function() {
	var count=decodeURI(getQueryString("value"));//获取传过来的值，并解码
	var b = new Base64(); 
	count = b.decode(count);
	var username = decodeURI(getQueryString("user"));
	username = b.decode(username)
	document.getElementById("email_user").innerHTML = username;
	
	var datatosend = "{\"REQUEST\":\""+count+"\",\"USER\":\""+username+"\"}";
	$.ajax({
		type:"post",//方式是post或是get，与后台人员协商，一般为get
		url:'GetEmailResult',//处理的后台action的URL
		data:datatosend,
		dataType:"json",//不用管
		success:function(data)
		{
			if(data.STATE=="SUCCESS")
			{
				for(var i=0;i<data.DATA.length;i++)
				{
					var outdiv = document.createElement("div");
					outdiv.className = "result_wrap";
					document.getElementsByClassName("all-container")[0].appendChild(outdiv);
					var img_node = document.createElement("img");
					img_node.setAttribute("src","img/down.png");
					img_node.className = "subject-flag";
					var p_node = document.createElement("p");
					p_node.className = "subject_title";
					p_node.innerHTML = data.DATA[i].subject;
					
					var table_node = document.createElement("table");
					table_node.className="altrowstable";
					table_node.id = "alternatecolor";
					var tr_node_begin = document.createElement("tr");
					tr_node_begin.className= "evenrowcolor";
					var th_node_1 = document.createElement("th");
					var th_node_2 = document.createElement("th");
					th_node_1.className = "first-column";
					th_node_1.innerHTML = "参数名";
					th_node_2.innerHTML = "抽取结果";
					tr_node_begin.appendChild(th_node_1);
					tr_node_begin.appendChild(th_node_2);
					table_node.appendChild(tr_node_begin);
					
					for(var j=1;j<8;j++)
					{
						var tr_node = document.createElement("tr");
						var td_node_1 = document.createElement("td");
						td_node_1.className="first-column";
						var td_node_2 = document.createElement("td");
						tr_node.appendChild(td_node_1);
						tr_node.appendChild(td_node_2);
						table_node.appendChild(tr_node);
						if(j%2==0)
						{
							tr_node.className="evenrowcolor";
						}
						else
						{
							tr_node.className="oddrowcolor";
						}
					}
					outdiv.appendChild(img_node);
					outdiv.appendChild(p_node);
					outdiv.appendChild(table_node);			
					outdiv.getElementsByTagName("td")[0].innerHTML = "公司地点";
					outdiv.getElementsByTagName("td")[1].id = "city";
					outdiv.getElementsByTagName("td")[1].innerHTML=data.DATA[i].location;
					outdiv.getElementsByTagName("td")[2].innerHTML="项目名称";
					outdiv.getElementsByTagName("td")[3].id="startup";
					outdiv.getElementsByTagName("td")[3].innerHTML=data.DATA[i].projectName;
					outdiv.getElementsByTagName("td")[4].innerHTML="公司名称";
					outdiv.getElementsByTagName("td")[5].id="company";
					outdiv.getElementsByTagName("td")[5].innerHTML=data.DATA[i].companyName;
					outdiv.getElementsByTagName("td")[6].innerHTML="创始人";
					outdiv.getElementsByTagName("td")[7].id="founders";
					outdiv.getElementsByTagName("td")[7].innerHTML=data.DATA[i].founders;
					outdiv.getElementsByTagName("td")[8].innerHTML="融资金额";
					outdiv.getElementsByTagName("td")[9].id="money";
					outdiv.getElementsByTagName("td")[9].innerHTML=data.DATA[i].money;
					outdiv.getElementsByTagName("td")[10].innerHTML="换股比例";
					outdiv.getElementsByTagName("td")[11].id="equity";
					outdiv.getElementsByTagName("td")[11].innerHTML=data.DATA[i].equity;
					outdiv.getElementsByTagName("td")[12].innerHTML="所属领域";
					outdiv.getElementsByTagName("td")[13].id="industries";
					outdiv.getElementsByTagName("td")[13].innerHTML=data.DATA[i].area;
													
				}
				
				document.getElementById("download").setAttribute("href",data.LINK);
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {//这是错误处理，会在console口输出一些状态号帮助我们查看错误原因
			console.log(XMLHttpRequest.status);
			console.log(XMLHttpRequest.readyState);
			console.log(textStatus);
		}
	});
});
function getQueryString(name)
{
	//匹配这样的类似参数,但是他只取name参数部分 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	//这里是开始匹配，找到了返回对应url值，没找到返回null。
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}