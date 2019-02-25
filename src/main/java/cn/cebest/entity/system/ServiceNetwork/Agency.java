package cn.cebest.entity.system.ServiceNetwork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Video;
import cn.cebest.entity.system.content.Content;
import cn.cebest.util.ExtendFiledUtil;
/**
 * 内容
 * @author liqk
 *
 */
public class Agency implements Serializable {
	
	private static final long serialVersionUID = -3118882611666749785L;
	
	private String id; //主键id
	private String siteId; //站点id
	private String name; //代理商名称
	private String email; //代理商电子邮件
	private String continentId;	//代理商所属大洲id
	private String continentCN; //代理商所属达州
	private String countryId; //代理商所属国家id
	private String countryCN; //代理商所属国家
	
	private String countryZH; //
	private String countryKO; //代理商所属国家
	private String countryVI; //代理商所属国家
	private String countryTH; //代理商所属国家
	private String countryID; //代理商所属国家
	private String countryRU; //代理商所属国家
	private String countryAR; //代理商所属国家
	private String countryES; //代理商所属国家
	private String countryPT; //代理商所属国家
	private String countryEN; //代理商所属国家
	private String country;
	private String continentEN; //代理商所属达州
	private String continentKO; //代理商所属国家
	private String continentVI; //代理商所属国家
	private String continentTH; //代理商所属国家
	private String continentID; //代理商所属国家
	private String continentRU; //代理商所属国家
	private String continentAR; //代理商所属国家
	private String continentES; //代理商所属国家
	private String continentPT; //代理商所属国家
	private String continentZH; //代理商所属国家
	private String continent;
	
	private String characterId; //代理商性质id
	private String characterCN; //代理商性质
	private String characterEN; //代理商性质
	private String character;
	private String createdTime; //创建时间
	private String updateTime; //更新时间
	private String releaseTime; //发布时间
	private String productCount; //代理产品数量
	private String url;
	private String address;
	private String characterCode;
	private String productSup;//产品补充
	
	private String productId;
	private String productName;
	private String[] productIds;
	private String productIdsLook;//产品ids回显用
	
	private String imageUrl;
	private String topPercent;
	private String leftPercent;
	
	private String phone;
	private String company;
	private String questionOrCommit;
	
	private String top;
	private String recommend;
	private String contentTitle;
	private String columName;
	private List<Agency> columConfigList;
	private String pageFlag;

	
	public String getPageFlag() {
		return pageFlag;
	}

	public void setPageFlag(String pageFlag) {
		this.pageFlag = pageFlag;
	}

	public String getProductSup() {
		return productSup;
	}

	public void setProductSup(String productSup) {
		this.productSup = productSup;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContinentKO() {
		return continentKO;
	}

	public void setContinentKO(String continentKO) {
		this.continentKO = continentKO;
	}

	public String getContinentVI() {
		return continentVI;
	}

	public void setContinentVI(String continentVI) {
		this.continentVI = continentVI;
	}

	public String getContinentTH() {
		return continentTH;
	}

	public void setContinentTH(String continentTH) {
		this.continentTH = continentTH;
	}

	public String getContinentID() {
		return continentID;
	}

	public void setContinentID(String continentID) {
		this.continentID = continentID;
	}

	public String getContinentRU() {
		return continentRU;
	}

	public void setContinentRU(String continentRU) {
		this.continentRU = continentRU;
	}

	public String getContinentAR() {
		return continentAR;
	}

	public void setContinentAR(String continentAR) {
		this.continentAR = continentAR;
	}

	public String getContinentES() {
		return continentES;
	}

	public void setContinentES(String continentES) {
		this.continentES = continentES;
	}

	public String getContinentPT() {
		return continentPT;
	}

	public void setContinentPT(String continentPT) {
		this.continentPT = continentPT;
	}

	public String getContinentZH() {
		return continentZH;
	}

	public void setContinentZH(String continentZH) {
		this.continentZH = continentZH;
	}

	public String getContinent() {
		return continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryZH() {
		return countryZH;
	}

	public void setCountryZH(String countryZH) {
		this.countryZH = countryZH;
	}

	public String getCountryKO() {
		return countryKO;
	}

	public void setCountryKO(String countryKO) {
		this.countryKO = countryKO;
	}

	public String getCountryVI() {
		return countryVI;
	}

	public void setCountryVI(String countryVI) {
		this.countryVI = countryVI;
	}

	public String getCountryTH() {
		return countryTH;
	}

	public void setCountryTH(String countryTH) {
		this.countryTH = countryTH;
	}

	public String getCountryID() {
		return countryID;
	}

	public void setCountryID(String countryID) {
		this.countryID = countryID;
	}

	public String getCountryRU() {
		return countryRU;
	}

	public void setCountryRU(String countryRU) {
		this.countryRU = countryRU;
	}

	public String getCountryAR() {
		return countryAR;
	}

	public void setCountryAR(String countryAR) {
		this.countryAR = countryAR;
	}

	public String getCountryES() {
		return countryES;
	}

	public void setCountryES(String countryES) {
		this.countryES = countryES;
	}

	public String getCountryPT() {
		return countryPT;
	}

	public void setCountryPT(String countryPT) {
		this.countryPT = countryPT;
	}

	public String getCharacterCode() {
		return characterCode;
	}

	public void setCharacterCode(String characterCode) {
		this.characterCode = characterCode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getProductIdsLook() {
		return productIdsLook;
	}

	public void setProductIdsLook(String productIdsLook) {
		this.productIdsLook = productIdsLook;
	}

	public String[] getProductIds() {
		return productIds;
	}

	public void setProductIds(String[] productIds) {
		this.productIds = productIds;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<Agency> getColumConfigList() {
		return columConfigList;
	}

	public void setColumConfigList(List<Agency> columConfigList) {
		this.columConfigList = columConfigList;
	}

	public String getContentTitle() {
		return contentTitle;
	}

	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}

	public String getColumName() {
		return columName;
	}

	public void setColumName(String columName) {
		this.columName = columName;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
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

	public String getProductCount() {
		return productCount;
	}

	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
