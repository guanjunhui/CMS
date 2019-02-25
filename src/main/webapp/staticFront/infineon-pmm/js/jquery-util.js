//将表单域数据转换json对象
$.fn.jsonObject=function(){
    var jsonObj = {};
    var array = this.serializeArray();
    $.each(array, function() {
      if (jsonObj[this.name]) { // 如果已存在了,那么转换为数组
        if (!jsonObj[this.name].push) {
          jsonObj[this.name] = [jsonObj[this.name]]; // 转为数组
        }
        jsonObj[this.name].push(this.value || '');
      } else {
        jsonObj[this.name] = this.value || '';
      }
    });
    return jsonObj;
}

//将表单域数据转换json字符串
$.fn.jsonString=function(){
    var jsonObj = {};
    var array = this.serializeArray();
    $.each(array, function() {
      if (jsonObj[this.name]) { // 如果已存在了,那么转换为数组
        if (!jsonObj[this.name].push) {
          jsonObj[this.name] = [jsonObj[this.name]]; // 转为数组
        }
        jsonObj[this.name].push(this.value || '');
      } else {
        jsonObj[this.name] = (this.value) || '';
      }
    });
    return JSON.stringify(jsonObj);
}

function removeArryByValue(arr, val) {
	for(var i=0; i<arr.length; i++){
		if(arr[i] == val) {
			arr.splice(i, 1);
			break;
		}
	}
}

// panduan('删除分类1','确认删除该分类吗？','此操作将会删除该分类下全部内容');
function mesageConfirm(a,b,c,_callback){
	$(".layer_bg").remove();
	html='<div class="layer_bg layer_bg04"><div class="layer_con cf xunwen"><h3><span>'+a+'</span><p class="close">x</p></h3><dl><dt><span class="ico bg-tishi03"></span></dt><dd><p>'+b+'</p><p>'+c+'</p></dd></dl><div class="all_btn cf"><input type="button" class="submit_btn" onclick="'+_callback+'"  value="确定" /><a href="javascript:;" class="confirm_re_btn">取消</a></div></div></div>'
	$('body').append(html);
}

function hideConfirm(){
    $('.layer_list li label').removeClass('radio_btn');
 	$('.layer_bg').find('input').removeAttr("checked");
 	$('.layer_bg').find('i').removeClass('active');
 	$('.layer_bg').hide();
}
/* 
 * formatMoney(s,type) 
 * 功能：金额按千位逗号分割
 * 参数：s，字符串，需要格式化的金额数值.
 * 参数：type,判断格式化后的金额小数位是几位.
 * 返回：返回格式化后的数值字符串.
 */
function formatMoney(s, n) {
	// 判断小数参数是否符合逻辑
	n != 0 && (n = n > 0 && n <= 20 ? n: 2);
	// 对金额进行小数点四舍五入
    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
    // 对金额整数进行数组化倒转，好进行加字符','
	var l = s.split(".")[0].split("").reverse(),
	// 截取金额小数
    r = s.split(".")[1];
    t = "";
    for (i = 0; i < l.length; i++) {
		// 对倒转后的金额数组进行加','重组
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? ",": "");
    }
	// 对重组后的金额再次还原，加入小数点
	n == 0 ? r = '' : r = "." + r;
    return t.split("").reverse().join("") + r;
}

//----------------------------------------------------------------------
//<summary>
//限制只能输入数字
//</summary>
//----------------------------------------------------------------------
$.fn.onlyNum = function () {
 $(this).keypress(function (event) {
     var eventObj = event || e;
     var keyCode = eventObj.keyCode || eventObj.which;
     if ((keyCode >= 48 && keyCode <= 57))
         return true;
     else
         return false;
 }).focus(function () {
 //禁用输入法
     this.style.imeMode = 'disabled';
 }).bind("paste", function () {
 //获取剪切板的内容
     var clipboard = window.clipboardData.getData("Text");
     if (/^\d+$/.test(clipboard))
         return true;
     else
         return false;
 });
};
//----------------------------------------------------------------------
//<summary>
//限制只能输入字母
//</summary>
//----------------------------------------------------------------------
$.fn.onlyAlpha = function () {
 $(this).keypress(function (event) {
     var eventObj = event || e;
     var keyCode = eventObj.keyCode || eventObj.which;
     if ((keyCode >= 65 && keyCode <= 90) || (keyCode >= 97 && keyCode <= 122))
         return true;
     else
         return false;
 }).focus(function () {
     this.style.imeMode = 'disabled';
 }).bind("paste", function () {
     var clipboard = window.clipboardData.getData("Text");
     if (/^[a-zA-Z]+$/.test(clipboard))
         return true;
     else
         return false;
 });
};

//----------------------------------------------------------------------
//<summary>
//限制只能输入数字和字母
//</summary>
//----------------------------------------------------------------------
$.fn.onlyNumAlpha = function () {
 $(this).keypress(function (event) {
     var eventObj = event || e;
     var keyCode = eventObj.keyCode || eventObj.which;
     if ((keyCode >= 48 && keyCode <= 57) || (keyCode >= 65 && keyCode <= 90) || (keyCode >= 97 && keyCode <= 122))
         return true;
     else
         return false;
 }).focus(function () {
     this.style.imeMode = 'disabled';
 }).bind("paste", function () {
     var clipboard = window.clipboardData.getData("Text");
     if (/^(\d|[a-zA-Z])+$/.test(clipboard))
         return true;
     else
         return false;
 });
};

//获取url参数
function GetRequest(param) {
  var url = location.search; //获取url中"?"符后的字串
   var theRequest = new Object();
   if (url.indexOf("?") != -1) {
      var str = url.substr(1);
      strs = str.split("&");
      for(var i = 0; i < strs.length; i ++) {
         theRequest[strs[i].split("=")[0]]=(strs[i].split("=")[1]);
      }
   }
   return theRequest[param];
}
/**
 * @author xieyanan
 * @see 对特殊字符进行校验，不允许文本框输入内容为特殊字符
 */
function ValidateValue(textbox) {
  var IllegalString = "\`~@#;,.！《》!#$%^&*()_+{}|\\:\"<>?-=/,\@#￥……&*（）&;—|{}【】‘；：”“'。，、？'";
  var textboxvalue = textbox.value;
  var index = textboxvalue.length - 1;

  var s = textbox.value.charAt(index);

  if (IllegalString.indexOf(s) >= 0) {
    s = textboxvalue.substring(0, index);
    textbox.value = s;
  }

  var rs = "";
  var currentValue = textbox.value;
  for (var i = 0; i < currentValue.length; i++) {
    var cv = currentValue.substr(i, 1);
    if (IllegalString.indexOf(cv) == -1) {
      rs += cv;
    }
  }
  textbox.value = rs;
}

/**
 * @author xieyanan
 * @see 消除文本框两端空格
 */
function clearDataBlank(obj) {
  obj.value = obj.value.replace(/(^\s*)|(\s*$)/g, "");
  if (obj.value == "") {
    obj.value == "";
  }
}

/**
 * 初始化字符串功能
 */
function initString() {
  // 判断是否为空字符串
  String.isEmpty = function(str) { // 这是一个静态方法
    if (str == null) return true;
    return /^\s*$/.test(str);
  };
  // ----以下是实例方法
  // 判断是否以指定字符串开始
  String.prototype.startWith = function(str) {
    var reg = new RegExp("^" + str);
    return reg.test(this);
  };
  // 判断是否以指定字符串结束
  String.prototype.endWith = function(str) {
    var reg = new RegExp(str + "$");
    return reg.test(this);
  };
  // 截取字符串前后空格
  String.prototype.trim = function() {
    return this.replace(/^\s+|\s+$/g, "");
  };
  // 判断是否整数
  String.prototype.isNumber = function() {
    if (String.isEmpty(this)) return false;
    return /^\d+$/.test(this);
  };
  // 判断是否汉字
  String.prototype.isChinese = function() {
    if (String.isEmpty(this)) return false;
    return /^[\u4E00-\u9FBF]+$/.test(this);
  };
  // 获取字符串的字节数
  String.prototype.getByteNum = function() {
    if (String.isEmpty(this)) return 0;
    return this.replace(/[^\x00-\xff]/g, "**").length;
  };
}
initString();
// 比较日期大小
function dateCompare(startdate, enddate) {
  var arr = startdate.split("-");
  var starttime = new Date(arr[0], arr[1], arr[2]);
  var starttimes = starttime.getTime();
  var arrs = enddate.split("-");
  var lktime = new Date(arrs[0], arrs[1], arrs[2]);
  var lktimes = lktime.getTime();
  if (starttimes >= lktimes) {
    return false;
  } else
    return true;
}

/**
 * 初始化日期处理功能
 */
function initDate() {
  /**
   * 初始化Date对象,为其增加pattern方法,使它具有转换为指定格式字符串的功能 用法:(new
   * Date()).pattern("yyyy-MM-dd hh:mm:ss") ==> 2013-10-24 11:50:20
   */
  Date.prototype.pattern = function(fmt) {
    var o = {
      "M+": this.getMonth() + 1, // 月份
      "d+": this.getDate(), // 日
      "h+": this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, // 小时
      "H+": this.getHours(), // 小时
      "m+": this.getMinutes(), // 分
      "s+": this.getSeconds(), // 秒
      "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
      "S": this.getMilliseconds()
    // 毫秒
    };
    var week = {
      "0": "/u65e5",
      "1": "/u4e00",
      "2": "/u4e8c",
      "3": "/u4e09",
      "4": "/u56db",
      "5": "/u4e94",
      "6": "/u516d"
    };
    if (/(y+)/.test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    if (/(E+)/.test(fmt)) {
      fmt = fmt.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "/u661f/u671f" : "/u5468") : "") + week[this.getDay() + ""]);
    }
    for ( var k in o) {
      if (new RegExp("(" + k + ")").test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
      }
    }
    return fmt;
  };
  /**
   * 获取指定月份的最后一天
   * 
   * @param year
   * @param month
   * @returns
   */
  Date.getLastDayInMonth = function(year, month) {
    month = parseInt(month, 10) + 1;
    var temp = new Date(year + "/" + month + "/0");
    return temp.getDate();
  };
}
initDate();

// 提交json对象数据
$.postJson = function(url, data, callback) {
  $.ajax({
    url: url,
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify(data || {}),
    dataType: 'json',
    success: callback || $.noop
  });
};


$.fn.setForm = function(jsonValue) {
  var obj = this;
  $.each(jsonValue, function(name, ival) {
    var $oinput = obj.find("input[name='" + name + "']");
    if ($oinput.attr("type") == "radio" || $oinput.attr("type") == "checkbox") {
      $oinput.each(function() {
        if (Object.prototype.toString.apply(ival) == '[object Array]') {// 是复选框，并且是数组
          for (var i = 0; i < ival.length; i++) {
            if ($(this).val() == ival[i]) $(this).attr("checked", "checked");
          }
        } else {
          if ($(this).val() == ival) $(this).attr("checked", "checked");
        }
      });
    } else if ($oinput.attr("type") == "textarea") {// 多行文本框
      obj.find("[name=" + name + "]").html(ival);
    } else {
      obj.find("[name=" + name + "]").val(ival);
    }
  });
};
