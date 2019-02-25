package cn.cebest.service.system.saveJson.saveJsonImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.service.system.saveJson.SaveJson;
import cn.cebest.util.PageData;
@Service("saveJsonImpl")
public class SaveJsonImpl implements SaveJson{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Override
	public void save(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		dao.save("saveJson.save", pd);
	}
	
	

}
