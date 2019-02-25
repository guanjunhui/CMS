package cn.cebest.service.system.infineon.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.service.system.infineon.IpcSiteMsgService;
import cn.cebest.util.PageData;
import oracle.net.aso.p;

/**
 *
 * @author wangweijie
 * @Date 2018年9月27日
 * @company 中企高呈
 */
@Service
public class IpcSiteMsgServiceImpl implements IpcSiteMsgService{
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Override
	public void saveMsg(PageData pd) throws Exception{
		this.dao.save("IpcSiteMsgMapper.saveMsg",pd);
	}

	@Override
	public Map<String, Object> getAllListByUserId(Page page) throws Exception {
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> collections = (List<Map<String, Object>>) dao.findForList("IpcSiteMsgMapper.getAllListByUserIdlistPage", page);
		data.put("data", collections);
		data.put("page", page);
		return data;
	}

	@Override
	public int getNoReadCount(String userId)throws Exception {
		return (int)dao.findForObject("IpcSiteMsgMapper.getNoReadCount", userId);
	}

	@Override
	public String getDetailById(Map<String, String> pMap) throws Exception {
		if("0".equals(pMap.get("state"))){
			//未读设置为已读
			this.dao.update("IpcSiteMsgMapper.setAlreadyRead", pMap);
		}
		//根据id获取站内信详情
		return this.dao.findForObject("IpcSiteMsgMapper.getDetailById", pMap).toString();
	}

	@Override
	public void deletes(String[] ids) throws Exception{
		this.dao.delete("IpcSiteMsgMapper.deletes", ids);
	}

	@Override
	public void patchSetAllReadyRead(String[] ids) throws Exception {
		this.dao.delete("IpcSiteMsgMapper.patchSetAllReadyRead", ids);
	}

}
