package cn.cebest.service.system.infineon;

import java.util.Map;

import cn.cebest.entity.Page;

/**
 *
 * @author wangweijie
 * @Date 2018年10月30日
 * @company 中企高呈
 */
public interface IpcSolutionService {

	/**
	 * @param page
	 * @return
	 */
	Map<String, Object> getSolutionByCondation(Page page) throws Exception;

}
