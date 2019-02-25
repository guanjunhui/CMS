package cn.cebest.service.system.recommend.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.system.Recommend;
import cn.cebest.service.system.recommend.RecommendService;


/**	角色
 * @author qichangxin
 * 修改日期：2015.11.6
 */
@Service
public class RecommendServiceImpl implements RecommendService{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Override
	public void saveBatch(List<Recommend> list) throws Exception {
		dao.save("RecommendMapper.saveBatch", list);
	}

	@Override
	public void deleteByContentId(String contentId) throws Exception {
		dao.delete("RecommendMapper.deleteByContentId", contentId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Recommend> selectByContentId(String contentId) throws Exception {
		return (List<Recommend>) dao.findForList("RecommendMapper.selectByContentId", contentId);
	}
	

}
