package cn.cebest.entity.system;

import java.io.Serializable;
import java.util.Date;
/**
 * 视频
 * @author lwt
 *
 */
public class Video implements Serializable {
	private static final long serialVersionUID =8501438468501594896L;
	private String id;//视频主键ID
	private String video_title;//视频标题
	private String video_content;//视频的信息
	private String video_url;//视频连接
	private String video_type;//视频类型
	private Date createtime;//创建时间
	private String master_id;//附属id
	private String tourl;//跳转路径
	private boolean flag=false;
	private String name;
	private Integer forder;//跳转路径
	private String releaseTime;
	
	public String getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getForder() {
		return forder;
	}
	public void setForder(Integer forder) {
		this.forder = forder;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getTourl() {
		return tourl;
	}
	public void setTourl(String tourl) {
		this.tourl = tourl;
	}
	public String getMaster_id() {
		return master_id;
	}
	public void setMaster_id(String master_id) {
		this.master_id = master_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVideo_title() {
		return video_title;
	}
	public void setVideo_title(String video_title) {
		this.video_title = video_title;
	}
	public String getVideo_content() {
		return video_content;
	}
	public void setVideo_content(String video_content) {
		this.video_content = video_content;
	}
	public String getVideo_url() {
		return video_url;
	}
	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}
	public String getVideo_type() {
		return video_type;
	}
	public void setVideo_type(String video_type) {
		this.video_type = video_type;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
