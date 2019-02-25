package cn.cebest.entity.system;

import java.io.Serializable;

/**
 * 图片
 * @author liqk
 *
 */
public class Image implements Serializable {
	
	private static final long serialVersionUID = 8501438477501594896L;
	
	private String imageId; //主键id
	private String color;//颜色
	private String title; //标题
	private String subhead;//副标题
	private String imgurl; //图片地址
	private String tourl; //跳转地址
	private String type; //类型
	private Integer forder; //排序
	private String bz; //备注
	private String master_id;
	private String name;//图片原始名字
	private boolean flag=false;
	
	//图片的无参构造
	public Image() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Image(String imageId,String color, String title, String subhead, String imgurl, String tourl, String type, Integer forder,
			String bz, String master_id, String name, boolean flag) {
		super();
		this.imageId = imageId;
		this.title = title;
		this.subhead = subhead;
		this.imgurl = imgurl;
		this.tourl = tourl;
		this.type = type;
		this.forder = forder;
		this.bz = bz;
		this.master_id = master_id;
		this.name = name;
		this.flag = flag;
		this.color = color;
	}
	
	
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMaster_id() {
		return master_id;
	}
	public void setMaster_id(String master_id) {
		this.master_id = master_id;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
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
	public String getTourl() {
		return tourl;
	}
	public void setTourl(String tourl) {
		this.tourl = tourl;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getForder() {
		return forder;
	}
	public void setForder(Integer forder) {
		this.forder = forder;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getSubhead() {
		return subhead;
	}
	public void setSubhead(String subhead) {
		this.subhead = subhead;
	}

	@Override
	public String toString() {
		return "Image [imageId=" + imageId + ", title=" + title + ", subhead=" + subhead + ", imgurl=" + imgurl
				+ ", tourl=" + tourl + ", type=" + type + ", forder=" + forder + ", bz=" + bz + ", master_id="
				+ master_id + ", name=" + name + ", flag=" + flag + "]";
	}
	
}
