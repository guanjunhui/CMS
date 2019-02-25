package cn.cebest.service.system.addressquery.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.addressquery.AddressQuery;
import cn.cebest.service.system.addressquery.AddressQueryManager;
import cn.cebest.util.PageData;

@Service("addressQueryService")
public class AddressQueryService implements AddressQueryManager {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<AddressQuery> find(Page page, String province, String city, String area, String brand) throws Exception {
		PageData pd = page.getPd();
		pd.put("province", province);
		pd.put("city", city);
		pd.put("area", area);
		pd.put("brand", brand);
		return (List<AddressQuery>) dao.findForList("AddressQueryMapper.selectAddressQueryList", page);
	}

	@Override
	public void save(AddressQuery address) throws Exception {
		dao.save("AddressQueryMapper.insertAddressQuery", address);
	}

	@Override
	public void update(AddressQuery address) throws Exception {
		dao.update("AddressQueryMapper.updateAddressQuery", address);
		
	}

	@Override
	public List<AddressQuery> queryListPage(Page page) throws Exception {
		return (List<AddressQuery>) dao.findForList("AddressQueryMapper.selectAddresslistPage", page);
	}

	@Override
	public AddressQuery findById(String id) throws Exception {
		return (AddressQuery) dao.findForObject("AddressQueryMapper.selectAddressById", id);
	}

	@Override
	public void delete(String[] ids) throws Exception {
		dao.delete("AddressQueryMapper.deleteAddressByIds", ids);
	}

	@Override
	public List<AddressQuery> queryList(PageData pd) throws Exception {
		
		return (List<AddressQuery>) dao.findForList("AddressQueryMapper.selectAddresslist", pd);
	}

}
