package cn.cebest.service.system.infineon.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.service.system.infineon.IpcSolutionService;
import cn.cebest.util.Logger;

/**
 *
 * @author wangweijie
 * @Date 2018年10月30日
 * @company 中企高呈
 */
@Service
public class IpcSolutionServiceImpl implements IpcSolutionService{
	private static Logger logger = Logger.getLogger(IpcSolutionServiceImpl.class);
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Override
	public Map<String, Object> getSolutionByCondation(Page page) throws Exception {
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> collections = new ArrayList<>();
		collections = (List<Map<String, Object>>) dao.findForList("IpcSolutionMapper.getSolutionByCondationlistPage", page);
		data.put("data", collections);
		data.put("page", page);
		return data;
	}

}
