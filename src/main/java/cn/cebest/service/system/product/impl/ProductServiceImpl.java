package cn.cebest.service.system.product.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Video;
import cn.cebest.entity.system.product.Brand;
import cn.cebest.entity.system.product.Product;
import cn.cebest.service.system.product.ProductService;
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

@Service("productService")
public class ProductServiceImpl implements ProductService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Resource(name = "seoService")
	private SeoService service;

	/**
	 * 
	 * <p>Title: findProductTypeList</p>   
	 * <p>Description: 查询产品分类列表 </p>   
	 * @param page
	 * @return
	 * @throws Exception   
	 */
	@Override
	public List<PageData> findProductTypeList(String columnId) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("ProductMapper.findProductTypeList",columnId);
		return list;
	}
	
	@Override
	public List<PageData> getProductByIdList(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("ProductMapper.getProductByIdlistpage", page.getPd());
		return list;
	}
	
	@Override
	public List<PageData> findProducts(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("ProductMapper.findProductslistPage", page);
		return list;
	}
	
	/**
	 * <p>Title: findProducts</p>   
	 * <p>Description: 根据条件查找商品 </p>   
	 * @param pd
	 * @return
	 * @throws Exception   
	 * @see cn.cebest.service.system.product.ProductService#findProducts(cn.cebest.util.PageData)
	 */
	@Override
	public List<PageData> findProducts(PageData pd) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("ProductMapper.findProducts", pd);// 查询图片路径
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void deleteProduct(String[] ids) throws Exception {
		dao.delete("ProductMapper.deleteProductRelevantList", ids);// 删除关联关系
		dao.delete("ProductMapper.deleteProductBrand", ids);// 删除关联关系
		List<PageData> list = (List<PageData>) dao.findForList("ProductMapper.selectImagePachByProductId", ids);// 查询图片路径
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
		// 删除图片
		List<PageData> list1 = (List<PageData>) dao.findForList("ContentMapper.selectImagePach", ids);// 查询图片路径

		for (PageData pd : list1) {
			if (pd != null) {
				String pach = pd.getString("IMGURL");
				if (pach != null && Tools.notEmpty(pach.trim())) {
					DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + pach); // 删除图片
				}
			}

		}
		if (list1.size() > 0) {
			dao.delete("ContentMapper.deleteImages", ids);// 删除图片信息
		}
		if (listID.size() > 0) {
			dao.delete("ProductMapper.deleteProductImage", listID.toArray());// 删除图片信息
		}
		// 删除视频
		List<PageData> fileList = (List<PageData>) dao.findForList("ContentMapper.selectVideoPach", ids);// 查询视频文件路径

		for (PageData pd : fileList) {
			if (pd != null) {
				String pach = pd.getString("VIDEO_URL");
				if (pach != null && Tools.notEmpty(pach.trim())) {
					DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + pach); // 删除视频文件
				}
			}
		}
		if (fileList.size() > 0) {
			dao.delete("ContentMapper.deleteVideos", ids);// 删除视频信息
		}
		dao.delete("ProductMapper.deleteType_product_relation", ids);// 删除产品和产品关系
		
		//删除资讯地址栏的url信息
		dao.delete("ProductMapper.delProductUrlName", ids);
		
		dao.delete("ProductMapper.deleteProductTxt", ids);// 删除富文本
		dao.delete("ProductMapper.deleteProduct", ids);// 删除产品
		dao.delete("ProductMapper.deleteProductF", ids);
		PageData pd = new PageData();
		pd.put("MASTER_ID", ids);
		service.deleteSeo(pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> findProductRelevantBycode(String id) throws Exception {

		return (List<PageData>) dao.findForList("ProductMapper.selectProductRelevantBycode", id);
	}

	@Override
	public Product findProductById(String id) throws Exception {
		Product   product= (Product) dao.findForObject("ProductMapper.selectProductById", id);
		ObjectMapper objectMapper = new ObjectMapper();
		if (product.getFiledJson() != null && !"".equals(product.getFiledJson())) {
			@SuppressWarnings("unchecked")
			List<ExtendFiledUtil> fileds = objectMapper.readValue(product.getFiledJson(), List.class);
			product.setFileds(fileds);
		}
		return product;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateProduct(Product product, PageData pd) throws Exception {
		// 修改封面图
		if ((Boolean) pd.get("flag")) {
			List<PageData> list = (List<PageData>) dao.findForList("ProductMapper.selectImagePachByProductId",
					new String[] { product.getId() });// 查询图片路径
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

			if (product.getImgeId() == null || "".equals(product.getImgeId())) {
				List<PageData> list = (List<PageData>) dao.findForList("ProductMapper.selectImagePachByProductId",
						new String[] { product.getId() });// 查询图片路径
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
		////////////////////////////////////////////
		// 操作图片-删除
		if (product.getImageids() != null && product.getImageids().size() > 0) {
			List<PageData> list = (List<PageData>) dao.findForList("ContentMapper.selectImagePach",
					new String[] { product.getId() });// 查询图片路径
			List<String> listID = new ArrayList<>();
			for (PageData p : list) {
				if (p != null) {
					String imageId = p.getString("IMAGE_ID");
					if (product.getImageids().contains(imageId)) {
						continue;
					}
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
		} else {
			List<PageData> list = (List<PageData>) dao.findForList("ContentMapper.selectImagePach",
					new String[] { product.getId() });// 查询图片路径
			List<String> listID = new ArrayList<>();
			for (PageData p : list) {
				String imageId = p.getString("IMAGE_ID");
				listID.add(imageId);
				if (p != null) {
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
		// 操作图片-保存新添加的
		List<Image> inm = product.getTimageList();
		if (inm != null && inm.size() > 0) {
			for (Image image : inm) {
				// 没有传图片,但有其他信息的情况
				if (image.getImageId() == null || "".equals(image.getImageId())) {
					image.setImageId(UuidUtil.get32UUID());
					image.setMaster_id(product.getId());
				}
			}
			dao.save("ProductMapper.saveImages", product);
		}
		// 操作图片-修改或保存
		List<Image> i = product.getImageList();
		if (i != null && i.size() > 0) {
			for (Image image : i) {
				// 只点击删除图片
				if (!product.getImageids().contains(image.getImageId())) {
					image.setMaster_id(product.getId());
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
		// 操作视频-删除视频
		if (product.getVideoids() != null && product.getVideoids().size() > 0) {
			List<PageData> fileList = (List<PageData>) dao.findForList("ContentMapper.selectVideoPach",
					new String[] { product.getId() });// 查询视频文件路径
			List<String> listID = new ArrayList<>();
			for (PageData d : fileList) {
				String vid = d.getString("ID");
				if (product.getVideoids().contains(vid)) {
					continue;
				}
				listID.add(vid);
				if (d != null) {
					String pach = d.getString("VIDEO_URL");
					if (pach != null && Tools.notEmpty(pach.trim())) {
						DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + pach); // 删除视频文件
					}
				}
			}
			if (listID.size() > 0) {
				dao.delete("ContentMapper.deleteVideosByIds", listID.toArray());// 删除视频信息
			}

		} else {
			List<PageData> fileList = (List<PageData>) dao.findForList("ContentMapper.selectVideoPach",
					new String[] { product.getId() });// 查询视频文件路径
			List<String> listID = new ArrayList<>();
			for (PageData d : fileList) {
				String vid = d.getString("ID");
				listID.add(vid);
				if (d != null) {
					String pach = d.getString("VIDEO_URL");
					if (pach != null && Tools.notEmpty(pach.trim())) {
						DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + pach); // 删除视频文件
					}
				}
			}
			if (listID.size() > 0) {
				dao.delete("ContentMapper.deleteVideosByIds", listID.toArray());// 删除视频信息
			}
		}
		// 操作视频-保存新添加的视频
		List<Video> vdo = product.getTvideoList();
		if (vdo.size() > 0) {
			for (Video video : vdo) {
				// 没有视频但有其它信息的情况
				if (video.getId() == null || "".equals(video.getId())) {
					video.setId(UuidUtil.get32UUID());
					video.setMaster_id(product.getId());
				}
			}
			dao.save("ProductMapper.savevideos", product);
		}
		// 操作视频-保存或修改视频
		List<Video> v = product.getVideoList();
		if (v != null && v.size() > 0) {
			for (Video video : v) {
				if (!product.getVideoids().contains(video.getId())) {
					// video.setId(UuidUtil.get32UUID());
					video.setMaster_id(product.getId());
					video.setVideo_url(null);
					dao.save("ContentMapper.savevideo", video);
					continue;
				}
				if (video != null) {
					dao.update("ContentMapper.updateVideoById", video);
				}
			}
		}
		// 重新建立产品类型和产品关联关系
		if ((boolean) pd.get("is_ColumnOrYype")) {
			// 删除产品类型和产品关联关系
			dao.delete("ProductMapper.deleteType_product_relation", new String[] { product.getId() });
			dao.save("ProductMapper.insertType_product_relation", product);
		} else {
			// 删除产品类型和产品关联关系
			dao.delete("ProductMapper.deleteType_product_relation", new String[] { product.getId() });
		}
		
		//修改产品地址栏的url信息
		/*Integer contentUrlNameSize = (Integer)dao.findForObject("ProductMapper.findContentUrlNameList", product);
		if(contentUrlNameSize > 0){
			dao.update("ProductMapper.editContentUrlNameconfig", product);
		}else{
			dao.save("ProductMapper.insertTypeProductUrlNameColumRelation", product);
		}*/
		
		//添加产品地址栏的url信息
		List<PageData> urlNameList = (List<PageData>) dao.findForList("ProductMapper.selectProductUrlName", product.getId());
		for (PageData pageData : urlNameList) {
			//添加产品地址栏的url信息
			dao.save("ProductMapper.insertProductUrlNameColum", pageData);
		}
		
		// 重新建分组和产品关联关系
		if (product.getBrandIds() != null && product.getBrandIds().length > 0) {
			dao.delete("ProductMapper.deleteProductBrand", new String[] { product.getId() });
			dao.save("ProductMapper.saveProductBrand", product);
		} else {
			dao.delete("ProductMapper.deleteProductBrand", new String[] { product.getId() });
		}

		if (product.getFlag().equals("1") &&product.getFproductids()!=null&& product.getFproductids().length > 0) {
			dao.delete("ProductMapper.deleteProductF", new String[] { product.getId() });
			dao.save("ProductMapper.saveProductF", product);
		} else if (product.getFlag().equals("1") &&(product.getFproductids()==null || product.getFproductids().length == 0)) {
			dao.delete("ProductMapper.deleteProductF", new String[] { product.getId() });
		}

		// 删除关联关系
		// 重新建立关联关系
		String[] relevantIdArr = product.getProductRelevantIdList();
		if (relevantIdArr != null && relevantIdArr.length > 0) {
			dao.delete("ProductMapper.deleteProductRelevantList", new String[] { product.getId() });// 删除关联关系
			dao.save("ProductMapper.saveProductRelevant", product);
		} else {
			dao.delete("ProductMapper.deleteProductRelevantList", new String[] { product.getId() });// 删除关联关系
		}

		// 修改文本表数据

		dao.update("ProductMapper.upateProductTxt", product);
		// 修改产品
		dao.update("ProductMapper.updateProduct", product);
		// 保存seo
		pd.put("SEO_TITLE", product.getSeo_title());
		pd.put("SEO_KEYWORDS", product.getProduct_KeyWords());
		pd.put("MASTER_ID", product.getId());
		pd.put("SEO_DESCRIPTION", product.getSeo_description());
		service.updateSeo(pd);
	}

	// ========================================================
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> findProduct_TypeAll(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("ProductMapper.selectProductType", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void findProductToList(Map<String, Object> map, Page page) throws Exception {
		map.put("product_TypeList", dao.findForList("ProductMapper.selectProductType", page.getPd()));
		map.put("productColumList", dao.findForList("ColumconfigMapper.selectColumByProductType", page.getPd()));
		List<String> ids = (List<String>) dao.findForList("ProductMapper.selectTestlistPage", page);
		if (ids == null || ids.size() == 0) {
			map.put("productList", null);
			return;
		}
		PageData pd = page.getPd();
		pd.put("ids", ids);

		map.put("productList", dao.findForList("ProductMapper.selectTest", pd));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Brand> findBrandAll(PageData pd) throws Exception {
		return (List<Brand>) dao.findForList("ProductMapper.selectBrandAll", pd);
	}

	@Override
	public void save(Product product, PageData pd) throws Exception {
		// 保存产品
		dao.save("ProductMapper.saveProduct", product);
		if (product.getFlag().equals("1")&&product.getFproductids()!=null) {
			dao.save("ProductMapper.saveProductF", product);
		}
		// 保存产品与分组关系
		if (product.getBrandIds() != null && product.getBrandIds().length > 0) {
			dao.save("ProductMapper.saveProductBrand", product);
		}
		// 保存产品类型关联关系
		if ((boolean) pd.get("is_ColumnOrYype")) {
			dao.save("ProductMapper.insertType_product_relation", product);
			
			//添加产品地址栏的url信息
			dao.save("ProductMapper.insertTypeProductUrlNameColumRelation", product);
		}
		// 保存产品关联关系
		String[] relevantIdArr = product.getProductRelevantIdList();
		if (relevantIdArr != null && relevantIdArr.length > 0) {
			dao.save("ProductMapper.saveProductRelevant", product);
		}
		// 保存封面图片
		if ((Boolean) pd.get("flag")) {
			dao.save("ProductMapper.saveImage", pd);
		}
		// 保存图片
		if (product.getTimageList().size() > 0) {
			dao.save("ProductMapper.saveImages", product);
		}
		// 保存视频
		if (product.getVideoList().size() > 0) {
			dao.save("ProductMapper.1savevideos", product);
		}
		// 创建文本表数据
		dao.save("ProductMapper.saveProductTxt", product);
		// 保存seo
		pd.put("SEO_TITLE", product.getSeo_title());
		pd.put("SEO_KEYWORDS", product.getProduct_KeyWords());
		pd.put("MASTER_ID", product.getId());
		pd.put("ID", UuidUtil.get32UUID());
		pd.put("CREATED_TIME", DateUtil.getTime());
		pd.put("SEO_DESCRIPTION", product.getSeo_description());
		service.insetSeo(pd);

	}

	@Override
	public Map<String,Object> toAddFind(Map<String, Object> map, PageData pd) throws Exception {
		// 1 查模板方作用域
		map.put("templateList", dao.findForList("ProductMapper.selectProductTemplate", pd));
		// 2查品牌方作用域
		map.put("brandList", dao.findForList("ProductMapper.selectBrandAll", pd));
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> findPropertyById(String id) throws Exception {

		return (List<PageData>) dao.findForList("ProductMapper.selectProductValueTypeList", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> findProductBytypeId(String[] id) throws Exception {

		return (List<Product>) dao.findForList("ProductMapper.selectProductBytypeId", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> findProductRelevant(String id) throws Exception {
		return (List<Product>) dao.findForList("ProductMapper.selectProductRelevant", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> findProductTypeById(String[] id) throws Exception {
		return (List<Product>) dao.findForList("ProductMapper.selectProductTypeById", id);
	}

	@Override
	public void updataStatus(Map<String, Object> map) throws Exception {
		dao.update("ProductMapper.updateProductStatusByIds", map);

	}

	@Override
	public void updataRecommendAndTop(Product product) throws Exception {
		dao.update("ProductMapper.updataRecommendAndTopByIds", product);

	}

	@Override
	public void sortlistPage(Map<String, Object> map, Page page) throws Exception {
		map.put("product_TypeList", dao.findForList("ProductMapper.selectProductType", page.getPd()));
		map.put("productList", dao.findForList("ProductMapper.findProductSortlistPage", page));

	}

	@Override
	public List<Product> downloadsQueryList(PageData pd) throws Exception {
		return (List<Product>) dao.findForList("ProductMapper.selectProduct", pd);
	}

	@Override
	public void save(List<Product> productList) throws Exception {
		//
		dao.save("ProductMapper.batchInsertProduct", productList);
		// 保存产品类型关联关系
		for (Product product : productList) {
			dao.save("ProductMapper.insertType_product_relation2", product);
		}

	}

	@Override
	public List<PageData> findProductTemplate(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("ProductMapper.selectProductTemplate", pd);
	}

	@Override
	public void findNoCount(Map<String, Object> map) throws Exception {
		map.put("count", dao.findForObject("ProductMapper.selectNoCount", map));

	}

	@Override
	public List<Product> findProductByColumnId(String id) throws Exception {
		List<String> ids = (List<String>) dao.findForList("ProductMapper.selectTypeId", id);
		PageData pd = new PageData();
		pd.put("ids", ids);
		pd.put("TOP", "1");
		pd.put("TIME", DateUtil.getYear());
		return (List<Product>) dao.findForList("ProductMapper.selectProductBytypeIds", pd);
	}

	@Override
	public List<Product> findProductAllByAll(Map<String, Object> map) throws Exception {
		return (List<Product>) dao.findForList("ProductMapper.selectAllByAll", map);
	}

	@Override
	public List<PageData> findTopList() throws Exception {
		return (List<PageData>) dao.findForList("ProductMapper.findTopList", null);
	}

	@Override
	public PageData findTemplatePachById(String contentId) throws Exception {

		return (PageData) dao.findForObject("ProductMapper.selectTemplatePachById", contentId);
	}

	@Override
	public List<Product> queryProductByColumnId(Page page) throws Exception {
		List<Product> productlist = (List<Product>) dao.findForList("ProductMapper.selectProductByColumnIdlistPage",
				page);
		ObjectMapper objectMapper = new ObjectMapper();
		for (Product product : productlist) {
			if (product.getFiledJson() != null && !"".equals(product.getFiledJson())) {
				@SuppressWarnings("unchecked")
				List<ExtendFiledUtil> fileds = objectMapper.readValue(product.getFiledJson(), List.class);
				product.setFileds(fileds);
			}
		}
		return productlist;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> findRecommendProductByColumnId(String id) throws Exception {
		return (List<Product>) dao.findForList("ProductMapper.findRecommendProductByColumnId", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> findProductColumnIdExcludeByTypeId(PageData pd) throws Exception {
		return (List<Product>) dao.findForList("ProductMapper.findProductColumnIdExcludeByTypeId", pd);
	}

	@Override
	public void deleteByColumnIds(String[] ids) throws Exception {
		// 查询产品类型ids
		List<String> listId = (List<String>) dao.findForList("ProductMapper.selectIdsByColumnIds", ids);
		if (listId.size() > 0) {
			List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) dao
					.findForList("ProductMapper.selectCountByIds", listId.toArray(new String[listId.size()]));
			for (HashMap<String, Object> hashMap : list) {
				if ((Long) hashMap.get("COUNT(*)") > 1) {
					listId.remove(hashMap.get("PRODUCTID"));
				}
			}
			// 删除产品类型
			deleteProduct(listId.toArray(new String[listId.size()]));
		}
		dao.delete("productTypeMapper.deleteByColumnIds", ids);

	}

	@Override
	public List<Product> queryProductAreaByColumnId(Page page) throws Exception {
		List<Product> productlist = (List<Product>) dao.findForList("ProductMapper.selectProductAreaByColumnIdlistPage",
				page);
		return productlist;
	}

	@Override
	public List<String> findProductbrands(String id) throws Exception {
		return (List<String>) dao.findForList("ProductMapper.selectBrands", id);
	}

	@Override
	public List<PageData> findProductfByIds(String[] id) throws Exception {
		return (List<PageData>) dao.findForList("ProductMapper.selectFproduct", id);
	}

	@Override
	public List<PageData> findProductfproductids(String id) throws Exception {
		return (List<PageData>) dao.findForList("ProductMapper.selectFproductByprid", id);
	}
	
	@Override
	public Product findProducBiaoTiById(String productId) throws Exception {
		return (Product) dao.findForObject("ProductMapper.selectBiaoTiById", productId);
	}
	
	@Override
	public Product findProducTeDianById(String productId) throws Exception {
		return (Product) dao.findForObject("ProductMapper.selectTeDianById", productId);
	}
	
	@Override
	public Product findProducLiangDianById(String productId) throws Exception {
		// TODO Auto-generated method stub
		return (Product) dao.findForObject("ProductMapper.selectLiangDianById", productId);
	}

	@Override
	public List<Product> findProductGuiGeById(String productId) throws Exception {
		List<Product> productlist = (List<Product>) dao.findForList("ProductMapper.selectProductGuiGeById", productId);
		ObjectMapper objectMapper = new ObjectMapper();
		for (Product product : productlist) {
			if (product.getFiledJson() != null && !"".equals(product.getFiledJson())) {
				@SuppressWarnings("unchecked")
				List<ExtendFiledUtil> fileds =(List<ExtendFiledUtil>) objectMapper.readValue(product.getFiledJson(), List.class);
				product.setFileds(fileds);
			}
		}
		return productlist;
	}

	@Override
	public List<Brand> findProductJieJueById(String productId) throws Exception {
		// TODO Auto-generated method stub
		return (List<Brand>) dao.findForList("ProductMapper.selectProductJieJueById", productId);
	}

	@Override
	public Product findProducPieJianById(String productId) throws Exception {
		// TODO Auto-generated method stub
		return (Product) dao.findForObject("ProductMapper.selectProducPieJianById", productId);
	}

	@Override
	public Product findPeiTaoFuWuById(String productId) throws Exception {
		 Product  product = (Product) dao.findForObject("ProductMapper.selectProductPeiTaoFuWuById", productId);
		 	ObjectMapper objectMapper = new ObjectMapper();
		 	if(product!=null){
		 		if (product.getFiledJson() != null && !"".equals(product.getFiledJson())) {
		 			@SuppressWarnings("unchecked")
		 			List<ExtendFiledUtil> fileds =(List<ExtendFiledUtil>) objectMapper.readValue(product.getFiledJson(), List.class);
		 			product.setFileds(fileds);
		 		}
		 	}
		 return product;
	}

	@Override
	public Product findProducLiuChengById(String productId) throws Exception {
		return (Product) dao.findForObject("ProductMapper.selectProducLiuChengById", productId);
	}
	
	@Override
	public void updataRecommendAndTopAndHot(Product product) throws Exception {
		dao.update("ProductMapper.updataRecommendAndTopAndHotByIds", product);

	}
	
	@Override
	public void updateSort(PageData pd) throws Exception {
		dao.update("ProductMapper.updateSort", pd);
	}

	@Override
	public List<Product> selectProductByAllColumdlistPage(Page page) throws Exception {
		List<Product> productlist = (List<Product>) dao.findForList("ProductMapper.selectProductByAllColumdlistPage",
				page);
		ObjectMapper objectMapper = new ObjectMapper();
		for (Product product : productlist) {
			if (product.getFiledJson() != null && !"".equals(product.getFiledJson())) {
				@SuppressWarnings("unchecked")
				List<ExtendFiledUtil> fileds = objectMapper.readValue(product.getFiledJson(), List.class);
				product.setFileds(fileds);
			}
		}
		return productlist;
	}


	/**
	 * 根据该分类名称获取分类id
	 * @throws Exception 
	 */
	@Override
	public Product findProductByname(String typeName) throws Exception {
		
		return (Product) dao.findForObject("ProductMapper.findTypeIdByName", typeName);
	}

	@Override
	public void saveImportData(Product product) throws Exception {
		dao.save("ProductMapper.saveProduct", product);
		dao.save("ProductMapper.saveProductTxt", product);
	}

	@Override
	public void insertTypeRealtion(PageData pd) throws Exception {
		dao.save("ProductMapper.insertTypeRealtion", pd);

	}
	@Override
	public Product findProductByTypeOrColumnid(PageData pd) throws Exception {
		return (Product) dao.findForObject("ProductMapper.findProductByTypeOrColumnid", pd);
	}


}
