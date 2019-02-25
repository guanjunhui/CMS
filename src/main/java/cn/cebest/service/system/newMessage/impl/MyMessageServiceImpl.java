package cn.cebest.service.system.newMessage.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Video;
import cn.cebest.entity.system.content.Content;
import cn.cebest.entity.system.newMessage.NewMessage;
import cn.cebest.entity.system.newMessage.NewMessageType;
import cn.cebest.service.system.newMessage.MyMessageService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.DelAllFile;
import cn.cebest.util.ExtendFiledUtil;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.Tools;
import cn.cebest.util.UuidUtil;
/**
 * 资讯接口的实现类
 * @author lwt
 *
 */
@Service("MyMessageService")
public class MyMessageServiceImpl implements MyMessageService {
	/**
	 * 根据id类型注入dao
	 */
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	

	@Override
	public List<NewMessage> findPmmNews(Page page) throws Exception {
		return (List<NewMessage>) dao.findForList("MessageMapper.findPmmNewsList", page);
	}
	
	/**
	 * <p>Title: findNewMessageById</p>   
	 * <p>Description: 通过id查找当前信息数据以及前后各一条数据 </p>   
	 * @param pd
	 * @return
	 * @throws Exception   
	 * @see cn.cebest.service.system.newMessage.MyMessageService#findNewMessageById(cn.cebest.util.PageData)
	 */
	@Override
	public PageData findNewMessageById(PageData pd) throws Exception {
		NewMessage thMessage = (NewMessage) dao.findForObject("MessageMapper.findNewMessageById", pd);
		pd.put("th",thMessage);
		/*Page page = new Page<>();
		page.setPd(pd);
		pd.put("id", null);
		pd.put("flag", "1");
		List<NewMessage> messages = (List<NewMessage>)dao.findForList("MessageMapper.selectMessagelistPage", page);
		Integer before = thMessage.getSort()-1;
		Integer aster = thMessage.getSort()+1;
		pd.put("before","");
		pd.put("after","");
		for (NewMessage newMessage : messages) {
			if (newMessage.getSort().equals(before)) {
				pd.put("before",newMessage);
			}
			if (newMessage.getSort().equals(aster)) {
				pd.put("after",newMessage);
			}
		}*/
		return pd;
	}
	
	/**
	 * <p>Title: findnewsList</p>   
	 * <p>Description: 根据条件查找新闻/活动 </p>   
	 * @param pd
	 * @return
	 * @throws Exception   
	 * @see cn.cebest.service.system.newMessage.MyMessageService#findnewsList(cn.cebest.util.PageData)
	 */
	@Override
	public List<PageData> findnewsList(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("MessageMapper.findnewsList", pd);
	}
	
	/**
	 * <p>Title: 根据跳过的数量,以及需要展示的条数显示数据</p>   
	 * <p>Description: </p>   
	 * @param page
	 * @return
	 * @throws Exception   
	 * @see cn.cebest.service.system.newMessage.MyMessageService#newsPageList(cn.cebest.entity.Page)
	 */
	@Override
	public List<NewMessage> newsPageList(Map<String, Object> map) throws Exception {
		return (List<NewMessage>)dao.findForList("MessageMapper.findnewsPageList", map);
	}
	
	/**
	 * 查询资讯列表  分页
	 */
	@Override
	public List<NewMessage> findMessagelistPage(Page page) throws Exception {
		return (List<NewMessage>)dao.findForList("MessageMapper.selectMessagelistPage", page);
	}
	
	/**
	 * 查询资讯图片合集
	 */
	@Override
	public List<Image> findNewsImageList(String id) throws Exception {
		return (List<Image>)dao.findForList("MessageMapper.findNewsImageList", id);
	}
	/**
	 * 查询资讯列表
	 */
	@Override
	public void findMessageToList(Map<String, Object> map, Page page) throws Exception {
		map.put("messageTypeList", dao.findForList("MessageMapper.selectMessageType", page.getPd()));
		map.put("messageList", dao.findForList("MessageMapper.findMessagelistPage", page));
	}
	/**
	 * 查询资讯列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessage> findMessageToFrontList(Page page) throws Exception {
		return (List<NewMessage>)dao.findForList("MessageMapper.selectMessageBycolumtypeid", page);
	}

	@Override
	public List<Map<String,Object>> toAddFind(PageData pd) throws Exception {
		//  查模板作用域
		return (List<Map<String, Object>>) dao.findForList("MessageMapper.selectMessageTemplate", pd);	
	}
	/**
	 * 刪除资讯
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void deleteMessage(String[] ids) throws Exception {
		dao.delete("MessageMapper.deleteMessageRelevantList", ids);// 删除关联关系
		
		//删除资讯地址栏的url信息
		dao.delete("MessageMapper.delMessageUrlName", ids);
		
		//删除图片文件
		List<PageData> list = (List<PageData>) dao.findForList("MessageMapper.selectImagePath", ids);// 查询图片路径
		List<String> listID = new ArrayList<>();
		for (PageData pd : list) {
			if (pd != null) {
				String imageId = pd.getString("IMAGE_ID");
				listID.add(imageId);
				String imagepath = pd.getString("IMGURL");
				if (imagepath != null && Tools.notEmpty(imagepath.trim())) {
					DelAllFile.deleteFile(PathUtil.getClasspath() + Const.FILEPATHIMG + imagepath); // 删除图片文件
				}
			}

		}
		if (listID.size() > 0) {
			dao.delete("MessageMapper.deleteMessageImage", listID.toArray());// 删除图片信息
		}
		//删除视频文件
		List<PageData> listv = (List<PageData>) dao.findForList("MessageMapper.selectVideoPath", ids);// 查询视频路径
		List<String> listVideoID = new ArrayList<>();
		for (PageData pd : listv) {
			if (pd != null) {
				String videoId = pd.getString("MASTER_ID");
				listVideoID.add(videoId);
				String videopath = pd.getString("VIDEO_URL");
				if (videopath != null && Tools.notEmpty(videopath.trim())) {
					DelAllFile.deleteFile(PathUtil.getClasspath() + Const.FILEPATHFILE + videopath); // 删除视频文件
				}
			}

		}
		if (listVideoID.size() > 0) {
			dao.delete("MessageMapper.deleteMessageVideo", listVideoID.toArray());// 删除视频信息
		}

		dao.delete("MessageMapper.deleteType_message_relation", ids);// 删除资讯和资讯关系
		dao.delete("MessageMapper.deleteMessageTxt", ids);// 删除富文本
		dao.delete("MessageMapper.deleteMessage", ids);// 删除资讯
		
	}
	//通过多个栏目id删除其下的所有资讯
	@SuppressWarnings("unchecked")
	@Override
	public void deleteMessages(String[] ids){
		try {
			List<String> messageids=(List<String>)dao.findForList("MessageMapper.findMessageIdByColumIds", ids);
			String[] targetArr=new String[messageids.size()];
			messageids.toArray(targetArr);
			if(targetArr.length>0){
				deleteMessage(targetArr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//===================================================================================================
	/**
	 * 保存资讯
	 */
	@Override
	public void save(NewMessage mess, PageData pd) throws Exception {
		// 保存资讯
		dao.save("MessageMapper.saveNewMessage", mess);
		// 保存资讯类型关联关系
		//dao.save("MessageMapper.insertType_message_relation", mess);
		//保存该条资讯与栏目类型之间的关系
		String[] columtype = mess.getColumtype();
		if(columtype!=null && columtype.length>0){
			for (String string : columtype) {
				String[] split = string.split("-");
				pd.put("messagetypeid", split[0]);
				pd.put("columnid",split[1]);
				pd.put("id", mess.getId());
				dao.save("MessageMapper.insertTypeMessageColumRelation", pd);
				//添加资讯地址栏的url信息
				dao.save("MessageMapper.insertTypeMessageUrlNameColumRelation", pd);
			}
		}else if(mess.getColumnids()!=null && mess.getColumnids().length>0){
			String[] columns=mess.getColumnids();
			for (int i = 0; i < columns.length; i++) {
				pd.put("columnid",columns[i]);
				pd.put("id", mess.getId());
				dao.save("MessageMapper.insertTypeMessageColumRelation", pd);
				//添加资讯地址栏的url信息
				dao.save("MessageMapper.insertTypeMessageUrlNameColumRelation", pd);
			}
			
		}
		// 保存资讯关联关系
		String[] relevantIdArr = mess.getMessageRelevantIdList();
		if (relevantIdArr != null && relevantIdArr.length > 0) {
			dao.save("MessageMapper.saveMessageRelevant", mess);
		}

		// 创建文本表数据
		dao.save("MessageMapper.saveMessageTxt", mess);
	}
	
	/**
	 * 保存资讯
	 */
	@Override
	public void save(NewMessage mess) throws Exception {
		PageData pd=mess.getPd();
		// 保存资讯
		dao.save("MessageMapper.saveNewMessage", mess);
		// 保存资讯类型关联关系
		//dao.save("MessageMapper.insertType_message_relation", mess);
		//保存该条资讯与栏目类型之间的关系
		String[] columtype = mess.getColumtype();
		if(columtype!=null && columtype.length>0){
			for (String string : columtype) {
				String[] split = string.split("-");
				pd.put("messagetypeid", split[0]);
				pd.put("columnid",split[1]);
				pd.put("id", mess.getId());
				dao.save("MessageMapper.insertTypeMessageColumRelation", pd);
				// 保存资讯地址栏的url信息
				dao.save("MessageMapper.saveContentUrlName", pd);
			}
		}else if(mess.getColumnids()!=null && mess.getColumnids().length>0){
			String[] columns=mess.getColumnids();
			for (int i = 0; i < columns.length; i++) {
				pd.put("columnid",columns[i]);
				pd.put("id", mess.getId());
				dao.save("MessageMapper.insertTypeMessageColumRelation", pd);
				// 保存资讯地址栏的url信息
				dao.save("MessageMapper.saveContentUrlName", pd);
			}
		}
		// 保存资讯关联关系
		String[] relevantIdArr = mess.getMessageRelevantIdList();
		if (relevantIdArr != null && relevantIdArr.length > 0) {
			dao.save("MessageMapper.saveMessageRelevant", mess);
		}

		// 创建文本表数据
		dao.save("MessageMapper.saveMessageTxt", mess);
	}
	/**
	 * 查询所有资讯类型
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessageType> findMessage_TypeAll(PageData pd) throws Exception {
		return (List<NewMessageType>) dao.findForList("MessageMapper.selectMessageType", pd);
	}
	/**
	 * 根据id查询相同类型的资讯
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> findMessageRelevantBycode(String id) throws Exception {
		return (List<PageData>) dao.findForList("MessageMapper.selectMessageRelevantBycode", id);
	}
	/**
	 * 修改资讯
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void updateMessage(NewMessage mess, PageData pd) throws Exception {
		if ((Boolean) pd.get("flag")) {
			if (mess.getSurface_image() != null && !"".equals(mess.getSurface_image())) {
				List<PageData> list = (List<PageData>) dao.findForList("MessageMapper.selectImagePath",
						new String[] { mess.getId() });// 查询图片路径
				for (PageData data : list) {
					if (data != null) {
						String pach = data.getString("IMGURL");
						if (pach != null && Tools.notEmpty(pach.trim())) {
							DelAllFile.deleteFile(PathUtil.getClasspath() + Const.FILEPATHIMG + pach); // 删除图片
						}
					}
				}
				dao.update("MessageMapper.updateMessageImage", pd);// 修改图片信息
			} else {
				pd.put("messageImageId", UuidUtil.get32UUID());
				mess.setSurface_image(pd.getString("messageImageId"));
				dao.save("MessageMapper.saveImage", pd);// 保存图片信息
			}
		}
		// 重新建立资讯类型和资讯关联关系
		if(mess.getColumnids()!=null && mess.getColumnids().length>0){
			if (mess.getColumtype() != null && mess.getColumtype().length > 0) {
				// 删除资讯类型和资讯关联关系
				dao.delete("MessageMapper.deleteType_message_relation", new String[] { mess.getId() });
				String[] columtype = mess.getColumtype();
				if(columtype!=null && columtype.length>0){
					for (String string : columtype) {
						String[] split = string.split("-");
						pd.put("messagetypeid", split[0]);
						pd.put("columnid",split[1]);
						pd.put("id", mess.getId());
						dao.save("MessageMapper.insertTypeMessageColumRelation", pd);
						//查询栏目地址栏的url信息
						PageData contentUrlNameSize = (PageData) dao.findForObject("MessageMapper.findMyMessageUrlNameList", pd);
						//修改栏目地址栏的url信息
						if(!"".equals(contentUrlNameSize) && contentUrlNameSize != null){
							dao.update("MessageMapper.editContentUrlNameconfig", pd);
						}else{
							dao.save("MessageMapper.saveContentUrlName", pd);
						}
					}
				}
			}else{
				//先删除关联关系
				dao.delete("MessageMapper.deleteType_message_relation", new String[] { mess.getId() });
				//再保存
				String[] columns=mess.getColumnids();
				for (int i = 0; i < columns.length; i++) {
					pd.put("columnid",columns[i]);
					pd.put("id", mess.getId());
					dao.save("MessageMapper.insertTypeMessageColumRelation", pd);
					//查询栏目地址栏的url信息
					List<PageData> contentUrlNameSize = (List<PageData>) dao.findForList("MessageMapper.findMyMessageUrlNameList", pd);
					//修改栏目地址栏的url信息
					if(!"".equals(contentUrlNameSize) && contentUrlNameSize != null){
						dao.update("MessageMapper.editContentUrlNameconfig", pd);
					}else{
						dao.save("MessageMapper.saveContentUrlName", pd);
					}
				}
			}
			
		}
		// 删除关联关系
		// 重新建立关联关系
		String[] relevantIdArr = mess.getMessageRelevantIdList();
		if (relevantIdArr != null && relevantIdArr.length > 0) {
			dao.delete("MessageMapper.deleteMessageRelevantList", new String[] { mess.getId() });// 删除关联关系
			dao.save("MessageMapper.saveMessageRelevant",mess);
		}

		// 修改文本表数据
		dao.update("MessageMapper.upateMessageTxt", mess);
		// 修改资讯
		dao.update("MessageMapper.updateMessage", mess);
		
		/*String[] typeids = mess.getColumtype();
		String[] columtype = mess.getColumtype();
		if(columtype!=null && columtype.length>0){
			for (int i=0;i<typeids.length;i++) {
				String[] split = columtype[i].split("-");
				typeids[i] = split[0];
			}
		}
		mess.setColumtype(typeids);*/
	}
	/**
	 * 根据多个资讯类型id查询资讯
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessage> findMessageBytypeId(String[] id) throws Exception {
		return (List<NewMessage>) dao.findForList("MessageMapper.selectMessageBytypeId", id);
	}
	/**
	 * 根据一个资讯类型id查询资讯====================并分页显示
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessage> findMessageBytypeOneId(Page page) throws Exception {
		return (List<NewMessage>)dao.findForList("MessageMapper.selectlistPageMessageByOnetypeId", page);
	}
	/**
	 * 根据资讯id查询相关联的资讯
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> findMessageRelevant(String id) throws Exception {
		return (List<PageData>) dao.findForList("MessageMapper.selectMessageRelevant", id);
	}
	/**
	 * 通用类型页面接口
	 * 根据id查询资讯并把其上一个和下一个资讯的id和标题带出来>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NewMessage findMessageById(String id) throws Exception {
		List<NewMessage> list=null;
		PageData pd=new PageData();
		list=(List<NewMessage>) dao.findForList("MessageMapper.findMessageById", id);
		if(list.size()==0){
			return null;
		}else if(list.size()>0 && list.size()<2){
			if(list.get(0).getCount()==null || list.get(0).getCount()<=Const.INT_0){
				pd.put("count", Const.INT_1);
			}else{
				pd.put("count",(list.get(0).getCount())+Const.INT_1);
			}
			pd.put("id", list.get(0).getId());
			dao.update("MessageMapper.updateReadCount",pd);
			return list.get(0);
		}else if(list.size()<3){
			if(list.get(0).getId().equals(id)){
				list.get(0).setAfterId(list.get(1).getId());//设置下一条资讯的id
				list.get(0).setAfterTitle(list.get(1).getMessage_title());//设置下一条资讯的标题
				if(list.get(0).getCount()==null || list.get(0).getCount()<=Const.INT_0){
					pd.put("count", Const.INT_1);
				}else{
					pd.put("count",(list.get(0).getCount())+Const.INT_1);
				}
				pd.put("id", list.get(0).getId());
				dao.update("MessageMapper.updateReadCount",pd);
				return list.get(0);
			}else{
				list.get(1).setBeforeId(list.get(0).getId());//设置下一条资讯的id
				list.get(1).setBeforeTitle(list.get(0).getMessage_title());//设置下一条资讯的标题
				if(list.get(1).getCount()==null || list.get(1).getCount()<=Const.INT_0){
					pd.put("count", Const.INT_1);
				}else{
					pd.put("count",(list.get(1).getCount())+Const.INT_1);
				}
				pd.put("id", list.get(1).getId());
				dao.update("MessageMapper.updateReadCount",pd);
				return list.get(1);
			}
		}else{
			list.get(1).setBeforeId(list.get(0).getId());//设置上一条资讯的id
			list.get(1).setBeforeTitle(list.get(0).getMessage_title());//设置上一条资讯的标题
			list.get(1).setAfterId(list.get(2).getId());//设置下一条资讯的id
			list.get(1).setAfterTitle(list.get(2).getMessage_title());//设置下一条资讯的标题
			if(list.get(1).getCount()==null || list.get(1).getCount()<=Const.INT_0){
				pd.put("count", Const.INT_1);
			}else{
				pd.put("count",(list.get(1).getCount())+Const.INT_1);
			}
			pd.put("id", list.get(1).getId());
			dao.update("MessageMapper.updateReadCount",pd);
			return list.get(1);
		}
	}
	/**
	 * 根据id查询详细资讯
	 */
	@Override
	public NewMessage findDetailMessageById(String id) throws Exception {
		return (NewMessage) dao.findForObject("MessageMapper.selectDetailMessageById", id);
	}
	/**
	 * 此方法用于添加资讯时使用
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessage> findMessageTypeById(String[] id) throws Exception {
		return (List<NewMessage>) dao.findForList("MessageMapper.selectMessageTypeById", id);
	}
	/**
	 * 此业务方法主要用于保存多张图片
	 */
	@Override
	public void saveImages(PageData pd) throws Exception {
		dao.save("MessageMapper.saveImage", pd);
	}
	/**
	 * 此业务方法主要用于保存多个视频
	 */
	@Override
	public void saveVideos(PageData pd) throws Exception {
		dao.save("MessageMapper.saveVideo", pd);
	}	
	
	/**
	 * 此业务方法主要用于保存多张图片
	 */
	@Override
	public void saveContentImages(PageData pd) throws Exception {
		dao.save("MessageMapper.saveContentImage", pd);
	}
	/**
	 * 此业务方法主要用于保存多个视频
	 */
	@Override
	public void saveContentVideos(PageData pd) throws Exception {
		dao.save("MessageMapper.saveContentVideo", pd);
	}	
	
	/**
	 * 修改资讯状态
	 */
	@Override
	public void updateStatus(PageData pd) throws Exception {
			dao.update("MessageMapper.updateMessageStatus", pd);	
	}
	/**
	 * 根据资讯id查找其对应的封面图片并回显
	 */
	@Override
	public Image selectImageByMessageId(String id) throws Exception {
		return (Image) dao.findForObject("MessageMapper.selectImageByMessageId", id);
	}
	//-----------------------------导入导出功能实现类--------------------------
	/**
	 *全部资讯列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> listAllMessage(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("MessageMapper.listAllMessage", pd);
	}
	/**
	 * 导入Excel表格中的资讯信息
	 */
	@Override
	public void saveExcelMessage(PageData pd) throws Exception {
		dao.save("MessageMapper.saveExcelMessage", pd);
	}
	/**
	 * 部分资讯列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> listPartMessage(String[] ids) throws Exception {
		return (List<PageData>) dao.findForList("MessageMapper.listPartMessage", ids);
	}
	/**
	 * 置顶和推荐
	 * @param product
	 * @throws Exception
	 */
	@Override
	public void updataRecommendAndTop(NewMessage message) throws Exception {
		dao.update("MessageMapper.updataRecommendAndTopByIds", message);
		
	}
	/**
	 * 根据栏目id查资讯
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessage> findMessageByColumnId(String id) throws Exception {
		List<String> ids=(List<String>) dao.findForList("MessageMapper.selectTypeId", id);
		PageData pd=new PageData();
		pd.put("ids", ids);
		pd.put("RECOMMEND", "1");
		pd.put("TIME", DateUtil.getYear());
		return (List<NewMessage>) dao.findForList("MessageMapper.selectMessageBytypeIds", pd);
	}
	/**
	 * 查询顶级目录
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> findTopList() throws Exception {
		return (List<PageData>) dao.findForList("MessageMapper.findTopList", null);
	}
	/**
	 * 根据条件查询咨询
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> findMessageAllByAll(Map<String, Object> pd) throws Exception{
		return (List<PageData>) dao.findForList("MessageMapper.selectAllByAll", pd);
	}
	/**
	 * 根据资讯id查找出其所有的图片
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Image> selectImages(String id) throws Exception {
		return (List<Image>) dao.findForList("MessageMapper.selectImageDetail", id);
	}
	
	/**
	 * 根据资讯id查找出一张图片
	 */
	@Override
	public Image selectOneImage(String id) throws Exception {
		return (Image)dao.findForObject("MessageMapper.selectImageOneDetail", id);
	}
	
	@Override
	public PageData findTemplatePachById(String contentId) throws Exception {
		return (PageData) dao.findForObject("MessageMapper.selectTemplatePachById", contentId);
	}
	/**
	 * 在所有资讯中找出一个为推荐的而且是最近更新的一个资讯
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessage> findNewRecommendMessage(PageData pd) throws Exception{
		return (List<NewMessage>) dao.findForList("MessageMapper.findNewRecommendMessage", pd);
	}
	/**
	 * 通用首页接口=========================================
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NewMessage findIndexMessageById(String id) throws Exception {
		List<NewMessage> list=null;
		PageData pd=new PageData();
		list=(List<NewMessage>) dao.findForList("MessageMapper.findThIndexMessageById", id);
		if(list.size()==0){
			return null;
		}else if(list.size()>0 && list.size()<2){
			if(list.get(0).getCount()==null || list.get(0).getCount()<=Const.INT_0){
				pd.put("count", Const.INT_1);
			}else{
				pd.put("count",(list.get(0).getCount())+Const.INT_1);
			}
			pd.put("id", list.get(0).getId());
			dao.update("MessageMapper.updateReadCount",pd);
			return list.get(0);
		}else if(list.size()<3){
			if(list.get(0).getId().equals(id)){
				list.get(0).setAfterId(list.get(1).getId());//设置下一条资讯的id
				list.get(0).setAfterTitle(list.get(1).getMessage_title());//设置下一条资讯的标题
				if(list.get(0).getCount()==null || list.get(0).getCount()<=Const.INT_0){
					pd.put("count", Const.INT_1);
				}else{
					pd.put("count",(list.get(0).getCount())+Const.INT_1);
				}
				pd.put("id", list.get(0).getId());
				dao.update("MessageMapper.updateReadCount",pd);
				return list.get(0);
			}else{
				list.get(1).setBeforeId(list.get(0).getId());//设置下一条资讯的id
				list.get(1).setBeforeTitle(list.get(0).getMessage_title());//设置下一条资讯的标题
				if(list.get(1).getCount()==null || list.get(1).getCount()<=Const.INT_0){
					pd.put("count", Const.INT_1);
				}else{
					pd.put("count",(list.get(1).getCount())+Const.INT_1);
				}
				pd.put("id", list.get(1).getId());
				dao.update("MessageMapper.updateReadCount",pd);
				return list.get(1);
			}
		}else{
			list.get(1).setBeforeId(list.get(0).getId());//设置上一条资讯的id
			list.get(1).setBeforeTitle(list.get(0).getMessage_title());//设置上一条资讯的标题
			list.get(1).setAfterId(list.get(2).getId());//设置下一条资讯的id
			list.get(1).setAfterTitle(list.get(2).getMessage_title());//设置下一条资讯的标题
			if(list.get(1).getCount()==null || list.get(1).getCount()<=Const.INT_0){
				pd.put("count", Const.INT_1);
			}else{
				pd.put("count",(list.get(1).getCount())+Const.INT_1);
			}
			pd.put("id", list.get(1).getId());
			dao.update("MessageMapper.updateReadCount",pd);
			return list.get(1);
		}
	}
	/**
	 * 以下为天华集团所用到的接口====================================================================天华集团
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessage> findRecommendTimeThMessage() throws Exception{
		return (List<NewMessage>) dao.findForList("MessageMapper.findRecommendTimeThMessage", null);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessage> findThNewsByOnetypeId(String id) throws Exception {
		return (List<NewMessage>)dao.findForList("MessageMapper.selectThNewsByOnetypeId", id);
	}
	/**
	 * 根据资讯id查找它对应的栏目
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ColumConfig> findColumConfigByMessageId(String id) throws Exception {
		return (List<ColumConfig>)dao.findForList("MessageMapper.selectColumConfigById", id);
	}
	
	/**
	 * 根据资讯id查找它对应的栏目
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ColumConfig findColumConfigById(String id) throws Exception {
		return (ColumConfig) dao.findForObject("MessageMapper.selectColumConfigById", id);
	}
	/**
	 * 根据资讯类型id把其及孩子类型下的所有资讯都展示出来
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessage> selectlistPageByTypeId(Page page) throws Exception {
		List<NewMessage> list=(List<NewMessage>) dao.findForList("MessageMapper.findMessagelistPage", page);
			String typeId=page.getPd().getString("type_id");
			List<Map<String,String>> mapTypeIds= (List<Map<String, String>>) dao.findForList("MessageMapper.selectalltype_ids", page);
			List<String> newTypeIds=new ArrayList<>();
			newTypeIds.add("'"+typeId+"'");
			for (Map<String, String> map : mapTypeIds) {
				if(newTypeIds.contains(map.get("PID"))){
					newTypeIds.add("'"+map.get("ID")+"'");
				}
			}
			
			page.getPd().put("list", newTypeIds.toArray(new String[newTypeIds.size()]));
			list=(List<NewMessage>) dao.findForList("MessageMapper.selectByTypeIDlistPage", page);
		return list;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> findType(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("MessageMapper.selectType", pd);
	}
	/**
	 * 展示排序列表
	 */
	@Override
	public void sortlistPage(Map<String, Object> map, Page page) throws Exception {
		map.put("messageTypeList", dao.findForList("MessageMapper.selectMessageType", page.getPd()));
		map.put("messageList", dao.findForList("MessageMapper.findMessageSortlistPage", page));
	}
	//-------------------前端页面资讯展示接口---------------
	@SuppressWarnings("unchecked")
	/**
	 * 根据栏目id与资讯id去查找相关资讯
	 */
	@Override
	public List<NewMessage> selectMessageBycolumtypeid(Page page) throws Exception {
		return (List<NewMessage>) dao.findForList("MessageMapper.selectMessageBycolumtypeidlistPage", page);
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 根据栏目id去查找没有分类的资讯列表
	 */
	@Override
	public List<NewMessage> selectMessagelistPageNoTypeid(Page page) throws Exception {
		return (List<NewMessage>) dao.findForList("MessageMapper.selectMessagelistPageNoTypeid", page);
	}
	
	/**
	 * 根据栏目id和资讯id去寻找资讯,上一篇,下一篇
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NewMessage findMessageBycolumtypeId(PageData pds) throws Exception {
		List<NewMessage> list=null;
		PageData pd=new PageData();
		list=(List<NewMessage>) dao.findForList("MessageMapper.findMessageBycolumtypeId", pds);
		if(list.size()==0){
			return null;
		}else if(list.size()>0 && list.size()<2){
			if(list.get(0).getCount()==null || list.get(0).getCount()<=Const.INT_0){
				pd.put("count", Const.INT_1);
			}else{
				pd.put("count",(list.get(0).getCount())+Const.INT_1);
			}
			pd.put("id", list.get(0).getId());
			dao.update("MessageMapper.updateReadCount",pd);
			return list.get(0);
		}else if(list.size()<3){
			if(list.get(0).getId().equals(pds.get("id"))){
				list.get(0).setAfterId(list.get(1).getId());//设置下一条资讯的id
				list.get(0).setAfterTitle(list.get(1).getMessage_title());//设置下一条资讯的标题
				list.get(0).setAfterUrlName(list.get(1).getContentUrlName());//设置下一条资讯的Url信息
				if(list.get(0).getCount()==null || list.get(0).getCount()<=Const.INT_0){
					pd.put("count", Const.INT_1);
				}else{
					pd.put("count",(list.get(0).getCount())+Const.INT_1);
				}
				pd.put("id", list.get(0).getId());
				dao.update("MessageMapper.updateReadCount",pd);
				return list.get(0);
			}else{
				list.get(1).setBeforeId(list.get(0).getId());//设置下一条资讯的id
				list.get(1).setBeforeTitle(list.get(0).getMessage_title());//设置下一条资讯的标题
				list.get(1).setBeforeUrlName(list.get(0).getContentUrlName());//设置下一条资讯的Url信息
				if(list.get(1).getCount()==null || list.get(1).getCount()<=Const.INT_0){
					pd.put("count", Const.INT_1);
				}else{
					pd.put("count",(list.get(1).getCount())+Const.INT_1);
				}
				pd.put("id", list.get(1).getId());
				dao.update("MessageMapper.updateReadCount",pd);
				return list.get(1);
			}
		}else{
			list.get(1).setBeforeId(list.get(0).getId());//设置上一条资讯的id
			list.get(1).setBeforeTitle(list.get(0).getMessage_title());//设置上一条资讯的标题
			list.get(1).setBeforeUrlName(list.get(0).getContentUrlName());//设置下一条资讯的Url信息
			list.get(1).setAfterId(list.get(2).getId());//设置下一条资讯的id
			list.get(1).setAfterTitle(list.get(2).getMessage_title());//设置下一条资讯的标题
			list.get(1).setAfterUrlName(list.get(2).getContentUrlName());//设置下一条资讯的Url信息
			if(list.get(1).getCount()==null || list.get(1).getCount()<=Const.INT_0){
				pd.put("count", Const.INT_1);
			}else{
				pd.put("count",(list.get(1).getCount())+Const.INT_1);
			}
			pd.put("id", list.get(1).getId());
			dao.update("MessageMapper.updateReadCount",pd);
			return list.get(1);
		}
	}
	//根据资讯id查找多个上传的视频
	@SuppressWarnings("unchecked")
	@Override
	public List<Video> selectVideosByMessageId(String id) throws Exception {
		return (List<Video>) dao.findForList("MessageMapper.selectVideoDetail", id);
	}
	//=============================更新操作图片和视频开始==========
	@SuppressWarnings("unchecked")
	@Override
	public List<Image> findImagesBymessageid(String id) throws Exception {
		return (List<Image>)dao.findForList("MessageMapper.findImagesBymessid", id);
	}
	@Override
	public Image selectImagePathByid(String id) throws Exception {
		return  (Image)dao.findForObject("MessageMapper.selectImagePathByid", id);// 查询图片路径
	}
	@Override
	public void deleteImage(String id) throws Exception {
		dao.delete("MessageMapper.deleteImage", id);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Video> findVideosBymessageid(String id) throws Exception {
		return (List<Video>)dao.findForList("MessageMapper.findVideosBymessid", id);
	}
	@Override
	public Video selectVideoPathByid(String id) throws Exception {
		return (Video)dao.findForObject("MessageMapper.selectVideoPathByid", id);// 查询视频路径
	}
	@Override
	public void deleteVideo(String id) throws Exception {
		dao.delete("MessageMapper.deleteVideo", id);
	}
	@Override
	public void updateImageById(Image image) throws Exception {
		dao.update("MessageMapper.updateImage", image);
	}
	@Override
	public void updateVideoById(Video video) throws Exception {
		dao.update("MessageMapper.updateVideo", video);
	}
	//==================更新操作图片和视频结束=========================
	//-------------扩展字段处理
	@Override
	public void toFindExtendWord(Map<String, Object> map) throws Exception {
		map.put("extendWordsList", dao.findForList("MessageMapper.selectExtendWordTopList", null));
	}
	/**
	 * 江铜前端实现接口开始>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessage> selectNewMessageByHotNew(Page page) throws Exception {
		return (List<NewMessage>)dao.findForList("MessageMapper.selectNewMessageByHotlistPage", page);
	}
	/**
	 *江心味业前端实现接口开始>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessage> selectNewMessageByexcludePage(Page page) throws Exception {
		String first=(String) dao.findForObject("MessageMapper.selectFirstMessageId", page);
		page.getPd().put("exid", first);
		return (List<NewMessage>)dao.findForList("MessageMapper.selectNewMessageByexcludelistPage", page);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessage> selectNewMessagelistPage(Page page) throws Exception {
		return (List<NewMessage>)dao.findForList("MessageMapper.selectNewMessagelistPage", page);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessage> findlistPageByColumId(Page page) throws Exception {
		return (List<NewMessage>) dao.findForList("MessageMapper.selectAllByColumIdlistPage", page);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessage> findlistStockPageByColumId(Page page) throws Exception {
		//寻找该栏目下的下级栏目
		List<ColumConfig> colums=(List<ColumConfig>)dao.findForList("MessageMapper.selectColumnByColumId", page);
		String type= page.getPd().getString("type");
		List<NewMessage> list=new ArrayList<NewMessage>();
		for (ColumConfig col : colums) {
			String columName = col.getColumName();
			if("0".equals(type) && "香港证券交易所".equals(columName)){
				page.getPd().put("thiscolum",col.getId());
				NewMessage newMessage=new NewMessage();
				newMessage.setColumid(col.getId());
				newMessage.setColumname(columName);
				list.add(newMessage);
				
			}else if("1".equals(type) && "上海证券交易所".equals(columName)){
				page.getPd().put("thiscolum",col.getId());
				NewMessage newMessage=new NewMessage();
				newMessage.setColumid(col.getId());
				newMessage.setColumname(columName);
				list.add(newMessage);
			}
		}
		return list;
	}
	/**
	 * 江铜前端实现接口结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	 */


	/**
	 * 汉能（开始）
	 * 招商加盟
	 */
	public List<Content> attractInvestmentMessage(Page page) throws Exception {
		//寻找该栏目下的下级栏目
		List<ColumConfig> colums=(List<ColumConfig>)dao.findForList("MessageMapper.selectColumnByColumId", page);
		String type= page.getPd().getString("type");
		List<Content> list=new ArrayList<Content>();
		for (ColumConfig col : colums) {
			String columName = col.getColumName();
			PageData pd = new PageData();
			pd.put("colum_id", col.getId());
			if("0".equals(type) && "加盟商扶持政策".equals(columName)){
				Content content = (Content) dao.findForObject("ContentMapper.selectCompanyProfile", pd);
				ObjectMapper objectMapper = new ObjectMapper();
				if (content!=null && content.getFiledJson() != null && !"".equals(content.getFiledJson())) {
					@SuppressWarnings("unchecked")
					List<ExtendFiledUtil> fileds = objectMapper.readValue(content.getFiledJson(), List.class);
					content.setFileds(fileds);
				}
				list.add(content);
			}else if("1".equals(type) && "加盟條件".equals(columName)){
				Content content = (Content) dao.findForObject("ContentMapper.selectCompanyProfile", pd);
				ObjectMapper objectMapper = new ObjectMapper();
				if (content!=null && content.getFiledJson() != null && !"".equals(content.getFiledJson())) {
					@SuppressWarnings("unchecked")
					List<ExtendFiledUtil> fileds = objectMapper.readValue(content.getFiledJson(), List.class);
					content.setFileds(fileds);
				}
				list.add(content);
			}else if("2".equals(type) && "加盟流程".equals(columName)){
				Content content = (Content) dao.findForObject("ContentMapper.selectCompanyProfile", pd);
				ObjectMapper objectMapper = new ObjectMapper();
				if (content!=null && content.getFiledJson() != null && !"".equals(content.getFiledJson())) {
					@SuppressWarnings("unchecked")
					List<ExtendFiledUtil> fileds = objectMapper.readValue(content.getFiledJson(), List.class);
					content.setFileds(fileds);
				}
				list.add(content);
			}
		}
		return list;
	}
	/**
	 * 汉能
	 * 人才招聘
	 */
	public List<Content> talentRecruitmentMessage(Page page) throws Exception {
		//寻找该栏目下的下级栏目
		List<ColumConfig> colums=(List<ColumConfig>)dao.findForList("MessageMapper.selectColumnByColumId", page);
		String type= page.getPd().getString("type");
		List<Content> list=new ArrayList<Content>();
		for (ColumConfig col : colums) {
			String columName = col.getColumName();
			PageData pd = new PageData();
			pd.put("colum_id", col.getId());
			if("0".equals(type) && "人才精英计划".equals(columName)){
				Content content = (Content) dao.findForObject("ContentMapper.selectCompanyProfile", pd);
				ObjectMapper objectMapper = new ObjectMapper();
				if (content!=null && content.getFiledJson() != null && !"".equals(content.getFiledJson())) {
					@SuppressWarnings("unchecked")
					List<ExtendFiledUtil> fileds = objectMapper.readValue(content.getFiledJson(), List.class);
					content.setFileds(fileds);
				}
				list.add(content);
			}
		}
		return list;
	}
	/**
	 * 汉能
	 * 查找文化信息
	 */
	public List<Content> findCultureMessage(Page page) throws Exception {
		//寻找该栏目下的下级栏目
		List<ColumConfig> colums=(List<ColumConfig>)dao.findForList("MessageMapper.selectColumnByColumId", page);
		String type= page.getPd().getString("type");
		List<Content> list=new ArrayList<Content>();
		for (ColumConfig col : colums) {
			String columName = col.getColumName();
			PageData pd = new PageData();
			pd.put("colum_id", col.getId());
			if("0".equals(type) && "企业文化".equals(columName)){
				Content content = (Content) dao.findForObject("ContentMapper.selectCompanyProfile", pd);
				ObjectMapper objectMapper = new ObjectMapper();
				if (content!=null && content.getFiledJson() != null && !"".equals(content.getFiledJson())) {
					@SuppressWarnings("unchecked")
					List<ExtendFiledUtil> fileds = objectMapper.readValue(content.getFiledJson(), List.class);
					content.setFileds(fileds);
				}
				list.add(content);
			}
		}
		return list;
	}
	/**
	 * 汉能：查找文化信息下的栏目
	 */
	public List<NewMessage> findCultureListMessage(Page page) throws Exception {
		//寻找该栏目下的下级栏目
		List<ColumConfig> colums=(List<ColumConfig>)dao.findForList("MessageMapper.selectColumnByColumId", page);
		String type= page.getPd().getString("type");
		List<NewMessage> list=new ArrayList<NewMessage>();
		for (ColumConfig col : colums) {
			String columName = col.getColumName();
			if("0".equals(type) && "企业文化".equals(columName)){
				page.getPd().put("thiscolum",col.getId());
				NewMessage newMessage=new NewMessage();
				newMessage.setColumid(col.getId());
				newMessage.setColumname(columName);
				list.add(newMessage);
			}
		}
		return list;
	}
	
	/**
	 * 置顶和推荐
	 * @param product
	 * @throws Exception
	 */
	@Override
	public void updataRecommendAndTopAndHot(NewMessage message) throws Exception {
		dao.update("MessageMapper.updataRecommendAndTopAndHotByIds", message);
		
	}
	
	@Override
	public void updateSort(PageData pd) throws Exception {
		dao.update("MessageMapper.updateSort", pd);
	}

	
	@Override
	public List<NewMessage> selectRecommendMessageByColumnIdOrTypeId(PageData pd) throws Exception {
		List<NewMessage> list= (List<NewMessage>) dao.findForList("MessageMapper.selectRecommendMessageByColumnIdOrTypeId", pd);
		/*ObjectMapper objectMapper = new ObjectMapper();
		for (NewMessage newMessage : list) {	
			if (newMessage!=null && newMessage.getValuetext() != null && !"".equals(newMessage.getValuetext())) {
				//List<ExtendFiledVo> fileds=JSON.parseArray(newMessage.getValuetext(), ExtendFiledVo.class); 
				List<ExtendFiledVo> fileds = objectMapper.readValue(newMessage.getValuetext(), List.class);
				newMessage.setFileds(fileds);
			}
			if(newMessage!=null && newMessage.getSurface_image() != null && !"".equals(newMessage.getSurface_image())){
				String id = newMessage.getSurface_image();
				Image image = (Image) dao.findForObject("MessageMapper.selectImagePathByid", id);
				newMessage.setImage(image);
			}
		}*/
		return list;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public HashMap<String, Object> selectProductsByColumnIds(Page page) throws Exception {
		List<NewMessage> proList = null;
		int totalPage = 0;
		ColumConfig cc = (ColumConfig) dao.findForObject("ColumconfigMapper.selectColumn", page);
		int count = (int) dao.findForObject("MessageMapper.selectMsgCount", page);
		if(count != 0) {
			int showCount = page.getShowCount();
			totalPage = count % showCount > 0 ? count / showCount +1 : count / showCount;
			page.setTotalPage(totalPage);
			String idsStr = "";
			List<NewMessage> ids = (List<NewMessage>) dao.findForList("MessageMapper.selectProductsByColumnIdslistPage", page);
			if(null != ids && ids.size() > 0) {
				for (NewMessage p : ids) {
					idsStr += "," + p.getId();
				}
				idsStr = idsStr.substring(1);
				proList = (List<NewMessage>) dao.findForList("MessageMapper.selectProductsByColumnIds", idsStr.split(","));
			}
		}
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("level", 2);
		resultMap.put("parientColumn", cc);
		resultMap.put("currentColumnId", page.getPd().get("columnId"));
		resultMap.put("currentPage", page.getCurrentPage());
		resultMap.put("totalPage", totalPage);
		if(null != proList && proList.size() > 0) {
			resultMap.put("status", 201);
			resultMap.put("proList", proList);
		} else {
			resultMap.put("status", 202);
		}
		return resultMap; 
	}
	@Override
	public HashMap<String, Object> selectProductsByColumnIdsTop(Page page) throws Exception {
		List<NewMessage> proList = null;
		int totalPage = 0;
		ColumConfig cc = (ColumConfig) dao.findForObject("ColumconfigMapper.selectColumn", page);
		int count = (int) dao.findForObject("MessageMapper.selectMsgCountTop", page);
		if(count != 0) {
			int showCount = page.getShowCount();
			totalPage = count % showCount > 0 ? count / showCount +1 : count / showCount;
			page.setTotalPage(totalPage);
			String idsStr = "";
			List<NewMessage> ids = (List<NewMessage>) dao.findForList("MessageMapper.selectProductsByColumnIdsToplistPage", page);
			if(null != ids && ids.size() > 0) {
				for (NewMessage p : ids) {
					idsStr += "," + p.getId();
				}
				idsStr = idsStr.substring(1);
				proList = (List<NewMessage>) dao.findForList("MessageMapper.selectProductsByColumnIds", idsStr.split(","));
			}
		}
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("level", 1);
		resultMap.put("parientColumn", cc);
		resultMap.put("currentColumnId", page.getPd().get("columnId"));
		resultMap.put("currentPage", page.getCurrentPage());
		resultMap.put("totalPage", totalPage);
		if(null != proList && proList.size() > 0) {
			resultMap.put("status", 201);
			resultMap.put("proList", proList);
		} else {
			resultMap.put("status", 202);
		}
		return resultMap; 
	}
	@Override
	public NewMessage selectProductDetailByProId(PageData pd) throws Exception {
		NewMessage pro = (NewMessage) dao.findForObject("MessageMapper.selectProductDetailByProId", pd);
		String description = pro.getDescription();
		ArrayList<String> arrayList = new ArrayList<String>();
		if(null != description && !"".equals(description)) {
			String[] split1 = description.split(",");
			for (String s1 : split1) {
				if(s1.contains("-")) {
					arrayList.add(s1);
				}
			}
		}
		pro.setArrayList(arrayList);
		//
		List<Image> pictures = pro.getPictures();
		if(null != pictures && pictures.size() > 0) {
			ArrayList<Image> otherImgs = new ArrayList<Image>();
			ArrayList<Image> eightImgs1 = new ArrayList<Image>();
			ArrayList<Image> eightImgs2 = new ArrayList<Image>();
			ArrayList<Image> eightImgs3 = new ArrayList<Image>();
			for (Image image : pictures) {
				if(null != image.getTitle() && "*".equals(image.getTitle())) {
					otherImgs.add(image);
				} else if(null != image.getTitle() && "1*".equals(image.getTitle())){
					eightImgs1.add(image);
				} else if(null != image.getTitle() && "2*".equals(image.getTitle())) {
					eightImgs2.add(image);
				} else if(null != image.getTitle() && "3*".equals(image.getTitle())) {
					eightImgs3.add(image);
				}
			}
			pro.setOtherImgs(otherImgs);
			pro.setEightImgs1(eightImgs1);
			pro.setEightImgs2(eightImgs2);
			pro.setEightImgs3(eightImgs3);
		}
		return pro; 
	}
	
	/**
	 * 根据id查询详细资讯
	 */
	@Override
	public NewMessage findDetailSinggleMessageById(String id) throws Exception {
		NewMessage newMessage = null;
		newMessage=(NewMessage) dao.findForObject("MessageMapper.selectDetailMessageById", id);
		if(newMessage!=null){
			PageData pd =new PageData();
			if(newMessage.getCount()==null || newMessage.getCount()<=Const.INT_0){
				pd.put("count", Const.INT_1);
			}else{
				pd.put("count",(newMessage.getCount())+Const.INT_1);
			}
			pd.put("id", newMessage.getId());
			dao.update("MessageMapper.updateReadCount",pd);
		}
		
		return newMessage;
	}
	@Override
	public List<NewMessage> selectRelProByProId(PageData pd) throws Exception {
		return (List<NewMessage>) dao.findForList("MessageMapper.selectRelProByProId", pd);
	}
	
	/**
	 * 根据id查询详细资讯
	 */
	@Override
	public NewMessage findMessageByTypeOrColumnid(PageData pd) throws Exception {
		return (NewMessage) dao.findForObject("MessageMapper.findMessageByTypeOrColumnid", pd);
	}

}

