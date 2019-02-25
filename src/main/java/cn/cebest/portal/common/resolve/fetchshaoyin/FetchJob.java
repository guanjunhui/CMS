package cn.cebest.portal.common.resolve.fetchshaoyin;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import cn.cebest.util.Logger;

public class FetchJob extends QuartzJobBean{
	protected Logger logger = Logger.getLogger(this.getClass());
	
	public void fetchUpdate() {
		logger.info("===========day job fetchUpdate start=========");
		try {
			PaseTool.startThreadFetch(true, null);
		} catch (Exception e) {
			logger.error("day job fetchUpdate occured error!",e);
		}
		logger.info("===========day job fetchUpdate end===========");
    }

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		fetchUpdate();
	}

}
