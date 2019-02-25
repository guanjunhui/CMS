package cn.cebest.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class GetLatAndLngByBaidu {
	/** 
	    * @param addr 
	    * 查询的地址 
	    * @return 
	    * @throws IOException 
	    */
	    public Object[] getCoordinate(String addr) throws IOException { 
	        String lng = null;//经度
	        String lat = null;//纬度
	        String address = null; 
	        try { 
	            address = java.net.URLEncoder.encode(addr, "UTF-8"); 
	        }catch (UnsupportedEncodingException e1) { 
	            e1.printStackTrace(); 
	        } 
	        String key = "f247cdb592eb43ebac6ccd27f796e2d2"; 
	        String url = String .format("http://api.map.baidu.com/geocoder?address=%s&output=json&key=%s", address, key); 
	        URL myURL = null; 
	        URLConnection httpsConn = null; 
	        try { 
	            myURL = new URL(url); 
	        } catch (MalformedURLException e) { 
	            e.printStackTrace(); 
	        } 
	        InputStreamReader insr = null;
	        BufferedReader br = null;
	        try { 
	            httpsConn = (URLConnection) myURL.openConnection();// 不使用代理 
	            if (httpsConn != null) { 
	                insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8"); 
	                br = new BufferedReader(insr); 
	                String data = null; 
	                int count = 1;
	                while((data= br.readLine())!=null){ 
	                    if(count==5){
	                        lng = (String)data.subSequence(data.indexOf(":")+1, data.indexOf(","));//经度
	                        count++;
	                    }else if(count==6){
	                        lat = data.substring(data.indexOf(":")+1);//纬度
	                        count++;
	                    }else{
	                        count++;
	                    }
	                } 
	            } 
	        } catch (IOException e) { 
	            e.printStackTrace(); 
	        } finally {
	            if(insr!=null){
	                insr.close();
	            }
	            if(br!=null){
	                br.close();
	            }
	        }
	        return new Object[]{lng,lat}; 
	    } 
	  
	  
	    public static void main(String[] args) throws IOException {
	        GetLatAndLngByBaidu getLatAndLngByBaidu = new GetLatAndLngByBaidu();
	        List<String> list = new ArrayList<>();
        	list.add("浙江省宁波市江东区沧海路1号港隆家具广场5楼爱依瑞斯专卖店");
        	list.add("福建省莆田市荔城区东圳路红星美凯龙爱依瑞斯店");
        	list.add("福建省宁德市福鼎市");
        	list.add("福州市晋安区连江北路喜盈门家居广场");
        	list.add("佛山市禅城区佛山大道中189号居然之家A座2楼爱依瑞斯");
        	list.add("广东省中山市石岐区大信南路2号四楼简爱家居爱依瑞斯店18676130878王娟");
        	list.add("广东省珠海市香洲区翠微路288号世邦5号厅");
        	list.add("包头市稀土高新区黄河大街红星美凯龙东厅2楼爱依瑞斯");
        	list.add("包头稀土高新区金荣美好家3楼爱依瑞斯");
        	list.add("包头昆都仑区民族东路居然之家3号门2楼爱依瑞斯");
        	list.add("内蒙古鄂尔多斯东盛区天娇南路居然之家1-2-023");
        	list.add("内蒙古锡林浩特市重庆路中段众力景尚园临街商业楼光大美居");
        	list.add("呼伦贝尔国贸家居二楼");
        	list.add("呼伦贝尔国贸家居二楼");
        	list.add("内蒙古乌海市海勃湾区月星家具东门1楼");
        	list.add("内蒙通辽市创业大道居然之家1号门3楼爱依瑞斯一楼");
        	list.add("内蒙古通辽市扎鲁特旗");
        	list.add("赤峰居然之家东厅2楼红星一部2楼东厅");
        	list.add("内蒙古赤峰市新城区居然之家");
        	list.add("内蒙古巴彦淖尔市红星美凯龙2层爱依瑞斯");
        	list.add("呼和浩特市新城区居然之家C座1楼");
        	list.add("呼和浩特市新城区红星美凯龙2楼");
        	list.add("呼和浩特市玉泉区红星美凯龙3楼");
        	list.add("辽阳市白塔区欧亚达3楼爱依瑞斯");
        	list.add("辽宁省丹东市振兴区锦锈家具2楼爱意瑞斯");
        	list.add("辽阳居然之家2楼南厅");
        	list.add("辽阳新区红星美凯龙三楼爱依瑞斯");
        	list.add("丹东锦绣家居");
        	list.add("葫芦岛市龙港区月星家居一层");
        	list.add("葫芦岛市龙湾大街168号独立店");
        	list.add("葫芦岛市兴城居然之家");
        	list.add("辽宁省本溪市明山区胜利路大千家具城2楼爱依瑞斯");
        	list.add("海城天成家居购物中心三楼爱依瑞斯");
        	list.add("大连市沙河口区马栏南街52号红星美凯龙二楼爱依瑞斯");
        	list.add("大连市普兰店颐安家居");
        	list.add("沈阳市铁西区北二东路35号红星美凯龙2楼");
        	list.add("沈阳市浑南大道临波路10号红星美凯龙1楼爱依瑞斯");
        	list.add("铁岭铁西居然之家3楼爱依瑞斯");
        	list.add("辽宁省营口市老边区居然之家二楼");
        	list.add("营口鲅鱼圈区昆仑大街艺海花园东门");
        	list.add("新抚区红星美凯龙");
        	list.add("朝阳市朝阳大街3段29-4百家合家居广场");
        	list.add("盘锦市兴隆台区红星美凯龙一楼、二楼");
        	list.add("辽宁省阜新市滨河路11号滨河家具广场一期一楼");
        	list.add("辽宁省阜新市滨河路11号滨河家具广场一期一楼");
        	list.add("锦州市太和区光彩市场3号厅门市");
        	list.add("辽宁省丹东市东港居然之家家居广场");
        	list.add("大庆市高新区正大方盛东城店三楼");
        	list.add("大庆市龙凤区居然之家二楼");
        	list.add("齐齐哈尔居然之家中汇城店");
        	list.add("齐齐哈尔居然之家凤凰城店");
        	list.add("黑龙江省佳木斯市红旗路凯联家具3楼爱依瑞斯店");
        	list.add("哈尔滨道里区城乡路168号红星美凯龙二楼ARIS");
        	list.add("哈尔滨群力大道与兴江路交口群里居然之家群力店3-024展位");
        	list.add("黑龙江省牡丹江市阳明区光华街66号月星家居一楼110展位");
	        for (int i = 0; i < list.size(); i++) {
	        	Object[] o = getLatAndLngByBaidu.getCoordinate(list.get(i));
	 	        System.out.println(o[0]+","+o[1]);//经度,纬度
			}
	    }
}
