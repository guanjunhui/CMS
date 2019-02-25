package cn.cebest.controller.system.roling.joinIn;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.addressquery.AddressQuery;
import cn.cebest.entity.system.product.Brand;
import cn.cebest.entity.system.product.Product;
import cn.cebest.service.system.addressquery.AddressQueryManager;
import cn.cebest.util.DateUtil;
import cn.cebest.util.PageData;
import cn.cebest.util.RequestUtils;
@RequestMapping("join")
@Controller
public class JoinInController extends BaseController{
	public static Logger log=LoggerFactory.getLogger(JoinInController.class);
	@Resource(name="addressQueryService")
	private AddressQueryManager joinIn;
	
	@RequiresPermissions("joinIn:list")
	@RequestMapping("/list")
	public String list(Map<String,Object> map,Page page){
		log.info("join分页,参数为:"+page);
		PageData pd = this.getPageData();
		pd.put("siteId", RequestUtils.getSite(this.getRequest()).getId());
		page.setPd(pd);
		try {
			log.info("开始调用join分页service");
			List<AddressQuery> listPage = joinIn.queryListPage(page);
			log.info("join分页service查询结果为:"+listPage.toString());
			map.put("listPage", listPage);
		} catch (Exception e) {
			log.error("join分页异常", e);
		}
		return "system/roling/joinIn_list";
	}
	
	@RequiresPermissions("joinIn:toAdd")
	@RequestMapping("/toAdd")
	public String toAdd(){
		log.info("join跳转添加页面");
		return "system/roling/joinIn_add";
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public Map<String,Object> add(AddressQuery address){
		log.info("join进入保存的方法,参数为:"+address);
		Map<String,Object> map=new HashMap<>();
		address.setCreateTime(DateUtil.getDay());
		address.setId(get32UUID());
		address.setSiteId(RequestUtils.getSite(this.getRequest()).getId());
		try {
			log.info("开始调用join保存service");
			joinIn.save(address);
			log.info("join调用service保存成功:"+true);
			map.put("success", true);
		} catch (Exception e) {
			map.put("success", false);
			log.error("join控制器保存失败:",e);
		}
		return map;
	}
	
	@RequiresPermissions("joinIn:toUpdate")
	@RequestMapping("/toUpdate")
	public String toUpdate(Map<String,Object> map,String id){
		log.info("join进入跳转修改页面方法,参数为:"+id);
		try {
			log.info("开始调用join查询service");
			AddressQuery addressQuery = joinIn.findById(id);
			log.info("join调用service查询结果为:"+addressQuery);
			map.put("addressQuery", addressQuery);
		} catch (Exception e) {
			log.error("join控制器跳转修改页面失败:",e);
		}
		return "system/roling/joinIn_edit";
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public Map<String,Object> update(AddressQuery address){
		log.info("join进入修改的方法,参数为:"+address);
		Map<String,Object> map=new HashMap<>();
		try {
			log.info("开始调用join修改service");
			joinIn.update(address);
			log.info("join调用service修改成功:"+true);
			map.put("success", true);
		} catch (Exception e) {
			log.error("join控制器修改失败:",e);
		}
		return map;
	}
	@RequiresPermissions("joinIn:delete")
	@RequestMapping("/delete")
	public String delete(String[] ids){
		log.info("join进入删除的方法,参数为:"+ids);
		try {
			log.info("开始调用join删除service");
			joinIn.delete(ids);
			log.info("join调用service删除成功:"+true);
		} catch (Exception e) {
			log.error("join控制器删除失败:",e);
		}
		return "redirect:list.do";
	}
	@RequestMapping("/exprotTemplate")
	public void exprotTemplate(HttpServletRequest request, HttpServletResponse response){
		logger.info("进入导出门店查询模板的方法");
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("门店查询导入模板");
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("门店名称");
		cell = row.createCell(1);
		cell.setCellValue("省");
		cell = row.createCell(2);
		cell.setCellValue("市");
		cell = row.createCell(3);
		cell.setCellValue("区域");
		cell = row.createCell(4);
		cell.setCellValue("X_POINT");
		cell = row.createCell(5);
		cell.setCellValue("Y_POINT");
		cell = row.createCell(6);
		cell.setCellValue("电话");
		cell = row.createCell(7);
		cell.setCellValue("地址");
		cell = row.createCell(8);
		cell.setCellValue("品牌");
		// 把excel返回客户端
		try {
			OutputStream os;
			response.setContentType("application/octet-stream;charset=UTF-8");
			String browser = request.getHeader("User-Agent");
			String fn = URLEncoder.encode("门店查询导入", "UTF-8");
			if (browser.toLowerCase().contains("firefox")) {
				fn = new String("门店查询导入".getBytes("UTF-8"), "ISO8859-1");
			}
			response.addHeader("Content-Disposition", "attachment;filename=" + fn + ".xls");
			os = response.getOutputStream();
			workbook.write(os);
			os.flush();
		} catch (UnsupportedEncodingException e) {
			logger.error("获得门店查询维护导入模板时异常为: ");
		} catch (IOException e) {
			logger.error("获得门店查询维护导入模板时io异常为: ");
		}
		logger.info("导出门店查询模板的方法结束");
	}
	
	@RequestMapping("/improt")
	public void improt(MultipartFile file){
		logger.info("进入批量导入门店查询方法");
		try{
			Workbook work = WorkbookFactory.create(file.getInputStream());
			Sheet sheet = work.getSheetAt(0);
			Row row = null;
			Cell cell = null;
			AddressQuery address = null;
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {// 行
				row = sheet.getRow(i);
				address = new AddressQuery();
				address.setCreateTime(DateUtil.getDay());
				address.setId(get32UUID());
				address.setSiteId(RequestUtils.getSite(this.getRequest()).getId());
				for (int j = 0; j < row.getLastCellNum(); j++) {// 列
					cell = row.getCell(j);
					if (j == 0) {// 第一列:门店名称
						address.setMyName(cell.getStringCellValue());
					} else if (j == 1) {// 省
						address.setProvince(cell.getStringCellValue());
					} else if (j == 2) {// 市
						address.setCity(cell.getStringCellValue());
					} else if (j == 3) {// 区域
						address.setArea(cell.getStringCellValue());
					} else if (j == 4) {// X_POINT
						address.setxPoint(cell.getStringCellValue());
					} else if (j == 5) {// Y_POINT
						address.setyPoint(cell.getStringCellValue());
					}else if (j == 6) {// 电话
						cell.setCellType(CellType.STRING);
						address.setTel(cell.getStringCellValue());
					}else if (j == 7) {// 地址
						address.setAddress(cell.getStringCellValue());
					}else if (j == 8) {// 品牌
						address.setBrand(cell.getStringCellValue());
					}
				}
				logger.info("批量导入门店查询控制器开始调用service");
				joinIn.save(address);
			}
			
		}catch(Exception e){
			logger.error("批量导入门店查询控制器异常为:",e);
		}
		logger.info("批量导入门店查询控制器开始结束");
	}
	@RequestMapping("/exprot")
	public void exprot(String[] ids,HttpServletRequest request, HttpServletResponse response){
		PageData pd = this.getPageData();
		pd.put("ids", ids);
		pd.put("SITEID", RequestUtils.getSite(this.getRequest()).getId());
		logger.info("进入导出门店查询模板的方法");
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("门店查询类表");
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("门店名称");
		cell = row.createCell(1);
		cell.setCellValue("省");
		cell = row.createCell(2);
		cell.setCellValue("市");
		cell = row.createCell(3);
		cell.setCellValue("区域");
		cell = row.createCell(4);
		cell.setCellValue("X_POINT");
		cell = row.createCell(5);
		cell.setCellValue("Y_POINT");
		cell = row.createCell(6);
		cell.setCellValue("电话");
		cell = row.createCell(7);
		cell.setCellValue("地址");
		cell = row.createCell(8);
		cell.setCellValue("品牌");
		// 把excel返回客户端
		try {
			List<AddressQuery> addressList=joinIn.queryList(pd);
			if(CollectionUtils.isNotEmpty(addressList)){
				for(int i=1;i<= addressList.size();i++){
					row = sheet.createRow(i);
					cell = row.createCell(0);
					cell.setCellValue(addressList.get(i-1).getMyName());
					cell = row.createCell(1);
					cell.setCellValue(addressList.get(i-1).getProvince());
					cell = row.createCell(2);
					cell.setCellValue(addressList.get(i-1).getCity());
					cell = row.createCell(3);
					cell.setCellValue(addressList.get(i-1).getArea());
					cell = row.createCell(4);
					cell.setCellValue(addressList.get(i-1).getxPoint());
					cell = row.createCell(5);
					cell.setCellValue(addressList.get(i-1).getyPoint());
					cell = row.createCell(6);
					cell.setCellValue(addressList.get(i-1).getTel());
					cell = row.createCell(7);
					cell.setCellValue(addressList.get(i-1).getAddress());
					cell = row.createCell(8);
					cell.setCellValue(addressList.get(i-1).getBrand());
				}
			}
			OutputStream os;
			response.setContentType("application/octet-stream;charset=UTF-8");
			String browser = request.getHeader("User-Agent");
			String fn = URLEncoder.encode("门店查询列表", "UTF-8");
			if (browser.toLowerCase().contains("firefox")) {
				fn = new String("门店查询列表".getBytes("UTF-8"), "ISO8859-1");
			}
			response.addHeader("Content-Disposition", "attachment;filename=" + fn + ".xls");
			os = response.getOutputStream();
			workbook.write(os);
			os.flush();
		} catch (Exception e) {
			logger.error("门店查询列表导出异常为: ");
		} 
		logger.info("门店查询列表导出方法结束");
	}
}
