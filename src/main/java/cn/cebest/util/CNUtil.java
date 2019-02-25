package cn.cebest.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class CNUtil {

	
	
	public CNUtil() {
	}

	
	/**
	 * 判断字符串的字符编码
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str) {        
	       String encode = "GB2312";        
	      try {        
	          if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是GB2312  
	               String s = encode;        
	              return s;      //是的话，返回“GB2312“，以下代码同理  
	           }        
	       } catch (Exception exception) {        
	       }        
	       encode = "ISO-8859-1";        
	      try {        
	    	  if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是ISO-8859-1  
	               String s1 = encode;        
	              return s1;        
	           }        
	       } catch (Exception exception1) {        
	       }        
	       encode = "UTF-8";        
	      try {        
	          if (str.equals(new String(str.getBytes(encode), encode))) {   //判断是不是UTF-8  
	               String s2 = encode;        
	              return s2;        
	           }        
	       } catch (Exception exception2) {        
	       }        
	       encode = "GBK";        
	      try {        
	          if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是GBK  
	               String s3 = encode;        
	              return s3;        
	           }        
	       } catch (Exception exception3) {        
	       }        
	      return "";        //如果都不是，说明输入的内容不属于常见的编码格式。
	}
	
	/**
	 * 有中文的混合字符串得到中文，没有则原样返回
	 * 如果字符串本身是有中文的混合字符串，则不做任何处理原样返回
	 * @param str
	 * @param stringCord
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String stringToChinese(String str) throws UnsupportedEncodingException{
		if(null != str && !str.equals("")){
			//str是否为中文或者中英文数字混合
			//判断是否为中文
			boolean chineseName = isChineseCharacter(str);
			String string = null;
			//如果不是中文
			if(!chineseName){
				//先进行转码，看是否能转换成中文
				string = new String(str.getBytes("ISO-8859-1"),"UTF-8");
			}else{
				return str;
			}
			
			String cnString = filterChinese(string);
			return cnString;
		}
		return null;
	}
	
	
	/**
	 * 判断字符串中是否包含汉字
	 * @param str
	 * 待校验字符串
	 * @return 是否为中文
	 * @warn 不能校验是否为中文标点符号 
	 */
	public static boolean isChineseCharacter(String name) {
		//Pattern pattern = Pattern.compile("^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]){2,5}$");
		//Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]+$");
		Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher matcher = pattern.matcher(name);
		if(matcher.find()){
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * 过滤后只留下中文
	 * @param str 待过滤中文的字符串
	 * @return 过滤后只留下中文的字符串
	 */
	public static String filterChinese(String str) {
		 // 用于返回结果
		 String result = str;
		 //判断是否为汉字
		 boolean flag = isChineseCharacter(str);
		 if (flag) {// 包含中文
		  // 用于拼接过滤中文后的字符
		  StringBuffer sb = new StringBuffer();
		  // 用于校验是否为中文
		  boolean flag2 = false;
		  // 用于临时存储单字符
		  char chinese = 0;
		  // 5.去除掉文件名中的中文
		  // 将字符串转换成char[]
		  char[] charArray = str.toCharArray();
		  // 过滤到中文及中文字符
		  for (int i = 0; i < charArray.length; i++) {
		   chinese = charArray[i];
		   flag2 = isChinese(chinese);
		   /*if (!flag2) {// 不是中日韩文字及标点符号
		    sb.append(chinese);
		   }*/
		   //判断是中文
		   if (flag2) {
			   sb.append(chinese);
		   }
		  }
		  result = sb.toString();
		 }
		 return result;
		}
	
	/**
	 * 校验一个字符是否是汉字
	 * 
	 * @param c
	 *  被校验的字符
	 * @return true代表是汉字
	 */
	private static  boolean isChinese(char c) {
		 try {
			 return String.valueOf(c).getBytes("UTF-8").length > 1;
		 } catch (UnsupportedEncodingException e) {
			 e.printStackTrace();
			 return false;
		 }
	}
}
