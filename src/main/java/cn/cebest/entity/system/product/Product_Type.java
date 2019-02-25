package cn.cebest.entity.system.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Template;

public class Product_Type implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String type_name;
	private String product_name;
	private String siteid;
	private String imageid;
	private String imgurl;
	private String title;
	private String type_status;
	private String type_txt;
	private String type_keywords;
	private String type_summary;
	private String pid;
	private String pname;
	private String type_wburl;
	private String created_time;
	private String update_time;
	private List<Product_Type> childList;
	private String txt_ext;
	private String [] columnids;
	private List<ColumConfig> columConfigList=new ArrayList<>();
	private Integer count;
	private String templateid;
	private Integer sort;
	private Template template=new Template();
	private String seo_description;
	private List<Image> imageList=new ArrayList<>(); //配图
	private String typeUrlName;
	
	public String getTypeUrlName() {
		return typeUrlName;
	}

	public void setTypeUrlName(String typeUrlName) {
		this.typeUrlName = typeUrlName;
	}
	
	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public List<Image> getImageList() {
		return imageList;
	}

	public void setImageList(List<Image> imageList) {
		this.imageList = imageList;
	}

	public String getSeo_description() {
		return seo_description;
	}

	public void setSeo_description(String seo_description) {
		this.seo_description = seo_description;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public String getTemplateid() {
		return templateid;
	}

	public void setTemplateid(String templateid) {
		this.templateid = templateid;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<ColumConfig> getColumConfigList() {
		return columConfigList;
	}

	public void setColumConfigList(List<ColumConfig> columConfigList) {
		this.columConfigList = columConfigList;
	}

	public String getTxt_ext() {
		return txt_ext;
	}

	public void setTxt_ext(String txt_ext) {
		this.txt_ext = txt_ext;
	}

	public String[] getColumnids() {
		return columnids;
	}

	public void setColumnids(String[] columnids) {
		this.columnids = columnids;
	}

	public String getCreated_time() {
		return created_time;
	}

	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getType_wburl() {
		return type_wburl;
	}

	public void setType_wburl(String type_wburl) {
		this.type_wburl = type_wburl;
	}

	public List<Product_Type> getChildList() {
		return childList;
	}

	public void setChildList(List<Product_Type> childList) {
		this.childList = childList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getSiteid() {
		return siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}

	public String getImageid() {
		return imageid;
	}

	public void setImageid(String imageid) {
		this.imageid = imageid;
	}

	public String getType_status() {
		return type_status;
	}

	public void setType_status(String type_status) {
		this.type_status = type_status;
	}

	public String getType_txt() {
		return type_txt;
	}

	public void setType_txt(String type_txt) {
		this.type_txt = type_txt;
	}

	public String getType_keywords() {
		return type_keywords;
	}

	public void setType_keywords(String type_keywords) {
		this.type_keywords = type_keywords;
	}

	public String getType_summary() {
		return type_summary;
	}

	public void setType_summary(String type_summary) {
		this.type_summary = type_summary;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
}
