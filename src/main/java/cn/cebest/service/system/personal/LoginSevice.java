package cn.cebest.service.system.personal;

import cn.cebest.entity.system.Phone;
import cn.cebest.util.PageData;

public interface LoginSevice {

	Phone login(PageData pd) throws Exception;

	Phone findPersonageByPhone(PageData pd) throws Exception;

	Boolean save(Phone phone) throws Exception;

	Boolean updatePassword(PageData pd) throws Exception;

}
