package cn.cebest.service.system.solr.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.solr.Search;
import cn.cebest.entity.system.solr.SearchResult;
import cn.cebest.service.system.solr.SolrService;
import cn.cebest.util.CeResultUtil;
import cn.cebest.util.ExceptionUtil;
import cn.cebest.util.Logger;
import cn.cebest.util.PageData;
import cn.cebest.util.SystemConfig;

@Service("solrService")
public class SolrServiceImpl implements SolrService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Autowired
	private SolrServer solrServer;
	protected Logger logger = Logger.getLogger(this.getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	public CeResultUtil importAll() {
		String columParentId= SystemConfig.getPropertiesString("search.columParentId");
		try {
			PageData pd=new PageData();
			//查询列表
			List<Search> contentList = (List<Search>) dao.findForList("SearchMapper.getContentList",pd);
			List<Search> columList = (List<Search>) dao.findForList("SearchMapper.getColumList",pd);
			List<Search> messageList = (List<Search>) dao.findForList("SearchMapper.getMessageList",pd);
			List<Search> employList = (List<Search>) dao.findForList("SearchMapper.getEmployList",pd);
			//去除文件的全文检索结果
			List<Search> fileList = (List<Search>) dao.findForList("SearchMapper.getFileList",pd);
			List<Search> productList = (List<Search>) dao.findForList("SearchMapper.getProductList",pd);
			for (Search search : contentList) {
				Boolean checkNum = this.checkNum(search.getName());
				if(!checkNum){
					SolrInputDocument document = new SolrInputDocument();
					document.setField("id", search.getId());
					document.setField("search_name", search.getName());
					document.setField("search_type", "1");
					document.setField("search_des", search.getDes());
					document.setField("search_columnId", search.getColumnId());
					document.setField("search_time", search.getTime());
					document.setField("search_typeId", search.getTypeId());
					if(!StringUtils.isEmpty(columParentId)){
						document.setField(columParentId, search.getColumParentId());
					}
					
					//写入索引库
					solrServer.add(document);
				}
				
			}
			for (Search search : columList) {
				Boolean checkNum = this.checkNum(search.getName());
				if(!checkNum){
					SolrInputDocument document = new SolrInputDocument();
					document.setField("id", search.getId());
					document.setField("search_name", search.getName());
					document.setField("search_type", null);
					document.setField("search_des", search.getDes());
					document.setField("search_columnId", search.getColumnId());
					document.setField("search_time", search.getTime());
					document.setField("search_typeId", search.getTypeId());
					if(!StringUtils.isEmpty(columParentId)){
						document.setField(columParentId, search.getColumParentId());
					}
					
					//写入索引库
					solrServer.add(document);
				}
				
			}
			for (Search search : messageList) {
				SolrInputDocument document = new SolrInputDocument();
				document.setField("id", search.getId());
				document.setField("search_name", search.getName());
				document.setField("search_type", "2");
				document.setField("search_des", search.getDes());
				document.setField("search_columnId", search.getColumnId());
				document.setField("search_time", search.getTime());
				document.setField("search_typeId", search.getTypeId());
				
				//写入索引库
				solrServer.add(document);
			}
			for (Search search : employList) {
				SolrInputDocument document = new SolrInputDocument();
				document.setField("id", search.getId());
				document.setField("search_name", search.getName());
				document.setField("search_type", "4");
				document.setField("search_des", search.getDes());
				document.setField("search_columnId", search.getColumnId());
				document.setField("search_time", search.getTime());
				
				//写入索引库
				solrServer.add(document);
			}
			
			for (Search search : fileList) {
				SolrInputDocument document = new SolrInputDocument();
				document.setField("id", search.getId());
				document.setField("search_name", search.getName());
				document.setField("search_type", "5");
				document.setField("search_des", search.getDes());
				document.setField("search_columnId", search.getColumnId());
				document.setField("search_time", search.getTime());
				document.setField("search_typeId", search.getTypeId());
				//写入索引库
				solrServer.add(document);
			}
			
			for (Search search : productList) {
				SolrInputDocument document = new SolrInputDocument();
				document.setField("id", search.getId());
				document.setField("search_name", search.getName());
				document.setField("search_type", "3");
				document.setField("search_des", search.getDes());
				document.setField("search_columnId", search.getColumnId());
				document.setField("search_time", search.getTime());
				document.setField("search_typeId", search.getTypeId());
				
				//写入索引库
				solrServer.add(document);
			}
			//提交修改
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return CeResultUtil.build(500, ExceptionUtil.getStackTrace(e));
		}
		return CeResultUtil.ok();
	}

	@Override
	public SearchResult findSolrList(String queryString, Page page) throws Exception {
		//判断查询条件是否为空
		if (null == queryString ) {
			return null;
		}
		//创建一个SolrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		//设置查询条件
		solrQuery.setQuery(queryString);
		
		int currentPage = page.getCurrentPageOrgi();
		if (currentPage == 0) {
			currentPage = 1;
		}
		//设置分页条件
		solrQuery.setStart((currentPage - 1) * (page.getShowCount()));
		solrQuery.setRows(page.getShowCount());
		//默认搜索域
		solrQuery.set("df", "search_keywords");
		//高亮显示
		solrQuery.setHighlight(true);
        solrQuery.addHighlightField("search_name");
        solrQuery.addHighlightField("search_des");
        solrQuery.setHighlightSimplePre("<span style='color:red'>");
        solrQuery.setHighlightSimplePost("</span>");
		SearchResult searchResult = this.search(solrQuery);
		//计算总条数
		int recordCount = searchResult.getRecordCount();
		page.setTotalResult(recordCount);
		//
		int totalPage=recordCount/page.getShowCount();
		if(recordCount%page.getShowCount()!=0){
			totalPage++;
		}
		page.setTotalPage(totalPage);
		int pageCount = (int) (recordCount/page.getShowCount());
		if (recordCount % page.getShowCount() > 0) {
			pageCount++;
		}
		page.setTotalPage(pageCount);
		page.setTotalResult(recordCount);
		return searchResult;
	}
	
	
	
	
	
	public SearchResult search(SolrQuery solrQuery) throws Exception {
		//根据查询条件执行查询
		QueryResponse response = solrServer.query(solrQuery);
		//取查询结果
		SolrDocumentList solrDocumentList = response.getResults();
		//查询结果总数量
		long recordCount = solrDocumentList.getNumFound();
		SearchResult result = new SearchResult();
		//result.setRecordCount(recordCount.in);          Integer.parseInt(String.valueOf(ll))
		int parseInt = Integer.parseInt(String.valueOf(recordCount));
		result.setRecordCount(parseInt);
		//取高亮显示
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		//创建一个查询列表
		List<Search> searchList = new ArrayList<Search>();
		//取查询结果列表
		for (SolrDocument solrDocument : solrDocumentList) {
			Search search = new Search();
			search.setId((String) solrDocument.get("id"));
			//取高亮显示的结果
			List<String> searchNameList = highlighting.get(solrDocument.get("id")).get("search_name");
			List<String> searchDesList = highlighting.get(solrDocument.get("id")).get("search_des");
			String name = "";
			String searchDes = "";
			if (searchNameList != null && searchNameList.size()>0) {
				name = searchNameList.get(0);
			} else {
				name = (String) solrDocument.get("search_name");
			}
			if (searchDesList != null && searchDesList.size()>0) {
				searchDes = searchDesList.get(0);
			} else {
				searchDes = (String) solrDocument.get("search_des");
			}
			search.setName(name);
			search.setType((String) solrDocument.get("search_type"));
			search.setDes(searchDes);
			search.setColumnId((String) solrDocument.get("search_columnId"));
			search.setTypeId((String) solrDocument.get("search_typeId"));
			search.setColumParentId((String) solrDocument.get("search_columParentId"));
			search.setTime((String)solrDocument.get("search_time"));
			searchList.add(search);
		}
		//添加到返回结果
		result.setDataList(searchList);
		
		return result;
	}

	@Override
	public void deleteAll(){
		String baseURL= SystemConfig.getPropertiesString("SOLR.SERVER.URL");
		logger.info("baseUrl是："+baseURL);
		System.out.println("baseUrl是："+baseURL);
		HttpSolrServer httpSolrServer = new HttpSolrServer(baseURL);
		logger.info("开始执行delete方法==================================");
		System.out.println("开始执行delete方法==================================");
		try {
			httpSolrServer.deleteByQuery("*:*");
			httpSolrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//验证是否是数字
	public Boolean checkNum(String str){
		Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*"); 
		Matcher isNum = pattern.matcher(str); 
		if (!isNum.matches()){ 
			return false; 
		}
		return true;
	}

	@Override
	public SearchResult findSolrList(String queryString,String columnId, String[] ids, Page page) throws Exception {
				SolrQuery solrQuery = new SolrQuery();
			//判断查询条件是否为空
				/*if (null == queryString || "".equals(queryString)) {
					return null;
				}*/
				
				if(null != queryString && !"".equals(queryString)){
					
					if(null == columnId || "".equals(columnId)){
						solrQuery.setQuery(queryString);
					}
					if(null != columnId && null != ids && ids.length > 0){
						StringBuilder params = new StringBuilder("search_columnId:"+ columnId +" AND search_keywords:"+queryString);
						params.append(" AND (");
						
						for (int i = 0; i < ids.length; i++) {
							String typeId = ids[i];
							
							if(i == 0 && ids.length == 1){
								params.append("search_typeId:"+typeId);
							}
							if(i < (ids.length-1)){
								params.append("search_typeId:"+ typeId +" OR ");
							}
							if( ids.length > 1 && i == (ids.length - 1)){
								params.append("search_typeId:"+typeId);
							}
							
						}
						
						params.append(")");
						solrQuery.setQuery(params.toString());
					}
					
					
				}else{
					
					if(null != columnId && null != ids && ids.length > 0){
						StringBuilder params = new StringBuilder("search_columnId:"+ columnId );
						params.append(" AND (");
						
						for (int i = 0; i < ids.length; i++) {
							String typeId = ids[i];
							
							if(i == 0 && ids.length == 1){
								params.append("search_typeId:"+typeId);
							}
							if(i < (ids.length-1)){
								params.append("search_typeId:"+ typeId +" OR ");
							}
							if(ids.length > 1 && i == (ids.length - 1)){
								params.append("search_typeId:"+typeId);
							}
							
						}
						
						params.append(")");
						solrQuery.setQuery(params.toString());
					}
					
					
				}
				//创建一个SolrQuery对象
				//设置查询条件
				
				int currentPage = page.getCurrentPageOrgi();
				if (currentPage == 0) {
					currentPage = 1;
				}
				//设置分页条件
				solrQuery.setStart((currentPage - 1) * (page.getShowCount()));
				solrQuery.setRows(page.getShowCount());
				//默认搜索域
				solrQuery.set("df", "search_keywords");
				//高亮显示
				solrQuery.setHighlight(true);
		        solrQuery.addHighlightField("search_name");
		        solrQuery.addHighlightField("search_des");
		        solrQuery.setHighlightSimplePre("<span style='color:red'>");
		        solrQuery.setHighlightSimplePost("</span>");
				SearchResult searchResult = this.search(solrQuery);
				//计算总条数
				int recordCount = searchResult.getRecordCount();
				page.setTotalResult(recordCount);
				//
				int totalPage=recordCount/page.getShowCount();
				if(recordCount%page.getShowCount()!=0){
					totalPage++;
				}
				page.setTotalPage(totalPage);
				int pageCount = (int) (recordCount/page.getShowCount());
				if (recordCount % page.getShowCount() > 0) {
					pageCount++;
				}
				page.setTotalPage(pageCount);
				page.setTotalResult(recordCount);
				searchResult.setPage(page);
				return searchResult;
	}
	
}
