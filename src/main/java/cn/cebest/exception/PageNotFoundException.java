package cn.cebest.exception;

/**
 * 模板未找到异常
 * @author qichangxin
 * @version 2017/11/20
 */
public class PageNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

    /**
     * 构造异常.
     * @param message 信息描述
     */
    public PageNotFoundException(String message){
        super(message);
    }

    /**
     * 构造异常.
     *
     * @param message 信息描述
     *            
     * @param cause 根异常类
     */
    public PageNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
    

}
