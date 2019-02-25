package cn.cebest.directive;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.cebest.entity.system.content.Content;
import cn.cebest.service.system.content.content.ContentService;
import cn.cebest.service.system.txt.TxtService;
import cn.cebest.util.Const;
import cn.cebest.util.Logger;
import cn.cebest.util.ParamsUtils;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 关于我们标签
 */
@Component("about_content")
public class AboutUsContentDirective implements TemplateDirectiveModel {
	protected Logger logger = Logger.getLogger(this.getClass());
	private static final String ISLIST="isList";

	/**
	 * 参数名称
	 */
	@Autowired
	private ContentService contentService;
	@Autowired
	private TxtService txtService;
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String columId=ParamsUtils.getString(Const.PARAM_CURRENTID, params);
		String isList=ParamsUtils.getString(ISLIST, params);
		String contentTxt=StringUtils.EMPTY;
        Map<String, String> retMap = new HashMap<>();
        List<Content>  contentList = null;
		try {
        	contentList= contentService.findContentsByColumnId(columId);
        	Content content=null;
        	if(CollectionUtils.isNotEmpty(contentList)){
        		if(Const.YES.equals(isList)){
        			//大事记
//                	for (Content con : contentList) {
//                		//大事记的时间即为标题
//                		String createdTime = con.getContentTitle().replace("-", ".");
//                		con.setCreatedTime(createdTime);
//					}
            		env.setVariable(Const.OUT_LIST, getBeansWrapper().wrap(contentList));
                }else{
	        		content=contentList.get(Const.INT_0);
//	        		PageData txtData=txtService.findById(new PageData("CONTENT_ID", content.getId()));
//	    			contentTxt=txtData!=null?txtData.getString("TXT"):StringUtils.EMPTY;
//	        		retMap.put("TXT", contentTxt);
	        		retMap.put("Title", content.getContentTitle());
	        		retMap.put("TXT", content.getContentTxt());
	        		retMap.put("imgTitle", content.getImgTitle());
	        		retMap.put("imgDesc", content.getImgDesc());
	        		String contentImageURL = content.getImgURL();
	        		retMap.put("imageUrl", contentImageURL);
	        		env.setVariable(Const.OUT_BEAN, getBeansWrapper().wrap(retMap));
                }
        	}
		} catch (Exception e) {
			logger.error("find the content txt ocurred error!",e);
		}
        if (body != null) {
            body.render(env.getOut());
        }  
	}
	
    public static BeansWrapper getBeansWrapper(){
        BeansWrapper beansWrapper =
                         new BeansWrapperBuilder(Configuration.VERSION_2_3_0).build();
        return beansWrapper;
    }
}
