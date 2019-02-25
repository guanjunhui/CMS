package cn.cebest.service.system.WarrantyClaim.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.service.system.WarrantyClaim.WarrantyClaimService;
import cn.cebest.util.PageData;

@Service("warrantyClaimService")
public class WarrantyClaimServiceImpl implements WarrantyClaimService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> warrantyClaimlistPage(Page page) throws Exception {
		return (List<PageData>) dao.findForList("WarrantyClaimMapper.warrantyClaimlistPage", page);
	}

	@Override
	public void delWarrantyClaim(String id) throws Exception {
		dao.delete("WarrantyClaimMapper.delteWarrantyClaim", id);
	}


	@Override
	public void changeStatus(PageData pd) throws Exception {
		dao.update("WarrantyClaimMapper.changeStatus", pd);
	}

	@Override
	public void saveWarrantyClaim(PageData pd) throws Exception {
		dao.save("WarrantyClaimMapper.saveWarrantyClaim", pd);
	}
	
	@Override
	public void delAllWarrantyClaim(String[] ids) throws Exception {
		dao.delete("WarrantyClaimMapper.delAllWarrantyClaim", ids);
		
	}

	
	//修改推荐状态
		@Override
		public void updateRecommend(PageData pd) throws Exception {
			dao.update("WarrantyClaimMapper.updateRecommendStatus",pd);
		}

	@Override
	public Object detailById(String id) throws Exception {
		return dao.findForObject("WarrantyClaimMapper.detailById", id);
	}

}
