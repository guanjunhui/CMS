package cn.cebest.service.system.WarrantyClaim;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import cn.cebest.entity.Page;
import cn.cebest.entity.web.ProductRegistration;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年7月12日
 * @company 中企高呈
 */
public interface ProductRegistrationService {

	/**
	 * @param pd
	 * @param file
	 */
	void saveProductRegistration(ProductRegistration pd, MultipartFile file) throws Exception;

	/**
	 * @param page
	 * @return
	 */
	List<PageData> ListPage(Page page) throws Exception;

	/**校验验证码
	 * @param serialCode
	 * @return
	 */
	Integer checkSerialCode(String serialCode) throws Exception ;

	/**修改处理状态
	 * @param pd
	 */
	void updataRecommend(PageData pd)  throws Exception;

	/**
	 * @param id
	 * @return
	 */
	Object detailById(String id)throws Exception;

	/**根据邮箱和注册码查询注册产品
	 * @param pMap
	 * @return
	 */
	int selectByEmailAndRegistionCode(Map<String, String> pMap)throws Exception;
	
}
