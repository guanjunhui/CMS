package cn.cebest.service.task.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import cn.cebest.entity.vo.PvInfo;
import cn.cebest.entity.vo.PvStatisVo;
import cn.cebest.service.task.PvService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.redis.RedisUtil;

@Service
public class PvServiceImpl implements PvService{
    private static final Long LONG_0=0L;

	/**
	 *浏览量统计 
	 */
	@Override
	public PvStatisVo pvStatistics() throws Exception {
		PvStatisVo pvStatisVo=new PvStatisVo();
		PvInfo todayPv=new PvInfo();
		PvInfo yesterdayPv=new PvInfo();
		PvInfo monthPv=new PvInfo();
		pvStatisVo.setToday(todayPv);
		pvStatisVo.setYesterday(yesterdayPv);
		pvStatisVo.setMonth(monthPv);
		
		String today=DateUtil.fomatDate();
		String yestoday=DateUtil.getAfterDayDate(Const.INT_UNSIGINED_1, DateUtil.DYAS);
		/**浏览量 start*/
		//今日
		long PVTodayCount=RedisUtil.getLong(today+Const.STATIS_PM);
		/*long pmToday_content=RedisUtil.getLong(today+Const.STATIS_PM+Const.COLUM_TYPE_1);
		long pmToday_new=RedisUtil.getLong(today+Const.STATIS_PM+Const.COLUM_TYPE_2);
		long pmToday_product=RedisUtil.getLong(today+Const.STATIS_PM+Const.COLUM_TYPE_3);
		long pmToday_employ=RedisUtil.getLong(today+Const.STATIS_PM+Const.COLUM_TYPE_4);
		long pmToday_download=RedisUtil.getLong(today+Const.STATIS_PM+Const.COLUM_TYPE_5);*/
		
		//昨日
		long PVYestodayCount=RedisUtil.getLong(yestoday+Const.STATIS_PM);
		/*long yestoday_content=RedisUtil.getLong(yestoday+Const.STATIS_PM+Const.COLUM_TYPE_1);
		long yestoday_new=RedisUtil.getLong(yestoday+Const.STATIS_PM+Const.COLUM_TYPE_2);
		long yestoday_product=RedisUtil.getLong(yestoday+Const.STATIS_PM+Const.COLUM_TYPE_3);
		long yestoday_employ=RedisUtil.getLong(yestoday+Const.STATIS_PM+Const.COLUM_TYPE_4);
		long yestoday_download=RedisUtil.getLong(yestoday+Const.STATIS_PM+Const.COLUM_TYPE_5);*/
		
		//前30天
		long month_content=LONG_0;
		/*long month_new=LONG_0;
		long month_product=LONG_0;
		long month_employ=LONG_0;
		long month_download=LONG_0;*/
		
		/**访客数 start*/
		long uvToday=RedisUtil.getLong(today+Const.STATIS_UV);
		long uvYestoday=RedisUtil.getLong(yestoday+Const.STATIS_UV);
		long uvMonth=LONG_0;
		
		/*今日 Pm(访问量)---Uv(今日访客量)*/
		todayPv.setPm(PVTodayCount+uvToday);
		todayPv.setUv(uvToday);
		
		/*昨日 Pm(访问量)---Uv(今日访客量)*/
		yesterdayPv.setPm(PVYestodayCount+uvYestoday);
		yesterdayPv.setUv(uvYestoday);
		/**访客数 end*/

		/**独立IP start*/
		long ipToday=RedisUtil.getSetLength(today+Const.STATIS_IP);
		if(ipToday==LONG_0) ipToday=RedisUtil.getLong(today+Const.STATIS_IP_COUNT);
		long ipYestoday=RedisUtil.getLong(yestoday+Const.STATIS_IP_COUNT);
		if(ipYestoday==LONG_0) ipYestoday=RedisUtil.getSetLength(yestoday+Const.STATIS_IP);
		long ipMonth=ipToday;
		todayPv.setIp(ipToday);
		yesterdayPv.setIp(ipYestoday);
		/**独立IP end*/
		for(int i=0;i<30;i++){
			String curentDay=DateUtil.getBeforeDayDate(i, DateUtil.DYAS);
			/*long curentDay_content=RedisUtil.getLong(curentDay+Const.STATIS_PM+Const.COLUM_TYPE_1);
			long curentDay_new=RedisUtil.getLong(curentDay+Const.STATIS_PM+Const.COLUM_TYPE_2);
			long curentDay_product=RedisUtil.getLong(curentDay+Const.STATIS_PM+Const.COLUM_TYPE_3);
			long curentDay_employ=RedisUtil.getLong(curentDay+Const.STATIS_PM+Const.COLUM_TYPE_4);
			long curentDay_download=RedisUtil.getLong(curentDay+Const.STATIS_PM+Const.COLUM_TYPE_5);
			month_content+=curentDay_content;
			month_new+=curentDay_new;
			month_product+=curentDay_product;
			month_employ+=curentDay_employ;
			month_download+=curentDay_download;*/
			long curentDay_content=RedisUtil.getLong(curentDay+Const.STATIS_PM);
			month_content+=curentDay_content;
			
			uvMonth+=RedisUtil.getLong(curentDay+Const.STATIS_UV);
			if(i>Const.INT_0){
				ipMonth+=RedisUtil.getLong(curentDay+Const.STATIS_IP_COUNT);
			}
		}
		monthPv.setPm(month_content+uvMonth);
		monthPv.setUv(uvMonth);
		monthPv.setIp(ipMonth);
		/**浏览量 end*/
		return pvStatisVo;
	}

	@Override
	public void dayStatistics() throws Exception {
		String before_30_today=DateUtil.getBeforeDayDate(Const.INT_30, DateUtil.DYAS);
		String yestoday=DateUtil.getBeforeDayDate(Const.INT_1, DateUtil.DYAS);

		//浏览量 start
		RedisUtil.delString(before_30_today+Const.STATIS_PM);
		/*RedisUtil.delString(before_30_today+Const.STATIS_PM+Const.COLUM_TYPE_1);
		RedisUtil.delString(before_30_today+Const.STATIS_PM+Const.COLUM_TYPE_2);
		RedisUtil.delString(before_30_today+Const.STATIS_PM+Const.COLUM_TYPE_3);
		RedisUtil.delString(before_30_today+Const.STATIS_PM+Const.COLUM_TYPE_4);
		RedisUtil.delString(before_30_today+Const.STATIS_PM+Const.COLUM_TYPE_5);*/
		
		//访客数
		RedisUtil.delString(before_30_today+Const.STATIS_UV);
		
		//独立IP 
		long ipYestoday=RedisUtil.getSetLength(yestoday+Const.STATIS_IP);
		
		/*RedisUtil.setString(yestoday+Const.STATIS_IP_COUNT, String.valueOf(ipYestoday));
		RedisUtil.delString(yestoday+Const.STATIS_IP);*/
		RedisUtil.staticsYestody(yestoday, ipYestoday);
		RedisUtil.delString(before_30_today+Const.STATIS_IP_COUNT);
	}

	@Override
	public List<PvInfo> pvMonthStatistics() throws Exception {
		List<PvInfo> pvInfoList=new ArrayList<PvInfo>();
		for(int i=0;i<30;i++){
			PvInfo pvInfo=new PvInfo();
			String curentDay=DateUtil.getBeforeDayDate(i, DateUtil.DYAS);
			/*long curentDay_content=RedisUtil.getLong(curentDay+Const.STATIS_PM+Const.COLUM_TYPE_1);
			long curentDay_new=RedisUtil.getLong(curentDay+Const.STATIS_PM+Const.COLUM_TYPE_2);
			long curentDay_product=RedisUtil.getLong(curentDay+Const.STATIS_PM+Const.COLUM_TYPE_3);
			long curentDay_employ=RedisUtil.getLong(curentDay+Const.STATIS_PM+Const.COLUM_TYPE_4);
			long curentDay_download=RedisUtil.getLong(curentDay+Const.STATIS_PM+Const.COLUM_TYPE_5);*/
			long curentDay_content=RedisUtil.getLong(curentDay+Const.STATIS_PM);
			pvInfo.setDate(curentDay);
			pvInfo.setPm(curentDay_content);
			long uv=RedisUtil.getLong(curentDay+Const.STATIS_UV);
			pvInfo.setUv(uv);
			if(i==Const.INT_0){
				long ipday=RedisUtil.getSetLength(curentDay+Const.STATIS_IP);
				if(ipday==LONG_0) ipday=RedisUtil.getLong(curentDay+Const.STATIS_IP_COUNT);
				pvInfo.setIp(ipday);
			}else{
				long ipday=RedisUtil.getLong(curentDay+Const.STATIS_IP_COUNT);
				pvInfo.setIp(ipday);
			}
			pvInfoList.add(pvInfo);
		}
		return pvInfoList;
	}

}
