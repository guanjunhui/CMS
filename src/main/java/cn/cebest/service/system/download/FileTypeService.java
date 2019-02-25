package cn.cebest.service.system.download;

import java.util.List;
import java.util.Map;

import cn.cebest.entity.Page;
import cn.cebest.entity.bo.ContentInfoBo;
import cn.cebest.entity.system.download.FileType;
import cn.cebest.util.PageData;

/**
 * 下载类型接口
 * @author wzd
 *
 */
public interface FileTypeService {
    /**
     * 返回有层级结构的数据
     * @param pd
     * @return
     * @throws Exception 
     */
	List<FileType> getTreeData(PageData pd) throws Exception;
	/**
	 * 保存文件类型
	 * @param product
	 * @param pd
	 * @throws Exception 
	 */
	void save(FileType download, PageData pd) throws Exception;
	/**
	 * 展示的页面发送ajax返回层级结构的文件类型数据
	 * @return
	 * @throws Exception 
	 */
	List<FileType> getListTree(PageData pd)throws Exception;
	/**
	 * 删除
	 * @param id
	 * @throws Exception 
	 */
	void deleteFileType(String[] id) throws Exception;
	/**
	 * 修改
	 * @param download
	 * @param pd
	 * @throws Exception
	 */
	void update(FileType download, PageData pd)throws Exception;
	/**
	 * 根据id查询
	 * @param map
	 * @param id
	 * @return 
	 * @throws Exception
	 */
	Map<String, Object> findById(Map<String, Object> map, String id)throws Exception;
	/**
	 * 展示的页面发送ajax返回层级结构的文件类型数据
	 * @return
	 * @throws Exception 
	 */
	List<FileType> search(PageData pd)throws Exception;
	/**
	 * 根据栏目ids批量删除
	 */
	public void deleteByColumnIds(String[] ids) throws Exception;
	/**
	 * 根据栏目id查询文件类型
	 */
	public List<FileType> findFileTypeByColumnIds(String id) throws Exception;
	/**
	 * 修改排序
	 * @param map
	 * @throws Exception
	 */
	void updateSort(Map<String, Object> map)throws Exception;
	/**
	 * 根据条件返回所有值得list集合,key值与数据库字段名相同,区分大小写
	 */
	public List<FileType> findFileTypeAllByAll(Map<String,Object> map) throws Exception;

	/**
	 * 根据栏目id查询文件类型
	 */
	public FileType findTypeInfoById(String id) throws Exception;
	/**
	 * 根据分类id批量查询内容
	 * @param typeIdList
	 * @return
	 */
	List<ContentInfoBo> findAllGroomData(List<String> typeIdList) throws Exception;
	/**
	 * 查询上首页的下载类型
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	List<FileType> findFileTypeOption(PageData pd)throws Exception;
	/**
	 * 重复性验证
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer findcount(Map<String, Object> map)throws Exception;
	List<FileType> getTreeByColumId(PageData pd)throws Exception;
	List<FileType> getTypeByColumId(PageData pd)throws Exception;
	void updateStatusByIds(PageData pd) throws Exception;
	void updataStatus(Map<String, Object> map) throws Exception;
	
	List<FileType> selectFileByColumdlistPage(Page page) throws Exception;

}
