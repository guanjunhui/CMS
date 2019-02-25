package cn.cebest.service.system.addressquery;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.addressquery.AddressQuery;
import cn.cebest.util.PageData;

/**
 * 门店查询服务接口
 * 
 * @author gaopeng@300.cn
 * @version 2018年7月5日 下午7:24:14
 */
public interface AddressQueryManager {
	/**
	 * 根据省市区、分类查询门店地址
	 * 
	 * @param province
	 * @param city
	 * @param area
	 * @param myClass
	 */
	public List<AddressQuery> find(Page page, String province, String city, String area, String brand) throws Exception;
	
    void save(AddressQuery address) throws Exception;
	
	void update(AddressQuery address) throws Exception;
	
	List<AddressQuery> queryListPage(Page page) throws Exception;
	
	AddressQuery findById(String id) throws Exception;
	
	void delete(String[] ids) throws Exception;

	public List<AddressQuery> queryList(PageData pd)throws Exception;
}
