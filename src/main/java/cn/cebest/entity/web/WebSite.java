package cn.cebest.entity.web;

import java.io.Serializable;
import java.util.Date;

public class WebSite implements Serializable{
    
	private static final long serialVersionUID = 1L;

	private String siteId;

    private String siteName;//站点名称

    private String siteLanguage;//语言版本

    private String siteStatus;//站点状态

    private Date createdTime;

    private String siteDomian;//站点域名

    private String siteType;//站点属性
    
    private String siteIndex;//站点首页

    private String siteLogo;
    
    private String siteSubdomian;
    
    private String siteRecordno;
    
    private String siteEmailAddr;
    
    private String siteEmailPwd;
    
    private String siteEmailPrefix;
    
    private String siteEmailSmtp;
    
    private String siteKeyword;
    
    private String siteDesc;
    
    private String ifStatic;
    
    private String siteLogoPath;

    public String getId() {
        return siteId;
    }

    public void setId(String siteId) {
        this.siteId = siteId == null ? null : siteId.trim();
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName == null ? null : siteName.trim();
    }

    public String getSiteLanguage() {
        return siteLanguage;
    }

    public void setSiteLanguage(String siteLanguage) {
        this.siteLanguage = siteLanguage == null ? null : siteLanguage.trim();
    }

    public String getSiteStatus() {
        return siteStatus;
    }

    public void setSiteStatus(String siteStatus) {
        this.siteStatus = siteStatus == null ? null : siteStatus.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getSiteDomian() {
        return siteDomian;
    }

    public void setSiteDomian(String siteDomian) {
        this.siteDomian = siteDomian == null ? null : siteDomian.trim();
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType == null ? null : siteType.trim();
    }

	public String getSiteIndex() {
		return siteIndex;
	}

	public void setSiteIndex(String siteIndex) {
		this.siteIndex = siteIndex;
	}

	public String getSiteLogo() {
		return siteLogo;
	}

	public void setSiteLogo(String siteLogo) {
		this.siteLogo = siteLogo;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteSubdomian() {
		return siteSubdomian;
	}

	public void setSiteSubdomian(String siteSubdomian) {
		this.siteSubdomian = siteSubdomian;
	}

	public String getSiteRecordno() {
		return siteRecordno;
	}

	public void setSiteRecordno(String siteRecordno) {
		this.siteRecordno = siteRecordno;
	}

	public String getSiteEmailAddr() {
		return siteEmailAddr;
	}

	public void setSiteEmailAddr(String siteEmailAddr) {
		this.siteEmailAddr = siteEmailAddr;
	}

	public String getSiteEmailPwd() {
		return siteEmailPwd;
	}

	public void setSiteEmailPwd(String siteEmailPwd) {
		this.siteEmailPwd = siteEmailPwd;
	}

	public String getSiteEmailPrefix() {
		return siteEmailPrefix;
	}

	public void setSiteEmailPrefix(String siteEmailPrefix) {
		this.siteEmailPrefix = siteEmailPrefix;
	}

	public String getSiteEmailSmtp() {
		return siteEmailSmtp;
	}

	public void setSiteEmailSmtp(String siteEmailSmtp) {
		this.siteEmailSmtp = siteEmailSmtp;
	}

	public String getSiteKeyword() {
		return siteKeyword;
	}

	public void setSiteKeyword(String siteKeyword) {
		this.siteKeyword = siteKeyword;
	}

	public String getSiteDesc() {
		return siteDesc;
	}

	public void setSiteDesc(String siteDesc) {
		this.siteDesc = siteDesc;
	}

	public String getIfStatic() {
		return ifStatic;
	}

	public void setIfStatic(String ifStatic) {
		this.ifStatic = ifStatic;
	}

	public String getSiteLogoPath() {
		return siteLogoPath;
	}

	public void setSiteLogoPath(String siteLogoPath) {
		this.siteLogoPath = siteLogoPath;
	}
	
}