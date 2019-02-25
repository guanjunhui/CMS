package cn.cebest.entity.system.solr;

import java.util.List;

import cn.cebest.entity.Page;

public class SearchResult {

	private List<Search> dataList;
	private int recordCount;	//记录数
	private int pageCount;		//总页数
	private int curPage;		//当前页
	private Page page;
	
	
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public List<Search> getDataList() {
		return dataList;
	}
	public void setDataList(List<Search> dataList) {
		this.dataList = dataList;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getCurPage() {
		return curPage;
	}
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	@Override
	public String toString() {
		return "SearchResult [dataList=" + dataList + ", recordCount=" + recordCount + ", pageCount=" + pageCount
				+ ", curPage=" + curPage + "]";
	}
	
	
	
}
