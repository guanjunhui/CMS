package cn.cebest.service.system.download;

import java.util.List;
import java.util.Map;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.download.DownloadFiles;
import cn.cebest.entity.system.download.FileResources;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

/**
 * 文件接口
 * @author wzd
 *
 */
public interface FileResourceService {
	/**
	 * 保存文件
	 * @param download
	 * @param pd
	 * @throws Exception 
	 */
	void save(FileResources download, PageData pd) throws Exception;
	/**
	 * 删除
	 * @param id
	 */
	void deleteFile(String[] id)throws Exception;
	/**
	 * 根据类型的ids查询相同类型文件
	 * @param id
	 * @throws Exception
	 */
	List<FileResources> findByTypeIds(Map<String,Object> map)throws Exception;
	/**
	 * 根据ids查询相同类型文件
	 * @param id
	 * @throws Exception
	 */
	List<FileResources> findTypeById(String[] id)throws Exception;
	/**
	 * 根据id查询文件
	 * @param id
	 * @return 
	 * @throws Exception
	 */
	Map<String, Object> findById(Map<String,Object> map,String id) throws Exception;
	/**
	 * 展示页面
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<FileResources> getList(Page page) throws Exception;
	/**
	 * 保存文件
	 * @param download
	 * @param pd
	 * @throws Exception 
	 */
	void update(FileResources download, PageData pd)throws Exception;
	/**
	 * 推荐
	 * @param file
	 * @throws Exception
	 */
	void updataRecommendAndTop(FileResources file)throws Exception;
	/**
	 * 根据栏目id查询产品
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<FileResources> findFileResourcesByColumnId(String id)throws Exception;
	/**
	 * 根据条件返回所有值得list集合,key值与数据库字段名相同,区分大小写
	 */
	public List<PageData> findFileAllByAll(Map<String,Object> map) throws Exception;
	
	public List<PageData> findTopList() throws Exception;
	/**
	 * 根据类型id查询文件以及可下载资源
	 * @param currentId
	 * @return
	 * @throws Exception 
	 */
	List<FileResources> findFileResourceAndDownloadFileByTypeId(Page page) throws Exception;
	/**
	 * 查找指定的政策法规记录
	 * @param columId
	 * @return
	 * @throws Exception 
	 */
	
	List<FileResources> findTopPolicyforIndex(String columId) throws Exception;
	/**
	 * 根据id查询模板路径
	 * @param contentId
	 * @return
	 * @throws Exception
	 */
	PageData findTemplatePachById(String contentId)throws Exception;
	/**
	 * 查询可下载文件
	 * @param id
	 * @return
	 * @throws Exception
	 */
	DownloadFiles findDownloadFileById(Integer id) throws Exception;
	/**
	 * 查询可下载文件
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<FileResources> findFileFileResourceAndDownloadFileByColumnId(Page page)throws Exception;
	/**
	 * 查询可下载文件
	 * @param id
	 * @return
	 * @throws Exception
	 */
	FileResources findFileResourceAndDownloadFileById(String contentid)throws Exception;
	void updateDownlosd_count(String fileid) throws Exception;
	List<PageData> getType(Page page) throws Exception;
	/**
	 * 根据栏目ids删除文件
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public void deleteByColumnIds(String[] ids) throws Exception;
	void updateStatusByIds(PageData pd) throws Exception;
	void updataRecommendAndTopAndHot(FileResources file) throws Exception;
	void updateSort(PageData pd) throws Exception;
	List<FileResources> findMessageTypeList(Page page) throws Exception;
	List<FileResources> findReleventFileByid(String id) throws Exception;

	
	FileResources findTypeIdByName(String typeName) throws Exception;
	
	void saveTypeRelation(PageData pd) throws Exception;
	void saveImportData(FileResources fileResourse) throws Exception;

	FileResources findFileResourcesByTypeOrColumnid(PageData pd) throws Exception;
	List<FileResources> findlistPageFile(Page page) throws Exception;
	JsonResult getDowList(Page page);

}
