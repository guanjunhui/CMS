package cn.cebest.service.system.customForm.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.customForm.CustomForm;
import cn.cebest.service.system.customForm.CustomFormService;
import cn.cebest.util.PageData;

@Service
public class CustomFormServiceImpl implements CustomFormService{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Override
	public CustomForm selectByPrimaryKey(String id) throws Exception {
		return (CustomForm) dao.findForObject("CustomFormMapper.selectByPrimaryKey", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomForm> selectAllFormAttribue(PageData pd) throws Exception {
		return (List<CustomForm>) dao.findForList("CustomFormMapper.selectAllFormAttribue", pd);
	}

	@Override
	public void saveForm(CustomForm pd) throws Exception {
		dao.save("CustomFormMapper.saveForm", pd);
	}

	@Override
	public void updateForm(CustomForm pd) throws Exception {
		dao.update("CustomFormMapper.updateForm", pd);
	}

	/**
	@SuppressWarnings("unchecked")
	@Override
	public List<ZTree> selectAllFormTreeAttribue(PageData pd) throws Exception {
		List<CustomForm> customFormList=(List<CustomForm>) dao.findForList("CustomFormMapper.selectAllFormAttribue", pd);
		if(CollectionUtils.isEmpty(customFormList)){
			return null;
		}
		List<ZTree> treeList=new ArrayList<ZTree>(customFormList.size());
		for(CustomForm customForm:customFormList){
			ZTree tree = new ZTree();
			tree.setId(customForm.getId());
			tree.setpId(Const.PARENT_FLAG);
			tree.setName(customForm.getFormName());
			treeList.add(tree);
			List<CustomFormAttribute> attributeList= customForm.getAttributeList();
			if(CollectionUtils.isEmpty(attributeList)) break;
			for(CustomFormAttribute attrInfo:attributeList){
				ZTree child = new ZTree();
				child.setId(attrInfo.getId());
				child.setpId(customForm.getId());
				child.setName(attrInfo.getAttrName());
				treeList.add(child);
			}
		}
		return treeList;
	}*/

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomForm> selectlistPageAllFormAttribue(Page page) throws Exception {
		return (List<CustomForm>) dao.findForList("CustomFormMapper.selectlistPageAllFormAttribue", page);
	}

	@Override
	public void deleteByPrimaryKey(String id) throws Exception {
		dao.delete("CustomFormMapper.deleteByPrimaryKey", id);
	}

}
