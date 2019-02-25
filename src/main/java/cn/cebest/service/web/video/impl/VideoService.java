package cn.cebest.service.web.video.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.service.web.video.VideoManager;
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
@Service("videoService")
public class VideoService implements VideoManager {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("VideoMapper.datalistPage", page);
	}
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("VideoMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("VideoMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("VideoMapper.edit", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("VideoMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("VideoMapper.deleteAll", ArrayDATA_IDS);
	}
	
	/**批量获取
	 * @param ArrayDATA_IDS
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> getAllById(String[] ArrayDATA_IDS)throws Exception{
		return (List<PageData>)dao.findForList("VideoMapper.getAllById", ArrayDATA_IDS);
	}
	
	/**删除图片
	 * @param pd
	 * @throws Exception
	 */
	public void delTp(PageData pd)throws Exception{
		dao.update("VideoMapper.delTp", pd);
	}

	@Override
	public void delVideos(String[] ids) throws Exception {
		//	// 删除封面
		if (ids.length > 0) {
			@SuppressWarnings("unchecked")
			List<PageData> listPath = (List<PageData>) dao.findForList("VideoMapper.selectVideoPachByColumId", ids);// 查询图片路径
			List<String> listID = new ArrayList<>();
			for (PageData p : listPath) {
				if (p != null) {
					/*String imageId = p.getString("IMAGE_ID");
					listID.add(imageId);*/
					String path = p.getString("VIDEO_URL");
					if (path != null && Tools.notEmpty(path.trim())) {
						DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + path); // 删除本地磁盘图片
					}
				}

			}
				dao.delete("VideoMapper.delVideo", ids);// 删除图片信息
			}
		
		
	}
	
}

