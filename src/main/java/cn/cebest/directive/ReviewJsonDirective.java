package cn.cebest.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.content.Content;
import cn.cebest.entity.web.WebSite;
import cn.cebest.portal.common.resolve.fetchshaoyin.PaseTool;
import cn.cebest.portal.common.resolve.fetchshaoyin.ReviewMainVO;
import cn.cebest.util.Const;
import cn.cebest.util.FrontUtils;
import cn.cebest.util.Logger;
import cn.cebest.util.PageData;
import cn.cebest.util.ParamsUtils;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("review_list")
public class ReviewJsonDirective implements TemplateDirectiveModel {

	protected Logger logger = Logger.getLogger(this.getClass());
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		//得到当前站点
		WebSite site = FrontUtils.getSite(env);
		//获取当前站点id
		String siteId = site.getSiteId();
		//获取参数
		String currentId = ParamsUtils.getString(Const.PARAM_CURRENTID, params);
		 
		ReviewMainVO reviewMainVO = null;
		ReviewMainVO reviewMainVO1 = null;
		try {
 			Map<String, ReviewMainVO> reviewFromJson = PaseTool.getReviewFromJson();
 			//以产品的id作为文件名 通过id查找对应的品论
 			reviewMainVO = reviewFromJson.get(currentId);
 			
 			//按评论时间获取平路数
 			/*reviewMainVO1=PaseTool.sortReviewsList(reviewMainVO, true);*/
		} catch (Exception e) {
			logger.error("find the colum ocurred error!",e);
		}
		//设置输出变量
        env.setVariable(Const.OUT_BEAN, getBeansWrapper().wrap(reviewMainVO));
        /*env.setVariable("SortReview", getBeansWrapper().wrap(reviewMainVO1));*/
        if (body != null) {
        	//输出变量
            body.render(env.getOut());
        }  
	}
	
    public static BeansWrapper getBeansWrapper(){
        BeansWrapper beansWrapper =
                         new BeansWrapperBuilder(Configuration.VERSION_2_3_0).build();
        return beansWrapper;
    }
}
