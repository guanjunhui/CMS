package cn.cebest.entity.system.customForm;

import java.io.Serializable;

public class CustomformAttributeValue implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -439358464941247773L;

	private String id;

    private String attrId;//属性ID

    private String attrValue;//属性值

    private Long createdTime;//创建时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId == null ? null : attrId.trim();
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue == null ? null : attrValue.trim();
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }
}