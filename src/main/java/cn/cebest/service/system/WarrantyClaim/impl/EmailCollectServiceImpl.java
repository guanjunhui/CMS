package cn.cebest.service.system.WarrantyClaim.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.service.system.WarrantyClaim.EmailCollectService;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年7月24日
 * @company 中企高呈
 */
@Service
public class EmailCollectServiceImpl implements EmailCollectService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Override
	public Integer save(PageData pd) throws Exception {
		//判断邮箱是否存在
		Integer count = (Integer)this.dao.findForObject("EmailCollectMapper.queryByEmail", pd.get("email"));
		if(count == 0){
			this.dao.save("EmailCollectMapper.save", pd);
		}
		return count;
	}
	@Override
	public List<PageData> ListPage(Page page)throws Exception {
		return (List<PageData>) dao.findForList("EmailCollectMapper.listPage", page);
	}
	@Override
	public void delete(PageData pd) throws Exception {
		dao.delete("EmailCollectMapper.delete", pd);
	}
	@Override
	public void deletes(String[] ids) throws Exception {
		dao.delete("EmailCollectMapper.deletes", ids);
	}

}
