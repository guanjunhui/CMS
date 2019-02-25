package com.baidu.ueditor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.ueditor.define.ActionMap;

import cn.cebest.util.SystemConfig;

/**
 * 配置管理器
 * 
 * @author hancong03@baidu.com
 *
 */
public final class ConfigManager {

	private final String rootPath;
	private final String originalPath;
	private final String contextPath;
	private static final String configFileName = "config.json";
	private String parentPath = null;
	private JSONObject jsonConfig = null;
	// 涂鸦上传filename定义
	private final static String SCRAWL_FILE_NAME = "scrawl";
	// 远程图片抓取filename定义
	private final static String REMOTE_FILE_NAME = "remote";

	/*
	 * 通过一个给定的路径构建一个配置管理器， 该管理器要求地址路径所在目录下必须存在config.properties文件
	 */
	private ConfigManager(String rootPath, String contextPath, String uri) throws FileNotFoundException, IOException {

		rootPath = rootPath.replace("\\", "/");

		this.rootPath = rootPath;
		this.contextPath = contextPath;
		uri = contextPath + "/plugins/ueditor/jsp/controller.jsp";
		if (contextPath.length() > 0) {
			this.originalPath = (this.rootPath + uri.substring(contextPath.length())).replace("//", "/");
		} else {
			this.originalPath = (this.rootPath + uri).replace("//", "/");
		}
		this.initEnv();
	}

	/**
	 * 配置管理器构造工厂
	 * 
	 * @param rootPath
	 *            服务器根路径
	 * @param contextPath
	 *            服务器所在项目路径
	 * @param uri
	 *            当前访问的uri
	 * @return 配置管理器实例或者null
	 */
	public static ConfigManager getInstance(String rootPath, String contextPath, String uri) {

		try {
			return new ConfigManager(rootPath, contextPath, uri);
		} catch (Exception e) {
			return null;
		}

	}

	// 验证配置文件加载是否正确
	public boolean valid() {
		return this.jsonConfig != null;
	}

	public JSONObject getAllConfig() {

		return this.jsonConfig;

	}

	public Map<String, Object> getConfig(int type) {

		Map<String, Object> conf = new HashMap<String, Object>();
		String savePath = "";
		String pathFormat = "";

		switch (type) {

			case ActionMap.UPLOAD_FILE:
				conf.put("isBase64", "false");
				conf.put("maxSize", this.jsonConfig.getLong("fileMaxSize"));
				conf.put("allowFiles", this.getArray("fileAllowFiles"));
				conf.put("fieldName", this.jsonConfig.getString("fileFieldName"));
				pathFormat = this.jsonConfig.getString("filePathFormat");
				savePath = this.jsonConfig.getString("fileSavePath");
				break;

			case ActionMap.UPLOAD_IMAGE:
				conf.put("isBase64", "false");
				conf.put("maxSize", this.jsonConfig.getLong("imageMaxSize"));
				conf.put("allowFiles", this.getArray("imageAllowFiles"));
				conf.put("fieldName", this.jsonConfig.getString("imageFieldName"));
				pathFormat = this.jsonConfig.getString("imagePathFormat");
				savePath = this.jsonConfig.getString("imageSavePath");
				break;

			case ActionMap.UPLOAD_VIDEO:
				conf.put("maxSize", this.jsonConfig.getLong("videoMaxSize"));
				conf.put("allowFiles", this.getArray("videoAllowFiles"));
				conf.put("fieldName", this.jsonConfig.getString("videoFieldName"));
				pathFormat = this.jsonConfig.getString("videoPathFormat");
				savePath = this.jsonConfig.getString("videoSavePath");
				break;

			case ActionMap.UPLOAD_SCRAWL:
				conf.put("filename", ConfigManager.SCRAWL_FILE_NAME);
				conf.put("maxSize", this.jsonConfig.getLong("scrawlMaxSize"));
				conf.put("fieldName", this.jsonConfig.getString("scrawlFieldName"));
				conf.put("isBase64", "true");
				pathFormat = this.jsonConfig.getString("scrawlPathFormat");
				savePath = this.jsonConfig.getString("scrawlSavePath");
				break;

			case ActionMap.CATCH_IMAGE:
				conf.put("filename", ConfigManager.REMOTE_FILE_NAME);
				conf.put("filter", this.getArray("catcherLocalDomain"));
				conf.put("maxSize", this.jsonConfig.getLong("catcherMaxSize"));
				conf.put("allowFiles", this.getArray("catcherAllowFiles"));
				conf.put("fieldName", this.jsonConfig.getString("catcherFieldName") + "[]");
				pathFormat = this.jsonConfig.getString("snapscreenPathFormat");
				savePath = this.jsonConfig.getString("snapscreenUrlPrefix");
				break;

			case ActionMap.LIST_IMAGE:
				conf.put("allowFiles", this.getArray("imageManagerAllowFiles"));
				conf.put("dir", this.jsonConfig.getString("imageManagerListPath"));
				conf.put("count", this.jsonConfig.getInt("imageManagerListSize"));
				break;

			case ActionMap.LIST_FILE:
				conf.put("allowFiles", this.getArray("fileManagerAllowFiles"));
				conf.put("dir", this.jsonConfig.getString("fileManagerListPath"));
				conf.put("count", this.jsonConfig.getInt("fileManagerListSize"));
				break;

		}

		conf.put("savePath", savePath);
		conf.put("pathFormat", pathFormat);
		conf.put("rootPath", this.rootPath);

		return conf;

	}

	private void initEnv() throws FileNotFoundException, IOException {

		File file = new File(this.originalPath);

		if (!file.isAbsolute()) {
			file = new File(file.getAbsolutePath());
		}

		this.parentPath = file.getParent();
		String configContent = this.readFile(this.getConfigPath());
		try {
			JSONObject jsonConfig = new JSONObject(configContent);

			//图片
			String imageSavePath = SystemConfig.getPropertiesString("ueditor.image.savePath");
			putConfig("imageSavePath", imageSavePath, jsonConfig);
			String imageUrlPrefix = SystemConfig.getPropertiesString("ueditor.image.imageUrlPrefix");
			putConfig("imageUrlPrefix", imageUrlPrefix, jsonConfig);
			String imagePathFormat = SystemConfig.getPropertiesString("ueditor.image.imagePathFormat");
			putConfig("imagePathFormat", imagePathFormat, jsonConfig);

			//涂鸦
			String scrawlSavePath = SystemConfig.getPropertiesString("ueditor.scrawl.savePath");
			putConfig("scrawlSavePath", scrawlSavePath, jsonConfig);
			String scrawlUrlPrefix = SystemConfig.getPropertiesString("ueditor.scrawl.scrawlUrlPrefix");
			putConfig("scrawlUrlPrefix", scrawlUrlPrefix, jsonConfig);
			String scrawlPathFormat = SystemConfig.getPropertiesString("ueditor.scrawl.scrawlPathFormat");
			putConfig("scrawlPathFormat", scrawlPathFormat, jsonConfig);

			//截图
			String snapscreenSavePath = SystemConfig.getPropertiesString("ueditor.snapscreen.savePath");
			putConfig("snapscreenSavePath", snapscreenSavePath, jsonConfig);
			String snapscreenUrlPrefix = SystemConfig.getPropertiesString("ueditor.snapscreen.snapscreenUrlPrefix");
			putConfig("snapscreenUrlPrefix", snapscreenUrlPrefix, jsonConfig);
			String snapscreenPathFormat = SystemConfig.getPropertiesString("ueditor.snapscreen.snapscreenPathFormat");
			putConfig("snapscreenPathFormat", snapscreenPathFormat, jsonConfig);

			//抓取远程图片配置
			String catcherSavePath = SystemConfig.getPropertiesString("ueditor.catcher.savePath");
			putConfig("catcherSavePath", catcherSavePath, jsonConfig);
			String catcherUrlPrefix = SystemConfig.getPropertiesString("ueditor.catcher.catcherUrlPrefix");
			putConfig("catcherUrlPrefix", catcherUrlPrefix, jsonConfig);
			String catcherPathFormat = SystemConfig.getPropertiesString("ueditor.catcher.catcherPathFormat");
			putConfig("catcherPathFormat", catcherPathFormat, jsonConfig);

			//上传视频配置
			String videoSavePath = SystemConfig.getPropertiesString("ueditor.video.savePath");
			putConfig("videoSavePath", videoSavePath, jsonConfig);
			String videoUrlPrefix = SystemConfig.getPropertiesString("ueditor.video.videoUrlPrefix");
			putConfig("videoUrlPrefix", videoUrlPrefix, jsonConfig);
			String videoPathFormat = SystemConfig.getPropertiesString("ueditor.video.videoPathFormat");
			putConfig("videoPathFormat", videoPathFormat, jsonConfig);

			//上传文件
			String fileSavePath = SystemConfig.getPropertiesString("ueditor.file.savePath");
			putConfig("fileSavePath", fileSavePath, jsonConfig);
			String fileUrlPrefix = SystemConfig.getPropertiesString("ueditor.file.fileUrlPrefix");
			putConfig("fileUrlPrefix", fileUrlPrefix, jsonConfig);
			String filePathFormat = SystemConfig.getPropertiesString("ueditor.file.filePathFormat");
			putConfig("filePathFormat", filePathFormat, jsonConfig);

			//列出指定目录下的图片
			String imageManagerListPath = SystemConfig.getPropertiesString("ueditor.imageManager.imageManagerListPath");
			putConfig("imageManagerListPath", imageManagerListPath, jsonConfig);
			String imageManagerUrlPrefix = SystemConfig.getPropertiesString("ueditor.imageManager.imageManagerUrlPrefix");
			putConfig("imageManagerUrlPrefix", imageManagerUrlPrefix, jsonConfig);

			//列出指定目录下的文件
			String fileManagerListPath = SystemConfig.getPropertiesString("ueditor.fileManager.fileManagerListPath");
			putConfig("fileManagerListPath", fileManagerListPath, jsonConfig);
			String fileManagerUrlPrefix = SystemConfig.getPropertiesString("ueditor.fileManager.fileManagerUrlPrefix");
			putConfig("fileManagerUrlPrefix", fileManagerUrlPrefix, jsonConfig);

			this.jsonConfig = jsonConfig;
		} catch (Exception e) {
			this.jsonConfig = null;
		}

	}

	private void putConfig(String key, String value, JSONObject jsonConfig) {
		if (StringUtils.isNotBlank(value)) {
			jsonConfig.put(key, value);
		} else {
			if (jsonConfig.has(key)) {
				jsonConfig.put(key, jsonConfig.getString(key));
			} else {
				jsonConfig.put(key, "");
			}
		}
	}

	private String getConfigPath() {
		return this.parentPath + File.separator + ConfigManager.configFileName;
	}

	private String[] getArray(String key) {

		JSONArray jsonArray = this.jsonConfig.getJSONArray(key);
		String[] result = new String[jsonArray.length()];

		for (int i = 0, len = jsonArray.length(); i < len; i++) {
			result[i] = jsonArray.getString(i);
		}

		return result;

	}

	private String readFile(String path) throws IOException {

		StringBuilder builder = new StringBuilder();

		try {

			InputStreamReader reader = new InputStreamReader(new FileInputStream(path), "UTF-8");
			BufferedReader bfReader = new BufferedReader(reader);

			String tmpContent = null;

			while ((tmpContent = bfReader.readLine()) != null) {
				builder.append(tmpContent);
			}

			bfReader.close();

		} catch (UnsupportedEncodingException e) {
			// 忽略
		}

		return this.filter(builder.toString());

	}

	// 过滤输入字符串, 剔除多行注释以及替换掉反斜杠
	private String filter(String input) {

		return input.replaceAll("/\\*[\\s\\S]*?\\*/", "");

	}

}
