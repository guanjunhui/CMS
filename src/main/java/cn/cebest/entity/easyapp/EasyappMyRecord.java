package cn.cebest.entity.easyapp;

import java.io.Serializable;
import java.util.Date;

public class EasyappMyRecord implements Serializable{


	private static final long serialVersionUID = 1L;

	private String id;

    private String siteId;//站点ID
    
    private String rcdMemberId;//会员ID

    private String rcdStuName;//学生姓名

    private Date rcdApplyDate;//申请日期

    private Date rcdStuBrth;//生日

    private String rcdStuSex;//性别

    private String rcdEmail;//邮件

    private String rcdTel;//联系电话

    private String rcdStuScool;//当前学校

    private Date rcdStuGrad;//毕业时间

    private String rcdAddr;//住址

    private String rcdStuAwards;//获奖情况 

    private String rcdStuAwardsFile;//获奖附件

    private String rcdStuGrade;//成绩

    private String rcdStuGradeFile;//成绩附件

    private Date rcdStartscoolDate;//申请入学时间

    private String rcdWantCtry;//意向留学国家

    private String rcdWantCourse;//意向课程

    private String rcdRemark;//备注

    private Date createdTime;//创建时间

    private String createdId;//创建人

    private Date updateTime;//更新时间


    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId == null ? null : siteId.trim();
    }

    public String getRcdStuName() {
        return rcdStuName;
    }

    public void setRcdStuName(String rcdStuName) {
        this.rcdStuName = rcdStuName == null ? null : rcdStuName.trim();
    }

    public Date getRcdApplyDate() {
        return rcdApplyDate;
    }

    public void setRcdApplyDate(Date rcdApplyDate) {
        this.rcdApplyDate = rcdApplyDate;
    }

    public Date getRcdStuBrth() {
        return rcdStuBrth;
    }

    public void setRcdStuBrth(Date rcdStuBrth) {
        this.rcdStuBrth = rcdStuBrth;
    }

    public String getRcdStuSex() {
        return rcdStuSex;
    }

    public void setRcdStuSex(String rcdStuSex) {
        this.rcdStuSex = rcdStuSex == null ? null : rcdStuSex.trim();
    }

    public String getRcdEmail() {
        return rcdEmail;
    }

    public void setRcdEmail(String rcdEmail) {
        this.rcdEmail = rcdEmail == null ? null : rcdEmail.trim();
    }

    public String getRcdTel() {
        return rcdTel;
    }

    public void setRcdTel(String rcdTel) {
        this.rcdTel = rcdTel == null ? null : rcdTel.trim();
    }

    public String getRcdStuScool() {
        return rcdStuScool;
    }

    public void setRcdStuScool(String rcdStuScool) {
        this.rcdStuScool = rcdStuScool == null ? null : rcdStuScool.trim();
    }

    public Date getRcdStuGrad() {
        return rcdStuGrad;
    }

    public void setRcdStuGrad(Date rcdStuGrad) {
        this.rcdStuGrad = rcdStuGrad;
    }

    public String getRcdAddr() {
        return rcdAddr;
    }

    public void setRcdAddr(String rcdAddr) {
        this.rcdAddr = rcdAddr == null ? null : rcdAddr.trim();
    }

    public String getRcdStuAwards() {
        return rcdStuAwards;
    }

    public void setRcdStuAwards(String rcdStuAwards) {
        this.rcdStuAwards = rcdStuAwards == null ? null : rcdStuAwards.trim();
    }

    public String getRcdStuAwardsFile() {
        return rcdStuAwardsFile;
    }

    public void setRcdStuAwardsFile(String rcdStuAwardsFile) {
        this.rcdStuAwardsFile = rcdStuAwardsFile == null ? null : rcdStuAwardsFile.trim();
    }

    public String getRcdStuGrade() {
        return rcdStuGrade;
    }

    public void setRcdStuGrade(String rcdStuGrade) {
        this.rcdStuGrade = rcdStuGrade == null ? null : rcdStuGrade.trim();
    }

    public String getRcdStuGradeFile() {
        return rcdStuGradeFile;
    }

    public void setRcdStuGradeFile(String rcdStuGradeFile) {
        this.rcdStuGradeFile = rcdStuGradeFile == null ? null : rcdStuGradeFile.trim();
    }

    public Date getRcdStartscoolDate() {
        return rcdStartscoolDate;
    }

    public void setRcdStartscoolDate(Date rcdStartscoolDate) {
        this.rcdStartscoolDate = rcdStartscoolDate;
    }

    public String getRcdWantCtry() {
        return rcdWantCtry;
    }

    public void setRcdWantCtry(String rcdWantCtry) {
        this.rcdWantCtry = rcdWantCtry == null ? null : rcdWantCtry.trim();
    }

    public String getRcdWantCourse() {
        return rcdWantCourse;
    }

    public void setRcdWantCourse(String rcdWantCourse) {
        this.rcdWantCourse = rcdWantCourse == null ? null : rcdWantCourse.trim();
    }

    public String getRcdRemark() {
        return rcdRemark;
    }

    public void setRcdRemark(String rcdRemark) {
        this.rcdRemark = rcdRemark == null ? null : rcdRemark.trim();
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getRcdMemberId() {
		return rcdMemberId;
	}

	public void setRcdMemberId(String rcdMemberId) {
		this.rcdMemberId = rcdMemberId;
	}
    
}