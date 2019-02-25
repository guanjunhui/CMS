package cn.cebest.service.system.WarrantyClaim;

import java.io.InputStream;
import java.util.List;
import java.util.Map;


/**
 *
 * @author wangweijie
 * @Date 2018年7月13日
 * @company 中企高呈
 */
public interface ExcelService {

	/**
	 * @param file
	 */
	List<String> read(InputStream inputStream) throws Exception;

	/**保存导入数据结果状态信息
	 * @param pMap
	 */
	void saveImportExcelMsg(Map<String, String> pMap) throws Exception ;

	/**过去导入数据结果状态信息
	 * @param pMap
	 */
	Object getImportExcelMsg() throws Exception ;

}
