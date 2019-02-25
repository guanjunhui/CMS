package cn.cebest.portal.common.resolve.fetchshaoyin;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by cebest on 2018/7/18.
 */
public class SortList implements Comparator {
	private boolean desc = false;
	public SortList(boolean desc){
		this.desc = desc;
	}
    @Override
    public int compare(Object o1, Object o2) {
        ReviewVO vo1=(ReviewVO)o1;
        ReviewVO vo2=(ReviewVO)o2;
        int flag=0;
        try {
            Date vo1Date=new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA).parse(vo1.getReviewDateCh());
            Date vo2Date=new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA).parse(vo2.getReviewDateCh());
            if(desc){
            	flag=vo2Date.compareTo(vo1Date);
            }else{
            	flag=vo1Date.compareTo(vo2Date);
            }            
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }
}
