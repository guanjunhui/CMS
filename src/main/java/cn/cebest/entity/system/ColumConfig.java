package cn.cebest.entity.system;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.cebest.entity.bo.ContentInfoBo;
import cn.cebest.entity.bo.TypeInfoBo;

public class ColumConfig implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String id;

	private String columName;
    
    private String columEnglishName;

    private String columUrlpath;

    private String columDisplay;

    private String parentid;

    private Integer sort;

    private String siteid;

    private String columTemplatetid;

    private String columSubname;

    private String columImage;
    
    private String columImagePath;

    private String columDesc;

    private String outUrl;
    
    private Seo seoInfo;
    
    private List<ColumConfig> subConfigList;
    
    private Template template;
    
    private int childLevel;
    
    private String columType;
    
    private List<TypeInfoBo> typeList;
    
    private List<ContentInfoBo> contentList;
    
    private boolean isSelect;
    
    private String columGroupId;
    
    private String nodeType;//临时变量，权限时区分系统菜单还是栏目数据类型（1：系统菜单 2：动态栏目）
    
    private String index_Status;
    
    private String columndetailId;
    
    private String columUrlName;
    
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createtime;
    
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date updateTime;
    
	public String getColumUrlName() {
		return columUrlName;
	}

	public void setColumUrlName(String columUrlName) {
		this.columUrlName = columUrlName;
	}
    public String getColumndetailId() {
		return columndetailId;
	}

	public void setColumndetailId(String columndetailId) {
		this.columndetailId = columndetailId;
	}
	
    public String getIndex_Status() {
		return index_Status;
	}

	public void setIndex_Status(String index_Status) {
		this.index_Status = index_Status;
	}

	public Seo getSeoInfo() {
		return seoInfo;
	}

	public void setSeoInfo(Seo seoInfo) {
		this.seoInfo = seoInfo;
	}

	public String getColumGroupId() {
		return columGroupId;
	}

	public void setColumGroupId(String columGroupId) {
		this.columGroupId = columGroupId;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
    
	public String getColumType() {
		return columType;
	}

	public void setColumType(String columType) {
		this.columType = columType == null ? null : columType.trim();
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getColumName() {
        return columName;
    }

    public void setColumName(String columName) {
        this.columName = columName == null ? null : columName.trim();
    }

    public String getColumUrlpath() {
        return columUrlpath;
    }

    public void setColumUrlpath(String columUrlpath) {
        this.columUrlpath = columUrlpath == null ? null : columUrlpath.trim();
    }

    public String getColumDisplay() {
        return columDisplay;
    }

    public void setColumDisplay(String columDisplay) {
        this.columDisplay = columDisplay == null ? null : columDisplay.trim();
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid == null ? null : parentid.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid == null ? null : siteid.trim();
    }

    public String getColumTemplatetid() {
        return columTemplatetid;
    }

    public void setColumTemplatetid(String columTemplatetid) {
        this.columTemplatetid = columTemplatetid == null ? null : columTemplatetid.trim();
    }

    public String getColumSubname() {
        return columSubname;
    }

    public void setColumSubname(String columSubname) {
        this.columSubname = columSubname == null ? null : columSubname.trim();
    }

    public String getColumImage() {
        return columImage;
    }

    public void setColumImage(String columImage) {
        this.columImage = columImage == null ? null : columImage.trim();
    }

    public String getColumDesc() {
        return columDesc;
    }

    public void setColumDesc(String columDesc) {
        this.columDesc = columDesc == null ? null : columDesc.trim();
    }

    public String getOutUrl() {
        return outUrl;
    }

    public void setOutUrl(String outUrl) {
        this.outUrl = outUrl == null ? null : outUrl.trim();
    }

	public List<ColumConfig> getSubConfigList() {
		return subConfigList;
	}

	public void setSubConfigList(List<ColumConfig> subConfigList) {
		this.subConfigList = subConfigList;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public int getChildLevel() {
		return childLevel;
	}

	public void setChildLevel(int childLevel) {
		this.childLevel = childLevel;
	}
	
	public List<TypeInfoBo> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<TypeInfoBo> typeList) {
		this.typeList = typeList;
	}
	
	public String getColumEnglishName() {
		return columEnglishName;
	}

	public void setColumEnglishName(String columEnglishName) {
		this.columEnglishName = columEnglishName;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	
	public List<ContentInfoBo> getContentList() {
		return contentList;
	}

	public void setContentList(List<ContentInfoBo> contentList) {
		this.contentList = contentList;
	}
	
	
	public String getColumImagePath() {
		return columImagePath;
	}

	public void setColumImagePath(String columImagePath) {
		this.columImagePath = columImagePath;
	}

	public boolean hashContainContent(String cotentId) {
		for(ContentInfoBo cotent:contentList){
			if(cotent.getId().equals(cotentId)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + childLevel;
		result = prime * result + ((columDesc == null) ? 0 : columDesc.hashCode());
		result = prime * result + ((columDisplay == null) ? 0 : columDisplay.hashCode());
		result = prime * result + ((columImage == null) ? 0 : columImage.hashCode());
		result = prime * result + ((columName == null) ? 0 : columName.hashCode());
		result = prime * result + ((columSubname == null) ? 0 : columSubname.hashCode());
		result = prime * result + ((columTemplatetid == null) ? 0 : columTemplatetid.hashCode());
		result = prime * result + ((columUrlpath == null) ? 0 : columUrlpath.hashCode());
		result = prime * result + ((createtime == null) ? 0 : createtime.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((outUrl == null) ? 0 : outUrl.hashCode());
		result = prime * result + ((parentid == null) ? 0 : parentid.hashCode());
		result = prime * result + ((siteid == null) ? 0 : siteid.hashCode());
		result = prime * result + ((sort == null) ? 0 : sort.hashCode());
		result = prime * result + ((subConfigList == null) ? 0 : subConfigList.hashCode());
		result = prime * result + ((template == null) ? 0 : template.hashCode());
		result = prime * result + ((columType == null) ? 0 : columType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumConfig other = (ColumConfig) obj;
		if (childLevel != other.childLevel)
			return false;
		if (columDesc == null) {
			if (other.columDesc != null)
				return false;
		} else if (!columDesc.equals(other.columDesc))
			return false;
		if (columDisplay == null) {
			if (other.columDisplay != null)
				return false;
		} else if (!columDisplay.equals(other.columDisplay))
			return false;
		if (columImage == null) {
			if (other.columImage != null)
				return false;
		} else if (!columImage.equals(other.columImage))
			return false;
		if (columName == null) {
			if (other.columName != null)
				return false;
		} else if (!columName.equals(other.columName))
			return false;
		if (columSubname == null) {
			if (other.columSubname != null)
				return false;
		} else if (!columSubname.equals(other.columSubname))
			return false;
		if (columTemplatetid == null) {
			if (other.columTemplatetid != null)
				return false;
		} else if (!columTemplatetid.equals(other.columTemplatetid))
			return false;
		if (columUrlpath == null) {
			if (other.columUrlpath != null)
				return false;
		} else if (!columUrlpath.equals(other.columUrlpath))
			return false;
		if (createtime == null) {
			if (other.createtime != null)
				return false;
		} else if (!createtime.equals(other.createtime))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (outUrl == null) {
			if (other.outUrl != null)
				return false;
		} else if (!outUrl.equals(other.outUrl))
			return false;
		if (parentid == null) {
			if (other.parentid != null)
				return false;
		} else if (!parentid.equals(other.parentid))
			return false;
		if (siteid == null) {
			if (other.siteid != null)
				return false;
		} else if (!siteid.equals(other.siteid))
			return false;
		if (sort == null) {
			if (other.sort != null)
				return false;
		} else if (!sort.equals(other.sort))
			return false;
		if (subConfigList == null) {
			if (other.subConfigList != null)
				return false;
		} else if (!subConfigList.equals(other.subConfigList))
			return false;
		if (template == null) {
			if (other.template != null)
				return false;
		} else if (!template.equals(other.template))
			return false;
		if (columType == null) {
			if (other.columType != null)
				return false;
		} else if (!columType.equals(other.columType))
			return false;
		return true;
	}
	
}