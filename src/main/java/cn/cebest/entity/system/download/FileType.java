package cn.cebest.entity.system.download;

import java.util.ArrayList;
import java.util.List;

import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Template;
import cn.cebest.util.ExtendFiledUtil;

/**
 * 下载分类实体类
 * 
 * @author wzd
 *
 */
public class FileType {
	private String download_id;
	private String pid;
	private String pname;
	private String download_name;
	private String wburl;
	private String created_time;
	private String update_time;
	private String summary;
	private String imageid;
	private String keywords;
	private String siteid;
	private String status;
	private String[] columnids;
	private String TXT;
	private List<ColumConfig> columConfigList;
	private List<FileType> childList;
	private String title;
	private String imgurl;
	private Integer count;
	private String templateid;
	private String is_home;
	private Template template=new Template();
	private String seo_description;
	
	private String filedJson; 
	private List<ExtendFiledUtil> fileds=new ArrayList<>();
	private String filepath;//下载文件路径
	private String filetypeId;
	private String columid;//栏目id
	private List<DownloadFiles> fileList;
	private String filetypename;
	private String typeUrlName;
	
	public String getTypeUrlName() {
		return typeUrlName;
	}

	public void setTypeUrlName(String typeUrlName) {
		this.typeUrlName = typeUrlName;
	}
	
	public String getFiletypename() {
		return filetypename;
	}

	public void setFiletypename(String filetypename) {
		this.filetypename = filetypename;
	}

	public List<DownloadFiles> getFileList() {
		return fileList;
	}

	public void setFileList(List<DownloadFiles> fileList) {
		this.fileList = fileList;
	}

	public String getColumid() {
		return columid;
	}

	public void setColumid(String columid) {
		this.columid = columid;
	}

	public String getFiletypeId() {
		return filetypeId;
	}

	public void setFiletypeId(String filetypeId) {
		this.filetypeId = filetypeId;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
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

	public String getSeo_description() {
		return seo_description;
	}

	public void setSeo_description(String seo_description) {
		this.seo_description = seo_description;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getIs_home() {
		return is_home;
	}

	public void setIs_home(String is_home) {
		this.is_home = is_home;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	private Integer sort;
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getTemplateid() {
		return templateid;
	}

	public void setTemplateid(String templateid) {
		this.templateid = templateid;
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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getTXT() {
		return TXT;
	}

	public void setTXT(String tXT) {
		TXT = tXT;
	}

	
	
	public List<ColumConfig> getColumConfigList() {
		return columConfigList;
	}

	public void setColumConfigList(List<ColumConfig> columConfigList) {
		this.columConfigList = columConfigList;
	}

	public String[] getColumnids() {
		return columnids;
	}

	public void setColumnids(String[] columnids) {
		this.columnids = columnids;
	}

	public List<FileType> getChildList() {
		return childList;
	}

	public void setChildList(List<FileType> childList) {
		this.childList = childList;
	}

	public String getDownload_id() {
		return download_id;
	}

	public void setDownload_id(String download_id) {
		this.download_id = download_id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getDownload_name() {
		return download_name;
	}

	public void setDownload_name(String download_name) {
		this.download_name = download_name;
	}

	public String getWburl() {
		return wburl;
	}

	public void setWburl(String wburl) {
		this.wburl = wburl;
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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getImageid() {
		return imageid;
	}

	public void setImageid(String imageid) {
		this.imageid = imageid;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getSiteid() {
		return siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
