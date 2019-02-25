package cn.cebest.service.system.WarrantyClaim.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.service.system.WarrantyClaim.CountryService;

/**
 *
 * @author wangweijie
 * @Date 2018年7月17日
 * @company 中企高呈
 */
@Service
public class CountryServiceImpl implements CountryService{
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Override
	public Object getAllCountrys() throws Exception {
		return this.dao.findForList("CountryMapper.getAllCountrys",null);
	}

	@Override
	public String getCountryCode(String nameEn) throws Exception {
		return this.dao.findForObject("CountryMapper.getCountryCode", nameEn).toString();
	}
	
}
