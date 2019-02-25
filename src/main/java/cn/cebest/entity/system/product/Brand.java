package cn.cebest.entity.system.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Brand implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String pid;
	private List<Brand> childList=new ArrayList<>();
	//图片信息
	private String imageid;
	private String imgurl;
	private String title;
	private List<Product> productList;
	
	public List<Product> getProductList() {
		return productList;
	}
	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}
	public String getImageid() {
		return imageid;
	}
	public void setImageid(String imageid) {
		this.imageid = imageid;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public List<Brand> getChildList() {
		return childList;
	}
	public void setChildList(List<Brand> childList) {
		this.childList = childList;
	}
}
