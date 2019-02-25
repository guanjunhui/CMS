package cn.cebest.controller.system.pvstatistics;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.vo.PvInfo;
import cn.cebest.entity.vo.PvStatisVo;
import cn.cebest.service.task.PvService;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;

/** 
 * 说明：浏览量统计
 * 创建人：qichangxin
 * 创建时间：2017-12-07
 */
@Controller
@RequestMapping(value="/pvstatistics")
public class PVStatisticsController extends BaseController {
	
	@Autowired
	private PvService pvService;
	
	
	 /**获取浏览量统计数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getpvdata")
	@ResponseBody
	public JsonResult getpvdata(){
		PvStatisVo pvStatisVo=null;
		try {
			pvStatisVo = pvService.pvStatistics();
		} catch (Exception e) {
			logger.error("get the pv statistics data occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK",pvStatisVo);
	}

	 /**获取月浏览量统计数据
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/getpvmonthdata")
	@ResponseBody
	public JsonResult getpvMonthData(){
		List<PvInfo> pvStatisVoList=null;
		try {
			pvStatisVoList = pvService.pvMonthStatistics();
			Collections.sort(pvStatisVoList, new Comparator<PvInfo>(){
				@Override
				public int compare(PvInfo o1, PvInfo o2) {
					String A=o1.getDate();
					String B=o2.getDate();
					return A.compareTo(B);
				}
			});
		} catch (Exception e) {
			logger.error("get the pv statistics data occured error!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK",pvStatisVoList);
	}

}
