package cn.cebest.entity.system;

import java.io.Serializable;
import java.util.List;

public class EmploySetting implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

    private String name;//名称

    private String nameEn;//英文名称

    private String bianma;//编码

    private Integer orderBy;//排序

    private String parentId;//上级ID

    private String bz;//备注

    private String value;//值
    
    private List<EmploySetting> childList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn == null ? null : nameEn.trim();
    }

    public String getBianma() {
        return bianma;
    }

    public void setBianma(String bianma) {
        this.bianma = bianma == null ? null : bianma.trim();
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz == null ? null : bz.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

	public List<EmploySetting> getChildList() {
		return childList;
	}

	public void setChildList(List<EmploySetting> childList) {
		this.childList = childList;
	}

}