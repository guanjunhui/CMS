package cn.cebest.entity.system.application;

import java.io.Serializable;

/**
 * 江心味业合作申请
 * @author lwt
 * @time 2018年8月30号
 */
@SuppressWarnings("serial")
public class Application implements Serializable {
	private Integer id;//主键id
	private String item;//事项
	private String industry;//行业
	private String field;//领域
	private String companyname;//公司名称
	private String establishment;//成立时间
	private String website;//官方网站
	private String registercapital;//注册资本
	private String number;//员工数量
	private String annualsales;//年销售额
	private String annualoutput;//年产量
	private String companyaddress;//公司地址
	private String workshoparea;//厂房面积
	private String fullname;//联系人姓名
	private String post;//职位
	private String contactnumber;//联系电话
	private String email;//电子邮件
	private String createtime;//申请时间
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}
	public String getEstablishment() {
		return establishment;
	}
	public void setEstablishment(String establishment) {
		this.establishment = establishment;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getRegistercapital() {
		return registercapital;
	}
	public void setRegistercapital(String registercapital) {
		this.registercapital = registercapital;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getAnnualsales() {
		return annualsales;
	}
	public void setAnnualsales(String annualsales) {
		this.annualsales = annualsales;
	}
	public String getAnnualoutput() {
		return annualoutput;
	}
	public void setAnnualoutput(String annualoutput) {
		this.annualoutput = annualoutput;
	}
	public String getCompanyaddress() {
		return companyaddress;
	}
	public void setCompanyaddress(String companyaddress) {
		this.companyaddress = companyaddress;
	}
	public String getWorkshoparea() {
		return workshoparea;
	}
	public void setWorkshoparea(String workshoparea) {
		this.workshoparea = workshoparea;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getContactnumber() {
		return contactnumber;
	}
	public void setContactnumber(String contactnumber) {
		this.contactnumber = contactnumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
}
