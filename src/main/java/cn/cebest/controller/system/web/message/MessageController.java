package cn.cebest.controller.system.web.message;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.service.system.fhlog.FHlogManager;
import cn.cebest.service.web.message.MessageService;
import cn.cebest.util.AppUtil;
import cn.cebest.util.DateUtil;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;

/**
 * 资讯管理
 * @author liuzhule
 *
 */
@Controller
@RequestMapping(value = "/Message")
public class MessageController extends BaseController {
	
	String menuUrl = "Message/MessagelistPage.do"; //菜单地址(权限用)
	@Resource(name = "messageService")
	private MessageService messageService;
	@Resource(name="fhlogService")
	private FHlogManager FHLOG;

	
	/**
	 * 后台接口,资讯列表
	 * @return
	 */
	@RequestMapping(value="/MessagelistPage")
	public ModelAndView MessagelistPage(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");		//资讯名称检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		try {
			List<PageData> MessageList = messageService.messageListPage(page);	//列出资讯列表
			mv.setViewName("system/Message/Message_list");
			mv.addObject("MessageList", MessageList);
			mv.addObject("pd", pd);
			mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**去新增资讯
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goAddMessage")
	public ModelAndView goAddMessage() {
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/Message/Message_edit");
		mv.addObject("msg", "saveMessage");
		mv.addObject("pd", pd);
		return mv;
	}
	/**保存资讯
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveMessage")
	public ModelAndView saveMessage(@RequestParam(required=false) MultipartFile file){
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		logBefore(logger, Jurisdiction.getUsername()+"新增资讯");
		String fileId="";
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("MessageId", this.get32UUID());//主键ID
		pd.put("MessageLogo", fileId);
		try {
			pd.put("createdTime", DateUtil.getTime());
			messageService.saveMessage(pd); 					//执行保存,默认添加的都是子站,Message_TYPE为1
			FHLOG.save(Jurisdiction.getUsername(), "新增内容类型："+pd.getString("USERNAME"));
			mv.addObject("msg","success");
		} catch (Exception e) {
			mv.addObject("msg","failed");
			e.printStackTrace();
		}
		mv.setViewName("save_result");
		return mv;
	}
	/**
	 * 删除资讯
	 * @return
	 */
	@RequestMapping("/delMessage")
	public void delMessage(PrintWriter out){
		logBefore(logger, Jurisdiction.getUsername()+"删除资讯");
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			messageService.delMessage(pd);
			FHLOG.save(Jurisdiction.getUsername(), "删除资讯："+pd);
			out.write("success");
		} catch (Exception e) {
			out.write("failed");
			e.printStackTrace();
		}
		out.close();
	}
	
	/**
	 * 批量删除资讯
	 * 
	 */
	@RequestMapping(value="/delAllMessage")
	@ResponseBody
	public Object delAllMessage() {
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Message");
		PageData pd = new PageData();
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		try {
			FHLOG.save(Jurisdiction.getUsername(), "批量删除Message");
			List<PageData> pdList = new ArrayList<PageData>();
			String Message_IDS = pd.getString("Message_IDS");
			if(null != Message_IDS && !"".equals(Message_IDS)){
				String arrayMessage_IDS[] = Message_IDS.split(",");
				messageService.delAllMessage(arrayMessage_IDS);
				pd.put("msg", "ok");
			}else{
				pd.put("msg", "no");
			}
			pdList.add(pd);
			map.put("list", pdList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return AppUtil.returnObject(pd, map);
	}
	
	/**去修改资讯
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goEditMessage")
	public ModelAndView goEditMessage() {
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd = messageService.findMessageById(pd); //根据ID读取
		} catch (Exception e) {
			e.printStackTrace();
		}	
		//语言版本map
		mv.setViewName("system/Message/Message_edit");
		mv.addObject("msg", "editMessage");
		mv.addObject("pd", pd);
		return mv;
	}
	/**
	 * 修改资讯
	 */
	@RequestMapping(value="/editMessage")
	public ModelAndView editMessage() {
		logBefore(logger, Jurisdiction.getUsername()+"修改Message");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			messageService.editMessage(pd); //执行修改
			FHLOG.save(Jurisdiction.getUsername(), "修改资讯");
			mv.addObject("msg","success");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		mv.setViewName("save_result");
		return mv;
	}
	/**
	 * 修改状态
	 * @return
	 */
	@RequestMapping("/auditMessage")
	@ResponseBody
	public Object auditMessage(
			@RequestParam("MessageId")String MessageId,
			@RequestParam("status")String status){
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			if(StringUtils.isNotBlank(status) && "0".equals(status)){
	    		status="1";
	    	}else if(StringUtils.isNotBlank(status) && "1".equals(status)){
	    		status="0";
	    	}
			pd.put("status", status);
			pd.put("msg", "ok");
			messageService.auditMessage(pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return AppUtil.returnObject(pd, map);
	}
	
}