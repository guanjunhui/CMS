package cn.cebest.service.system.newMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Video;
import cn.cebest.entity.system.content.Content;
import cn.cebest.entity.system.newMessage.NewMessage;
import cn.cebest.entity.system.newMessage.NewMessageType;
import cn.cebest.util.PageData;
/**
 * 资讯接口
 * @author lwt
 *
 */
public interface MyMessageService {
	
	List<NewMessage> findPmmNews(Page page) throws Exception;
	
	PageData findNewMessageById(PageData pd) throws Exception;
	
	List<PageData> findnewsList(PageData pd) throws Exception;
	
	List<NewMessage> newsPageList(Map<String, Object> map)throws Exception;
	
	List<NewMessage> findMessagelistPage(Page page) throws Exception;
	
	List<Image> findNewsImageList(String id) throws Exception;
	
	/**
	 * 查询资讯列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	void findMessageToList(Map<String,Object> map,Page page)throws Exception;
	/**
	 * 刪除资讯
	 */
	void deleteMessage(String [] ids)throws Exception;

	/**
	 * 添加资讯
	 * @param product
	 */
	void save(NewMessage mess,PageData pd) throws Exception;
	/**
	 * 查询所有资讯类型
	 * @return
	 * @throws Exception
	 */
	List<NewMessageType> findMessage_TypeAll(PageData pd) throws Exception;
	/**
	 * 根据id查询相同类型资讯
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<PageData> findMessageRelevantBycode(String id)throws Exception;
	/**
	 * 修改资讯
	 * @param product
	 * @throws Exception
	 */
	void updateMessage(NewMessage mess,PageData pd)throws Exception;
	/**
	 * 根据资讯类型id查询资讯
	 * @param id
	 * @return
	 */
	List<NewMessage> findMessageBytypeId(String[] id)throws Exception;
	/**
	 * 根据资讯id查询相关联的资讯
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<PageData> findMessageRelevant(String id)throws Exception;
	/**
	 * 根据id查询资讯,前端页面类型显示页面通用接口===========================================
	 * @param id
	 * @return
	 * @throws Exception
	 */
	NewMessage findMessageById(String id)throws Exception;
	/**
	 * 根据资讯id查询详细的资讯信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	NewMessage findDetailMessageById(String id) throws Exception;
	/**
	 * 根据id查询相关资讯类型产品
	 * @param id
	 * @return
	 */
	List<NewMessage> findMessageTypeById(String[] id)throws Exception;
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
	 * 修改资讯状态
	 * @param pd
	 * @throws Exception
	 */
	void updateStatus(PageData pd)throws Exception;
	/**
	 * 根据资讯id查找其对应的封面图片
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Image selectImageByMessageId(String id)throws Exception;
	//根据资讯id查找上传的多张视频
	List<Video> selectVideosByMessageId(String id)throws Exception;
	//========================下面为导入导出功能方法===================
	/**资讯列表(全部)
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAllMessage(PageData pd)throws Exception;
	/**保存excel中的资讯
	 * @param pd
	 * @throws Exception
	 */
	public void saveExcelMessage(PageData pd)throws Exception;
	/**
	 * 部分资讯列表
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	List<PageData> listPartMessage(String[] ids) throws Exception;
	/**
	 * 置顶和推荐
	 * @param ids
	 * @throws Exception 
	 */
	void updataRecommendAndTop(NewMessage message)throws Exception;
	/**
	 * 根据栏目id查询资讯
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<NewMessage> findMessageByColumnId(String id)throws Exception;


	/**
	 * 根据类型id查找该资讯下的所有资讯====并分页显示
	 * @return
	 * @throws Exception
	 */
	List<NewMessage> findMessageBytypeOneId(Page page) throws Exception;
	
	/**
	 * 查询各个栏目下置顶的产品
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	List<PageData> findTopList() throws Exception;
	//==================前端页面使用接口=================
	/**
	 * 前端页面使用
	 * 根据资讯ID查找与该资讯相关的图片
	 */
	List<Image> selectImages(String id)throws Exception;
	/**
	 * 前端页面使用
	 * 根据资讯ID查找与该资讯相关的图片
	 */
	Image selectOneImage(String id)throws Exception;
	/**
	 * 根据条件查询资讯
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public List<PageData> findMessageAllByAll(Map<String, Object> pd) throws Exception;
	
	PageData findTemplatePachById(String contentId) throws Exception;
	/**
	 * 在所有资讯中找出一个为推荐的而且是最近更新的一个资讯
	 * @return
	 * @throws Exception
	 */
	List<NewMessage> findNewRecommendMessage(PageData pd) throws Exception;
	/**
	 * 首页先推荐后时间进行排序 ==========显示上一篇下一篇,通用接口
	 * @param id
	 * @param index
	 * @return
	 * @throws Exception
	 */
	NewMessage findIndexMessageById(String id) throws Exception;
	//=================================以下为天华集团所用接口=============================
	/**
	 * 在所有资讯中找出一个为推荐的而且是最近更新的一个资讯
	 * @return
	 * @throws Exception
	 */
	List<NewMessage> findRecommendTimeThMessage() throws Exception;
	/**
	 * 通过类型id查找出相对应的资讯信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<NewMessage> findThNewsByOnetypeId(String id) throws Exception;
	/**
	 * 根据资讯id查找所属栏目
	 */
	List<ColumConfig> findColumConfigByMessageId(String id)throws Exception;
	/**
	 * 根据资讯类型id把其及孩子类型下的所有资讯都展示出来
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<NewMessage> selectlistPageByTypeId(Page page) throws Exception;
	List<PageData> findType(PageData pd) throws Exception;
	/**
	 * 展示排序
	 * @param map
	 * @param page
	 * @throws Exception 
	 */
	void sortlistPage(Map<String, Object> map, Page page) throws Exception;
	//-------------------前台页面接口-----------------------------
	/**
	 * 根据栏目id和类型id查找资讯
	 */
	List<NewMessage> selectMessageBycolumtypeid(Page page)throws Exception;
	/**
	 * 通过资讯栏目和资讯类型查找资讯详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	NewMessage findMessageBycolumtypeId(PageData pds)throws Exception;
	/**
	 * 通过banner的id查询出该banner下的所有图片信息
	 */
	List<Image> findImagesBymessageid(String id)throws Exception;
	//根据图片id查找图片路径
	Image selectImagePathByid(String id) throws Exception;
	//根据图片id删除图片
	void deleteImage(String id)throws Exception;
	/**
	 * 通过资讯的id查询出该banner下的所有视频信息
	 */
	List<Video> findVideosBymessageid(String id)throws Exception;
	//根据视频id查找视频路径
	Video selectVideoPathByid(String id) throws Exception;
	//根据视频id删除视频
	void deleteVideo(String id)throws Exception;
	//根据图片id更新图片信息
	void updateImageById(Image image)throws Exception;
	//根据视频id更新视频信息
	void updateVideoById(Video video)throws Exception;
	/**
	 * 扩展字段处理
	 */
	void toFindExtendWord(Map<String, Object> map)throws Exception;
	/**
	 * 前端分页使用
	 * @param map
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<NewMessage> findMessageToFrontList(Page page) throws Exception;
	/**
	 * 江铜前端使用接口开始>>>>>>>>>>>>>>>>>>>>>>>>>>>
	 */
	//在传媒视点页面第一张显示按照热和最新时间进行排序
	List<NewMessage> selectNewMessageByHotNew(Page page)throws Exception;
	List<NewMessage> findlistPageByColumId(Page page) throws Exception;
	List<NewMessage> findlistStockPageByColumId(Page page) throws Exception;
	/**
	 * 江铜前端使用接口结束<<<<<<<<<<<<<<<<<<<<<<<<<<<
	 */
	//通过栏目id删除其下的资讯
	void deleteMessages(String[] ids);
	
	/**
	 * 汉能（开始）
	 * 汉能：招商加盟
	 */
	List<Content> attractInvestmentMessage(Page page) throws Exception;
	/**
	 * 汉能：人才招聘
	 */
	List<Content> talentRecruitmentMessage(Page page) throws Exception;
	/**
	 * 汉能：查找文化信息(关于汉能下)
	 */
	List<Content> findCultureMessage(Page page) throws Exception;
	/**
	 * 汉能：查找文化信息下的栏目
	 */
	List<NewMessage> findCultureListMessage(Page page) throws Exception;
	void updataRecommendAndTopAndHot(NewMessage message) throws Exception;
	void updateSort(PageData pd) throws Exception;
	List<Map<String, Object>> toAddFind(PageData pd) throws Exception;
	void save(NewMessage mess) throws Exception;
	ColumConfig findColumConfigById(String id) throws Exception;
	void saveContentImages(PageData pd) throws Exception;
	void saveContentVideos(PageData pd) throws Exception;

	List<NewMessage> selectRecommendMessageByColumnIdOrTypeId(PageData pd) throws Exception;
	List<NewMessage> selectMessagelistPageNoTypeid(Page page) throws Exception;

	

	HashMap<String, Object> selectProductsByColumnIds(Page page) throws Exception;
	HashMap<String, Object> selectProductsByColumnIdsTop(Page page) throws Exception;
	NewMessage selectProductDetailByProId(PageData pd) throws Exception;
	/**
	 * 根据id查询详细资讯无上下一条信息
	 */	
	NewMessage findDetailSinggleMessageById(String id) throws Exception;
	List<NewMessage> selectRelProByProId(PageData pd) throws Exception;

	NewMessage findMessageByTypeOrColumnid(PageData pd) throws Exception;

	List<NewMessage> selectNewMessageByexcludePage(Page page) throws Exception;

	List<NewMessage> selectNewMessagelistPage(Page page) throws Exception;

}
