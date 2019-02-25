package cn.cebest.controller.system.infineon;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.cebest.controller.base.BaseController;
import cn.cebest.service.system.infineon.CommitDemandService;
import cn.cebest.service.system.infineon.CommitPlanService;
import cn.cebest.service.system.infineon.ContactPlanService;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.UuidUtil;

/**
 *
 * @author wangweijie
 * @Date 2018年11月5日
 * @company 中企高呈
 */
@Controller
@RequestMapping("ipcExcel")
public class IpcExcelController extends BaseController{
	@Autowired
	private ContactPlanService contactPlanService;
	@Autowired
	private CommitPlanService commitPlanService;
	@Autowired
	private CommitDemandService commitDemandService;
	
	
	/**
	 * 模板下载
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="key",required=true)String key) {
		ModeUtil modeUtil = new ModeUtil();
		String mode =modeUtil.getMode(key);
		try {
			String filePath = null;
			if(StringUtils.isNotEmpty(mode)){
				filePath = request.getSession().getServletContext().getRealPath("") + mode;
			}
			File file = new File(filePath);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentDispositionFormData("attachment", new String(modeUtil.getName(key).getBytes("utf-8"), "ISO8859-1"));
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
			return entity;
		} catch (Exception e) {
			logger.error("save the ContactUs occured error!", e);
			return null;
		}
	}
	
	@RequestMapping(value = "import", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult readExcel(@RequestParam(name="file",required=true) MultipartFile file,
								@RequestParam(name="key",required=true)String key) {
		// 获取数据
		try {
			if (file != null && file.getSize() != 0) {
				ModeUtil modeUtil = new ModeUtil();
				List<String> attrs = modeUtil.getAttr(key);
				InputStream inputStream = file.getInputStream();
				 //构建Workbook对象
				Workbook workbook = WorkbookFactory.create(inputStream);
				//Sheet的下标是从0开始
		        //获取第一张Sheet表
		        Sheet sheet = workbook.getSheetAt(0);
		        //Sheet的行是从0开始
		        //获取需要读取内容的开始行标
		        int dataRow = 0;
		        //获取需要读取内容的结束行标
		        int endRow = sheet.getLastRowNum() +1;
		        if(endRow >= 1){
		           	List<Map<String,String>> pList = new ArrayList<>();
		           	for (int i = dataRow; i < endRow; i++) {
		           		  //获取需要读取内容的行
		                   Row row = sheet.getRow(i);
		    	        	//Sheet的列是从0开始
		    	            //遍历并获取需要读取的单元格
		                   Map<String, String> map = new HashMap<>();
		                   for(int j=0; j<row.getLastCellNum()-1;j++){
		                	   Cell cell = row.getCell(j);
		                	   map.put(attrs.get(j), cell.getStringCellValue());
		                   }
		                   map.put("id", UuidUtil.get32UUID()); 
		                   pList.add(map);
		                   if(pList.size() == 20){
		                	   inserts(pList,key);
		                	   pList.clear();
		                   }
		           	}
		           	if(pList.size() > 0){
		           		inserts(pList,key);
		           	}
		        }
		        inputStream.close();
			}
		} catch (Exception e) {
			logger.error("save the ContactUs occured error!", e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 导出数据
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="key",required=true)String key) {
		ModeUtil modeUtil = new ModeUtil();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles =modeUtil.getTable(key);
		List<String> attrs =modeUtil.getAttr(key);
		List<Map<String, String>> excelList = null;
		
		try {
			dataMap.put("titles", titles);
			if("1".equals(key)){
				excelList = this.contactPlanService.findAll();
			}else if("2".equals(key)){
				excelList = this.commitPlanService.findAll();
			}else{
				excelList = this.commitDemandService.findAll();
			}
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
			
			this.setResponseHeader(response, modeUtil.getExportName(key));
			OutputStream os = response.getOutputStream();
			workbook.write(os);
	        os.flush();
	        os.close();
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
    
    //导入
    
    public void inserts(List<Map<String,String>> pList,String key) throws Exception{
    	if(StringUtils.isNotEmpty(key)){
    		if("1".equals(key)){
    			this.contactPlanService.inserts(pList);
    		}else if("2".equals(key)){
    			
    		}else{
    			
    		}
    	}
    }
	
}
