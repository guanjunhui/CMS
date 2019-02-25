package cn.cebest.entity.system;

import java.io.Serializable;



/**
 *SEO信息 
 */

public class Seo implements Serializable{
	
	private static final long serialVersionUID = 5477218028343836784L;

	private String seoTitle;

    private String seoKeywords;

    private String seoDescription;

    private String seoType;

    private String masterId;

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public String getSeoType() {
		return seoType;
	}

	public void setSeoType(String seoType) {
		this.seoType = seoType;
	}

	public String getMasterId() {
		return masterId;
	}

	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    
}