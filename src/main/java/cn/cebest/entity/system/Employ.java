package cn.cebest.entity.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.cebest.util.ExtendFiledUtil;
/**
 * 内容
 * @author liu
 *
 */
public class Employ implements Serializable {
	
	private static final long serialVersionUID = -3118882611666749785L;
	
	private List<ExtendFiledUtil> fileds=new ArrayList<>();
	private String filedJson;
	
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
	
}
