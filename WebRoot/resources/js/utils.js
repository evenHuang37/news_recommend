/*---------------------------------------------------------------------------*\
|                          tabPlugin 公共函数-工具类                           |
|-----------------------------------------------------------------------------|
|                Created by  王彬    yysam@yeah.net                           |
|-----------------------------------------------------------------------------|
|               Copyright (c) 2006 - ?  Emil A Eklund                         |
|-----------------------------------------------------------------------------|
|                                                                             |
|-----------------------------------------------------------------------------|
| 2006-08-31 | 建立该脚本												      |
|-----------------------------------------------------------------------------|
| If you find any bugs,you may send Emails to creater's MailAddress           |
\----------------------------------------------------------------------------*/

/*
==================================================================
getRandom:得到随机数
==================================================================
*/
function getRandom(length){ 
	if(!length){length=5;}
	var j=1;
	for(var i=0;i<length;i++){j*=10;}
	return Math.round(Math.random()*j);
}
/*
==================================================================
LTrim(string):去除左边的空格
==================================================================
*/
function leftTrim(str)
{
    var whitespace = new String(" \t\n\r");
    var s = new String(str);
    
    if (whitespace.indexOf(s.charAt(0)) != -1)
    {
        var j=0, i = s.length;
        while (j < i && whitespace.indexOf(s.charAt(j)) != -1)
        {
            j++;
        }
        s = s.substring(j, i);
    }
    return s;
}
/*
==================================================================
RTrim(string):去除右边的空格
==================================================================
*/
function rightTrim(str)
{
    var whitespace = new String(" \t\n\r");
    var s = new String(str);

    if (whitespace.indexOf(s.charAt(s.length-1)) != -1)
    {
        var i = s.length - 1;
        while (i >= 0 && whitespace.indexOf(s.charAt(i)) != -1)
        {
            i--;
        }
        s = s.substring(0, i+1);
    }
    return s;
}
/*
==================================================================
Trim(string):去除前后空格
==================================================================
*/
function trim(str)
{
    return rightTrim(leftTrim(str));
}
/*
================================================================================
XMLEncode(string):对字符串进行XML编码
================================================================================
*/
function getXMLEncode(str)
{
	str=trim(str);
	str=str.replace("&","&amp;");
	str=str.replace("<","&lt;");
	str=str.replace(">","&gt;");
	str=str.replace("'","&apos;");
	str=str.replace("\"","&quot;");
	return str;
}
/*
================================================================================
取指定对象的x坐标
================================================================================
*/	
function getX(e){
  var l=e.offsetLeft;
  while(e=e.offsetParent){
    l+=e.offsetLeft;
    }
  return(l);
  }
/*
================================================================================
取指定对象的y坐标
================================================================================
*/
function getY(e){
  var t=e.offsetTop;
  while(e=e.offsetParent){
    t+=e.offsetTop;
    }
  return(t);
}/*
================================================================================
取指定对象的可视x坐标
================================================================================
*/	
function getAvailX(e){
  var l=e.offsetLeft-e.scrollLeft;
  while(e=e.offsetParent){ 
    l+=e.offsetLeft-e.scrollLeft;
    }
  return(l);
  }
/*
================================================================================
取指定对象的可视y坐标
================================================================================
*/
function getAvailY(e){
  var t=e.offsetTop-e.scrollTop;
  while(e=e.offsetParent){
    t+=e.offsetTop-e.scrollTop;
    }
  return(t);
}
/*
================================================================================
得到自己上级对象
the_ele 子对象
the_tag 父对象名称
================================================================================
*/
function get_Element(the_ele,the_tag){
	the_tag = the_tag.toLowerCase();
	if(the_ele.tagName.toLowerCase()==the_tag)return the_ele;
	while(the_ele=the_ele.offsetParent){
		if(the_ele.tagName.toLowerCase()==the_tag)return the_ele;
	}
	return(null);
}	

/*
================================================================================
取得传值的函数
================================================================================
*/
function getRequestParameter(qs)
{
	var s = location.href.replace("?","?&").split("&");
	var re = "";
	for(i=1;i<s.length;i++)
	(s[i].indexOf(qs+"=")==0)&&(re=s[i].replace(qs+"=",""))
	return re;
}
	/*
================================================================================
验证类函数
================================================================================
*/
function isEmpty(obj)
{
    if(trim(obj.value)=="")
    {
        alert("字段不能为空。");        
        if(obj.disabled==false && obj.readOnly==false)
        {
            obj.focus();
        }
    }
}
/*
================================================================================
IsInt(string,string,int or string):(测试字符串,+ or - or empty,empty or 0)
功能：判断是否为整数、正整数、负整数、正整数+0、负整数+0
================================================================================
*/
function IsInt(objStr,sign,zero)
{
    var reg;    
    var bolzero;    
    
    if(trim(objStr)=="")
    {
        return false;
    }
    else
    {
        objStr=objStr.toString();
    }    
    
    if((sign==null)||(trim(sign)==""))
    {
        sign="+-";
    }
    
    if((zero==null)||(trim(zero)==""))
    {
        bolzero=false;
    }
    else
    {
        zero=zero.toString();
        if(zero=="0")
        {
            bolzero=true;
        }
        else
        {
            alert("检查是否包含0参数，只可为(空、0)");
        }
    }
    
    switch(sign)
    {
        case "+-":
            //整数
            reg=/(^-?|^\+?)\d+$/;            
            break;
        case "+": 
            if(!bolzero)           
            {
                //正整数
                reg=/^\+?[0-9]*[1-9][0-9]*$/;
            }
            else
            {
                //正整数+0
                //reg=/^\+?\d+$/;
                reg=/^\+?[0-9]*[0-9][0-9]*$/;
            }
            break;
        case "-":
            if(!bolzero)
            {
                //负整数
                reg=/^-[0-9]*[1-9][0-9]*$/;
            }
            else
            {
                //负整数+0
                //reg=/^-\d+$/;
                reg=/^-[0-9]*[0-9][0-9]*$/;
            }            
            break;
        default:
            alert("检查符号参数，只可为(空、+、-)");
            return false;
            break;
    }
    
    var r=objStr.match(reg);
    if(r==null)
    {
        return false;
    }
    else
    {        
        return true;     
    }
}

/*
================================================================================
IsFloat(string,string,int or string):(测试字符串,+ or - or empty,empty or 0)
功能：判断是否为浮点数、正浮点数、负浮点数、正浮点数+0、负浮点数+0
================================================================================
*/
function isFloat(objStr,sign,zero)
{
    var reg;    
    var bolzero;    
    
    if(trim(objStr)=="")
    {
        return false;
    }
    else
    {
        objStr=objStr.toString();
    }    
    
    if((sign==null)||(trim(sign)==""))
    {
        sign="+-";
    }
    
    if((zero==null)||(trim(zero)==""))
    {
        bolzero=false;
    }
    else
    {
        zero=zero.toString();
        if(zero=="0")
        {
            bolzero=true;
        }
        else
        {
            alert("检查是否包含0参数，只可为(空、0)");
        }
    }
    
    switch(sign)
    {
        case "+-":
            //浮点数
            reg=/^((-?|\+?)\d+)(\.\d+)?$/;
            break;
        case "+": 
            if(!bolzero)           
            {
                //正浮点数
                reg=/^\+?(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/;
            }
            else
            {
                //正浮点数+0
                reg=/^\+?\d+(\.\d+)?$/;
            }
            break;
        case "-":
            if(!bolzero)
            {
                //负浮点数
                reg=/^-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/;
            }
            else
            {
                //负浮点数+0
                reg=/^((-\d+(\.\d+)?)|(0+(\.0+)?))$/;
            }            
            break;
        default:
            alert("检查符号参数，只可为(空、+、-)");
            return false;
            break;
    }
    
    var r=objStr.match(reg);
    if(r==null)
    {
        return false;
    }
    else
    {        
        return true;     
    }
}

/*
================================================================================
检验中文字符长度
================================================================================
*/
String.prototype.clength = function() {
    var cArr = this.match(/[^\x00-\xff]/ig);
   return this.length + (cArr == null ? 0 : cArr.length);
}
/*
================================================================================
删除数组元素
================================================================================
*/ 
Array.prototype.del = function(AItem){
	var value;
	for ( var i = 0;i < this.length; i++ )
		if ( this[i] == AItem ) {
      		value=i;
     	}
    if(!i){return;}
    var ret=new Array();
    for(var i = value;i < this.length-1; i++){ 
    	this[i]=this[i+1];
    }
    this.pop();
}
/*
================================================================================
SpecialString: 用于判断一个字符串是否含有或不含有某些字符 

返回值： 
	true或false 

参数： 
	string ： 需要判断的字符串 
	compare ： 比较的字符串(基准字符串) 
	BelongOrNot： true或false，“true”表示string的每一个字符都包含在compare中， 
	“false”表示string的每一个字符都不包含在compare中 
	
例子:
	if (!SpecialString('string','\'"@#$%&^*',false))
		 alert('Error: Your password can not contain \'"@#$%&^*'); 
================================================================================
*/
function specialString(string,compare,BelongOrNot) 
{ 
	if ((string==null) || (compare==null) || ((BelongOrNot!=null) && (BelongOrNot!=true) && (BelongOrNot!=false))) 
	{ 
		alert('function SpecialString(string,compare,BelongOrNot)参数错误'); 
		return false; 
	}
	if (BelongOrNot==null || BelongOrNot==true){ 
		for (var i=0;i<string.length;i++){ 
			if (compare.indexOf(string.charAt(i))==-1) 
				return false 
		} 
		return true; 
	} else{ 
		for (var i=0;i<string.length;i++){ 
			if (compare.indexOf(string.charAt(i))!=-1) 
				return false 
		} 
		return true; 
	} 
} 
/*
================================================================================
检验Email合法性
================================================================================
*/
function checkEmail(email) {
	if (/^w+([.-]?w+)*@w+([.-]?w+)*(.w{2,3})+$/.test(email)){
		return (true);
	}
	return (false);
}
/*
================================================================================
检查日期正确性
================================================================================
*/
function chkDateTime(str){
	var reg = /^(\d{1,4})-(\d{1,2})-(\d{1,2})$/; 
	var r = str.match(reg); 
	if(r==null)return false; 
	var d= new Date(r[1], --r[2],r[3]); 
	if(d.getFullYear()!=r[1])return false;
	if(d.getMonth()!=r[2])return false;
	if(d.getDate()!=r[3])return false;
	return true;
}