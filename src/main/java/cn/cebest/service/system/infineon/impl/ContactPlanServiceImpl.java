package cn.cebest.service.system.infineon.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.MsgTemplate;
import cn.cebest.entity.Page;
import cn.cebest.service.system.infineon.ContactPlanService;
import cn.cebest.service.system.infineon.IpcSiteMsgService;
import cn.cebest.util.Const;
import cn.cebest.util.PageData;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.UuidUtil;

/**
 *
 * @author wangweijie
 * @Date 2018年9月12日
 * @company 中企高呈
 */
@Service
public class ContactPlanServiceImpl implements ContactPlanService{
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Autowired
	private IpcSiteMsgService ipcSiteMsgService;
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> ListPage(Page page) throws Exception {
		return (List<PageData>) dao.findForList("contactPlanMapper.listPage", page);
	}
	

	@Override
	public List<PageData> ListDetailPage(Page page) throws Exception {
		page.setTemplateId(SystemConfig.getPropertiesString("web.solution_detail_template_id"));
		return (List<PageData>) dao.findForList("contactPlanMapper.listPageDetail", page);
	}

	@Override
	public Object detailById(String id) throws Exception {
		return dao.findForObject("contactPlanMapper.detailById", id);
	}

	@Override
	public void updateHandle(PageData pd) throws Exception {
 	}

	@Override
	public void deleteById(PageData pd) throws Exception {
		dao.delete("contactPlanMapper.deleteById", pd);
	}

	@Override
	public void deleteByIds(String[] ids) throws Exception {
		dao.delete("contactPlanMapper.deleteByIds", ids);
	}

	@Override
	public void savecontactPlan(PageData pd) throws Exception {
		this.dao.save("contactPlanMapper.save", pd);
		//发送站内信
		String template = new MsgTemplate().getTemplate(Const.MSG_TEMPLATE_CONTACT_PLAN);
		PageData pd1 = new PageData();
		pd1.put("type", Const.MSG_TEMPLATE_CONTACT_PLAN);
		template = template.replace("{title}", pd.getString("planName"));
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		template = template.replace("{time}", dateFormat.format(date));
		pd1.put("content", template);
		pd1.put("id", UuidUtil.get32UUID());
		pd1.put("userId", "000");
		pd1.put("touserId", pd.get("userId"));
		this.ipcSiteMsgService.saveMsg(pd1);
	}

	@Override
	public void collectionPlan(PageData pd) throws Exception {
		Object obj = this.dao.findForObject("contactPlanMapper.findBySourceIdAndSourcetypeAndUserId", pd);
		if(!obj.toString().equals("0")){
			throw new Exception("您已收藏");
		}
		this.dao.save("contactPlanMapper.saveCollectionPlan", pd);
		
	}

	@Override
	public void thumbPlan(PageData pd) throws Exception {
		Object obj = this.dao.findForObject("contactPlanMapper.findBySourceIdAndSourcetypeAndUserId_thumb", pd);
		if(!obj.toString().equals("0")){
			throw new Exception("您已点赞");
		}
		this.dao.save("contactPlanMapper.saveThumb", pd);
		
	}


	@Override
	public String isThumb(PageData pd) throws Exception {
		return this.dao.findForObject("contactPlanMapper.findBySourceIdAndSourcetypeAndUserId_thumb", pd).toString();
	}

	@Override
	public String isCollectionPlan(PageData pd) throws Exception {
		return  this.dao.findForObject("contactPlanMapper.findBySourceIdAndSourcetypeAndUserId", pd).toString();
	}


	@Override
	public void cancelThumbPlan(PageData pd) throws Exception {
		this.dao.delete("contactPlanMapper.cancelThumbPlan", pd);
		
	}


	@Override
	public void cancelCollection(PageData pd) throws Exception {
		this.dao.delete("contactPlanMapper.cancelCollectionPlan", pd);
		
	}


	@Override
	public String getThumbNum(PageData pd) throws Exception {
		return this.dao.findForObject("contactPlanMapper.getThumbNum", pd).toString();
	}


	@Override
	public List<Map<String, String>> findAll() throws Exception {
		return (List<Map<String, String>>) this.dao.findForList("contactPlanMapper.findAll", null);
	}


	@Override
	public void inserts(List<Map<String, String>> pList) throws Exception {
		this.dao.save("contactPlanMapper.inserts", pList);
	}

}
