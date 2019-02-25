package cn.cebest.entity.system.customForm;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CustomForm implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4041298526628119882L;

	private String id;

	private String siteId;//站点ID
	
    private String formName;//表单名称

    private String formTemplate;//模板

    private String formDescription;//表单描述
    
    private Date createdTime;
    
    public String getSYSNAME() {
		return SYSNAME;
	}

	public void setSYSNAME(String sYSNAME) {
		SYSNAME = sYSNAME;
	}

	private String SYSNAME;
    private List<CustomFormAttribute> attributeList;//表单项列表

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName == null ? null : formName.trim();
    }

    public String getFormTemplate() {
        return formTemplate;
    }

    public void setFormTemplate(String formTemplate) {
        this.formTemplate = formTemplate == null ? null : formTemplate.trim();
    }

    public String getFormDescription() {
        return formDescription;
    }

    public void setFormDescription(String formDescription) {
        this.formDescription = formDescription == null ? null : formDescription.trim();
    }

	public List<CustomFormAttribute> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<CustomFormAttribute> attributeList) {
		this.attributeList = attributeList;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
    
}