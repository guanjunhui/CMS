package cn.cebest.controller.system.recommend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.cebest.entity.system.Recommend;
import cn.cebest.service.system.recommend.RecommendService;

/**	
 * 推荐设置
 * @author qichangxin
 * 修改日期：2015.11.6
 */
@RestController
@RequestMapping("/recommendService")
public class RecommendRestController {
	
	@Autowired
	private RecommendService recommendService;
	
	/**
	 * 批量保存推荐的映射关系
	 * @param String contentId
	 */
	@RequestMapping(value = "/saveBatch",method = RequestMethod.POST)
	public void saveBatch(@RequestBody List<Recommend> list)throws Exception{
		recommendService.saveBatch(list);
	}
	
	/**
	 * 根据内容ID删除推荐的映射关系
	 * @param String contentId
	 */
	@RequestMapping(value = "/deleteByContentId",method = RequestMethod.GET)
	public void deleteByContentId(@RequestParam("contentId")String contentId)throws Exception{
		recommendService.deleteByContentId(contentId);
	}
	/**
	 * 根据内容ID推荐的映射关系
	 * @param String contentId
	 */
	@RequestMapping(value = "/selectByContentId",method = RequestMethod.GET)
	public List<Recommend> selectByContentId(@RequestParam("contentId")String contentId)throws Exception{
		return recommendService.selectByContentId(contentId);
	}
}
