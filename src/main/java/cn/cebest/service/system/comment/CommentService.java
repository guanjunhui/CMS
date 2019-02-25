package cn.cebest.service.system.comment;

import java.util.List;
import java.util.Map;

import cn.cebest.entity.Page;
import cn.cebest.util.PageData;

public interface CommentService {

	String reSendMail(PageData pageData) throws Exception;
	
	void sendRelationMail() throws Exception;
	
	void deleteRelationByIds(PageData pd) throws Exception;
	
	void insertCommentByUserId(List<Map<String, Object>> pd) throws Exception;
	
	List<PageData> findNoReCommentListByUserId(PageData pd) throws Exception;
	
	List<PageData> findCommentListByUserId(PageData pd) throws Exception;
	
	List<PageData> listcommentlistPage(Page page) throws Exception;
	
	List<PageData> commentManagerlistPage(Page page) throws Exception;
	
	List<PageData> commentReplylistpage(Page page) throws Exception;

	void saveComment(PageData pd) throws Exception;
	
	void saveCommentReply(PageData pd) throws Exception;

	void updateCommentReply(PageData pd) throws Exception;

	void delComment(PageData pd) throws Exception;

	PageData findCommentById(PageData pd) throws Exception;

	void auditComment(PageData pd) throws Exception;

	void deleteAllComment(String[] arrayCOMMENT_IDS) throws Exception;

}
