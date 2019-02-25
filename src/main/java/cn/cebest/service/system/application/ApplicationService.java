package cn.cebest.service.system.application;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.application.Application;
import cn.cebest.util.PageData;

public interface ApplicationService {
	
	List<Application> contentlistPage(Page page) throws Exception;

	Application findContentById(PageData pd) throws Exception;

	void editContent(Application content) throws Exception;

	void saveContent(Application content) throws Exception;

	List<PageData> listAllApplication(PageData pd) throws Exception;
}
