package cn.cebest.entity.system;

import java.util.Date;

public class Template {
    private String id;

    private String temName;

    private String temFilepath;

    private String temFilename;

    private Date createtime;

    private String temType;

    private String temImagepath;
    
    private String isDefault;
    
    private String isColumId;
    
    private String type;
    
    private String siteID;

    
    public String getSiteID() {
		return siteID;
	}

	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsColumId() {
		return isColumId;
	}

	public void setIsColumId(String isColumId) {
		this.isColumId = isColumId;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTemName() {
        return temName;
    }

    public void setTemName(String temName) {
        this.temName = temName == null ? null : temName.trim();
    }

    public String getTemFilepath() {
        return temFilepath;
    }

    public void setTemFilepath(String temFilepath) {
        this.temFilepath = temFilepath == null ? null : temFilepath.trim();
    }

    public String getTemFilename() {
        return temFilename;
    }

    public void setTemFilename(String temFilename) {
        this.temFilename = temFilename == null ? null : temFilename.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getTemType() {
        return temType;
    }

    public void setTemType(String temType) {
        this.temType = temType == null ? null : temType.trim();
    }

    public String getTemImagepath() {
        return temImagepath;
    }

    public void setTemImagepath(String temImagepath) {
        this.temImagepath = temImagepath == null ? null : temImagepath.trim();
    }

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
    
}