package cn.cebest.service.system.download.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.bo.ContentInfoBo;
import cn.cebest.entity.system.download.FileType;
import cn.cebest.service.system.download.FileResourceService;
import cn.cebest.service.system.download.FileTypeService;
import cn.cebest.service.system.seo.SeoService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.DelAllFile;
import cn.cebest.util.ExtendFiledUtil;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.Tools;
import cn.cebest.util.UuidUtil;

/**
 * 下载类型接口实现类
 * 
 * @author wzd
 *
 */
@Service("fileTypeService")
public class FileTypeServiceImpl implements FileTypeService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Resource(name = "fileResourceService")
	private FileResourceService service;
	@Resource(name = "seoService")
	private SeoService seoService;

	@SuppressWarnings("unchecked")
	@Override
	public List<FileType> getTreeData(PageData pd) throws Exception {
		List<FileType> listTop = (List<FileType>) dao.findForList("fileTypeMapper.selectFileTypeTop", pd);
		List<FileType> listAll = (List<FileType>) dao.findForList("fileTypeMapper.selectFileTypeAll", pd);
		if (CollectionUtils.isEmpty(listAll) || CollectionUtils.isEmpty(listTop)) {
			return null;
		}
		for (FileType top : listTop) {
			this.appendChild(top, listAll);
		}
		return listTop;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FileType> getTreeByColumId(PageData pd) throws Exception {
		List<FileType> listAll = (List<FileType>) dao.findForList("fileTypeMapper.selectFileTypeByCloumnId", pd);
		List<FileType> listTop = new ArrayList<>();
		List<String> ids = new ArrayList<>();
		if (CollectionUtils.isEmpty(listAll)) {
			return null;
		}
		for (FileType fileType : listAll) {
			ids.add(fileType.getDownload_id());
		}
		for (int i = 0; i < listAll.size(); i++) {
			if (!ids.contains(listAll.get(i).getPid())) {
				this.appendChildFileType(listAll.get(i), listAll);
				listTop.add(listAll.get(i));
			}
		}
		return listTop;
	}

	private void appendChild(FileType root, List<FileType> allList) {
		for (int i = 0; i < allList.size(); i++) {
			FileType pt = allList.get(i);
			if (pt != null && pt.getPid().equals(root.getDownload_id())) {
				if (CollectionUtils.isEmpty(root.getChildList())) {
					root.setChildList(new ArrayList<FileType>());
				}
				root.getChildList().add(pt);
				this.appendChild(pt, allList);
			}
		}
	}

	@Override
	public void save(FileType download, PageData pd) throws Exception {
		// 保存文件类型
		dao.save("fileTypeMapper.insertFileType", download);
		// 保存图片
		if ((Boolean) pd.get("flag")) {
			dao.save("ProductMapper.saveImage", pd);
		}
		// 保存富文本
		dao.save("fileTypeMapper.insertTXT", download);
		// 保存栏目关系
		if (download.getColumnids() != null && download.getColumnids().length > 0) {
			dao.save("fileTypeMapper.insertColumnids", download);
			dao.save("fileTypeMapper.saveContentTypeUrlNameconfig", download);
		}
		// 保存seo
		pd.put("SEO_KEYWORDS", download.getKeywords());
		pd.put("MASTER_ID", download.getDownload_id());
		pd.put("ID", UuidUtil.get32UUID());
		pd.put("CREATED_TIME", DateUtil.getTime());
		pd.put("SEO_DESCRIPTION", download.getSeo_description());
		seoService.insetSeo(pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FileType> getListTree(PageData pd) throws Exception {
		List<FileType> listTop = (List<FileType>) dao.findForList("fileTypeMapper.selectFileTypeliltPageTop", pd);
		List<FileType> listAll = (List<FileType>) dao.findForList("fileTypeMapper.selectFileTypeliltPageAll", pd);
		if (CollectionUtils.isEmpty(listAll) || CollectionUtils.isEmpty(listTop)) {
			return null;
		}
		for (FileType top : listTop) {
			this.appendChildListPage(top, listAll);
		}
		return listTop;
	}

	private void appendChildListPage(FileType root, List<FileType> allList) {
		for (int i = 0; i < allList.size(); i++) {
			FileType pt = allList.get(i);
			if (pt != null && pt.getPid().equals(root.getDownload_id())) {
				if (CollectionUtils.isEmpty(root.getChildList())) {
					root.setChildList(new ArrayList<FileType>());
				}
				root.getChildList().add(pt);
				this.appendChildListPage(pt, allList);
			}
		}
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

	@SuppressWarnings("unchecked")
	@Override
	public void deleteFileType(String[] ids) throws Exception {
		List<String> newIdlist = new ArrayList(Arrays.asList(ids));
		PageData data = new PageData();
		List<Map<String, String>> listAll = (List<Map<String, String>>) dao.findForList("fileTypeMapper.selectAllIdPiD",
				data);
		for (int i = 0; i < newIdlist.size(); i++) {
			appendChildIds(newIdlist, newIdlist.get(i), listAll);
		}
		// 删除关联关系
		dao.delete("fileTypeMapper.deleteColumnids", ids);
		
		// 删除下载分类的地址栏URL信息
		dao.delete("fileTypeMapper.deleteFiletypeUrlName", ids);
		// 删除下载内容的地址栏URL信息
		dao.delete("fileTypeMapper.deleteFiletypeContentUrlName", ids);
		
		List<PageData> list = (List<PageData>) dao.findForList("fileTypeMapper.selectImagePach",
				newIdlist.toArray(new String[newIdlist.size()]));// 查询图片路径
		List<String> listID = new ArrayList<>();
		for (PageData pd : list) {
			if (pd != null) {
				String imageId = pd.getString("IMAGE_ID");
				listID.add(imageId);
				String pach = pd.getString("IMGURL");
				if (pach != null && Tools.notEmpty(pach.trim())) {
					DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + pach); // 删除图片
				}
			}

		}

		if (listID.size() > 0) {
			dao.delete("ProductMapper.deleteProductImage", listID.toArray());// 删除图片信息
		}
		List<String> listid = (List<String>) dao.findForList("fileTypeMapper.selectFiles",
				newIdlist.toArray(new String[newIdlist.size()]));// 根据产品类型id查询产品id

		if (listid.size() > 0) {
			service.deleteFile(listid.toArray(new String[listid.size()]));
		}
		dao.delete("fileTypeMapper.deleteTXT", newIdlist.toArray(new String[newIdlist.size()]));// 删除富文本
		dao.delete("fileTypeMapper.deleteFileType", newIdlist.toArray(new String[newIdlist.size()]));
		PageData pd = new PageData();
		pd.put("MASTER_ID", ids);
		seoService.deleteSeo(pd);
	}

	@Override
	public void update(FileType download, PageData pd) throws Exception {
		if ((Boolean) pd.get("flag")) {

			List<PageData> list = (List<PageData>) dao.findForList("fileTypeMapper.selectImagePach",
					new String[] { download.getDownload_id() });// 查询图片路径
			List<String> listID = new ArrayList<>();
			for (PageData p : list) {
				if (p != null) {
					String imageId = p.getString("IMAGE_ID");
					listID.add(imageId);
					String pach = p.getString("IMGURL");
					if (pach != null && Tools.notEmpty(pach.trim())) {
						DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + pach); // 删除图片
					}
				}

			}

			if (listID.size() > 0) {
				dao.delete("ProductMapper.deleteProductImage", listID.toArray());// 删除图片信息
			}

			dao.save("ProductMapper.saveImage", pd);// 保存图片信息

		} else {

			if (download.getImageid() == null || "".equals(download.getImageid())) {
				List<PageData> list = (List<PageData>) dao.findForList("fileTypeMapper.selectImagePach",
						new String[] { download.getDownload_id() });// 查询图片路径
				List<String> listID = new ArrayList<>();
				for (PageData p : list) {
					if (p != null) {
						String imageId = p.getString("IMAGE_ID");
						listID.add(imageId);
						String pach = p.getString("IMGURL");
						if (pach != null && Tools.notEmpty(pach.trim())) {
							DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + pach); // 删除图片
						}
					}

				}

				if (listID.size() > 0) {
					dao.delete("ProductMapper.deleteProductImage", listID.toArray());// 删除图片信息
				}
			}

		}
		// 删除关联关系
		// 保存栏目关系
		// 重新建立关联关系
		if (download.getColumnids() != null && download.getColumnids().length > 0) {
			dao.delete("fileTypeMapper.deleteColumnids", new String[] { download.getDownload_id() });
			dao.save("fileTypeMapper.insertColumnids", download);
		}
		
		//修改栏目地址栏的url信息
		Integer columUrlNameSize = (Integer)dao.findForObject("fileTypeMapper.findContentTypeUrlNameList", download);
		if(columUrlNameSize > 0){
			dao.update("fileTypeMapper.editContentTypeUrlNameconfig", download);
		}else{
			dao.save("fileTypeMapper.saveContentTypeUrlNameconfig", download);
		}
		
		Integer i = (Integer) dao.findForObject("fileTypeMapper.selectTXTCount", download);
		if (i > 0) {
			dao.update("fileTypeMapper.updateTXT", download);// 修改富文本
		} else {
			if (download.getTXT() != null) {
				dao.save("fileTypeMapper.insertTXT", download);
			}
		}
		dao.update("fileTypeMapper.updateById", download);
		// 保存seo
		pd.put("SEO_KEYWORDS", download.getKeywords());
		pd.put("MASTER_ID", download.getDownload_id());
		pd.put("SEO_DESCRIPTION", download.getSeo_description());
		seoService.updateSeo(pd);
	}

	@Override
	public Map<String, Object> findById(Map<String, Object> map, String id) throws Exception {
		FileType list = (FileType) dao.findForObject("fileTypeMapper.selectById", id);
		//map.put("fileType", dao.findForObject("fileTypeMapper.selectById", id));\
		map.put("fileType",list);
		PageData pd = new PageData();
		pd.put("MASTER_ID", id);
		map.put("seo", seoService.querySeoForObject(pd));
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FileType> search(PageData pd) throws Exception {
		// List<FileType> listTop = (List<FileType>)
		// dao.findForList("fileTypeMapper.selectFileTypeAearchTop", pd);
		List<FileType> listAll = (List<FileType>) dao.findForList("fileTypeMapper.selectFileTypeliltPageAll", pd);
		List<FileType> listTop = new ArrayList<>();
		List<String> ids = new ArrayList<>();
		if (CollectionUtils.isEmpty(listAll)) {
			return null;
		}
		for (FileType fileType : listAll) {
			ids.add(fileType.getDownload_id());
		}
		for (int i = 0; i < listAll.size(); i++) {
			if (!ids.contains(listAll.get(i).getPid())) {
				this.appendChildFileType(listAll.get(i), listAll);
				listTop.add(listAll.get(i));
			}
		}
		return listTop;
	}

	@Override
	public void deleteByColumnIds(String[] ids) throws Exception {
		// 查询产品类型ids
		List<String> listId = (List<String>) dao.findForList("fileTypeMapper.selectIdsByColumnIds", ids);

		if (listId.size() > 0) {
			List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) dao
					.findForList("fileTypeMapper.selectCountByIds", listId.toArray(new String[listId.size()]));
			for (HashMap<String, Object> hashMap : list) {
				if ((Long) hashMap.get("COUNT(*)") > 1) {
					listId.remove(hashMap.get("DOWNLOADID"));
				}
			}
			// 删除产品类型
			if (listId.size() > 0) {
				deleteFileType(listId.toArray(new String[listId.size()]));
			}
		}
		dao.delete("fileTypeMapper.deleteByColumnIds", ids);
	}

	@Override
	public List<FileType> findFileTypeByColumnIds(String id) throws Exception {
		List<FileType> listAll = (List<FileType>) dao.findForList("fileTypeMapper.selectFileTypeByColumnIdTop", id);
		List<FileType> listTop = new ArrayList<>();
		List<String> ids = new ArrayList<>();
		if (CollectionUtils.isEmpty(listAll)) {
			return null;
		}
		for (FileType fileType : listAll) {
			ids.add(fileType.getDownload_id());
		}
		for (int i = 0; i < listAll.size(); i++) {
			if (!ids.contains(listAll.get(i).getPid())) {
				this.appendChildFileType(listAll.get(i), listAll);
				listTop.add(listAll.get(i));
			}
		}
		return listTop;

	}

	private void appendChildFileType(FileType root, List<FileType> allList) {
		for (int i = 0; i < allList.size(); i++) {
			FileType pt = allList.get(i);
			if (pt != null && pt.getPid().equals(root.getDownload_id())) {
				if (CollectionUtils.isEmpty(root.getChildList())) {
					root.setChildList(new ArrayList<FileType>());
				}
				root.getChildList().add(pt);
				this.appendChildFileType(pt, allList);
			}
		}
	}
	
	@Override
	public List<FileType> selectFileByColumdlistPage(Page page) throws Exception {
		List<FileType> fileList=(List<FileType>)dao.findForList("fileTypeMapper.selectFileByColumdlistPage", page);
		ObjectMapper objectMapper = new ObjectMapper();
		int sum = 0;
		for (FileType fileType : fileList) {
			if(fileType.getFiledJson()!= null && !"".equals(fileType.getFiledJson())){
				@SuppressWarnings("unchecked")
				List<ExtendFiledUtil> fileds = objectMapper.readValue(fileType.getFiledJson(), List.class);
				fileType.setFileds(fileds);
			}
			String[] array = new String[fileList.size()];
			String download_id = fileType.getDownload_id();
			array[sum] = download_id;
			List downloadList =(List) dao.findForList("fileResourceMapper.selectFileById", array);
			if(downloadList.size()>0){
				fileType.setFileList(downloadList);
			}
			sum+=1;
		}
		
		return fileList;
	}
	
	
	
	
	
	

	@Override
	public void updateSort(Map<String, Object> map) throws Exception {
		dao.update("fileTypeMapper.updateSortById", map);

	}

	@Override
	public List<FileType> findFileTypeAllByAll(Map<String, Object> map) throws Exception {
		return (List<FileType>) dao.findForList("fileTypeMapper.selectAllByAll", map);
	}

	@Override
	public FileType findTypeInfoById(String id) throws Exception {
		return (FileType) dao.findForObject("fileTypeMapper.findTypeInfoById", id);
	}

	@Override
	public List<ContentInfoBo> findAllGroomData(List<String> typeIdList) throws Exception {
		// TODO Auto-generated method stub
		return (List<ContentInfoBo>) dao.findForList("fileTypeMapper.findAllGroomData", typeIdList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FileType> findFileTypeOption(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		List<String> ids = (List<String>) dao.findForList("fileTypeMapper.selectFileTypeOption1", pd);
		return (List<FileType>) dao.findForList("fileTypeMapper.selectFileTypeOption", ids);
	}

	@Override
	public Integer findcount(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return (Integer) dao.findForObject("fileTypeMapper.selectCount", map);
	}

	@Override
	public List<FileType> getTypeByColumId(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (List<FileType>) dao.findForList("fileTypeMapper.selectFileTypeByCloumnId", pd);
	}
	
	@Override
	public void updateStatusByIds(PageData pd) throws Exception {
		dao.update("fileTypeMapper.updateStatusByIds", pd);
	}
	
	/**
	 * 修改状态*/
	@Override
	public void updataStatus(Map<String, Object> map) throws Exception {
		dao.update("fileTypeMapper.updateContentStatusByIds", map);
		dao.update("fileTypeMapper.updateTypeStatusByIds", map);
	}

	
}
