package cn.cebest.service.system.content.contentType;

import java.util.List;
import java.util.Map;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.content.ContentType;
import cn.cebest.util.PageData;

public interface ContentTypeService {

	List<PageData> contentTypelistPage(Page page) throws Exception;
	
	void delContentType(PageData pd) throws Exception;

	void delAllContentType(String[] arrayCOMMENT_IDS) throws Exception;

	void saveContentType(PageData pd) throws Exception;

	PageData findContentTypeById(PageData pd) throws Exception;

	void editContentType(PageData pd) throws Exception;

	void auditContentType(PageData pd) throws Exception;

	List<ContentType> findContentTypeByColumnIds(String id) throws Exception;

	void updateTypeStatusByIds(PageData pd) throws Exception;

	void delete(String[] ids) throws Exception;

	List<ContentType> getTreeData(Map map) throws Exception;

	List<ContentType> getUpdateTree(Map map) throws Exception;

	List<ContentType> getTreeByColumId(Map map) throws Exception;

	List<ContentType> getTypeByColumId(Map map) throws Exception;

	List<ContentType> getlistTreeData(Map map) throws Exception;

	void save(ContentType content) throws Exception;

	List<ContentType> findContentTypeToList(Map map) throws Exception;

	Map<String, Object> findContentTypeToEdit(String id) throws Exception;

	void update(ContentType content) throws Exception;

	void updataStatus(Map<String, Object> map) throws Exception;

	void updateSort(Map<String, Object> map) throws Exception;

	Integer findcount(Map<String, Object> map) throws Exception;

	Integer findContentycount(Map<String, Object> map) throws Exception;

	Integer findBrandcount(Map<String, Object> map) throws Exception;

	List<ContentType> findContentTypeList(PageData pd) throws Exception;

	List<ContentType> findContentTypeListByColumnIdOrPid(PageData pd)throws Exception;
}
