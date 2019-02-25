package cn.cebest.service.system.customForm.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.bo.FormRecordBo;
import cn.cebest.entity.system.customForm.CustomFormAttribute;
import cn.cebest.entity.system.customForm.CustomformAttributeValue;
import cn.cebest.entity.system.infineon.StationMessage;
import cn.cebest.entity.vo.CustomFormVo;
import cn.cebest.service.system.customForm.CustomformAttributeValueService;
import cn.cebest.util.Const;
import cn.cebest.util.PageData;

@Service("customformAttributeValueService")
public class CustomformAttributeValueServiceImpl implements CustomformAttributeValueService{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Override
	public void save(CustomformAttributeValue pd) throws Exception {
		dao.save("CustomformAttributeValueMapper.save", pd);
	}
	@Override
	public void deleteByAttrIds(List<String> attrIds) throws Exception {
		if(CollectionUtils.isNotEmpty(attrIds)){
			dao.delete("CustomformAttributeValueMapper.deleteByAttrIds", attrIds);
		}
	}

	@Override
	public void deleteByAttrId(String attrId) throws Exception {
		dao.delete("CustomformAttributeValueMapper.deleteByAttrId", attrId);
	}

	@Override
	public void saveBatch(List<CustomformAttributeValue> list) throws Exception {
		
		dao.save("CustomformAttributeValueMapper.saveBatch", list);
		
	}

	@Override
	public void deleteByTimes(String[] times) throws Exception {
		dao.delete("CustomformAttributeValueMapper.deleteByTimes", times);
	}

	@Override
	public CustomFormVo selectformAttributeValueList(PageData pd) throws Exception {
		Page page=new Page();
		//根据formId查询表单项的数量
		String formId=pd.getString("formId");
		Integer formAttrCount=(Integer) dao.findForObject("CustomFormAttributeMapper.selectCountByFormID", formId);
		//查询表单项
		List<CustomFormAttribute> attrInfoList=(List<CustomFormAttribute>) 
				dao.findForList("CustomFormAttributeMapper.getAttributeListByFormID", formId);
		if(CollectionUtils.isEmpty(attrInfoList)){
			return null;
		}
		//填充表单头部
		CustomFormVo customFormVo = new CustomFormVo();
		List<FormRecordBo> recordHead=new ArrayList<FormRecordBo>(attrInfoList.size());
		customFormVo.setRecordHead(recordHead);
		List<String> attrIdList=new ArrayList<String>();
		for(CustomFormAttribute info:attrInfoList){
			FormRecordBo formRecordBo = new FormRecordBo();
			formRecordBo.setId(info.getId());
			formRecordBo.setName(info.getAttrName());
			recordHead.add(formRecordBo);
			attrIdList.add(info.getId());
		}
		pd.put("list", attrIdList);
		//查询表单项值的总记录数
		Integer attrValuesCount=(Integer) dao.findForObject("CustomformAttributeValueMapper.selectCountByAttrValueIds", pd);
		int totalResult=Const.INT_0;//总记录数
		if(attrValuesCount%formAttrCount==0)
			totalResult = attrValuesCount/formAttrCount;
		else
			totalResult = attrValuesCount/formAttrCount+1;
		page.setTotalResult(totalResult);
		page.setEntityOrField(true);
		int offset=page.getCurrentResult()*formAttrCount;
		int limit=page.getShowCount()*formAttrCount;
		
		//根据表单项集合查询表单项值的集合
//				List<CustomformAttributeValue> customFormAttributeValueList = (List<CustomformAttributeValue>) 
//						dao.findForList("CustomformAttributeValueMapper.selectListByAttrIds", attrIdList, new RowBounds(offset,limit));
		pd.put("offset", offset);
		pd.put("limit", limit);
		List<CustomformAttributeValue> customFormAttributeValueList = (List<CustomformAttributeValue>) 
				dao.findForList("CustomformAttributeValueMapper.selectListByAttrValueIds", pd);

		if(CollectionUtils.isEmpty(customFormAttributeValueList)){
			return customFormVo;
		}
		//表单项值的在列表页面展现的条数
		List<List<FormRecordBo>> recordDataTmp=new ArrayList<List<FormRecordBo>>();
		for(CustomFormAttribute attr:attrInfoList){
			List<FormRecordBo> formRecordBoList = new ArrayList<FormRecordBo>();
			for(CustomformAttributeValue attrvalue:customFormAttributeValueList){
				if(attrvalue.getAttrId().equals(attr.getId())){
					FormRecordBo recordBo = new FormRecordBo();
					recordBo.setId(attrvalue.getId());
					recordBo.setName(attrvalue.getAttrValue());
					recordBo.setCreatTime(attrvalue.getCreatedTime());
					formRecordBoList.add(recordBo);
				}
			}
			Collections.sort(formRecordBoList, new Comparator<FormRecordBo>(){
				@Override
				public int compare(FormRecordBo o1, FormRecordBo o2) {
					Long A=o1.getCreatTime();
					Long B=o2.getCreatTime();
					
					return (int) (B.longValue()-A.longValue());
				}
			});
			recordDataTmp.add(formRecordBoList);
		}
		int recordLength=customFormAttributeValueList.size()/formAttrCount;
		List<List<FormRecordBo>> recordData = new ArrayList<List<FormRecordBo>>(recordLength);
		customFormVo.setRecordData(recordData);
		//开辟内存空间
		for(int i=0;i<recordLength;i++){
			List<FormRecordBo> dataList=new ArrayList<FormRecordBo>();
			recordData.add(dataList);
			for(int m=0;m<formAttrCount;m++){
				FormRecordBo data = new FormRecordBo();
				dataList.add(data);
			}
		}
		//把表单项值的列式记录转化为行式记录
		for(int i=0;i<recordDataTmp.size();i++){
			List<FormRecordBo> tmpDataList=recordDataTmp.get(i);
			for(int m=0;m<tmpDataList.size();m++){
				recordData.get(m).set(i, tmpDataList.get(m));
			}
		}
		return customFormVo;
	}
	
	@Override
	public void updateformAttributeValue(PageData pd) throws Exception {
		//更改表单数据
		String[] formValueNameS = (String[]) pd.get("formValueNameS");
		String[] formValueIdS = (String[]) pd.get("formValueIdS");
		Map<String, Object> map = new HashMap<>();
		for(int i=0;formValueIdS.length > i;i++){
			map.put("name", formValueNameS[i]);
			map.put("id", formValueIdS[i]);
			dao.update("CustomformAttributeValueMapper.updateformAttributeValue", map);
		}
	}
}
