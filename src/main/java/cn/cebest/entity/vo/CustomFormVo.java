package cn.cebest.entity.vo;

import java.io.Serializable;
import java.util.List;

import cn.cebest.entity.bo.FormRecordBo;

public class CustomFormVo implements Serializable{

	private static final long serialVersionUID = -4682116242123120401L;

	private List<FormRecordBo> recordHead;//表单项
	private List<List<FormRecordBo>> recordData;//表单记录集合
	
	public List<FormRecordBo> getRecordHead() {
		return recordHead;
	}
	public List<List<FormRecordBo>> getRecordData() {
		return recordData;
	}
	public void setRecordHead(List<FormRecordBo> recordHead) {
		this.recordHead = recordHead;
	}
	public void setRecordData(List<List<FormRecordBo>> recordData) {
		this.recordData = recordData;
	}
	
}
