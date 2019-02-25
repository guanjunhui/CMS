package cn.cebest.service.task;

import java.util.List;

import cn.cebest.entity.vo.PvInfo;
import cn.cebest.entity.vo.PvStatisVo;

public interface PvService {

	/**
	 *浏览量统计 
	 */
	public PvStatisVo pvStatistics() throws Exception;
	
	/**
	 *每天定时执行
	 */
	public void dayStatistics() throws Exception;
	
	/**
	 *月浏览量统计 
	 */
	public List<PvInfo> pvMonthStatistics() throws Exception;

	
}
