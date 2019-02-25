package cn.cebest.controller.system.infineon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.util.HttpClientUtil;
import cn.cebest.util.Jurisdiction;
import cn.cebest.util.PageData;
import cn.cebest.util.SystemConfig;

/**
 * 联系该方案接口
 * @author wangweijie
 * @Date 2018年9月12日
 * @company 中企高呈
 */
@Controller
@RequestMapping(value="userInfo")
public class UserInfoController extends BaseController {
	String menuUrl = "userInfo/list.do"; //菜单地址(权限用)
	/**
	 * 联系方案列表
	 * 
	 * @param map
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("list")
	@RequiresPermissions("userInfo:list")
	public ModelAndView index(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		String queryKey = pd.getString("text");
		page.setPd(pd);
		String url = SystemConfig.getPropertiesString("web.center_url") + "api/getUserPage";
        Map<String,String> pMap = new HashMap<>();
        if(pd.get("currentPage") == null){
        	page.setCurrentPage(1);
        	pMap.put("currentPage","1");
        }else{
        	pMap.put("currentPage",pd.get("currentPage").toString());
        }
        pMap.put("pageCount",String.valueOf(page.getShowCount()));
        pMap.put("queryKey", queryKey);
        List<PageData> list = null;
        try {
        	String str = HttpClientUtil.doPost(url,pMap);
			if(StringUtils.isNotEmpty(str)){
				JSONObject jsonObject = JSON.parseObject(str);
				String status = jsonObject.get("code").toString();
                if("200".equals(status)){
                	Map<String,Object> data = (Map<String,Object> )jsonObject.get("data");
                	Integer total = (Integer)data.get("total");
                	list = (List<PageData>)data.get("data");
                	
                	//设置分页参数
                	page.setTotalResult(total);
                	int pageCount = 0;
                	if(total%(page.getShowCount()) == 0){
                		pageCount = total/(page.getShowCount());
                	}else{
                		pageCount = total/(page.getShowCount()) + 1;
                	}
                	page.setTotalPage(pageCount);
                	page.setEntityOrField(true);
                }
            }
		} catch (Exception e) {
			logger.error("后台分页获取用户信息列表失败",e);
		}
		//List<PageData> list = userInfoService.ListPage(page);
		mv.setViewName("system/infineonipc/userInfoList");
		mv.addObject("list", list);
		return mv;
	}
	
	/**
	 * 联系方案详情列表（点赞收藏）
	 * 
	 * @param map
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("countList")
	@RequiresPermissions("userInfo:countList")
	public ModelAndView index1(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		PageData pd = this.getPageData();
		page.setPd(pd);
		//List<PageData> list = userInfoService.ListDetailPage(page);
		List<PageData> list = null;
		mv.setViewName("system/infineonipc/userInfoDetailList");
		mv.addObject("list", list);
		return mv;
	}
	
	/**
	 * 根据id查询详情
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("detailById")
	@RequiresPermissions("userInfo:detailById")
	public ModelAndView detailById(String id) {
		ModelAndView mv = this.getModelAndView();
		Object obj = null;
		try {
			if(id!=null && id!=""){
				//obj = userInfoService.detailById(id);
				//TODO
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "查询联系方案详情异常");
			e.printStackTrace();
		}
		mv.setViewName("system/infineonipc/userInfoDetail");
		mv.addObject("data", obj);
		return mv;
	}
	/**
	 * 根据id删除数据
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("deleteById")
	@RequiresPermissions("userInfo:deleteById")
	public String deleteById(@RequestParam(name="id",required=true)String id) {
		String redUrl = "";
		try {
			if(id!=null && id!=""){
				String url = SystemConfig.getPropertiesString("web.center_url") + "api/delUser";
				Map<String, String> pMap = new HashMap<>();
				pMap.put("id",id);
				String str = HttpClientUtil.doPost(url,pMap);
				if(StringUtils.isNotEmpty(str)){
					JSONObject jsonObject = JSON.parseObject(str);
					String status = jsonObject.get("code").toString();
	                if("200".equals(status)){
	                	redUrl = "redirect:list.do";
	                }
	            }
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "删除用户异常");
			e.printStackTrace();
		}
		
		return redUrl;
	}
	/**
	 * 根据id批量删除数据
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("deleteByIds")
	@RequiresPermissions("userInfo:deleteByIds")
	public String deleteByIds(@RequestParam(name="id",required=true)String id) {
		String redUrl = "";
		try {
			if(id!=null && id!=""){
				String url = SystemConfig.getPropertiesString("web.center_url") + "api/delUser";
				Map<String, String> pMap = new HashMap<>();
				pMap.put("id",id);
				String str = HttpClientUtil.doPost(url,pMap);
				if(StringUtils.isNotEmpty(str)){
					JSONObject jsonObject = JSON.parseObject(str);
					String status = jsonObject.get("code").toString();
	                if("200".equals(status)){
	                	redUrl = "redirect:list.do";
	                }
	            }
			}
		} catch (Exception e) {
			logBefore(logger, Jurisdiction.getUsername() + "批量删除联系方案异常");
			e.printStackTrace();
		}
		
		return redUrl;
	}
	
	/**
	 * 导出数据
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public void export(HttpServletRequest request, HttpServletResponse response,
			String queryKey) {
		
		ModeUtil modeUtil = new ModeUtil();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<Map<String, String>> excelList = null;
		
		//httpclient获取用户信息
		String url = SystemConfig.getPropertiesString("web.center_url") + "api/getAllUserInfo";
		Map<String, String> pMap = new HashMap<>();
		pMap.put("queryKey",queryKey);
		List<String> titles =modeUtil.getTable("4");
		List<String> attrs =modeUtil.getAttr("4");
		String str = HttpClientUtil.doPost(url,pMap);
		//String str = HttpClientUtil.doPostJson(url,queryKey);
		if(StringUtils.isNotEmpty(str)){
			JSONObject jsonObject = JSON.parseObject(str);
			String status = jsonObject.get("code").toString();
            if("200".equals(status)){
            	excelList = (List<Map<String, String>>) jsonObject.get("data");
            }
        }
		
		try {
			dataMap.put("titles", titles);
			//创建excel表格，并填写表头
			//创建一个excel工作簿 
			@SuppressWarnings("resource")
			HSSFWorkbook workbook = new HSSFWorkbook(); 
			//创建一个sheet工作表 
			HSSFSheet sheet = workbook.createSheet("sheet1"); 
			//创建第0行表头，再在这行里在创建单元格，并赋值 
			HSSFRow row = sheet.createRow(0); 
			HSSFCell cell = null;
			for (int i = 0; i < titles.size(); i++) {
	            cell = row.createCell(i);
	            cell.setCellValue(titles.get(i));//设置值
	        }
			//填充内容数据
			if(excelList != null && excelList.size()>0){
				for(int i=0; i<excelList.size(); i++){
					row = sheet.createRow(i + 1);
					Map<String, String> map = excelList.get(i);
					for(int j=0; j<map.size();j++){
						cell = row.createCell(j);
						cell.setCellValue(map.get(attrs.get(j)));//设置值
					}
				}
			}
			//调整列宽
			for (int i = 0; i < titles.size(); i++) {
	            sheet.autoSizeColumn(i); 
	        }
			
			//this.setResponseHeader(response, modeUtil.getExportName("4"));
			//OutputStream os = response.getOutputStream();
			
			String filePath = request.getSession().getServletContext().getRealPath("") + modeUtil.getMode("4");
			OutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			//workbook.write(os);
	        //os.flush();
	        //os.close();
		} catch (Exception e) {
			logger.error("export the excel error!", e);
		}
	}
	
	//发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
	 * 模板下载
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(HttpServletRequest request, HttpServletResponse response) {
		ModeUtil modeUtil = new ModeUtil();
		try {
			String filePath = SystemConfig.getPropertiesString("web.export_user_url") + modeUtil.getMode("4");
			File file = new File(filePath);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentDispositionFormData("attachment", new String(("userInfo-"+new Date().getTime()+".xls").getBytes("utf-8"), "ISO8859-1"));
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
			return entity;
		} catch (Exception e) {
			logger.error("save the ContactUs occured error!", e);
			return null;
		}
	}
}
