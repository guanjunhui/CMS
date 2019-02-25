package cn.cebest.entity.system.ServiceNetwork;

import java.io.Serializable;
/**
 * 内容
 * @author liqk
 *
 */
public class ServiceNetwork implements Serializable {
	
	private static final long serialVersionUID = 7627265406958836056L;
	
	private String id; //主键id
	private String name; //服务网点名称
	private String address; //地址
	private String contact; //联系人
	private String phone;  //电话
	private String email; //服务网点电子邮件
	private String character;//服务网点性质
	private String serviceNatureId;		//服务网点性质id
	private String serviceNatureName;	//服务网点性质名称
	private String longitude;	//经度
	private String latitude;	//纬度
	private String catagory;	//种类
	private String serviceNatureCode;//服务商性质编码
	
	private String siteId; //站点id
	private String continentId;	//服务网点所属大洲id
	private String continentCN; //服务网点所属大洲-中文
	private String continentEN; //服务网点所属达州
	private String countryId; //服务网点所属国家id
	private String countryCN; //服务网点所属国家-中文
	private String countryEN; //服务网点所属国家
	private String characterId; //代理商性质id
	private String characterCN; //代理商性质
	private String characterEN; //代理商性质
	private String createdTime; //创建时间
	private String updateTime; //更新时间
	private String releaseTime; //发布时间
	
	private String topPercent;
	private String leftPercent;
	
	private String company;
	private String questionOrCommit;
	
	public String getServiceNatureCode() {
		return serviceNatureCode;
	}
	public void setServiceNatureCode(String serviceNatureCode) {
		this.serviceNatureCode = serviceNatureCode;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getCatagory() {
		return catagory;
	}
	public void setCatagory(String catagory) {
		this.catagory = catagory;
	}
	public String getServiceNatureId() {
		return serviceNatureId;
	}
	public void setServiceNatureId(String serviceNatureId) {
		this.serviceNatureId = serviceNatureId;
	}
	public String getServiceNatureName() {
		return serviceNatureName;
	}
	public void setServiceNatureName(String serviceNatureName) {
		this.serviceNatureName = serviceNatureName;
	}
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
		this.siteId = siteId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContinentId() {
		return continentId;
	}
	public void setContinentId(String continentId) {
		this.continentId = continentId;
	}
	public String getContinentCN() {
		return continentCN;
	}
	public void setContinentCN(String continentCN) {
		this.continentCN = continentCN;
	}
	public String getContinentEN() {
		return continentEN;
	}
	public void setContinentEN(String continentEN) {
		this.continentEN = continentEN;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getCountryCN() {
		return countryCN;
	}
	public void setCountryCN(String countryCN) {
		this.countryCN = countryCN;
	}
	public String getCountryEN() {
		return countryEN;
	}
	public void setCountryEN(String countryEN) {
		this.countryEN = countryEN;
	}
	public String getCharacterId() {
		return characterId;
	}
	public void setCharacterId(String characterId) {
		this.characterId = characterId;
	}
	public String getCharacterCN() {
		return characterCN;
	}
	public void setCharacterCN(String characterCN) {
		this.characterCN = characterCN;
	}
	public String getCharacterEN() {
		return characterEN;
	}
	public void setCharacterEN(String characterEN) {
		this.characterEN = characterEN;
	}
	public String getCharacter() {
		return character;
	}
	public void setCharacter(String character) {
		this.character = character;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}
	public String getTopPercent() {
		return topPercent;
	}
	public void setTopPercent(String topPercent) {
		this.topPercent = topPercent;
	}
	public String getLeftPercent() {
		return leftPercent;
	}
	public void setLeftPercent(String leftPercent) {
		this.leftPercent = leftPercent;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getQuestionOrCommit() {
		return questionOrCommit;
	}
	public void setQuestionOrCommit(String questionOrCommit) {
		this.questionOrCommit = questionOrCommit;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	
	
}
