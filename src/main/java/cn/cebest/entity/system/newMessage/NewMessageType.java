package cn.cebest.entity.system.newMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Template;
import cn.cebest.util.PageData;
public class NewMessageType implements Serializable{
	private String id;//主键id
	private String type_name;//分类名称
	private String siteid;//站點id
	private String type_id;//所属分类 
	private String imageid;//封面图片
	private String imgurl;//图片地址
	private String type_status;//状态
	private String type_detail;//详细描述
	private String type_keywords;//关键词
	private String type_summary;//概要描述
	private String type_wburl;//外部链接
	private String columnid;//所属模板
	private String created_time;//资讯创建时间
	private String update_time;//资讯更新时间
	private String pid;//表自连接使用父id
	private List<ColumConfig> columConfigList=new ArrayList<ColumConfig>();//资讯所属栏目
	private List<NewMessageType> childList;//所属资讯类型
	private String [] columnids;//所属栏目id集合
	private String txt_ext;//
	private Integer count;
	private Integer sort;//做排序使用
	private Template template;
	private String columId;
	private Image image;//用来存储封面图片的详细内容
	private PageData pd;
	private String typeUrlName;
	
	public String getTypeUrlName() {
		return typeUrlName;
	}

	public void setTypeUrlName(String typeUrlName) {
		this.typeUrlName = typeUrlName;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public PageData getPd() {
		return pd;
	}
	public void setPd(PageData pd) {
		this.pd = pd;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSiteid() {
		return siteid;
	}
	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String type_id) {
		this.type_id = type_id;
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
	public String getType_detail() {
		return type_detail;
	}
	public void setType_detail(String type_detail) {
		this.type_detail = type_detail;
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
	public String getType_wburl() {
		return type_wburl;
	}
	public void setType_wburl(String type_wburl) {
		this.type_wburl = type_wburl;
	}
	public String getColumnid() {
		return columnid;
	}
	public void setColumnid(String columnid) {
		this.columnid = columnid;
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
	public List<ColumConfig> getColumConfigList() {
		return columConfigList;
	}
	public void setColumConfigList(List<ColumConfig> columConfigList) {
		this.columConfigList = columConfigList;
	}
	public List<NewMessageType> getChildList() {
		return childList;
	}
	public void setChildList(List<NewMessageType> childList) {
		this.childList = childList;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String[] getColumnids() {
		return columnids;
	}
	public void setColumnids(String[] columnids) {
		this.columnids = columnids;
	}
	public String getTxt_ext() {
		return txt_ext;
	}
	public void setTxt_ext(String txt_ext) {
		this.txt_ext = txt_ext;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Template getTemplate() {
		return template;
	}
	public void setTemplate(Template template) {
		this.template = template;
	}
	public String getColumId() {
		return columId;
	}
	public void setColumId(String columId) {
		this.columId = columId;
	}
	
}
