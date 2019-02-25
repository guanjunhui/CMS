package cn.cebest.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
/**
 * 项目名称：
 * @author:中企高呈
 * 修改日期：2015/11/2
*/
public class Const {
	public static final String SESSION_SECURITY_CODE = "sessionSecCode";	//验证码
	public static final String SESSION_SECURITY_FRONT_CODE = "sessionFrontSecCode";//前台验证码
	public static String SESSION_SECURITY_NOTE_CODE =  "sessionFrontNoteCode";//前台 短信 验证码
	public static final String SESSION_USER = "sessionUser";				//session用的用户
	public static final String SESSION_MEMBER_USER = "sessionMemberUser";	//session用的用户
	public static final String SESSION_ROLE_RIGHTS = "sessionRoleRights";
	public static final String sSESSION_ROLE_RIGHTS = "sessionRoleRights";
	public static final String SESSION_menuList = "menuList";				//当前菜单
	public static final String SESSION_allmenuList = "allmenuList";			//全部菜单
	public static final String SESSION_CHANGEMENU_KEY = "changeMenu";			//全部菜单
	public static final String SESSION_PERSITELIST = "permsiteList";			//具有权限的站点

	public static final String SESSION_QX = "QX";
	public static final String SESSION_userpds = "userpds";			
	public static final String SESSION_USERROL = "USERROL";					//用户对象
	public static final String SESSION_USERNAME = "USERNAME";				//用户名
	public static final String TRUE = "T";
	public static final String FALSE = "F";
	public static final String ADMIN_PREFIX = "/manage/";					//登录地址
	public static final String MEMBER_PREFIX = "/member/";					//登录地址
	public static final String LOGIN = "/manage/login.do";					//登录地址
	public static final String NOAUTH = "/manage/noauth.do";				//未授权地址
	public static final String SYSNAME = "admin/config/SYSNAME.txt";		//系统名称路径
	public static final String PAGE	= "admin/config/PAGE.txt";				//分页条数配置路径
	public static final String EMAIL = "admin/config/EMAIL.txt";			//邮箱服务器配置路径
	public static final String SMS1 = "admin/config/SMS1.txt";				//短信账户配置路径1
	public static final String SMS2 = "admin/config/SMS2.txt";				//短信账户配置路径2
	public static final String FWATERM = "admin/config/FWATERM.txt";		//文字水印配置路径
	public static final String IWATERM = "admin/config/IWATERM.txt";		//图片水印配置路径
	public static final String LOGINEDIT = "admin/config/LOGIN.txt";		//登录页面配置
	public static final String FTLPATH = "admin/config/FTLPATH.txt";		//前端模版路径
	public static final String FILEPREFIX = "../";							//文件上传路径
	public static final String FILEPATHIMG = "/uploadFiles/uploadImgs/";		//图片上传路径
	public static final String FILEPATHFILE = "/uploadFiles/file/";			//文件上传路径
	public static final String FILEPATHFILEOA = "/uploadFiles/uploadFile/";	//文件上传路径(oa管理)
	public static final String FILEPATHTWODIMENSIONCODE = "/uploadFiles/twoDimensionCode/"; //二维码存放路径
	public static final String NO_INTERCEPTOR_PATH = "/((login)|(logout)|(code)|(app)|(static)|(main)|(websocket)|(fhadmin)|(web)|(fh_static_1)|(fh_static_2)|(fh_static_3)|(fh_static_4)|(ueditor)|(index)|(uploadImgs)).*";	//不对匹配该值的访问路径拦截（正则）
	public static final String NO_INTERCEPTOR_PATH_FB = ".*/((login)|(logout)|(code)|(app)|(index)).*";	//不对匹配该值的访问路径拦截（正则）

	public static ApplicationContext WEB_APP_CONTEXT = null; //该值会在web容器启动时由WebAppContextListener初始化
	
	
	public static final String NO_RUNTIME_PATH = ".*/((saveSite)|(sitelistPage)|(goAddSite)|(getDefinedTree)).do";	//不对匹配该值的访问路径拦截（正则）

	public static final String OUT_BEAN = "out_bean";//输出参数：对象数据
	public static final String OUT_LIST = "out_list";//输出参数：列表数据
	public static final String OUT_LIST2 = "out_list2";//输出参数：列表数据
	public static final String OUT_PAGE = "out_page";//输出参数：分页数据
	public static final String PARAM_TPL = "tpl";//参数：是否调用模板。
	public static final String PARAM_TPL_SUB = "tplSub";//参数：次级模板名称
	public static final String PARAM_CURRENTID = "currentId";//参数：当前栏目或者类别的ID。

	public static final String APP_ID = "20180730000190149";  				//百度翻译的APP_ID
	public static final String SECURITY_KEY = "vENoLnYsgl7Gl2nTJQf6";		//百度翻译的SECURITY_KEY
	
	public static final String VALID = "1";//有效
	public static final String INVALID = "0";//无效

	public static final String YES = "1";//是
	public static final String NO = "0";//否
	
	public static final String SPLIT_CHAR = ",";//字符串分割符
	public static final String SPLIT_COLON = ":";//冒号

	public static final String PARENT_FLAG = "0";
	
	public static final int INT_0 = 0;
	public static final int INT_1 = 1;
	public static final int INT_2 = 2;
	public static final int INT_3 = 3;
	public static final int INT_10 = 15;
	public static final int INT_30 = 30;

	public static final int INT_UNSIGINED_1 = -1;

	public static final String STRING_YES = "yes";//是
	public static final String STRING_NO = "no";//否

	public static final int PASSWORD_ENCRY_TIMES = 2;//密码加密次数

	//超管角色用户名
	public static final String SUPER_ADMIN_NAME;
    static{
    	SUPER_ADMIN_NAME=SystemConfig.getPropertiesString("super.admin.name");
    }


	public static final String VIDEO = 
			"((3gp)|(mp4)|(rmvb)|(mov)|(avi)|(m4v)|(wmv))";	//不对匹配该值的访问路径拦截（正则）

	/**
	 * APP Constants
	 */
	//系统用户注册接口_请求协议参数)
	public static final String[] SYSUSER_REGISTERED_PARAM_ARRAY = new String[]{"USERNAME","PASSWORD","NAME","EMAIL","rcode"};
	public static final String[] SYSUSER_REGISTERED_VALUE_ARRAY = new String[]{"用户名","密码","姓名","邮箱","验证码"};
	
	//app根据用户名获取会员信息接口_请求协议中的参数
	public static final String[] APP_GETAPPUSER_PARAM_ARRAY = new String[]{"USERNAME"};
	public static final String[] APP_GETAPPUSER_VALUE_ARRAY = new String[]{"用户名"};
	
	/**
	 * 申请状态：0==未发起,1==已申请,2==学校审核中,3==申请通过,4==退回
	 * (2:当下载学生申请信息时触发)
	 */
	public static final String APPLY_STATUS_0 = "0";
	public static final String APPLY_STATUS_1 = "1";
	public static final String APPLY_STATUS_2 = "2";
	public static final String APPLY_STATUS_3 = "3";
	public static final String APPLY_STATUS_4 = "4";
	
	/**
	 * 节点类型：0==栏目组,1==栏目,2==分类
	 */
	public static final String NODE_TYPE_0 = "0";
	public static final String NODE_TYPE_1 = "1";
	public static final String NODE_TYPE_2 = "2";
	
	/**
	 * 权限节点类型：1==系统菜单,2==动态栏目,3==栏目组,4==无效节点
	 */
	public static final String PER_NODE_TYPE_1 = "1";
	public static final String PER_NODE_TYPE_2 = "2";
	public static final String PER_NODE_TYPE_3 = "3";
	public static final String PER_NODE_TYPE_INVALID = "4";


	public static final String PER_NODETYPE = "nodeType";
	
	/**
	 * 模板类型：1==内容模板,2==资讯模板,3==产品模板,4==招聘模板,5==下载模板,6==首页模板
	 */
	public static final String TEMPLATE_TYPE_1 = "1";//内容模板
	public static final String TEMPLATE_TYPE_2 = "2";//资讯模板
	public static final String TEMPLATE_TYPE_3 = "3";//产品模板
	public static final String TEMPLATE_TYPE_4 = "4";//招聘模板
	public static final String TEMPLATE_TYPE_5 = "5";//下载模板
	public static final String TEMPLATE_TYPE_6 = "6";//首页模板
	
	/**
	 * 模板类型：1==列表,2==详情
	 */
	public static final String TEMPLATE_TYPE_LIST = "1";//列表
	public static final String TEMPLATE_TYPE_DETAIL = "2";//详情

	/**
	 * 栏目类型：1==内容栏目,2==资讯栏目,3==产品栏目,4==招聘栏目,5==下载栏目,6==首页栏目
	 */
	public static final String COLUM_TYPE_1 = "1";//内容栏目
	public static final String COLUM_TYPE_2 = "2";//资讯栏目
	public static final String COLUM_TYPE_3 = "3";//产品栏目
	public static final String COLUM_TYPE_4 = "4";//招聘栏目
	public static final String COLUM_TYPE_5 = "5";//下载栏目
	public static final String COLUM_TYPE_6 = "6";//首页栏目
	
	
	/**
	 * 菜单类型：1==系统,2==业务菜单
	 */
	public static final String MENU_TYPE_SYSTEM = "1";
	public static final String MENU_TYPE_BUSINESS = "2";
	

	

	//统计应用名
	public static final String APPLICATION_NAME;
	static{
		APPLICATION_NAME=SystemConfig.getPropertiesString("application.name");
	}
	/**
	 * 统计常量 pm==浏览量 uv==访客数 ip独立IP
	 */
	public static final String STATIS_PM= APPLICATION_NAME+"_pm_";

	public static final String STATIS_UV=APPLICATION_NAME +"_uv";

	public static final String STATIS_IP= APPLICATION_NAME+"_ip";
	public static final String STATIS_IP_COUNT= APPLICATION_NAME+"_ipcount";
	
	public enum TEMPLATETYPE{//模板类型
		TYPE_1("1","内容模板"),TYPE_2("2","资讯模板"),TYPE_3("3","产品模板"),
		TYPE_4("4","招聘模板"),TYPE_5("5","下载模板"),TYPE_6("6","首页模板");
		private String code;
		private String name;
		
		private TEMPLATETYPE(String code,String name){
			this.code=code;
			this.name=name;
		}

		public String getCode() {
			return code;
		}
		public String getName() {
			return name;
		}

	}
	
	/**
	 * 栏目类型：1==varchar -文本,2==textare -长文本,3==dateTime - 日期(格式-2017-11-01 HH:mm:ss),4==int,正数,5==double-金额
	 */
	public enum EXTEND_FIELD_TYPE{//扩展字段类型
		FIELD_TYPE_1("1","文本"),FIELD_TYPE_2("2","长文本"),FIELD_TYPE_3("3","日期"),
		FIELD_TYPE_4("4","整数"),FIELD_TYPE_5("5","小数");
		private String code;
		private String name;
		
		private EXTEND_FIELD_TYPE(String code,String name){
			this.code=code;
			this.name=name;
		}

		public String getCode() {
			return code;
		}
		public String getName() {
			return name;
		}

	}
	//栏目类型
   public enum COLUMTYPE{
		COLUM_TYPE_1("1","内容栏目"),COLUM_TYPE_2("2","资讯栏目"),COLUM_TYPE_3("3","产品栏目"),
		COLUM_TYPE_4("4","招聘栏目"),COLUM_TYPE_5("5","下载栏目"),COLUM_TYPE_6("6","首页栏目");
		
		private String code;
		private String name;
		
		private COLUMTYPE(String code,String name){
			this.code=code;
			this.name=name;
		}

		public String getCode() {
			return code;
		}
		public String getName() {
			return name;
		}

	}
   /**
  	 * 自定义表单类型：1==STRING -文本,2==textare -长文本,6==dateTime - 日期(格式-2017-11-01 HH:mm:ss),
  	 * 4==int,正数,5==double-金额
  	 */
  	public enum CUSTOMFORMTYPE{//自定义表单字段类型
  		VARCHAR("1","文本"),TEXTAREA("2","长文本"),BOLB("3","富文本"),
  		DECIMAL("4","数据类型"),SELECT("5","下拉框"),DATETIME("6","日期"),
  		IMAGE("7","图片"),FILE("8","附件");
  		private String code;
  		private String name;
  		
  		private CUSTOMFORMTYPE(String code,String name){
  			this.code=code;
  			this.name=name;
  		}

  		public String getCode() {
  			return code;
  		}
  		public String getName() {
  			return name;
  		}
  		
  	    /**
  	     * 获取指定type的name名称
  	     * @return
  	     */
  		public static String getMatchName(String code){
  			for(CUSTOMFORMTYPE e:CUSTOMFORMTYPE.values()){
  				if(e.code.equals(code)){
  					return e.name;
  				}
  			}
  			return StringUtils.EMPTY;
  		}
  	}
	public enum APPLYSTATUS{//申请状态
		STATUS_0("0","未发起"),STATUS_1("1","申请中"),STATUS_2("2","学校审核中"),
		STATUS_3("3","申请通过"),STATUS_4("4","退回");
		
		private String status;
		private String name;
		
		private APPLYSTATUS(String status,String name){
			this.status=status;
			this.name=name;
		}

		public String getStatus() {
			return status;
		}

		public String getName() {
			return name;
		}
		
	    /**
	     * 获取指定type的name名称
	     * @return
	     */
		public static String getMatchName(String status){
			for(APPLYSTATUS e:APPLYSTATUS.values()){
				if(e.status.equals(status)){
					return e.name;
				}
			}
			return "";
		}

		
	}
	
	/**
	 * 站内信模板类型
	 */
	public static final String MSG_TEMPLATE_REPLY_TOPIC = "1";//评论话题
	public static final String MSG_TEMPLATE_REPLY_REPLY = "2";//回复评论
	public static final String MSG_TEMPLATE_REPLY_ADOPT = "3";//评论被采纳
	public static final String MSG_TEMPLATE_CONTACT_PLAN = "4";//联系方案
	public static final String MSG_TEMPLATE_COMMIT_PLAN = "5";//提交方案
	public static final String MSG_TEMPLATE_COMMIT_DEMOND = "6";//提交需求
	
	
	/**
	 * 返回状态
	 */
	public static final int HTTP_OK = 200;
	public static final int HTTP_ERROR = -1;
	public static final int HTTP_ERROR_400 = 400;
	public static final int HTTP_ERROR_401 = 401;
	public static final int HTTP_ERROR_402 = 402;
	public static final int HTTP_ERROR_403 = 403;
	public static final int HTTP_ERROR_405 = 405;
	
	public enum LANGUAGE{//语言环境类型
		Chinese("中文","ZH"),English("英文","EN");
		
		private String name;
		private String value;
		
		private LANGUAGE(String name,String value){
			this.name=name;
			this.value=value;
		}
		public String getName() {
			return name;
		}
		public String getValue() {
			return value;
		}
		
	}


	
}
