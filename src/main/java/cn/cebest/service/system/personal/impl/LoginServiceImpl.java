package cn.cebest.service.system.personal.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.system.Phone;
import cn.cebest.service.system.personal.LoginSevice;
import cn.cebest.util.PageData;
@Service("loginSevice")
public class LoginServiceImpl implements LoginSevice{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	
	@Override
	public Phone login(PageData pd) throws Exception {
		return (Phone) dao.findForObject("PhoneMapper.login", pd);
	}


	@Override
	public Phone findPersonageByPhone(PageData pd) throws Exception {
		
		return (Phone) dao.findForObject("PhoneMapper.findPersonageByPhone", pd);
	}


	@Override
	public Boolean save(Phone phone) throws Exception {
		
		 dao.save("PhoneMapper.save", phone);
		return true;
	}


	@Override
	public Boolean updatePassword(PageData pd) throws Exception {
		dao.update("PhoneMapper.updatePassword", pd);
		return true;
	}

}
