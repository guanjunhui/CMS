package cn.cebest.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/** 
 * 生成验证码的工具类 
 * @author qichangxin@300.cn 
 */  
public class CreateYZMCodeUtils {
	private Logger logger = Logger.getLogger(this.getClass());

	private Integer width;//验证码图片宽度  
    private Integer height;//验证吗图片高度  
    private Integer num;//验证码的个数  
    private String code;//生成验证码一组字符串  
      
    private static final Random ran=new Random();//随机数  
    private static CreateYZMCodeUtils createYZMCodeUtils;  
    /** 
     * 通过默认构造初始化参数 
     */  
    private CreateYZMCodeUtils(){
        width=100;  
        height=20;  
        code="0123456789adcdefghijklmnopqrstuvwxyzABCDEFGHJKLMNPRSTUVWXYZ"; 
//        code="0123456789ABCDEFGHJKLMNPRSTUVWXYZ";  
        num=4;  
    }  
    /** 
     * 利用单利模式创建该验证码工具类 
     * @return 
     */  
    public static CreateYZMCodeUtils getInstance(){  
        if(createYZMCodeUtils==null){  
            createYZMCodeUtils=new CreateYZMCodeUtils();  
        }  
        return createYZMCodeUtils;  
    }  
    public void setCreateYZMCodeUtils(Integer width,Integer height,Integer num,String code){  
        this.width=width;  
        this.height=height;  
        this.num=num;  
        this.code=code;  
    }  
    public void setCreateYZMCodeUtils(Integer width,Integer height,String code){  
        this.width=width;  
        this.height=height;  
        this.code=code;  
    }
    
    /** 
     * 随机生成验证码 
     * @param code 生成验证码的一组字符串 
     * @return 
     */  
    public String getCreateYZMCode(){  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < num; i++) {  
            sb.append(code.charAt(ran.nextInt(code.length())));  
        }  
        return sb.toString();  
    }
    
    /** 
     * 生成buffere Image图片 
     * @param finshCode 生成好的验证码字符串 
     * @return 
     */  
    public String getCreateYZMImg(ByteArrayOutputStream output){
    	String finshCode=getCreateYZMCode();
        // 创建BufferedImage对象  
        BufferedImage img = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);  
        Graphics2D graphic = img.createGraphics();//创建画笔  
        graphic.setColor(Color.WHITE);// 设置颜色  
        graphic.fillRect(0, 0, width, height);//是用预定的颜色填充一个矩形，得到一个着色的矩形块。  
        graphic.drawRect(0, 0, width - 1, height - 1);// 画正方形  
        // 设置字体 风格 风格 高度  
        Font font = new Font("Times New Roman", Font.BOLD + Font.ITALIC,(int) (height * 0.8));  
        graphic.setFont(font);  
        for (int i = 0; i < num; i++) {  
            // 随机设置字体RGB颜色  
            graphic.setColor(new Color(ran.nextInt(255), ran.nextInt(255),ran.nextInt(255)));  
            // 画出验证码  
            graphic.drawString(String.valueOf(finshCode.charAt(i)), i* (width / num) + 4, (int) (height * 0.8));  
        }  
        for (int i = 0; i < (width + height); i++) {  
            // 随机设置字体RGB颜色  
            graphic.setColor(new Color(ran.nextInt(255), ran.nextInt(255),ran.nextInt(255)));  
            // 生成干扰点  
            //graphic.drawOval(ran.nextInt(width), ran.nextInt(height), 1, 1);  
        }  
        for(int i = 0; i <2; i++){
            // 随机设置字体RGB颜色  
            graphic.setColor(new Color(ran.nextInt(255), ran.nextInt(255),ran.nextInt(255)));  
            // 随机生成干扰线  
            //graphic.drawLine(0, ran.nextInt(height), width,ran.nextInt(height));  
        }
		try {
			ImageIO.write(img, "jpg", output);
		} catch (IOException e) {
			logger.error("create the yan zheng ma occured error!", e);
		}
        return finshCode;
    }
	
    public Integer getWidth() {  
        return width;  
    }  
  
    public void setWidth(Integer width) {  
        this.width = width;  
    }  
  
    public Integer getHeight() {  
        return height;  
    }  
  
    public void setHeight(Integer height) {  
        this.height = height;  
    }  
  
    public Integer getNum() {  
        return num;  
    }  
  
    public void setNum(Integer num) {  
        this.num = num;  
    }  
  
    public String getCode() {  
        return code;  
    }  
  
    public void setCode(String code) {  
        this.code = code;  
    }  
  
    public static Random getRan() {  
        return ran;  
    }  

}
