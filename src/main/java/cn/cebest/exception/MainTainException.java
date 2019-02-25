package cn.cebest.exception;

/**
 * 站点维护异常
 * @author qichangxin
 * @version 2017/11/20
 */
public class MainTainException extends RuntimeException{

	private static final long serialVersionUID = 1L;

    /**
     * 构造异常.
     * @param message 信息描述
     */
    public MainTainException(String message){
        super(message);
    }

    /**
     * 构造异常.
     *
     * @param message 信息描述
     *            
     * @param cause 根异常类
     */
    public MainTainException(String message, Throwable cause){
        super(message, cause);
    }
    

}
