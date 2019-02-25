package cn.cebest.service.web.banner;

import java.util.List;
import java.util.Map;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Video;
import cn.cebest.entity.web.Banner;
import cn.cebest.util.PageData;
/** 
 * 说明： 广告位接口
 * 创建人：liuzhule
 * @version
 */
public interface BannerManagerService {
	/**
	 * 新增
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd) throws Exception;

	/**
	 * 删除
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd) throws Exception;

	/**
	 * 修改
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd) throws Exception;
	
	/**
	 * 修改状态
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public void updateStatus(PageData pd) throws Exception;

	/**
	 * 列表
	 * 
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception;

	/**
	 * 列表(全部)
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd) throws Exception;

	/**
	 * 通过id获取数据
	 * 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd) throws Exception;

	/**
	 * 批量删除
	 * 
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception;
	//========================开启终结之路=======================>>>>>>>>>
	/**
	 * 保存图片
	 * @throws Exception
	 */
	void saveImages(PageData pd)throws Exception;
	/**
	 * 保存视频
	 * @throws Exception
	 */
	void saveVideos(PageData pd)throws Exception;
	/**
	 * 保存banner
	 * @param product
	 */
	void saveBanner(Banner bann,PageData pd) throws Exception;
	/**
	 * 保存栏目与banner之间的关系
	 */
	void saveBannerColumn(PageData pd)throws Exception;
	/**
	 * 查询banner列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	void findBannerToList(Map<String,Object> map,Page page)throws Exception;
	/**
	 * 删除banner
	 * @param id
	 * @return
	 */
	void deleteBanner(String [] id)throws Exception;
	/**
	 * 根据banner的id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Banner findBannerById(String id)throws Exception;
	/**
	 * 根据bannerid去找相关的栏目
	 * @param id
	 * @throws Exception
	 */
	Banner findColumnByBannerId(String id)throws Exception;
	/**
	 * 修改banner
	 * @param product
	 * @throws Exception
	 */
	void updateBanner(Banner banner)throws Exception;
	/**
	 * 根据栏目ids批量删除
	 */
	public void deleteByColumnIds(String[] ids) throws Exception;
	/**
	 * 通过id查询相关栏目的详细信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	void selectColumnDetail(Map<String, Object> map,String id)throws Exception;
	/**
	 * 根据栏目的id查找对应所有banner的详细信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<Banner> selectBannerDetailByColumnId(PageData pd) throws Exception;
	/**
	 * 根据Banner的id查找对应的Image信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public  List<Image> selectImagesPath(String id) throws Exception;
	/**
	 * 查询视频路径
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<Video> selectVideosPath(String id) throws Exception;

	Long selectBannerByName(Map map) throws Exception;
	/**
	 * 通过banner的id删除栏目与banner之间的关系
	 */
	void deleteRelationByBannerId(String id)throws Exception;

	void deleteImages(String[] ids)throws Exception;

	void deleteVideos(String[] ids) throws Exception;
	/**
	 * 通过banner的id查询出该banner下的所有图片信息
	 */
	List<Image> findImagesBybannerid(String id)throws Exception;
	//根据图片id查找图片路径
	Image selectImagePathByid(String id) throws Exception;
	//根据图片id删除图片
	void deleteImage(String id)throws Exception;
	/**
	 * 通过banner的id查询出该banner下的所有视频信息
	 */
	List<Video> findVideosBybannerid(String id)throws Exception;
	//根据视频id查找视频路径
	Video selectVideoPathByid(String id) throws Exception;
	//根据视频id删除视频
	void deleteVideo(String id)throws Exception;
	//根据图片id更新图片信息
	void updateImageById(Image image)throws Exception;
	//根据视频id更新视频信息
	void updateVideoById(Video video)throws Exception;
	//查询banner有关的栏目列表
	List<PageData> findColum(PageData pd) throws Exception;
	//根据栏目查询其下的所有banner并分页显示
	List<Banner> selectlistPageByColumID(Page page) throws Exception;

	List<Banner> selectBannerListDetailByColumnId(PageData pd) throws Exception;
	//修改banner列表排序值
	public void updateSort(PageData pd) throws Exception;
}
