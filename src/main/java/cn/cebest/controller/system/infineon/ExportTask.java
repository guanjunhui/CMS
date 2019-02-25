package cn.cebest.controller.system.infineon;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.cebest.util.HttpClientUtil;
import cn.cebest.util.Logger;
import cn.cebest.util.SystemConfig;

/**
 *
 * @author wangweijie
 * @Date 2018年11月29日
 * @company 中企高呈
 */
@Component
public class ExportTask {
	protected Logger logger = Logger.getLogger(this.getClass());
	/**
     * 用户信息
     */
	@Scheduled(cron = "0 0 1 * * ?") //每天凌晨1点执行一次
    public void scanTopic() {
    	logger.info("===========day job statistics start=========");
    	ModeUtil modeUtil = new ModeUtil();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<Map<String, String>> excelList = null;
		
		//httpclient获取用户信息
		String url = SystemConfig.getPropertiesString("web.center_url") + "api/getAllUserInfo";
		Map<String, String> pMap = new HashMap<>();
		pMap.put("queryKey","");
		List<String> titles =modeUtil.getTable("4");
		List<String> attrs =modeUtil.getAttr("4");
		String str = HttpClientUtil.doPost(url,pMap);
		//String str = HttpClientUtil.doPostJson(url,queryKey);
		if(StringUtils.isNotEmpty(str)){
			JSONObject jsonObject = JSON.parseObject(str);
			
			logger.error("jsonObject" + str );
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
			
			String filePath = SystemConfig.getPropertiesString("web.export_user_url") + modeUtil.getMode("4");
			OutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			//workbook.write(os);
	        //os.flush();
	        //os.close();
		} catch (Exception e) {
			logger.error("day job statistics occured error!",e);
		}
    	
		logger.info("===========day job statistics end===========");
    }
}
