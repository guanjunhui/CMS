package cn.cebest.util;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Iterator;
import java.util.Map;

/**
 * @ClassName RequestWrapper
 * @Description TODO
 * @Author Roger
 * @Date 2018/12/17 15:45
 * @Company 中企高呈
 **/
public class RequestWrapper extends HttpServletRequestWrapper {
    private final Logger logger = LoggerFactory.getLogger(RequestWrapper.class);
    public RequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String,String[]> requestMap = super.getParameterMap();
        Iterator iterator = requestMap.entrySet().iterator();

        while(iterator.hasNext()){
            Map.Entry me = (Map.Entry) iterator.next();

            String[] values = (String[]) me.getValue();

            for (int i=0; i<values.length; i++){
                values[i] = xssClean(values[i]);
            }
        }

        return requestMap;
    }

    public String[] getParameterValues(String name) {
        String[] v=super.getParameterValues(name);
        if(v==null || v.length==0)
            return v;
        for(int i=0;i<v.length;i++){
            v[i]=xssClean(v[i]);
        }
        return v;
    }

    public String xssClean(String value){

        AntiSamy antiSamy = new AntiSamy();

        try {
            Policy policy = Policy.getInstance(SystemConfig.getPropertiesString("web.export_user_url") + "/WEB-INF/classes/antisamy-myspace.xml");
            final CleanResults cr = antiSamy.scan(value,policy);
            return cr.getCleanHTML();
        }catch (Exception e){
            logger.error("xss过滤出错：" + e);
        }
        return value;
    }
}
