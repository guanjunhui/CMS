package cn.cebest.service.system.WarrantyClaim.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.Page;
import cn.cebest.entity.web.ProductRegistration;
import cn.cebest.service.system.WarrantyClaim.CountryService;
import cn.cebest.service.system.WarrantyClaim.ProductRegistrationService;
import cn.cebest.util.CodeMap;
import cn.cebest.util.Const;
import cn.cebest.util.DateUtil;
import cn.cebest.util.PageData;
import cn.cebest.util.PathUtil;
import cn.cebest.util.SystemConfig;

/**
 *
 * @author wangweijie
 * @Date 2018年7月12日
 * @company 中企高呈
 */
@Service
public class ProductRegistrationServiceImpl implements ProductRegistrationService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Autowired
	private CountryService countryService;
	
	@Override
	public void saveProductRegistration(ProductRegistration pd, MultipartFile file) throws Exception{
		if(file != null){
			String ffile = DateUtil.getDays();
			String filePath = SystemConfig.getPropertiesString("filepath.img", PathUtil.getUploadPath() + Const.FILEPATHIMG) + ffile; // 文件上传路径
			String[] strs = file.getOriginalFilename().split("\\.");
			// 组装文件名
			String fileUrl = new Date().getTime() + "." + strs[strs.length - 1];
			File targetFile = new File(filePath, fileUrl);
			// 判断文件是否存在
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			//保存文件
			file.transferTo(targetFile);
			pd.setFileUrl(ffile+"/"+fileUrl);
			pd.setFileName(file.getOriginalFilename());
			
			//组装注册码
			String country = pd.getCountry();
			//国家编号
			String registCode = this.countryService.getCountryCode(country);
			//产品编号
			CodeMap codeMap = new CodeMap();
			registCode += codeMap.getMap().get(pd.getProduct());
			/*if(pd.getProduct().contains("AIR")){
				registCode += "A";
			}else if(pd.getProduct().contains("SPORT")){
				registCode += "C";
			}else{
				registCode += "B";
			}*/
			
			//当前年月
			Date currentTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String currentTimeStr = formatter.format(currentTime);
			String[] arr = currentTimeStr.split("-");
			String year = arr[0].substring(2);
	        String month = arr[1];  
	        String day = arr[2]; 
	        registCode = registCode + year + month + day;
	        //获取当前数据库产品最大编号
	        Object obj = this.dao.findForObject("ProductRegistrationMapper.getMaxNo", null);
	        String maxNo = null;
	        if(obj == null){
	        	maxNo = "1";
	        }else{
	        	maxNo = (Integer.valueOf(obj.toString())+1) + "";
	        }
	        if(Integer.valueOf(maxNo) < 10){
	        	maxNo = "00" + maxNo;
	        }else if(10 <= Integer.valueOf(maxNo) && Integer.valueOf(maxNo) < 100){
	        	maxNo = "0" + maxNo;
	        }
	        registCode = registCode + maxNo;
	        pd.setRegistCode(registCode);
	        pd.setNo(Integer.valueOf(maxNo));
	        //保存
			this.dao.save("ProductRegistrationMapper.save", pd);
			
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> ListPage(@SuppressWarnings("rawtypes") Page page)  throws Exception{
		return (List<PageData>) dao.findForList("ProductRegistrationMapper.listPage", page);
	}

	@Override
	public Integer checkSerialCode(String serialCode) throws Exception {
		return (Integer)this.dao.findForObject("ProductRegistrationMapper.getCountBySerialCode", serialCode);
	}

	@Override
	public void updataRecommend(PageData pd) throws Exception {
		this.dao.update("ProductRegistrationMapper.updataRecommend", pd);
	}

	@Override
	public Object detailById(String id) throws Exception {
		return dao.findForObject("ProductRegistrationMapper.detailById", id);
	}

	@Override
	public int selectByEmailAndRegistionCode(Map<String, String> pMap)throws Exception {
		return (int)dao.findForObject("ProductRegistrationMapper.selectByEmailAndRegistionCode", pMap);
	}
	
	
}
