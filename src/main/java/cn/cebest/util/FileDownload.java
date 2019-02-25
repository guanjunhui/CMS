package cn.cebest.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.repository.query.Param;

import cn.cebest.exception.DownloadException;
import cn.cebest.exception.PageNotFoundException;

/**
 * 下载文件
 * 创建人：中企高呈
 * 创建时间：2014年12月23日
 * @version
 */
public class FileDownload {

	/**
	 * @param response 
	 * @param filePath		//文件完整路径(包括文件名和扩展名)
	 * @param fileName		//下载后看到的文件名
	 * @return  文件名
	 */
	public static void fileDownload(final HttpServletResponse response, String filePath, String fileName) throws Exception{  
		   
		byte[] data = FileUtil.toByteArray2(filePath);  
	    fileName = URLEncoder.encode(fileName, "UTF-8");  
	    response.reset();  
	    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
	    response.addHeader("Content-Length", "" + data.length);  
	    response.setContentType("application/octet-stream;charset=UTF-8");  
	    OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());  
	    outputStream.write(data);  
	    outputStream.flush();  
	    outputStream.close();
	    response.flushBuffer();
	    
	} 
	public static void downloadFile(final HttpServletResponse response, String filePath, String fileName,HttpServletRequest request)throws Exception{
		byte[] data=null;
		boolean flag=true;
		try {
			data = FileUtil.toByteArray2(filePath);
		} catch (IOException e) {
			flag=false;
			e.printStackTrace();
		}  
		if(!flag){
			throw new DownloadException("文件不存在");
		}
		response.setContentType("application/octet-stream;charset=UTF-8");  
	    //fileName = URLEncoder.encode(fileName, "UTF-8"); 
	    String browser = request.getHeader("User-Agent").toUpperCase();
	    if (browser.contains("MSIE")
				||browser.contains("LIKE GECKO")) {//IE
			fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
		}else{
			fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");
		}
	    response.reset();
	    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
	    response.addHeader("Content-Length", "" + data.length);
	    OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
	    outputStream.write(data);
	    outputStream.flush();
	    outputStream.close();
	    response.flushBuffer();
	}
	public static void downloadBigFile(HttpServletResponse response,@Param("url")String url,HttpServletRequest request) throws Exception{  
	      //String path = request.getSession().getServletContext().getRealPath("vod"+File.separator+"log"+File.separator+url);   
	      try {     
	          File file = new File(url);     
	            if (file.exists()) {     
	               String filename = file.getName();     
	               InputStream fis = new BufferedInputStream(new FileInputStream( file));   //用BufferedInputStream读取文件  
	               	response.setContentType("application/octet-stream;charset=UTF-8");     
	                response.reset();     
	                response.setContentType("application/x-download");  
	                response.setHeader("Content-Disposition","attachment;filename="+ new String(filename.getBytes("UTF-8"),"ISO8859-1"));  
	                response.addHeader("Content-Length", "" + file.length());     
	                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());     
	                byte[] buffer = new byte[1024];     
	                int i = -1;     
	                while ((i = fis.read(buffer)) != -1) {   //不能一次性读完，大文件会内存溢出（不能直接fis.read(buffer);）  
	                    toClient.write(buffer, 0, i);    
	                      
	                }     
	                fis.close();     
	                toClient.flush();     
	                toClient.close();    
	            } else {    
	            	throw new PageNotFoundException("文件未找到");
	               /*PrintWriter out = response.getWriter();     
	               out.print("<script>");     
	               out.print("alert(\"Not find the file\")"); 
	               out.print("</script>");  */  
	            }     
	        } catch (IOException ex) {    
	        	throw new PageNotFoundException("文件未找到");
	          /* PrintWriter out = response.getWriter();     
	               out.print("<script>");     
	               out.print("alert(\"Not find the file\")");
	               out.print("</script>");     */
	        }     
	  }  
}
