package cn.cebest.entity;

import java.util.HashMap;
import java.util.Map;

import cn.cebest.util.Const;

/**
 * 站内信模板类
 * @author wangweijie
 * @Date 2018年9月28日
 * @company 中企高呈
 */
public class MsgTemplate {
	private Map<String, String> tMap;
	
	public MsgTemplate(){
		Map<String, String> pMap = new HashMap<String, String>();
		/*pMap.put(Const.MSG_TEMPLATE_REPLY_TOPIC,  "提问被回答：【{nickname}】评论了你的话题【{title}】：{detail}");
		pMap.put(Const.MSG_TEMPLATE_REPLY_REPLY,  "回答被回复：【{nickname}】回复了你的评论【{title}】：{detail}");
		pMap.put(Const.MSG_TEMPLATE_REPLY_ADOPT,  "回答被选为最佳答案：【{nickname}】回复了你的评论【{title}】：{detail}");*/
		pMap.put(Const.MSG_TEMPLATE_REPLY_TOPIC,  "提问被回答：您的提问【{title}】有新的回答，请点击<ccc href='{url}' class='bbb'>【此处】查看。</ccc>");
		pMap.put(Const.MSG_TEMPLATE_REPLY_REPLY,  "回答被回复：您的回答【{title}】被回复了，请点击<ccc href='{url}' class='bbb'>【此处】查看。</ccc>");
		pMap.put(Const.MSG_TEMPLATE_REPLY_ADOPT,  "回答被选为最佳答案：您的回答【{title}】被选为最佳答案，请点击<ccc href='{url}' class='bbb'>【此处】查看。</ccc>");
		pMap.put(Const.MSG_TEMPLATE_CONTACT_PLAN, "联系方案: 您于{time}联系方案【{title}】的请求已收到，我们会尽快跟进并反馈。");
		pMap.put(Const.MSG_TEMPLATE_COMMIT_PLAN,  "提交方案: 您于{time}提交的方案【{title}】已收到，我们会尽快跟进并反馈。");
		pMap.put(Const.MSG_TEMPLATE_COMMIT_DEMOND,"提交需求: 您于{time}提交的需求【{title}】已收到，我们会尽快跟进并反馈。");
		this.tMap = pMap;
	};
	
	public String getTemplate(String key){
		return this.tMap.get(key);
	}
}
