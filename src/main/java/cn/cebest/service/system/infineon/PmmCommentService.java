package cn.cebest.service.system.infineon;

import java.util.Map;

import cn.cebest.entity.Page;
import cn.cebest.util.PageData;

public interface PmmCommentService {

	Map<String, Object> getCommentList(Page page);

	Map<String, Object> getContentList(Page page);

	Map<String, Object> getCommentById(PageData pd);

	void deleteComment(PageData pd);

}
