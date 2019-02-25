package cn.cebest.controller.front.cms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import javax.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Node;
import cn.cebest.entity.bo.ColumInfoBo;
import cn.cebest.entity.bo.SelectTemplateBo;
import cn.cebest.entity.bo.TypeInfoBo;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Seo;
import cn.cebest.entity.system.content.Content;
import cn.cebest.entity.system.download.FileType;
import cn.cebest.entity.system.newMessage.NewMessageType;
import cn.cebest.entity.system.product.Product_Type;
import cn.cebest.entity.web.WebSite;
import cn.cebest.exception.ChannelNotFoundException;
import cn.cebest.exception.PageNotFoundException;
import cn.cebest.service.system.columconfig.ColumconfigService;
import cn.cebest.service.system.content.content.ContentService;
import cn.cebest.service.system.download.FileResourceService;
import cn.cebest.service.system.download.FileTypeService;
import cn.cebest.service.system.employ.EmployService;
import cn.cebest.service.system.newMessage.MyMessageService;
import cn.cebest.service.system.newMessage.MyMessageTypeService;
import cn.cebest.service.system.product.ProductService;
import cn.cebest.service.system.product.ProductTypeService;
import cn.cebest.service.system.seo.SeoService;
import cn.cebest.service.web.template.TemplateManager;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.FrontUtils;
import cn.cebest.util.PageData;
import cn.cebest.util.RequestUtils;
import cn.cebest.util.redis.RedisUtil;

/**
 * @author qichangxin 站点入口
 */
@Controller
public class SitePortalController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(SitePortalController.class);
	private static final String GROUP_ID = "fff712e8c4e446e4ac73f8534b1228a7"; //5b4bc3a194814756b2be6aa3f8de0415
	private static final String SEO_TITLE = "seoTitle";
	private static final String SEO_KEYWORDS = "seoKeywords";
	private static final String SEO_DESCRIPTION = "seoDescription";

	@Autowired
	private TemplateManager templateManager;
	@Autowired
	private ColumconfigService columconfigService;
	@Autowired
	private ProductTypeService productTypeService;
	@Autowired
	private FileTypeService fileTypeService;
	@Autowired
	private MyMessageTypeService messageTypeService;
	@Resource(name = "contentService")
	private ContentService contentService;
	@Resource(name = "productService")
	private ProductService productService;
	@Resource(name = "fileResourceService")
	private FileResourceService fileResourceService;
	@Resource(name = "MyMessageService")
	private MyMessageService messageService;
	@Autowired
	private EmployService employService;
	@Autowired
	private SeoService seoService;

	/**
	 * 站点寻址
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		WebSite site = RequestUtils.getSite(request);
		String sitetplPath = StringUtils.EMPTY;
		if (StringUtils.isNotEmpty(site.getSiteIndex())) {
			ColumConfig columCurrent = null;
			try {
				columCurrent = this.columconfigService.findColumconfigPoById(new PageData("ID", site.getSiteIndex()));
				//Template template = templateManager.findPoById(site.getSiteIndex());
				sitetplPath = columCurrent.getTemplate().getTemFilepath();
				Seo seo = this.seoService.selectSeoForObject(columCurrent.getId());
				if (seo != null) {
					model.addAttribute(SEO_TITLE, seo.getSeoTitle());
					model.addAttribute(SEO_KEYWORDS, seo.getSeoKeywords());
					model.addAttribute(SEO_DESCRIPTION, seo.getSeoDescription());
				} else {
					//取整站SEO
					model.addAttribute(SEO_TITLE, site.getSiteName());
					model.addAttribute(SEO_KEYWORDS, site.getSiteKeyword());
					model.addAttribute(SEO_DESCRIPTION, site.getSiteDesc());
				}
				model.addAttribute("columCurrent", columCurrent);
			} catch (Exception e) {
				logger.error("find template info by ID ocurred error", e);
			}
		}
		if (StringUtils.isEmpty(sitetplPath)) {
			throw new PageNotFoundException("the site[" + site.getSiteDomian() + "]'s index template path does not exist!");
		}
		//统计访客数
		Cookie cookie = RequestUtils.getCookie(request, "IP");
		if(cookie==null){
			RedisUtil.incr(DateUtil.fomatDate()+Const.STATIS_UV);
			RequestUtils.setCookie(request, response, "IP", RequestUtils.getRemoteAddr(request),null,false);
		}
		// 统计独立IP
		RedisUtil.addSet(DateUtil.fomatDate() + Const.STATIS_IP, RequestUtils.getRemoteAddr(request));
		return sitetplPath;
	}

	/**
	 * 导航寻址
	 * 
	 * @param request
	 * @param isChannel
	 *            是否为栏目（1：是 [栏目] 0：否[分类]）
	 * @param type
	 *            分类类别
	 * @param isSelect
	 *            是否选中栏目或类别
	 * @return
	 */
	@RequestMapping(value = "/channel/{columId}")
	public String dynamicChannel(@PathVariable("columId") String columId, 
			HttpServletRequest request, 
			@RequestParam(value = "isChannel", required = false) String isChannel, 
			@RequestParam(value = "type", required = false) String type, 
			@RequestParam(value = "typeId", required = false) String typeId, 
			@RequestParam(value = "isSelect", required = false) String isSelect, ModelMap model,
			@RequestParam(value = "login_error_msg", required = false) String login_error_msg,
			String firstName,String email,String lastName) {
		PageData  ColumUrlNamePage  =  new  PageData();
		WebSite site = RequestUtils.getSite(request);
		String templatePath = StringUtils.EMPTY;
		// 当前栏目
		ColumConfig columCurrent = null;
		Seo seo = null;
		try {
			ColumUrlNamePage = this.columconfigService.findColumUrlNameconfigById(columId);
			if(!"".equals(ColumUrlNamePage) && ColumUrlNamePage != null){
				if("".equals(isChannel) || isChannel == null){
					isChannel = String.valueOf(ColumUrlNamePage.get("Url_ISCHANNEL"));
				}
				type = String.valueOf(ColumUrlNamePage.get("Url_TYPE"));
				typeId = (String) ColumUrlNamePage.get("Url_TYPEID");
				if("".equals(isSelect) || isSelect == null){
					isSelect = String.valueOf(ColumUrlNamePage.get("Url_ISSELECT"));
				}
				columCurrent = this.columconfigService.findColumconfigPoById(new PageData("ID", ColumUrlNamePage.get("Url_COLUMNID")));
				seo = this.seoService.selectSeoForObject(ColumUrlNamePage.getString("Url_COLUMNID"));
				if (seo != null) {
					model.addAttribute(SEO_TITLE, seo.getSeoTitle());
					model.addAttribute(SEO_KEYWORDS, seo.getSeoKeywords());
					model.addAttribute(SEO_DESCRIPTION, seo.getSeoDescription());
				} else {
					//取整站SEO
					model.addAttribute(SEO_TITLE, site.getSiteName());
					model.addAttribute(SEO_KEYWORDS, site.getSiteKeyword());
					model.addAttribute(SEO_DESCRIPTION, site.getSiteDesc());
				}
			}else{
				columCurrent = this.columconfigService.findColumconfigPoById(new PageData("ID", columId));
				seo = this.seoService.selectSeoForObject(columId);
				if(seo!=null){
					model.addAttribute(SEO_TITLE, seo.getSeoTitle());
					model.addAttribute(SEO_KEYWORDS, seo.getSeoKeywords());
					model.addAttribute(SEO_DESCRIPTION, seo.getSeoDescription());
				}
			}
		} catch (Exception e) {
			logger.error("find dynamicChannel[" + columId + "]'s nav ocurred error", e);
		}
		if (columCurrent == null) {
			throw new ChannelNotFoundException("the Channel[" + columId + "]' does not exist!");
		}
		// 所有栏目
		List<ColumConfig> columAllList = null;
		try {
			PageData pageDate = new PageData();
			pageDate.put("siteid", site.getId());
			pageDate.put("COLUM_DISPLAY", Const.YES);
			columAllList = this.columconfigService.findAllList(pageDate);
		} catch (Exception e) {
			logger.error("find dynamicChannel[" + columId + "]'s nav ocurred error", e);
		}
		// 查询顶级栏目
		ColumConfig columConfigTop = null;
		List<ColumConfig> columAllListTop = new ArrayList<ColumConfig>();
		columAllListTop.addAll(columAllList);
		columConfigTop = this.searchTop(columCurrent, columAllListTop, columConfigTop);
		model.addAttribute("firstName", firstName);
		model.addAttribute("email", email);
		model.addAttribute("lastName", lastName);
		model.addAttribute("login_error_msg", login_error_msg);
		// 查询顶级栏目下所有的子栏目(树状结构)
		this.appendChild(columConfigTop, columAllList);
		// 对栏目排序
		if (CollectionUtils.isNotEmpty(columConfigTop.getSubConfigList())) {
			Collections.sort(columConfigTop.getSubConfigList(), new Comparator<ColumConfig>() {
				@Override
				public int compare(ColumConfig o1, ColumConfig o2) {
					int A = o1.getSort() == null ? Integer.MAX_VALUE : o1.getSort().intValue();
					int B = o2.getSort() == null ? Integer.MAX_VALUE : o2.getSort().intValue();
					return A - B;
				}
			});
		}
		// 查询每个栏目对应的分类
		try {
			this.setType(columCurrent);
			this.setType(columConfigTop);
		} catch (Exception e) {
			logger.error("find dynamicChannel[" + columId + "]'s nav ocurred error", e);
		}
		if (CollectionUtils.isNotEmpty(columConfigTop.getSubConfigList())) {
			for (ColumConfig colum : columConfigTop.getSubConfigList()) {
				try {
					this.setType(colum);
				} catch (Exception e) {
					logger.error("find dynamicChannel[" + columId + "]'s nav ocurred error", e);
				}
			}
		}
		// 如果是选中的栏目或分类，直接check。否则获取排序值高的模板（栏目或者分类）
		SelectTemplateBo selectTemplate = null;
		if (Const.YES.equals(isSelect)) {// 是否选中
			String template_Path = StringUtils.EMPTY;
			String id = StringUtils.EMPTY;
			String ifColumOrType = StringUtils.EMPTY;
			if (Const.NO.equals(isChannel)) {// 类别
				switch (type) {
					case Const.TEMPLATE_TYPE_2:// 资讯模板
						NewMessageType messageType = null;
						try {
							messageType = this.messageTypeService.findTypeInfoById(typeId);
						} catch (Exception e) {
							logger.error("find dynamicChannel[" + columId + "]'s nav ocurred error", e);
						}
						if (messageType != null && messageType.getTemplate() != null) {
							template_Path = messageType.getTemplate().getTemFilepath();
						}
						break;
					case Const.TEMPLATE_TYPE_3:// 产品模板
						Product_Type productType = null;
						try {
							productType = this.productTypeService.findTypeInfoById(typeId);
						} catch (Exception e) {
							logger.error("find dynamicChannel[" + columId + "]'s nav ocurred error", e);
						}
						if (productType != null && productType.getTemplate() != null) {
							template_Path = productType.getTemplate().getTemFilepath();

						}
						break;
					case Const.TEMPLATE_TYPE_5:// 下载模板
						FileType fileType = null;
						try {
							fileType = this.fileTypeService.findTypeInfoById(typeId);
						} catch (Exception e) {
							logger.error("find dynamicChannel[" + columId + "]'s nav ocurred error", e);
						}
						if (fileType != null && fileType.getTemplate() != null) {
							template_Path = fileType.getTemplate().getTemFilepath();
						}
						break;
					default:
						break;
				}
				id = typeId;
				ifColumOrType = Const.NO;
			} else {// 栏目
				if(!"".equals(ColumUrlNamePage) && ColumUrlNamePage != null){
					id = ColumUrlNamePage.getString("Url_COLUMNID");
				}else{
					id = columId;
				}
				template_Path = columCurrent.getTemplate().getTemFilepath();
				try {
					this.setType(columCurrent);
				} catch (Exception e) {
					e.printStackTrace();
				}
				selectTemplate = this.searchTemplate(columCurrent);
				ifColumOrType = Const.YES;
			}
			if (StringUtils.isNotEmpty(template_Path)) {
				selectTemplate = new SelectTemplateBo();
				selectTemplate.setId(id);
				selectTemplate.setIfColumOrType(ifColumOrType);
				selectTemplate.setTemPlatePath(template_Path);
				if(!"".equals(ColumUrlNamePage) && ColumUrlNamePage != null){
					selectTemplate.setColumId(ColumUrlNamePage.getString("Url_COLUMNID"));
				}else{
					selectTemplate.setColumId(columId);
				}
				selectTemplate.setColumParentId(columCurrent.getParentid());
				selectTemplate.setColumName(columCurrent.getColumName());
			}
		} else {// 未选中
			templatePath = columCurrent.getTemplate() != null ? columCurrent.getTemplate().getTemFilepath() : StringUtils.EMPTY;
			if (StringUtils.isNotEmpty(templatePath)) {
				selectTemplate = new SelectTemplateBo();
				selectTemplate.setId(columCurrent.getId());
				selectTemplate.setIfColumOrType(Const.YES);
				selectTemplate.setTemPlatePath(templatePath);
				selectTemplate.setColumId(columCurrent.getId());
				selectTemplate.setColumParentId(columCurrent.getParentid());
				selectTemplate.setColumName(columCurrent.getColumName());
			} else {
				selectTemplate = this.searchTemplate(columConfigTop);
				try {
					columCurrent = this.columconfigService.findColumconfigPoById(new PageData("ID", selectTemplate.getColumId()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (selectTemplate == null || StringUtils.isEmpty(selectTemplate.getTemPlatePath())) {
			throw new PageNotFoundException("the template of columID[" + columId + "],typeId[" + typeId + "] does not find!");
		}
		model.addAttribute("columConfigTop", columConfigTop);
		model.addAttribute("template", selectTemplate);
		model.addAttribute(Const.PARAM_CURRENTID, selectTemplate.getId());
		model.addAttribute("columCurrent", columCurrent);
		model.addAttribute("contentId", "");
		model.addAttribute("purple_flag", "1");
		model.addAttribute("siteName", site.getSiteName());
		model.addAttribute("firstAndLowestColumn", selectFirstAndLowestColumn(columAllList, columCurrent));
		model.addAttribute("columUrlName",columId);
		//model.addAttribute("typeId", typeId);
		if (typeId != null && !typeId.isEmpty()) {
			model.addAttribute("typeId", typeId);
		}
		// 组装面包屑
		ColumConfig columConfig = new ColumConfig();
		columConfig.setId(selectTemplate.getColumId());
		columConfig.setParentid(selectTemplate.getColumParentId());
		ColumInfoBo columInfoBo = new ColumInfoBo();
		columInfoBo.setId(selectTemplate.getColumId());
		columInfoBo.setName(selectTemplate.getColumName());
		Node<ColumInfoBo> selectNode = new Node<ColumInfoBo>(columInfoBo);
		// 查询栏目的父级栏目，存储方式为链表
		this.searchParent(columConfig, selectNode, columAllList);

		////////////////////////
		//wzd
		LinkedList<ColumConfig> columnlist = new LinkedList<ColumConfig>();
		this.appenColumnList(columCurrent, columnlist, columAllList);
		LinkedList<Node<ColumInfoBo>> list = new LinkedList<>();
		reversalList(selectNode, list);
		// reversal(selectNode);
		// 实现链表反转
		model.addAttribute("crumb", list);
		if (columnlist.size() == 0) {
			for (ColumConfig c : columAllList) {
				if (columCurrent.getParentid().equals(c.getId())) {
					this.appenColumnList(c, columnlist, columAllList);
					break;
				}
			}
		}
		///////////
		model.addAttribute("columnlist", columnlist);
		model.addAttribute("brand", request.getParameter("brand"));
		// 设置前台分页数据
		FrontUtils.frontPageData(request, model);
		if (selectTemplate == null || StringUtils.isEmpty(selectTemplate.getTemPlatePath())) {
			throw new PageNotFoundException("the template of columID[" + columId + "],typeId[" + typeId + "] does not find!");
		}
		// 统计访客数
		RedisUtil.incr(DateUtil.fomatDate() + Const.STATIS_PM);
		RedisUtil.addSet(DateUtil.fomatDate() + Const.STATIS_IP, RequestUtils.getRemoteAddr(request));
		return selectTemplate.getTemPlatePath();
	}
	
	/**
	 * 导航寻址
	 * 
	 * @param request
	 * @param type
	 *            内容类型
	 * @param contentId
	 *            内容ID
	 * @return
	 */
	/**
	 * @param contentId
	 * @param typeId
	 * @param type
	 * @param request
	 * @param columnId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/content/{contentId}", method = RequestMethod.GET)
	public String dynamicChannel(@PathVariable("contentId") String contentId, @RequestParam(value = "typeId", required = false) String typeId, @RequestParam(value = "type", required = true) String type, HttpServletRequest request, @RequestParam(value = "columnId", required = true) String columnId, ModelMap model) {
		WebSite site = RequestUtils.getSite(request);
		String template_Path = StringUtils.EMPTY;
		ColumConfig columCurrent = null;
		List<ColumConfig> columAllList = null;
		Seo seo = null;
		// 当前栏目
		try {
			columCurrent = this.columconfigService.findColumconfigPoById(new PageData("ID", columnId));
			seo = this.seoService.selectSeoForObject(contentId);
			if (seo != null) {
				model.addAttribute(SEO_TITLE, seo.getSeoTitle());
				model.addAttribute(SEO_KEYWORDS, seo.getSeoKeywords());
				model.addAttribute(SEO_DESCRIPTION, seo.getSeoDescription());
			} else {
				//取整站SEO
				model.addAttribute(SEO_TITLE, site.getSiteName());
				model.addAttribute(SEO_KEYWORDS, site.getSiteKeyword());
				model.addAttribute(SEO_DESCRIPTION, site.getSiteDesc());
			}
		} catch (Exception e5) {
			e5.printStackTrace();
		}
		if (columCurrent == null) {
			throw new ChannelNotFoundException("the Channel[" + columnId + "]' does not exist!");
		}
		// 所有栏目
		try {
			columAllList = this.columconfigService.findAllList(new PageData("siteid", site.getId()));
		} catch (Exception e5) {
			e5.printStackTrace();
		}
		switch (type) {
			case Const.COLUM_TYPE_1:
				Content content = null;
				try {
					content = contentService.findTemplatePachById(contentId);
				} catch (Exception e4) {
					e4.printStackTrace();
				}
				if (content == null) {
					throw new ChannelNotFoundException("the Content[" + contentId + "]' does not exist!");
				}
				template_Path = content != null ? content.getContentTemplateId() : StringUtils.EMPTY;
				break;
			case Const.COLUM_TYPE_2:
				PageData pd2 = null;
				try {
					pd2 = messageService.findTemplatePachById(contentId);
				} catch (Exception e3) {
					e3.printStackTrace();
				}
				if (pd2 == null) {
					throw new ChannelNotFoundException("the Content[" + contentId + "]' does not exist!");
				}
				template_Path = pd2 != null ? pd2.getString("template_Path") : StringUtils.EMPTY;
				break;
			case Const.COLUM_TYPE_3:
				PageData pd3 = null;
				try {
					pd3 = productService.findTemplatePachById(contentId);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				if (pd3 == null) {
					throw new ChannelNotFoundException("the Content[" + contentId + "]' does not exist!");
				}
				template_Path = pd3 != null ? pd3.getString("template_Path") : StringUtils.EMPTY;
				break;
			case Const.COLUM_TYPE_4:
				PageData pd4 = null;
				try {
					pd4 = employService.findTemplatePachById(contentId);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if (pd4 == null) {
					throw new ChannelNotFoundException("the Content[" + contentId + "]' does not exist!");
				}
				template_Path = pd4 != null ? pd4.getString("template_Path") : StringUtils.EMPTY;
				break;
			case Const.COLUM_TYPE_5:
				PageData pd5 = null;
				try {
					pd5 = fileResourceService.findTemplatePachById(contentId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (pd5 == null) {
					throw new ChannelNotFoundException("the Content[" + contentId + "]' does not exist!");
				}
				template_Path = pd5 != null ? pd5.getString("template_Path") : StringUtils.EMPTY;
				break;
			default:
				break;
		}
		////////////////////////////////////////////////////////
		// 查询顶级栏目
		ColumConfig columConfigTop = null;
		List<ColumConfig> columAllListTop = new ArrayList<ColumConfig>();
		columAllListTop.addAll(columAllList);
		columConfigTop = this.searchTop(columCurrent, columAllListTop, columConfigTop);
		// 查询顶级栏目下所有的子栏目(树状结构)
		this.appendChild(columConfigTop, columAllList);
		// 对栏目排序
		if (CollectionUtils.isNotEmpty(columConfigTop.getSubConfigList())) {
			Collections.sort(columConfigTop.getSubConfigList(), new Comparator<ColumConfig>() {
				@Override
				public int compare(ColumConfig o1, ColumConfig o2) {
					int A = o1.getSort() == null ? Integer.MAX_VALUE : o1.getSort().intValue();
					int B = o2.getSort() == null ? Integer.MAX_VALUE : o2.getSort().intValue();
					return A - B;
				}
			});
		}
		try {
			this.setType(columConfigTop);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (CollectionUtils.isNotEmpty(columConfigTop.getSubConfigList())) {
			for (ColumConfig colum : columConfigTop.getSubConfigList()) {
				try {
					this.setType(colum);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if(StringUtils.isBlank(template_Path)){
			template_Path=columCurrent.getTemplate() != null ? columCurrent.getTemplate().getTemFilepath() : StringUtils.EMPTY;
		}

		//////////
		SelectTemplateBo selectTemplate = new SelectTemplateBo();
		selectTemplate.setId(contentId);
		selectTemplate.setTemPlatePath(template_Path);
		selectTemplate.setColumId(columnId);
		selectTemplate.setColumParentId(columCurrent.getParentid());
		selectTemplate.setColumName(columCurrent.getColumName());
		// 组装面包屑
		ColumConfig columConfig = new ColumConfig();
		columConfig.setId(selectTemplate.getColumId());
		columConfig.setParentid(selectTemplate.getColumParentId());
		ColumInfoBo columInfoBo = new ColumInfoBo();
		columInfoBo.setId(selectTemplate.getColumId());
		columInfoBo.setName(selectTemplate.getColumName());
		Node<ColumInfoBo> selectNode = new Node<ColumInfoBo>(columInfoBo);
		// 查询栏目的父级栏目，存储方式为链表
		this.searchParent(columConfig, selectNode, columAllList);
		LinkedList<Node<ColumInfoBo>> list = new LinkedList<>();
		reversalList(selectNode, list);
		// reversal(selectNode);
		// 实现链表反转
		model.addAttribute("crumb", list);
		model.addAttribute("contentId", contentId);
		model.addAttribute("template", selectTemplate);
		model.addAttribute(Const.PARAM_CURRENTID, columnId);
		model.addAttribute("columCurrent", columCurrent);
		model.addAttribute("currentColumnName", columCurrent.getColumName());
		model.addAttribute("columConfigTop", columConfigTop);
		model.addAttribute("purple_flag", "2");
		model.addAttribute("siteName", site.getSiteName());
		//model.addAttribute("typeId", typeId);
		if (typeId != null && !typeId.isEmpty()) {
			model.addAttribute("typeId", typeId);
		}
		if (selectTemplate == null || StringUtils.isEmpty(selectTemplate.getTemPlatePath())) {
			throw new PageNotFoundException("the template with Content type[" + type + "],ID[" + contentId + "] does not exits!");
		}
		// 统计浏览量
		RedisUtil.incr(DateUtil.fomatDate() + Const.STATIS_PM);
		return template_Path;
	}
	
	/**
	 * 导航寻址
	 * 
	 * @param request
	 * @param columnName
	 *            栏目或类型地址栏URL的名称
	 * @param contentId
	 *            内容地址栏URL的ID
	 * @return
	 */
	/**
	 * @param columnName
	 * @param contentId
	 * @param typeId
	 * @param type
	 * @param request
	 * @param columnId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/skippath/{columnName}/{contentId}", method = RequestMethod.GET)
	public String dynamicChannel(@PathVariable("columnName") String columnName,@PathVariable("contentId") String contentId, @RequestParam(value = "typeId", required = false) String typeId, @RequestParam(value = "type", required = false) String type, HttpServletRequest request, @RequestParam(value = "columnId", required = false) String columnId, ModelMap model) {
		PageData ColumUrlNamePage = new PageData();
		PageData ContentUrlNamePage = new PageData();
		WebSite site = RequestUtils.getSite(request);
		String template_Path = StringUtils.EMPTY;
		ColumConfig columCurrent = null;
		List<ColumConfig> columAllList = null;
		Seo seo = null;
		// 当前栏目
		try {
			ColumUrlNamePage = this.columconfigService.findColumUrlNameconfigById(columnName);
			if(!"".equals(ColumUrlNamePage) && ColumUrlNamePage != null){
				type = String.valueOf(ColumUrlNamePage.get("Url_TYPE"));
				typeId = (String) ColumUrlNamePage.get("Url_TYPEID");
				columnId = (String) ColumUrlNamePage.get("Url_COLUMNID");
				columCurrent = this.columconfigService.findColumconfigPoById(new PageData("ID", columnId));
			}
			String contentName = contentId.substring(contentId.lastIndexOf("_")+1);
			ColumUrlNamePage.put("contentName", contentName);
			ContentUrlNamePage = this.columconfigService.findContentUrlNameconfigById(ColumUrlNamePage);
			if(!"".equals(ContentUrlNamePage) && ContentUrlNamePage != null){
				contentId = (String) ContentUrlNamePage.get("Url_CONTENTID");
			}
			seo = this.seoService.selectSeoForObject(contentId);
			model.addAttribute(SEO_TITLE, seo.getSeoTitle());
			model.addAttribute(SEO_KEYWORDS, seo.getSeoKeywords());
			model.addAttribute(SEO_DESCRIPTION, seo.getSeoDescription());
		} catch (Exception e5) {
			e5.printStackTrace();
		}
		if (columCurrent == null) {
			throw new ChannelNotFoundException("the Channel[" + columnId + "]' does not exist!");
		}
		// 所有栏目
		try {
			columAllList = this.columconfigService.findAllList(new PageData("siteid", site.getId()));
		} catch (Exception e5) {
			e5.printStackTrace();
		}
		switch (type) {
			case Const.COLUM_TYPE_1:
				Content content = null;
				try {
					content = contentService.findTemplatePachById(contentId);
				} catch (Exception e4) {
					e4.printStackTrace();
				}
				if (content == null) {
					throw new ChannelNotFoundException("the Content[" + contentId + "]' does not exist!");
				}
				template_Path = content != null ? content.getContentTemplateId() : StringUtils.EMPTY;
				break;
			case Const.COLUM_TYPE_2:
				PageData pd2 = null;
				try {
					pd2 = messageService.findTemplatePachById(contentId);
				} catch (Exception e3) {
					e3.printStackTrace();
				}
				if (pd2 == null) {
					throw new ChannelNotFoundException("the Content[" + contentId + "]' does not exist!");
				}
				template_Path = pd2 != null ? pd2.getString("template_Path") : StringUtils.EMPTY;
				break;
			case Const.COLUM_TYPE_3:
				PageData pd3 = null;
				try {
					pd3 = productService.findTemplatePachById(contentId);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				if (pd3 == null) {
					throw new ChannelNotFoundException("the Content[" + contentId + "]' does not exist!");
				}
				template_Path = pd3 != null ? pd3.getString("template_Path") : StringUtils.EMPTY;
				break;
			case Const.COLUM_TYPE_4:
				PageData pd4 = null;
				try {
					pd4 = employService.findTemplatePachById(contentId);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				if (pd4 == null) {
					throw new ChannelNotFoundException("the Content[" + contentId + "]' does not exist!");
				}
				template_Path = pd4 != null ? pd4.getString("template_Path") : StringUtils.EMPTY;
				break;
			case Const.COLUM_TYPE_5:
				PageData pd5 = null;
				try {
					pd5 = fileResourceService.findTemplatePachById(contentId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (pd5 == null) {
					throw new ChannelNotFoundException("the Content[" + contentId + "]' does not exist!");
				}
				template_Path = pd5 != null ? pd5.getString("template_Path") : StringUtils.EMPTY;
				break;
			default:
				break;
		}
		////////////////////////////////////////////////////////
		// 查询顶级栏目
		ColumConfig columConfigTop = null;
		List<ColumConfig> columAllListTop = new ArrayList<ColumConfig>();
		columAllListTop.addAll(columAllList);
		columConfigTop = this.searchTop(columCurrent, columAllListTop, columConfigTop);
		// 查询顶级栏目下所有的子栏目(树状结构)
		this.appendChild(columConfigTop, columAllList);
		// 对栏目排序
		if (CollectionUtils.isNotEmpty(columConfigTop.getSubConfigList())) {
			Collections.sort(columConfigTop.getSubConfigList(), new Comparator<ColumConfig>() {
				@Override
				public int compare(ColumConfig o1, ColumConfig o2) {
					int A = o1.getSort() == null ? Integer.MAX_VALUE : o1.getSort().intValue();
					int B = o2.getSort() == null ? Integer.MAX_VALUE : o2.getSort().intValue();
					return A - B;
				}
			});
		}
		try {
			this.setType(columConfigTop);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (CollectionUtils.isNotEmpty(columConfigTop.getSubConfigList())) {
			for (ColumConfig colum : columConfigTop.getSubConfigList()) {
				try {
					this.setType(colum);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if(StringUtils.isBlank(template_Path)){
			template_Path=columCurrent.getTemplate() != null ? columCurrent.getTemplate().getTemFilepath() : StringUtils.EMPTY;
		}

		//////////
		SelectTemplateBo selectTemplate = new SelectTemplateBo();
		selectTemplate.setId(contentId);
		selectTemplate.setTemPlatePath(template_Path);
		selectTemplate.setColumId(columnId);
		selectTemplate.setColumParentId(columCurrent.getParentid());
		selectTemplate.setColumName(columCurrent.getColumName());
		// 组装面包屑
		ColumConfig columConfig = new ColumConfig();
		columConfig.setId(selectTemplate.getColumId());
		columConfig.setParentid(selectTemplate.getColumParentId());
		ColumInfoBo columInfoBo = new ColumInfoBo();
		columInfoBo.setId(selectTemplate.getColumId());
		columInfoBo.setName(selectTemplate.getColumName());
		Node<ColumInfoBo> selectNode = new Node<ColumInfoBo>(columInfoBo);
		// 查询栏目的父级栏目，存储方式为链表
		this.searchParent(columConfig, selectNode, columAllList);
		LinkedList<Node<ColumInfoBo>> list = new LinkedList<>();
		reversalList(selectNode, list);
		// reversal(selectNode);
		// 实现链表反转
		model.addAttribute("crumb", list);
		model.addAttribute("contentId", contentId);
		model.addAttribute("template", selectTemplate);
		model.addAttribute(Const.PARAM_CURRENTID, columnId);
		model.addAttribute("columCurrent", columCurrent);
		model.addAttribute("currentColumnName", columCurrent.getColumName());
		model.addAttribute("columConfigTop", columConfigTop);
		model.addAttribute("columUrlName",columnName);
		model.addAttribute("purple_flag", "2");
		model.addAttribute("siteName", site.getSiteName());
		if (typeId != null && !typeId.isEmpty()) {
			model.addAttribute("typeId", typeId);
		}
		if (selectTemplate == null || StringUtils.isEmpty(selectTemplate.getTemPlatePath())) {
			throw new PageNotFoundException("the template with Content type[" + type + "],ID[" + contentId + "] does not exits!");
		}
		// 统计浏览量
		RedisUtil.incr(DateUtil.fomatDate() + Const.STATIS_PM + type);
		return template_Path;
	}

	/**
	 * 网站地图寻址
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/webmap", method = RequestMethod.GET)
	public String webmap(HttpServletRequest request, ModelMap model) {
		List<ColumConfig> list = null;
		WebSite site = RequestUtils.getSite(request);
		try {
			PageData pd = new PageData();
			pd.put("siteid", site.getId());
			pd.put("COLUMGROUP_ID", GROUP_ID);
			list = columconfigService.findTopAndChildList(pd);
			this.setAllType(list);
		} catch (Exception e) {
			logger.error("find the colum ocurred error!", e);
		}
		model.addAttribute("toplist", list);
		return "front_hanneng/webmap/index";
	}

	public void setAllType(List<ColumConfig> list) throws Exception {
		if (CollectionUtils.isNotEmpty(list)) {
			for (ColumConfig colum : list) {
				this.setType(colum);
				this.setAllType(colum.getSubConfigList());
			}
		}
	}

	// 实现链表反转
	public Node<ColumInfoBo> reversal(Node<ColumInfoBo> current) {
		if (current == null || current.getPrev() == null) {
			return current;
		}
		Node<ColumInfoBo> reNode = reversal(current.getPrev());
		current.getPrev().setPrev(current);// 当前节点的prev反转指向当前节点
		current.setPrev(null);// 当前节点的prev重置为null
		return reNode;
	}

	public void reversalList(Node<ColumInfoBo> current, LinkedList<Node<ColumInfoBo>> list) {
		if (current == null) {
			return;
		}
		list.addFirst(current);
		reversalList(current.getPrev(), list);
		current.setPrev(null);
	}

	public void appenColumnList(ColumConfig current, LinkedList<ColumConfig> list, List<ColumConfig> allList) {
		if (current == null) {
			return;
		}
		for (ColumConfig columConfig : allList) {
			if (current.getId().equals(columConfig.getParentid())) {
				list.addLast(columConfig);
				appenColumnList(columConfig, list, allList);
			}
		}
	}

	// 查找顶级栏目
	public void searchParent(ColumConfig columConfig, Node<ColumInfoBo> node, List<ColumConfig> columAllList) {
		for (ColumConfig colum : columAllList) {
			if (columConfig.getParentid().equals(colum.getId())) {
				ColumInfoBo columInfoBo = new ColumInfoBo();
				columInfoBo.setId(colum.getId());
				columInfoBo.setName(colum.getColumName());
				Node<ColumInfoBo> prev = new Node<ColumInfoBo>(columInfoBo);
				node.setPrev(prev);
				this.searchParent(colum, prev, columAllList);
				break;
			}
		}
	}

	// 查找排序值高的模板（栏目或者分类）
	public SelectTemplateBo searchTemplate(ColumConfig colum) {
		SelectTemplateBo selectTemplateBo = null;
		String templatePath = colum.getTemplate() != null ? colum.getTemplate().getTemFilepath() : StringUtils.EMPTY;
		if (StringUtils.isNotEmpty(templatePath)) {
			selectTemplateBo = new SelectTemplateBo();
			selectTemplateBo.setId(colum.getId());
			selectTemplateBo.setTemPlatePath(templatePath);
			selectTemplateBo.setColumId(colum.getId());
			selectTemplateBo.setColumParentId(colum.getParentid());
			selectTemplateBo.setColumName(colum.getColumName());
			selectTemplateBo.setIfColumOrType(Const.YES);
			return selectTemplateBo;
		}
		// 在栏目中查找模板不为空的
		selectTemplateBo = this.searchColum(colum.getSubConfigList());
		if (selectTemplateBo != null) {
			return selectTemplateBo;
		}
		// 在分类中查找模板不为空的
		selectTemplateBo = this.searchType(colum.getTypeList());
		if (selectTemplateBo != null) {
			selectTemplateBo.setColumId(colum.getId());
			selectTemplateBo.setColumParentId(colum.getParentid());
			selectTemplateBo.setColumName(colum.getColumName());
		}
		return selectTemplateBo;
	}

	// 在栏目中查找模板不为空的
	public SelectTemplateBo searchColum(List<ColumConfig> list) {
		SelectTemplateBo bo = null;
		if (CollectionUtils.isNotEmpty(list)) {
			for (ColumConfig colum : list) {
				String templatePath = colum.getTemplate() != null ? colum.getTemplate().getTemFilepath() : StringUtils.EMPTY;
				if (StringUtils.isNotEmpty(templatePath)) {
					bo = new SelectTemplateBo();
					bo.setId(colum.getId());
					bo.setTemPlatePath(templatePath);
					bo.setColumId(colum.getId());
					bo.setColumParentId(colum.getParentid());
					bo.setColumName(colum.getColumName());
					bo.setIfColumOrType(Const.YES);
					break;
					//return bo;
				} else {
					if (CollectionUtils.isNotEmpty(colum.getTypeList())) {
						bo = this.searchType(colum.getTypeList());
						if (bo != null) {
							bo.setColumId(colum.getId());
							bo.setColumParentId(colum.getParentid());
							bo.setColumName(colum.getColumName());
							return bo;
						}
					}
					if (CollectionUtils.isNotEmpty(colum.getSubConfigList())) {
						return this.searchColum(colum.getSubConfigList());
					}
				}
			}
		}
		return bo;
	}

	// 在分类中查找模板不为空的
	public SelectTemplateBo searchType(List<TypeInfoBo> typeList) {
		SelectTemplateBo bo = null;
		if (CollectionUtils.isNotEmpty(typeList)) {
			for (TypeInfoBo type : typeList) {
				if (StringUtils.isNotEmpty(type.getTemPlatePath())) {
					bo = new SelectTemplateBo();
					bo.setId(type.getId());
					bo.setIfColumOrType(Const.NO);
					bo.setTemPlatePath(type.getTemPlatePath());
					break;
				} else if (CollectionUtils.isNotEmpty(type.getChildList())) {
					this.searchType(type.getChildList());
				}
			}
		}
		return bo;
	}

	// 查询栏目下所有的分类
	public void setType(ColumConfig colum) throws Exception {
		List<TypeInfoBo> resultTypeList = new ArrayList<TypeInfoBo>();
		switch (colum.getColumType()) {
			case Const.TEMPLATE_TYPE_2:// 资讯模板
				List<NewMessageType> messageTypeList = this.messageTypeService.findMessage_TypeByColumnIds(colum.getId());
				this.convertMesageTypeList(messageTypeList, resultTypeList, colum.getId());
				break;
			case Const.TEMPLATE_TYPE_3:// 产品模板
				List<Product_Type> productTypeList = this.productTypeService.findProduct_TypeByColumnIds(colum.getId());
				this.convertProductTypeList(productTypeList, resultTypeList, colum.getId());
				break;
			case Const.TEMPLATE_TYPE_5:// 下载模板
				List<FileType> fileTypeList = this.fileTypeService.findFileTypeByColumnIds(colum.getId());
				this.convertFileTypeList(fileTypeList, resultTypeList, colum.getId());
				break;
			default:
				break;
		}
		Collections.sort(resultTypeList, new Comparator<TypeInfoBo>() {
			@Override
			public int compare(TypeInfoBo o1, TypeInfoBo o2) {
				int A = o1.getSort() == null ? Integer.MAX_VALUE : o1.getSort().intValue();
				int B = o2.getSort() == null ? Integer.MAX_VALUE : o2.getSort().intValue();
				return A - B;
			}
		});
		colum.setTypeList(resultTypeList);
	}

	// 转换咨询分类
	public void convertMesageTypeList(List<NewMessageType> typeList, List<TypeInfoBo> resultList, String columId) {
		if (CollectionUtils.isEmpty(typeList)) {
			return;
		}
		for (NewMessageType newMessageType : typeList) {
			TypeInfoBo typeInfoBo = new TypeInfoBo();
			resultList.add(typeInfoBo);
			typeInfoBo.setId(newMessageType.getId());
			typeInfoBo.setName(newMessageType.getType_name());
			typeInfoBo.setTemPlatePath(newMessageType.getTemplate() != null ? newMessageType.getTemplate().getTemFilepath() : StringUtils.EMPTY);
			typeInfoBo.setSort(newMessageType.getSort());
			typeInfoBo.setType(Const.TEMPLATE_TYPE_2);
			typeInfoBo.setColumId(columId);
			typeInfoBo.setTypeUrlName(newMessageType.getTypeUrlName());
			if (CollectionUtils.isNotEmpty(newMessageType.getChildList())) {
				typeInfoBo.setChildList(new ArrayList<TypeInfoBo>());
				this.convertMesageTypeList(newMessageType.getChildList(), typeInfoBo.getChildList(), columId);
			}
		}
	}

	// 转换产品分类
	public void convertProductTypeList(List<Product_Type> typeList, List<TypeInfoBo> resultList, String columId) {
		if (CollectionUtils.isEmpty(typeList)) {
			return;
		}
		for (Product_Type type : typeList) {
			TypeInfoBo typeInfoBo = new TypeInfoBo();
			resultList.add(typeInfoBo);
			typeInfoBo.setId(type.getId());
			typeInfoBo.setName(type.getType_name());
			typeInfoBo.setSummary(type.getType_summary());
			typeInfoBo.setImageUrl(type.getImgurl());
			typeInfoBo.setType_status(type.getType_status());
			typeInfoBo.setTemPlatePath(type.getTemplate() != null ? type.getTemplate().getTemFilepath() : StringUtils.EMPTY);
			typeInfoBo.setSort(type.getSort());
			typeInfoBo.setType(Const.TEMPLATE_TYPE_3);
			typeInfoBo.setColumId(columId);
			typeInfoBo.setTypeUrlName(type.getTypeUrlName());
			if (CollectionUtils.isNotEmpty(type.getChildList())) {
				typeInfoBo.setChildList(new ArrayList<TypeInfoBo>());
				this.convertProductTypeList(type.getChildList(), typeInfoBo.getChildList(), columId);
			}
		}
	}

	// 转换下载分类
	public void convertFileTypeList(List<FileType> typeList, List<TypeInfoBo> resultList, String columId) {
		if (CollectionUtils.isEmpty(typeList)) {
			return;
		}
		for (FileType type : typeList) {
			TypeInfoBo typeInfoBo = new TypeInfoBo();
			resultList.add(typeInfoBo);
			typeInfoBo.setId(type.getDownload_id());
			typeInfoBo.setName(type.getDownload_name());
			typeInfoBo.setTemPlatePath(type.getTemplate() != null ? type.getTemplate().getTemFilepath() : StringUtils.EMPTY);
			typeInfoBo.setSort(type.getSort());
			typeInfoBo.setType(Const.TEMPLATE_TYPE_5);
			typeInfoBo.setColumId(columId);
			typeInfoBo.setImageUrl(type.getImgurl());
			typeInfoBo.setTypeUrlName(type.getTypeUrlName());
			if (CollectionUtils.isNotEmpty(type.getChildList())) {
				typeInfoBo.setChildList(new ArrayList<TypeInfoBo>());
				this.convertFileTypeList(type.getChildList(), typeInfoBo.getChildList(), columId);
			}
		}
	}

	// 查找顶级栏目
	//查找顶级栏目
	public ColumConfig searchTop(ColumConfig columCureent, List<ColumConfig> columAllList, ColumConfig columConfigTop) {
		if (Const.PARENT_FLAG.equals(columCureent.getParentid())) {
			return columCureent;
		}
		for (int i = 0; i < columAllList.size(); i++) {
			ColumConfig colum = columAllList.get(i);
			if (columCureent.getParentid().equals(colum.getId())) {
				columAllList.remove(i);
				columConfigTop = colum;
				columConfigTop = this.searchTop(colum, columAllList, columConfigTop);
				break;
			}
		}
		return columConfigTop;
	}

	// 查找子栏目
	private void appendChild(ColumConfig config, List<ColumConfig> allList) {
		for (int i = 0; i < allList.size(); i++) {
			ColumConfig po = allList.get(i);
			if (po.getParentid().equals(config.getId())) {
				if (CollectionUtils.isEmpty(config.getSubConfigList())) {
					config.setSubConfigList(new ArrayList<ColumConfig>());
				}
				config.getSubConfigList().add(po);
				//				allList.remove(i);
				//				i--;
				this.appendChild(po, allList);
			}
		}
	}

	/**
	 * Created on 2018-06-27
	 * <p>
	 * Discription:[selectFirstAndLowestColumn获取当前栏目的最低级且排序为第一个的栏目，如没有返回当前栏目]
	 * </p>
	 * 
	 * @param1 List<ColumConfig> columAllList 所有栏目
	 * @param2 List<ColumConfig> firstAndLowestColumn 当前栏目
	 * @return ColumConfig
	 * @author:zengxiangpeng
	 */
	private ColumConfig selectFirstAndLowestColumn(List<ColumConfig> columAllList, ColumConfig firstAndLowestColumn) {
		ArrayList<ColumConfig> arrayList = new ArrayList<ColumConfig>();
		for (ColumConfig zc : columAllList) {
			if (firstAndLowestColumn.getId().equals(zc.getParentid())) {
				arrayList.add(zc);
			}
		}
		if (CollectionUtils.isNotEmpty(arrayList)) {
			Collections.sort(arrayList, new Comparator<ColumConfig>() {
				@Override
				public int compare(ColumConfig o1, ColumConfig o2) {
					int A = o1.getSort() == null ? Integer.MAX_VALUE : o1.getSort().intValue();
					int B = o2.getSort() == null ? Integer.MAX_VALUE : o2.getSort().intValue();
					return A - B;
				}
			});
			firstAndLowestColumn = selectFirstAndLowestColumn(columAllList, arrayList.get(0));
		}
		return firstAndLowestColumn;
	}

}
