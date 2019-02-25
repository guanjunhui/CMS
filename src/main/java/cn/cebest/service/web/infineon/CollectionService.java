package cn.cebest.service.web.infineon;

import cn.cebest.entity.Page;
import cn.cebest.entity.system.infineon.Collection;
import cn.cebest.util.JsonResult;

public interface CollectionService {

	JsonResult listByUserId(Page page);

	JsonResult addCollection(Collection collection);

	JsonResult getColl(Collection collection);

	JsonResult delColl(String id);

}
