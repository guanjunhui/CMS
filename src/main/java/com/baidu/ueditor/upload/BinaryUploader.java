package com.baidu.ueditor.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;

public class BinaryUploader {

	public static final State save(HttpServletRequest request, Map<String, Object> conf) {
		//		FileItemStream fileStream = null;
		boolean isAjaxUpload = request.getHeader("X_Requested_With") != null;

		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}

		MultipartHttpServletRequest murequest = (MultipartHttpServletRequest) request;

		//		ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
		if (isAjaxUpload) {
			//upload.setHeaderEncoding( "UTF-8" );
			try {
				murequest.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		try {
			//			FileItemIterator iterator = upload.getItemIterator(request);

			//			while (iterator.hasNext()) {
			//				fileStream = iterator.next();
			//
			//				if (!fileStream.isFormField())
			//					break;
			//				fileStream = null;
			//			}

			String savePath = (String) conf.get("savePath");
			String pathFormat = (String) conf.get("pathFormat");
			String fullPath = "";

			Iterator<String> iter = murequest.getFileNames();
			while (iter.hasNext()) {
				//取得上传文件  
				MultipartFile file = murequest.getFile(iter.next());
				if (file == null || file.getSize() <= 0) {
					return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
				}
				String originFileName = file.getOriginalFilename();
				String suffix = FileType.getSuffixByFilename(originFileName);

				originFileName = originFileName.substring(0, originFileName.length() - suffix.length());

				long maxSize = ((Long) conf.get("maxSize")).longValue();

				if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
					return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
				}

				pathFormat = pathFormat + suffix;
				pathFormat = PathFormat.parse(pathFormat, originFileName);
				fullPath = savePath + pathFormat;

				InputStream is = file.getInputStream();
				State storageState = StorageManager.saveFileByInputStream(is, fullPath, maxSize);
				is.close();

				if (storageState.isSuccess()) {
					storageState.putInfo("url", PathFormat.format(pathFormat));
					storageState.putInfo("type", suffix);
					storageState.putInfo("original", originFileName + suffix);
				}
				return storageState;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
}
