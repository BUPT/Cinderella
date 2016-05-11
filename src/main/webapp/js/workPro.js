$(document).ready(function(){
	window.onscroll = function() 
	{
		var divObj = document.getElementsByClassName("right-wrap")[0];
		if(window.scrollY>600)
		{			
			divObj.style.position = "fixed";
			divObj.style.top = "0px";
			divObj.style.right = "123px";
			divObj.style.marginTop="10px";
		}
		else
		{
			divObj.style.position = "relative";
			divObj.style.top = "0px";
			divObj.style.right = "0px";
			divObj.style.marginTop="30px";
		}
		if(window.scrollY>1000)
		{
			document.getElementById("backtop").style.visibility="visible";
		}
		else
		{
			document.getElementById("backtop").style.visibility="hidden";
		}
	}
});
$(document).ready(function(){
	var obj1=document.getElementById("backtop").getElementsByTagName("img")[0];
	$("#backtop").hover(
		function(){
			obj1.setAttribute("src","img/moveup2.png");
			//alert("lllll");
		},
		function(){
			obj1.setAttribute("src","img/moveup.png");
		}
	);
	$("#backtop").click(function(){
		$("html").animate({"scrollTop": "0px"},300); //IE,FF
        $("body").animate({"scrollTop": "0px"},300); //Webkit
	});
});