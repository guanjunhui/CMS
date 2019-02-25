package cn.cebest.service.system.site;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.entity.bo.SiteMain;
import cn.cebest.entity.web.WebSite;
import cn.cebest.util.PageData;

public interface SiteService {

	void changeStatus(PageData pd) throws Exception;

	void editSite(PageData pd) throws Exception;

	PageData findSiteById(PageData pd) throws Exception;

	void saveSite(PageData pd) throws Exception;

	void delAllSite(String[] arraySITE_IDS) throws Exception;

	void delSite(PageData pd) throws Exception;

	List<PageData> sitelistPage(Page page) throws Exception;
	
	/**获取所有站点
	 * @throws Exception
	 */
	List<WebSite> findAllSiteByStatus(String status) throws Exception;
	
	/**获取所有菜单主要信息
	 * @throws Exception
	 */
	List<SiteMain> findAllSiteMainInfo() throws Exception;
	
	/**根据ID获取站点
	 * @throws Exception
	 */
	WebSite findSitePoById(String siteId) throws Exception;

	void updateSiteIndexStatus(PageData pd) throws Exception;


}
