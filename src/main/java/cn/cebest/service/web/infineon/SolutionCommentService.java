package cn.cebest.service.web.infineon;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.infineon.SolutionCommentVO;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;

public interface SolutionCommentService {

	JsonResult synchComment(SolutionCommentVO solution);

	JsonResult getCommentList(Page page);

	JsonResult likeComment(PageData pd);

	JsonResult cancelLike(PageData pd);

	JsonResult getAllUser(PageData pd);

}
