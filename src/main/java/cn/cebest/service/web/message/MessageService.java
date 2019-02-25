package cn.cebest.service.web.message;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.util.PageData;

public interface MessageService {

	void auditMessage(PageData pd) throws Exception;

	void editMessage(PageData pd) throws Exception;

	PageData findMessageById(PageData pd) throws Exception;

	void saveMessage(PageData pd) throws Exception;

	void delAllMessage(String[] arrayMessage_IDS) throws Exception;

	void delMessage(PageData pd) throws Exception;

	List<PageData> messageListPage(Page page) throws Exception;






}
