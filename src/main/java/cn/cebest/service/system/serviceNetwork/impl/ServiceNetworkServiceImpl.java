package cn.cebest.service.system.serviceNetwork.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.bo.ContentInfoBo;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Video;
import cn.cebest.entity.system.ServiceNetwork.Agency;
import cn.cebest.entity.system.ServiceNetwork.ServiceNetwork;
import cn.cebest.entity.system.content.Content;
import cn.cebest.entity.web.ProductRegistration;
import cn.cebest.service.system.seo.SeoService;
import cn.cebest.service.system.serviceNetwork.ServiceNetworkService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.DelAllFile;
import cn.cebest.util.ExtendFiledUtil;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.Tools;
import cn.cebest.util.UuidUtil;

@Service("serviceNetworkService")
public class ServiceNetworkServiceImpl implements ServiceNetworkService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Resource(name = "seoService")
	private SeoService service;

	/**
	 * 前台查询服务商
	 */
	public List<ServiceNetwork> selectServiceFront(Page page) throws Exception {
		List<ServiceNetwork> snl = (List<ServiceNetwork>) dao.findForList("ServiceNetworkMapper.selectServiceFrontlistPage", page);
		return snl;
	}
	/**
	 * 前台根据名称查询服务商
	 * @throws Exception 
	 */
	@Override
	public List<ServiceNetwork> selectNetworkByName(PageData pd) throws Exception {
		List<ServiceNetwork> network = (List<ServiceNetwork>) dao.findForList("ServiceNetworkMapper.selectNetworkByName", pd);
		return network;
	}

	/**
	 * 获取服务商性质
	 */
	public List<ServiceNetwork> selectServiceNature() throws Exception {
		return (List<ServiceNetwork>) dao.findForList("ServiceNetworkMapper.selectServiceNature", null);
	}
	/**
	 * service：代理商显示列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ServiceNetwork> serviceNetworkListPage(Page page) throws Exception {
		List<ServiceNetwork> serviceNetwork = (List<ServiceNetwork>) dao.findForList("ServiceNetworkMapper.selectServiceNetworklistPage", page);
		return serviceNetwork;
	}
	/**
	 * zengxiangpeng
	 * service：新增服务网点
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void saveServiceNetwork(ServiceNetwork serviceNetwork) throws Exception {
		//保存服务网点
		dao.save("ServiceNetworkMapper.saveServiceNetwork", serviceNetwork);
	}
	
	@Override
	public void saveServiceNetwork(List<ServiceNetwork> list) throws Exception {
		//保存服务网点
		dao.save("ServiceNetworkMapper.saveBachServiceNetwork", list);
	}
	
	
	/**
	 * zengxiangpeng
	 * service：导入产品注册数据
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void saveProductRegistration(ProductRegistration productRegistration) throws Exception {
		//保存服务网点
		dao.save("ProductRegistrationMapper.save", productRegistration);
	}
	@Override
	public void saveProductRegistration(List<ProductRegistration> list) throws Exception {
		//保存服务网点
		dao.save("ProductRegistrationMapper.saveBachProductRegistration", list);
	}
	
	
	/**
	 * zengxiangpeng
	 * service：修改根据id查询服务网点
	 */
	@Override
	public ServiceNetwork selectServiceNetworkById(String id) throws Exception {
		ServiceNetwork serviceNetwork = (ServiceNetwork)dao.findForObject("ServiceNetworkMapper.selectServiceNetworkById", id);
		return serviceNetwork;
	}
	/**
	 * zengxiangpeng
	 * service：修改服务网点
	 */
	@Override
	public void updateServiceNetwork(ServiceNetwork serviceNetwork) throws Exception {
		//修改服务网点
		dao.update("ServiceNetworkMapper.updateServiceNetwork", serviceNetwork);
	}
	/**
	 * zengxiangpeng
	 * service：；批量删除代理商
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void delAllServiceNetwork(String[] id) throws Exception {
		dao.delete("ServiceNetworkMapper.deleteServiceNetworkBatch", id);// 删除内容
	}
	
	/**
	 * zengxiangpeng
	 * service：根据查询所有国家
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Agency> selectCountry(Agency agency) throws Exception {
		List<Agency> countryList =  (List<Agency>) dao.findForList("ServiceNetworkMapper.selectCountry", agency);
		/*String pageFlag = agency.getPageFlag();
		if("1".equals(pageFlag) && null != pageFlag) {
			for (int i = 0; i<countryList.size(); i++) {
				int count = (int) dao.findForObject("AgencyMapper.selectAgencyByCountryId", countryList.get(i).getCountryId());
				if(count == 0) {
					countryList.remove(i);
					i--;
				}
			}
		}*/
		return countryList;
	}
	/**
	 * 查询地址
	 */
	/*
	@SuppressWarnings("unchecked")
	public List<Banner> loadAddress(String country) throws Exception {
		return (List<Banner>)dao.findForList("AgencyFrontMapper.selectAddress", country);
	}*/
//结束
	
	
	
	
	
	
	
	
	
	/* begin */
	@SuppressWarnings("unchecked")
	@Override
	public List<Content> contentlistPage(Page page) throws Exception {///////////////////////////////////
		List<String> ids = (List<String>) dao.findForList("ContentMapper.selectTestlistPage", page);
		// PageData pd=page.getPd();
		// pd.put("ids", ids);
		if (ids == null || ids.size() == 0) {
			return null;
		}
		PageData pd = page.getPd();
		pd.put("ids", ids);
		return (List<Content>) dao.findForList("ContentMapper.selectTest", pd);
	}
	
	@Override
	public List<Content> listContentlistPage(Page page) throws Exception {
		return (List<Content>) dao.findForList("ContentMapper.listContentlistPage", page);
	}

	@Override
	public Content detailByContentId(Page page) throws Exception {
		return (Content) dao.findForObject("ContentMapper.detailByContentId", page);
	}

	@Override
	public void delContent(PageData pd) throws Exception {
		dao.update("ContentMapper.updateContent", pd);
	}

	@Override
	public void delAllContent(String[] arrayCONTENT_IDS) throws Exception {
		dao.update("ContentMapper.updateAllContent", arrayCONTENT_IDS);
	}

	@Override
	public void editContent(Content content) throws Exception {
		dao.update("ContentMapper.editContent", content);
		// 先删除相关内容关系
		dao.delete("ContentMapper.delContentRelevant", content);
		// 保存相关内容关系
		String[] relevantIdArr = content.getContentRelevantIdList();
		if (relevantIdArr != null && relevantIdArr.length > 0) {
			dao.save("ContentMapper.saveContentRelevant", content);
		}
		// 先删除栏目的中间表关系
		dao.delete("ContentMapper.delContentColumconfig", content);
		dao.save("ContentMapper.saveContentColumconfig", content);
	}

	@Override
	public void auditContent(PageData pd) throws Exception {
		dao.update("ContentMapper.auditContent", pd);
	}

	@Override
	public List<PageData> contentTypeList(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("ContentMapper.contentTypeList", pd);
	}

	@Override
	public List<PageData> contentRelevantList(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("ContentMapper.contentRelevantList", pd);
	}

	@Override
	public void editContentTxt(PageData pd) throws Exception {
		dao.update("ContentMapper.editContentTxt", pd);
	}

	@Override
	public Content findContentById(PageData pd) throws Exception {
		return (Content) dao.findForObject("ContentMapper.findContentById", pd);
	}

	@Override
	public PageData findContentTxtById(PageData pd) throws Exception {
		return (PageData) dao.findForObject("ContentMapper.findContentTxtById", pd);
	}



	@Override
	public void saveContent(Content content) throws Exception {
		// 保存内容
		dao.save("ContentMapper.saveContent", content);
		// 保存相关内容关系
		String[] relevantIdArr = content.getContentRelevantIdList();
		if (relevantIdArr != null && relevantIdArr.length > 0) {
			dao.save("ContentMapper.saveContentRelevant", content);
		}
		// 保存图片
		if (content.getImageList().size() > 0) {
			dao.save("ContentMapper.1saveImages", content);
		}
		// 保存视频
		if (content.getVideoList().size() > 0) {
			dao.save("ContentMapper.1savevideos", content);
		}
		// 保存栏目的中间表关系
		dao.save("ContentMapper.saveContentColumconfig", content);
		// 创建内容文本表数据
		dao.save("ContentMapper.saveContentTxt", content);

		// 保存seo
		PageData pd = new PageData();
		pd.put("SEO_KEYWORDS", content.getContentKeyWords());
		pd.put("MASTER_ID", content.getId());
		pd.put("ID", UuidUtil.get32UUID());
		pd.put("CREATED_TIME", DateUtil.getTime());
		pd.put("SEO_DESCRIPTION", content.getSeo_description());
		service.insetSeo(pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateContent(Content content, PageData pd) throws Exception {
		// 修改内容
		dao.update("ContentMapper.updaeContent", content);
		// 修改相关内容关系
		String[] relevantIdArr = content.getContentRelevantIdList();
		if (relevantIdArr != null && relevantIdArr.length > 0) {
			dao.delete("ContentMapper.delContentRelevants", new String[] { content.getId() });
			dao.save("ContentMapper.saveContentRelevant", content);
		} else {
			dao.delete("ContentMapper.delContentRelevants", new String[] { content.getId() });
		}
		// 修改封面图
		if ((Boolean) pd.get("flag")) {

			List<PageData> list = (List<PageData>) dao.findForList("ContentMapper.selectImagePachByContentId",
					new String[] { content.getId() });// 查询图片路径
			List<String> listID = new ArrayList<>();
			for (PageData p : list) {
				if (p != null) {
					String imageId = p.getString("IMAGE_ID");
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

			dao.save("ProductMapper.saveImage", pd);// 保存图片信息

		} else {

			if (content.getSurface_imageid() == null || "".equals(content.getSurface_imageid())) {
				List<PageData> list = (List<PageData>) dao.findForList("ContentMapper.selectImagePachByContentId",
						new String[] { content.getId() });// 查询图片路径
				List<String> listID = new ArrayList<>();
				for (PageData p : list) {
					if (p != null) {
						String imageId = p.getString("IMAGE_ID");
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
			}
		}
		////////////////////////////////////////////////
		// 操作图片-删除
		if (content.getImageids() != null && content.getImageids().size() > 0) {
			List<PageData> list = (List<PageData>) dao.findForList("ContentMapper.selectImagePach",
					new String[] { content.getId() });// 查询图片路径
			List<String> listID = new ArrayList<>();
			for (PageData p : list) {
				if (p != null) {
					String imageId = p.getString("IMAGE_ID");
					if (content.getImageids().contains(imageId)) {
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
					new String[] { content.getId() });// 查询图片路径
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
		List<Image> inm = content.getTimageList();
		if (inm != null && inm.size() > 0) {
			for (Image image : inm) {
				// 没有传图片,但有其他信息的情况
				if (image.getImageId() == null || "".equals(image.getImageId())) {
					image.setImageId(UuidUtil.get32UUID());
					image.setMaster_id(content.getId());
				}
			}
			dao.save("ContentMapper.saveImages", content);
		}
		// 操作图片-修改或保存
		List<Image> i = content.getImageList();
		if (i != null && i.size() > 0) {
			for (Image image : i) {
				// 只点击删除图片
				if (!content.getImageids().contains(image.getImageId())) {
					image.setMaster_id(content.getId());
					image.setImgurl(null);
					dao.save("ContentMapper.saveImage", image);
					continue;
				}
				if (image != null) {
					dao.update("ContentMapper.updateImageById", image);
				}
			}

		}
		////////////////////////////////
		// 操作视频-删除视频
		if (content.getVideoids() != null && content.getVideoids().size() > 0) {
			List<PageData> fileList = (List<PageData>) dao.findForList("ContentMapper.selectVideoPach",
					new String[] { content.getId() });// 查询视频文件路径
			List<String> listID = new ArrayList<>();
			for (PageData d : fileList) {
				String vid = d.getString("ID");
				if (content.getVideoids().contains(vid)) {
					continue;
				}
				listID.add(vid);
				if (d != null) {
					String pach = d.getString("VIDEO_URL");
					if (pach != null && Tools.notEmpty(pach.trim())) {
						DelAllFile.deleteFile(PathUtil.getUploadPath() + Const.FILEPATHFILE + pach); // 删除视频文件
					}
				}
			}
			if (listID.size() > 0) {
				dao.delete("ContentMapper.deleteVideosByIds", listID.toArray());// 删除视频信息
			}

		} else {
			List<PageData> fileList = (List<PageData>) dao.findForList("ContentMapper.selectVideoPach",
					new String[] { content.getId() });// 查询视频文件路径
			List<String> listID = new ArrayList<>();
			for (PageData d : fileList) {
				String vid = d.getString("ID");
				listID.add(vid);
				if (d != null) {
					String pach = d.getString("VIDEO_URL");
					if (pach != null && Tools.notEmpty(pach.trim())) {
						DelAllFile.deleteFile(PathUtil.getUploadPath() + Const.FILEPATHFILE + pach); // 删除视频文件
					}
				}
			}
			if (listID.size() > 0) {
				dao.delete("ContentMapper.deleteVideosByIds", listID.toArray());// 删除视频信息
			}
		}
		// 操作视频-保存新添加的视频
		List<Video> vdo = content.getTvideoList();
		if (vdo.size() > 0) {
			for (Video video : vdo) {
				// 没有视频但有其它信息的情况
				if (video.getId() == null || "".equals(video.getId())) {
					video.setId(UuidUtil.get32UUID());
					video.setMaster_id(content.getId());
				}
			}
			dao.save("ContentMapper.savevideos", content);
		}
		// 操作视频-保存或修改视频
		List<Video> v = content.getVideoList();
		if (v != null && v.size() > 0) {
			for (Video video : v) {
				if (!content.getVideoids().contains(video.getId())) {
					// video.setId(UuidUtil.get32UUID());
					video.setMaster_id(content.getId());
					video.setVideo_url(null);
					dao.save("ContentMapper.savevideo", video);
					continue;
				}
				if (video != null) {
					dao.update("ContentMapper.updateVideoById", video);
				}
			}
		} /////////////////////////////////////////////////////////////////////////////////
			// 修改栏目中间表关系
		if (content.getColumconfigIds() != null && content.getColumconfigIds().length > 0) {
			dao.delete("ContentMapper.delContentColumconfigs", new String[] { content.getId() });// 删除栏目中间表关系
			dao.save("ContentMapper.saveContentColumconfig", content);// 批量添加栏目表关系
		}
		Integer num = (Integer) dao.findForObject("ContentMapper.selectTXTCount", content);
		if (num > 0) {
			// 修改文本数据
			dao.update("ContentMapper.editContentTxt", content);
		} else {
			if (content.getContentTxt() != null) {
				dao.save("ContentMapper.saveContentTxt", content);
			}
		}
		// 保存seo
		pd.put("SEO_KEYWORDS", content.getContentKeyWords());
		pd.put("MASTER_ID", content.getId());
		pd.put("SEO_DESCRIPTION", content.getSeo_description());
		service.updateSeo(pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> findAll(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("ContentMapper.selectAll", pd);
	}

	@Override
	public Content findContentById(String id) throws Exception {
		Content content = (Content) dao.findForObject("ContentMapper.selectById", id);
		if (content.getFiledJson() != null && !"".equals(content.getFiledJson())) {
			ObjectMapper objectMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			List<ExtendFiledUtil> fileds = objectMapper.readValue(content.getFiledJson(), List.class);
			content.setFileds(fileds);
		}
		return content;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deleteAll(String[] id) throws Exception {
		dao.delete("ContentMapper.deleteAll", id);// 删除内容
		dao.delete("ContentMapper.delContentRelevants", id);// 删除关联关系
		// 删除栏目的中间表关系
		dao.delete("ContentMapper.delContentColumconfigs", id);
		// 删除内容文本表数据
		dao.delete("fileResourceMapper.deleteTXT", id);// 删除富文本
		// 删除封面
		List<PageData> list1 = (List<PageData>) dao.findForList("ContentMapper.selectImagePachByContentId", id);// 查询图片路径
		List<String> listID = new ArrayList<>();
		for (PageData p : list1) {
			if (p != null) {
				String imageId = p.getString("IMAGE_ID");
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
		// 删除图片
		List<PageData> list = (List<PageData>) dao.findForList("ContentMapper.selectImagePach", id);// 查询图片路径

		for (PageData pd : list) {
			if (pd != null) {
				String pach = pd.getString("IMGURL");
				if (pach != null && Tools.notEmpty(pach.trim())) {
					DelAllFile.deleteFile(PathUtil.getUploadPath() + Const.FILEPATHIMG + pach); // 删除图片
				}
			}

		}
		if (list.size() > 0) {
			dao.delete("ContentMapper.deleteImages", id);// 删除图片信息
		}
		// 删除视频
		List<PageData> fileList = (List<PageData>) dao.findForList("ContentMapper.selectVideoPach", id);// 查询视频文件路径

		for (PageData pd : fileList) {
			if (pd != null) {
				String pach = pd.getString("VIDEO_URL");
				if (pach != null && Tools.notEmpty(pach.trim())) {
					DelAllFile.deleteFile(PathUtil.getUploadPath() + Const.FILEPATHFILE + pach); // 删除视频文件
				}
			}
		}
		if (fileList.size() > 0) {
			dao.delete("ContentMapper.deleteVideos", id);// 删除视频信息
		}
		PageData pd = new PageData();
		pd.put("MASTER_ID", id);
		service.deleteSeo(pd);
	}
	/* end */

	@SuppressWarnings("unchecked")
	@Override
	public void deleteByColumnIds(String[] ids) throws Exception {
		// 查询产品类型ids
		List<String> listId = (List<String>) dao.findForList("ContentMapper.selectIdsByColumnIds", ids);
		List<String> flagList = new ArrayList<>();
		if (listId.size() > 0) {
			List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) dao
					.findForList("ContentMapper.selectCountByIds", listId.toArray(new String[listId.size()]));
			for (HashMap<String, Object> hashMap : list) {
				if ((Long) hashMap.get("COUNT(*)") > 1) {
					listId.remove(hashMap.get("CONTENT_ID"));
					flagList.add((String) hashMap.get("CONTENT_ID"));
				}
			}
			// 删除产品类型
			if(listId.size()>0)
			deleteAll(listId.toArray(new String[listId.size()]));
		}
		dao.delete("ContentMapper.deleteByColumnIds", ids);
		if(flagList.size()>0){
			List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) dao
					.findForList("ContentMapper.selectCountByIds", flagList.toArray(new String[flagList.size()]));
			for (HashMap<String, Object> hashMap : list) {
				if ((Long) hashMap.get("COUNT(*)") > 1) {
					flagList.remove(hashMap.get("CONTENT_ID"));
				}
			}
			// 删除产品类型
			if(flagList.size()>0)
			deleteAll(flagList.toArray(new String[flagList.size()]));
		}
	}

	@Override
	public List<ColumConfig> findTopAndChildList(PageData pd) throws Exception {
		List<ColumConfig> allList = (List<ColumConfig>) dao.findForList("ContentMapper.findAllList", pd);
		List<ColumConfig> topList = (List<ColumConfig>) dao.findForList("ContentMapper.findTopList1", pd);
		if (CollectionUtils.isEmpty(allList) || CollectionUtils.isEmpty(topList)) {
			return null;
		}
		List<ColumConfig> resultList = new ArrayList<ColumConfig>();
		for (ColumConfig top : topList) {
			resultList.add(top);
			this.appendChild(top, allList);
		}

		return resultList;
	}

	private void appendChild(ColumConfig config, List<ColumConfig> allList) {
		for (int i = 0; i < allList.size(); i++) {
			ColumConfig po = allList.get(i);
			if (po.getParentid().equals(config.getId())) {
				if (CollectionUtils.isEmpty(config.getSubConfigList())) {
					config.setSubConfigList(new ArrayList<ColumConfig>());
				}
				po.setChildLevel(config.getChildLevel() + Const.INT_1);
				config.getSubConfigList().add(po);
				allList.remove(i);
				i--;
				this.appendChild(po, allList);
			}
		}
	}

	@Override
	public void updataRecommendAndTop(Content content) throws Exception {
		dao.update("ContentMapper.updataRecommendAndTopByIds", content);

	}

	@Override
	public List<Content> findContentByColumnIds(String id) throws Exception {
		PageData pd = new PageData();
		pd.put("id", id);
		pd.put("TOP", "1");
		pd.put("TIME", DateUtil.getYear());
		return (List<Content>) dao.findForList("ContentMapper.selectContentByColumnId", id);
	}

	@Override
	public List<PageData> findContentAllByAll(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return (List<PageData>) dao.findForList("ContentMapper.selectAllByAll", map);
	}

	@Override
	public List<Content> findContentsByColumnId(String id) throws Exception {
		return (List<Content>) dao.findForList("ContentMapper.findContentsByColumnId", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Content findContentsByColumnIdOne(String id) throws Exception {
		return (Content) dao.findForObject("ContentMapper.findContentsByColumnIdOne", id);
	}

	@Override
	public List<PageData> findTopList() throws Exception {
		return (List<PageData>) dao.findForList("ContentMapper.findTopList", null);
	}

	@Override
	public Content findTemplatePachById(String contentId) throws Exception {
		return (Content) dao.findForObject("ContentMapper.selectTemplatePachById", contentId);
	}

	@Override
	public List<PageData> findColum(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("ContentMapper.selectColum", pd);
	}

	@Override
	public Content findTxt(String id) throws Exception {
		return (Content) dao.findForObject("ContentMapper.findTxt", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> selectlistPageByColumID(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("ContentMapper.selectlistPageByColumID", page);

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Content> selectlistPageByColumIDD(Page page) throws Exception {
		List<Content> list = this.contentlistPage(page);
		if (list == null || list.size() == 0) {
			String colum_id = page.getPd().getString("colum_id");
			List<Map<String, String>> mapColumIds = (List<Map<String, String>>) dao
					.findForList("ContentMapper.selectallcolum_ids", page);
			List<String> newCloumIds = new ArrayList<>();
			newCloumIds.add(colum_id);
			for (Map<String, String> map : mapColumIds) {
				if (newCloumIds.contains(map.get("PID"))) {
					newCloumIds.add(map.get("ID"));
				}
			}

			page.getPd().put("list", newCloumIds);
			List<String> ids = (List<String>) dao.findForList("ContentMapper.selectByColumIDDlistPage", page);
			if (ids == null || ids.size() == 0) {
				return null;
			}
			page.getPd().put("ids", ids);
			list = (List<Content>) dao.findForList("ContentMapper.selectTest", page.getPd());
		}
		return list;
	}

	@Override
	public List<ContentInfoBo> findContentInfoBoById(String id) throws Exception {
		return (List<ContentInfoBo>) dao.findForList("ContentMapper.findContentInfoBoById", id);
	}

	@Override
	public List<Content> contentFrontlistPage(Page page) throws Exception {
		// TODO Auto-generated method stub
		return (List<Content>) dao.findForList("ContentMapper.selectlistPage", page);
	}

	@Override
	public List<Content> contentCompanyProfile(Page page) throws Exception {
		List<String> ids = (List<String>) dao.findForList("ContentMapper.selectTestlistPage", page);
		if (ids == null || ids.size() == 0) {
			return null;
		}
		PageData pd = page.getPd();
		pd.put("ids", ids);
		List<Content> contentList = (List<Content>) dao.findForList("ContentMapper.selectCompanyProfile", pd);
		ObjectMapper objectMapper = new ObjectMapper();
		for (Content content : contentList) {
			if (content!=null && content.getFiledJson() != null && !"".equals(content.getFiledJson())) {
				@SuppressWarnings("unchecked")
				List<ExtendFiledUtil> fileds = objectMapper.readValue(content.getFiledJson(), List.class);
				content.setFileds(fileds);
			}
		}
		return contentList;
	}
	
	/**
	 * 根据栏目id查询内容，内容按创建时间排序
	 */
	public List<Content> ContentListOrderByCreateTime(Page page) throws Exception {
		List<String> ids = (List<String>) dao.findForList("ContentMapper.selectTestlistPage", page);
		if (ids == null || ids.size() == 0) {
			return null;
		}
		PageData pd = page.getPd();
		pd.put("ids", ids);
		List<Content> contentList = (List<Content>) dao.findForList("ContentMapper.selectContentListOrderByCreateTime", pd);
		ObjectMapper objectMapper = new ObjectMapper();
		for (Content content : contentList) {
			if (content!=null && content.getFiledJson() != null && !"".equals(content.getFiledJson())) {
				@SuppressWarnings("unchecked")
				List<ExtendFiledUtil> fileds = objectMapper.readValue(content.getFiledJson(), List.class);
				content.setFileds(fileds);
			}
		}
		return contentList;
	}

	@Override
	public void updatePv(String contentId) throws Exception {
		dao.update("ContentMapper.updatePv", contentId);
		
	}

	@Override
	public List<Content> findContent(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (List<Content>) dao.findForList("ContentMapper.selectContent", pd);
	}
	/**
	 * 江铜联系我们
	 */
	@Override
	public List<Content> contentContact(PageData pd) throws Exception {
		List<Content> contentlist = (List<Content>) dao.findForList("ContentMapper.selectUsProfile", pd);
		ObjectMapper objectMapper = new ObjectMapper();
		for (Content content : contentlist) {
			if (content!=null && content.getFiledJson() != null && !"".equals(content.getFiledJson())) {
				@SuppressWarnings("unchecked")
				List<ExtendFiledUtil> fileds = objectMapper.readValue(content.getFiledJson(), List.class);
				content.setFileds(fileds);
			}
		}
		return contentlist;
	}
	
}
