package cn.cebest.controller.member;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.cebest.controller.base.BaseController;
import cn.cebest.service.system.appuser.AppUserExtManager;
import cn.cebest.service.system.appuser.AppuserManager;
import cn.cebest.service.web.image.ImageManager;
import cn.cebest.util.AppUtil;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.FileUpload;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.RequestUtils;
import cn.cebest.util.SystemConfig;
import cn.cebest.util.Tools;
import cn.cebest.util.Watermark;


/**@author qichangxin
  * 会员管理
 */
@Controller
@RequestMapping(value="/member")
public class MemberManageController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(MemberManageController.class);

	@Autowired
	private AppuserManager appuserService;

	@Autowired
	private AppUserExtManager appUserExtManager;
	
/*	@Autowired
	private PicturesManager picturesManager;*/
	
	@Autowired
	private ImageManager imageService;
	/**保存用户
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public JsonResult edit() throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("USER_ID", this.get32UUID());	//ID
		pd.put("RIGHTS", "");					
		pd.put("LAST_LOGIN", "");				//最后登录时间
		pd.put("IP", RequestUtils.getRemoteAddr(this.getRequest()));//IP
		String salt=pd.getString("USERNAME")+new SecureRandomNumberGenerator().nextBytes().toHex();
		pd.put("SALT", salt);	
		pd.put("PASSWORD", new SimpleHash("MD5",  pd.getString("PASSWORD"),salt,2).toString());	//密码加密
		if(null == appuserService.findByUsername(pd)){
			appuserService.saveU(pd);
			return jsonResult(Const.HTTP_OK, "ok");
		}else{
			return jsonResult(Const.HTTP_ERROR,"failed");
		}
	}
	
	/**修改用户
	 * @param out
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public void editU() throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		
		PageData pd_ext=(PageData) pd.clone();
		
		appuserService.editU(pd);
		appUserExtManager.saveU(pd_ext);
	}

	/**新增
	 * @param file USER_ID:会员ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveimage")
	@ResponseBody
	public JsonResult save(
			@RequestParam(required=false) MultipartFile file
			) {
		String  ffile = DateUtil.getDays(), fileName = "";
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData pdImage = (PageData) pd.clone();
		if (null != file && !file.isEmpty()) {
			String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile;		//文件上传路径
			fileName = FileUpload.fileUp(file, filePath, this.get32UUID());				//执行上传
		}else{
			System.out.println("上传失败");
		}
		String picId=this.get32UUID();
		pd.put("IMAGE_ID", picId);		//主键
		pd.put("TITLE", "用户图片");	
		pd.put("TYPE", 6);//标题
		pd.put("NAME", fileName);							//文件名
		pd.put("IMGURL", ffile + "/" + fileName);				//路径
		pd.put("CREATE_TIME", Tools.date2Str(new Date()));	//创建时间
		pd.put("MASTER_ID", pd.getString("id"));			//附属与
		pd.put("BZ", "用户图片");						//备注
		Watermark.setWatemark(SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile + "/" + fileName);//加水印
		pdImage.put("MEM_IMG_PATH", picId);
		try {
			imageService.save(pd);
			appUserExtManager.saveImage(pd);
		} catch (Exception e) {
			logger.error("hapeened error in save member image!", e);
			return jsonResult(Const.HTTP_ERROR,e.getMessage());
		}
		return jsonResult(Const.HTTP_OK, "ok");
	}

	/**修改密码
	 * @param out
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/changePwd")
	public void changePwd() throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		String salt=pd.getString("USERNAME")+new SecureRandomNumberGenerator().nextBytes().toHex();
		pd.put("SALT", salt);	
		pd.put("PASSWORD", new SimpleHash("MD5",  pd.getString("PASSWORD"),salt,2).toString());	//密码加密
		appuserService.editU(pd);
	}

	
	/**判断用户名是否存在
	 * @return
	 */
	@RequestMapping(value="/hasU")
	@ResponseBody
	public Object hasU(){
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			if(appuserService.findByUsername(pd) != null){
				errInfo = "error";
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
	}
	
	/**判断邮箱是否存在
	 * @return
	 */
	@RequestMapping(value="/hasE")
	@ResponseBody
	public Object hasE(){
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			if(appuserService.findByEmail(pd) != null){
				errInfo = "error";
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		map.put("result", errInfo);				//返回结果
		return AppUtil.returnObject(new PageData(), map);
	}


}
	
 