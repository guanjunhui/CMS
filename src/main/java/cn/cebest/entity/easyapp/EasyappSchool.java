package cn.cebest.entity.easyapp;

import java.io.Serializable;
import java.util.Date;

public class EasyappSchool implements Serializable{
	private static final long serialVersionUID = 1L;

	private String id;

    private String scNameCn;//中文名称

    private String scNameEn;//英文名称

    private String scArea;//地区

    private String scSite;//地址

    private String scTuition;//学费

    private String scCondition;//招生条件

    private String scScale;//规模（在校人数）

    private Boolean istop;//是否推荐

    private String scCourse;//课程

    private String scCountry;//留学国家

    private String scGraduationDest;//毕业去向

    private String scGraduationDes;//毕业去向描述

    private String scDes;//学校描述

    private Date createdTime;//创建时间

    private String createdId;//创建人

    private String scPresidentName;//董事长姓名
   
    private String scPresidentDes;//董事长描述

    private String scPresidentImgPath;//董事长照片

    private String scPresidentSign;//董事长签名

    private String scImgPath;//学校照片

    private String scMediaPath;//学校视频

    private String scIntroduce;//学校介绍

    private String scPartner;//合作院校

    private String scWebsite;//学校官网

    private String scTel;//招生电话

    private String scPublicnumPath;//公众号(img)

    private String scWexinPath;//微信号(img)

    private String scQq;//QQ号(img)

    private String scWeibo;//微博号(url)

    private String scPoint;//地图Gps位置
    
    private String scStarLevel;//学校星级
    
    private String siteId;//站点ID
    
    private Boolean isHomeTop;//是否首页推荐

    private Integer homeOrder;//首页推荐排序


    private String graduationDestkey;
    private String graduationDestvalue;
    private String scPartnerName;
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScNameCn() {
        return scNameCn;
    }

    public void setScNameCn(String scNameCn) {
        this.scNameCn = scNameCn == null ? null : scNameCn.trim();
    }

    public String getScNameEn() {
        return scNameEn;
    }

    public void setScNameEn(String scNameEn) {
        this.scNameEn = scNameEn == null ? null : scNameEn.trim();
    }

    public String getScArea() {
        return scArea;
    }

    public void setScArea(String scArea) {
        this.scArea = scArea == null ? null : scArea.trim();
    }

    public String getScSite() {
        return scSite;
    }

    public void setScSite(String scSite) {
        this.scSite = scSite == null ? null : scSite.trim();
    }

    public String getScTuition() {
        return scTuition;
    }

    public void setScTuition(String scTuition) {
        this.scTuition = scTuition == null ? null : scTuition.trim();
    }

    public String getScCondition() {
        return scCondition;
    }

    public void setScCondition(String scCondition) {
        this.scCondition = scCondition == null ? null : scCondition.trim();
    }

    public String getScScale() {
        return scScale;
    }

    public void setScScale(String scScale) {
        this.scScale = scScale == null ? null : scScale.trim();
    }

    public Boolean getIstop() {
        return istop;
    }

    public void setIstop(Boolean istop) {
        this.istop = istop;
    }

    public String getScCourse() {
        return scCourse;
    }

    public void setScCourse(String scCourse) {
        this.scCourse = scCourse == null ? null : scCourse.trim();
    }

    public String getScCountry() {
        return scCountry;
    }

    public void setScCountry(String scCountry) {
        this.scCountry = scCountry == null ? null : scCountry.trim();
    }

    public String getScGraduationDest() {
        return scGraduationDest;
    }

    public void setScGraduationDest(String scGraduationDest) {
        this.scGraduationDest = scGraduationDest == null ? null : scGraduationDest.trim();
    }

    public String getScGraduationDes() {
        return scGraduationDes;
    }

    public void setScGraduationDes(String scGraduationDes) {
        this.scGraduationDes = scGraduationDes == null ? null : scGraduationDes.trim();
    }

    public String getScDes() {
        return scDes;
    }

    public void setScDes(String scDes) {
        this.scDes = scDes == null ? null : scDes.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId == null ? null : createdId.trim();
    }

    
    public String getScPresidentName() {
		return scPresidentName;
	}

	public void setScPresidentName(String scPresidentName) {
		this.scPresidentName = scPresidentName;
	}

	public String getScPresidentDes() {
		return scPresidentDes;
	}

	public void setScPresidentDes(String scPresidentDes) {
		this.scPresidentDes = scPresidentDes;
	}

	public String getScPresidentImgPath() {
        return scPresidentImgPath;
    }

    public void setScPresidentImgPath(String scPresidentImgPath) {
        this.scPresidentImgPath = scPresidentImgPath == null ? null : scPresidentImgPath.trim();
    }

    public String getScPresidentSign() {
        return scPresidentSign;
    }

    public void setScPresidentSign(String scPresidentSign) {
        this.scPresidentSign = scPresidentSign == null ? null : scPresidentSign.trim();
    }

    public String getScImgPath() {
        return scImgPath;
    }

    public void setScImgPath(String scImgPath) {
        this.scImgPath = scImgPath == null ? null : scImgPath.trim();
    }

    public String getScMediaPath() {
        return scMediaPath;
    }

    public void setScMediaPath(String scMediaPath) {
        this.scMediaPath = scMediaPath == null ? null : scMediaPath.trim();
    }

    public String getScIntroduce() {
        return scIntroduce;
    }

    public void setScIntroduce(String scIntroduce) {
        this.scIntroduce = scIntroduce == null ? null : scIntroduce.trim();
    }

    public String getScPartner() {
        return scPartner;
    }

    public void setScPartner(String scPartner) {
        this.scPartner = scPartner == null ? null : scPartner.trim();
    }

    public String getScWebsite() {
        return scWebsite;
    }

    public void setScWebsite(String scWebsite) {
        this.scWebsite = scWebsite == null ? null : scWebsite.trim();
    }

    public String getScTel() {
        return scTel;
    }

    public void setScTel(String scTel) {
        this.scTel = scTel == null ? null : scTel.trim();
    }

    public String getScPublicnumPath() {
        return scPublicnumPath;
    }

    public void setScPublicnumPath(String scPublicnumPath) {
        this.scPublicnumPath = scPublicnumPath == null ? null : scPublicnumPath.trim();
    }

    public String getScWexinPath() {
        return scWexinPath;
    }

    public void setScWexinPath(String scWexinPath) {
        this.scWexinPath = scWexinPath == null ? null : scWexinPath.trim();
    }

    public String getScQq() {
        return scQq;
    }

    public void setScQq(String scQq) {
        this.scQq = scQq == null ? null : scQq.trim();
    }

    public String getScWeibo() {
        return scWeibo;
    }

    public void setScWeibo(String scWeibo) {
        this.scWeibo = scWeibo == null ? null : scWeibo.trim();
    }

    public String getScPoint() {
        return scPoint;
    }

    public void setScPoint(String scPoint) {
        this.scPoint = scPoint == null ? null : scPoint.trim();
    }

	public String getScStarLevel() {
		return scStarLevel;
	}

	public void setScStarLevel(String scStarLevel) {
		this.scStarLevel = scStarLevel;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getGraduationDestkey() {
		return graduationDestkey;
	}

	public void setGraduationDestkey(String graduationDestkey) {
		this.graduationDestkey = graduationDestkey;
	}

	public String getGraduationDestvalue() {
		return graduationDestvalue;
	}

	public void setGraduationDestvalue(String graduationDestvalue) {
		this.graduationDestvalue = graduationDestvalue;
	}

	public String getScPartnerName() {
		return scPartnerName;
	}

	public void setScPartnerName(String scPartnerName) {
		this.scPartnerName = scPartnerName;
	}

	public Boolean getIsHomeTop() {
		return isHomeTop;
	}

	public void setIsHomeTop(Boolean isHomeTop) {
		this.isHomeTop = isHomeTop;
	}

	public Integer getHomeOrder() {
		return homeOrder;
	}

	public void setHomeOrder(Integer homeOrder) {
		this.homeOrder = homeOrder;
	}
	
}