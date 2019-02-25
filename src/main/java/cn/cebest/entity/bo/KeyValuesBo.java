package cn.cebest.entity.bo;

import java.io.Serializable;


public class KeyValuesBo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String key;
	private String values;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValues() {
		return values;
	}
	public void setValues(String values) {
		this.values = values;
	}
	
}
