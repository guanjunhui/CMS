package cn.cebest.service.system.columconfig.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.Tree;
import cn.cebest.entity.bo.TypeInfoBo;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Template;
import cn.cebest.entity.system.download.FileType;
import cn.cebest.entity.system.newMessage.NewMessageType;
import cn.cebest.entity.system.product.Product_Type;
import cn.cebest.service.information.pictures.PicturesManager;
import cn.cebest.service.system.columconfig.ColumGroupService;
import cn.cebest.service.system.columconfig.ColumconfigService;
import cn.cebest.service.system.content.content.ContentService;
import cn.cebest.service.system.download.FileResourceService;
import cn.cebest.service.system.download.FileTypeService;
import cn.cebest.service.system.employ.EmployService;
import cn.cebest.service.system.newMessage.MyMessageService;
import cn.cebest.service.system.newMessage.MyMessageTypeService;
import cn.cebest.service.system.product.ProductService;
import cn.cebest.service.system.product.ProductTypeService;
import cn.cebest.service.system.seo.SeoService;
import cn.cebest.service.web.banner.BannerManagerService;
import cn.cebest.service.web.video.VideoManager;
import cn.cebest.util.Const;
import cn.cebest.util.PageData;

@Service("columconfigService")
public class ColumconfigServiceImpl implements ColumconfigService {
	public static final String COLUMTYPE = "columType";
	public static final String COLUMID = "columId";
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Autowired
	private ColumGroupService columGroupService;
	
	@Autowired
	private PicturesManager picturesService;
	
	@Autowired
	private VideoManager videoService;

	@Autowired
	private SeoService seoService;
	
	@Autowired
	private ProductTypeService productTypeService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ContentService contentService;
	
	@Autowired
	private BannerManagerService bannerManagerService;

	@Autowired
	private MyMessageTypeService myMessageTypeService;

	@Autowired
	private MyMessageService mymessageService;
	
	@Autowired
	private EmployService employService;

	@Autowired
	private FileTypeService fileTypeService;

	@Autowired
	private FileResourceService fileResourceService;
	
	@Override
	public List<PageData> findColumconfiglistPage(Page page) throws Exception {
		return (List<PageData>) dao.findForList("ColumconfigMapper.findColumconfiglistPage", page);
	}
	
	@Override
	public List<PageData> columconfiglistPage(Page page) throws Exception {
		return (List<PageData>) dao.findForList("ColumconfigMapper.columconfiglistPage", page);
	}

	@Override
	public void delColumconfig(PageData pd) throws Exception {
		dao.update("ColumconfigMapper.delete", pd);
	}

	@Override
	public void updateAllColumconfig(String[] arrayCOLUMCONFIG_IDS) throws Exception {
		dao.update("ColumconfigMapper.updateAllColumconfig", arrayCOLUMCONFIG_IDS);
	}

	@Override
	public void saveColumconfig(PageData pd) throws Exception {
		dao.save("ColumconfigMapper.saveColumconfig", pd);
		
		//添加栏目地址栏的url信息
		dao.save("ColumconfigMapper.saveColumUrlNameconfig", pd);
	}

	@Override
	public PageData findColumconfigById(PageData pd) throws Exception {
		PageData columconfigUrlName = (PageData)dao.findForObject("ColumconfigMapper.findColumconfigUrlNameById", pd);
		PageData findForObject = (PageData)dao.findForObject("ColumconfigMapper.findColumconfigById", pd);
		if(columconfigUrlName != null){
			findForObject.put("COLUM_URLNAME", columconfigUrlName.get("COLUM_URLNAME"));
		}
		return findForObject;
	}

	@Override
	public void editColumconfig(PageData pd) throws Exception {
		dao.update("ColumconfigMapper.editColumconfig", pd);
		
		//修改栏目地址栏的url信息
		Integer columUrlNameSize = (Integer)dao.findForObject("ColumconfigMapper.findColumUrlNameList", pd);
		if(columUrlNameSize > 0){
			dao.update("ColumconfigMapper.editColumUrlNameconfig", pd);
		}else{
			dao.save("ColumconfigMapper.saveColumUrlNameconfig", pd);
		}
	}

	@Override
	public void auditColumconfig(PageData pd) throws Exception {
		dao.update("ColumconfigMapper.auditColumconfig", pd);
	}

	@Override
	public List<ColumConfig> findAllList(PageData pd) throws Exception {
		return (List<ColumConfig>) dao.findForList("ColumconfigMapper.findAllList", pd);
	}

	@Override
	public List<ColumConfig> findTopList(PageData pd) throws Exception {
		return (List<ColumConfig>) dao.findForList("ColumconfigMapper.findTopList", pd);
	}

	@Override
	public List<PageData> findAllTree(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("ColumconfigMapper.findAllTree", pd);
	}

	@Override
	public List<PageData> findTopTree(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("ColumconfigMapper.findTopTree", pd);
	}

	@Override
	public List<PageData> findAssignTypeAllTree(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("ColumconfigMapper.findAssignTypeAllTree", pd);
	}

	@Override
	public List<PageData> findAssignTypeTopTree(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("ColumconfigMapper.findAssignTypeTopTree", pd);
	}

	@Override
	public List<PageData> columconfigAllList(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("ColumconfigMapper.columconfigAllList", pd);
	}

	@Override
	public List<Tree> findTopAndChildTree(PageData pd) throws Exception {
		String selfId = pd.getString("ID");
		List<PageData> allList = this.findAllTree(pd);
		// List<PageData> topList=this.findTopTree(pd);
		pd.put("SITE_ID", pd.getString("SITEID"));
		List<PageData> group = columGroupService.findAll(pd);
		if (CollectionUtils.isEmpty(allList) || CollectionUtils.isEmpty(group)) {
			return null;
		}

		List<String> ids = new ArrayList<>();
		for (int i = 0; i < allList.size(); i++) {
			ids.add(allList.get(i).getString("ID"));
		}
		List<Tree> columnList = new ArrayList<Tree>();
		for (int i = 0; i < allList.size(); i++) {
			if (!ids.contains(allList.get(i).getString("PARENTID"))) {
				Tree tree = convert(allList.get(i));
				columnList.add(tree);
				this.appendChildTree(tree, allList, selfId);
			}
		}
		List<Tree> resultList = new ArrayList<Tree>();
		for (PageData pageData : group) {
			Tree t = convertGorup(pageData);
			resultList.add(t);
			for (Tree tree : columnList) {
				if (pageData.getString("ID").equals(tree.getGroupId())) {
					if (CollectionUtils.isEmpty(t.getChildList())) {
						t.setChildList(new ArrayList<Tree>());
					}
					t.getChildList().add(tree);
				}
			}
		}
		return resultList;
	}

	@Override
	public List<Tree> findTopAndChildTreeByGroupId(PageData pd) throws Exception {
		String selfId = pd.getString("ID");
		List<PageData> allList = this.findAllTree(pd);
		pd.put("SITE_ID", pd.getString("SITEID"));
		if (CollectionUtils.isEmpty(allList)) {
			return null;
		}

		List<String> ids = new ArrayList<>();
		for (int i = 0; i < allList.size(); i++) {
			ids.add(allList.get(i).getString("ID"));
		}
		List<Tree> columnList = new ArrayList<Tree>();
		for (int i = 0; i < allList.size(); i++) {
			if (!ids.contains(allList.get(i).getString("PARENTID"))) {
				Tree tree = convert(allList.get(i));
				columnList.add(tree);
				this.appendChildTree(tree, allList, selfId);
			}
		}
		return columnList;
	}

	private void appendChildTree(Tree config, List<PageData> allList, String selfId) {
		for (int i = 0; i < allList.size(); i++) {
			PageData po = allList.get(i);
			if (StringUtils.isNotEmpty(selfId)
					&& (selfId.equals(po.getString("PARENTID")) || selfId.equals(po.getString("ID")))) {
				continue;
			}
			if (config.getId().equals(po.getString("PARENTID"))) {
				if (CollectionUtils.isEmpty(config.getChildList())) {
					config.setChildList(new ArrayList<Tree>());
				}
				Tree tree = convert(po);
				tree.setChildLevel(config.getChildLevel() + 1);
				config.getChildList().add(tree);
//				allList.remove(i);
//				i--;
				this.appendChildTree(tree, allList, selfId);
			}
		}
	}

	private void appendChildTrees(Tree config, List<PageData> allList) {
		for (int i = 0; i < allList.size(); i++) {
			PageData po = allList.get(i);
			if (config.getId().equals(po.getString("PARENTID"))) {
				if (CollectionUtils.isEmpty(config.getChildList())) {
					config.setChildList(new ArrayList<Tree>());
				}
				Tree tree = convert(po);
				tree.setChildLevel(config.getChildLevel() + 1);
				config.getChildList().add(tree);
				// allList.remove(i);
				// i--;
				this.appendChildTrees(tree, allList);
			}
		}
	}
	
	private void appendChildTrees_Po(Tree config, List<ColumConfig> allList) {
		for (int i = 0; i < allList.size(); i++) {
			ColumConfig po = allList.get(i);
			if (config.getId().equals(po.getParentid())) {
				if (CollectionUtils.isEmpty(config.getChildList())) {
					config.setChildList(new ArrayList<Tree>());
				}
				Tree tree = convert(po);
				tree.setChildLevel(config.getChildLevel() + 1);
				config.getChildList().add(tree);
				//allList.remove(i);
				//i--;
				this.appendChildTrees_Po(tree, allList);
			}
		}
	}
	
	
	
	
	
	public Tree convertColumConfig(ColumConfig columConfig) {
		Tree tree = new Tree();
		tree.setId(columConfig.getId());
		tree.setName(columConfig.getColumName());
		tree.setPid(columConfig.getParentid());
		tree.setText(columConfig.getColumSubname());
		tree.setGroupId(columConfig.getColumGroupId());
		tree.setType(Const.NODE_TYPE_1);
		tree.getAttribute().put(COLUMTYPE, columConfig.getColumType());
		return tree;
	}

	public Tree convert(PageData pd) {
		Tree tree = new Tree();
		tree.setId(pd.getString("ID"));
		tree.setName(pd.getString("COLUM_NAME"));
		tree.setPid(pd.getString("PARENTID"));
		tree.setText(pd.getString("COLUM_SUBNAME"));
		tree.setGroupId(pd.getString("COLUMGROUP_ID"));
		tree.setType(Const.NODE_TYPE_1);
		tree.getAttribute().put(COLUMTYPE, pd.getString("COLUM_TYPE"));
		return tree;
	}
	
	public Tree convert(ColumConfig columConfig) {
		Tree tree = new Tree();
		tree.setId(columConfig.getId());
		tree.setName(columConfig.getColumName());
		tree.setPid(columConfig.getParentid());
		tree.setText(columConfig.getColumSubname());
		tree.setGroupId(columConfig.getColumGroupId());
		tree.setType(Const.NODE_TYPE_1);
		tree.getAttribute().put(Const.PER_NODETYPE, columConfig.getNodeType());
		tree.getAttribute().put(COLUMTYPE, columConfig.getColumType());
		return tree;
	}

	public Tree convertGorup(PageData pd) {
		Tree tree = new Tree();
		tree.setId(pd.getString("ID"));
		tree.setName(pd.getString("COLUM_GROUPNAME"));
		tree.setType(Const.NODE_TYPE_0);
		tree.getAttribute().put(Const.PER_NODETYPE, Const.PER_NODE_TYPE_3);
		return tree;
	}

	@Override
	public List<ColumConfig> findTopAndChildList(PageData pd) throws Exception {
		List<ColumConfig> allList = this.findAllList(pd);
		List<String> ids = new ArrayList<>();
		for (ColumConfig columConfig : allList) {
			ids.add(columConfig.getId());
		}
		if (CollectionUtils.isEmpty(allList)) {
			return null;
		}
		List<ColumConfig> resultList = new ArrayList<ColumConfig>();
		for (ColumConfig top : allList) {
			if (!ids.contains(top.getParentid())) {
				resultList.add(top);
				this.appendChilds(top, allList);
				//添加每个栏目里面的分类集合
				if (CollectionUtils.isNotEmpty(top.getSubConfigList())) {
					for (ColumConfig colum : top.getSubConfigList()) {
						this.setType(colum);
					}
				}
			}
		}
		return resultList;
	}

	private void appendChilds(ColumConfig config, List<ColumConfig> allList) {
		for (int i = 0; i < allList.size(); i++) {
			ColumConfig po = allList.get(i);
			if (po.getParentid().equals(config.getId())) {
				if (CollectionUtils.isEmpty(config.getSubConfigList())) {
					config.setSubConfigList(new ArrayList<ColumConfig>());
				}
				po.setChildLevel(config.getChildLevel() + Const.INT_1);
				config.getSubConfigList().add(po);

				this.appendChilds(po, allList);
			}
		}
	}

	private void appendChild(ColumConfig config, List<ColumConfig> allList) {
		for (int i = 0; i < allList.size(); i++) {
			ColumConfig po = allList.get(i);
			if (po.getParentid().equals(config.getId())) {
				if (CollectionUtils.isEmpty(config.getSubConfigList())) {
					config.setSubConfigList(new ArrayList<ColumConfig>());
				}
				po.setChildLevel(config.getChildLevel() + Const.INT_1);
				config.getSubConfigList().add(po);
//				allList.remove(i);
//				i--;
				this.appendChild(po, allList);
			}
		}
	}

	@Override
	public List<Tree> findAssignTypeTree(PageData pd) throws Exception {
		//获取栏目组
		List<PageData> allList = this.findAssignTypeAllTree(pd);
		Set<String> groupIds = new HashSet<String>();
		if (CollectionUtils.isEmpty(allList)) {
			return null;
		}
		List<Tree> tmpList = new ArrayList<Tree>();
		List<String> ids = new ArrayList<>();
		for (PageData top : allList) {
			ids.add(top.getString("ID"));
			groupIds.add(top.getString("COLUMGROUP_ID"));
		}
		if (CollectionUtils.isEmpty(allList)) {
			return null;
		}
		for (int i = 0; i < allList.size(); i++) {
			// 此节点的父级节点如果不在当前节点集合之内，
			// 说明获得的当前节点集合内，此节点既是顶级节点。
			PageData item = allList.get(i);
			if (!ids.contains(item.getString("PARENTID"))) {
				Tree tree=convert(allList.get(i));
				// 追加此节点的所有孩子节点
				this.appendChildTrees(tree, allList);
				tmpList.add(tree);
			}
		}
		List<Tree> resultList = new ArrayList<Tree>();
		//组装栏目组与栏目的从属关系
		for (String groupId : groupIds) {
			PageData group = (PageData) dao.findForObject("ColumGroupMapper.findById", new PageData("ID", groupId));
			if(group==null) continue;
			Tree tree = convertGorup(group);
			List<Tree> childList = new ArrayList<Tree>();
			for (Tree po : tmpList) {
				if (tree.getId().equals(po.getGroupId())) {
					childList.add(po);
				}
				tree.setChildList(childList);
			}
			resultList.add(tree);
		}
		return resultList;
	}

	@Override
	public List<ColumConfig> findAssignTypeAllColums(PageData pd) throws Exception {
		return (List<ColumConfig>) dao.findForList("ColumconfigMapper.findAssignTypeAllColums", pd);
	}

	@Override
	public List<ColumConfig> findAssignTypeTopColums(PageData pd) throws Exception {
		return (List<ColumConfig>) dao.findForList("ColumconfigMapper.findAssignTypeTopColums", pd);
	}

	@Override
	public List<ColumConfig> findAssignTypeTopAndChildList(PageData pd) throws Exception {
		List<ColumConfig> allList = this.findAssignTypeAllColums(pd);
		List<ColumConfig> topList = this.findAssignTypeTopColums(pd);
		if (CollectionUtils.isEmpty(allList) || CollectionUtils.isEmpty(topList)) {
			return null;
		}
		List<ColumConfig> resultList = new ArrayList<ColumConfig>();
		for (ColumConfig top : topList) {
			resultList.add(top);
			this.appendChild(top, allList);
		}
		return topList;
	}

	@Override
	public void delAll(String[] ids) throws Exception {
		dao.delete("ColumconfigMapper.delAll", ids);
		dao.delete("ColumconfigMapper.delUrlNmaeAll", ids);
	}
		
	@Override
	public void delContentAll(String[] ids) throws Exception {
		dao.delete("ColumconfigMapper.delContentUrlNmaeAll", ids);
	}

	@Override
	public List<PageData> findAllIds(String siteId) throws Exception {
		return (List<PageData>) dao.findForList("ColumconfigMapper.findAllIds", siteId);
	}

	@Override
	public List<String> findSelfAndChildIds(String parentId, String siteId) throws Exception {
		List<PageData> childrens = this.findAllIds(siteId);
		List<String> childIds = new ArrayList<String>();
		childIds.add(parentId);
		if (childrens != null && !childrens.isEmpty()) {
			this.eachChildIds(parentId, childIds, childrens);
		}
		return childIds;
	}

	public void eachChildIds(String id, List<String> childIds, List<PageData> childrens) {
		for (int i = 0; i < childrens.size(); i++) {
			PageData pd = childrens.get(i);
			if (id.equals(pd.getString("PARENTID"))) {
				childIds.add(pd.getString("ID"));
				this.eachChildIds(pd.getString("ID"), childIds, childrens);
			}
		}
	}

	@Override
	public void updateColumSort(List<ColumConfig> updateList) throws Exception {
		dao.batchUpdate("ColumconfigMapper.updateColumSort", updateList);
	}

	@Override
	public ColumConfig findColumconfigPoById(PageData pd) throws Exception {
		return (ColumConfig) dao.findForObject("ColumconfigMapper.findColumconfigPoById", pd);
	}

	@Override
	public int getIncludeColumCountByColumGroupId(String columGroupId) throws Exception {

		return (int) dao.findForObject("ColumconfigMapper.findIncludeColumCountByColumGroupId", columGroupId);
	}

	@Override
	public List<PageData> findColumconfigByPid(PageData pd) throws Exception {

		return (List<PageData>) dao.findForList("ColumconfigMapper.findColumconfigByPid", pd);
	}
	// 江铜集团前端显示页面接口开始====================================>

	@Override
	public ColumConfig selectColumnById(PageData pd) throws Exception {
		return (ColumConfig) dao.findForObject("ColumconfigMapper.findColumconfigByColumnId", pd);
	}
	// 江铜集团前端显示页面接口结束====================================>
	/*private void appendChildTrees_Po(Tree config, List<ColumConfig> allList) {
		for (int i = 0; i < allList.size(); i++) {
			ColumConfig po = allList.get(i);
			if (config.getId().equals(po.getParentid())) {
				if (CollectionUtils.isEmpty(config.getChildList())) {
					config.setChildList(new ArrayList<Tree>());
				}
				Tree tree = convertColumConfig(po);
				tree.setChildLevel(config.getChildLevel() + 1);
				config.getChildList().add(tree);
				//allList.remove(i);
				//i--;
				this.appendChildTrees_Po(tree, allList);
			}
		}
	}*/
	@Override
	public List<Tree> findTopAndGroupTree(PageData pd) throws Exception {
		//获取栏目组
		List<ColumConfig> allList = this.findAllList(pd);
		Set<String> groupIds = new HashSet<String>();
		if (CollectionUtils.isEmpty(allList)) {
			return null; 
		}
		List<Tree> tmpList = new ArrayList<Tree>();
		List<String> ids = new ArrayList<>();
		for (ColumConfig top : allList) {
			ids.add(top.getId());
			groupIds.add(top.getColumGroupId());
		}
		if (CollectionUtils.isEmpty(allList)) {
			return null;
		}
		
		for (int i = 0; i < allList.size(); i++) {
			// 此节点的父级节点如果不在当前节点集合之内，
			// 说明获得的当前节点集合内，此节点既是顶级节点。
			ColumConfig item = allList.get(i);
			if (!ids.contains(item.getParentid())) {
				Tree tree = convert(allList.get(i));
				//追加此节点的所有孩子节点
				this.appendChildTrees_Po(tree, allList);
				tmpList.add(tree);
			}
		}
		
		List<Tree> resultList = new ArrayList<Tree>();
		//组装栏目组与栏目的从属关系
		for (String groupId : groupIds) {
			PageData group = (PageData) dao.findForObject("ColumGroupMapper.findById", new PageData("ID", groupId));
			if(group==null) continue;
			Tree tree = convertGorup(group);
			resultList.add(tree);
			List<Tree> childList = new ArrayList<Tree>();
			tree.setChildList(childList);
			for (Tree po : tmpList) {
				if (groupId.equals(po.getGroupId())) {
					childList.add(po);
				}
			}
		}
		return resultList;
	}

	@Override
	public List<Tree> findPermGroupAndColumTree(PageData pd) throws Exception {
		//获取栏目
		@SuppressWarnings("unchecked")
		List<ColumConfig> allList = (List<ColumConfig>) dao.findForList("ColumconfigMapper.findPermAllList", pd);
		//List<ColumConfig> allList = (List<ColumConfig>) dao.findForList("ColumconfigMapper.findColumAllList", pd);
		//Set<String> groupIds = new HashSet<String>();
		if (CollectionUtils.isEmpty(allList)) {
			return null; 
		}
		List<Tree> tmpList = new ArrayList<Tree>();
		List<String> ids = new ArrayList<>();
		for (ColumConfig top : allList) {
			ids.add(top.getId());
			//groupIds.add(top.getColumGroupId());
		}
		if (CollectionUtils.isEmpty(allList)) {
			return null;
		}
		
		for (int i = 0; i < allList.size(); i++) {
			// 此节点的父级节点如果不在当前节点集合之内，
			// 说明获得的当前节点集合内，此节点既是顶级节点。
			ColumConfig item = allList.get(i);
			if (!ids.contains(item.getParentid())) {
				Tree tree = convertColumConfig(allList.get(i));
				//追加此节点的所有孩子节点
				this.appendChildTrees_Po(tree, allList);
				tmpList.add(tree);
			}
		}
		
		List<Tree> resultList = new ArrayList<Tree>();
		
		List<PageData> groupList= columGroupService.findPermAllList(pd);
		if(CollectionUtils.isEmpty(groupList)) return null;
		//组装栏目组与栏目的从属关系
		for (PageData group : groupList) {
			Tree tree = convertGorup(group);
			resultList.add(tree);
			List<Tree> childList = new ArrayList<Tree>();
			tree.setChildList(childList);
			for (Tree po : tmpList) {
				String groupId=group.getString("ID");
				if (groupId.equals(po.getGroupId())) {
					childList.add(po);
				}
			}
		}
		return resultList;
	}
	
	@Override
	public List<ColumConfig> findAllListByAssignedColum(PageData pd) throws Exception {
		//查询本级栏目
		ColumConfig self=this.findColumconfigPoById(pd);
		//查询所有栏目
		List<ColumConfig> allList = this.findAllList(pd);
		
		List<ColumConfig> resultList = new ArrayList<ColumConfig>();

		List<ColumConfig> subConfigList = new ArrayList<ColumConfig>();
		
		for (ColumConfig po : allList) {
			if (self.getId().equals(po.getParentid())) {
				subConfigList.add(po);
				po.setChildLevel(Const.INT_1);
				this.appendChilds(po, allList);
			}
		}
		boolean flag_1=true;
		if(self.getTemplate()!=null && pd.containsKey("TEM_TYPE") 
				&& StringUtils.isNotBlank(pd.getString("TEM_TYPE"))
				&& !pd.get("TEM_TYPE").equals(self.getTemplate().getTemType()))
			flag_1=false;
		
		boolean flag_2=true;
		if(pd.containsKey("COLUM_NAME") && StringUtils.isNotBlank(pd.getString("COLUM_NAME")) 
				&& self.getColumName().indexOf(pd.getString("COLUM_NAME")) == -1)
			flag_2=false;
		
		if(flag_1&&flag_2){
			resultList.add(self);
			self.setSubConfigList(subConfigList);
		} else {
			resultList=subConfigList;
		}
		return resultList;
	}
	
	@Override
	public List<ColumConfig> findSelfAndChildList(PageData pd) throws Exception {
		//查询本级栏目
		ColumConfig self=this.findColumconfigPoById(pd);
		if(self==null) return null;
		//查询所有栏目
		List<ColumConfig> allList = this.findAllList(pd);
		
		List<ColumConfig> resultList = new ArrayList<ColumConfig>();
		resultList.add(self);
		List<ColumConfig> subConfigList = new ArrayList<ColumConfig>();
		self.setSubConfigList(subConfigList);
		for (ColumConfig po : allList) {
			if (self.getId().equals(po.getParentid())) {
				subConfigList.add(po);
				po.setChildLevel(Const.INT_1);
				this.appendChilds(po, allList);
			}
		}
		return resultList;
	}
	
	@Override
	public Template findTemplateDetailByColumId(String id) throws Exception {
		return (Template) dao.findForObject("ColumconfigMapper.findTemplateDetailByColumId", id);
	}
	
	@Override
	public ColumConfig findColumDetailByColumId(String columId) throws Exception {
		return (ColumConfig) dao.findForObject("ColumconfigMapper.findColumDetailByColumId", columId);
	}
	
	@Override
	public List<ColumConfig> findColumList() throws Exception {
		return (List<ColumConfig>) dao.findForList("ColumconfigMapper.findColumList", "");
	}
	
	@Override
	public List<Tree> findColumAndTypeTree(PageData pd) throws Exception {
		String selfId = pd.getString("ID");
		//获取所有栏目
		List<PageData> allList = this.findAllTree(pd);
		pd.put("SITE_ID", pd.getString("SITEID"));
		//获取栏目组
		List<PageData> group = columGroupService.findAll(pd);
		if (CollectionUtils.isEmpty(allList) || CollectionUtils.isEmpty(group)) {
			return null;
		}

		List<String> ids = new ArrayList<>();
		for (int i = 0; i < allList.size(); i++) {
			ids.add(allList.get(i).getString("ID"));
		}
		//追加栏目的孩子节点
		List<Tree> columnList = new ArrayList<Tree>();
		for (int i = 0; i < allList.size(); i++) {
			if (!ids.contains(allList.get(i).getString("PARENTID"))) {
				Tree tree = convert(allList.get(i));
				columnList.add(tree);
				this.appendChildTree(tree, allList, selfId);
			}
		}
		//组装栏目组与栏目的从属关系
		List<Tree> resultList = new ArrayList<Tree>();
		for (PageData pageData : group) {
			Tree t = convertGorup(pageData);
			resultList.add(t);
			for (Tree tree : columnList) {
				if (pageData.getString("ID").equals(tree.getGroupId())) {
					if (CollectionUtils.isEmpty(t.getChildList())) {
						t.setChildList(new ArrayList<Tree>());
					}
					t.getChildList().add(tree);
				}
			}
		}
		return resultList;
	}
	
	@Override
//	@Transactional(rollbackFor = Exception.class)
	public void deleteColum(String parentId, String siteId) throws Exception {
		List<String> columIds=this.findSelfAndChildIds(parentId, siteId);
		String[] ids=columIds.toArray(new String[columIds.size()]);
		//删除栏目图片
		this.picturesService.delPicutre(ids);
		this.videoService.delVideos(ids);
		//根据栏目Id删除SEO
		PageData pd = new PageData("MASTER_ID", ids);
		//删除栏目与栏目组的关系.
		dao.delete("ColumconfigMapper.delAll", ids);
		this.seoService.deleteSeo(pd);
		//删除栏目与内容的映射关系
		this.productTypeService.deleteByColumnIds(ids);
		this.productService.deleteByColumnIds(ids);
		this.contentService.deleteByColumnIds(ids);
		this.bannerManagerService.deleteByColumnIds(ids);
		this.myMessageTypeService.deleteByColumnIds(ids);
		this.mymessageService.deleteMessages(ids);
		this.employService.deleteByColumnIds(ids);
		this.fileTypeService.deleteByColumnIds(ids);
		this.fileResourceService.deleteByColumnIds(ids);
	}
	
	@Override
	public void updateColumIndexStatus(PageData pd) throws Exception {
		dao.update("ColumconfigMapper.updateColumIndexStatus", pd);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> findColumconfigByPidPage(Page page) throws Exception {

		return (List<PageData>) dao.findForList("ColumconfigMapper.findColumconfigByPidlistPage", page);
	}
	


	@Override
	public List<ColumConfig> selectSubColumnListByParientId(PageData pd) throws Exception {
		return (List<ColumConfig>) dao.findForList("ColumconfigMapper.selectSubColumnListByParientId", pd);
	}

	@Override
	public List<ColumConfig> listAllForCopy(String siteID) throws Exception {
		return (List<ColumConfig>) dao.findForList("ColumconfigMapper.listAllForCopy", siteID);
	}
	
	@Override
	public void insertBatchColumn(List<ColumConfig> list) throws Exception {		
		dao.batchSave("ColumconfigMapper.insertBatch", list);
	}
	@Override
	public void deleteBeforCopy(String siteID) throws Exception{
		dao.delete("ColumconfigMapper.deleteBeforCopy", siteID);
	}

	@Override

	public ColumConfig selectColumnIdByName(String columnName) throws Exception {
		
		return (ColumConfig) dao.findForObject("ColumconfigMapper.selectColumnIdByName", columnName);
	}


	public PageData findColumUrlNameconfigById(String skipPath) throws Exception {
		return (PageData) dao.findForObject("ColumconfigMapper.findColumUrlNameconfigById", skipPath);
	}
	
	@Override
	public PageData findContentUrlNameconfigById(PageData pd) throws Exception {
		return (PageData) dao.findForObject("ColumconfigMapper.findContentUrlNameconfigById", pd);
	}

	
	// 查询栏目下所有的分类
	public void setType(ColumConfig colum) throws Exception {
		List<TypeInfoBo> resultTypeList = new ArrayList<TypeInfoBo>();
		switch (colum.getColumType()) {
		case Const.TEMPLATE_TYPE_2:// 资讯模板
			List<NewMessageType> messageTypeList = myMessageTypeService.findMessage_TypeByColumnIds(colum.getId());
			this.convertMesageTypeList(messageTypeList, resultTypeList, colum.getId());
			break;
		case Const.TEMPLATE_TYPE_3:// 产品模板
			List<Product_Type> productTypeList = productTypeService.findProduct_TypeByColumnIds(colum.getId());
			this.convertProductTypeList(productTypeList, resultTypeList, colum.getId());
			break;
		case Const.TEMPLATE_TYPE_5:// 下载模板
			List<FileType> fileTypeList = fileTypeService.findFileTypeByColumnIds(colum.getId());
			this.convertFileTypeList(fileTypeList, resultTypeList, colum.getId());
			break;
		default:
			break;
		}
		Collections.sort(resultTypeList, new Comparator<TypeInfoBo>() {
			@Override
			public int compare(TypeInfoBo o1, TypeInfoBo o2) {
				int A = o1.getSort() == null ? Integer.MAX_VALUE : o1.getSort().intValue();
				int B = o2.getSort() == null ? Integer.MAX_VALUE : o2.getSort().intValue();
				return A - B;
			}
		});
		colum.setTypeList(resultTypeList);
	}
	
	// 转换咨询分类
	public void convertMesageTypeList(List<NewMessageType> typeList, List<TypeInfoBo> resultList, String columId) {
		if (CollectionUtils.isEmpty(typeList)) {
			return;
		}
		for (NewMessageType newMessageType : typeList) {
			TypeInfoBo typeInfoBo = new TypeInfoBo();
			resultList.add(typeInfoBo);
			typeInfoBo.setId(newMessageType.getId());
			typeInfoBo.setName(newMessageType.getType_name());
			typeInfoBo.setTemPlatePath(newMessageType.getTemplate() != null
					? newMessageType.getTemplate().getTemFilepath() : StringUtils.EMPTY);
			typeInfoBo.setSort(newMessageType.getSort());
			typeInfoBo.setType(Const.TEMPLATE_TYPE_2);
			typeInfoBo.setColumId(columId);
			typeInfoBo.setTypeUrlName(newMessageType.getTypeUrlName());
			if (CollectionUtils.isNotEmpty(newMessageType.getChildList())) {
				typeInfoBo.setChildList(new ArrayList<TypeInfoBo>());
				this.convertMesageTypeList(newMessageType.getChildList(), typeInfoBo.getChildList(), columId);
			}
		}
	}

	// 转换产品分类
	public void convertProductTypeList(List<Product_Type> typeList, List<TypeInfoBo> resultList, String columId) {
		if (CollectionUtils.isEmpty(typeList)) {
			return;
		}
		for (Product_Type type : typeList) {
			TypeInfoBo typeInfoBo = new TypeInfoBo();
			resultList.add(typeInfoBo);
			typeInfoBo.setId(type.getId());
			typeInfoBo.setName(type.getType_name());
			typeInfoBo.setSummary(type.getType_summary());
			typeInfoBo.setImageUrl(type.getImgurl());
			typeInfoBo.setType_status(type.getType_status());
			typeInfoBo.setTemPlatePath(
					type.getTemplate() != null ? type.getTemplate().getTemFilepath() : StringUtils.EMPTY);
			typeInfoBo.setSort(type.getSort());
			typeInfoBo.setType(Const.TEMPLATE_TYPE_3);
			typeInfoBo.setColumId(columId);
			typeInfoBo.setTypeUrlName(type.getTypeUrlName());
			if (CollectionUtils.isNotEmpty(type.getChildList())) {
				typeInfoBo.setChildList(new ArrayList<TypeInfoBo>());
				this.convertProductTypeList(type.getChildList(), typeInfoBo.getChildList(), columId);
			}
		}
	}

	// 转换下载分类
	public void convertFileTypeList(List<FileType> typeList, List<TypeInfoBo> resultList, String columId) {
		if (CollectionUtils.isEmpty(typeList)) {
			return;
		}
		for (FileType type : typeList) {
			TypeInfoBo typeInfoBo = new TypeInfoBo();
			resultList.add(typeInfoBo);
			typeInfoBo.setId(type.getDownload_id());
			typeInfoBo.setName(type.getDownload_name());
			typeInfoBo.setTemPlatePath(
					type.getTemplate() != null ? type.getTemplate().getTemFilepath() : StringUtils.EMPTY);
			typeInfoBo.setSort(type.getSort());
			typeInfoBo.setType(Const.TEMPLATE_TYPE_5);
			typeInfoBo.setColumId(columId);
			typeInfoBo.setImageUrl(type.getImgurl());
			typeInfoBo.setTypeUrlName(type.getTypeUrlName());
			if (CollectionUtils.isNotEmpty(type.getChildList())) {
				typeInfoBo.setChildList(new ArrayList<TypeInfoBo>());
				this.convertFileTypeList(type.getChildList(), typeInfoBo.getChildList(), columId);
			}
		}
	}

	@Override
	public Object getParentName(String columId) throws Exception {
		return dao.findForObject("ColumconfigMapper.getParentName", columId);
	}

}