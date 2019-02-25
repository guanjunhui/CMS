package cn.cebest.service.system.content.contentImage;

import java.util.List;

import cn.cebest.entity.Page;
import cn.cebest.util.PageData;

public interface ContentImageService {

	List<PageData> contentImageList(Page page) throws Exception;

	void save(PageData pd) throws Exception;

	void delete(PageData pd) throws Exception;

}
