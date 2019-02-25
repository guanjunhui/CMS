package cn.cebest.controller.system.ueditor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baidu.ueditor.ActionEnter;

import cn.cebest.controller.base.BaseController;
import cn.cebest.util.RequestUtils;

/**
 * @类名称：Ueditor辅助类
 * @创建人：qichangxin
 * @创建时间：2014年11月17日 
 * @修改：gp
 */
@Controller
@RequestMapping(value = "/ueditor")
public class UeditorController extends BaseController {

	/**
	 * 百度富文本编辑器：图片上传
	 * 
	 * @param page
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/upload")
	public void upload(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("utf-8");
		response.setHeader("Content-Type", "text/html");
		String rootPath = RequestUtils.getRealPath(request, "/");
		PrintWriter out = response.getWriter();
		out.write(new ActionEnter(request, rootPath).exec());
	}

}
