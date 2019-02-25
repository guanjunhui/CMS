package cn.cebest.util;

import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * json处理工具类
 * @author qichangxin
 */
@Component
public class JsonUtil {

	private static final String DEFAULT_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
	private static final ObjectMapper mapper;

	static {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		mapper = new ObjectMapper();
		mapper.setDateFormat(dateFormat);
	}
	
	public static String toJson(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException("转换json字符失败!");
		}
	}
	
	public static <T> T toObject(String json,Class<T> clazz) {
		try {
			return mapper.readValue(json, clazz);
		} catch (IOException e) {
			throw new RuntimeException("将json字符转换为对象时失败!");
		}
	}
	public static void readJson(){
		JsonParser parse =new JsonParser();  //创建json解析器
        try {
            JsonObject json=(JsonObject) parse.parse(new FileReader("C:\\Users\\lenovo\\Documents\\WeChat Files\\wzdong12\\Files\\address.json"));  //创建jsonObject对象
            JsonArray array=json.get("槟榔").getAsJsonArray();   //将json数据转为为String型的数据
            for(int i=0;i<array.size();i++){
            	PageData pd=new PageData();
            	JsonObject reust=array.get(i).getAsJsonObject();
                pd.put("MY_NAME", reust.get("name").getAsString());
                pd.put("TEL", reust.get("tel").getAsString());
                pd.put("ADDRESS", reust.get("address").getAsString());
                pd.put("CITY", reust.get("city").getAsString());
                pd.put("PROVINCE", reust.get("province").getAsString());
                pd.put("BRAND", "槟榔");
                pd.put("ID",UuidUtil.get32UUID());
                //入库
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
