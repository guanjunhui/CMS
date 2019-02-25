package cn.cebest.service.system.WarrantyClaim;

/**
 *
 * @author wangweijie
 * @Date 2018年7月17日
 * @company 中企高呈
 */
public interface CountryService {

	/**获取所有国家
	 * @return
	 */
	Object getAllCountrys()throws Exception ;
	
	/**
	 * 根据国家英文名称获取国家编号 
	 * @param nameEn
	 * @return
	 * @throws Exception
	 */
	String getCountryCode(String nameEn)throws Exception ;
}
