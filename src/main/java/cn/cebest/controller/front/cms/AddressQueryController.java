package cn.cebest.controller.front.cms;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.cebest.controller.base.BaseController;
import cn.cebest.entity.Page;
import cn.cebest.entity.system.addressquery.AddressQuery;
import cn.cebest.service.system.addressquery.AddressQueryManager;
import cn.cebest.util.Const;
import cn.cebest.util.JsonResult;
import cn.cebest.util.PageData;
import cn.cebest.util.RequestUtils;

@RestController
@RequestMapping(value = "/addressquery")
public class AddressQueryController extends BaseController {

	@Resource(name = "addressQueryService")
	AddressQueryManager addressQueryManager;

	@RequestMapping(value = "/list")
	@ResponseBody
	public MappingJacksonValue list(HttpServletRequest request,Page page, String province, String city, String area, String brand, String jsonpCallback) {
        List<AddressQuery> list = null;
        String siteId=RequestUtils.getSite(this.getRequest()).getId();
        String orderCity = RequestUtils.getAddrByClientIp(request);
		try {
			PageData pd=page.getPd();
			pd.put("orderCity", orderCity);
			pd.put("siteId", siteId);
			if(StringUtils.isNotBlank(province))
			    province = new String(Base64Utils.decodeFromString(province));
			if(StringUtils.isNotBlank(city))
				city = new String(Base64Utils.decodeFromString(city));
			if(StringUtils.isNotBlank(area))
				area = new String(Base64Utils.decodeFromString(area));
			if(StringUtils.isNotBlank(brand))
				brand = new String(Base64Utils.decodeFromString(brand));
			list = addressQueryManager.find(page, province, city, area, brand);
		} catch (Exception e) {
			e.printStackTrace();
			list = new ArrayList<AddressQuery>();
		}
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(new JsonResult(Const.HTTP_OK, "OK", list));
		mappingJacksonValue.setJsonpFunction(jsonpCallback);
		return mappingJacksonValue;
	}
}
