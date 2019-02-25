package cn.cebest.service.system.infineon;

import java.util.Map;

import cn.cebest.entity.Page;
import cn.cebest.util.PageData;

public interface KeyWordsService {

	Map<String, Object> getkeyWordsList(Page page);

	Map<String, Object> getKeyWords(PageData pd);

	void addKeyWords(PageData pd);

	void updateKeyWords(PageData pd);

	void deleteKeyWords(PageData pd);

}
