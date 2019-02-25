package cn.cebest.controller.system.columconfig;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.columconfig.ColumGroupService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.JsonResult;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
import cn.cebest.util.RequestUtils;

/**
 * 栏目组管理
 * @author qichangxin
 *
 */
@Controller
@RequestMapping(value = "/columgroup")
public class ColumGroupController extends BaseController {
	
	String menuUrl = "columgroup/list.do"; //菜单地址(权限用)
	
	@Autowired
	private ColumGroupService columGroupService;
	
	/**
	 * 后台接口,栏目组列表
	 * @return
	 */
	@RequiresPermissions("columgroup:list")
	@RequestMapping(value="/list")
	public ModelAndView columconfiglistPage(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");		//栏目检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		try {
			pd.put("SITE_ID", RequestUtils.getSiteId(this.getRequest()));//站点id
			List<PageData> list = columGroupService.gropulistPage(page);
			mv.addObject("list", list);
			mv.setViewName("system/columgroup/columgroup_list");
		} catch (Exception e) {
			logger.error("find the columGroup list failed!",e);
		}
		return mv;
	}
	
	/**
	 * 删除栏目
	 * @return
	 */
	@RequiresPermissions("columgroup:delete")
	@RequestMapping("/del")
	public void delColumconfig(PrintWriter out){
		logBefore(logger, Jurisdiction.getUsername()+"删除栏目组");
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			columGroupService.delete(pd);
			out.write("success");
		} catch (Exception e) {
			out.write("failed");
			e.printStackTrace();
		}
		out.close();
	}
	
	
	/**保存栏目
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("columgroup:save")
	@RequestMapping(value="/save")
	@ResponseBody
	public JsonResult saveColumconfig(){
		logBefore(logger, Jurisdiction.getUsername()+"新增栏目组");
		PageData pd = new PageData();
		pd = this.getPageData();
		String id=pd.getString("ID");
		String groupName = pd.getString("COLUM_GROUPNAME");
		if(StringUtils.isEmpty(id)){
			pd.put("ID", this.get32UUID());//主键ID
			pd.put("SITE_ID", RequestUtils.getSiteId(this.getRequest()));//站点id
			pd.put("CREATETIME", DateUtil.DatePattern(new Date()));
		}
		try {
			if(StringUtils.isEmpty(id)){
				String group_name = columGroupService.findByName(pd);
				if(!groupName.equals(group_name)){
					columGroupService.save(pd);//执行保存
				}else{
					return new JsonResult(0, "名字已存在");
				}
			}else{
				columGroupService.update(pd);
			}
		} catch (Exception e) {
			logger.error("save or update the columGroup failed!",e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**去修改页面
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("columgroup:goEdit")
	@RequestMapping(value="/goEdit")
	public ModelAndView goEditColumconfig() {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd = columGroupService.findById(pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("system/columgroup/columgroup_edit");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value="/edit")
	public ModelAndView editColumconfig() {
		logBefore(logger, Jurisdiction.getUsername()+"修改栏目组");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			columGroupService.update(pd);
			mv.addObject("msg","success");
		} catch (Exception e) {
			mv.addObject("msg","failed");
			e.printStackTrace();
		}	
		mv.setViewName("save_result");
		return mv;
	}

}
