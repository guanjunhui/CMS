package cn.cebest.entity.system.content;

/**
 * 栏目、内容、内容分类的关系实体
 * @author mingpinglin<mingpinglin@300.cn>
 *
 */

public class Content_Type_Colum {
	private static final long serialVersionUID = -3118882611666749785L;
	
	private String contentId; //内容id
	private String contentTypeId; //内容分类id
	private String columnId; //栏目id
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getContentTypeId() {
		return contentTypeId;
	}
	public void setContentTypeId(String contentTypeId) {
		this.contentTypeId = contentTypeId;
	}
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	
}
