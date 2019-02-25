package cn.cebest.service.system.newMessage.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.newMessage.MessageExtendWords;
import cn.cebest.service.system.newMessage.MessageExtendService;
import cn.cebest.util.PageData;
@Service
public class MessageExtendServiceImpl implements MessageExtendService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/*
	private void appendChild(ProductValueType root, List<ProductValueType> allList) {
		for (int i = 0; i < allList.size(); i++) {
			ProductValueType pt = allList.get(i);
			if (pt != null && pt.getPid().equals(root.getId())) {
				if (CollectionUtils.isEmpty(root.getChildList())) {
					root.setChildList(new ArrayList<ProductValueType>());
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
*/
	//保存字段
	@Override
	public void save(PageData pd) throws Exception {
		dao.save("MessageMapper.insertExtendWord", pd);
		
	}
	//字段展示
	@SuppressWarnings("unchecked")
	@Override
	public List<MessageExtendWords> getTree(PageData pd) throws Exception {
		List<MessageExtendWords> listTop = (List<MessageExtendWords>) dao.findForList("MessageMapper.selectExtendWordTopList", pd);
		/*List<ProductValueType> listALL = (List<ProductValueType>) dao
				.findForList("ProductMapper.selectPropertyTypeList", pd);
		if (!CollectionUtils.isEmpty(listALL) && !CollectionUtils.isEmpty(listTop)) {
			for (ProductValueType top : listTop) {
				this.appendChild(top, listALL);
			}
		}*/
		return listTop;

	}
	//字段展示
	@SuppressWarnings("unchecked")
	@Override
	public List<MessageExtendWords> getData(Page page) throws Exception {
		List<MessageExtendWords> listTop = (List<MessageExtendWords>) dao.findForList("MessageMapper.selectExtendWordlistPage", page);
		return listTop;

	}
	//编辑回显
	@Override
	public void findById(Map<String,Object> map,String id) throws Exception {
		map.put("property", dao.findForObject("MessageMapper.selectExtendWordById", id));
		
	}
	//更新字段
	@Override
	public void update(PageData pd) throws Exception {
		dao.update("MessageMapper.updateExtendWord", pd);
		
	}
	//删除字段
	@Override
	public void delete(String[] id) throws Exception {
		/*List<String> newIdlist=new ArrayList(Arrays.asList(id));
		PageData data=new PageData();
		List<Map<String,String>> listAll = (List<Map<String,String>>) dao.findForList("ProductMapper.selectAllIdPiD", data);
		for (int i=0;i<newIdlist.size();i++) {
			appendChildIds(newIdlist,newIdlist.get(i),listAll);
		}*/
		dao.delete("MessageMapper.deleteExtendWord", id);
	}
}
