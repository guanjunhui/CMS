package cn.cebest.portal.common.resolve.fetchshaoyin;

import java.io.File;
import java.io.FileOutputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.cebest.util.SystemConfig;

/**
 * Created by lhb on 2018/5/25.
 */
public class PaseTool {

    //失败数据统计，解析页面过程中如果有空值的时候，写入该变量中
    public  static StringBuffer falsetext=new StringBuffer();

    private static String JSON_PATH=null;
    private static String KEY_JSON_PATH = "JSON.PATH";

    private static String[][] fetchUrldefalut=new String[][]{
            {
                    "https://www.amazon.co.uk/AfterShokz-Conduction-Headphones-Microphone-grey/product-reviews/B018XNGQOE/ref=cm_cr_dp_d_show_all_btm?ie=UTF8&reviewerType=all_reviews",
                    "https://www.amazon.co.uk/AfterShokz-Conduction-Headphones-Microphone-grey/product-reviews/B018XNGQOE/ref=cm_cr_othr_d_paging_btm_",
                    "?ie=UTF8&reviewerType=all_reviews&pageNumber=",
                    "c89ee85dd6294ebb805b7f53f898d006"
            },
            {
                    "https://www.amazon.co.uk/Aftershokz-AS650MB-Sweatproof-Conduction-Lightweight-blue/product-reviews/B076FCWFPH/ref=cm_cr_dp_d_show_all_top?ie=UTF8&reviewerType=all_reviews",
                    "https://www.amazon.co.uk/Aftershokz-AS650MB-Sweatproof-Conduction-Lightweight-blue/product-reviews/B076FCWFPH/ref=cm_cr_othr_d_paging_btm_",
                    "?ie=UTF8&reviewerType=all_reviews&pageNumber=",
                    "f101d369ecc4429db04e5c2ef49a71d2"
            },
            {
                    "https://www.amazon.co.uk/Aftershokz-Titanium-Conduction-Headphones-Microphone-Ocean-Blue-Black/product-reviews/B01N1ENL38/ref=cm_cr_dp_d_show_all_top?ie=UTF8&reviewerType=all_reviews",
                    "https://www.amazon.co.uk/Aftershokz-Titanium-Conduction-Headphones-Microphone-Ocean-Blue-Black/product-reviews/B01N1ENL38/ref=cm_cr_arp_d_paging_btm_",
                    "?ie=UTF8&reviewerType=all_reviews&pageNumber=",
                    "3dcd0be37bb74ec785395f2bb14b3e07"
            },
            {
                    "https://www.amazon.co.uk/Aftershokz-Titanium-Conduction-Headphones-Earphone-Onyx-Black/product-reviews/B01N51R0GL/ref=cm_cr_dp_d_show_all_top?ie=UTF8&reviewerType=all_reviews",
                    "https://www.amazon.co.uk/Aftershokz-Titanium-Conduction-Headphones-Earphone-Onyx-Black/product-reviews/B01N51R0GL/ref=cm_cr_arp_d_paging_btm_",
                    "?ie=UTF8&reviewerType=all_reviews&pageNumber=",
                    "3dcd0be37bb74ec785395f2bb14b3e07"
            }
    };

    /**
     * 
     * 启动线程，爬虫开始抓取亚马逊页面，并保存到指定文件内
     * @param needproxy
     * @param fetchUrl
     */
    public static void startThreadFetch(boolean needproxy,String[][] fetchUrl){
        MyRunnable runnable = new MyRunnable(needproxy,fetchUrl);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * 对外提供抓取方法，用于抓取全部三个商品的评论首页统计，以及每个商品对应的全部评论内容
     * 获取方法为 map 中的三个对象，通过以下key来获取
     * Reviews_B018XNGQOE
     * Reviews_B075FMK7SD
     * Reviews_B01N1ENL38
     *
     * @param needproxy 是否开启代理：本地访问亚马逊需要使用代理软件自由门，并且该值设置为true
     * @param fetchUrl 需要抓取商品的URL地址：注意，如果地址没有变化，此处可以传null,即使用默认的地址，解决将来地址变化了，可以在此处传入给方法
     * @return
     */
    public static void fetchHtmlContent(boolean needproxy,String[][] fetchUrl){
        //Map<String ,ReviewMainVO> mainVOMap=new HashMap<String, ReviewMainVO>();
        //B018XNGQOE 评论URL
        //String allurl="https://www.amazon.co.uk/AfterShokz-Conduction-Headphones-Microphone-grey/product-reviews/B018XNGQOE/ref=cm_cr_dp_d_show_all_btm?ie=UTF8&reviewerType=all_reviews";
        //String pageUpUrl="https://www.amazon.co.uk/AfterShokz-Conduction-Headphones-Microphone-grey/product-reviews/B018XNGQOE/ref=cm_cr_othr_d_paging_btm_";
        //String pageDownUrl="?ie=UTF8&reviewerType=all_reviews&pageNumber=";

        //B075FMK7SD 评论URL
        //String allurl="https://www.amazon.co.uk/AfterShokz-Conduction-Headphones-Bluetooth-Sweatproof-grey/product-reviews/B075FMK7SD/ref=cm_cr_dp_d_show_all_btm?ie=UTF8&reviewerType=all_reviews";
        //String pageUpUrl="https://www.amazon.co.uk/AfterShokz-Conduction-Headphones-Bluetooth-Sweatproof-grey/product-reviews/B075FMK7SD/ref=cm_cr_arp_d_paging_btm_";
        //String pageDownUrl="?ie=UTF8&reviewerType=all_reviews&pageNumber=";

        //B01N1ENL38 评论URL
        //String allurl="https://www.amazon.co.uk/Aftershokz-Titanium-Conduction-Headphones-Microphone-Ocean-Blue-Black/product-reviews/B01N1ENL38/ref=cm_cr_dp_d_show_all_top?ie=UTF8&reviewerType=all_reviews";
        //String pageUpUrl="https://www.amazon.co.uk/Aftershokz-Titanium-Conduction-Headphones-Microphone-Ocean-Blue-Black/product-reviews/B01N1ENL38/ref=cm_cr_arp_d_paging_btm_";
        //String pageDownUrl="?ie=UTF8&reviewerType=all_reviews&pageNumber=";

        //B01N51R0GL 评论URL
        //String allurl="https://www.amazon.co.uk/Aftershokz-Titanium-Conduction-Headphones-Earphone-Onyx-Black/product-reviews/B01N51R0GL/ref=cm_cr_dp_d_show_all_top?ie=UTF8&reviewerType=all_reviews";
        //String pageUpUrl="https://www.amazon.co.uk/Aftershokz-Titanium-Conduction-Headphones-Earphone-Onyx-Black/product-reviews/B01N51R0GL/ref=cm_cr_arp_d_paging_btm_";
        //String pageDownUrl="?ie=UTF8&reviewerType=all_reviews&pageNumber=";

        System.out.println("抓取程序，开始执行！");
        long starttime=System.currentTimeMillis();

        //传入参数fetchUrl如果为空，使用默认URL地址
        if(fetchUrl==null||fetchUrl.length<0){
            fetchUrl=fetchUrldefalut;
        }

        for(int i=0;i<fetchUrl.length;i++){
            ReviewMainVO vo=fetchHtmlContent(needproxy,fetchUrl[i][0],fetchUrl[i][1],fetchUrl[i][2]);
            if(vo!=null){
                String json = JSONObject.toJSONString(vo);
                try{
                    String fileto="";
                    String filename=fetchUrl[i][3]+".json";
                    fileto=JSON_PATH+filename;
                    writeFile(fileto,json);
                    System.out.println(fetchUrl[i][3]+": 生成json文件完成,路径："+fileto);
                    //mainVOMap.put("ReviewVO-"+i,vo);
                }catch (Exception e){
                    System.out.println("抓取程序，生成json文件失败，报错信息为："+e.getMessage());
                }
            }
        }
        long endtime=System.currentTimeMillis();
        long counttime=(endtime-starttime)/1000;
        System.out.println("抓取程序，执行完成，总用时间:"+counttime+"秒！");
        //return mainVOMap;

    }

    /**
     * 通过读取抓取后存储的文件，生成评论信息
     * 获取map 中的key值为：B018XNGQOE ; B076FCWFPH ; B01N1ENL38 ; B01N51R0GL ;
     * @param
     * @return
     */
    public static Map<String ,ReviewMainVO> getReviewFromJson(){
        Map<String ,ReviewMainVO> mainVOMap=new HashMap<String, ReviewMainVO>();
        try {
            List<File> reviewsFile=readFile(JSON_PATH);
            /*FileUtils.readFileToString();*/
            if(reviewsFile!=null&&reviewsFile.size()>0){
                for (File file:reviewsFile
                     ) {
                    if(file!=null){
                        String filename=file.getName();
                        String mapkey=filename.substring(0,filename.length()-".json".length());
                        String json=FileUtils.readFileToString(file);
                        if(json!=null&&json.length()>0){
                            ReviewMainVO vo=JSON.parseObject(json,ReviewMainVO.class);
                            mainVOMap.put(mapkey,vo);
                        }
                    }
                }
            }
        }catch (Exception e){
            System.out.println("读取json文件报错，错误信息为："+e.getMessage());
            return  null;
        }
        return mainVOMap;
    }
    
    /**
     * 评论内容排序，按照评论日期排序
     * @param vo
     * @param desc true为日期降序，即由近及远，false为日期升序，即由远及近
     * @return
     */
    public static ReviewMainVO sortReviewsList(ReviewMainVO vo,boolean desc){    	
    	SortList sortList = new SortList(desc);
    	Collections.sort(vo.getReviews(),sortList);    	
    	return vo;
    	
    }

    /**
     * 抓取单条商品的首页统计信息，以及该商品的全部评论信息
     * @param needproxy 是否开启代理：本地访问亚马逊需要使用代理软件自由门，并且该值设置为true
     * @param allUrl    商品首页URL
     * @param pageUpUrl 组装分页URL的前半部分地址
     * @param pageDownUrl   组装分页URL的后半部分地址
     * @return
     */
    private static ReviewMainVO fetchHtmlContent(boolean needproxy,String allUrl,String pageUpUrl,String pageDownUrl){

        List<ReviewVO> lists=new ArrayList<>();
        ReviewMainVO mainVO=getReviewMainVO(needproxy,allUrl);
        if(mainVO!=null){
            String pagenum=mainVO.getPagenum();
            if(pagenum!=null&&!"".equals(pagenum)){
                System.out.println("抓取主页完成！");
                int page=Integer.parseInt(pagenum);
                String pageurl="";
                for (int i=1;i<page+1;i++){
                    // 分页链接地址：组装而成 i代表页码
                    pageurl=pageUpUrl+i+pageDownUrl+i;

                    List<ReviewVO> list=getReviewVOs(needproxy,pageurl,i);
                    if(list!=null&&list.size()>0){
                        lists.addAll(list);
                        System.out.println("抓取分页中第 "+i+" 页完成！ 本页抓取数量为："+list.size()+"条！");
                    }

                }
                mainVO.setReviews(lists);
                System.out.println("抓取分页全部完成！");
                System.out.println("========================================================================== 已成功抓取的评论数量： "+mainVO.getReviews().size());
            }else{
                mainVO=null;
            }

        }
        return mainVO;
    }


    /**
     * 抓取商品评论主页面的评论统计，以及全部评论的分页总数
     * @param needproxy 是否开启代理，本地访问亚马逊需要使用代理软件自由门，并且该值设置为true
     * @param allUrl    商品评论主页面的url地址
     * @return
     */
    private static ReviewMainVO getReviewMainVO(boolean needproxy,String allUrl){

        ReviewMainVO mainVO=new ReviewMainVO();
        try {
            Document doc=null;
            if(needproxy){
                doc = Jsoup.connect(allUrl).proxy("127.0.0.1",8580)
                        .timeout(30000)
                        .get();
            }else {
                doc = Jsoup.connect(allUrl).get();
            }

            //评论分页总数 "page-button"
            String page="";
            try {
                page=doc.getElementsByClass("page-button").last().text();
                mainVO.setPagenum(page);
            } catch (Exception e) {
                falsetext.append("全部评论的'分页总数'为空  ："+System.getProperty("line.separator"));
            }

            //全部评论第一页，评论总数 class="a-size-medium totalReviewCount"
            String totalReviewCount="";
            try {
                totalReviewCount=doc.getElementsByClass("a-size-medium totalReviewCount").first().html();
                mainVO.setTotalReviewCount(totalReviewCount);
            } catch (Exception e) {
                falsetext.append("全部评论的'评论总数'为空  ："+System.getProperty("line.separator"));
            }

            //全部评论第一页，星标比例 class="arp-rating-out-of-text"
            String starCount="";
            try {
                starCount=doc.getElementsByClass("arp-rating-out-of-text").first().html();
                mainVO.setStarCount(starCount);
            } catch (Exception e) {
                falsetext.append("全部评论的'星标比例'为空  ："+System.getProperty("line.separator"));
            }

            //全部评论5星比例 class="a-size-small a-link-normal 5star histogram-review-count"
            String star5="";
            try {
                star5=doc.getElementsByClass("a-size-small a-link-normal 5star histogram-review-count").first().html();
                mainVO.setStar5(star5);
            } catch (Exception e) {
                falsetext.append("全部评论的'5星比例'为空  ："+System.getProperty("line.separator"));
            }

            //全部评论4星比例 class="a-size-small a-link-normal 4star histogram-review-count"
            String star4="";
            try {
                star4=doc.getElementsByClass("a-size-small a-link-normal 4star histogram-review-count").first().html();
                mainVO.setStar4(star4);
            } catch (Exception e) {
                falsetext.append("全部评论的'4星比例'为空  ："+System.getProperty("line.separator"));
            }

            //全部评论3星比例 class="a-size-small a-link-normal 3star histogram-review-count"
            String star3="";
            try {
                star3=doc.getElementsByClass("a-size-small a-link-normal 3star histogram-review-count").first().html();
                mainVO.setStar3(star3);
            } catch (Exception e) {
                falsetext.append("全部评论的'3星比例'为空  ："+System.getProperty("line.separator"));
            }

            //全部评论2星比例 class="a-size-small a-link-normal 2star histogram-review-count"
            String star2="";
            try {
                star2=doc.getElementsByClass("a-size-small a-link-normal 2star histogram-review-count").first().html();
                mainVO.setStar2(star2);
            } catch (Exception e) {
                falsetext.append("全部评论的'2星比例'为空  ："+System.getProperty("line.separator"));
            }

            //全部评论1星比例 class="a-size-small a-link-normal 1star histogram-review-count"
            String star1="";
            try {
                star1=doc.getElementsByClass("a-size-small a-link-normal 1star histogram-review-count").first().html();
                mainVO.setStar1(star1);
            } catch (Exception e) {
                falsetext.append("全部评论的'1星比例'为空  ："+System.getProperty("line.separator"));
            }

        }catch (SocketTimeoutException ee){
            System.out.println("抓取主页面失败，原因为：《连接超时》，报错信息为："+ee.getMessage());
            return null;
        }catch (SocketException se){
            System.out.println("抓取主页面失败，原因为：《网络中断》，报错信息为："+se.getMessage());
            return null;
        }catch (Exception e){
            System.out.println("抓取主页面失败，原因为：《程序异常》，报错信息为："+e.getMessage());
            return null;
        }
        return mainVO;
    }

    /**
     * 抓取每个分页里的评论内容主体
     * @param needproxy 是否开启代理，本地访问亚马逊需要使用代理软件自由门，并且该值设置为true
     * @param pageUrl   商品评论每个分页URL地址
     * @return
     */
    private static List<ReviewVO> getReviewVOs(boolean needproxy,String pageUrl,int pagenum){

        List<ReviewVO> list=new ArrayList<>();
        try {
            Document doc=null;
            if(needproxy){
                doc = Jsoup.connect(pageUrl).proxy("127.0.0.1",8580)
                        .timeout(30000)
                        .get();
            }else {
                doc = Jsoup.connect(pageUrl).get();
            }
            
            

            //评论内容主体
            int countall=0;
            Elements elements=doc.getElementsByClass("a-section review");
            for (Element el:elements
                    ) {
                ReviewVO reviewVO =new ReviewVO();
                countall++;

                //评论作者 a-size-base a-link-normal author
                String author="";
                try {
                    author=el.getElementsByClass("a-size-base a-link-normal author").first().html();
                    reviewVO.setAuthor(author);
                } catch (Exception e) {
                    falsetext.append("第 "+pagenum+" 页的第 "+countall+" 条评论的‘评论作者’为空的评论 ："+System.getProperty("line.separator"));
                }

                //评论概要a-size-base a-link-normal review-title a-color-base a-text-bold
                String gaiyao="";
                try {
                    gaiyao=el.getElementsByClass("a-size-base a-link-normal review-title a-color-base a-text-bold").first().html();
                    reviewVO.setGaiyao(gaiyao);
                } catch (Exception e) {
                    falsetext.append("第 "+pagenum+" 页的第 "+countall+" 条评论的‘评论概要’为空的评论 ："+author+System.getProperty("line.separator"));
                }

                //评论星标 a-icon-alt
                String stars="";
                try {
                    stars=el.getElementsByClass("a-icon-alt").first().html();
                    reviewVO.setStars(stars);
                } catch (Exception e) {
                    falsetext.append("第 "+pagenum+" 页的第 "+countall+" 条评论的‘评论星标’为空的评论 ："+author+System.getProperty("line.separator"));
                }

                //评论日期 a-size-base a-color-secondary review-date
                String reviewDate="";
                try {
                    reviewDate=el.getElementsByClass("a-size-base a-color-secondary review-date").first().html();
                    reviewVO.setReviewDate(reviewDate);
                    reviewVO.setReviewDateCh(formatDate(reviewDate));
                } catch (Exception e) {
                    falsetext.append("第 "+pagenum+" 页的第 "+countall+" 条评论的‘评论日期’为空的评论 ："+author+System.getProperty("line.separator"));
                }

                //购买颜色 a-size-mini a-link-normal a-color-secondary
                String reviewColour="";
                try {
                    reviewColour=el.getElementsByClass("a-size-mini a-link-normal a-color-secondary").first().html();
                    reviewVO.setReviewColour(reviewColour);
                } catch (Exception e) {
                    falsetext.append("第 "+pagenum+" 页的第 "+countall+" 条评论的‘购买颜色’为空的评论 ："+author+System.getProperty("line.separator"));
                }

                //颜色说明 a-size-mini a-color-state a-text-bold
                String textColour="";
                try {
                    textColour=el.getElementsByClass("a-size-mini a-color-state a-text-bold").first().html();
                    //System.out.println("颜色说明 :"+textColour );
                    reviewVO.setTextColour(textColour);
                }catch (Exception e){
                    falsetext.append("第 "+pagenum+" 页的第 "+countall+" 条评论的‘颜色说明’为空的评论 ："+author+System.getProperty("line.separator"));

                }

                //评论主体 a-size-base review-text
                String reviewText="";
                try {
                    reviewText=el.getElementsByClass("a-size-base review-text").first().html();
                    reviewVO.setReviewText(reviewText);
                } catch (Exception e) {
                    falsetext.append("第 "+pagenum+" 页的第 "+countall+" 条评论的‘评论主体’为空的评论 ："+author+System.getProperty("line.separator"));
                }

                //评论有用数量 a-size-base a-color-secondary cr-vote-text
                String voteText="";
                try{
                    voteText=el.getElementsByClass("a-size-base a-color-secondary cr-vote-text").first().html();
                    reviewVO.setVoteText(voteText);
                }catch (Exception e){
                    falsetext.append("第 "+pagenum+" 页的第 "+countall+" 条评论的‘评论有用数量’为空的评论 ："+author+System.getProperty("line.separator"));
                }
                list.add(reviewVO);
            }


        }catch (SocketTimeoutException ee){
            System.out.println("抓取分页页面失败，原因为：《连接超时》，报错信息为："+ee.getMessage());
            return null;
        }catch (SocketException se){
            System.out.println("抓取分页页面失败，原因为：《网络中断》，报错信息为："+se.getMessage());
            return null;
        }catch (Exception e){
            System.out.println("抓取分页页面失败，原因为：《程序异常》，报错信息为："+e.getMessage());
            return null;
        }
        /*catch (Exception e){
            System.out.println("抓取分页第 "+pagenum+" 页面的URL无法访问，报错信息为：");
            e.printStackTrace();
            return null;
        }*/
        return list;
    }

    public static void printReviews(ReviewMainVO reviewMainVO){
        if(reviewMainVO!=null){
            System.out.println();
            //评论分页总数
            System.out.println("评论分页总数              :"+reviewMainVO.getPagenum());

            //全部评论第一页，评论总数 class="a-size-medium totalReviewCount"
            System.out.println("全部评论第一页，评论总数    ："+reviewMainVO.getTotalReviewCount());

            //全部评论第一页，星标比例 class="arp-rating-out-of-text"
            System.out.println("全部评论第一页，星标比例    ："+reviewMainVO.getStarCount());

            //全部评论5星比例 class="a-size-small a-link-normal 5star histogram-review-count"
            System.out.println("全部评论5星比例            ："+reviewMainVO.getStar5());

            //全部评论4星比例 class="a-size-small a-link-normal 4star histogram-review-count"
            System.out.println("全部评论4星比例            ："+reviewMainVO.getStar4());


            //全部评论3星比例 class="a-size-small a-link-normal 3star histogram-review-count"
            System.out.println("全部评论3星比例            ："+reviewMainVO.getStar3());

            //全部评论2星比例 class="a-size-small a-link-normal 2star histogram-review-count"
            System.out.println("全部评论2星比例            ："+reviewMainVO.getStar2());

            //全部评论1星比例 class="a-size-small a-link-normal 1star histogram-review-count"
            System.out.println("全部评论1星比例            ："+reviewMainVO.getStar1());

            List<ReviewVO> list=reviewMainVO.getReviews();
            for (int i=0;i<list.size();i++){
                ReviewVO reviewVO=list.get(i);
                System.out.println("第 "+(i+1)+" 条评论");
                //评论概要a-size-base a-link-normal review-title a-color-base a-text-bold
                System.out.println("评论概要               :"+reviewVO.getGaiyao() );

                //评论星标 a-icon-alt
                System.out.println("评论星标                :"+ reviewVO.getStars());

                //评论作者 a-size-base a-link-normal author
                System.out.println("评论作者                :"+reviewVO.getAuthor() );

                //评论日期 a-size-base a-color-secondary review-date
                System.out.println("评论日期                :"+reviewVO.getReviewDate() );

                //购买颜色 a-size-mini a-link-normal a-color-secondary
                System.out.println("购买颜色                :"+reviewVO.getReviewColour() );

                //颜色说明 a-size-mini a-color-state a-text-bold
                System.out.println("颜色说明                :"+reviewVO.getTextColour() );

                //评论主体 a-size-base review-text
                System.out.println("评论主体                :"+reviewVO.getReviewText() );

                //评论有用数量 a-size-base a-color-secondary cr-vote-text
                System.out.println("评论有用数量             :"+reviewVO.getVoteText() );
            }
        }
    }

    /**
     * 将抓取的评论数据按照每个商品一个json的文件格式，写到指定位置
     * @param filepath
     * @param value
     * @throws Exception
     */
    private static void writeFile(String filepath,String value)throws Exception{
        File file=new File(filepath);
        if(!file.isDirectory()){
            file.createNewFile();
        }else {
            System.out.println("file is create");
        }
        FileOutputStream out=null;
        try {
            out=new FileOutputStream(file);
            out.write(value.getBytes("UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(out!=null){
                out.close();
            }
            out=null;
        }
    }

    /**
     * 读取指定路径的文件，将抓取到的评论数据读到系统中
     * @param filepath
     * @return
     * @throws Exception
     */
    private static List<File> readFile(String filepath)throws Exception{
        File file=null;
        File[] files=null;
        List<File> fileList=null;

        try {
            file=new File(filepath);
            if(file!=null){
                files =file.listFiles();
            }
            if(files!=null&&files.length>0){
                fileList=new ArrayList<File>();

                for (File ff:files
                        ) {
                    if(ff.isFile()&&ff.getName().contains("json")){
                        fileList.add(ff);
                    }
                }
            }

        }catch (Exception e){
            System.out.println("读取评论数据文件失败，错误原因为："+e.getMessage());
            return null;
        }

        return  fileList;
    }
    
    private static String formatDate(String enDate){
    	DateFormat format1 	= 	new SimpleDateFormat("d MMM yyyy",Locale.US);
        Date date			=	null;
        DateFormat format2 	= 	new SimpleDateFormat("yyyy/MM/dd",Locale.CHINA);
        String chDate		=	"";
        String _enDate		=	null;
        try {
            if(enDate!=null&&!"".equals(enDate)){
            	enDate=enDate.trim();
                if(enDate.startsWith("on")){
                	_enDate	=	enDate.substring(3, enDate.length());
                	date	=	format1.parse(_enDate);
                	chDate	=	format2.format(date);
                }
            }            
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return chDate;
    	
    }

    static {
        JSON_PATH=SystemConfig.getPropertiesString(KEY_JSON_PATH);
        
    }

}

class MyRunnable implements Runnable{
    private boolean needproxy;
    private String[][] fetchUrl;
    public MyRunnable(boolean needproxy,String[][] fetchUrl) {
        this.fetchUrl=fetchUrl;
        this.needproxy=needproxy;
    }

    @Override
    public void run() {
        PaseTool.fetchHtmlContent(needproxy,fetchUrl);
    }
}
