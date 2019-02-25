package cn.cebest.service.system.content.collection;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.content.Content;
import cn.cebest.util.PageData;

public interface ContentCollectionService {

	void contentCollection(PageData pd) throws Exception;

	List<Content> contentCollectionlistpage(Page page) throws Exception;
	
}
