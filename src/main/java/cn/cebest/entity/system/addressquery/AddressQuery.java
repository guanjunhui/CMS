package cn.cebest.entity.system.addressquery;

import java.io.Serializable;
import java.sql.Date;

public class AddressQuery implements Serializable {

	private static final long serialVersionUID = -3118882611666749786L;

	private String siteId;
	private String id;
	private String province;
	private String city;
	private String area;
	private String brand;
	private String xPoint;
	private String yPoint;
	private String myName;
	private String tel;
	private String address;
	private String createTime;

	
	
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getxPoint() {
		return xPoint;
	}

	public void setxPoint(String xPoint) {
		this.xPoint = xPoint;
	}

	public String getyPoint() {
		return yPoint;
	}

	public void setyPoint(String yPoint) {
		this.yPoint = yPoint;
	}

	public String getMyName() {
		return myName;
	}

	public void setMyName(String myName) {
		this.myName = myName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Override
	public String toString() {
		return "AddressQuery [siteId=" + siteId + ", id=" + id + ", province=" + province + ", city=" + city + ", area="
				+ area + ", brand=" + brand + ", xPoint=" + xPoint + ", yPoint=" + yPoint + ", myName=" + myName
				+ ", tel=" + tel + ", address=" + address + ", createTime=" + createTime + "]";
	}
	
}
