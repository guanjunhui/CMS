package cn.cebest.entity.system;

import java.io.Serializable;
import java.util.List;

import cn.cebest.util.ExtendFiledUtil;

public class Recruit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String recruitTitle;// 招聘标题名称

	private String recruitPosition;// 招聘职位

	private String workAreas;// 工作地区

	private String jobDescription;// 职位介绍
	
	private String jobRequire;// 职位介绍
	
	private String jobDsssimple;// 福利待遇
	
	private String created_Time;// 创建时间
	
	private String updateTime;// 修改时间
	
	private String releaseTime;// 发布时间
	
	private Integer recruit_peoplenum;//招聘人数
	
	private String release_time;//发布时间
	
	private String education_required;//学历要求
	
	private String columid;//栏目id
	
	private List<ColumConfig> columConfigList;
	
	private String status;//状态(0:禁用,1:启用,2:删除)
	
	private Integer sort;
	
	private String filedJson;
	
	private String ifrecommend;
    private String recommend_time;
    private String ifhot;
    private String hot_time;
    private String iftop;
    private String top_time;
    private Seo seoInfo;
	private String seo_title;
	private String seo_keywords;
	private String seo_description;
	
	private String workAge;//工作年限
	private String sex;//性别
	private String work_category;//工作性质
	private String salary_range;//薪资范围
	private String age_required;//年龄要求
	
	private List<ExtendFiledUtil> fileds;
	
	private String contentUrlName;
	private String columUrlName;
	
	public String getColumUrlName() {
		return columUrlName;
	}
	public void setColumUrlName(String columUrlName) {
		this.columUrlName = columUrlName;
	}
	public String getContentUrlName() {
		return contentUrlName;
	}
	public void setContentUrlName(String contentUrlName) {
		this.contentUrlName = contentUrlName;
	}
	public List<ExtendFiledUtil> getFileds() {
		return fileds;
	}

	public void setFileds(List<ExtendFiledUtil> fileds) {
		this.fileds = fileds;
	}

	public String getFiledJson() {
		return filedJson;
	}

	public void setFiledJson(String filedJson) {
		this.filedJson = filedJson;
	}

	public String getWorkAge() {
		return workAge;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getWork_category() {
		return work_category;
	}

	public void setWork_category(String work_category) {
		this.work_category = work_category;
	}

	public String getSalary_range() {
		return salary_range;
	}

	public void setSalary_range(String salary_range) {
		this.salary_range = salary_range;
	}

	public String getAge_required() {
		return age_required;
	}

	public void setAge_required(String age_required) {
		this.age_required = age_required;
	}

	public void setWorkAge(String workAge) {
		this.workAge = workAge;
	}
	public Seo getSeoInfo() {
		return seoInfo;
	}

	public void setSeoInfo(Seo seoInfo) {
		this.seoInfo = seoInfo;
	}

	public String getSeo_title() {
		return seo_title;
	}

	public void setSeo_title(String seo_title) {
		this.seo_title = seo_title;
	}

	public String getSeo_keywords() {
		return seo_keywords;
	}

	public void setSeo_keywords(String seo_keywords) {
		this.seo_keywords = seo_keywords;
	}

	public String getSeo_description() {
		return seo_description;
	}

	public void setSeo_description(String seo_description) {
		this.seo_description = seo_description;
	}

	public String getIfrecommend() {
		return ifrecommend;
	}

	public void setIfrecommend(String ifrecommend) {
		this.ifrecommend = ifrecommend;
	}

	public String getIfhot() {
		return ifhot;
	}

	public void setIfhot(String ifhot) {
		this.ifhot = ifhot;
	}

	public String getIftop() {
		return iftop;
	}

	public void setIftop(String iftop) {
		this.iftop = iftop;
	}

	public String getRecommend_time() {
		return recommend_time;
	}

	public void setRecommend_time(String recommend_time) {
		this.recommend_time = recommend_time;
	}

	public String getHot_time() {
		return hot_time;
	}

	public void setHot_time(String hot_time) {
		this.hot_time = hot_time;
	}

	public String getTop_time() {
		return top_time;
	}

	public void setTop_time(String top_time) {
		this.top_time = top_time;
	}

	public String getCreated_Time() {
		return created_Time;
	}


	public void setCreated_Time(String created_Time) {
		this.created_Time = created_Time;
	}


	public List<ColumConfig> getColumConfigList() {
		return columConfigList;
	}


	public void setColumConfigList(List<ColumConfig> columConfigList) {
		this.columConfigList = columConfigList;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Integer getSort() {
		return sort;
	}


	public void setSort(Integer sort) {
		this.sort = sort;
	}


	public String getUpdateTime() {
		return updateTime;
	}
	

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRecruitTitle() {
		return recruitTitle;
	}

	public void setRecruitTitle(String recruitTitle) {
		this.recruitTitle = recruitTitle;
	}

	public String getRecruitPosition() {
		return recruitPosition;
	}

	public void setRecruitPosition(String recruitPosition) {
		this.recruitPosition = recruitPosition;
	}

	public String getWorkAreas() {
		return workAreas;
	}

	public void setWorkAreas(String workAreas) {
		this.workAreas = workAreas;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getJobDsssimple() {
		return jobDsssimple;
	}

	public void setJobDsssimple(String jobDsssimple) {
		this.jobDsssimple = jobDsssimple;
	}

	public Integer getRecruit_peoplenum() {
		return recruit_peoplenum;
	}

	public void setRecruit_peoplenum(Integer recruit_peoplenum) {
		this.recruit_peoplenum = recruit_peoplenum;
	}

	public String getRelease_time() {
		return release_time;
	}

	public void setRelease_time(String release_time) {
		this.release_time = release_time;
	}

	public String getEducation_required() {
		return education_required;
	}

	public void setEducation_required(String education_required) {
		this.education_required = education_required;
	}

	public String getColumid() {
		return columid;
	}

	public void setColumid(String columid) {
		this.columid = columid;
	}

	public String getJobRequire() {
		return jobRequire;
	}

	public void setJobRequire(String jobRequire) {
		this.jobRequire = jobRequire;
	}

}