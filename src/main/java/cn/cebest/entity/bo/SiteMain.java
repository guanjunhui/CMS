package cn.cebest.entity.bo;

import java.io.Serializable;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *站点主要信息
 **/
public class SiteMain implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String siteName;
	private String siteLanguage;
	private String siteDomian;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSiteLanguage() {
		return siteLanguage;
	}
	public void setSiteLanguage(String siteLanguage) {
		this.siteLanguage = siteLanguage;
	}
	public String getSiteDomian() {
		return siteDomian;
	}
	public void setSiteDomian(String siteDomian) {
		this.siteDomian = siteDomian;
	}
	
	public boolean equals(Object o){
		if(this==o) return true;
		if(o==null || getClass() != o.getClass()) return false;
		SiteMain site=(SiteMain)o;
		return id.equals(site.getId());
	}
	
	public int hashCode(){
		return new HashCodeBuilder(17,37).append(id).toHashCode();
	}
	
}
