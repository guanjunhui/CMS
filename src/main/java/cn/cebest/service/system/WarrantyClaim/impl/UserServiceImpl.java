package cn.cebest.service.system.WarrantyClaim.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.service.system.WarrantyClaim.EmailService;
import cn.cebest.service.system.WarrantyClaim.UserService;
import cn.cebest.util.MD5;
import cn.cebest.util.PageData;

/**
 *
 * @author wangweijie
 * @Date 2018年7月12日
 * @company 中企高呈
 */
@Service
public class UserServiceImpl implements UserService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Autowired
	private EmailService emailService;
	@Override
	public int saveUser(PageData pd) throws Exception {
		//判断邮箱是否存在
		Integer count = (Integer)this.dao.findForObject("FrontUserMapper.queryByEmail", pd.get("email"));
		if(count == 0){
			String password = (String)pd.get("password");
			pd.put("password", MD5.md5(password));
			this.dao.save("FrontUserMapper.save", pd);
		}
		return count;
	}

	@Override
	public Object queryUser(PageData pd) throws Exception {
		String password = (String)pd.get("password");
		pd.put("password", MD5.md5(password));
		return this.dao.findForObject("FrontUserMapper.frontLogin", pd);
		
	}

	@Override
	public Integer sendEmail(String email,HttpServletRequest request) throws Exception {
		//判断邮箱是否存在
		Integer count = (Integer)this.dao.findForObject("FrontUserMapper.queryByEmail", email);
		if(count == 1){
			//发送邮件
			emailService.sendPassword(email, "",request);
		}
		return count;
	}

	@Override
	public void upatePassword(PageData pd) throws Exception {
		String password = (String)pd.get("password");
		pd.put("password", MD5.md5(password));
		this.dao.save("FrontUserMapper.updatePassword", pd);
	}

	@Override
	public List<PageData> ListPage(Page page) throws Exception {
		return (List<PageData>) dao.findForList("FrontUserMapper.listPage", page);
	}

	@Override
	public void delete(PageData pd)  throws Exception {
		dao.delete("FrontUserMapper.delete", pd);
		
	}

	@Override
	public void deletes(String[] ids)  throws Exception {
		dao.delete("FrontUserMapper.deletes", ids);
	}

	@Override
	public Object detailById(String id) throws Exception {
		return dao.findForObject("FrontUserMapper.detailById", id);
	}
}
