package cn.cebest.controller.system.shaoYin;

import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.ServiceNetwork.ServiceNetwork;
import cn.cebest.entity.web.ProductRegistration;
import cn.cebest.service.system.WarrantyClaim.ExcelService;
import cn.cebest.service.system.serviceNetwork.ServiceNetworkService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.JsonResult;

/**
 *
 * @author wangweijie
 * @Date 2018年7月13日
 * @company 中企高呈
 */
@Controller
@RequestMapping(value = "/excel")
public class ExcelController extends BaseController {
	String menuUrl = "excel/jump.do"; // 菜单地址(权限用)
	@Autowired
	private ExcelService excelService;
	@Resource(name = "serviceNetworkService")
	private ServiceNetworkService serviceNetworkService;

	/**
	 * 联系我们列表
	 * 
	 * @param map
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("jump")
	@RequiresPermissions("excel:jump")
	public ModelAndView index(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/shaoYin/importExcel");
		return mv;
	}

	// 服务网点的页面跳转
	@RequestMapping("jumpImportNetwork")
	public ModelAndView serviceNetwork(Page page) throws Exception {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("system/serviceNetwork/importExcel");
		return mv;
	}
	//临时使用 导入产品注册的信息
	@RequestMapping(value = "readreadNetworkExcel2", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult readNetworkExcel2(@RequestParam("file") MultipartFile file) {
		try {
		    Workbook workbook = WorkbookFactory.create(file.getInputStream());
		    Sheet sheet = workbook.getSheetAt(0);
			Row row = null;
			Cell cell = null;
			ProductRegistration p = null;
			//定义 转换类 将excel读取到的数字转换成 字符创保存
			DecimalFormat df=new DecimalFormat("0");
			List<ProductRegistration> productRegistration = new ArrayList<ProductRegistration>();
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {// 行
				row = sheet.getRow(i);
				p = new ProductRegistration();
				p.setId(this.get32UUID());
				String fullName = null;
				for (int j = 0; j < row.getLastCellNum(); j++) {// 列
					cell = row.getCell(j);
					if (j == 0) {// 第一列:产品注册码
						p.setRegistCode(df.format(cell.getNumericCellValue()));
					} else if (j == 1) {// 第一列:名
						fullName = cell.getStringCellValue() + " ";
					} else if (j == 2) {// 第一列:姓
						fullName += cell.getStringCellValue();
						p.setFullName(fullName);
					} else if (j == 3) {// 第一列:邮箱地址
						p.setEmail(cell.getStringCellValue());
					} else if (j == 4) {// 第一列:零售商
						p.setPlacePurchase(cell.getStringCellValue());
					} else if (j == 5) {// 第一列:购买日期
						p.setDatePurchase(cell.getStringCellValue());
					} else if (j == 6) {// 第一列:产品序列号
						p.setSerialNumber(df.format(cell.getNumericCellValue()));
					} else if (j == 7) {// 第一列:邮编
						p.setPostalCode(cell.getStringCellValue());
					} else if (j == 8) {// 产品名称
						p.setProduct(cell.getStringCellValue());
					} else if (j == 9) {// 街道地址
						p.setStreet(cell.getStringCellValue());
					} else if (j == 10) {// 城镇
						p.setCity(cell.getStringCellValue());
					} else if (j == 11) {// 国家
						p.setCountry(cell.getStringCellValue());
					}	
				}
					productRegistration.add(p);
				// 每三十条保存一次
				if (i % 20 == 0) {

					serviceNetworkService.saveProductRegistration(productRegistration);

					productRegistration.clear();
				}
			}
			// 最后保存一次
			if (productRegistration.size() > 0) {
				serviceNetworkService.saveProductRegistration(productRegistration);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	@RequestMapping(value = "readreadNetworkExcel", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult readNetworkExcel(@RequestParam("file") MultipartFile file) {
		try {
		    Workbook workbook = WorkbookFactory.create(file.getInputStream());
		    Sheet sheet = workbook.getSheetAt(0);
			Row row = null;
			Cell cell = null;
			ServiceNetwork p = null;
			//定义 转换类 将excel读取到的数字转换成 字符创保存
			DecimalFormat df=new DecimalFormat("0");
			List<ServiceNetwork> serviceNetwork = new ArrayList<ServiceNetwork>();
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {// 行
				row = sheet.getRow(i);
				p = new ServiceNetwork();
				/* p.setSiteId(siteId); */
				p.setId(this.get32UUID());
				String time = DateUtil.getTime();
				p.setUpdateTime(time);
				p.setCreatedTime(time);
				p.setReleaseTime(time);
				for (int j = 0; j < row.getLastCellNum(); j++) {// 列
					cell = row.getCell(j);
					if (j == 0) {// 第一列:国家名称
						p.setCountryEN(cell.getStringCellValue());
					} else if (j == 1) {// 经销商名称
						p.setName(cell.getStringCellValue());
					} else if (j == 2) {// 地址
						p.setAddress(cell.getStringCellValue());
					} else if (j == 3) {// 电话
						p.setPhone(df.format(cell.getNumericCellValue()));
					} /*
						 * else if (j == 4) {// 状态
						 * p.setProduct_Status("显示".equals(cell.
						 * getStringCellValue().trim()) ? "1" : "0"); } else if
						 * (j == 5) {// 跳转地址
						 * p.setProduct_WbUrl(cell.getStringCellValue()); }
						 */
				}
				serviceNetwork.add(p);
				// 每三十条保存一次
				if (i % 20 == 0) {

					serviceNetworkService.saveServiceNetwork(serviceNetwork);

					serviceNetwork.clear();
				}
			}
			// 最后保存一次
			if (serviceNetwork.size() > 0) {
				serviceNetworkService.saveServiceNetwork(serviceNetwork);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}

	@RequestMapping(value = "read", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult readExcel(@RequestParam("file") MultipartFile file) {
		// 获取数据
		try {
			if (file != null && file.getSize() != 0) {
				ImportExcelThread thread = new ImportExcelThread();
				InputStream inputStream = file.getInputStream();
				thread.setExcelServcie(excelService);
				thread.setInputStream(inputStream);
				thread.start();
			}
		} catch (Exception e) {
			logger.error("save the ContactUs occured error!", e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}

	/**
	 * 模板下载
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "download", method = RequestMethod.POST)
	public ResponseEntity<byte[]> download(HttpServletRequest request, HttpServletResponse response) {
		// 获取数据
		try {
			String filePath = request.getSession().getServletContext().getRealPath("") + "/WEB-INF/jsp/system/shaoYin/download/mode.xlsx";
			File file = new File(filePath);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentDispositionFormData("attachment", new String("数据导入模板.xlsx".getBytes("utf-8"), "ISO8859-1"));
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
			return entity;
		} catch (Exception e) {
			logger.error("save the ContactUs occured error!", e);
			return null;
		}
	}

	/**
	 * 获取上次导入数据的结果信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "getMsg", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getImportExcelMsg() {
		Object obj = null;
		JsonResult result = null;
		// 获取数据
		try {
			obj = this.excelService.getImportExcelMsg();
			result = new JsonResult(Const.HTTP_OK, "OK");
			result.setData(obj);
		} catch (Exception e) {
			logger.error("save the ContactUs occured error!", e);
			result = new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return result;
	}

}
