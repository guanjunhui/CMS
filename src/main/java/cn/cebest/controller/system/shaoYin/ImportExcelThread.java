package cn.cebest.controller.system.shaoYin;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import cn.cebest.service.system.WarrantyClaim.ExcelService;

/**
 *
 * @author wangweijie
 * @Date 2018年8月3日
 * @company 中企高呈
 */
@Component
public class ImportExcelThread extends Thread{
	private ExcelService excelServcie;
	private InputStream inputStream;
	
	
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setExcelServcie(ExcelService excelServcie) {
		this.excelServcie = excelServcie;
	}

	@Override
	public void run() {
		try {
			long startTime=System.currentTimeMillis();   //获取开始时间
			List<String> rList = this.excelServcie.read(inputStream);
			
			String msg = "上次数据全部插入成功";
			String code = "200";
			if(rList!=null && rList.size() > 0){
				msg = "上次导入出错的数据为："+rList.toString()+",其余数据全部导入成功";
				code = "201";
			}
			Map<String, String> pMap = new HashMap<>();
			pMap.put("msg", msg);
			pMap.put("code", code);
			this.excelServcie.saveImportExcelMsg(pMap);
			long endTime=System.currentTimeMillis(); //获取结束时间
			System.out.println("**************************************导入数据程序运行时间： "+(endTime-startTime)+"ms");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
