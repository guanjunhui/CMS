package cn.cebest.controller.front.cms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.bo.TypeInfoBo;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.content.Content;
import cn.cebest.entity.system.content.ContentType;
import cn.cebest.entity.system.download.FileType;
import cn.cebest.entity.system.newMessage.NewMessage;
import cn.cebest.entity.system.newMessage.NewMessageType;
import cn.cebest.entity.system.product.Product;
import cn.cebest.entity.system.product.Product_Type;
import cn.cebest.service.system.columconfig.ColumconfigService;
import cn.cebest.service.system.content.content.ContentService;
import cn.cebest.service.system.content.contentType.ContentTypeService;
import cn.cebest.service.system.download.FileResourceService;
import cn.cebest.service.system.download.FileTypeService;
import cn.cebest.service.system.newMessage.MyMessageService;
import cn.cebest.service.system.newMessage.MyMessageTypeService;
import cn.cebest.service.system.product.ProductService;
import cn.cebest.service.system.product.ProductTypeService;
import cn.cebest.util.AppUtil;
import cn.cebest.util.Const;
import cn.cebest.util.ExtendFiledUtil;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;
/**
 * 前台查询产品控制层
 * @author blue
 * @param <V>
 *
 */
@Controller
@RequestMapping(value = "/productfront")
public class ProductFrontController<V> extends BaseController{

	@Resource(name = "productService")
	private ProductService productService;
	
	@Autowired
	private ColumconfigService columconfigService;
	@Autowired
	private ProductTypeService productTypeService;
	@Autowired
	private FileTypeService fileTypeService;
	@Autowired
	private MyMessageTypeService messageTypeService;
	@Autowired
	private ContentTypeService contentTypeService;
	@Autowired
	private MyMessageService messageService;
	@Autowired
	private ContentService contentService;
	@Autowired
	private FileResourceService fileResourceService;
	
	@RequestMapping("/getProductByIdList")
	@ResponseBody
	public Object getProductByIdList(Page page) throws Exception {
		// 用page传参
		PageData pd = this.getPageData();
		String ids = (String) pd.get("ids");
		String[] idArray = ids.split(",");
		pd.put("ids", idArray);
		page.setPd(pd);
		List<PageData> productlist = productService.getProductByIdList(page);
		Map<String, Object> resultMap = new HashMap<String,Object>();
		resultMap.put("list", productlist);
		resultMap.put("page", page);
		JsonResult jsonResult=new JsonResult(200, "ok",resultMap);
		return AppUtil.returnObjectByJsonResult(pd, jsonResult);
	}
	
	@ResponseBody
	@RequestMapping("findProducts")
	public Object findProducts(String HOT) throws Exception{
		PageData pd = this.getPageData();
		pd.put("HOT", HOT);
		List<PageData> productlist = productService.findProducts(pd);
		JsonResult jsonResult=new JsonResult(200, "ok",productlist);
		return AppUtil.returnObjectByJsonResult(pd, jsonResult);
	}
	
	/**
	 * 通过资讯类型id或者栏目columId或两者同时去查询相关资讯
	 * 
	 * @param id 类型ID
	 * @param columId 栏目ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getProductById")
	@ResponseBody
	public Object list(String columId,String typeId,String name,Page page) throws Exception {
		// 用page传参
		PageData pd = this.getPageData();
		page.setPd(pd);
		List<PageData> productlist = productService.findProducts(page);
		for (int i = 0; i < productlist.size(); i++) {
			ObjectMapper objectMapper = new ObjectMapper();
			if (productlist.get(i).get("FILEDJSON") != null && !"".equals(productlist.get(i).get("FILEDJSON"))) {
				@SuppressWarnings("unchecked")
				List<ExtendFiledUtil> fileds = objectMapper.readValue(String.valueOf(productlist.get(i).get("FILEDJSON")), List.class);
				productlist.get(i).put("FILEDJSON", fileds);
			}
		}
		Map<String, Object> resultMap = new HashMap<String,Object>();
		resultMap.put("list", productlist);
		resultMap.put("page", page);
		JsonResult jsonResult=new JsonResult(200, "ok",resultMap);
		return AppUtil.returnObjectByJsonResult(pd, jsonResult);
	}
	
	/**
	 * 通过类型查询产品
	 * @param columId 栏目id
	 *
	 * @return
	 */
	/*@RequestMapping("/getProductByType")
	@ResponseBody
	public Object selectProductByTypeId(String columId,String xilieType,String fenggeType,
			String homeType,Integer currentPage,Integer showCount)throws Exception{
		Integer Page = currentPage;
		Integer Count = showCount;
		Page page=new Page(Page,Count);
		PageData pd = new PageData();
		pd.put("columId", columId);
		pd.put("xilieType", xilieType);
		pd.put("fenggeType", fenggeType);
		pd.put("homeType", homeType);
		page.setPd(pd);
		
		Map<String, Object> productMap = new HashMap<String, Object>();
		List<Product> list = productService.queryProductByColumnSelectType(page);
		productMap.put("list", list);
		productMap.put("page", page);
		return new JsonResult(Const.HTTP_OK, "OK",productMap);
	}*/
	/**
	 * 根据id查询产品
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/selectProductById")
	@ResponseBody
	public Object selectProductById(String id)throws Exception{
		Product product = productService.findProductById(id);
		return new JsonResult(Const.HTTP_OK, "OK",product);
	}
	
	
	
	@RequestMapping("/getAllProduct")
	@ResponseBody
	public Object selectAllProduct(@RequestBody(required=true) Map<String,Object> jsondata)throws Exception{
		Map<Object,Object> map = new HashMap();
		PageData pd = new PageData();
		Page page=new Page();
		for (Map.Entry<String, Object> entry : jsondata.entrySet()) {
			pd.put(entry.getKey(), String.valueOf(entry.getValue()));
		}
		if(pd.getString("currentPage") != null  && !"".equals(pd.getString("currentPage"))){
			page.setCurrentPage(Integer.valueOf(pd.getString("currentPage")));
		}
		if(pd.getString("showCount") != null  && !"".equals(pd.getString("showCount"))){
			
			page.setShowCount(Integer.valueOf(pd.getString("showCount")));
		}
		page.setPd(pd);
		List<Product> list = null;
		try {
			list = productService.selectProductByAllColumdlistPage(page);
			map.put("list", list);
			map.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JsonResult(Const.HTTP_OK, "OK",map);
	}
	
	/**
	 * 根据栏目id,分类id去查下载的商品
	 * @param jsondata
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getAllfile")
	@ResponseBody
	public Object selectAllFile(@RequestBody(required=true) Map<String,Object> jsondata)throws Exception{
		Map<Object,Object> map = new HashMap();
		PageData pd = new PageData();
		Page page=new Page();
		for (Map.Entry<String, Object> entry : jsondata.entrySet()) {
			pd.put(entry.getKey(), String.valueOf(entry.getValue()));
		}
		if(pd.getString("currentPage") != null  && !"".equals(pd.getString("currentPage"))){
			page.setCurrentPage(Integer.valueOf(pd.getString("currentPage")));
		}
		if(pd.getString("showCount") != null  && !"".equals(pd.getString("showCount"))){
			
			page.setShowCount(Integer.valueOf(pd.getString("showCount")));
		}
		page.setPd(pd);
		List<FileType> list = null;
		try {
			list = fileTypeService.selectFileByColumdlistPage(page);
			map.put("list", list);
			map.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JsonResult(Const.HTTP_OK, "OK",map);
	}
	
	@RequestMapping("/getMessage")
	@ResponseBody
	public Object getMessage(@RequestBody(required=true)Map<String,Object> jsondata) throws Exception{
		Map<Object,Object> map = new HashMap();
		PageData pd = new PageData();
		Page page=new Page();
		for (Map.Entry<String, Object> entry : jsondata.entrySet()) {
			pd.put(entry.getKey(), String.valueOf(entry.getValue()));
		}
		pd.put("sort", "sort");
		page.setCurrentPage(Integer.valueOf(pd.getString("currentPage")));
		page.setShowCount(Integer.valueOf(pd.getString("showCount")));
		page.setPd(pd);
		List<NewMessage> list=null;
		try {
			 list = messageService.selectMessageBycolumtypeid(page);
			 map.put("list", list);
			 map.put("page", page);
		} catch (Exception e) {
			logger.error("find the message ocurred error!",e);
		}
		
		return new JsonResult(Const.HTTP_OK, "OK",map);
	}
	
	
	/**
	 * 内容列表
	 * @param page
	 */
	@RequestMapping("/getContentList")
	@ResponseBody
	public JsonResult getContentList(Page page){
		PageData pd = this.getPageData();
		
		//String ids = (String) pd.get("typeId");
		String typeIds1 = (String) pd.get("typeIds1");
		String typeIds2 = (String) pd.get("typeIds2");
		String typeIds3 = (String) pd.get("typeIds3");
		if(!"".equals(typeIds1) && typeIds1 != null){
			String[] array = typeIds1.split(",");
			pd.put("ids1", array);
		}
		if(!"".equals(typeIds2) && typeIds2 != null){
			String[] array = typeIds2.split(",");
			pd.put("ids2", array);
		}
		if(!"".equals(typeIds3) && typeIds3 != null){
			String[] array = typeIds3.split(",");
			pd.put("ids3", array);
		}
		page.setPd(pd);
		JsonResult jr = contentService.getContentList(page);
		
		return jr;
	}
	
	/**
	 * 资讯列表
	 * @param page
	 */
	@RequestMapping("/getNewsList")
	@ResponseBody
	public JsonResult getNewsList(Page page){
		PageData pd = this.getPageData();
		page.setPd(pd);
		JsonResult jr = contentService.getNewsList(page);
		
		return jr;
	}
	
	/**
	 * 下载栏目列表
	 * @param page
	 */
	@RequestMapping("/getDowList")
	@ResponseBody
	public JsonResult getDowList(Page page){
		PageData pd = this.getPageData();
		page.setPd(pd);
		JsonResult jr = fileResourceService.getDowList(page);
		
		return jr;
	}
	
	/**
	 * 根据栏目id去查分类集合
	 */
	@RequestMapping("/findTypeListById")
	@ResponseBody
	public Map findTypeListById(String columId){
		ColumConfig columCurrent=null;
		List<TypeInfoBo>  typeList = null;
		HashMap<Object,Object> map = new HashMap<>();
		try {
			columCurrent =columconfigService.findColumconfigPoById(new PageData("ID", columId));
			//查询每个栏目对应的分类
			this.setType(columCurrent);
			typeList=columCurrent.getTypeList();
			this.sort(typeList);
		    
		} catch (Exception e) {
			logger.error("get the type by the columId occured error!",e);
		}
		map.put("list", typeList);
		return map;
	}
	

	public void sort(List<TypeInfoBo> typeList){
		if(CollectionUtils.isEmpty(typeList)){
			return;
		}
		Collections.sort(typeList, new Comparator<TypeInfoBo>(){
			@Override
			public int compare(TypeInfoBo o1, TypeInfoBo o2) {
				int A=o1.getSort()==null?Integer.MAX_VALUE:o1.getSort().intValue();
				int B=o2.getSort()==null?Integer.MAX_VALUE:o2.getSort().intValue();
				return A-B;
			}
		});
		for(TypeInfoBo type:typeList){
			this.sort(type.getChildList());
		}
	}
	
	//查询栏目下所有的分类
	public void setType(ColumConfig colum) throws Exception{
		List<TypeInfoBo> resultTypeList=new ArrayList<TypeInfoBo>();
		switch(colum.getColumType()){
			case Const.TEMPLATE_TYPE_1://内容模板
				List<ContentType> contentTypeList = this.contentTypeService.findContentTypeByColumnIds(colum.getId());
				this.convertContentTypeList(contentTypeList, resultTypeList, colum.getId());
				break;
			case Const.TEMPLATE_TYPE_2://资讯模板
				List<NewMessageType> messageTypeList=this.messageTypeService.findMessage_TypeByColumnIds(colum.getId());
				this.convertMesageTypeList(messageTypeList, resultTypeList,colum.getId());
				break;
			case Const.TEMPLATE_TYPE_3://产品模板
				//查询栏目下的分类
				List<Product_Type> productTypeList=this.productTypeService.
					findProduct_TypeByColumnIds(colum.getId());
				//根据栏目查询分类对应的产品数量
				List<PageData> countList=this.productTypeService.
						selectCountByTypeAndColumID(colum.getId());
				
				this.convertProductTypeList(productTypeList, resultTypeList,colum.getId(),countList);
				
				break;
			case Const.TEMPLATE_TYPE_5://下载模板
				List<FileType> fileTypeList=this.fileTypeService.findFileTypeByColumnIds(colum.getId());
				this.convertFileTypeList(fileTypeList, resultTypeList,colum.getId());
				break;
			default:break;
		}
		Collections.sort(resultTypeList, new Comparator<TypeInfoBo>(){
			@Override
			public int compare(TypeInfoBo o1, TypeInfoBo o2) {
				int A=o1.getSort()==null?Integer.MAX_VALUE:o1.getSort().intValue();
				int B=o2.getSort()==null?Integer.MAX_VALUE:o2.getSort().intValue();
				return A-B;
			}
		});
		colum.setTypeList(resultTypeList);
	}
	
	//转换内容分类
	public void convertContentTypeList(List<ContentType> typeList,
			List<TypeInfoBo> resultList,String columId){
		if(CollectionUtils.isEmpty(typeList)){
			return;
		}
		Integer sum = 0;
		for(ContentType contentType:typeList){
			TypeInfoBo typeInfoBo=new TypeInfoBo();
			resultList.add(typeInfoBo);
			typeInfoBo.setId(contentType.getId());
			typeInfoBo.setName(contentType.getTypeName());
			typeInfoBo.setTemPlatePath(contentType.getTemplate()!=null
					?contentType.getTemplate().getTemFilepath():StringUtils.EMPTY);
			typeInfoBo.setSort(contentType.getSort());
			typeInfoBo.setType(Const.TEMPLATE_TYPE_1);
			typeInfoBo.setColumId(columId);
			typeInfoBo.setColumName(contentType.getColumConfigList().get(sum).getColumName());
			typeInfoBo.setStatus(contentType.getTypeStatus());
			if(CollectionUtils.isNotEmpty(contentType.getChildList())){
				typeInfoBo.setChildList(new ArrayList<TypeInfoBo>());
				this.convertContentTypeList(contentType.getChildList(), typeInfoBo.getChildList(),columId);
			}
		}
	}
	
	//转换咨询分类
	public void convertMesageTypeList(List<NewMessageType> typeList,
			List<TypeInfoBo> resultList,String columId){
		if(CollectionUtils.isEmpty(typeList)){
			return;
		}
		for(NewMessageType newMessageType:typeList){
			TypeInfoBo typeInfoBo=new TypeInfoBo();
			resultList.add(typeInfoBo);
			typeInfoBo.setId(newMessageType.getId());
			typeInfoBo.setName(newMessageType.getType_name());
			typeInfoBo.setTemPlatePath(newMessageType.getTemplate()!=null
					?newMessageType.getTemplate().getTemFilepath():StringUtils.EMPTY);
			typeInfoBo.setSort(newMessageType.getSort());
			typeInfoBo.setType(Const.TEMPLATE_TYPE_2);
			typeInfoBo.setColumId(columId);
			if(CollectionUtils.isNotEmpty(newMessageType.getChildList())){
				typeInfoBo.setChildList(new ArrayList<TypeInfoBo>());
				this.convertMesageTypeList(newMessageType.getChildList(), typeInfoBo.getChildList(),columId);
			}
		}
	}
	
	//转换产品分类
	public void convertProductTypeList(List<Product_Type> typeList,
			List<TypeInfoBo> resultList,String columId,List<PageData> countList){
		if(CollectionUtils.isEmpty(typeList)){
			return;
		}
		for(Product_Type type:typeList){
			TypeInfoBo typeInfoBo=new TypeInfoBo();
			resultList.add(typeInfoBo);
			typeInfoBo.setId(type.getId());
			typeInfoBo.setName(type.getType_name());
			typeInfoBo.setTemPlatePath(type.getTemplate()!=null
					?type.getTemplate().getTemFilepath():StringUtils.EMPTY);
			typeInfoBo.setSort(type.getSort());
			typeInfoBo.setType(Const.TEMPLATE_TYPE_3);
			typeInfoBo.setColumId(columId);
			typeInfoBo.setImageUrl(type.getImgurl());
			
			typeInfoBo.setPid(type.getPid());
			if(CollectionUtils.isNotEmpty(countList)){
				for(PageData countMap:countList){
					if(type.getId().equals(countMap.getString("typeId"))){
						typeInfoBo.setCount((Long)countMap.get("count"));
					}
				}
			}
			if(CollectionUtils.isNotEmpty(type.getChildList())){
				typeInfoBo.setChildList(new ArrayList<TypeInfoBo>());
				this.convertProductTypeList(type.getChildList(), typeInfoBo.getChildList(),columId,countList);
			}
		}
	}

	//转换下载分类
	public void convertFileTypeList(List<FileType> typeList,
			List<TypeInfoBo> resultList,String columId){
		if(CollectionUtils.isEmpty(typeList)){
			return;
		}
		for(FileType type:typeList){
			TypeInfoBo typeInfoBo=new TypeInfoBo();
			resultList.add(typeInfoBo);
			typeInfoBo.setId(type.getDownload_id());
			typeInfoBo.setName(type.getDownload_name());
			typeInfoBo.setTemPlatePath(type.getTemplate()!=null
					?type.getTemplate().getTemFilepath():StringUtils.EMPTY);
			typeInfoBo.setSort(type.getSort());
			typeInfoBo.setType(Const.TEMPLATE_TYPE_5);
			typeInfoBo.setColumId(columId);
			if(CollectionUtils.isNotEmpty(type.getChildList())){
				typeInfoBo.setChildList(new ArrayList<TypeInfoBo>());
				this.convertFileTypeList(type.getChildList(), typeInfoBo.getChildList(),columId);
			}
		}
	}
	
	
}
