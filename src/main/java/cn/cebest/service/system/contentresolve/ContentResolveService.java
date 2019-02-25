package cn.cebest.service.system.contentresolve;

import java.util.List;
import java.util.Map;

import cn.cebest.entity.bo.ContentInfoBo;

public interface ContentResolveService {

	List<ContentInfoBo> findTopContentList() throws Exception;
	List<ContentInfoBo> findRecommendContentList(Map<String, Object> map) throws Exception;
}
