package cn.cebest.controller.system.solr;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.solr.SearchResult;
import cn.cebest.service.system.solr.SolrService;
import cn.cebest.util.CeResultUtil;
import cn.cebest.util.ExceptionUtil;
import cn.cebest.util.Logger;

@Controller
@RequestMapping("/search")
public class SolrController{
	protected Logger logger = Logger.getLogger(this.getClass());
	@Resource(name = "solrService")
	private SolrService solrService;

	@RequestMapping("/import")
	public CeResultUtil importAll(){
		CeResultUtil result = solrService.importAll();
		return CeResultUtil.ok(result);
	}
	
	@RequestMapping("/delete")
	public void deleteAll() throws SolrServerException, IOException{
		
		logger.error("solr controller方法执行：******************************************");
		System.out.println("solr controller方法执行：******************************************");
		solrService.deleteAll();
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public CeResultUtil search(String keyword, Page page) {
		try {
			String newKeyWord = new String(keyword.getBytes("iso8859-1"),"utf-8");
			SearchResult result = solrService.findSolrList(newKeyWord,page);
			return CeResultUtil.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
			return CeResultUtil.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
}
