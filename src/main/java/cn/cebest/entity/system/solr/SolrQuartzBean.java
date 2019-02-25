package cn.cebest.entity.system.solr;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import cn.cebest.service.system.solr.SolrService;

public class SolrQuartzBean extends QuartzJobBean{
	
	private SolrService solrService;
	
	public void query(){
		//清空solr索引库
		solrService.deleteAll();
		//添加数据到索引库
		solrService.importAll();
	}
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		solrService = (SolrService) context.getJobDetail().getJobDataMap().get("solrService");
		this.query();
	} 
}
