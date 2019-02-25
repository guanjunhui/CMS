package cn.cebest.util;

/**
* @Title: EHcacheUtils.java 
* @Package com 
* @Description: 缓存工具类
* @author qichangxin   
* @date 2016年09月17日 下午1:27:52 
* @version V1.0
 */
public class EHcacheUtils {
//    //缓存的名称
//    public static final String CACHE_NAME = "mycache";
//    private static CacheManager cacheManager = null;
//    private static Cache cache = null;
//    //ehcache.xml的路径
//    public static String EHCACHE_CONFIG_PATH;
//    
//    public static void init(){
//    	cacheManager = CacheManager.create(EHCACHE_CONFIG_PATH);
//        cache = cacheManager.getCache(CACHE_NAME);
//    }
//   
//    /**
//    * @Title: get 
//    * @Description: 根据key获取缓存的数据
//    * @param key
//    * @param @return    设定文件 
//    * @return Object    返回类型 
//    * @throws
//     */
//    public static Object get(String key){
//        return cache.get(key).getValue();
//    }
//    
//    /**
//    * @Title: get 
//    * @Description: 根据key获取缓存的数据
//    * @param key
//    * @param @return    设定文件 
//    * @return Object    返回类型 
//    * @throws
//     */
//    public static String getString(String key){
//        return cache.get(key).getValue().toString();
//    }
//
//    /**
//     * 
//    * @Title: add 
//    * @Description: 把数据放入缓存中
//    * @param @param key
//    * @param @param obj    设定文件 
//    * @return void    返回类型 
//    * @throws
//     */
//    public static void set(String key,Object obj){
//        Element element = new Element(key,obj);
//        cache.put(element);
//    }
//    
//    /**
//     * 
//    * @Title: remove 
//    * @Description: 根据key删除指定的缓存信息
//    * @param @param key    设定文件 
//    * @return void    返回类型 
//    * @throws
//     */
//    public static void remove(String key){
//        cache.remove(key);
//    }
//    
//    /**
//     * 
//    * @Title: removeAll 
//    * @Description: 删除所有的缓存数据
//    * @param     设定文件 
//    * @return void    返回类型 
//    * @throws
//     */
//    public static void removeAll(){
//        cache.removeAll();
//    }
}
