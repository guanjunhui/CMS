package cn.cebest.service.system.newMessage.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.newMessage.NewMessageType;
import cn.cebest.service.system.newMessage.MyMessageService;
import cn.cebest.service.system.newMessage.MyMessageTypeService;
import cn.cebest.util.Const;
import cn.cebest.util.DelAllFile;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.Tools;
import cn.cebest.util.UuidUtil;
/**
 * 资讯类型实现类
 * @author lwt
 *
 */
@Service("MyMessageTypeService")
public class MyMessageTypeServiceImpl implements MyMessageTypeService {
	/**
	 * 根据id类型注入dao
	 */
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	/**
	 * 注入资讯的service
	 */
	@Resource(name = "MyMessageService")
	private MyMessageService messageService;
	/**
	 * 查询资讯类型列表
	 */
	@Override
	public void findMessageTypeToList(Map<String, Object> map, Page page) throws Exception {
		map.put("messageTypeList", dao.findForList("MessageTypeMapper.findMessageTypelistPage", page));
	}
	@Override
	public void toAddFind(Map<String, Object> map, PageData pd) throws Exception {
		// 1 查模板作用域
		map.put("templateList", dao.findForList("MessageTypeMapper.selectMessageTemplate", pd));
	}
	/**
	 * 添加资讯时显示所属栏目
	 */
	@Override
	public void findAllColumconfig(Map<String, Object> map,Page page) throws Exception {
		map.put("listColumn", dao.findForList("MessageTypeMapper.findAllColumconfig", page));	
	}
	/**
	 * 添加资讯时,显示所属分类
	 */
	@Override
	public void findAllMessageType(Map<String, Object> map,Page page) throws Exception {
		map.put("listMessageType", dao.findForList("MessageTypeMapper.findAllMessageType", page));	
	}
	//============================================================================================
	@Override
	public void findMessageTypeToList(Map<String, Object> map, PageData pd) throws Exception {
		// 查询所有类型
		map.put("messageTypeList", dao.findForList("MessageTypeMapper.selectmessageTypeListPage", pd));

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessageType> getTreeData(PageData pd) throws Exception {
		List<NewMessageType> topList = (List<NewMessageType>) dao.findForList("MessageTypeMapper.selectTop", pd);
		List<NewMessageType> allList = (List<NewMessageType>) dao.findForList("MessageTypeMapper.selectAll", pd);

		if (CollectionUtils.isEmpty(allList) || CollectionUtils.isEmpty(topList)) {
			return null;
		}
		for (NewMessageType top : topList) {
			this.appendChild(top, allList);
		}
		return topList;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessageType> getlistTreeData(PageData pd) throws Exception {
		List<NewMessageType> topList = (List<NewMessageType>) dao.findForList("MessageTypeMapper.selectListTop", pd);
		List<NewMessageType> allList = (List<NewMessageType>) dao.findForList("MessageTypeMapper.selectListAll", pd);

		if (CollectionUtils.isEmpty(allList) || CollectionUtils.isEmpty(topList)) {
			return null;
		}
		for (NewMessageType top : topList) {
			this.appendChild(top, allList);
		}
		return topList;
	}
/**
 *添加子节点 
 * @param root
 * @param allList
 */
	private void appendChild(NewMessageType root, List<NewMessageType> allList) {
		for (int i = 0; i < allList.size(); i++) {
			NewMessageType mt = allList.get(i);
			if (mt != null && mt.getPid().equals(root.getId())) {
				if (CollectionUtils.isEmpty(root.getChildList())) {
					root.setChildList(new ArrayList<NewMessageType>());
				}
				root.getChildList().add(mt);
				allList.remove(i);
				i--;
				this.appendChild(mt, allList);
			}
		}
	}
/**
 * 保存资讯类型
 */
	@Override
	public void save(NewMessageType message, PageData pd) throws Exception {
		// 保存资讯
		dao.save("MessageTypeMapper.insertMessageType", message);
		// 保存图片
		if ((Boolean) pd.get("flag")) {
			dao.save("MessageTypeMapper.saveMessageImage", pd);
		}
		// 保存富文本
		dao.save("MessageTypeMapper.insertMessageTXT", message);
		// 保存栏目关系
		if (message.getColumnids() != null && message.getColumnids().length > 0) {
			dao.save("MessageTypeMapper.insertMessagetype_column", message);
		}
	}
	
	/**
	 * 保存资讯类型
	 */
		@Override
		public void save(NewMessageType message) throws Exception {
			PageData pd=message.getPd();
			// 保存资讯
			dao.save("MessageTypeMapper.insertMessageType", message);
			// 保存图片
			if ((Boolean) pd.get("flag")) {
				dao.save("MessageTypeMapper.saveMessageImage", pd);
			}
			// 保存富文本
			dao.save("MessageTypeMapper.insertMessageTXT", message);
			// 保存栏目关系
			if (message.getColumnids() != null && message.getColumnids().length > 0) {
				dao.save("MessageTypeMapper.insertMessagetype_column", message);
				//添加内容分类地址栏的url信息
				dao.save("MessageTypeMapper.saveContentTypeUrlNameconfig", message);
			}
		}
	@Override
	public Map<String, Object> findMessageTypeToEdit(String id) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		//根据id查询当前资讯类型对象的详细信息
		map.put("productType", dao.findForObject("MessageTypeMapper.selectMessageTypeById", id));
		//根据id查询当前对象的封面图片
		map.put("image", dao.findForObject("MessageTypeMapper.selectSurfaceImageById", id));
		return map;
	}
	/**
	 * 更新资讯类型
	 */
	@Override
	public void update(NewMessageType message, PageData pd) throws Exception {
		if ((Boolean) pd.get("flag")) {
			if(message.getImageid()!=null && !"".equals(message.getImageid())){
				@SuppressWarnings("unchecked")
				List<PageData> list = (List<PageData>) dao.findForList("MessageTypeMapper.selectImagePath",
						new String[] { message.getId() });// 查询图片路径
				for (PageData data : list) {
					if (data != null) {
						String pach = data.getString("IMGURL");
						if (pach != null && Tools.notEmpty(pach.trim())) {
							DelAllFile.deleteFile(PathUtil.getClasspath() + Const.FILEPATHIMG + pach); // 删除图片
						}
					}

				}
				dao.update("MessageTypeMapper.updateMessageImage", pd);//修改图片信息
			}else{
				pd.put("messageImageId", UuidUtil.get32UUID());
				message.setImageid(pd.getString("messageImageId"));
				dao.save("MessageTypeMapper.saveImage", pd);// 保存图片信息
			}
			
		}
		// 删除关联关系
		// 保存栏目关系
		// 重新建立关联关系
		if (message.getColumnids() != null && message.getColumnids().length > 0) {
			dao.delete("MessageTypeMapper.deleteMessagetype_column", new String[] { message.getId() });
			dao.save("MessageTypeMapper.insertMessagetype_column", message);
		}
		dao.update("MessageTypeMapper.updateMessageTXT", message);// 修改富文本
		dao.update("MessageTypeMapper.updateMessageById", message);
	}
	
	/**
	 * 更新资讯类型
	 */
	@Override
	public void update(NewMessageType message) throws Exception {
		PageData pd = message.getPd();
		if ((Boolean) pd.get("flag")) {
			if(message.getImageid()!=null && !"".equals(message.getImageid())){
				@SuppressWarnings("unchecked")
				List<PageData> list = (List<PageData>) dao.findForList("MessageTypeMapper.selectImagePath",
						new String[] { message.getId() });// 查询图片路径
				List<String> listID = new ArrayList<>();
				for (PageData data : list) {
					if (data != null) {
						String pach = data.getString("IMGURL");
						String imageId = data.getString("IMAGE_ID");
						listID.add(imageId);
						if (pach != null && Tools.notEmpty(pach.trim())) {
							DelAllFile.deleteFile(PathUtil.getUploadPath() + Const.FILEPATHIMG + pach); // 删除图片
						}
					}

				}
				if (listID.size() > 0) {
					dao.delete("MessageTypeMapper.deleteMessageImage", listID.toArray());// 删除图片信息
				}
				dao.save("MessageTypeMapper.saveMessageImage", pd);//修改图片信息
			}else{
				pd.put("messageImageId", UuidUtil.get32UUID());
				message.setId(pd.getString("messageImageId"));
				dao.save("MessageTypeMapper.saveImage", pd);// 保存图片信息
			}
			
		}else {
			if (message.getImageid() == null || "".equals(message.getImageid())) {
				@SuppressWarnings("unchecked")
				List<PageData> list = (List<PageData>) dao.findForList("MessageTypeMapper.selectImagePath",
						new String[] { message.getId() });// 查询图片路径
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
					dao.delete("MessageTypeMapper.deleteMessageImage", listID.toArray());// 删除图片信息
				}
			}

		}
		// 删除关联关系
		// 保存栏目关系
		// 重新建立关联关系
		if (message.getColumnids() != null && message.getColumnids().length > 0) {
			dao.delete("MessageTypeMapper.deleteMessagetype_column", new String[] { message.getId() });
			dao.save("MessageTypeMapper.insertMessagetype_column", message);
		}
		//修改栏目地址栏的url信息
		Integer columUrlNameSize = (Integer)dao.findForObject("MessageTypeMapper.findContentTypeUrlNameList", message);
		if(columUrlNameSize > 0){
			dao.update("MessageTypeMapper.editContentTypeUrlNameconfig", message);
		}else{
			dao.save("MessageTypeMapper.saveContentTypeUrlNameconfig", message);
		}
		dao.update("MessageTypeMapper.updateMessageTXT", message);// 修改富文本
		dao.update("MessageTypeMapper.updateMessageById", message);
	}
	
	//删除使用的子节点层级结构
	private void appendChildIds(List<String> newIdlist,String str,List<Map<String,String>> listAll){
		for (int i=0;i<listAll.size();i++) {
			if(str.equals(listAll.get(i).get("PID"))){
				newIdlist.add(listAll.get(i).get("ID"));
				listAll.remove(i);
				i--;
			}
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void delete(String[] ids) throws Exception {
		List<String> newIdlist=new ArrayList<String>(Arrays.asList(ids));
		PageData data=new PageData();
		List<Map<String,String>> listAll = (List<Map<String,String>>) dao.findForList("MessageTypeMapper.selectAllIdPiD", data);
		for (int i=0;i<newIdlist.size();i++) {
			appendChildIds(newIdlist,newIdlist.get(i),listAll);
		}
		// 删除关联关系
		dao.delete("MessageTypeMapper.deleteMessagetype_column", newIdlist.toArray(new String[newIdlist.size()]));
		
		// 删除资讯分类的地址栏URL信息
		dao.delete("MessageTypeMapper.deleteMessagetypeUrlName", newIdlist.toArray(new String[newIdlist.size()]));
		// 删除资讯内容的地址栏URL信息
		dao.delete("MessageTypeMapper.deleteMessagetypeContentUrlName", newIdlist.toArray(new String[newIdlist.size()]));
		
		List<PageData> list = (List<PageData>) dao.findForList("MessageTypeMapper.selectImagePath", newIdlist.toArray(new String[newIdlist.size()]));// 查询图片路径
		List<String> listID = new ArrayList<>();
		for (PageData pd : list) {
			if (pd != null) {
				String imageId = pd.getString("IMAGE_ID");
				listID.add(imageId);
				String path = pd.getString("IMGURL");
				if (path != null && Tools.notEmpty(path.trim())) {
					DelAllFile.deleteFile(PathUtil.getClasspath() + Const.FILEPATHIMG + path); // 删除图片
				}
			}

		}

		if (listID.size() > 0) {
			dao.delete("MessageTypeMapper.deleteMessageImage", listID.toArray());// 删除图片信息		
		}
		List<String> listid = (List<String>) dao.findForList("MessageTypeMapper.selectMessageIds", newIdlist.toArray(new String[newIdlist.size()]));// 根据资讯类型id查询资讯id

		if (listid.size() > 0) {
			messageService.deleteMessage(listid.toArray(new String[listid.size()]));//删除资讯..............
		}
		dao.delete("MessageTypeMapper.deleteMessageTXT",  newIdlist.toArray(new String[newIdlist.size()]));// 删除富文本
		dao.delete("MessageTypeMapper.deleteMessageByIds", newIdlist.toArray(new String[newIdlist.size()]));
	}
	//删除栏目与类型之间的关系接口
	@SuppressWarnings("unchecked")
	public void deleteByColumnIds(String[] ids) throws Exception{
		//查询资讯类型ids
		List<String> listId=(List<String>) dao.findForList("MessageTypeMapper.selectIdsByColumnIds", ids);
		if(listId.size()>0){
			List<HashMap<String,Object>> list=(List<HashMap<String,Object>>) dao.findForList("MessageTypeMapper.selectCountByIds", listId.toArray(new String[listId.size()]));
			for (HashMap<String, Object> hashMap : list) {
				if((Long) hashMap.get("COUNT(*)")>1){
					
				}else{
					//删除资讯类型
					delete(new String[]{(String) hashMap.get("NEWMESSAGETYPEID")});
				}
			}
		}
		dao.delete("MessageTypeMapper.deleteByColumnIds", ids);
	}
	/**
	 *更新资讯状态
	 */
	@Override
	public void updateMessageStatus(Map<String, Object> map) throws Exception {
		dao.update("MessageTypeMapper.updateMessageTypeStatusByIds", map);
		dao.update("MessageTypeMapper.updateMessageStatusByIds", map);

	}
	/**
	 * 更新资讯类型状态
	 */
	@Override
	public void updateTypeStatus(PageData pd) throws Exception {
		dao.update("MessageTypeMapper.updateMessageTypeStatus", pd);	
	}
	/**
	 * 更新资讯类型状态
	 */
	@Override
	public void updateMessageTypeStatus(Map<String, Object> map) throws Exception {
		dao.update("MessageTypeMapper.updateTypeStatus", map);	
	}
	
	/**
	 * 根据栏目id查找所有资讯的类型
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessageType> findMessage_TypeByColumnIds(String id) throws Exception {
		List<NewMessageType> listAll= (List<NewMessageType>) dao.findForList("MessageTypeMapper.selectMessage_TypeByColumnIdTop", id);
		List<NewMessageType> listTop=new ArrayList<>();
		List<String> ids=new ArrayList<>();
		for (NewMessageType messageType : listAll) {
			ids.add(messageType.getId());
		}
		if (CollectionUtils.isEmpty(listAll)) {
			return null;
		}
		for (int i=0;i<listAll.size();i++) {
			for(int j=0;j<listAll.get(i).getChildList().size();j++){
				if(listAll.get(i).getChildList().get(j).getId() == null || listAll.get(i).getChildList().get(j).getId() == ""){
					listAll.get(i).getChildList().set(j, null);
					if(listAll.get(i).getChildList().size() == 1){
						listAll.get(i).setChildList(null);
						break;
					}
				}
			}
			if(!ids.contains(listAll.get(i).getPid())){
				this.appendChildMessageType(listAll.get(i), listAll);
				listTop.add(listAll.get(i));
			}
		}
		return listTop;
	}
	private void appendChildMessageType(NewMessageType root,List<NewMessageType> allList){
		for (int i = 0; i < allList.size(); i++) {
			NewMessageType mt = allList.get(i);
			if (mt != null && mt.getPid().equals(root.getId())) {
				if (CollectionUtils.isEmpty(root.getChildList())) {
					root.setChildList(new ArrayList<NewMessageType>());
				}
				root.getChildList().add(mt);
				this.appendChildMessageType(mt, allList);
			}
		}
	}
	/**
	 * 更新资讯类型的SORT
	 */
	@Override
	public void updateTypeSort(PageData pd) throws Exception {
		dao.update("MessageTypeMapper.updateMessageTypeSort", pd);
	}
	@Override
	public NewMessageType findTypeInfoById(String id) throws Exception {
		return (NewMessageType) dao.findForObject("MessageTypeMapper.findTypeInfoById", id);
	}
	
	/**
	 * 根据栏目id查找所有资讯的类型
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessageType> selectMessageTypeByColumId(String id) throws Exception {
		List<NewMessageType> listAll= (List<NewMessageType>) dao.findForList("MessageTypeMapper.findMessageTypeByColumnId", id);
		List<NewMessageType> listTop=new ArrayList<>();
		List<String> ids=new ArrayList<>();
		for (NewMessageType messageType : listAll) {
			ids.add(messageType.getId());
		}
		if (CollectionUtils.isEmpty(listAll)) {
			return null;
		}
		for (int i=0;i<listAll.size();i++) {
			if(!ids.contains(listAll.get(i).getPid())){
				this.appendChildMessageType(listAll.get(i), listAll);
				listTop.add(listAll.get(i));
			}
		}
		return listTop;
	}
	/**
	 * 根据栏目id查找所有资讯的类型
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NewMessageType> selectMessageTypeByColumIds(PageData pd) throws Exception {
		List<NewMessageType> listAll= (List<NewMessageType>)dao.findForList("MessageTypeMapper.findMessageTypeByColumnIds", pd);
		List<NewMessageType> listTop=new ArrayList<>();
		List<String> ids=new ArrayList<>();
		for (NewMessageType messageType : listAll) {
			ids.add(messageType.getId());
		}
		if (CollectionUtils.isEmpty(listAll)) {
			return null;
		}
		for (int i=0;i<listAll.size();i++) {
			if(!ids.contains(listAll.get(i).getPid())){
				this.appendChildMessageType(listAll.get(i), listAll);
				listTop.add(listAll.get(i));
			}
		}
		return listTop;
	}
	/**
	 * 通过类型名查找资讯类型
	 */
	@Override
	public NewMessageType selectNewMessageType(String name) throws Exception {
		return (NewMessageType)dao.findForObject("MessageTypeMapper.findMessageTypeByName", name);
	}
	
	/**
	 * 修改资讯状态
	 */
	@Override
	public void updateStatus(PageData pd) throws Exception {
			dao.update("MessageTypeMapper.updateMessageStatus", pd);	
	}
}
