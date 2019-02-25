import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.baidu.translate.demo.TransApi;
import com.google.gson.Gson;

public class Main {

    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "20180730000190149";
    private static final String SECURITY_KEY = "vENoLnYsgl7Gl2nTJQf6";

    public static void main(String[] args) {
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String query = "原文";
        String transResult = api.getTransResult(query, "auto", "en");
        //将string字符串转为map集合
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<String, Object>();
        map = gson.fromJson(transResult, map.getClass());
        //将原文和结果的集合取出来
        ArrayList<Map> map2 = (ArrayList<Map>) map.get("trans_result");
        //将结果打印出来
        System.out.println(map2.get(0).get("dst"));
    }

}
