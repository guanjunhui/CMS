package cn.cebest.service.system.customForm.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.bo.FormRecordBo;
import cn.cebest.entity.system.customForm.CustomFormAttribute;
import cn.cebest.entity.system.customForm.CustomformAttributeValue;
import cn.cebest.entity.vo.CustomFormVo;
import cn.cebest.service.system.customForm.CustomFormAttributeService;
import cn.cebest.util.Const;
import cn.cebest.util.PageData;

@Service("customFormAttributeService")
public class CustomFormAttributeServiceImpl implements CustomFormAttributeService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Override
	public void save(CustomFormAttribute pd) throws Exception {
		dao.save("CustomFormAttributeMapper.save", pd);
	}

	@Override
	public void update(CustomFormAttribute pd) throws Exception {
		dao.update("CustomFormAttributeMapper.update", pd);
	}

	@Override
	public CustomFormAttribute getObjectById(String id) throws Exception {
		return (CustomFormAttribute) dao.findForObject("CustomFormAttributeMapper.getObjectById", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomFormAttribute> getAttributeAndValueListByFormID(String formId) throws Exception {
		return (List<CustomFormAttribute>) dao.findForList("CustomFormAttributeMapper.getAttributeAndValueListByFormID", formId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CustomFormVo getAttributeAndValuePageByFormID(Page page) throws Exception {
		//根据formId查询表单项的数量
		String formId = page.getPd().getString("formId");
		Integer formAttrCount = (Integer) dao.findForObject("CustomFormAttributeMapper.selectCountByFormID", formId);
		//查询表单项
		List<CustomFormAttribute> attrInfoList = (List<CustomFormAttribute>) dao.findForList("CustomFormAttributeMapper.getAttributeListByFormID", formId);
		if (CollectionUtils.isEmpty(attrInfoList)) {
			return null;
		}
		//填充表单头部
		CustomFormVo customFormVo = new CustomFormVo();
		List<FormRecordBo> recordHead = new ArrayList<FormRecordBo>(attrInfoList.size());
		customFormVo.setRecordHead(recordHead);
		List<String> attrIdList = new ArrayList<String>();
		for (CustomFormAttribute info : attrInfoList) {
			FormRecordBo formRecordBo = new FormRecordBo();
			formRecordBo.setId(info.getId());
			formRecordBo.setName(info.getAttrName());
			recordHead.add(formRecordBo);
			attrIdList.add(info.getId());
		}
		PageData pd = page.getPd();
		pd.put("list", attrIdList);
		//查询表单项值的总记录数
		Integer attrValuesCount = (Integer) dao.findForObject("CustomformAttributeValueMapper.selectCountByAttrIds", pd);
		int totalResult = Const.INT_0;//总记录数
		if (attrValuesCount % formAttrCount == 0)
			totalResult = attrValuesCount / formAttrCount;
		else
			totalResult = attrValuesCount / formAttrCount + 1;
		page.setTotalResult(totalResult);
		page.setEntityOrField(true);
		int offset = page.getCurrentResult() * formAttrCount;
		int limit = page.getShowCount() * formAttrCount;

		//根据表单项集合查询表单项值的集合
		//		List<CustomformAttributeValue> customFormAttributeValueList = (List<CustomformAttributeValue>) 
		//				dao.findForList("CustomformAttributeValueMapper.selectListByAttrIds", attrIdList, new RowBounds(offset,limit));

		pd.put("offset", offset);
		pd.put("limit", limit);
		List<CustomformAttributeValue> customFormAttributeValueList = new ArrayList<>();
		if (StringUtils.isNotEmpty(pd.getString("customform_keyword"))) {
			List<CustomformAttributeValue> keywordFormAttributeValueList = (List<CustomformAttributeValue>) dao.findForList("CustomformAttributeValueMapper.selectListByAttrIds", pd);

			Long[] creatTimeList = new Long[keywordFormAttributeValueList.size()];
			for (int i = 0; i < keywordFormAttributeValueList.size(); i++) {
				creatTimeList[i] = keywordFormAttributeValueList.get(i).getCreatedTime();
			}
			pd.put("creatTimeList", creatTimeList);
			List<CustomformAttributeValue> formAttributeValueList = (List<CustomformAttributeValue>) dao.findForList("CustomformAttributeValueMapper.selectListByAttrValueCreatTimes", pd);
			customFormAttributeValueList.addAll(formAttributeValueList);
		} else {
			customFormAttributeValueList = (List<CustomformAttributeValue>) dao.findForList("CustomformAttributeValueMapper.selectListByAttrIds", pd);
		}

		if (CollectionUtils.isEmpty(customFormAttributeValueList)) {
			return customFormVo;
		}
		//表单项值的在列表页面展现的条数
		List<List<FormRecordBo>> recordDataTmp = new ArrayList<List<FormRecordBo>>();
		for (CustomFormAttribute attr : attrInfoList) {
			List<FormRecordBo> formRecordBoList = new ArrayList<FormRecordBo>();
			for (CustomformAttributeValue attrvalue : customFormAttributeValueList) {
				if (attrvalue.getAttrId().equals(attr.getId())) {
					FormRecordBo recordBo = new FormRecordBo();
					recordBo.setId(attrvalue.getId());
					recordBo.setName(attrvalue.getAttrValue());
					recordBo.setCreatTime(attrvalue.getCreatedTime());
					formRecordBoList.add(recordBo);
				}
			}
			Collections.sort(formRecordBoList, new Comparator<FormRecordBo>() {
				@Override
				public int compare(FormRecordBo o1, FormRecordBo o2) {
					Long A = o1.getCreatTime();
					Long B = o2.getCreatTime();

					return (int) (B.longValue() - A.longValue());
				}
			});
			recordDataTmp.add(formRecordBoList);
		}
		int recordLength = customFormAttributeValueList.size() / formAttrCount;
		List<List<FormRecordBo>> recordData = new ArrayList<List<FormRecordBo>>(recordLength);
		customFormVo.setRecordData(recordData);
		//开辟内存空间
		for (int i = 0; i < recordLength; i++) {
			List<FormRecordBo> dataList = new ArrayList<FormRecordBo>();
			recordData.add(dataList);
			for (int m = 0; m < formAttrCount; m++) {
				FormRecordBo data = new FormRecordBo();
				dataList.add(data);
			}
		}
		//把表单项值的列式记录转化为行式记录
		for (int i = 0; i < recordDataTmp.size(); i++) {
			List<FormRecordBo> tmpDataList = recordDataTmp.get(i);
			for (int m = 0; m < tmpDataList.size(); m++) {
				recordData.get(m).set(i, tmpDataList.get(m));
			}
		}
		return customFormVo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CustomFormVo getAttributeAndValueByFormID(PageData pd) throws Exception {
		//根据formId查询表单项的数量
		String formId = pd.getString("formId");
		Integer formAttrCount = (Integer) dao.findForObject("CustomFormAttributeMapper.selectCountByFormID", formId);
		//查询表单项
		List<CustomFormAttribute> attrInfoList = (List<CustomFormAttribute>) dao.findForList("CustomFormAttributeMapper.getAttributeListByFormID", formId);
		if (CollectionUtils.isEmpty(attrInfoList)) {
			return null;
		}
		//填充表单头部
		CustomFormVo customFormVo = new CustomFormVo();
		List<FormRecordBo> recordHead = new ArrayList<FormRecordBo>(attrInfoList.size());
		customFormVo.setRecordHead(recordHead);
		List<String> attrIdList = new ArrayList<String>();
		for (CustomFormAttribute info : attrInfoList) {
			FormRecordBo formRecordBo = new FormRecordBo();
			formRecordBo.setId(info.getId());
			formRecordBo.setName(info.getAttrName());
			recordHead.add(formRecordBo);
			attrIdList.add(info.getId());
		}

		//根据表单项集合查询表单项值的集合
		//List<CustomformAttributeValue> customFormAttributeValueList = (List<CustomformAttributeValue>) 
		//dao.findForList("CustomformAttributeValueMapper.selectListByAttrIds", attrIdList);

		List<CustomformAttributeValue> customFormAttributeValueList = (List<CustomformAttributeValue>) dao.findForList("CustomformAttributeValueMapper.selectValueListByAttrIds", attrIdList);
		if (CollectionUtils.isEmpty(customFormAttributeValueList)) {
			return customFormVo;
		}
		//表单项值的在列表页面展现的条数
		List<List<FormRecordBo>> recordDataTmp = new ArrayList<List<FormRecordBo>>();
		for (CustomFormAttribute attr : attrInfoList) {
			List<FormRecordBo> formRecordBoList = new ArrayList<FormRecordBo>();
			for (CustomformAttributeValue attrvalue : customFormAttributeValueList) {
				if (attrvalue.getAttrId().equals(attr.getId())) {
					FormRecordBo recordBo = new FormRecordBo();
					recordBo.setId(attrvalue.getId());
					recordBo.setName(attrvalue.getAttrValue());
					recordBo.setCreatTime(attrvalue.getCreatedTime());
					formRecordBoList.add(recordBo);
				}
			}
			Collections.sort(formRecordBoList, new Comparator<FormRecordBo>() {
				@Override
				public int compare(FormRecordBo o1, FormRecordBo o2) {
					Long A = o1.getCreatTime();
					Long B = o2.getCreatTime();

					return (int) (B.longValue() - A.longValue());
				}
			});
			recordDataTmp.add(formRecordBoList);
		}
		int recordLength = customFormAttributeValueList.size() / formAttrCount;
		List<List<FormRecordBo>> recordData = new ArrayList<List<FormRecordBo>>(recordLength);
		customFormVo.setRecordData(recordData);
		//开辟内存空间
		for (int i = 0; i < recordLength; i++) {
			List<FormRecordBo> dataList = new ArrayList<FormRecordBo>();
			recordData.add(dataList);
			for (int m = 0; m < formAttrCount; m++) {
				FormRecordBo data = new FormRecordBo();
				dataList.add(data);
			}
		}
		//把表单项值的列式记录转化为行式记录
		for (int i = 0; i < recordDataTmp.size(); i++) {
			List<FormRecordBo> tmpDataList = recordDataTmp.get(i);
			for (int m = 0; m < tmpDataList.size(); m++) {
				recordData.get(m).set(i, tmpDataList.get(m));
			}
		}
		return customFormVo;
	}

	@Override
	public void deleteByPrimaryKey(String id) throws Exception {
		dao.delete("CustomFormAttributeMapper.deleteByPrimaryKey", id);
	}

	@Override
	public void deleteByFormId(String formId) throws Exception {
		dao.delete("CustomFormAttributeMapper.deleteByFormId", formId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> selectAttrIdsByFormID(String formId) throws Exception {
		return (List<String>) dao.findForList("CustomFormAttributeMapper.selectAttrIdsByFormID", formId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomFormAttribute> getAttributelistPageByFormID(Page page) throws Exception {
		return (List<CustomFormAttribute>) dao.findForList("CustomFormAttributeMapper.getAttributelistPageByFormID", page);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomFormAttribute> getAttributeListByFormID(String formId) throws Exception {
		return (List<CustomFormAttribute>) dao.findForList("CustomFormAttributeMapper.getAttributeListByFormID", formId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomFormAttribute> getAttributeAlllistPageByFormID(Page page) throws Exception {
		return (List<CustomFormAttribute>) dao.findForList("CustomFormAttributeMapper.getAttributeAlllistPageByFormID", page);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CustomFormVo getAttributeAndValueByFormIdAndCondition(String formId, String condition) throws Exception {
		//查询表单项
		List<CustomFormAttribute> attrInfoList = (List<CustomFormAttribute>) dao.findForList("CustomFormAttributeMapper.getAttributeListByFormID", formId);
		if (CollectionUtils.isEmpty(attrInfoList)) {
			return null;
		}
		//填充表单头部
		CustomFormVo customFormVo = new CustomFormVo();
		List<FormRecordBo> recordHead = new ArrayList<FormRecordBo>(attrInfoList.size());
		customFormVo.setRecordHead(recordHead);
		List<String> attrIdList = new ArrayList<String>();
		for (CustomFormAttribute info : attrInfoList) {
			FormRecordBo formRecordBo = new FormRecordBo();
			formRecordBo.setId(info.getId());
			formRecordBo.setName(info.getAttrName());
			recordHead.add(formRecordBo);
			attrIdList.add(info.getId());
		}
		//根据表单项集合查询表单项值的集合
		PageData pd = new PageData();
		pd.put("list", attrIdList);
		pd.put("attrValue", condition);
		List<CustomformAttributeValue> customFormAttributeValueList = (List<CustomformAttributeValue>) dao.findForList("CustomformAttributeValueMapper.selectListByAttrIdsAndConditon", pd);

		if (CollectionUtils.isEmpty(customFormAttributeValueList)) {
			return customFormVo;
		}
		//表单项值的在列表页面展现的条数
		List<List<FormRecordBo>> recordDataTmp = new ArrayList<List<FormRecordBo>>();
		for (CustomFormAttribute attr : attrInfoList) {
			List<FormRecordBo> formRecordBoList = new ArrayList<FormRecordBo>();
			for (CustomformAttributeValue attrvalue : customFormAttributeValueList) {
				if (attrvalue.getAttrId().equals(attr.getId())) {
					FormRecordBo recordBo = new FormRecordBo();
					recordBo.setId(attrvalue.getId());
					recordBo.setName(attrvalue.getAttrValue());
					recordBo.setCreatTime(attrvalue.getCreatedTime());
					formRecordBoList.add(recordBo);
				}
			}
			Collections.sort(formRecordBoList, new Comparator<FormRecordBo>() {
				@Override
				public int compare(FormRecordBo o1, FormRecordBo o2) {
					Long A = o1.getCreatTime();
					Long B = o2.getCreatTime();

					return (int) (B.longValue() - A.longValue());
				}
			});
			recordDataTmp.add(formRecordBoList);
		}
		Integer formAttrCount = (Integer) dao.findForObject("CustomFormAttributeMapper.selectCountByFormID", formId);
		int recordLength = customFormAttributeValueList.size() / formAttrCount;
		List<List<FormRecordBo>> recordData = new ArrayList<List<FormRecordBo>>(recordLength);
		customFormVo.setRecordData(recordData);
		//开辟内存空间
		for (int i = 0; i < recordLength; i++) {
			List<FormRecordBo> dataList = new ArrayList<FormRecordBo>();
			recordData.add(dataList);
			for (int m = 0; m < formAttrCount; m++) {
				FormRecordBo data = new FormRecordBo();
				dataList.add(data);
			}
		}
		//把表单项值的列式记录转化为行式记录
		for (int i = 0; i < recordDataTmp.size(); i++) {
			List<FormRecordBo> tmpDataList = recordDataTmp.get(i);
			for (int m = 0; m < tmpDataList.size(); m++) {
				recordData.get(m).set(i, tmpDataList.get(m));
			}
		}
		return customFormVo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomFormAttribute> getAttributeListAllByFormID(String formId) throws Exception {
		return (List<CustomFormAttribute>) dao.findForList("CustomFormAttributeMapper.getAttributeListAllByFormID", formId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CustomFormVo getAttributeAndValueByFormIdAndAddress(String formId, String condition) throws Exception {
		//查询表单项
		List<CustomFormAttribute> attrInfoList = (List<CustomFormAttribute>) dao.findForList("CustomFormAttributeMapper.getAttributeListByFormID", formId);
		if (CollectionUtils.isEmpty(attrInfoList)) {
			return null;
		}
		//填充表单头部
		CustomFormVo customFormVo = new CustomFormVo();
		List<FormRecordBo> recordHead = new ArrayList<FormRecordBo>(attrInfoList.size());
		customFormVo.setRecordHead(recordHead);
		List<String> attrIdList = new ArrayList<String>();
		for (CustomFormAttribute info : attrInfoList) {
			FormRecordBo formRecordBo = new FormRecordBo();
			formRecordBo.setId(info.getId());
			formRecordBo.setName(info.getAttrName());
			recordHead.add(formRecordBo);
			attrIdList.add(info.getId());
		}
		//根据表单项集合查询表单项值的集合
		PageData pd = new PageData();
		pd.put("list", attrIdList);
		String[] conditionList = condition.split(",");
		pd.put("attrValue", Arrays.asList(conditionList));
		List<CustomformAttributeValue> customFormAttributeValueList = (List<CustomformAttributeValue>) dao.findForList("CustomformAttributeValueMapper.selectListByAttrIdsAndAddress", pd);

		if (CollectionUtils.isEmpty(customFormAttributeValueList)) {
			return customFormVo;
		}
		//表单项值的在列表页面展现的条数
		List<List<FormRecordBo>> recordDataTmp = new ArrayList<List<FormRecordBo>>();
		for (CustomFormAttribute attr : attrInfoList) {
			List<FormRecordBo> formRecordBoList = new ArrayList<FormRecordBo>();
			for (CustomformAttributeValue attrvalue : customFormAttributeValueList) {
				if (attrvalue.getAttrId().equals(attr.getId())) {
					FormRecordBo recordBo = new FormRecordBo();
					recordBo.setId(attrvalue.getId());
					recordBo.setName(attrvalue.getAttrValue());
					recordBo.setCreatTime(attrvalue.getCreatedTime());
					formRecordBoList.add(recordBo);
				}
			}
			Collections.sort(formRecordBoList, new Comparator<FormRecordBo>() {
				@Override
				public int compare(FormRecordBo o1, FormRecordBo o2) {
					Long A = o1.getCreatTime();
					Long B = o2.getCreatTime();

					return (int) (B.longValue() - A.longValue());
				}
			});
			recordDataTmp.add(formRecordBoList);
		}
		Integer formAttrCount = (Integer) dao.findForObject("CustomFormAttributeMapper.selectCountByFormID", formId);
		int recordLength = customFormAttributeValueList.size() / formAttrCount;
		List<List<FormRecordBo>> recordData = new ArrayList<List<FormRecordBo>>(recordLength);
		customFormVo.setRecordData(recordData);
		//开辟内存空间
		for (int i = 0; i < recordLength; i++) {
			List<FormRecordBo> dataList = new ArrayList<FormRecordBo>();
			recordData.add(dataList);
			for (int m = 0; m < formAttrCount; m++) {
				FormRecordBo data = new FormRecordBo();
				dataList.add(data);
			}
		}
		//把表单项值的列式记录转化为行式记录
		for (int i = 0; i < recordDataTmp.size(); i++) {
			List<FormRecordBo> tmpDataList = recordDataTmp.get(i);
			for (int m = 0; m < tmpDataList.size(); m++) {
				recordData.get(m).set(i, tmpDataList.get(m));
			}
		}
		return customFormVo;
	}

}
