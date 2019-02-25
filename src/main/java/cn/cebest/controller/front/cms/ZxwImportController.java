package cn.cebest.controller.front.cms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.download.FileResources;
import cn.cebest.entity.system.product.Product;
import cn.cebest.service.system.columconfig.ColumconfigService;
import cn.cebest.service.system.content.content.ContentExtendFiledService;
import cn.cebest.service.system.download.FileResourceService;
import cn.cebest.service.system.product.ProductService;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.ExtendFiledUtil;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;
import cn.cebest.util.ReadExcelUtil;
import cn.cebest.util.RequestUtils;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("import")
public class ZxwImportController extends BaseController{

	@Resource(name = "fileResourceService")
	private FileResourceService service;

	@Resource(name = "contentExtendFiledServiceImpl")
	private ContentExtendFiledService contentExtendFiledService;
	
	@Resource(name = "columconfigService")
	private ColumconfigService columconfigService;
	
	@Resource(name = "productService")
	private ProductService productService;

	@RequestMapping("importList")
	@ResponseBody
	public JsonResult importList(MultipartFile file,String type) throws Exception{
		PageData pd = this.getPageData();
		
		if(file == null || "".equals(file)){ 
			return new JsonResult(Const.HTTP_ERROR, "错误");
		}else{
			try {
				String name = file.getOriginalFilename();
				String siteId = RequestUtils.getSite(this.getRequest()).getId();// 站点id
				if (name.matches("^.+\\.(?i)(xlsx)$")) {
					saveXlsx(file, pd, siteId,type);
				}
				;
				if (name.matches("^.+\\.(?i)(xls)$")) {
					saveXls(file, pd, siteId,type);
				}
				return  new JsonResult(Const.HTTP_OK, "ok");
			} catch (Exception e) {
				
				e.printStackTrace();
				return new JsonResult(Const.HTTP_ERROR, "fail");
			}
			
		}
		
	}
	
	private void saveXls(MultipartFile file, PageData pd, String siteId,String type) throws Exception {
		
		
		ReadExcelUtil readExcel = new ReadExcelUtil();
		List<Map<String,Object>> excelInfo = readExcel.getExcelInfo(file);
		switch (type) {
		case "3":
			Product product = new Product();
			for (Map<String, Object> info : excelInfo) {
				Map titleMap = (Map) info.get("title");
				String  columnName  = (String)titleMap.get("var1");
				String  typeName  = (String)titleMap.get("var2");
				product.setName((String)titleMap.get("var3"));//产品标题
				product.setProductTxt((String)titleMap.get("var4"));//富文本
				product.setId(this.get32UUID());
				pd.put("productId", product.getId());
				product.setCreated_Time(DateUtil.getTime());
				product.setSiteId(siteId);
				try {
					//根据栏目名字查询栏目id
					ColumConfig column = columconfigService.selectColumnIdByName(columnName);
					if(column != null && !"".equals(column)){
						product.setProduct_TemplateId(column.getColumndetailId());//设置详情模板
						pd.put("COLUM_ID", column.getId());
					}
					//根据分类名称获取分类id
					Product pro=productService.findProductByname(typeName);
					String productTypeId = pro.getId();
					pd.put("productTypeId", productTypeId);
					
				} catch (Exception e) {
					logger.error("find columid typeid by name  error ");
					e.printStackTrace();
				}
				Map map = (Map) info.get("extendFiled");
				List<String> extendName = new ArrayList<String>(map.keySet());
				List<String> extendValue = new ArrayList<String>(map.values());
				List<ExtendFiledUtil> list = new ArrayList<>();
				for (int i = 0; i < extendName.size(); i++) {
					ExtendFiledUtil filedUtil = new ExtendFiledUtil();
					String value = extendName.get(i);//获取表头数据
					String val = extendValue.get(i);//获取每行的数据
					//根据名字和栏目id查询
					pd.put("typeName", value);
					Map extendMap=contentExtendFiledService.findIdByName(pd);
					if(extendMap != null && !extendMap.isEmpty()){
						String  id = (String) extendMap.get("ID");
						filedUtil.setId(id);//属性id
					}
					filedUtil.setName(value);//属性name
					filedUtil.setSort(i+1);//属性排序
					filedUtil.setFieldtype("1");//属性文本类型
					filedUtil.setValue(val);//属性value
					filedUtil.setPid(null);
					filedUtil.setType(null);
					list.add(filedUtil);
				}
				Gson gson = new Gson();
				String json = gson.toJson(list);
				product.setFiledJson(json); 
				//执行添加
				productService.saveImportData(product);
				//保存分类关系
				productService.insertTypeRealtion(pd);
			}
			break;

		default:
			FileResources fileResourse=new FileResources();
			for (Map<String, Object> info : excelInfo) {
				Map titleMap = (Map) info.get("title");
				//取前三个值 获取数据
				String  columnName  = (String)titleMap.get("var1");
				String  typeName  = (String)titleMap.get("var2");
				fileResourse.setTitle((String)titleMap.get("var3"));//产品标题
				fileResourse.setTXT((String)titleMap.get("var4"));//功能特点
				fileResourse.setFileid(this.get32UUID());//产品id
				pd.put("fileid", fileResourse.getFileid());
				fileResourse.setCreated_time(DateUtil.getTime());// 创建时间
				fileResourse.setSiteid(siteId);
				try {
					//根据栏目名字查询栏目id
					ColumConfig column = columconfigService.selectColumnIdByName(columnName);
					if(column != null && !"".equals(column)){
						fileResourse.setTemplateid(column.getColumndetailId());//设置详情模板
						pd.put("COLUM_ID", column.getId());
					}
					//根据分类名称获取分类id
					FileResources resourse= service.findTypeIdByName(typeName);
					//分类id
					String download_id = resourse.getDownload_id();
					pd.put("download_id", download_id);
				} catch (Exception e) {
					logger.error("find columid typeid by name  error ");
					e.printStackTrace();
				}
				Map map = (Map) info.get("extendFiled");
				List<String> extendName = new ArrayList<String>(map.keySet());
				List<String> extendValue = new ArrayList<String>(map.values());
				List<ExtendFiledUtil> list = new ArrayList<>();
			
				for (int i = 0; i < extendName.size(); i++) {
					ExtendFiledUtil filedUtil = new ExtendFiledUtil();
					String value = extendName.get(i);//获取表头数据
					String val = extendValue.get(i);//获取每行的数据
					//根据名字和栏目id查询
					pd.put("typeName", value);
					Map extendMap=contentExtendFiledService.findIdByName(pd);
					if(extendMap != null && !extendMap.isEmpty()){
						String  id = (String) extendMap.get("ID");
						filedUtil.setId(id);//属性id
					}
					
					filedUtil.setName(value);//属性name
					filedUtil.setSort(i+1);//属性排序
					if(!"芯片参数".equals(value)){
						filedUtil.setFieldtype("1");//属性文本类型
					}else{
						filedUtil.setFieldtype("2");//属性文本类型
					}
					
					//filedUtil.setFieldtype("2");
					filedUtil.setValue(val);//属性value
					filedUtil.setPid(null);
					filedUtil.setType(null);
					list.add(filedUtil);
				}
				//将对象转换为字符串
				Gson gson = new Gson();
				String json = gson.toJson(list);
				fileResourse.setFiledJson(json);
				//执行添加数据
				service.saveImportData(fileResourse);
				//保存分类关系
				service.saveTypeRelation(pd);
			}
			break;
		}
		
		
	}
	private void saveXlsx(MultipartFile file, PageData pd, String siteId,String type) throws Exception {
		
		ReadExcelUtil readExcel = new ReadExcelUtil();
		List<Map<String,Object>> excelInfo = readExcel.getExcelInfo(file);
		switch (type) {
		case "3":
			Product product = new Product();
			for (Map<String, Object> info : excelInfo) {
				Map titleMap = (Map) info.get("title");
				String  columnName  = (String)titleMap.get("var1");
				String  typeName  = (String)titleMap.get("var2");
				product.setName((String)titleMap.get("var3"));//产品标题
				product.setProductTxt((String)titleMap.get("var4"));//富文本
				product.setId(this.get32UUID());
				pd.put("productId", product.getId());
				product.setCreated_Time(DateUtil.getTime());
				product.setSiteId(siteId);
				try {
					//根据栏目名字查询栏目id
					ColumConfig column = columconfigService.selectColumnIdByName(columnName);
					if(column != null && !"".equals(column)){
						product.setProduct_TemplateId(column.getColumndetailId());//设置详情模板
						pd.put("COLUM_ID", column.getId());
					}
					//根据分类名称获取分类id
					Product pro=productService.findProductByname(typeName);
					String productTypeId = pro.getId();
					pd.put("productTypeId", productTypeId);
					
				} catch (Exception e) {
					logger.error("find columid typeid by name  error ");
					e.printStackTrace();
				}
				Map map = (Map) info.get("extendFiled");
				List<String> extendName = new ArrayList<String>(map.keySet());
				List<String> extendValue = new ArrayList<String>(map.values());
				List<ExtendFiledUtil> list = new ArrayList<>();
				for (int i = 0; i < extendName.size(); i++) {
					ExtendFiledUtil filedUtil = new ExtendFiledUtil();
					String value = extendName.get(i);//获取表头数据
					String val = extendValue.get(i);//获取每行的数据
					//根据名字和栏目id查询
					pd.put("typeName", value);
					Map extendMap=contentExtendFiledService.findIdByName(pd);
					if(extendMap != null && !extendMap.isEmpty()){
						String  id = (String) extendMap.get("ID");
						filedUtil.setId(id);//属性id
					}
					filedUtil.setName(value);//属性name
					filedUtil.setSort(i+1);//属性排序
					filedUtil.setFieldtype("1");//属性文本类型
					filedUtil.setValue(val);//属性value
					filedUtil.setPid(null);
					filedUtil.setType(null);
					list.add(filedUtil);
				}
				Gson gson = new Gson();
				String json = gson.toJson(list);
				product.setFiledJson(json); 
				//执行添加
				productService.saveImportData(product);
				//保存分类关系
				productService.insertTypeRealtion(pd);
			}
			break;

		default:
			FileResources fileResourse=new FileResources();
			for (Map<String, Object> info : excelInfo) {
				Map titleMap = (Map) info.get("title");
				//取前三个值 获取数据
				String  columnName  = (String)titleMap.get("var1");
				String  typeName  = (String)titleMap.get("var2");
				fileResourse.setTitle((String)titleMap.get("var3"));//产品标题
				fileResourse.setTXT((String)titleMap.get("var4"));//功能特点
				fileResourse.setFileid(this.get32UUID());//产品id
				pd.put("fileid", fileResourse.getFileid());
				fileResourse.setCreated_time(DateUtil.getTime());// 创建时间
				fileResourse.setSiteid(siteId);
				try {
					//根据栏目名字查询栏目id
					ColumConfig column = columconfigService.selectColumnIdByName(columnName);
					if(column != null && !"".equals(column)){
						fileResourse.setTemplateid(column.getColumndetailId());//设置详情模板
						pd.put("COLUM_ID", column.getId());
					}
					//根据分类名称获取分类id
					FileResources resourse= service.findTypeIdByName(typeName);
					//分类id
					String download_id = resourse.getDownload_id();
					pd.put("download_id", download_id);
				} catch (Exception e) {
					logger.error("find columid typeid by name  error ");
					e.printStackTrace();
				}
				Map map = (Map) info.get("extendFiled");
				List<String> extendName = new ArrayList<String>(map.keySet());
				List<String> extendValue = new ArrayList<String>(map.values());
				List<ExtendFiledUtil> list = new ArrayList<>();
			
				for (int i = 0; i < extendName.size(); i++) {
					ExtendFiledUtil filedUtil = new ExtendFiledUtil();
					String value = extendName.get(i);//获取表头数据
					String val = extendValue.get(i);//获取每行的数据
					//根据名字和栏目id查询
					pd.put("typeName", value);
					Map extendMap=contentExtendFiledService.findIdByName(pd);
					if(extendMap != null && !extendMap.isEmpty()){
						String  id = (String) extendMap.get("ID");
						filedUtil.setId(id);//属性id
					}
					
					filedUtil.setName(value);//属性name
					filedUtil.setSort(i+1);//属性排序
					if(!"芯片参数".equals(value)){
						filedUtil.setFieldtype("1");//属性文本类型
					}else{
						filedUtil.setFieldtype("2");//属性文本类型
					}
					
					//filedUtil.setFieldtype("2");
					filedUtil.setValue(val);//属性value
					filedUtil.setPid(null);
					filedUtil.setType(null);
					list.add(filedUtil);
				}
				//将对象转换为字符串
				Gson gson = new Gson();
				String json = gson.toJson(list);
				fileResourse.setFiledJson(json);
				//执行添加数据
				service.saveImportData(fileResourse);
				//保存分类关系
				service.saveTypeRelation(pd);
			}
			break;
		}
		
	}
}
