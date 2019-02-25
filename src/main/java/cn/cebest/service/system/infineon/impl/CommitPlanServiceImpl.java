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
import cn.cebest.service.system.infineon.CommitPlanService;
import cn.cebest.service.system.infineon.IpcSiteMsgService;
import cn.cebest.util.Const;
import cn.cebest.util.PageData;
import cn.cebest.util.UuidUtil;

/**
 *
 * @author wangweijie
 * @Date 2018年9月12日
 * @company 中企高呈
 */
@Service
public class CommitPlanServiceImpl implements CommitPlanService{
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Autowired
	private IpcSiteMsgService ipcSiteMsgService;
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> ListPage(Page page) throws Exception {
		return (List<PageData>) dao.findForList("commitPlanMapper.listPage", page);
	}

	@Override
	public Object detailById(String id) throws Exception {
		return dao.findForObject("commitPlanMapper.detailById", id);
	}

	@Override
	public void updateHandle(PageData pd) throws Exception {
		this.dao.update("commitPlanMapper.updateHandle", pd);
	}

	@Override
	public void deleteById(PageData pd) throws Exception {
		dao.delete("commitPlanMapper.deleteById", pd);
	}

	@Override
	public void deleteByIds(String[] ids) throws Exception {
		dao.delete("commitPlanMapper.deleteByIds", ids);
	}

	@Override
	public void saveCommitPlan(PageData pd) throws Exception {
		this.dao.save("commitPlanMapper.save", pd);
		//发送站内信
		String template = new MsgTemplate().getTemplate(Const.MSG_TEMPLATE_COMMIT_PLAN);
		PageData pd1 = new PageData();
		pd1.put("type", Const.MSG_TEMPLATE_COMMIT_PLAN);
		template = template.replace("{title}", pd.getString("planMsg"));
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
	public List<Map<String, String>> findAll() throws Exception {
		return (List<Map<String, String>>) this.dao.findForList("commitPlanMapper.findAll", null);
	}

}
