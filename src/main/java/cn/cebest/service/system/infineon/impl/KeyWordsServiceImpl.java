package cn.cebest.service.system.infineon.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.infineon.KeyWords;
import cn.cebest.service.system.infineon.KeyWordsService;
import cn.cebest.util.Logger;
import cn.cebest.util.PageData;

@Service("KeyWordsService")
public class KeyWordsServiceImpl implements KeyWordsService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	private static Logger logger = Logger.getLogger(KeyWordsServiceImpl.class);
	
	@Override
	public Map<String, Object> getkeyWordsList(Page page) {
		Map<String, Object> data = new HashMap<>();
		try {
			List<KeyWords> list = (List<KeyWords>) dao.findForList("KeyWordsMapper.selectKeyWordslistPage", page);
			data.put("data", list);
			data.put("page", page);
		} catch (Exception e) {
			logger.error("获取关键词列表异常", e);
		}
		return data;
	}

	@Override
	public Map<String, Object> getKeyWords(PageData pd) {
		Map<String, Object> data = new HashMap<>();
		try {
			KeyWords key = (KeyWords) dao.findForObject("KeyWordsMapper.selectKeyWords", pd);
			data.put("data", key);
		} catch (Exception e) {
			logger.error("获取关键词详情异常", e);
		}
		return data;
	}

	@Override
	public void addKeyWords(PageData pd) {
		try {
			dao.save("KeyWordsMapper.insert", pd);
		} catch (Exception e) {
			logger.error("添加关键词详情异常", e);
		}
		
	}

	@Override
	public void updateKeyWords(PageData pd) {
		try {
			dao.update("KeyWordsMapper.update", pd);
		} catch (Exception e) {
			logger.error("修改关键词详情异常", e);
		}
	}

	@Override
	public void deleteKeyWords(PageData pd) {
		try {
			dao.delete("KeyWordsMapper.delete", pd);
		} catch (Exception e) {
			logger.error("删除关键词详情异常", e);
		}
	}

}
