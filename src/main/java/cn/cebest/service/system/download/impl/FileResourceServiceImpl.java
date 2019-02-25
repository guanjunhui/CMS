package cn.cebest.service.system.download.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;

import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.content.Content;

import cn.cebest.entity.system.download.DownloadFiles;
import cn.cebest.entity.system.download.FileResources;
import cn.cebest.entity.system.newMessage.NewMessage;
import cn.cebest.service.system.content.content.impl.ContentServiceImpl;
import cn.cebest.service.system.download.FileResourceService;
import cn.cebest.service.system.seo.SeoService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.DelAllFile;
import cn.cebest.util.ExtendFiledUtil;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Logger;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.Tools;
import cn.cebest.util.UuidUtil;

/**
 * 
 * @author wzd
 *
 */
@Service("fileResourceService")
public class FileResourceServiceImpl implements FileResourceService {
	
	private static Logger logger = Logger.getLogger(FileResourceServiceImpl.class);
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Resource(name = "seoService")
	private SeoService service;

	@Override
	public void save(FileResources download, PageData pd) throws Exception {
		// 保存文件
		dao.save("fileResourceMapper.insertFile", download);
		// 保存可下载的文件
		if (download.getFiles().size() > 0) {
			dao.save("fileResourceMapper.insertDownloadFiles", download);
		}
		// 保存图片
		if ((Boolean) pd.get("flag")) {
			dao.save("ProductMapper.saveImage", pd);
		}
		//保存多张图片
		if(download.getTimageList().size() >0 ){
			dao.save("ProductMapper.saveImages", download);
		}
		
		
		// 保存富文本
		dao.save("fileResourceMapper.insertTXT", download);
		if (download.getFileids() != null && download.getFileids().length > 0) {
			// 保存文件和文件关系
			dao.save("fileResourceMapper.insertRelevant", download);

		}
		// 保存文件和文件类型的关系
		if ((boolean) pd.get("is_ColumnOrYype")){
			dao.save("fileResourceMapper.insertFiletype_file", download);
			//添加文件地址栏的url信息
			dao.save("fileResourceMapper.insertFileUrlNametype_file", download);
		}
		// 保存seo
		pd.put("SEO_TITLE", download.getSeo_title());
		pd.put("SEO_KEYWORDS", download.getKeywords());
		pd.put("MASTER_ID", download.getFileid());
		pd.put("ID", UuidUtil.get32UUID());
		pd.put("CREATED_TIME", DateUtil.getTime());
		pd.put("SEO_DESCRIPTION", download.getSeo_description());
		service.insetSeo(pd);

	}
	/**
	 * 查询资讯列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FileResources> findMessageTypeList(Page page) throws Exception {
		//分类已改为异步请求独立获取
		//map.put("product_TypeList", dao.findForList("ProductMapper.selectProductType", page.getPd()));
		List<String> ids = (List<String>) dao.findForList("fileResourceMapper.selectTestlistPage", page);
		if (ids == null || ids.size() == 0) {
			return null;
		}
		Set<String> idsSet=new HashSet<String>(ids);
		PageData pd = page.getPd();
		pd.put("ids", idsSet);
		List<FileResources> list = (List<FileResources>) dao.findForList("fileResourceMapper.selectTest", pd);
		return list;
				
	}
	@SuppressWarnings("unchecked")
	@Override
	public void deleteFile(String[] ids) throws Exception {
		dao.delete("fileResourceMapper.deleteRelevant", ids);// 删除关联关系
		List<PageData> list = (List<PageData>) dao.findForList("fileResourceMapper.selectImagePach", ids);// 查询图片路径
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
		List<PageData> fileList = (List<PageData>) dao.findForList("fileResourceMapper.selectFilePach", ids);// 查询文件路径
		List<Integer> fileIdList = new ArrayList<>();
		for (PageData pd : fileList) {
			if (pd != null) {
				Integer fileId = (Integer) pd.get("ID");
				fileIdList.add(fileId);
				String pach = pd.getString("FILEPACH");
				if (pach != null && Tools.notEmpty(pach.trim())) {
					DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + pach); // 删除文件
				}
			}
		}

		if (fileIdList.size() > 0) {
			dao.delete("fileResourceMapper.deleteDownloadFiles", fileIdList.toArray());// 删除可下载文件信息
		}
		if (listID.size() > 0) {
			dao.delete("ProductMapper.deleteProductImage", listID.toArray());// 删除图片信息
		}
		dao.delete("fileResourceMapper.deleteFiletype_file", ids);// 删除类型和产品关系
		
		dao.delete("fileResourceMapper.deleteFileUrlNametype_file", ids);// 删除文件地址栏的url信息
		
		dao.delete("fileResourceMapper.deleteTXT", ids);// 删除富文本
		dao.delete("fileResourceMapper.deleteFile", ids);// 删除产品
		PageData pd = new PageData();
		pd.put("MASTER_ID", ids);
		service.deleteSeo(pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FileResources> findByTypeIds(Map<String, Object> map) throws Exception {
		return (List<FileResources>) dao.findForList("fileResourceMapper.selectByTypeIds", map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FileResources> findTypeById(String[] id) throws Exception {
		// TODO Auto-generated method stub
		return (List<FileResources>) dao.findForList("fileResourceMapper.selectTypeById", id);
	}

	@Override
	public Map<String, Object> findById(Map<String, Object> map, String id) throws Exception {
		FileResources fileResources = (FileResources) dao.findForObject("fileResourceMapper.selectById", id);
		if (fileResources.getFiledJson() != null && !"".equals(fileResources.getFiledJson())) {
			ObjectMapper objectMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			List<ExtendFiledUtil> fileds = objectMapper.readValue(fileResources.getFiledJson(), List.class);
			fileResources.setFileds(fileds);
		}
		map.put("file", fileResources);
		map.put("fileR", dao.findForList("fileResourceMapper.selectByReId", id));

		PageData pd = new PageData();
		pd.put("MASTER_ID", id);
		map.put("seo", service.querySeoForObject(pd));
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FileResources> getList(Page page) throws Exception {
		List<String> ids = (List<String>) dao.findForList("fileResourceMapper.selectTestlistPage", page);
		if (ids == null || ids.size() == 0) {
			return null;
		}
		PageData pd = page.getPd();
		pd.put("ids", ids);
		return (List<FileResources>) dao.findForList("fileResourceMapper.selectTest", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(FileResources download, PageData pd) throws Exception {
		if ((Boolean) pd.get("flag")) {
			List<PageData> list = (List<PageData>) dao.findForList("fileResourceMapper.selectImagePach",
					new String[] { download.getFileid() });// 查询图片路径
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

				List<PageData> list = (List<PageData>) dao.findForList("fileResourceMapper.selectImagePach",
						new String[] { download.getFileid() });// 查询图片路径
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
		
		// 操作图片-删除
		if (download.getImageids() != null && download.getImageids().size() > 0) {
			List<PageData> list = (List<PageData>) dao.findForList("ContentMapper.selectImagePach",
					new String[] { download.getFileid() });// 查询图片路径
			List<String> listID = new ArrayList<>();
			for (PageData p : list) {
				if (p != null) {
					String imageId = p.getString("IMAGE_ID");
					if (download.getImageids().contains(imageId)) {
						continue;
					}
					listID.add(imageId);
					String pach = p.getString("IMGURL");
					if (pach != null && Tools.notEmpty(pach.trim())) {
						DelAllFile.deleteFile(PathUtil.getUploadPath() + Const.FILEPATHIMG + pach); // 删除图片
					}
				}
			}
			if (listID.size() > 0) {
				dao.delete("ProductMapper.deleteProductImage", listID.toArray());// 删除图片信息
			}
		} else {
			List<PageData> list = (List<PageData>) dao.findForList("ContentMapper.selectImagePach",
					new String[] { download.getFileid() });// 查询图片路径
			List<String> listID = new ArrayList<>();
			for (PageData p : list) {
				String imageId = p.getString("IMAGE_ID");
				listID.add(imageId);
				if (p != null) {
					String pach = p.getString("IMGURL");
					if (pach != null && Tools.notEmpty(pach.trim())) {
						DelAllFile.deleteFile(PathUtil.getUploadPath() + Const.FILEPATHIMG + pach); // 删除图片
					}
				}
			}
			if (listID.size() > 0) {
				dao.delete("ProductMapper.deleteProductImage", listID.toArray());// 删除图片信息
			}
		}
		// 操作图片-保存新添加的
		List<Image> inm = download.getPictureList();
		if (inm != null && inm.size() > 0) {
			for (Image image : inm) {
				// 没有传图片,但有其他信息的情况
				if (image.getImageId() == null || "".equals(image.getImageId())) {
					image.setImageId(UuidUtil.get32UUID());
					image.setMaster_id(download.getFileid());
				}
			}
			dao.save("fileResourceMapper.saveImages", download);
		}
		// 操作图片-修改或保存
		List<Image> i = download.getTimageList();
		if (i != null && i.size() > 0) {
			for (Image image : i) {
				// 只点击删除图片
				if (!download.getImageids().contains(image.getImageId())) {
					image.setMaster_id(download.getFileid());
					if(!image.isFlag()){
						image.setImgurl(null);
					}
					dao.save("ContentMapper.saveImage", image);
					continue;
				}
				if (image != null) {
					dao.update("ContentMapper.updateImageById", image);
				}
			}

		}
		
		// 重新建立产品类型和产品关联关系
		if ((boolean) pd.get("is_ColumnOrYype")) {
			// 删除产品类型和产品关联关系
			dao.delete("fileResourceMapper.deleteFiletype_file", new String[] { download.getFileid() });
			// 保存文件和文件类型的关系
			dao.save("fileResourceMapper.insertFiletype_file", download);
		} else {
			// 删除产品类型和产品关联关系
			dao.delete("fileResourceMapper.deleteFiletype_file", new String[] { download.getFileid() });
		}
		/*//修改文件地址栏的url信息
		Integer contentUrlNameSize = (Integer)dao.findForObject("fileResourceMapper.findContentUrlNameList", download);
		if(contentUrlNameSize > 0){
			dao.update("fileResourceMapper.editContentUrlNameconfig", download);
		}else{
			//添加文件地址栏的url信息
			dao.save("fileResourceMapper.insertFileUrlNametype_file", download);
		}*/
		if (download.getFileids() != null && download.getFileids().length > 0) {
			// 删除关联关系
			dao.delete("fileResourceMapper.deleteRelevant", new String[] { download.getFileid() });// 删除关联关系
			// 保存文件和文件关系
			dao.save("fileResourceMapper.insertRelevant", download);

		} else {
			dao.delete("fileResourceMapper.deleteRelevant", new String[] { download.getFileid() });// 删除关联关系
		}
		if (download.getFilesIds() != null && download.getFilesIds().size() > 0) {
			List<PageData> fileList = (List<PageData>) dao.findForList("fileResourceMapper.selectFilePach",
					new String[] { download.getFileid() });// 查询文件路径
			List<Integer> fileIdList = new ArrayList<>();
			int index = 0 ;
			for (PageData pageData : fileList) {
				if (pageData != null) {
					Integer fileId = (Integer) pageData.get("ID");
					if (download.getFilesIds() != null && download.getFilesIds().contains(fileId)) {
						String mark =(String) pageData.get("MARK");
						DownloadFiles downloadMark = download.getDownloadMark().get(index);//获取未增加文件情况下,mark值
						String mark2 = downloadMark.getMark();//执行修改操作后的标记值
						if(mark !=null ){
							if(mark !=mark2 || !mark.equals(mark2)){
								
								//两次标记值不同,说明未变动的文件的标记发生改变,需要更改
								PageData pageData2 = new PageData();
								pageData2.put("downloadMark", downloadMark.getMark());
								pageData2.put("ID", fileId);
								dao.update("fileResourceMapper.updateFileMark", pageData2);
							}
						}
						index ++;
						continue;
					}
					fileIdList.add(fileId);
					String pach = pageData.getString("FILEPACH");
					if (pach != null && Tools.notEmpty(pach.trim())) {
						DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + pach); // 删除文件
					}
					
				}
				
				
				
			}

			if (fileIdList.size() > 0) {
				dao.delete("fileResourceMapper.deleteDownloadFiles", fileIdList.toArray());
			}

		} else {
			List<PageData> fileList = (List<PageData>) dao.findForList("fileResourceMapper.selectFilePach",
					new String[] { download.getFileid() });// 查询文件路径
			List<Integer> fileIdList = new ArrayList<>();
			for (PageData p : fileList) {
				if (p != null) {
					Integer fileId = (Integer) p.get("ID");
					fileIdList.add(fileId);
					String pach = p.getString("FILEPACH");
					if (pach != null && Tools.notEmpty(pach.trim())) {
						DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + pach); // 删除文件
					}
				}
			}
			if (fileIdList.size() > 0) {
				dao.delete("fileResourceMapper.deleteDownloadFiles", fileIdList.toArray());// 删除可下载文件信息
			}
		}
		// 保存可下载的文件
		if (download.getFiles() != null && download.getFiles().size() > 0) {
			dao.save("fileResourceMapper.insertDownloadFiles", download);
		}
		Integer i1 = (Integer) dao.findForObject("fileResourceMapper.selectTXTCount", download);
		if (i1 > 0) {
			// 修改文本表数据
			dao.update("fileResourceMapper.updateTXT", download);
		} else {
			if (download.getTXT() != null) {
				dao.save("fileResourceMapper.insertTXT", download);
			}
		}
		// 修改产品
		dao.update("fileResourceMapper.updateFile", download);
		// 保存seo
		pd.put("SEO_TITLE", download.getSeo_title());
		pd.put("SEO_KEYWORDS", download.getKeywords());
		pd.put("MASTER_ID", download.getFileid());
		pd.put("SEO_DESCRIPTION", download.getSeo_description());
		service.updateSeo(pd);
	}

	@Override
	public void updataRecommendAndTop(FileResources file) throws Exception {
		dao.update("fileResourceMapper.updataRecommendAndTopByIds", file);

	}

	@Override
	public List<FileResources> findFileResourcesByColumnId(String id) throws Exception {
		List<String> ids = (List<String>) dao.findForList("fileResourceMapper.selectFileTypeIdsByColumnId", id);
		PageData pd = new PageData();
		pd.put("ids", ids);
		pd.put("TOP", "1");
		pd.put("TIME", DateUtil.getYear());
		return (List<FileResources>) dao.findForList("fileResourceMapper.selectFileResourcesByTypeIds", pd);
	}

	@Override
	public List<PageData> findFileAllByAll(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return (List<PageData>) dao.findForList("fileResourceMapper.selectAllByAll", map);
	}

	@Override
	public List<PageData> findTopList() throws Exception {
		return (List<PageData>) dao.findForList("fileResourceMapper.findTopList", null);
	}

	@Override
	public List<FileResources> findFileResourceAndDownloadFileByTypeId(Page page) throws Exception {
		PageData pd = page.getPd();
		pd.put("TOP", "1");
		pd.put("TIME", DateUtil.getYear());
		return (List<FileResources>) dao
				.findForList("fileResourceMapper.selectFileResourceAndDownloadFileByTypeIdlistPage", page);
	}

	@Override
	public List<FileResources> findTopPolicyforIndex(String columId) throws Exception {
		// TODO Auto-generated method stub
		return (List<FileResources>) dao.findForList("fileResourceMapper.findTopPolicyforIndex", columId);
	}

	@Override
	public PageData findTemplatePachById(String contentId) throws Exception {

		return (PageData) dao.findForObject("fileResourceMapper.selectTemplatePachById", contentId);
	}

	@Override
	public DownloadFiles findDownloadFileById(Integer id) throws Exception {
		return (DownloadFiles) dao.findForObject("fileResourceMapper.selectDownloadFileById", id);
	}

	@Override
	public List<FileResources> findFileFileResourceAndDownloadFileByColumnId(Page page) throws Exception {
		List<FileResources> fileList =(List<FileResources>) dao.findForList("fileResourceMapper.selectFileFileResourceAndDownloadFileByColumnIdlistPage", page);
		ObjectMapper objectMapper = new ObjectMapper();
		for (FileResources list : fileList) {
			if (list.getFiledJson() != null && !"".equals(list.getFiledJson())) {
				@SuppressWarnings("unchecked")
				List<ExtendFiledUtil> fileds = objectMapper.readValue(list.getFiledJson(), List.class);
				list.setFileds(fileds);
			}
		}
		return fileList;
	}

	
	@Override
	public JsonResult getDowList(Page page) {
		
		JsonResult jr = new JsonResult();
		Map<String, Object> data = new HashMap<>();
		try {
			List<String> ids = (List<String>) dao.findForList("fileResourceMapper.getDowIdlistPage", page);
			if (ids == null || ids.size() == 0) {
				data.put("data", null);
				data.put("page", null);
				jr.setCode(200);
				jr.setData(data);
				return jr;
			}
			PageData pd = page.getPd();
			pd.put("ids", ids);
			List<FileResources> fileList =(List<FileResources>) dao.findForList("fileResourceMapper.getDowList", pd);
			data.put("data", fileList);
			data.put("page", page);
			jr.setCode(200);
			jr.setData(data);
		} catch (Exception e) {
			jr.setCode(500);
			logger.error("查询资讯列表出现异常", e);
		}
		return jr;
	}
	
	@Override
	public FileResources findFileResourceAndDownloadFileById(String contentid) throws Exception {
		return (FileResources) dao.findForObject("fileResourceMapper.selectFileResourceAndDownloadFileById", contentid);
	}

	@Override
	public void updateDownlosd_count(String fileid) throws Exception {
		dao.update("fileResourceMapper.updateDownload_count", fileid);

	}

	@Override
	public List<PageData> getType(Page page) throws Exception {
		// TODO Auto-generated method stub
		return (List<PageData>) dao.findForList("fileResourceMapper.selectType", page);
	}

	@Override
	public void deleteByColumnIds(String[] ids) throws Exception {
		// 查询内容ids
		List<String> listId = (List<String>) dao.findForList("fileResourceMapper.selectIdsByColumnIds", ids);
		if (listId.size() > 0) {
			List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) dao
					.findForList("fileResourceMapper.selectCountByIds", listId.toArray(new String[listId.size()]));
			for (HashMap<String, Object> hashMap : list) {
				if ((Long) hashMap.get("COUNT(*)") > 1) {
					listId.remove(hashMap.get("FILEID"));
				}
			}
			// 删除内容
			deleteFile(listId.toArray(new String[listId.size()]));
		}
		// 删除关联关系
		dao.delete("fileResourceMapper.deleteByColumnIds", ids);

	}
	
	@Override
	public void updateStatusByIds(PageData pd) throws Exception {
		dao.update("fileResourceMapper.updateStatusByIds", pd);
	}
	
	@Override
	public void updataRecommendAndTopAndHot(FileResources file) throws Exception {
		dao.update("fileResourceMapper.updataRecommendAndTopAndHotByIds", file);

	}
	
	@Override
	public void updateSort(PageData pd) throws Exception {
		dao.update("fileResourceMapper.updateSort", pd);
		
	}
	@Override
	public List<FileResources> findReleventFileByid(String id) throws Exception {
		return (List<FileResources>) dao.findForList("fileResourceMapper.findReleventFileByid", id);
	}

	@Override
	public FileResources findTypeIdByName(String typeName) throws Exception {
		
		return  (FileResources)dao.findForObject("fileResourceMapper.findTypeIdByName",typeName);
	}
	@Override
	public void saveTypeRelation(PageData pd) throws Exception {
		
		dao.save("fileResourceMapper.saveTypeRelation", pd);
	}
	@Override
	public void saveImportData(FileResources fileResourse) throws Exception {
		// 保存文件
		dao.save("fileResourceMapper.insertFile", fileResourse);
		//保存富文本
		dao.save("fileResourceMapper.insertTXT", fileResourse);

	}
	@Override
	public FileResources findFileResourcesByTypeOrColumnid(PageData pd) throws Exception {
		return (FileResources) dao.findForObject("fileResourceMapper.findFileResourcesByTypeOrColumnid", pd);
	}
		
	@Override
	public List<FileResources> findlistPageFile(Page page) throws Exception {
		List<FileResources> fileList =(List<FileResources>) dao.findForList("fileResourceMapper.findlistPageFile", page);
		ObjectMapper objectMapper = new ObjectMapper();
		for (FileResources list : fileList) {
			if (list.getFiledJson() != null && !"".equals(list.getFiledJson())) {
				@SuppressWarnings("unchecked")
				List<ExtendFiledUtil> fileds = objectMapper.readValue(list.getFiledJson(), List.class);
				list.setFileds(fileds);
			}
		}
		return fileList;
	}
	
}
