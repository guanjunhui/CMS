package cn.cebest.controller.front.cms;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.system.customForm.CustomformAttributeValue;
import cn.cebest.entity.vo.CustomFormVo;
import cn.cebest.service.system.customForm.CustomFormAttributeService;
import cn.cebest.service.system.customForm.CustomformAttributeValueService;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.StringUtil;

/**
 * 说明：自定义表单提交 创建人：qichangxin@300.cn 创建时间：2018-01-19
 */
@Controller
@RequestMapping(value = "/customformsubmit")
public class CustomFormSubmitController extends BaseController {

	public static ReentrantLock lock = new ReentrantLock();

	@Autowired
	private CustomformAttributeValueService customformAttributeValueService;

	@Autowired
	private CustomFormAttributeService customFormAttributeService;
	
	@ResponseBody
	@RequestMapping(value="/arisSave/{code}")
	public JsonResult arisSave(@RequestBody(required = true) Map<String, Object> itemsData,@PathVariable(value = "code") String code) {
		//验证码校验
		HttpSession session = getSession();
		//获取session中的验证码
		String sessionCode = (String) session.getAttribute(Const.SESSION_SECURITY_FRONT_CODE);
		if (!code.equalsIgnoreCase(sessionCode)) {//判断验证码
			return new JsonResult(Const.HTTP_ERROR_400, "验证码错误！");
		}
		Long createdTime = null;//创建时间必须保证唯一性
		lock.lock();
		try {
			createdTime = System.currentTimeMillis();
		} finally {
			lock.unlock();
		}
		List<CustomformAttributeValue> fiormList = new LinkedList<CustomformAttributeValue>();
		for (Map.Entry<String, Object> entry : ((Map<String, Object>) itemsData.get("itemsData")).entrySet()) {
			CustomformAttributeValue data = new CustomformAttributeValue();
			data.setAttrId(entry.getKey());
			data.setAttrValue(String.valueOf(entry.getValue()));
			data.setCreatedTime(createdTime);
			data.setId(this.get32UUID());
			fiormList.add(data);
		}
		
		try {
			customformAttributeValueService.saveBatch(fiormList);
		} catch (Exception e) {
			logger.error("save the form datas occured error!", e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}

	/**
	 * 保存表单数据(含验证码)
	 * 
	 * @param itemsData
	 *            表单对象
	 * @param code
	 *            验证码(没有则传递空字符串)
	 * @return
	 */
	@RequestMapping(value = "/save/{code}")
	@ResponseBody
	public JsonResult save(@RequestBody(required = true) Map<String, Object> itemsData, @PathVariable(value = "code") String code) {
		//验证码校验
		HttpSession session = getSession();
		//获取session中的验证码
		String sessionCode = (String) session.getAttribute(Const.SESSION_SECURITY_FRONT_CODE);
		if (!code.equalsIgnoreCase(sessionCode)) {//判断验证码
			return new JsonResult(Const.HTTP_ERROR_400, "验证码错误！");
		}
		Long createdTime = null;//创建时间必须保证唯一性
		lock.lock();
		try {
			createdTime = System.currentTimeMillis();
		} finally {
			lock.unlock();
		}
		List<CustomformAttributeValue> fiormList = new LinkedList<CustomformAttributeValue>();
		for (Map.Entry<String, Object> entry : itemsData.entrySet()) {
			CustomformAttributeValue data = new CustomformAttributeValue();
			data.setAttrId(entry.getKey());
			data.setAttrValue(String.valueOf(entry.getValue()));
			data.setCreatedTime(createdTime);
			data.setId(this.get32UUID());
			fiormList.add(data);
		}
		try {
			customformAttributeValueService.saveBatch(fiormList);
		} catch (Exception e) {
			logger.error("save the form datas occured error!", e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 保存表单数据(不含验证码)
	 * 
	 * @param itemsData
	 *            表单对象
	 * @param code
	 *            验证码(没有则传递空字符串)
	 * @return
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public JsonResult save(@RequestBody(required = true) Map<String, Object> itemsData) {
		Long createdTime = null;//创建时间必须保证唯一性
		lock.lock();
		try {
			createdTime = System.currentTimeMillis();
		} finally {
			lock.unlock();
		}
		List<CustomformAttributeValue> fiormList = new LinkedList<CustomformAttributeValue>();
		for (Map.Entry<String, Object> entry : itemsData.entrySet()) {
			CustomformAttributeValue data = new CustomformAttributeValue();
			data.setAttrId(entry.getKey());
			data.setAttrValue(String.valueOf(entry.getValue()));
			data.setCreatedTime(createdTime);
			data.setId(this.get32UUID());
			fiormList.add(data);
		}
		try {
			customformAttributeValueService.saveBatch(fiormList);
		} catch (Exception e) {
			logger.error("save the form datas occured error!", e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 保存表单数据(不含验证码)
	 * 
	 * @param itemsData
	 *            表单对象
	 * @param code
	 *            验证码(没有则传递空字符串)
	 * @return
	 */
	@RequestMapping(value = "/beinengSave")
	@ResponseBody
	public JsonResult beinengSave(@RequestBody(required = true) Map<String, Object> itemsData) {
		Long createdTime = null;//创建时间必须保证唯一性
		lock.lock();
		try {
			createdTime = System.currentTimeMillis();
		} finally {
			lock.unlock();
		}
		List<CustomformAttributeValue> fiormList = new LinkedList<CustomformAttributeValue>();
		for (Map.Entry<String, Object> entry : ((Map<String, Object>) itemsData.get("itemsData")).entrySet()) {
			CustomformAttributeValue data = new CustomformAttributeValue();
			data.setAttrId(entry.getKey());
			data.setAttrValue(String.valueOf(entry.getValue()));
			data.setCreatedTime(createdTime);
			data.setId(this.get32UUID());
			fiormList.add(data);
		}
		
		try {
			customformAttributeValueService.saveBatch(fiormList);
		} catch (Exception e) {
			logger.error("save the form datas occured error!", e);
			return new JsonResult(Const.HTTP_ERROR, e.getMessage());
		}
		return new JsonResult(Const.HTTP_OK, "OK");
	}
	
	/**
	 * 临时方法
	 * 
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 * @param jsonpCallback
	 * @return
	 */
	@RequestMapping(value = "/saveForJsonp")
	@ResponseBody
	public MappingJacksonValue saveForJsonp(String arg1, String jsonpCallback) {
		if (StringUtil.isNotBlank(arg1)) {
			List<CustomformAttributeValue> fiormList = new LinkedList<CustomformAttributeValue>();
			String jsonStr = new String(Base64Utils.decodeFromString(arg1));
			JSONObject json = JSONObject.parseObject(jsonStr);
			Set<String> keySet = json.keySet();
			CustomformAttributeValue data = null;
			for (String key : keySet) {
				System.out.println(key + " == " + json.getString(key));
				data = new CustomformAttributeValue();
				data.setAttrId(key);
				data.setAttrValue(json.getString(key));
				data.setCreatedTime(System.currentTimeMillis());
				data.setId(this.get32UUID());
				fiormList.add(data);
			}
			try {
				customformAttributeValueService.saveBatch(fiormList);
			} catch (Exception e) {
				logger.error("save the form datas occured error!", e);
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(new JsonResult(Const.HTTP_ERROR, e.getMessage()));
				mappingJacksonValue.setJsonpFunction(jsonpCallback);
				return mappingJacksonValue;
			}
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(new JsonResult(Const.HTTP_OK, "OK"));
			mappingJacksonValue.setJsonpFunction(jsonpCallback);
			return mappingJacksonValue;
		} else {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(new JsonResult(Const.HTTP_ERROR, "没有接收到参数"));
			mappingJacksonValue.setJsonpFunction(jsonpCallback);
			return mappingJacksonValue;
		}
	}

	/**
	 * 表单数据查询（模糊查询）
	 * 
	 * @param itemsData
	 *            表单对象
	 * @param code
	 *            验证码(没有则传递空字符串)
	 * @return
	 */
	@RequestMapping(value = "/search")
	@ResponseBody
	public JsonResult search(@RequestParam("formId") String formId, @RequestParam("condition") String condition) {
		CustomFormVo customFormVo = null;
		try {
			customFormVo = customFormAttributeService.getAttributeAndValueByFormIdAndCondition(formId, condition);
		} catch (Exception e) {
			logger.error("get the customform attr and data list failed!", e);
		}
		return new JsonResult(Const.HTTP_OK, "OK", customFormVo);
	}

	/**
	 * 表单数据查询（模糊查询，专卖店查询专用）
	 * 
	 * @param itemsData
	 *            表单对象
	 * @param code
	 *            验证码(没有则传递空字符串)
	 * @return
	 */
	@RequestMapping(value = "/address")
	@ResponseBody
	public JsonResult address(@RequestParam("formId") String formId, @RequestParam("condition") String condition) {
		CustomFormVo customFormVo = null;
		try {
			customFormVo = customFormAttributeService.getAttributeAndValueByFormIdAndAddress(formId, condition);
		} catch (Exception e) {
			logger.error("get the customform attr and data list failed!", e);
		}
		return new JsonResult(Const.HTTP_OK, "OK", customFormVo);
	}

}
