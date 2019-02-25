package cn.cebest.service.web.message.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.service.web.message.MessageService;
import cn.cebest.util.PageData;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> messageListPage(Page page) throws Exception {
		return (List<PageData>) dao.findForList("MessageMapper.messageListMapper", page);
	}

	@Override
	public void delMessage(PageData pd) throws Exception {
		dao.update("MessageMapper.updateMessage", pd);
	}

	@Override
	public void delAllMessage(String[] arrayMessage_IDS) throws Exception {
		dao.update("MessageMapper.updateAllMessage", arrayMessage_IDS);
	}

	@Override
	public void saveMessage(PageData pd) throws Exception {
		dao.save("MessageMapper.saveMessage", pd);
	}

	@Override
	public PageData findMessageById(PageData pd) throws Exception {
		return (PageData)dao.findForObject("MessageMapper.findMessageById", pd);
	}

	@Override
	public void editMessage(PageData pd) throws Exception {
		dao.update("MessageMapper.editMessage", pd);
	}

	@Override
	public void auditMessage(PageData pd) throws Exception {
		dao.update("MessageMapper.auditMessage", pd);
	}
}
