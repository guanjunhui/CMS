package cn.cebest.controller.front.cms;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jcraft.jsch.Logger;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.download.FileResources;
import cn.cebest.service.system.download.FileResourceService;
import cn.cebest.util.Const;
import cn.cebest.util.FileDownload;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.SystemConfig;
@Controller
public class DownloadController {
	@Resource(name = "fileResourceService")
	private FileResourceService fileResourceService;
	
	@RequestMapping(value = "DownloadFile")
	public void DownloadFiles(HttpServletResponse response, String filePath,String fileName,HttpServletRequest request) throws UnsupportedEncodingException{
		fileName = new String(fileName.getBytes("iso-8859-1"),"UTF-8");
		try {
			FileDownload.downloadFile(response, SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + filePath, fileName,request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "DownloadFileById")
	public void DownloadFileById(HttpServletResponse response,Integer id,HttpServletRequest request) throws Exception {
		cn.cebest.entity.system.download.DownloadFiles file=null;
			file = fileResourceService.findDownloadFileById(id);
			//FileDownload.downloadFile(response, SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + file.getFilepach(), file.getName(),request);
			FileDownload.downloadBigFile(response,SystemConfig.getPropertiesString("filepath.file", PathUtil.getUploadPath() + Const.FILEPATHFILE) + file.getFilepach(), request);
			fileResourceService.updateDownlosd_count(file.getFileid());
	}
	
	/**
	 * 通过栏目columId去查询下载列表
	 * Json异步加载(分页)
	 * @param id 类型ID
	 * @param columId 栏目ID
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getDownloadList")
	@ResponseBody
	public Object getDownloadList(Integer currentPage,Integer showCount,String columId,String typeId) throws Exception{
		Integer Page = currentPage;
		Integer Count = showCount;
		Page page=new Page(Page,Count);
		PageData pd = new PageData();
		pd.put("columId", columId);
		pd.put("typeId", typeId);
		page.setPd(pd);
		Map<String, Object> messageMap = new HashMap<String, Object>();
		List<FileResources> expertSaylistPageFile = null;
		try {
			expertSaylistPageFile = fileResourceService.findlistPageFile(page);
			messageMap.put("list", expertSaylistPageFile);
			messageMap.put("page", page);
		} catch (Exception e) {
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK",messageMap);
	}
}
