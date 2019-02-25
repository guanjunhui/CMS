package cn.cebest.controller.front.cms;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.cebest.util.Const;
import cn.cebest.util.FrontUtils;

@Controller
@RequestMapping("/search")
public class SearchResultController {
	
	@RequestMapping("tosearchResult")
	public String tosearchResult(String keyword,ModelMap model,HttpServletRequest request){
		if(StringUtils.isNotBlank(keyword)){
			try {
				//keyword= URLDecoder.decode(keyword, "UTF-8");
				keyword=SearchResultController.delHTMLTag(keyword);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			keyword=StringUtils.EMPTY;
		}
		model.addAttribute("keyword", keyword);
		model.addAttribute("isSerach", Const.YES);
		FrontUtils.frontPageData(request, model);
		return "search";
	}
	
	 public static String delHTMLTag(String htmlStr){ 
	        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
	        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
	        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式 
	         
	        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
	        Matcher m_script=p_script.matcher(htmlStr); 
	        htmlStr=m_script.replaceAll(""); //过滤script标签 
	         
	        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
	        Matcher m_style=p_style.matcher(htmlStr); 
	        htmlStr=m_style.replaceAll(""); //过滤style标签 
	         
	        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
	        Matcher m_html=p_html.matcher(htmlStr); 
	        htmlStr=m_html.replaceAll(""); //过滤html标签 

	        return htmlStr.trim(); //返回文本字符串 
	    } 
	
}
