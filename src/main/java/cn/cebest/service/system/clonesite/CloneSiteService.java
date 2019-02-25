package cn.cebest.service.system.clonesite;

import java.util.List;

import cn.cebest.entity.system.content.Content;
import cn.cebest.entity.system.content.ContentType;
import cn.cebest.entity.system.content.Content_Type_Colum;
import cn.cebest.entity.system.content.Contenttype_Column;

public interface CloneSiteService {
	List<ContentType> findContentTypeBySiteId(String siteID) throws Exception;
	List<Contenttype_Column> findContentTypeColumnByTypeIds(String[] typeIDS) throws Exception;
	List<Content> findContentBySiteId(String siteID) throws Exception;
	List<Content_Type_Colum> findCTCByContentIds(String[] contentIDS) throws Exception;
	void insertBatchCType(List<ContentType> list) throws Exception;
	void insertBatchTypeColumn(List<Contenttype_Column> list) throws Exception;
	void insertBatchContent(List<Content> list) throws Exception;
	void insertBatchCTC(List<Content_Type_Colum> list) throws Exception;
	void deleteBeforCopyC(String siteID)throws Exception;
	void deleteBeforCopyCC(String[] contentIDS)throws Exception;
	void deleteBeforCopyCT(String siteID)throws Exception;
	void deleteBeforCopyCTC(String[] contentTypeIDS)throws Exception;
}
