package cn.cebest.service.system.employ.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.EmployField;
import cn.cebest.entity.system.Recruit;
import cn.cebest.service.system.employ.EmployService;
import cn.cebest.service.system.seo.SeoService;
import cn.cebest.util.ExtendFiledUtil;
import cn.cebest.util.PageData;

@Service
public class EmployServiceImpl implements EmployService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Autowired
	private SeoService service;
	
	@Override
	public List<PageData> listPage(Page page) throws Exception {
		return (List<PageData>) dao.findForList("EmployMapper.listPage", page);
	}

	@Override
	public void del(PageData pd) throws Exception {
		dao.delete("EmployMapper.del", pd);
		//删除招聘地址栏的url信息
		dao.delete("EmployMapper.delUrlName", pd);
	}

	@Override
	public void delAll(String[] ids) throws Exception {
		dao.delete("EmployMapper.delAll", ids);
		//删除招聘地址栏的url信息
		dao.delete("EmployMapper.delAllUrlName", ids);
	}

	@Override
	public void save(PageData pd) throws Exception {
		dao.save("EmployMapper.save", pd);
		//添加招聘地址栏的url信息
		dao.save("EmployMapper.insertTypeEmployUrlNameColumRelation", pd);
	}

	@Override
	public PageData findById(PageData pd) throws Exception {
		pd = (PageData) dao.findForObject("EmployMapper.findById", pd);
		pd.put("MASTER_ID", pd.getString("ID"));
		pd.put("ID", null);
		pd.put("seo", service.querySeoForObject(pd));
		pd.put("ID", pd.getString("MASTER_ID"));
		String string = pd.getString("FILEDJSON");
		if (string != null && !"".equals(string)) {
			ObjectMapper objectMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")                                                                      
			List<ExtendFiledUtil> fileds = objectMapper.readValue(string, List.class);
			pd.put("fileds", fileds);
		}
		return pd;
	}

	@Override
	public void edit(PageData pd) throws Exception {
		dao.update("EmployMapper.edit", pd);
		
		Integer contentUrlNameSize = (Integer)dao.findForObject("EmployMapper.findContentUrlNameList", pd);
		if(contentUrlNameSize > 0){
			//修改招聘地址栏的url信息
			dao.update("EmployMapper.editUrlName", pd);
		}else{
			//添加招聘地址栏的url信息
			dao.save("EmployMapper.insertTypeEmployUrlNameColumRelation", pd);
		}
		
	}

	@Override
	public void recommend(PageData pd) throws Exception {
		dao.update("EmployMapper.recommend", pd);
	}

	@Override
	public void top(PageData pd) throws Exception {
		dao.update("EmployMapper.top", pd);
	}

	@Override
	public void deleteByColumnIds(String[] ids) throws Exception {
		dao.delete("EmployMapper.deleteByColumnIds", ids);
	}

	@Override
	public List<PageData> findTopList() throws Exception {
		return (List<PageData>) dao.findForList("EmployMapper.findTopContentList", null);
	}

	@Override
	public PageData findTemplatePachById(String contentId) throws Exception{ 
		return (PageData) dao.findForObject("EmployMapper.selectTemplatePachById", contentId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Recruit> findlistPageAllByColumId(Page page) throws Exception {
		List<Recruit> list=(List<Recruit>) dao.findForList("EmployMapper.selectAllByColumIdlistPage", page);
		if(CollectionUtils.isNotEmpty(list)){
			for (Recruit recruit : list) {
				String string=recruit.getFiledJson();
				if (string != null && !"".equals(string)) {
					ObjectMapper objectMapper = new ObjectMapper();
					@SuppressWarnings("unchecked")                                                                      
					List<ExtendFiledUtil> fileds = objectMapper.readValue(string, List.class);
					recruit.setFileds(fileds);
				}
			}
		}
		return list ;
	}

	/**
	 * 查询当前站点下 栏目类型=4(招聘栏目)的所有栏目
	 * @author liu
	 */
	@Override
	public List<PageData> findColum(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (List<PageData>) dao.findForList("EmployMapper.selectColum", pd);
	}

	/**
	 * 通过招聘栏目id查询有关数据
	 * @author liu
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> selectlistPageByColumIDD(Page page) throws Exception {
		List<PageData> list=this.listPage(page);
		if(list==null || list.size()==0){
			String colum_id=page.getPd().getString("colum_id");
			List<Map<String,String>> mapColumIds= (List<Map<String, String>>) dao.findForList("EmployMapper.selectallcolum_ids", page);
			List<String> newCloumIds=new ArrayList<>();
			newCloumIds.add(colum_id);
			for (Map<String, String> map : mapColumIds) {
				if(newCloumIds.contains(map.get("PID"))){
					newCloumIds.add(map.get("ID"));
				}
			}
//			page.getPd().put("IDS", newCloumIds.toArray(new String[newCloumIds.size()]));
			page.getPd().put("list", newCloumIds);
			List<String>ids= (List<String>) dao.findForList("EmployMapper.selectByColumIDDlistPage", page);
			if(ids==null || ids.size()==0){
				return null;
			}
			list=(List<PageData>) dao.findForList("EmployMapper.selectTest", ids);
		}
		return list;
	}
	/**
	 * 江铜开始
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Recruit> findlistPageByColumId(Page page) throws Exception {
		//寻找该栏目下的下级栏目
		List<ColumConfig> colums=(List<ColumConfig>)dao.findForList("EmployMapper.selectColumnByColumId", page);
		String type= page.getPd().getString("type");
		List<Recruit> list=null;
		for (ColumConfig col : colums) {
			String columName = col.getColumName();
			if("1".equals(type) && "社会招聘".equals(columName)){
				page.getPd().put("thiscolum",col.getId());
				list=(List<Recruit>) dao.findForList("EmployMapper.selectAllByColumIdlistPage", page);
				for (Recruit recruit : list) {
					recruit.setColumid(col.getId());
				}
			}else if("0".equals(type) && "校园招聘".equals(columName)){
				page.getPd().put("thiscolum",col.getId());
				list=(List<Recruit>) dao.findForList("EmployMapper.selectAllByColumIdlistPage", page);
				for (Recruit recruit : list) {
					recruit.setColumid(col.getId());
				}
			}
		}
		return list;
	}	
	
	
	@Override
	public void updateStatusByIds(PageData pd) throws Exception {
		dao.update("EmployMapper.updateStatusByIds", pd);
	}
	
	@Override
	public void hot(PageData pd) throws Exception {
		dao.update("EmployMapper.hot", pd);
	}
	
	@Override
	public void updateSort(PageData pd) throws Exception {
		dao.update("EmployMapper.updateSort", pd);
	}
	
	@Override
	public List<EmployField> findlistEmployFields(Page page) throws Exception {
		return (List<EmployField>) dao.findForList("EmployMapper.listEmployFields", page);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Recruit findRecruitByTypeOrColumnid(PageData pd) throws Exception {
		return (Recruit)dao.findForObject("EmployMapper.findRecruitByTypeOrColumnid", pd);
	}
}
