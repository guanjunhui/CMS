package cn.cebest.entity;

import java.io.Serializable;

import cn.cebest.util.Const;
import cn.cebest.util.PageData;
import cn.cebest.util.Tools;

/**
 * 分页类
 * @author 中企高呈
 * 创建时间：2014年6月28日
 */
public class Page<T> implements Serializable {
	private String userId;  //当前用户id
	private String isClose;  //是否关闭
	private String time;  //是否关闭
	private String isSolve;  //是否解决
	private String text;  //是否解决
	private String templateId;  //内容模板所属id
	private String queryKey;  //查询条件
	private int showCount; 		//每页显示记录数
	private int totalPage;		//总页数
	private int totalResult;	//总记录数
	private int currentPage;	//当前页
	private int currentResult;	//当前记录起始索引
	private boolean entityOrField;//true:需要分页的地方，传入的参数就是Page实体；false:需要分页的地方，传入的参数所代表的实体拥有Page属性
	private String pageStr;//最终页面显示的底部翻页导航，详细见：getPageStr();
	private String ajaxPageStr;//最终页面显示的底部翻页导航，详细见：getAjaxPageStr();
	private PageData pd = new PageData();
	private T data;
	
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIsClose() {
		return isClose;
	}

	public void setIsClose(String isClose) {
		this.isClose = isClose;
	}

	public String getIsSolve() {
		return isSolve;
	}

	public void setIsSolve(String isSolve) {
		this.isSolve = isSolve;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	public Page(){
		try {
			this.showCount = Integer.parseInt(Tools.readTxtFile(Const.PAGE));
		} catch (Exception e) {
			this.showCount = 15;
		}
	}
	
	public Page(int currentPage,int showCount){
		this.currentPage=currentPage;
		this.showCount=showCount;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public int getTotalPage() {
		if(totalResult%showCount==0)
			totalPage = totalResult/showCount;
		else
			totalPage = totalResult/showCount+1;
		return totalPage;
	}
	
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
	public int getTotalResult() {
		return totalResult;
	}
	
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}
	
	public int getCurrentPage() {
		if(currentPage<=0)
			currentPage = 1;
		if(currentPage>getTotalPage())
			currentPage = getTotalPage();
		return currentPage;
	}
	
	public int getCurrentPageOrgi() {
		return currentPage;
	}

	
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	//拼接分页 页面及JS函数
	public String getPageStr() {
		StringBuffer sb = new StringBuffer();
		if(totalResult>0){
			sb.append("	<ul class=\"pagination pull-right no-margin\">\n");
			if(currentPage==1){
				sb.append("	<li class=\"totalClass\"><a>共<font color=red>"+totalResult+"</font>条</a></li>\n");
				sb.append("	<li class=\"pageNumClass\"><input type=\"number\" value=\"\" id=\"toGoPage\" style=\"width:50px;text-align:center;float:left\" placeholder=\"页码\"/></li>\n");
				sb.append("	<li class=\"goClass\" style=\"cursor:pointer;\"><a onclick=\"toTZ();\"  class=\"btn btn-mini btn-success\">跳转</a></li>\n");
				sb.append("	<li class=\"indexClass\"><a>首页</a></li>\n");
				sb.append("	<li class=\"prevClass\"><a><</a></li>\n");
			}else{
				sb.append("	<li class=\"totalClass\"><a>共<font color=red>"+totalResult+"</font>条</a></li>\n");
				sb.append("	<li class=\"pageNumClass\"><input type=\"number\" value=\"\" id=\"toGoPage\" style=\"width:50px;text-align:center;float:left\" placeholder=\"页码\"/></li>\n");
				sb.append("	<li class=\"goClass\" style=\"cursor:pointer;\"><a onclick=\"toTZ();\"  class=\"btn btn-mini btn-success\">跳转</a></li>\n");
				sb.append("	<li class=\"indexClass\" style=\"cursor:pointer;\"><a onclick=\"nextPage(1)\">首页</a></li>\n");
				sb.append("	<li class=\"prevClass\" style=\"cursor:pointer;\"><a onclick=\"nextPage("+(currentPage-1)+")\"><</a></li>\n");
			}
			int showTag = 5;//分页标签显示数量
			int startTag = 1;
			if(currentPage>showTag){
				startTag = currentPage-1;
			}
			int endTag = startTag+showTag-1;
			for(int i=startTag; i<=totalPage && i<=endTag; i++){
				if(currentPage==i)
					sb.append("<li class=\"active\"><a><font color='white'>"+i+"</font></a></li>\n");
				else
					sb.append("	<li class=\"noactive\" style=\"cursor:pointer;\"><a onclick=\"nextPage("+i+")\">"+i+"</a></li>\n");
			}
			if(currentPage==totalPage){
				sb.append("	<li class=\"nextClass\"><a>></a></li>\n");
				sb.append("	<li class=\"lastClass\"><a>尾页</a></li>\n");
			}else{
				sb.append("	<li class=\"nextClass\" style=\"cursor:pointer;\"><a onclick=\"nextPage("+(currentPage+1)+")\">></a></li>\n");
				sb.append("	<li class=\"lastClass\" style=\"cursor:pointer;\"><a onclick=\"nextPage("+totalPage+")\">尾页</a></li>\n");
			}
			sb.append("	<li class=\"totalPageClass\"><a>共"+totalPage+"页</a></li>\n");
			sb.append("	<li class=\"totalCountClass\"><select title='显示条数' style=\"width:55px;float:left;margin-top:1px;\" onchange=\"changeCount(this.value)\">\n");
			sb.append("	<option value='"+showCount+"'>"+showCount+"</option>\n");
			sb.append("	<option value='10'>10</option>\n");
			sb.append("	<option value='20'>20</option>\n");
			sb.append("	<option value='30'>30</option>\n");
			sb.append("	<option value='40'>40</option>\n");
			sb.append("	<option value='50'>50</option>\n");
			sb.append("	<option value='60'>60</option>\n");
			sb.append("	<option value='70'>70</option>\n");
			sb.append("	<option value='80'>80</option>\n");
			sb.append("	<option value='90'>90</option>\n");
			sb.append("	<option value='99'>99</option>\n");
			sb.append("	</select>\n");
			sb.append("	</li>\n");
			
			sb.append("</ul>\n");
			sb.append("<script type=\"text/javascript\">\n");
			
			//换页函数
			sb.append("function nextPage(page){");
			sb.append(" top.jzts();");
			sb.append("	if(true && document.forms[0]){\n");
			sb.append("		var url = document.forms[0].getAttribute(\"action\");\n");
			sb.append("		if(url.indexOf('?')>-1){url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
			sb.append("		else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
			sb.append("		url = url + page + \"&" +(entityOrField?"showCount":"page.showCount")+"="+showCount+"\";\n");
			sb.append("		document.forms[0].action = url;\n");
			sb.append("		document.forms[0].submit();\n");
			sb.append("	}else{\n");
			sb.append("		var url = document.location+'';\n");
			sb.append("		if(url.indexOf('?')>-1){\n");
			sb.append("			if(url.indexOf('currentPage')>-1){\n");
			sb.append("				var reg = /currentPage=\\d*/g;\n");
			sb.append("				url = url.replace(reg,'currentPage=');\n");
			sb.append("			}else{\n");
			sb.append("				url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";\n");
			sb.append("			}\n");
			sb.append("		}else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
			sb.append("		url = url + page + \"&" +(entityOrField?"showCount":"page.showCount")+"="+showCount+"\";\n");
			sb.append("		document.location = url;\n");
			sb.append("	}\n");
			sb.append("}\n");
			
			//调整每页显示条数
			sb.append("function changeCount(value){");
			sb.append(" top.jzts();");
			sb.append("	if(true && document.forms[0]){\n");
			sb.append("		var url = document.forms[0].getAttribute(\"action\");\n");
			sb.append("		if(url.indexOf('?')>-1){url += \"&"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
			sb.append("		else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
			sb.append("		url = url + \"1&" +(entityOrField?"showCount":"page.showCount")+"=\"+value;\n");
			sb.append("		document.forms[0].action = url;\n");
			sb.append("		document.forms[0].submit();\n");
			sb.append("	}else{\n");
			sb.append("		var url = document.location+'';\n");
			sb.append("		if(url.indexOf('?')>-1){\n");
			sb.append("			if(url.indexOf('currentPage')>-1){\n");
			sb.append("				var reg = /currentPage=\\d*/g;\n");
			sb.append("				url = url.replace(reg,'currentPage=');\n");
			sb.append("			}else{\n");
			sb.append("				url += \"1&"+(entityOrField?"currentPage":"page.currentPage")+"=\";\n");
			sb.append("			}\n");
			sb.append("		}else{url += \"?"+(entityOrField?"currentPage":"page.currentPage")+"=\";}\n");
			sb.append("		url = url + \"&" +(entityOrField?"showCount":"page.showCount")+"=\"+value;\n");
			sb.append("		document.location = url;\n");
			sb.append("	}\n");
			sb.append("}\n");
			
			//跳转函数 
			sb.append("function toTZ(){");
			sb.append("var toPaggeVlue = document.getElementById(\"toGoPage\").value;");
			sb.append("if(toPaggeVlue == ''){document.getElementById(\"toGoPage\").value=1;return;}");
			sb.append("if(isNaN(Number(toPaggeVlue))){document.getElementById(\"toGoPage\").value=1;return;}");
			sb.append("nextPage(toPaggeVlue);");
			sb.append("}\n");
			sb.append("</script>\n");
		}
		pageStr = sb.toString();
		return pageStr;
	}
	
	public void setPageStr(String pageStr) {
		this.pageStr = pageStr;
	}
	
	//拼接分页 页面及JS函数
	public String getAjaxPageStr() {
		StringBuffer sb = new StringBuffer();
		if(totalResult>0){
			sb.append("	<ul class=\"pagination pull-right no-margin\">\n");
			if(currentPage==1){
				sb.append("	<li class=\"totalClass\"><a>共<font color=red>"+totalResult+"</font>条</a></li>\n");
				sb.append("	<li class=\"pageNumClass\"><input type=\"number\" value=\"\" id=\"toGoPage\" style=\"width:50px;text-align:center;float:left\" placeholder=\"页码\"/></li>\n");
				sb.append("	<li class=\"goClass\" style=\"cursor:pointer;\"><a onclick=\"toTZ();\"  class=\"btn btn-mini btn-success\">跳转</a></li>\n");
				sb.append("	<li class=\"indexClass\"><a>首页</a></li>\n");
				sb.append("	<li class=\"prevClass\"><a><</a></li>\n");
			}else{
				sb.append("	<li class=\"totalClass\"><a>共<font color=red>"+totalResult+"</font>条</a></li>\n");
				sb.append("	<li class=\"pageNumClass\"><input type=\"number\" value=\"\" id=\"toGoPage\" style=\"width:50px;text-align:center;float:left\" placeholder=\"页码\"/></li>\n");
				sb.append("	<li class=\"goClass\" style=\"cursor:pointer;\"><a onclick=\"toTZ();\"  class=\"btn btn-mini btn-success\">跳转</a></li>\n");
				sb.append("	<li class=\"indexClass\" style=\"cursor:pointer;\"><a onclick=\"getData(1)\">首页</a></li>\n");
				sb.append("	<li class=\"prevClass\" style=\"cursor:pointer;\"><a onclick=\"getData("+(currentPage-1)+")\"><</a></li>\n");
			}
			int showTag = 5;//分页标签显示数量
			int startTag = 1;
			if(currentPage>showTag){
				startTag = currentPage-1;
			}
			int endTag = startTag+showTag-1;
			for(int i=startTag; i<=totalPage && i<=endTag; i++){
				if(currentPage==i)
					sb.append("<li class=\"active\"><a><font color='white'>"+i+"</font></a></li>\n");
				else
					sb.append("	<li class=\"noactive\" style=\"cursor:pointer;\"><a onclick=\"getData("+i+")\">"+i+"</a></li>\n");
			}
			if(currentPage==totalPage){
				sb.append("	<li class=\"nextClass\"><a>></a></li>\n");
				sb.append("	<li class=\"lastClass\"><a>尾页</a></li>\n");
			}else{
				sb.append("	<li class=\"nextClass\" style=\"cursor:pointer;\"><a onclick=\"getData("+(currentPage+1)+")\">></a></li>\n");
				sb.append("	<li class=\"lastClass\" style=\"cursor:pointer;\"><a onclick=\"getData("+totalPage+")\">尾页</a></li>\n");
			}
			sb.append("	<li class=\"totalPageClass\"><a>共"+totalPage+"页</a></li>\n");
			sb.append("	<li class=\"totalCountClass\"><select title='显示条数' style=\"width:55px;float:left;margin-top:1px;\" onchange=\"changeCount(this.value)\">\n");
			sb.append("	<option value='"+showCount+"'>"+showCount+"</option>\n");
			sb.append("	<option value='10'>10</option>\n");
			sb.append("	<option value='20'>20</option>\n");
			sb.append("	<option value='30'>30</option>\n");
			sb.append("	<option value='40'>40</option>\n");
			sb.append("	<option value='50'>50</option>\n");
			sb.append("	<option value='60'>60</option>\n");
			sb.append("	<option value='70'>70</option>\n");
			sb.append("	<option value='80'>80</option>\n");
			sb.append("	<option value='90'>90</option>\n");
			sb.append("	<option value='99'>99</option>\n");
			sb.append("	</select>\n");
			sb.append("	</li>\n");
			sb.append("</ul>\n");
		}
		ajaxPageStr = sb.toString();
		return ajaxPageStr;
	}
		
	public void setAjaxPageStr(String ajaxPageStr) {
		this.ajaxPageStr = ajaxPageStr;
	}
	
	public int getShowCount() {
		return showCount;
	}
	
	public void setShowCount(int showCount) {
		
		this.showCount = showCount;
	}
	
	public int getCurrentResult() {
		currentResult = (getCurrentPage()-1)*getShowCount();
		if(currentResult<0)
			currentResult = 0;
		return currentResult;
	}
	
	public void setCurrentResult(int currentResult) {
		this.currentResult = currentResult;
	}
	
	public boolean isEntityOrField() {
		return entityOrField;
	}
	
	public void setEntityOrField(boolean entityOrField) {
		this.entityOrField = entityOrField;
	}
	
	public PageData getPd() {
		return pd;
	}

	public void setPd(PageData pd) {
		this.pd = pd;
	}

	/**
	 * 获取前一页
	 */
	public int getPrevPage() {
		return currentPage-Const.INT_1;
	}

	/**
	 * 获取后一页
	 */
	public int getNextPage() {
		return currentPage+Const.INT_1;
	}

	/**
	 * 是否第一页
	 */
	public boolean isFirstPage() {
		return currentPage <= 1;
	}

	/**
	 * 是否最后一页
	 */
	public boolean isLastPage() {
		return currentPage >= getTotalPage();
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
