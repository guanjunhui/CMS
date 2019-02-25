package cn.cebest.task;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.cebest.service.system.comment.CommentService;
import cn.cebest.service.system.solr.SolrService;
import cn.cebest.service.task.PvService;
import cn.cebest.util.DateUtil;
import cn.cebest.util.Logger;

@Component
public class TaskJob {

	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private PvService pvService;
	
	@Autowired
	private SolrService solrService;
	
	@Scheduled(cron="0 0 6 * * ?")
	public void sendMail() {
		String today=DateUtil.getTime();
		logger.info(today+"===========day job sendEmail start=========");
		try {
			commentService.sendRelationMail();
		} catch (Exception e) {
			logger.error("day job statistics occured error!",e);
		}
		logger.info(today+"===========day job sendEmail end===========");
	}
	
    @Scheduled(cron = "0 0 3 * * ?") //每天凌晨3点执行一次 
    public void dayPvStatistics() {
    	String today=DateUtil.getTime();
		logger.info(today+"===========day job statistics start=========");
		try {
			pvService.dayStatistics();
		} catch (Exception e) {
			logger.error("day job statistics occured error!",e);
		}
		logger.info(today+"===========day job statistics end===========");
    }
    
    @Scheduled(cron = "0 0 0 * * ?") //每天0点执行一次 
    public void solrUpdate() {
		logger.info("===========day job statistics start=========");
		try {
			//清空solr索引库
			solrService.deleteAll();
			//添加数据到索引库
			solrService.importAll();
		} catch (Exception e) {
			logger.error("day job statistics occured error!",e);
		}
		logger.info("===========day job statistics end===========");
    }
}
