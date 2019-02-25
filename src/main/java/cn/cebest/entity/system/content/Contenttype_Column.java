package cn.cebest.entity.system.content;

/**
 * 栏目和内容分类的关系实体
 * @author mingpinglin<mingpinglin@300.cn>
 *
 */


public class Contenttype_Column {
	
	private static final long serialVersionUID = -3118882611666749785L;
	
	private String columnId;  //栏目id
	private String contentTypeId; //内容分类id
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	public String getContentTypeId() {
		return contentTypeId;
	}
	public void setContentTypeId(String contentTypeId) {
		this.contentTypeId = contentTypeId;
	}
	
}
