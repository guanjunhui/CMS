package cn.cebest.controller.system.infineon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wangweijie
 * @Date 2018年11月5日
 * @company 中企高呈
 */
public class ModeUtil {
	
	private Map<String, String> modeMap;
	private Map<String, String> nameMap;
	private Map<String, String> exportNameMap;
	private Map<String, List<String>> tableMap;
	private Map<String, List<String>> attrMap;
	
	public ModeUtil(){
		//模板路径
		Map<String, String> modeMap = new HashMap<String, String>();
		modeMap.put("1", "/WEB-INF/jsp/system/infineonipc/download/contact_plan_mode.xlsx");
		modeMap.put("2", "/WEB-INF/jsp/system/infineonipc/download/commit_plan_mode.xlsx");
		modeMap.put("3", "/WEB-INF/jsp/system/infineonipc/download/commit_demand_mode.xlsx");
		modeMap.put("4", "/WEB-INF/jsp/system/infineonipc/download/userInfo.xlsx");//用于信息
		this.modeMap = modeMap;
		
		//下载模板名称
		Map<String, String> nameMap = new HashMap<String, String>();
		nameMap.put("1", "联系方案导入模板.xlsx");
		nameMap.put("2", "我有方案导入模板.xlsx");
		nameMap.put("3", "我有需求导入模板.xlsx");
		this.nameMap = nameMap;
		//导出模板名称
		Map<String, String> exportNameMap = new HashMap<String, String>();
		exportNameMap.put("1", "联系方案");
		exportNameMap.put("2", "我有方案");
		exportNameMap.put("3", "我有需求");
		exportNameMap.put("4", "用户信息");
		this.exportNameMap = exportNameMap;
		
		//导出表模板
		Map<String, List<String>> tableMap = new HashMap<>();
		tableMap.put("1", Arrays.asList("姓名", "电话","邮箱","公司","方案名称","联系理由","提交时间"));//联系方案
		tableMap.put("2", Arrays.asList("姓名", "电话","邮箱","公司","方案信息","提交时间"));//我有方案
		tableMap.put("3", Arrays.asList("姓名", "电话","邮箱","公司","需求信息","提交时间"));//我有需求
		tableMap.put("4", Arrays.asList("ID", "姓","名","昵称","邮箱","电话","密码","国家","公司","渠道","渠道说明","头像url","城市","地址","座机","部门","职位","公司领域","上次登录时间","上次登录源","来源","是否订阅","创建时间"));//用户信息
		this.tableMap = tableMap;
		//表属性模板
		Map<String, List<String>> attrMap = new HashMap<>();
		attrMap.put("1", Arrays.asList("username", "phone","email","company","planName","contactReason","createTime"));//联系方案
		attrMap.put("2", Arrays.asList("username", "phone","email","company","planMsg","createTime"));//我有方案
		attrMap.put("3", Arrays.asList("username", "phone","email","company","demand","createTime"));//我有需求
		attrMap.put("4", Arrays.asList("id", "surname","name","nickname","email","phone","password","country","company","channel","channelMsg","photourl","city","address","seatPhone","dept","job","companyField","loginTime","loginSource","source","isSub","createTime"));//用户信息
		this.attrMap = attrMap;
	}
	
	public String getMode(String key){
		return this.modeMap.get(key);
	}
	public String getName(String key){
		return this.nameMap.get(key);
	}
	public String getExportName(String key){
		return this.exportNameMap.get(key)+"-"+System.currentTimeMillis()+".xlsx";
	}
	public List<String> getTable(String key){
		return this.tableMap.get(key);
	}
	public List<String> getAttr(String key){
		return this.attrMap.get(key);
	}
}
