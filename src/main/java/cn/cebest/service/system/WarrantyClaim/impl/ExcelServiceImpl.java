package cn.cebest.service.system.WarrantyClaim.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import cn.cebest.dao.DaoSupport;
import cn.cebest.service.system.WarrantyClaim.ExcelService;

/**
 *
 * @author wangweijie
 * @Date 2018年7月13日
 * @company 中企高呈
 */
@Service
public class ExcelServiceImpl implements ExcelService {
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	//用于手机错误编号的list集合
	private List<String> rList = new ArrayList<>();
	@Override
	public List<String> read(InputStream inputStream) throws Exception {
		rList.clear();
		
		 //构建Workbook对象
		Workbook workbook = WorkbookFactory.create(inputStream);
		//Sheet的下标是从0开始
       //获取第一张Sheet表
       Sheet sheet = workbook.getSheetAt(0);
       //Sheet的行是从0开始
       //获取需要读取内容的开始行标
       int dataRow = 0;
       //获取需要读取内容的结束行标
       int endRow = sheet.getLastRowNum() +1;

       if(endRow >= 1){
       	Set<String> pSet = new HashSet<String>();
       	for (int i = dataRow; i < endRow; i++) {
       		  //获取需要读取内容的行
               Row row = sheet.getRow(i);
	        	//Sheet的列是从0开始
	            //遍历并获取需要读取的单元格
	        	Cell cell = row.getCell(0);
	        	String value = cell.getStringCellValue();
	        	pSet.add(value);
               if(pSet.size() == 1000){
            	   inserts(pSet);
            	   pSet.clear();
               }
       	}
       	if(pSet.size() > 0){
       		inserts(pSet);
       	}
       }
       inputStream.close();
       return rList;
	}
	
	/**
	 * 递归处理插入的重复错误数据
	 * @param pSet
	 * @throws Exception
	 */
	public void inserts(Set<String> pSet) throws Exception{
		try {
			this.dao.save("SerialCodeMapper.inserts", new ArrayList<>(pSet));
		} catch (Exception e) {
			String errorCode = getErrorCode(e);
			rList.add(errorCode);
			pSet.remove(errorCode);
			if(pSet.size()>0){
				inserts(pSet);
			}
		}
	}
	
	/**
	 * 获取错误数据编码
	 * @param e
	 * @return
	 */
	public String getErrorCode(Exception e){
		String msg = e.getMessage();
		String errCode = msg.split("entry")[1].split("for")[0].replaceAll("'", "").trim();
		return errCode;
	}

	@Override
	public void saveImportExcelMsg(Map<String, String> pMap) throws Exception {
		this.dao.save("SerialCodeMapper.insertImportExcelMsg", pMap);
	}

	@Override
	public Object getImportExcelMsg() throws Exception {
		return this.dao.findForObject("SerialCodeMapper.getImportExcelMsg", null);
	}
	
	
}
