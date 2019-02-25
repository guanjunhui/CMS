package cn.cebest.service.system.product.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.system.product.Brand;
import cn.cebest.service.system.product.BrandService;
import cn.cebest.util.Const;
import cn.cebest.util.DelAllFile;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.Tools;
@Service("brandService")
public class BrandServiceImpl implements BrandService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Override
	public List<Brand> getTree(PageData pd) throws Exception {
		List<Brand> listTop = (List<Brand>) dao
				.findForList("ProductMapper.selectBrandTopList", pd);
		List<Brand> listALL = (List<Brand>) dao
				.findForList("ProductMapper.selectBrandAllList", pd);
		if (!CollectionUtils.isEmpty(listALL) && !CollectionUtils.isEmpty(listTop)) {
			for (Brand top : listTop) {
				this.appendChild(top, listALL);
			}
		}
		return listTop;

	}

	private void appendChild(Brand root, List<Brand> allList) {
		for (int i = 0; i < allList.size(); i++) {
			Brand pt = allList.get(i);
			if (pt != null && pt.getPid().equals(root.getId())) {
				if (CollectionUtils.isEmpty(root.getChildList())) {
					root.setChildList(new ArrayList<Brand>());
				}
				root.getChildList().add(pt);
				allList.remove(i);
				i--;
				this.appendChild(pt, allList);
			}
		}
	}
	private void appendChildIds(List<String> newIdlist,String str,List<Map<String,String>> listAll){
		for (int i=0;i<listAll.size();i++) {
			if(str.equals(listAll.get(i).get("PID"))){
				newIdlist.add(listAll.get(i).get("ID"));
				listAll.remove(i);
				i--;
			}
		}
	}
	@Override
	public void delete(String[] id) throws Exception {
		List<String> newIdlist=new ArrayList(Arrays.asList(id));
		PageData data=new PageData();
		List<Map<String,String>> listAll = (List<Map<String,String>>) dao.findForList("ProductMapper.selectBrandAllIdPiD", data);
		for (int i=0;i<newIdlist.size();i++) {
			appendChildIds(newIdlist,newIdlist.get(i),listAll);
		}
		@SuppressWarnings("unchecked")
		List<PageData> list = (List<PageData>) dao.findForList("ProductMapper.selectImagePachByBrandId",
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
		dao.delete("ProductMapper.deleteBrand", newIdlist.toArray(new String[newIdlist.size()]));
		
	}

	@Override
	public void save(PageData pd) throws Exception {
		// 保存图片
		if ((Boolean) pd.get("flag")) {
			dao.save("ProductMapper.saveImage", pd);
		}
		dao.save("ProductMapper.insertBrand", pd);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(PageData pd) throws Exception {
		// 修改封面图
		if ((Boolean) pd.get("flag")) {
			List<PageData> list = (List<PageData>) dao.findForList("ProductMapper.selectImagePachByBrandId",
					new String[] { pd.getString("id") });// 查询图片路径
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
			if (pd.getString("productImageId") == null || "".equals(pd.getString("productImageId"))) {
				List<PageData> list = (List<PageData>) dao.findForList("ProductMapper.selectImagePachByProductId",
						new String[] { pd.getString("id") });// 查询图片路径
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
		dao.update("ProductMapper.updateBrand", pd);
		
	}

	@Override
	public void findById(Map<String, Object> map, String id) throws Exception {
		map.put("brand", dao.findForObject("ProductMapper.selectBrandById", id));
		
	}

}
