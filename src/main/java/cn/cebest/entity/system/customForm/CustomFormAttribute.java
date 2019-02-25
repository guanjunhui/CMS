package cn.cebest.entity.system.customForm;

import java.io.Serializable;
import java.util.List;

public class CustomFormAttribute implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2863555217274384697L;

	private String id;

    private String formId;//所属表单

    private String attrName;//表单项名称

    private String attrRequired;//是否必填

    private String attrStats;//属性状态

    private String attrType;//数据类型 Const.CUSTOMFORMTYPE

    private String attrDefault;//属性默认值

    private String attrDescription;//字段描述
    
    private Integer attrSort;//排序值
    
    private List<CustomformAttributeValue> attributeValueList;//表单项的值集合
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId == null ? null : formId.trim();
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName == null ? null : attrName.trim();
    }

    public String getAttrRequired() {
        return attrRequired;
    }

    public void setAttrRequired(String attrRequired) {
        this.attrRequired = attrRequired == null ? null : attrRequired.trim();
    }

    public String getAttrStats() {
        return attrStats;
    }

    public void setAttrStats(String attrStats) {
        this.attrStats = attrStats == null ? null : attrStats.trim();
    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType == null ? null : attrType.trim();
    }

    public String getAttrDefault() {
        return attrDefault;
    }

    public void setAttrDefault(String attrDefault) {
        this.attrDefault = attrDefault == null ? null : attrDefault.trim();
    }

    public String getAttrDescription() {
        return attrDescription;
    }

    public void setAttrDescription(String attrDescription) {
        this.attrDescription = attrDescription == null ? null : attrDescription.trim();
    }

	public Integer getAttrSort() {
		return attrSort;
	}

	public void setAttrSort(Integer attrSort) {
		this.attrSort = attrSort;
	}

	public List<CustomformAttributeValue> getAttributeValueList() {
		return attributeValueList;
	}

	public void setAttributeValueList(List<CustomformAttributeValue> attributeValueList) {
		this.attributeValueList = attributeValueList;
	}
    
}