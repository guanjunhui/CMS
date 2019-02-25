package cn.cebest.controller.system.site;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Template;
import cn.cebest.entity.system.content.Content;
import cn.cebest.entity.system.content.ContentType;
import cn.cebest.entity.system.content.Content_Type_Colum;
import cn.cebest.entity.system.content.Contenttype_Column;
import cn.cebest.entity.web.WebSite;
import cn.cebest.portal.common.resolve.fetchshaoyin.PaseTool;
import cn.cebest.portal.common.resolve.fetchshaoyin.ReviewMainVO;
import cn.cebest.portal.common.resolve.fetchshaoyin.ReviewVO;
import cn.cebest.service.system.clonesite.CloneSiteService;
import cn.cebest.service.system.columconfig.ColumGroupService;
import cn.cebest.service.system.columconfig.ColumconfigService;
import cn.cebest.service.system.site.SiteService;
import cn.cebest.service.web.template.TemplateManager;
import cn.cebest.util.DateUtil;
import cn.cebest.util.PageData;

/**
 * 克隆多站点网站数据
 * @author lihaibo
 *
 */

@Controller
@RequestMapping(value = "/clone")
public class CloneSiteController extends BaseController {
	
	@Autowired
	private ColumGroupService columGroupService;
	@Resource(name="templateService")
	private TemplateManager templateService;
	@Resource(name = "columconfigService")
	private ColumconfigService columconfigService;
	@Resource(name = "siteService")
	private SiteService siteService;	
	@Resource(name = "cloneSiteService")
	private CloneSiteService cloneSiteService;
	
	
	//临时存储源网站栏目组ID与复制的新栏目组ID关系
	private Map<String,String> groupIDs	=new HashMap();
	//临时存储源网站模板ID与复制的新模板ID关系
	Map<String,String> templateIDs		=new HashMap();
	//临时存储源网站栏目ID与复制的新栏目ID关系
	Map<String,String> columnIDs		=new HashMap();
	//临时存储源网站 源内容分类与新分类的关系
	Map<String,String> typeID_from_to	=new HashMap();
	//临时存储源网站 源内容与新内容的关系
	Map<String,String> ContentID_from_to	=new HashMap();
	
	/**
	 * 删除已经复制过的内容等数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goDelContent")
	public ModelAndView goDelContent(HttpServletRequest request) throws Exception {
		String siteId_to = request.getParameter("siteTo");
		this.copySiteContentDeleteOld(siteId_to);
		return this.goSuperAdmin("SUPERDOTHIS");
	}
	
	/**
	 * 删除已经复制过的栏目等数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/goDelColumn")
	public ModelAndView goDelColumn(HttpServletRequest request) throws Exception {
		String siteId_to = request.getParameter("siteTo");
		this.copySiteDeleteOld(siteId_to, true);
		return this.goSuperAdmin("SUPERDOTHIS");
	}
	
	/**去新增站点复制整站栏目及模板
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("site:add")
	@RequestMapping(value="/goCopySite")
	public ModelAndView goCopySite(HttpServletRequest request) throws Exception {
		
		String siteId_from = request.getParameter("siteFrom");
		String siteId_to = request.getParameter("siteTo");
		String lanuagePrefix = request.getParameter("lanuagePrefix");
		String templatePrefix = request.getParameter("templatePrefix");
		String templateCatalogue = request.getParameter("templateCatalogue");
		String templateSuffix = request.getParameter("templateSuffix");
		String templateNewCatalogue = request.getParameter("templateNewCatalogue");
		String iscopydata = request.getParameter("iscopydata");
		if(siteId_from!=null&&!"".equals(siteId_from)&&siteId_to!=null&&!"".equals(siteId_to)&&!siteId_from.equals(siteId_to)){
			//拷贝源网站
			WebSite site_from = siteService.findSitePoById(siteId_from);
			//拷贝到网站
			WebSite site_to = siteService.findSitePoById(siteId_to);
			if(site_from!=null&&site_to!=null){
				this.copySiteDeleteOld(siteId_to,true);
				this.copySiteColumnAndTemplete(
						siteId_from,
						siteId_to,
						lanuagePrefix,
						templatePrefix,
						templateCatalogue,
						templateSuffix,
						templateNewCatalogue,
						true);
				if(iscopydata!=null&&"1".equals(iscopydata)){
					this.copySiteContentDeleteOld(siteId_to);
					this.copyContentDatas(siteId_from, siteId_to);
				}
				
			}
		}		
		return this.goSuperAdmin("SUPERDOTHIS");
	}
	
	@RequestMapping(value="/showContents")
	public ModelAndView showContents() throws Exception {
		String siteID = "001";
		List<ContentType> typeList = cloneSiteService.findContentTypeBySiteId(siteID);
		if(typeList!=null && typeList.size()>0){
			String[] typeIDS = new String[typeList.size()];
			int index=0;
			for(ContentType type:typeList){
				typeIDS[index]=type.getId();
				System.out.println("分类："+type.getId());
				index++;
			}			
			List<Contenttype_Column> typeColumnList = cloneSiteService.findContentTypeColumnByTypeIds(typeIDS);
			for(Contenttype_Column columtype:typeColumnList){
				System.out.println("栏目-分类："+columtype.getColumnId()+"   "+columtype.getContentTypeId());				
			}
		}
		
		List<Content> contentList = cloneSiteService.findContentBySiteId(siteID);
		if(contentList!=null && contentList.size()>0){
			String[] contentIDS = new String[contentList.size()];
			int index=0;
			for(Content content:contentList){
				contentIDS[index]=content.getId();
				System.out.println("内容："+content.getId());
				index++;
			}
			
			List<Content_Type_Colum> CTCList = cloneSiteService.findCTCByContentIds(contentIDS);
			for(Content_Type_Colum ctc:CTCList){
				System.out.println("内容-栏目-分类："+ctc.getColumnId()+"   "+ctc.getContentTypeId()+"  "+ctc.getContentId());				
			}
		}
		
		
		return null;
	}
	
	private void copyContentDatas(String siteId_from,String siteId_to){
		try{
			//源网站内容分类数据
			List<ContentType> typeList_from = cloneSiteService.findContentTypeBySiteId(siteId_from);
			List<ContentType> typeList_to	= new ArrayList();
			
			if(typeList_from!=null && typeList_from.size()>0){
				String[] typeIDS = new String[typeList_from.size()];
				int index=0;
				for(ContentType type:typeList_from){
					String type_new_ID=this.get32UUID();
					String type_templateID_from=type.getTemplateId();
					//源分类ID与新分类ID
					typeID_from_to.put(type.getId(), type_new_ID);
					typeIDS[index]=type.getId();
					index++;
					
					type.setId(type_new_ID);
					type.setSiteId(siteId_to);
					type.setCreateTime(new Date());
					type.setUpdateTime(null);
					type.setDeleteTime(null);					
					type.setTemplateId(templateIDs.get(type_templateID_from));
					typeList_to.add(type);
				}
				for(ContentType newtype:typeList_to){
					if(newtype.getpId()!=null&&!"0".equals(newtype.getpId())){
						String newPID=typeID_from_to.get(newtype.getpId());
						newtype.setpId(newPID);
					}
				}
				/**
				 * 执行批量插入内容分类数据
				 */
				cloneSiteService.insertBatchCType(typeList_to);
				
				
				//源网站内容分类与栏目关系
				List<Contenttype_Column> typeColumnList = cloneSiteService.findContentTypeColumnByTypeIds(typeIDS);
				List<Contenttype_Column> typeColumnList_new = new ArrayList();
				for(Contenttype_Column columtype:typeColumnList){
					String columnID_to 	= 	columnIDs.get(columtype.getColumnId());
					String typeID_to	=	typeID_from_to.get(columtype.getContentTypeId());
					columtype.setColumnId(columnID_to);
					columtype.setContentTypeId(typeID_to);
					typeColumnList_new.add(columtype);									
				}				
				/**
				 * 执行批量插入复制后的内容分类与栏目关系
				 */
				cloneSiteService.insertBatchTypeColumn(typeColumnList_new);
			}
			
			//源网站内容数据
			List<Content> contentList = cloneSiteService.findContentBySiteId(siteId_from);
			List<Content> contentList_new = new ArrayList();
			if(contentList!=null && contentList.size()>0){
				String[] contentIDS = new String[contentList.size()];
				int index=0;
				for(Content content:contentList){
					contentIDS[index]=content.getId();
					String content_new_ID=this.get32UUID();
					ContentID_from_to.put(content.getId(), content_new_ID);
					String content_new_templateID=templateIDs.get(content.getContentTemplateId());
					String content_new_typeID=typeID_from_to.get(content.getTypeId());
										
					content.setId(content_new_ID);
					content.setSiteId(siteId_to);
					content.setContentTemplateId(content_new_templateID);
					content.setCreatedTime(new Date().toString());
					content.setUpdateTime(null);
					content.setPv(0L);
					content.setShareCount(null);
					content.setReleaseTime(null);					
					content.setTypeId(content_new_typeID);
					index++;
					contentList_new.add(content);
				}
				/**
				 * 执行批量插入复制后的内容
				 */
				cloneSiteService.insertBatchContent(contentList_new);
				
				//源网站内容与栏目与分类的关系
				List<Content_Type_Colum> CTCList = cloneSiteService.findCTCByContentIds(contentIDS);
				List<Content_Type_Colum> CTCList_new = new ArrayList();
				for(Content_Type_Colum ctc:CTCList){
					String columnID_to 	= 	columnIDs.get(ctc.getColumnId());
					String typeID_to	=	typeID_from_to.get(ctc.getContentTypeId());
					String contentID_to	=	ContentID_from_to.get(ctc.getContentId());
					
					ctc.setColumnId(columnID_to);
					ctc.setContentId(contentID_to);
					ctc.setContentTypeId(typeID_to);
					
					CTCList_new.add(ctc);
				}
				cloneSiteService.insertBatchCTC(CTCList_new);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 复制整站内容，首先删掉已复制过的数据，让复制功能可以重复操作
	 * @param siteId_copydone
	 */
	private void copySiteContentDeleteOld(String siteId_copydone){
		
		try {
			List<ContentType> typeList = cloneSiteService.findContentTypeBySiteId(siteId_copydone);
			if(typeList!=null && typeList.size()>0){
				String[] typeIDS = new String[typeList.size()];
				int index=0;
				for(ContentType type:typeList){
					typeIDS[index]=type.getId();					
					index++;
				}	
				
				//删除栏目与分类关系,内容与栏目与分类关系
				if(typeIDS!=null&&typeIDS.length>0){					
					cloneSiteService.deleteBeforCopyCTC(typeIDS);
					//cloneSiteService.deleteBeforCopyCC(typeIDS);
				}
			}
			
			List<Content> contentList = cloneSiteService.findContentBySiteId(siteId_copydone);
			if(contentList!=null && contentList.size()>0){
				String[] contentIDS = new String[contentList.size()];
				int index=0;
				for(Content conent:contentList){
					contentIDS[index]=conent.getId();					
					index++;
				}
				//删除内容，分类，栏目
				cloneSiteService.deleteBeforCopyCC(contentIDS);
			}
			//删除内容数据
			cloneSiteService.deleteBeforCopyC(siteId_copydone);
			//删除分类数据
			cloneSiteService.deleteBeforCopyCT(siteId_copydone);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	
	}
	
	@RequiresPermissions("site:add")
	@RequestMapping(value="/goFetch")
	public ModelAndView goFetch() throws Exception {
		
		PaseTool.startThreadFetch(true, null);	
		
		return this.goSuperAdmin("SUPERDOTHIS");
	}
	
	@RequestMapping(value="/showViews")
	public ModelAndView showViews() throws Exception {
		
		Map<String,ReviewMainVO> map=PaseTool.getReviewFromJson();
		ReviewMainVO vo1=map.get("f101d369ecc4429db04e5c2ef49a71d2");
		 for (int i=0;i<vo1.getReviews().size();i++
	             ) {
	            ReviewVO rvo = vo1.getReviews().get(i);
	            System.out.println("vo1  作者："+rvo.getAuthor()+"， 日期："+rvo.getReviewDate()+"， 格式化日期:"+rvo.getReviewDateCh());

	        }
		ReviewMainVO vo2=PaseTool.sortReviewsList(vo1, true);
		 for (int i=0;i<vo2.getReviews().size();i++
	             ) {
	            ReviewVO rvo = vo2.getReviews().get(i);
	            System.out.println("vo2  作者："+rvo.getAuthor()+"， 日期："+rvo.getReviewDate()+"， 格式化日期:"+rvo.getReviewDateCh());

	        }
		
		return this.goSuperAdmin("SUPERDOTHIS");
	}
	
	//@RequiresPermissions("site:add")
	@RequestMapping(value="/goSuperAdmin")
	public ModelAndView goSuperAdmin(@RequestParam("SUPER")String SUPER) throws Exception {
		ModelAndView mv = this.getModelAndView();
		if(SUPER!=null&&"SUPERDOTHIS".equals(SUPER)){
			
			Page page=new Page();
			List<PageData> siteList = siteService.sitelistPage(page);//列出站点列表			
			mv.setViewName("system/site/siteadmin");
			mv.addObject("siteList", siteList);
			return mv;
		}else{
			return null;
		}
		
	}
	
	//如果执行过复制，再次执行，首先要删除已复制的栏目，模板，栏目组
	private void copySiteDeleteOld(String siteId_copydone,boolean flag){
		
		try {
			if(flag && siteId_copydone!=null && !"".equals(siteId_copydone)){
				columconfigService.deleteBeforCopy(siteId_copydone);
				templateService.deleteBeforCopy(siteId_copydone);
				columGroupService.deleteBeforCopy(siteId_copydone);				
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void copySiteColumnAndTemplete(
			String siteId_from,
			String siteId_to,
			String lanuage,
			String templatePrefix ,
			String templateCatalogue,
			String templateSuffix,
			String templateNewCatalogue,
			boolean flag){
		
		try {			
			//拷贝源网站
			WebSite site_from = siteService.findSitePoById(siteId_from);
			//拷贝到网站
			WebSite site_to = siteService.findSitePoById(siteId_to);
			
			PageData grouppd = new PageData();
			Page grouppage=new Page();
			
			grouppd.put("SITE_ID", site_to.getId());//站点id
			grouppage.setPd(grouppd);
			List<PageData> columGroup_to = columGroupService.gropulistPage(grouppage);
			if(columGroup_to==null||columGroup_to.size()<1){

				grouppd.put("SITE_ID", site_from.getId());//站点id
				grouppage.setPd(grouppd);
				//源网站栏目组
				List<PageData> columGroup_from = columGroupService.gropulistPage(grouppage);								
				//源网站模板
				List<Template> template_from = templateService.listAllForCopy(null);
				//源网站栏目			
				List<ColumConfig> column_from = columconfigService.listAllForCopy(siteId_from);
				
				//复制栏目组
				if(columGroup_from!=null && columGroup_from.size()>0){					
					for(PageData pPageData:columGroup_from){
						String groupID_from = pPageData.getString("ID");
						String groupID_to = this.get32UUID();//主键ID
						String groupName_from = pPageData.getString("COLUM_GROUPNAME");
						String groupName_to = lanuage + groupName_from;
						groupIDs.put(groupID_from, groupID_to);
						pPageData.put("ID", groupID_to);
						pPageData.put("SITE_ID",site_to.getId());
						pPageData.put("CREATETIME", DateUtil.DatePattern(new Date()));
						pPageData.put("COLUM_GROUPNAME",groupName_to);
						//保存新复制出来的栏目组
						if(flag){
							columGroupService.save(pPageData);//执行保存
						}
						
					}
				}
				
				//复制模板
				if(template_from!=null && template_from.size()>0){	
					List<Template> list = new ArrayList<Template>();										
					for(Template temp:template_from){						
						String newID = this.get32UUID();
						templateIDs.put(temp.getId(), newID);
						String temName_from = temp.getTemName();
						String temFile_from = temp.getTemFilepath();
						temp.setId(newID);
						temp.setTemName(lanuage+temName_from);
						temp.setTemFilepath(templatePrefix+temFile_from);
						temp.setCreatetime(new Date());
						temp.setTemImagepath(null);
						temp.setSiteID(site_to.getId());
						//需要实现程序拷贝模板
						list.add(temp);	
						String fileTemplate_from=templateCatalogue+temFile_from+templateSuffix;
						String fileTemplate_copy=templateNewCatalogue+templatePrefix+temFile_from+templateSuffix;
						if(flag){
							this.fileChannelCopy(fileTemplate_from, fileTemplate_copy);
						}
						
						
					}
					//保存新复制出来的模板
					if(flag){
						templateService.insertBatchTemplate(list);
					}
					
				}				
				
				//复制栏目
				if(column_from!=null && column_from.size()>0){
					List<ColumConfig> list = new ArrayList<ColumConfig>();
					for(ColumConfig column:column_from){
						String columnid_from=column.getId();
						String newID = this.get32UUID();
						columnIDs.put(columnid_from, newID);
						column.setId(newID);
						column.setUpdateTime(null);
						column.setCreatetime(new Date());
						column.setSiteid(site_to.getId());
						//按源网站逻辑替换复制栏目的栏目组ID
						String newGroupID = groupIDs.get(column.getColumGroupId());
						column.setColumGroupId(newGroupID);
						//按源网站逻辑替换复制栏目的详情模板ID
						String newTempDetailID = templateIDs.get(column.getColumndetailId());
						column.setColumndetailId(newTempDetailID);
						//按源网站逻辑替换复制栏目的模板ID
						String newtemplateID = templateIDs.get(column.getColumTemplatetid());
						column.setColumTemplatetid(newtemplateID);						
						column.setColumImage(null);
						list.add(column);
					}
					
					//替换parentID
					if(list!=null && list.size()>0){
						for(ColumConfig columns:list){
							if(columns.getParentid()!=null && !"0".equals(columns.getParentid())){
								String columnParentID=columns.getParentid();
								columns.setParentid(columnIDs.get(columnParentID));
							}							
						}						
						//保存新复制出来的栏目	
						if(flag){
							columconfigService.insertBatchColumn(list);	
							PageData sitePage = new PageData();
							String siteIndex_from = site_from.getSiteIndex();
							String siteIndex_to = columnIDs.get(siteIndex_from);
							sitePage.put("ID", siteIndex_to);
							sitePage.put("SITEID", site_to.getId());
							//更新新网站站点内的首页栏目ID;
							siteService.updateSiteIndexStatus(sitePage);
						}					
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void fileChannelCopy(String sourcePath, String toPath) {
		
        File sourcefile=new File(sourcePath);
        if(sourcefile!=null&&sourcefile.isFile()){
            File toFile=new File(toPath);
            if(toFile.exists()){
                new File(toPath).delete();
            }
            FileInputStream fi = null;
            FileOutputStream fo = null;
            FileChannel in = null;
            FileChannel out = null;
            try {
                fi = new FileInputStream(sourcefile);
                fo = new FileOutputStream(toFile);
                in = fi.getChannel();//得到对应的文件通道
                out = fo.getChannel();//得到对应的文件通道
                in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道                
            } catch (IOException e) {                
                e.printStackTrace();
            } finally {
                try {

                    fi.close();
                    in.close();
                    fo.close();
                    out.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
