package cn.cebest.service.system.WarrantyClaim;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.util.PageData;

public interface WarrantyClaimService {

	void changeStatus(PageData pd) throws Exception;

	void delWarrantyClaim(String id) throws Exception;

	List<PageData> warrantyClaimlistPage(Page page) throws Exception;
	
	void saveWarrantyClaim(PageData pd) throws Exception;
	
	void delAllWarrantyClaim(String[] ids) throws Exception;
	
	//修改推荐
	void updateRecommend(PageData pd) throws Exception;

	/**
	 * @param id
	 * @return
	 */
	Object detailById(String id) throws Exception;
	
}
