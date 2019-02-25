package cn.cebest.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/** 
 * 说明：日期处理
 * 创建人：中企高呈
 * 修改时间：2015年11月24日
 * @version
 */
public class DateUtil {
	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

	public final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
	public final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	public final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");
	public final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat sdfTimes = new SimpleDateFormat("yyyyMMddHHmmss");
	public final static SimpleDateFormat sdfTimeMs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	public final static String YEAR="yyyy";
	public final static String DAY="yyyy-MM-dd";
	public final static String DYAS="yyyyMMdd";
	public final static String TIME="yyyy-MM-dd HH:mm:ss";
	public final static String TIMES="yyyyMMddHHmmss";
	public final static String TIMEMS="yyyy-MM-dd HH:mm:ss.S";
	public final static String MONTHDAY="MM-dd";

	
	/**
	 * 获取YYYY格式
	 * @return
	 */
	public static String getSdfTimes() {
		return sdfTimes.format(new Date());
	}
	
	/**
	 * 获取YYYY格式
	 * @return
	 */
	public static String getYear() {
		return sdfYear.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD格式
	 * @return
	 */
	public static String getDay() {
		return sdfDay.format(new Date());
	}
	
	/**
	 * 获取YYYYMMDD格式
	 * @return
	 */
	public static String getDays(){
		return sdfDays.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 * @return
	 */
	public static String getTime() {
		return sdfTime.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss.f格式
	 * @return
	 */
	public static String getTimeMs() {
		return sdfTimeMs.format(new Date());
	}

	/**
	* @Title: compareDate
	* @Description: TODO(日期比较，如果s>=e 返回true 否则返回false)
	* @param s
	* @param e
	* @return boolean  
	* @throws
	* @author fh
	 */
	public static boolean compareDate(String s, String e) {
		if(fomatDate(s)==null||fomatDate(e)==null){
			return false;
		}
		return fomatDate(s).getTime() >=fomatDate(e).getTime();
	}

	/**
	 * 格式化日期
	 * @return
	 */
	public static Date fomatDate(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 格式化日期
	 * @return
	 */
	public static String fomatDate() {
		String curDay = new java.text.SimpleDateFormat(DYAS).format(new Date());
		return curDay;
	}

	/**
	 * 日期格式转化
	 * 
	 * @param srcDate
	 * @param srcFormat
	 * @param destFormat
	 * @return
	 */
	public static String dateFormat(Date srcDate, String destFormat) {
		if (srcDate == null)
			return "";
		if (destFormat == null || destFormat.equals(""))
			destFormat = "yyyy-MM-dd";
		String curDay = new java.text.SimpleDateFormat(destFormat).format(srcDate);
		return curDay;
	}

	/**
	 * 日期格式转化
	 * 
	 * @param srcDate
	 * @param srcFormat
	 * @param destFormat
	 * @return
	 */
	public static String dateFormat(String srcDate, String srcFormat, String destFormat) {
		if (srcFormat == null || srcFormat.equals(""))
			srcFormat = "yyyy-MM-dd";
		if (destFormat == null || destFormat.equals(""))
			destFormat = "yyyy-MM-dd";
		if (srcDate == null || srcDate.equals(""))
			return "";

		Calendar cal = Calendar.getInstance();
		Date currentTime = null;
		try {
			currentTime = (Date) new java.text.SimpleDateFormat(srcFormat).parse(srcDate);
		} catch (ParseException e) {
			e.printStackTrace();
			currentTime = new Date();
		}
		cal.setTime(currentTime);
		String curDay = new java.text.SimpleDateFormat(destFormat).format(cal.getTime());
		return curDay;
	}
	

	  /** 
	   * sql时间对象转换成字符串 
	   * @param Timestamp timestamp 
	   * @param String formatStr
	   * @return 
	   */ 
	  public static String timestamp2string(Timestamp timestamp, String formatStr) { 
		    String strDate = ""; 
		    SimpleDateFormat sdf = new SimpleDateFormat(formatStr); 
		    strDate = sdf.format(timestamp); 
		    return strDate; 
	  } 

	/**
	 * 校验日期是否合法
	 * @return
	 */
	public static boolean isValidDate(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}
	
	/**
	 * 将时间格式化
	 * 
	 * @return
	 */
	public static Date DatePattern(Date date) {
		try {
			String dd = sdfTime.format(date);
			date = stringToDate(dd);
		} catch (Exception e) {
			logger.error("", e);
		}
		return date;
	}

	
	/**
	 * 将字符串格式化为‘yyyy-MM-dd HH:mm:ss’格式的java.util.Date
	 */
	public static Date stringToDate(String srcDate) {
		if (srcDate == null || srcDate.equals(""))
			return null;
		Date d = null;
		try {
			d = sdfTime.parse(srcDate);
		} catch (Exception e) {
			logger.error("", e);
		}
		return d;
	}

	/**
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int getDiffYear(String startTime,String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			//long aa=0;
			int years=(int) (((fmt.parse(endTime).getTime()-fmt.parse(startTime).getTime())/ (1000 * 60 * 60 * 24))/365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}
	 
	/**
     * <li>功能描述：时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long 
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr,String endDateStr){
        long day=0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate = null;
        java.util.Date endDate = null;
        
            try {
				beginDate = format.parse(beginDateStr);
				endDate= format.parse(endDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
            //System.out.println("相隔的天数="+day);
      
        return day;
    }
    
    /**
     * 得到n天之后的日期
     * @param days
     * @return
     */
    public static String getAfterDayDate(int days,String format) {
    	if(StringUtils.isEmpty(format)){
    		format=DYAS;
    	}
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, days); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        
        SimpleDateFormat sdfd = new SimpleDateFormat(format);
        String dateStr = sdfd.format(date);
        return dateStr;
    }
    
    public static String getBeforeDayDate(int days,String format) {
    	days= days*(-1);
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, days); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        
        SimpleDateFormat sdfd = new SimpleDateFormat(format);
        String dateStr = sdfd.format(date);
        return dateStr;
    }

    /**
     * 得到n天之后是周几
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
    	int daysInt = Integer.parseInt(days);
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);
        return dateStr;
    }
    
    /**
     * @Description:  获取当对应现在的昨天时间
     * @author: GuanJunHui     
     * @param @return  
     * @return String
     * @date:  2018年12月21日 下午3:38:52   
     * @version V1.0
     */
    public static String beforeDay() {
    	Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1); //得到前一天
		Date date = calendar.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
    }
    
    /**
     * @Description:  获取昨天的时间
     * @author: GuanJunHui     
     * @param @return  
     * @return String
     * @date:  2019年2月20日 上午11:09:46   
     * @version V1.0
     */
    public static String beforeDayFormat() {
    	Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1); //得到前一天
		Date date = calendar.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
    } 
    
}
