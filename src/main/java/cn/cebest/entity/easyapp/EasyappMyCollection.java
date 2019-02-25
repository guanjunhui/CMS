package cn.cebest.entity.easyapp;

import java.io.Serializable;
import java.util.Date;

public class EasyappMyCollection implements Serializable{
	private static final long serialVersionUID = 1L;

	private String id;

    private String memId;//会员ID

    private String scId;//学校ID

    private String applyStatus;//状态（1：未申请 2：已申请  3：申请通过）

    private Date createdTime;//创建时间

    private String siteId;//站点ID

    private String remark;//备注
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMemId() {
        return memId;
    }

    public void setMemId(String memId) {
        this.memId = memId == null ? null : memId.trim();
    }

    public String getScId() {
        return scId;
    }

    public void setScId(String scId) {
        this.scId = scId == null ? null : scId.trim();
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus == null ? null : applyStatus.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId == null ? null : siteId.trim();
    }

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
}