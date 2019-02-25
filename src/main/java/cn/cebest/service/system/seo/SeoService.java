package cn.cebest.service.system.seo;

import java.util.List;
import java.util.Map;

import cn.cebest.entity.system.Seo;
import cn.cebest.util.PageData;

public interface SeoService {
	/**
	 * 添加
	 * @param pd
	 * @throws Exception
	 */
	void insetSeo(PageData pd)throws Exception ;
	/**
	 * 根据id或者MASTER_ID删除
	 * @param pd
	 * @throws Exception
	 */
	void deleteSeo(PageData pd)throws Exception ;
	/**
	 * 根据MASTER_ID更新
	 * @param pd
	 * @throws Exception
	 */
	void updateSeo(PageData pd)throws Exception ;
	/**
	 * 根据id或者MASTER_ID查询
	 * @param pd
	 * @throws Exception
	 */
	PageData querySeoForObject(PageData pd)throws Exception ;
	/**
	 * 根据id或者MASTER_ID查询
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> querySeoForList(PageData pd)throws Exception ;
	void saveSeoContent(Map map) throws Exception;
	Map querySeoForContent(Map map) throws Exception;
	Integer updateSeoContent(Map map) throws Exception;
	Seo selectSeoForObject(String masterId) throws Exception;
}
