package cn.cebest.service.system.contentresolve;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.entity.bo.ContentInfoBo;
import cn.cebest.entity.web.Banner;

/**
 * 
 * */
public interface ColumContentResolveService {

	/**
	 *根据栏目ID查询所属内容
	 *@param ID 栏目ID 
	 */
	Page<List<ContentInfoBo>> findContentlistPageByColumID(Page<List<ContentInfoBo>> page,String columType) throws Exception;
	/**
	 *根据栏目ID查询所属banner
	 *@param ID 栏目ID 
	 */
	Page<List<Banner>> findBannerlistPageByColumID(Page<List<Banner>> page, String columType) throws Exception;

}
