//---------------------判断贷款金额是否合法
function isRangeNumSpan(id) {   
        var val = document.getElementById(id).value;  
        if (/^\d+$/.test(val))     
	    {     
	       if(val<0 || val>1000000){
		    	alert("贷款金额必须是大于等于0且小于等于1000000的数字!"); 
		    	document.getElementById(id).value = "";
		   }  
	    }else     
	    {     
		   alert("请输入数字");
		   document.getElementById(id).value = "";
	       return false;     
	    }     

    }   

//--------------判断账号是否合法
function checkPay(id){
	
	var pay = document.getElementById(id).value;
	var reg = /^\d{19}$/g;
	if(!reg.test(pay)){
		alert("格式错误，应该是19位数字组成！");
		document.getElementById(id).value = "";
	}else{
		alert("验证成功。");
	}
}


//--------------判断日期是合法性
function isDate(id) {
	var strDate = document.getElementById(id).value;
	var result1 = strDate
			.match(/((^((1[8-9]\d{2})|([2-9]\d{3}))(-)(10|12|0?[13578])(-)(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(11|0?[469])(-)(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))(-)(0?2)(-)(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)(-)(0?2)(-)(29)$)|(^([3579][26]00)(-)(0?2)(-)(29)$)|(^([1][89][0][48])(-)(0?2)(-)(29)$)|(^([2-9][0-9][0][48])(-)(0?2)(-)(29)$)|(^([1][89][2468][048])(-)(0?2)(-)(29)$)|(^([2-9][0-9][2468][048])(-)(0?2)(-)(29)$)|(^([1][89][13579][26])(-)(0?2)(-)(29)$)|(^([2-9][0-9][13579][26])(-)(0?2)(-)(29)$))/);
	
	var a=/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})/ 
		if (!a.test(strDate)){ 
		alert("请输入正确的日期格式！,例如：2013-06-06。");
		document.getElementById(id).value = "";
	} else {
		alert("验证成功。");
	}
}

//--------------------------------判断电话号码/手机号码 
function checkTel(id) {
	var tel = document.getElementById(id);
	if (tel.value.length != 11) {
		alert("必须填写真实的联系方式");
		document.getElementById(id).value = "";
	} else {
		for ( var i = 0; i < 11; i++) {
			if (tel.value.charAt(i) < "0" || tel.value.charAt(i) > "9") {
				alert("联系电话只能是0--9的数字组成");
				document.getElementById(id).value = "";
				return;
			}
		}
	}
}

//----------------------验证身份证号码的合法性
function isIdCardNo(id) {
	var idcard = document.getElementById(id).value;
	var area = {
		11 : "北京",
		12 : "天津",
		13 : "河北",
		14 : "山西",
		15 : "内蒙古",
		21 : "辽宁",
		22 : "吉林",
		23 : "黑龙江",
		31 : "上海",
		32 : "江苏",
		33 : "浙江",
		34 : "安徽",
		35 : "福建",
		36 : "江西",
		37 : "山东",
		41 : "河南",
		42 : "湖北",
		43 : "湖南",
		44 : "广东",
		45 : "广西",
		46 : "海南",
		50 : "重庆",
		51 : "四川",
		52 : "贵州",
		53 : "云南",
		54 : "西藏",
		61 : "陕西",
		62 : "甘肃",
		63 : "青海",
		64 : "宁夏",
		65 : "新疆",
		71 : "台湾",
		81 : "香港",
		82 : "澳门",
		91 : "国外"
	}
	var Y, JYM;
	var S, M;
	var idcard_array = new Array();
	idcard_array = idcard.split("");

	//地区检验 
	if (area[parseInt(idcard.substr(0, 2))] == null) {
		alert("身份证地区非法!");
		document.getElementById(id).value = "";
	}
	//身份号码位数及格式检验 
	switch (idcard.length) {
	case 15:
		if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0
				|| ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard
						.substr(6, 2)) + 1900) % 4 == 0)) {
			ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/; //测试出生日期的合法性 
		} else {
			ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/; //测试出生日期的合法性 
		}
		if (ereg.test(idcard)) {
			alert("验证通过!");
		} else {
			alert("身份证号码出生日期超出范围或含有非法字符!");
			document.getElementById(id).value = "";
		}
		break;
	case 18:
		//18位身份号码检测 
		if (parseInt(idcard.substr(6, 4)) % 4 == 0
				|| (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard
						.substr(6, 4)) % 4 == 0)) {
			ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/; //闰年出生日期的合法性正则表达式 
		} else {
			ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/; //平年出生日期的合法性正则表达式 
		}
		if (ereg.test(idcard)) {//测试出生日期的合法性 
			//计算校验位 
			S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
					+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11]))
					* 9
					+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12]))
					* 10
					+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13]))
					* 5
					+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14]))
					* 8
					+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15]))
					* 4
					+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16]))
					* 2 + parseInt(idcard_array[7]) * 1
					+ parseInt(idcard_array[8]) * 6 + parseInt(idcard_array[9])
					* 3;
			Y = S % 11;
			M = "F";
			JYM = "10X98765432";
			M = JYM.substr(Y, 1); //判断校验位 
			if (M == idcard_array[17]) {
				alert("验证通过!");//检测ID的校验位 
			} else {
				alert("身份证号码校验错误!");
				document.getElementById(id).value = "";
			}
		} else {
			alert("身份证号码出生日期超出范围或含有非法字符!");
			document.getElementById(id).value = "";
		}
		break;
	default: {
		alert("身份证号码位数不对!");
		document.getElementById(id).value = "";
		break;
	}
	}
}

