package cn.cebest.controller.system.customForm;


import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.bo.FormRecordBo;
import cn.cebest.entity.system.customForm.CustomForm;
import cn.cebest.entity.system.customForm.CustomFormAttribute;
import cn.cebest.entity.system.customForm.CustomformAttributeValue;
import cn.cebest.entity.vo.CustomFormVo;
import cn.cebest.service.system.customForm.CustomFormAttributeService;
import cn.cebest.service.system.customForm.CustomFormService;
import cn.cebest.service.system.customForm.CustomformAttributeValueService;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;
import cn.cebest.util.UuidUtil;
import cn.cebest.util.customform.XLSX2CSV;

/** 
 * 说明：自定义表单-表单项及数据展示
 * 创建人：qichangxin@300.cn
 * 创建时间：2018-01-12
 */
@Controller
@RequestMapping(value="/customformdata")
public class CustomformAttributeValueController extends BaseController {
	@Autowired
	private CustomFormService customFormService;
	@Autowired
	private CustomFormAttributeService customFormAttributeService;
	@Autowired
	private CustomformAttributeValueService customformAttributeValueService;
	
	
	/**
	 * 表单项及数据展示
	 * @param formId
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequiresPermissions("customformdata:list")
	@RequestMapping(value="/list")
	public ModelAndView golist(Page page) throws UnsupportedEncodingException{
		long startTime=System.currentTimeMillis();   //获取开始时间

		ModelAndView modelAndView = new ModelAndView();
		CustomFormVo customFormVo=null;
		PageData pd = new PageData();
		pd = this.getPageData();
		String customform_keyword = pd.getString("customform_keyword");//模板名称检索
		if(StringUtils.isNotEmpty(customform_keyword)){
			String decode= URLDecoder.decode(customform_keyword, "UTF-8");
			pd.put("customform_keyword",decode);
		}
		page.setPd(pd);
		try {
			customFormVo=customFormAttributeService.getAttributeAndValuePageByFormID(page);
		} catch (Exception e) {
			logger.error("get the customform attr and data list failed!",e);
		}
		modelAndView.addObject("info", customFormVo);
		modelAndView.addObject("formId", pd.getString("formId"));
		modelAndView.addObject("customform_keyword", pd.getString("customform_keyword"));
		modelAndView.addObject("lenth", customFormVo==null?Const.INT_0:
			(CollectionUtils.isEmpty(customFormVo.getRecordHead())?Const.INT_0:customFormVo.getRecordHead().size()));
		modelAndView.setViewName("system/customform/customformdata_list");
		long endTime=System.currentTimeMillis(); //获取结束时间
		
		logger.debug("customform list page run time： "+(endTime-startTime)+"ms");
		return modelAndView;
	}
	
	/**去修改页面
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("customformdata:goEdit")
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit(@RequestParam("formId") String formId,@RequestParam("creatTime") String creatTime) {
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd.put("formId", formId);
		pd.put("creatTime", creatTime);
		CustomFormVo customFormVo = null;
				try {
			customFormVo=customformAttributeValueService.selectformAttributeValueList(pd);
			pd.put("customFormVo", customFormVo);
		} catch (Exception e) {
			logger.error("get the customForm by primary key failed!",e);
		}
		mv.setViewName("system/customform/customformdata_edit");
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**修改
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("customformdata:Edit")
	@RequestMapping(value="/Edit")
	public String Edit(String formId,String[] formValueNameS,String[] formValueIdS) {
		PageData pd = new PageData();
		pd.put("formValueNameS", formValueNameS);
		pd.put("formValueIdS", formValueIdS);
		try {
			customformAttributeValueService.updateformAttributeValue(pd);
		} catch (Exception e) {
			logger.error("update the form data occured error!", e);
			return "redirect:/manage/customformdata/list.do?formId="+formId;
		}
		return "redirect:/manage/customformdata/list.do?formId="+formId;
		//return new JsonResult(Const.HTTP_OK,StringUtils.EMPTY);
	}
	
	/**
	 * 导出模板
	 */
	@SuppressWarnings("resource")
	@RequestMapping("exprotTemplate")
	@RequiresPermissions("customformdata:exprotTemplate")
	public void exprotTemplate(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("formId") String formId) {
		List<CustomFormAttribute> attrInfoList=null;
		//查询表单项
		try {
			attrInfoList=(List<CustomFormAttribute>) 
					customFormAttributeService.getAttributeListByFormID(formId);
		} catch (Exception e) {
			logger.error("get the form items occured error!", e);
		}
		XSSFWorkbook work_book = new XSSFWorkbook();
		XSSFSheet sheet = work_book.createSheet("表单导入模板");
		XSSFRow row = sheet.createRow(0);
		if(CollectionUtils.isNotEmpty(attrInfoList)){
			for(int i=0;i<attrInfoList.size();i++){
				CustomFormAttribute attrData=attrInfoList.get(i);
				XSSFCell cell = row.createCell(i);
				cell.setCellValue(attrData.getAttrName());
			}
		}
		// 把excel返回客户端
		try {
			OutputStream os;
			response.setContentType("application/octet-stream;charset=UTF-8");
			String fn = "表单导入模板";
			fn = new String(fn.getBytes("gbk"),"ISO8859-1");
			response.addHeader("Content-Disposition", "attachment;filename=" + fn + ".xls");
			os = response.getOutputStream();
			work_book.write(os);
			os.flush();
		} catch (UnsupportedEncodingException e) {
			logger.error("Encoding the file name in download the form template occured error!", e);
		} catch (IOException e) {
			logger.error("output stream in download the form template occured error!", e);
		}
	}

	/**
	 * 导出数据
	 */
	@SuppressWarnings("resource")
	@RequestMapping("exportData")
	@RequiresPermissions("customformdata:exportData")
	public void exportData(HttpServletRequest request, HttpServletResponse response) {
		CustomFormVo customFormVo=null;
		PageData pd=this.getPageData();
		//查询表单项
		try {
			customFormVo=(CustomFormVo) customFormAttributeService.getAttributeAndValueByFormID(pd);
		} catch (Exception e) {
			logger.error("get the form items occured error!", e);
		}
		CustomForm customForm = null;
		try {
			customForm = customFormService.selectByPrimaryKey(pd.getString("formId"));
		} catch (Exception e) {
			logger.error("get the form info occured error!", e);
		}
		XSSFWorkbook work_book = new XSSFWorkbook();
		XSSFSheet sheet = work_book.createSheet(customForm.getFormName());
		//数据填充
		List<FormRecordBo> formRecordBoList = customFormVo.getRecordHead();
		List<List<FormRecordBo>> dataTotalList= customFormVo.getRecordData();
		XSSFRow row = sheet.createRow(0);
		if(CollectionUtils.isNotEmpty(formRecordBoList)){
			for(int i=0;i<formRecordBoList.size();i++){
				FormRecordBo attrData=formRecordBoList.get(i);
				XSSFCell cell = row.createCell(i);
				cell.setCellValue(attrData.getName());
			}
		}
		if(CollectionUtils.isNotEmpty(dataTotalList)){
			for(int i=0;i<dataTotalList.size();i++){
				List<FormRecordBo> dataList=dataTotalList.get(i);
				if(CollectionUtils.isNotEmpty(dataList)){
					XSSFRow rowCurret = sheet.createRow(i+1);
					for(int m=0;m<dataList.size();m++){
						FormRecordBo cellData = dataList.get(m);
						XSSFCell cell = rowCurret.createCell(m);
						cell.setCellValue(cellData.getName());
					}
				}
			}
		}
		// 把excel返回客户端
		try {
			OutputStream os;
			response.setContentType("application/octet-stream;charset=UTF-8");
			String fn = customForm.getFormName();
			fn = new String(fn.getBytes("gbk"),"ISO8859-1");
			response.addHeader("Content-Disposition", "attachment;filename=" + fn + ".xls");
			os = response.getOutputStream();
			work_book.write(os);
			os.flush();
		} catch (UnsupportedEncodingException e) {
			logger.error("Encoding the file name in export the form datas occured error!", e);
		} catch (IOException e) {
			logger.error("output stream in export the form datas occured error!", e);
		}
	}

	/**
	 * 导入
	 */
	@RequestMapping("import")
	@RequiresPermissions("customformdata:import")
	@ResponseBody
	public JsonResult improt(MultipartFile file,@RequestParam("formId") String formId) {
		if (file.isEmpty()) {
			return new JsonResult(Const.HTTP_ERROR_400, StringUtils.EMPTY);
		} else {
			String name = file.getOriginalFilename();
			if (name.matches("^.+\\.(?i)(xlsx)$")) {//文件后缀xlsx
				return saveXlsx(file,formId);
			}
			;
			if (name.matches("^.+\\.(?i)(xls)$")) {//文件后缀xls
				return saveXls(file,formId);
			}
		}
		return new JsonResult(Const.HTTP_OK,StringUtils.EMPTY);
	}

	@RequestMapping("delete")
	@RequiresPermissions("customformdata:delete")
	@ResponseBody
	public JsonResult delete(@RequestParam("dates") String dates) {
		if(StringUtils.isNotEmpty(dates)){
			try {
				String[] dateArry=StringUtils.split(dates, Const.SPLIT_CHAR);
				this.customformAttributeValueService.deleteByTimes(dateArry);
			} catch (Exception e) {
				logger.error("delete the form data occured error!", e);
				return new JsonResult(Const.HTTP_ERROR,StringUtils.EMPTY);
			}
		}
		return new JsonResult(Const.HTTP_OK,StringUtils.EMPTY);
	}

	private JsonResult saveXlsx(MultipartFile file,String formId) {
		long startTime=System.currentTimeMillis();   //获取开始时间
		
        OPCPackage p=null;
		try {
			p = OPCPackage.open(file.getInputStream());
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        XLSX2CSV xlsx2csv = new XLSX2CSV(p);
		try {
			List<CustomFormAttribute> attrInfoList=customFormAttributeService.getAttributeListByFormID(formId);
			xlsx2csv.setAttrInfoList(attrInfoList);
		} catch (Exception e) {
			logger.error("get the form items occured error!", e);
//			return new JsonResult(Const.HTTP_ERROR,StringUtils.EMPTY);
		}

        try {
			xlsx2csv.process();
			ConcurrentHashMap<Integer, List<String>> rowMap=xlsx2csv.getRowMap();
        	if(rowMap!=null && !rowMap.isEmpty()){
        		final List<CustomformAttributeValue> dataList = 
        				new ArrayList<CustomformAttributeValue>();
        		for(Map.Entry<Integer,List<String>> row:rowMap.entrySet()){
        			List<String> rowData=row.getValue();
        			//如果此时间不能保证唯一性，则还原为上面方法
        			long createdTime=System.nanoTime();
        			int count=0;
        			for(String data:rowData){
        				CustomformAttributeValue customformAttributeValue = 
        						new CustomformAttributeValue();
        				customformAttributeValue.setCreatedTime(createdTime);
        				customformAttributeValue.setAttrId(xlsx2csv.getAttrInfoList().get(count++).getId());
        				customformAttributeValue.setId(UuidUtil.get32UUID());
        				customformAttributeValue.setAttrValue(data);
        				dataList.add(customformAttributeValue);
        			}
        			int size=xlsx2csv.getAttrInfoList().size();
        			if(count<size){
        				for(int i=count;i<size;i++){
            				CustomformAttributeValue customformAttributeValue = 
            						new CustomformAttributeValue();
            				customformAttributeValue.setCreatedTime(createdTime);
            				customformAttributeValue.setAttrId(xlsx2csv.getAttrInfoList().get(count++).getId());
            				customformAttributeValue.setId(UuidUtil.get32UUID());
            				customformAttributeValue.setAttrValue(StringUtils.EMPTY);
            				dataList.add(customformAttributeValue);
        				}
        			}
        		}
        		try {
					customformAttributeValueService.saveBatch(dataList);
				} catch (Exception e) {
					e.printStackTrace();
				}
        	}

		} catch (IOException | OpenXML4JException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
        try {
        	if(p!=null){
    			p.close();
        	}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        long endTime=System.currentTimeMillis(); //获取结束时间
        
        if(logger.getLog4jLogger().isDebugEnabled()){
    		logger.debug("customform import excel data run time： "+(endTime-startTime)+"ms");
        }
		
		return new JsonResult(Const.HTTP_OK,StringUtils.EMPTY);
	}


	@SuppressWarnings("resource")
	private JsonResult saveXls(MultipartFile file,String formId){
		XSSFWorkbook workbook;
		try {
			workbook = new XSSFWorkbook(file.getInputStream());
		} catch (IOException e) {
			logger.error("read excel file inputStream occured error!", e);
			return new JsonResult(Const.HTTP_ERROR,StringUtils.EMPTY);
		}
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow row = null;
		XSSFCell cell = null;
		List<CustomFormAttribute> attrInfoList=null;
		//查询表单项
		try {
			attrInfoList=(List<CustomFormAttribute>) 
					customFormAttributeService.getAttributeListByFormID(formId);
		} catch (Exception e) {
			logger.error("get the form items occured error!", e);
			return new JsonResult(Const.HTTP_ERROR,StringUtils.EMPTY);
		}
		if(CollectionUtils.isEmpty(attrInfoList)){//表单项为空
			return new JsonResult(Const.HTTP_ERROR_401,StringUtils.EMPTY);
		}
		List<CustomformAttributeValue> dataList = 
				new LinkedList<CustomformAttributeValue>();
		for (int i = 1; i <sheet.getLastRowNum()+1; i++) {// 行
			row = sheet.getRow(i);
			//同一行的创建时间必须相同
//			long createdTime=System.currentTimeMillis();
//			try {
//				TimeUnit.MICROSECONDS.sleep(1);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			//如果此时间不能保证唯一性，则还原为上面方法
			long createdTime=System.nanoTime();

			//列数据与表单项目对应不上,请修正sheeet页数据
			if(row==null || row.getLastCellNum() != attrInfoList.size()){
				return new JsonResult(Const.HTTP_ERROR_402,StringUtils.EMPTY,i);
			}
			for (int j = 0; j < row.getLastCellNum(); j++) {// 列
				cell = row.getCell(j);
				
				CustomformAttributeValue customformAttributeValue = 
						new CustomformAttributeValue();
				customformAttributeValue.setCreatedTime(createdTime);
				customformAttributeValue.setAttrId(attrInfoList.get(j).getId());
				customformAttributeValue.setId(this.get32UUID());
				if(cell==null){
					customformAttributeValue.setAttrValue(StringUtils.EMPTY);
				}else{
					CellType cellType=cell.getCellTypeEnum();
					switch(cellType){
						case _NONE:
							customformAttributeValue.setAttrValue(StringUtils.EMPTY);
							break;
						case NUMERIC:
							DecimalFormat df = new DecimalFormat("0");  

							customformAttributeValue.setAttrValue(df.format(cell.getNumericCellValue()));
							break;
						case STRING:
							customformAttributeValue.setAttrValue(cell.getStringCellValue());
							break;
						case FORMULA:
							return new JsonResult(Const.HTTP_ERROR_403,"第"+i+"行第"+(j+1)+"列未识别的类型(禁止出现表达式)");
						case BLANK:
							customformAttributeValue.setAttrValue(StringUtils.EMPTY);
							break;
						case BOOLEAN:
							boolean flag=cell.getBooleanCellValue();
							customformAttributeValue.setAttrValue(flag?Const.YES:Const.NO);
							break;
						case ERROR:
							return new JsonResult(Const.HTTP_ERROR_403,"第"+i+"行第"+(j+1)+"列未识别的类型");
						default:break;
					}
				}
				dataList.add(customformAttributeValue);
			}
		}
		try {
			this.customformAttributeValueService.saveBatch(dataList);
		} catch (Exception e) {
			logger.error("save the form data occured error!", e);
			return new JsonResult(Const.HTTP_ERROR,StringUtils.EMPTY);
		}
		return new JsonResult(Const.HTTP_OK,StringUtils.EMPTY);
	}
	/**
	 * 审核
	 * 
	 * @param ids
	 * @return
	 *//*
	@RequestMapping(value = "/updataAudit")
	@ResponseBody
	@RequiresPermissions("customformdata:updataAudit")
	public Map<String, Object> updataAudit(String ids,String audit) {
		Map<String, Object> map = new HashMap<>();
		logBefore(logger, Jurisdiction.getUsername() + "审核");
		PageData pd = new PageData();
		pd.put("id", ids);
		pd.put("audit", audit);
		try {
			this.customformAttributeValueService.updataAudit(pd);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			logBefore(logger, Jurisdiction.getUsername() + "审核出现异常");
			e.printStackTrace();
		}
		return map;
	}*/
}
