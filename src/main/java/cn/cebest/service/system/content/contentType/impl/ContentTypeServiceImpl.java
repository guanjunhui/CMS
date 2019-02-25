package cn.cebest.service.system.content.contentType.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.xmlbeans.SystemProperties;
import org.springframework.stereotype.Service;

import com.baidu.translate.demo.TransApi;
import com.google.gson.Gson;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.content.ContentType;
import cn.cebest.service.system.content.content.ContentService;
import cn.cebest.service.system.content.contentType.ContentTypeService;
import cn.cebest.service.system.seo.SeoService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.DelAllFile;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.Tools;
import cn.cebest.util.UuidUtil;

@Service("contentTypeService")
public class ContentTypeServiceImpl implements ContentTypeService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Resource(name = "contentService")
	private ContentService service;
	@Resource(name = "seoService")
	private SeoService seoService;

	@Override
	public List<PageData> contentTypelistPage(Page page) throws Exception {
		return (List<PageData>) dao.findForList("ContentTypeMapper.contentTypelistPage", page);
	}

	@Override
	public void delContentType(PageData pd) throws Exception {
		dao.update("ContentTypeMapper.updateContentType", pd);
	}

	@Override
	public void delAllContentType(String[] arrayCOMMENT_IDS) throws Exception {
		dao.update("ContentTypeMapper.updateAllContentType", arrayCOMMENT_IDS);
	}

	@Override
	public void saveContentType(PageData pd) throws Exception {
		dao.save("ContentTypeMapper.saveContentType", pd);
	}

	@Override
	public PageData findContentTypeById(PageData pd) throws Exception {
		return (PageData) dao.findForObject("ContentTypeMapper.findContentTypeById", pd);
	}

	@Override
	public void editContentType(PageData pd) throws Exception {
		dao.update("ContentTypeMapper.editContentType", pd);
	}

	@Override
	public void auditContentType(PageData pd) throws Exception {
		dao.update("ContentTypeMapper.auditContentType", pd);
	}

	@Override
	public List<ContentType> findContentTypeByColumnIds(String id) throws Exception {
		List<ContentType> listAll = (List<ContentType>) dao.findForList("ContentTypeMapper.selectContentTypeByColumnIdTop", id);
		List<ContentType> listTop = new ArrayList<>();
		List<String> ids = new ArrayList<>();
		for (ContentType contentType : listAll) {
			ids.add(contentType.getId());
		}
		if (CollectionUtils.isEmpty(listAll)) {
			return null;
		}
		for (int i = 0; i < listAll.size(); i++) {
			if (!ids.contains(listAll.get(i).getpId())) {
				this.appendChildContentType(listAll.get(i), listAll);
				listTop.add(listAll.get(i));
			}
		}
		return listTop;
	}

	private void appendChildContentType(ContentType root, List<ContentType> allList) {
		for (int i = 0; i < allList.size(); i++) {
			ContentType pt = allList.get(i);
			if (pt != null && pt.getpId().equals(root.getId())) {
				if (CollectionUtils.isEmpty(root.getChildList())) {
					root.setChildList(new ArrayList<ContentType>());
				}
				root.getChildList().add(pt);
				this.appendChildContentType(pt, allList);
			}
		}
	}

	@Override
	public void updateTypeStatusByIds(PageData pd) throws Exception {
		dao.update("ContentTypeMapper.updateTypeStatusByIds", pd);
	}

	private void appendChildIds(List<String> newIdlist, String str, List<Map<String, String>> listAll) {
		for (int i = 0; i < listAll.size(); i++) {
			if (str.equals(listAll.get(i).get("PID"))) {
				newIdlist.add(listAll.get(i).get("ID"));
				listAll.remove(i);
				i--;
			}
		}
	}

	/**
	 * 删除方法
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void delete(String[] ids) throws Exception {
		List<String> newIdlist = new ArrayList(Arrays.asList(ids));
		Map map = new HashMap();
		//PageData data = new PageData();
		List<Map<String, String>> listAll = (List<Map<String, String>>) dao.findForList("ContentTypeMapper.selectAllIdPiD", map);
		for (int i = 0; i < newIdlist.size(); i++) {
			appendChildIds(newIdlist, newIdlist.get(i), listAll);
		}
		// 删除关联关系
		dao.delete("ContentTypeMapper.deleteContenttype_column", newIdlist.toArray(new String[newIdlist.size()]));
		//删除内容分类地址栏的url信息
		dao.delete("ColumconfigMapper.delUrlNmaeAll", newIdlist.toArray(new String[newIdlist.size()]));
		//删除内容地址栏的url信息
		dao.delete("ColumconfigMapper.delContentUrlNmaeAll", newIdlist.toArray(new String[newIdlist.size()]));
		
		List<Map> list = (List<Map>) dao.findForList("ContentTypeMapper.selectImagePach", newIdlist.toArray(new String[newIdlist.size()]));// 查询图片路径
		List<String> listID = new ArrayList<>();
		for (Map pd : list) {
			if (pd != null) {
				String imageId = (String) pd.get("IMAGE_ID");
				listID.add(imageId);
				String pach = (String) pd.get("IMGURL");
				if (pach != null && Tools.notEmpty(pach.trim())) {
					DelAllFile.deleteFile(PathUtil.getClasspath() + Const.FILEPATHIMG + pach); // 删除图片
				}
			}

		}

		if (listID.size() > 0) {
			dao.delete("ContentMapper.deleteContentImage", listID.toArray());// 删除图片信息
		}
		List<String> listid = (List<String>) dao.findForList("ContentTypeMapper.selectContentIds", newIdlist.toArray(new String[newIdlist.size()]));// 根据产品类型id查询产品id

		if (listid.size() > 0) {
			service.delAllContent(listid.toArray(new String[listid.size()]));
		}
		dao.delete("ContentTypeMapper.deleteTXT", newIdlist.toArray(new String[newIdlist.size()]));// 删除富文本
		dao.delete("ContentTypeMapper.deleteByIds", newIdlist.toArray(new String[newIdlist.size()]));
		PageData pd = new PageData();
		pd.put("MASTER_ID", ids);
		seoService.deleteSeo(pd);

	}

	private void appendChild(ContentType root, List<ContentType> allList) {
		for (int i = 0; i < allList.size(); i++) {
			ContentType pt = allList.get(i);
			if (pt != null && pt.getpId().equals(root.getId())) {
				if (CollectionUtils.isEmpty(root.getChildList())) {
					root.setChildList(new ArrayList<ContentType>());
				}
				root.getChildList().add(pt);
				this.appendChild(pt, allList);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContentType> getTreeData(Map map) throws Exception {
		List<ContentType> topList = (List<ContentType>) dao.findForList("ContentTypeMapper.selectTop", map);
		List<ContentType> allList = (List<ContentType>) dao.findForList("ContentTypeMapper.selectAll", map);

		if (CollectionUtils.isEmpty(allList) || CollectionUtils.isEmpty(topList)) {
			return null;
		}
		for (ContentType top : topList) {
			this.appendChild(top, allList);
		}
		return topList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContentType> getUpdateTree(Map map) throws Exception {
		List<ContentType> topList = (List<ContentType>) dao.findForList("ContentTypeMapper.selectUpdateTop", map);
		List<ContentType> allList = (List<ContentType>) dao.findForList("ContentTypeMapper.selectUpdateAll", map);

		if (CollectionUtils.isEmpty(allList) || CollectionUtils.isEmpty(topList)) {
			return null;
		}
		for (ContentType top : topList) {
			this.appendChild(top, allList);
		}
		return topList;
	}

	@Override
	public List<ContentType> getTreeByColumId(Map map) throws Exception {
		List<ContentType> listAll = (List<ContentType>) dao.findForList("ContentTypeMapper.selectContentTypeByCloumnId", map);
		List<ContentType> listTop = new ArrayList<>();
		List<String> ids = new ArrayList<>();
		if (CollectionUtils.isEmpty(listAll)) {
			return null;
		}
		for (ContentType contentType : listAll) {
			ids.add(contentType.getId());
		}
		for (int i = 0; i < listAll.size(); i++) {
			if (!ids.contains(listAll.get(i).getpId())) {
				this.appendChild(listAll.get(i), listAll);
				listTop.add(listAll.get(i));
			}
		}
		return listTop;
	}

	@Override
	public List<ContentType> getTypeByColumId(Map map) throws Exception {
		// TODO Auto-generated method stub
		return (List<ContentType>) dao.findForList("ContentTypeMapper.selectContentTypeByCloumnId", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContentType> getlistTreeData(Map map) throws Exception {
		List<ContentType> topList = (List<ContentType>) dao.findForList("ContentTypeMapper.selectListTop", map);
		List<ContentType> allList = (List<ContentType>) dao.findForList("ContentTypeMapper.selectListAll", map);

		if (CollectionUtils.isEmpty(allList) || CollectionUtils.isEmpty(topList)) {
			return null;
		}
		for (ContentType top : topList) {
			this.appendChild(top, allList);
		}
		return topList;
	}

	/**
	 * 保存
	 **/
	@Override
	public void save(ContentType content) throws Exception {
		Map map = content.getContentTypeMap();
		// 保存产品
		dao.save("ContentTypeMapper.insertContentType", content);
		// 保存图片
		if ((Boolean) map.get("flag")) {
			dao.save("ContentMapper.saveImage", map);
		}
		// 保存富文本
		dao.save("ContentTypeMapper.insertTXT", content);
		//
		// 保存栏目关系
		if (content.getColumnId() != null && !"".equals(content.getColumnId())) {
			dao.save("ContentTypeMapper.insertContenttype_column", content);
			//添加内容分类地址栏的url信息
			dao.save("ContentTypeMapper.saveContentTypeUrlNameconfig", content);
		}
		// 保存seo
		map.put("SEO_KEYWORDS", content.getTypeKeywords());
		map.put("MASTER_ID", content.getId());
		map.put("ID", UuidUtil.get32UUID());
		map.put("CREATE_TIME", DateUtil.getTime());
		map.put("SEO_DESCRIPTION", content.getTypeDescription());
		seoService.saveSeoContent(map);
	}

	/**
	 * 查询所有分类
	 **/
	@SuppressWarnings("unchecked")
	@Override
	public List<ContentType> findContentTypeToList(Map map) throws Exception {
		// 查询所有类型
		return (List<ContentType>) dao.findForList("ContentTypeMapper.selectContentTypeListPage", map);
	}

	/**
	 * 跳到修改页面
	 **/
	@Override
	public Map<String, Object> findContentTypeToEdit(String id) throws Exception {
		Map<String, Object> map= new HashMap<>();
		Map pd= new HashMap();
		String propertiesString = SystemConfig.getPropertiesString("jdbc.url");
		int beginIndex = propertiesString.lastIndexOf("/")+1;
		int endIndex = propertiesString.indexOf("?");
		String jdbc = propertiesString.substring(beginIndex,endIndex);
		map.put("contentTypeNotStyle", dao.findForList("ContentTypeMapper.selectContentTypeNotStyle", jdbc));
		if(map.get("contentTypeNotStyle").equals(null)){
			map.put("contentType", dao.findForObject("ContentTypeMapper.selectContentTypeById", id));
		}else{
			map.put("contentType", dao.findForObject("ContentTypeMapper.selectStyleContentTypeById", id));
		}
		pd.put("MASTER_ID", id);
		//		pd.put("SEO_KEYWORDS", map.get("typeKeywords"));
		//		pd.put("SEO_DESCRIPTION", map.get("typeDescription"));
		map.put("seo", seoService.querySeoForContent(pd));
		return map;
	}

	/**
	 * 修改方法
	 */
	@Override
	public void update(ContentType content) throws Exception {
		Map map = content.getContentTypeMap();
		//PageData pd=product.getPd();
		if ((Boolean) map.get("flag")) {
			List<Map> list = (List<Map>) dao.findForList("ContentTypeMapper.selectImagePach", new String[] { content.getId() });// 查询图片路径
			List<String> listID = new ArrayList<>();
			for (Map p : list) {
				if (map != null) {
					String imageId = (String) p.get("IMAGE_ID");
					listID.add(imageId);
					String pach = (String) p.get("IMGURL");
					if (pach != null && Tools.notEmpty(pach.trim())) {
						DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + pach); // 删除图片
					}
				}

			}

			if (listID.size() > 0) {
				dao.delete("ContentMapper.deleteContentImage", listID.toArray());// 删除图片信息
			}
			dao.save("ContentMapper.saveImage", map);// 保存图片信息

		} else {

			if (content.getImageId() == null || "".equals(content.getImageId())) {
				List<Map> list = (List<Map>) dao.findForList("ContentTypeMapper.selectImagePach", new String[] { content.getId() });// 查询图片路径
				List<String> listID = new ArrayList<>();
				for (Map p : list) {
					if (p != null) {
						String imageId = (String) p.get("IMAGE_ID");
						listID.add(imageId);
						String pach = (String) p.get("IMGURL");
						if (pach != null && Tools.notEmpty(pach.trim())) {
							DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + pach); // 删除图片
						}
					}

				}

				if (listID.size() > 0) {
					dao.delete("ContentMapper.deleteContentImage", listID.toArray());// 删除图片信息
				}
			}

		}
		// 删除关联关系
		// 保存栏目关系
		// 重新建立关联关系
		if (content.getColumnids() != null && content.getColumnids().length > 0) {
			dao.delete("ContentTypeMapper.deleteContenttype_column", new String[] { content.getId() });
			dao.save("ContentTypeMapper.insertContenttype_column", content);
		}
		//修改栏目地址栏的url信息
		Integer columUrlNameSize = (Integer)dao.findForObject("ContentTypeMapper.findContentTypeUrlNameList", content);
		if(columUrlNameSize > 0){
			dao.update("ContentTypeMapper.editContentTypeUrlNameconfig", content);
		}else{
			dao.save("ContentTypeMapper.saveContentTypeUrlNameconfig", content);
		}

		dao.update("ContentTypeMapper.updateTXT", content);// 修改富文本
		dao.update("ContentTypeMapper.updateById", content);
		// 保存seo
		map.put("SEO_KEYWORDS", content.getTypeKeywords());
		map.put("MASTER_ID", content.getId());
		map.put("SEO_DESCRIPTION", content.getTypeDescription());
		seoService.updateSeoContent(map);
	}

	/**
	 * 修改状态
	 */
	@Override
	public void updataStatus(Map<String, Object> map) throws Exception {
		dao.update("ContentTypeMapper.updateContentStatusByIds", map);
		dao.update("ContentTypeMapper.updateStatusByIds", map);
	}

	@Override
	public void updateSort(Map<String, Object> map) throws Exception {
		dao.update("ContentTypeMapper.updateSortById", map);
	}

	@Override
	public Integer findcount(Map<String, Object> map) throws Exception {
		return (Integer) dao.findForObject("ContentTypeMapper.selectCount", map);
	}

	@Override
	public Integer findContentycount(Map<String, Object> map) throws Exception {
		return (Integer) dao.findForObject("ContentTypeMapper.selectContentycountCount", map);
	}

	@Override
	public Integer findBrandcount(Map<String, Object> map) throws Exception {
		return (Integer) dao.findForObject("ContentTypeMapper.selectBrandcountCount", map);
	}

	@Override
	public List<ContentType> findContentTypeList(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> map= new HashMap<>();
		String propertiesString = SystemConfig.getPropertiesString("jdbc.url");
		int beginIndex = propertiesString.lastIndexOf("/")+1;
		int endIndex = propertiesString.indexOf("?");
		String jdbc = propertiesString.substring(beginIndex,endIndex);
		map.put("contentTypeNotStyle", dao.findForList("ContentTypeMapper.selectContentTypeNotStyle", jdbc));
		if(map.get("contentTypeNotStyle").equals(null)){
			return (List<ContentType>) dao.findForList("ContentTypeMapper.findContentTypeList", pd);
		}else{
			return (List<ContentType>) dao.findForList("ContentTypeMapper.findStyleContentTypeList", pd);
		}
	}

	@Override
	public List<ContentType> findContentTypeListByColumnIdOrPid(PageData pd) throws Exception {
		return (List<ContentType>) dao.findForList("ContentTypeMapper.selectContentTypeListByColumnIdOrPid", pd);
	}
}
