package cn.cebest.service.system.solr;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.solr.SearchResult;
import cn.cebest.util.CeResultUtil;

public interface SolrService {

	/**
	 * solr索引库导入相关数据
	 * @param siteId 
	 * @return
	 */
	CeResultUtil importAll();
	
	/**
	 * 从索引库中查询数据
	 * @param keyword
	 * @param page
	 * @return
	 * @throws Exception 
	 */
	SearchResult findSolrList(String queryString,Page page) throws Exception;
	
	
	/**
	 * 清空solr索引库
	 */
	void deleteAll();

	/**
	 * 多参数搜索
	 * @param keyword
	 * @param ids
	 * @param page
	 * @return
	 * @throws Exception 
	 */
	SearchResult findSolrList(String keyword, String columnId, String[] ids, Page page) throws Exception;

}
