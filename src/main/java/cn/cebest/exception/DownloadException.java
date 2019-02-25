package cn.cebest.exception;

public class DownloadException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public DownloadException(String str){
		super(str);
	}
	
    /**
     * 构造异常.
     *
     * @param message 信息描述
     *            
     * @param cause 根异常类
     */
    public DownloadException(String message, Throwable cause){
        super(message, cause);
    }
}
