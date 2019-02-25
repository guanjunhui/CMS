package cn.cebest.service.system.product.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.system.product.Product_Type;
import cn.cebest.service.system.product.ProductService;
import cn.cebest.service.system.product.ProductTypeService;
import cn.cebest.service.system.seo.SeoService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.DelAllFile;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.Tools;
import cn.cebest.util.UuidUtil;

/**
 * 商品类型service接口实现类
 * 
 * @author wzd
 *
 */
@Service("productTypeService")
public class ProductTypeServiceImpl implements ProductTypeService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "seoService")
	private SeoService service;

	@Override
	public void findProductTypeToList(Map<String, Object> map, PageData pd) throws Exception {
		// 查询所有类型
		map.put("productTypeList", dao.findForList("productTypeMapper.selectproductTypeListPage", pd));

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product_Type> getTreeData(PageData pd) throws Exception {
		List<Product_Type> topList = (List<Product_Type>) dao.findForList("productTypeMapper.selectTop", pd);
		List<Product_Type> allList = (List<Product_Type>) dao.findForList("productTypeMapper.selectAll", pd);

		if (CollectionUtils.isEmpty(allList) || CollectionUtils.isEmpty(topList)) {
			return null;
		}
		for (Product_Type top : topList) {
			this.appendChild(top, allList);
		}
		return topList;
	}

	private void appendChild(Product_Type root, List<Product_Type> allList) {
		for (int i = 0; i < allList.size(); i++) {
			Product_Type pt = allList.get(i);
			if (pt != null && pt.getPid().equals(root.getId())) {
				if (CollectionUtils.isEmpty(root.getChildList())) {
					root.setChildList(new ArrayList<Product_Type>());
				}
				root.getChildList().add(pt);
				this.appendChild(pt, allList);
			}
		}
	}

	@Override
	public void save(Product_Type product, PageData pd) throws Exception {
		// 保存产品
		dao.save("productTypeMapper.insertProductType", product);
		// 保存图片
		if ((Boolean) pd.get("flag")) {
			dao.save("ProductMapper.saveImage", pd);
		}
		// 保存富文本
		dao.save("productTypeMapper.insertTXT", product);
		// 保存栏目关系
		if (product.getColumnids() != null && product.getColumnids().length > 0) {
			dao.save("productTypeMapper.insertProducttype_column", product);
			dao.save("productTypeMapper.saveContentTypeUrlNameconfig", product);
		}
		// 保存seo
		pd.put("SEO_KEYWORDS", product.getType_keywords());
		pd.put("MASTER_ID", product.getId());
		pd.put("ID", UuidUtil.get32UUID());
		pd.put("CREATED_TIME", DateUtil.getTime());
		pd.put("SEO_DESCRIPTION", product.getSeo_description());
		service.insetSeo(pd);
	}

	@Override
	public Map<String, Object> findProductTypeToEdit(Map<String, Object> map, String id) throws Exception {
		map.put("productType", dao.findForObject("productTypeMapper.selectProductTypeById", id));
		PageData pd = new PageData();
		pd.put("MASTER_ID", id);
		map.put("seo", service.querySeoForObject(pd));
		return map;
	}

	@Override
	public void update(Product_Type product, PageData pd) throws Exception {
		if ((Boolean) pd.get("flag")) {

			List<PageData> list = (List<PageData>) dao.findForList("productTypeMapper.selectImagePach",
					new String[] { product.getId() });// 查询图片路径
			List<String> listID = new ArrayList<>();
			for (PageData p : list) {
				if (pd != null) {
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

			if (product.getImageid() == null || "".equals(product.getImageid())) {
				List<PageData> list = (List<PageData>) dao.findForList("productTypeMapper.selectImagePach",
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
		// 删除关联关系
		// 保存栏目关系
		// 重新建立关联关系
		if (product.getColumnids() != null && product.getColumnids().length > 0) {
			dao.delete("productTypeMapper.deleteProducttype_column", new String[] { product.getId() });
			dao.save("productTypeMapper.insertProducttype_column", product);
		}
		//修改栏目地址栏的url信息
		Integer columUrlNameSize = (Integer)dao.findForObject("productTypeMapper.findContentTypeUrlNameList", product);
		if(columUrlNameSize > 0){
			dao.update("productTypeMapper.editContentTypeUrlNameconfig", product);
		}else{
			dao.save("productTypeMapper.saveContentTypeUrlNameconfig", product);
		}
		dao.update("productTypeMapper.updateTXT", product);// 修改富文本
		
		dao.update("productTypeMapper.updateById", product);
		// 保存seo
		pd.put("SEO_KEYWORDS", product.getType_keywords());
		pd.put("MASTER_ID", product.getId());
		pd.put("SEO_DESCRIPTION", product.getSeo_description());
		service.updateSeo(pd);
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
	public void delete(String[] ids) throws Exception {
		List<String> newIdlist = new ArrayList(Arrays.asList(ids));
		PageData data = new PageData();
		List<Map<String, String>> listAll = (List<Map<String, String>>) dao
				.findForList("productTypeMapper.selectAllIdPiD", data);
		for (int i = 0; i < newIdlist.size(); i++) {
			appendChildIds(newIdlist, newIdlist.get(i), listAll);
		}
		// 删除关联关系
		dao.delete("productTypeMapper.deleteProducttype_column", newIdlist.toArray(new String[newIdlist.size()]));
		
		// 删除产品分类的地址栏URL信息
		dao.delete("productTypeMapper.deleteProducttypeUrlName", newIdlist.toArray(new String[newIdlist.size()]));
		// 删除产品内容的地址栏URL信息
		dao.delete("productTypeMapper.deleteProductypeContentUrlName", newIdlist.toArray(new String[newIdlist.size()]));
		
		List<PageData> list = (List<PageData>) dao.findForList("productTypeMapper.selectImagePach",
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
		List<String> listid = (List<String>) dao.findForList("productTypeMapper.selectProductIds",
				newIdlist.toArray(new String[newIdlist.size()]));// 根据产品类型id查询产品id

		if (listid.size() > 0) {
			productService.deleteProduct(listid.toArray(new String[listid.size()]));
		}
		dao.delete("productTypeMapper.deleteTXT", newIdlist.toArray(new String[newIdlist.size()]));// 删除富文本
		dao.delete("productTypeMapper.deleteByIds", newIdlist.toArray(new String[newIdlist.size()]));
		PageData pd = new PageData();
		pd.put("MASTER_ID", ids);
		service.deleteSeo(pd);

	}

	public void deleteByColumnIds(String[] ids) throws Exception {
		// 查询产品类型ids
		List<String> listId = (List<String>) dao.findForList("productTypeMapper.selectIdsByColumnIds", ids);
		if (listId.size() > 0) {
			List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) dao
					.findForList("productTypeMapper.selectCountByIds", listId.toArray(new String[listId.size()]));
			for (HashMap<String, Object> hashMap : list) {
				if ((Long) hashMap.get("COUNT(*)") > 1) {
					listId.remove(hashMap.get("PRDUCTTYPEID"));
				}
			}
			// 删除产品类型
			if (listId.size() > 0) {
				delete(listId.toArray(new String[listId.size()]));
			}
		}
		dao.delete("productTypeMapper.deleteByColumnIds", ids);
	}

	@Override
	public void updataStatus(Map<String, Object> map) throws Exception {
		dao.update("productTypeMapper.updateProductStatusByIds", map);
		dao.update("productTypeMapper.updateStatusByIds", map);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product_Type> getlistTreeData(PageData pd) throws Exception {
		List<Product_Type> topList = (List<Product_Type>) dao.findForList("productTypeMapper.selectListTop", pd);
		List<Product_Type> allList = (List<Product_Type>) dao.findForList("productTypeMapper.selectListAll", pd);

		if (CollectionUtils.isEmpty(allList) || CollectionUtils.isEmpty(topList)) {
			return null;
		}
		for (Product_Type top : topList) {
			this.appendChild(top, allList);
		}
		return topList;
	}

	@Override
	public List<Product_Type> findProduct_TypeByColumnIds(String id) throws Exception {
		List<Product_Type> listAll = (List<Product_Type>) dao
				.findForList("productTypeMapper.selectProduct_TypeByColumnIdTop", id);
		List<Product_Type> listTop = new ArrayList<>();
		List<String> ids = new ArrayList<>();
		for (Product_Type productType : listAll) {
			ids.add(productType.getId());
		}
		if (CollectionUtils.isEmpty(listAll)) {
			return null;
		}
		for (int i = 0; i < listAll.size(); i++) {
			if (!ids.contains(listAll.get(i).getPid())) {
				this.appendChildProductType(listAll.get(i), listAll);
				listTop.add(listAll.get(i));
			}
		}
		return listTop;

	}

	private void appendChildProductType(Product_Type root, List<Product_Type> allList) {
		for (int i = 0; i < allList.size(); i++) {
			Product_Type pt = allList.get(i);
			if (pt != null && pt.getPid().equals(root.getId())) {
				if (CollectionUtils.isEmpty(root.getChildList())) {
					root.setChildList(new ArrayList<Product_Type>());
				}
				root.getChildList().add(pt);
				this.appendChildProductType(pt, allList);
			}
		}
	}

	@Override
	public void updateSort(Map<String, Object> map) throws Exception {
		dao.update("productTypeMapper.updateSortById", map);

	}

	@Override
	public List<Product_Type> findProduct_TypeAllByAll(Map<String, Object> map) throws Exception {

		return (List<Product_Type>) dao.findForList("productTypeMapper.selectAllByAll", map);
	}

	@Override
	public Product_Type findTypeInfoById(String id) throws Exception {
		return (Product_Type) dao.findForObject("productTypeMapper.findTypeInfoById", id);
	}

	@Override
	public Integer findcount(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return (Integer) dao.findForObject("productTypeMapper.selectCount", map);
	}

	@Override
	public Integer findBrandcount(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return (Integer) dao.findForObject("productTypeMapper.selectBrandcountCount", map);
	}

	@Override
	public Integer findPropertycount(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return (Integer) dao.findForObject("productTypeMapper.selectPropertycountCount", map);
	}

	@Override
	public List<Product_Type> getTreeByColumId(PageData pd) throws Exception {
		List<Product_Type> listAll = (List<Product_Type>) dao
				.findForList("productTypeMapper.selectProductTypeByCloumnId", pd);
		List<Product_Type> listTop = new ArrayList<>();
		List<String> ids = new ArrayList<>();
		if (CollectionUtils.isEmpty(listAll)) {
			return null;
		}
		for (Product_Type product_Type : listAll) {
			ids.add(product_Type.getId());
		}
		for (int i = 0; i < listAll.size(); i++) {
			if (!ids.contains(listAll.get(i).getPid())) {
				this.appendChild(listAll.get(i), listAll);
				listTop.add(listAll.get(i));
			}
		}
		return listTop;
	}

	@Override
	public List<Product_Type> getTypeByColumId(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (List<Product_Type>) dao.findForList("productTypeMapper.selectProductTypeByCloumnId", pd);
	}
	
	@Override
	public List<Product_Type> getTypesByColumId(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (List<Product_Type>) dao.findForList("productTypeMapper.findProductTypeByCloumnId", pd);
	}
	
	@Override
	public Product_Type findProductTypeById(String id) throws Exception {
		return (Product_Type) dao.findForObject("productTypeMapper.findProductTypeById", id);
	}
	
	@Override
	public void updataTypeStatus(PageData pd) throws Exception {
		dao.update("productTypeMapper.updateProductTypeStatusByIds", pd);
		dao.update("productTypeMapper.updateTypeStatusByIds", pd);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> selectCountByTypeAndColumID(String columnId) throws Exception {
		return (List<PageData>) dao.findForList("productTypeMapper.selectCountByTypeAndColumID", columnId);
	}
}
