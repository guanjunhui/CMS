package cn.cebest.service.web.banner.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Video;
import cn.cebest.entity.web.Banner;
import cn.cebest.service.web.banner.BannerManagerService;
import cn.cebest.util.Const;
import cn.cebest.util.DelAllFile;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.Tools;

/**
 * 说明： 广告位接口实现 创建人：liuzhule
 * 
 * @version
 */
@SuppressWarnings("unchecked")
@Service("bannerService")
public class BannerManagerServiceImpl implements BannerManagerService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd) throws Exception {
		dao.save("BannerMapper.insert", pd);
	}

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception {
		dao.delete("BannerMapper.deleteByPrimaryKey", pd);
	}

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception {
		dao.update("BannerMapper.updateByPrimaryKey", pd);
	}

	/**
	 * 修改状态
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void updateStatus(PageData pd) throws Exception {
		dao.update("BannerMapper.updateStatusByPrimaryKey", pd);
	}

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception {
		return (PageData) dao.findForObject("BannerMapper.selectByPrimaryKey", pd);
	}

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page) throws Exception {
		return (List<PageData>) dao.findForList("BannerMapper.datalistPage", page);
	}

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("BannerMapper.listAll", pd);
	}

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		dao.delete("BannerMapper.deleteAll", ArrayDATA_IDS);
	}

	//下面由我加入=======================================================================
	/**
	 * 此业务方法主要用于保存多张图片
	 */
	@Override
	public void saveImages(PageData pd) throws Exception {
		// 保存图片
		if ((Boolean) pd.get("flag")) {
			dao.save("BannerMapper.saveImage", pd);
		}

	}

	/**
	 * 此业务方法主要用于保存多个视频
	 */
	@Override
	public void saveVideos(PageData pd) throws Exception {
		//保存视频
		if ((Boolean) pd.get("flag")) {
			dao.save("BannerMapper.saveVideo", pd);
		}
	}

	/**
	 * 保存banner
	 */
	@Override
	public void saveBanner(Banner bann, PageData pd) throws Exception {
		// 保存banner
		dao.save("BannerMapper.saveBanner", bann);
	}

	/**
	 * 保存banner与栏目之间的关系
	 */
	@Override
	public void saveBannerColumn(PageData pd) throws Exception {
		//保存栏目和banner之间的关系
		dao.save("BannerMapper.saveColumnBannerRelation", pd);
	}

	/**
	 * banner显示列表方法
	 */
	@Override
	public void findBannerToList(Map<String, Object> map, Page page) throws Exception {
		map.put("bannerList", dao.findForList("BannerMapper.findBannerlistPage", page));
	}

	/**
	 * 根据id删除banner
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void deleteBanner(String[] ids) throws Exception {
		for (String id : ids) {
			//删除与该banner相关的图片文件
			List<Image> listImage = (List<Image>) dao.findForList("BannerMapper.selectImagesPath", id);// 查询图片路径
			for (Image im : listImage) {
				if (im != null) {
					String path = im.getImgurl();
					if (path != null && Tools.notEmpty(path.trim())) {
						DelAllFile.deleteFile(PathUtil.getClasspath() + Const.FILEPATHIMG + path); // 删除图片文件
					}
				}

			}
			//删除与该banner相关的视频文件
			List<Video> listVideo = (List<Video>) dao.findForList("BannerMapper.selectVideosPath", id);// 查询视频路径
			for (Video vi : listVideo) {
				if (vi != null) {
					String filePath = vi.getVideo_url();
					if (filePath != null && Tools.notEmpty(filePath.trim())) {
						DelAllFile.deleteFile(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + filePath); // 删除视频文件
					}
				}

			}
		}
		dao.delete("BannerMapper.deleteCBrelation", ids);// 删除banner和栏目之间的关系
		dao.delete("BannerMapper.deleteBanner", ids);// 删除banner
		dao.delete("BannerMapper.deleteBannerImage", ids);// 删除banner相关联图片信息
		dao.delete("BannerMapper.deleteBannerVideo", ids);// 删除banner相关联视频信息
	}

	/**
	 * 根据id查询banner
	 */
	@Override
	public Banner findBannerById(String id) throws Exception {
		return (Banner) dao.findForObject("BannerMapper.selectBannerById", id);
	}

	/**
	 * 根据bannerid去查询其对应的栏目
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public Banner findColumnByBannerId(String id) throws Exception {
		return (Banner) dao.findForObject("BannerMapper.selectColumByBannerId", id);
	}

	/**
	 * 修改banner的方法
	 * 
	 * @param banner
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void updateBanner(Banner banner) throws Exception {
		dao.update("BannerMapper.updateBanner", banner);

	}

	//删除banner与栏目的关系
	@SuppressWarnings("unchecked")
	public void deleteByColumnIds(String[] ids) throws Exception {
		//查询banner类型ids
		List<String> listId = (List<String>) dao.findForList("BannerMapper.selectIdsByColumnIds", ids);
		if (listId.size() > 0) {
			List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) dao.findForList("BannerMapper.selectCountByIds", listId.toArray(new String[listId.size()]));
			for (HashMap<String, Object> hashMap : list) {
				if ((Long) hashMap.get("COUNT(*)") > 1) {

				} else {
					//删除banner
					deleteBanner(new String[] { (String) hashMap.get("BANNERID") });
				}
			}
		}
		dao.delete("BannerMapper.deleteByColumnIds", ids);
	}

	/**
	 * 根据栏目的id查询对应栏目的详细信息
	 */
	@Override
	public void selectColumnDetail(Map<String, Object> map, String id) throws Exception {
		map.put("column", dao.findForObject("BannerMapper.selectColumnDetail", id));
	}

	/**
	 * 根据栏目的id查询对应banner的详细信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Banner> selectBannerDetailByColumnId(PageData pd) throws Exception {
		return (List<Banner>) dao.findForList("BannerMapper.selectListPageBannerDetailByColumnId", pd);
	}

	@Override
	public List<Image> selectImagesPath(String id) throws Exception {
		return (List<Image>) dao.findForList("BannerMapper.selectImagesPath", id);// 查询图片路径

	}

	/**
	 * 删除图片信息
	 */
	@Override
	public void deleteImages(String[] ids) throws Exception {
		dao.delete("BannerMapper.deleteBannerImage", ids);// 删除banner相关联图片信息
	}

	/**
	 * 删除视频信息
	 */
	@Override
	public void deleteVideos(String[] ids) throws Exception {
		dao.delete("BannerMapper.deleteBannerVideo", ids);// 删除banner相关联视频信息
	}

	@Override
	public List<Video> selectVideosPath(String id) throws Exception {
		return (List<Video>) dao.findForList("BannerMapper.selectVideosPath", id);// 查询视频路径

	}

	@Override
	public Long selectBannerByName(Map map) throws Exception {
		return (Long) dao.findForObject("BannerMapper.selectBannerByName", map);// 查询图片路径

	}

	/**
	 * 通过banner的id删除banner与栏目之间的关系
	 */
	@Override
	public void deleteRelationByBannerId(String id) throws Exception {
		dao.delete("BannerMapper.deleteBannerColumRelation", id);
	}

	/**
	 * 通过banner的id查询出该banner下的所有图片信息
	 */
	@Override
	public List<Image> findImagesBybannerid(String id) throws Exception {
		return (List<Image>) dao.findForList("BannerMapper.findImagesBybannerid", id);
	}

	/**
	 * 根据图片id查找对应的图片路径当然包括它的信息
	 */
	@Override
	public Image selectImagePathByid(String id) throws Exception {
		return (Image) dao.findForObject("BannerMapper.selectImagePathByid", id);// 查询图片路径

	}

	//根据图片id删除该图片详情
	@Override
	public void deleteImage(String id) throws Exception {
		dao.delete("BannerMapper.deleteImage", id);
	}

	//通过banner的id查询出该banner下的所有视频信息
	@Override
	public List<Video> findVideosBybannerid(String id) throws Exception {
		return (List<Video>) dao.findForList("BannerMapper.findVideosBybannerid", id);
	}

	//根据视频id查找视频路径
	@Override
	public Video selectVideoPathByid(String id) throws Exception {
		return (Video) dao.findForObject("BannerMapper.selectVideoPathByid", id);// 查询视频路径
	}

	//根据视频id删除视频
	@Override
	public void deleteVideo(String id) throws Exception {
		dao.delete("BannerMapper.deleteVideo", id);
	}

	//根据图片id更新图片信息
	@Override
	public void updateImageById(Image image) throws Exception {
		dao.update("BannerMapper.updateImage", image);
	}

	//根据视频id更新视频信息
	@Override
	public void updateVideoById(Video video) throws Exception {
		dao.update("BannerMapper.updateVideo", video);
	}

	//查询banner的所有栏目
	@Override
	public List<PageData> findColum(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("BannerMapper.selectColum", pd);
	}

	//根据栏目id查询banner
	@Override
	public List<Banner> selectlistPageByColumID(Page page) throws Exception {
		return (List<Banner>) dao.findForList("BannerMapper.selectListPageBannerByColumnId", page);
	}

	//++++++++++
	/**
	 * 根据栏目的id查询对应banner的详细信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Banner> selectBannerListDetailByColumnId(PageData pd) throws Exception {
		return (List<Banner>) dao.findForList("BannerMapper.selectBannerListPageByColumId", pd);
	}

	@Override
	public void updateSort(PageData pd) throws Exception {
		
		dao.update("BannerMapper.updateSort", pd);
	}
}
