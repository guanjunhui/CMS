package cn.cebest.service.system.recommend;

import java.util.List;

import cn.cebest.entity.system.Recommend;


/**	
 * 推荐设置
 * @author qichangxin
 * 修改日期：2015.11.6
 */
public interface RecommendService {
	/**
	 * 批量保存推荐的映射关系
	 * @param String contentId
	 */
	public void saveBatch(List<Recommend> list)throws Exception;
	
	/**
	 * 根据内容ID删除推荐的映射关系
	 * @param String contentId
	 */
	public void deleteByContentId(String contentId)throws Exception;
	/**
	 * 根据内容ID推荐的映射关系
	 * @param String contentId
	 */
	public List<Recommend> selectByContentId(String contentId)throws Exception;
}
