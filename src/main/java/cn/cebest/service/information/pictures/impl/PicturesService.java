package cn.cebest.service.information.pictures.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.service.information.pictures.PicturesManager;
import cn.cebest.util.Const;
import cn.cebest.util.DelAllFile;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.Tools;


/** 图片管理
 * @author 中企高呈
 * 修改时间：2015.11.2
 */
@Service("picturesService")
public class PicturesService implements PicturesManager {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("PicturesMapper.datalistPage", page);
	}
	
	/**广告位列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> bannerList(Page page)throws Exception{
		return (List<PageData>)dao.findForList("PicturesMapper.banner_datalistPage", page);
	}
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("PicturesMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("PicturesMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("PicturesMapper.edit", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("PicturesMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("PicturesMapper.deleteAll", ArrayDATA_IDS);
	}
	
	/**批量获取
	 * @param ArrayDATA_IDS
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> getAllById(String[] ArrayDATA_IDS)throws Exception{
		return (List<PageData>)dao.findForList("PicturesMapper.getAllById", ArrayDATA_IDS);
	}
	
	/**修改图片
	 * @param pd
	 * @throws Exception
	 */
	public void delTp(PageData pd)throws Exception{
		dao.update("PicturesMapper.delTp", pd);
	}

	/**根据栏目ID批量删除图片
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void delPicutre(String[] ids) throws Exception {
			// 删除封面
		if (ids.length > 0) {
			@SuppressWarnings("unchecked")
			List<PageData> listPath = (List<PageData>) dao.findForList("PicturesMapper.selectPicturesPachByColumId", ids);// 查询图片路径
			List<String> listID = new ArrayList<>();
			for (PageData p : listPath) {
				if (p != null) {
					/*String imageId = p.getString("IMAGE_ID");
					listID.add(imageId);*/
					String path = p.getString("PATH");
					if (path != null && Tools.notEmpty(path.trim())) {
						DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + path); // 删除本地磁盘图片
					}
				}

			}
				dao.delete("PicturesMapper.delPicutre", ids);// 删除图片信息
			}
		
	}
	
}

