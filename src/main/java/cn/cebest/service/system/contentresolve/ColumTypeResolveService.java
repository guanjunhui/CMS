package cn.cebest.service.system.contentresolve;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.entity.bo.TypeInfoBo;



/**
 * 分类转换
 * */
public interface ColumTypeResolveService {

	/**
	 *根据栏目获取其所有分类 
	 */
	List<TypeInfoBo> findTypeByColumID(String columId,String columType) throws Exception;

	Page<List<TypeInfoBo>> findTypelistPageByColumID(Page<List<TypeInfoBo>> page, String columType) throws Exception;
}
