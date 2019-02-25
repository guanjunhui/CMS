package cn.cebest.entity.system.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.cebest.entity.system.ColumConfig;
import cn.cebest.entity.system.Image;
import cn.cebest.entity.system.Seo;
import cn.cebest.entity.system.Video;
import cn.cebest.entity.system.download.KeyValuesUtil;
import cn.cebest.util.ExtendFiledUtil;
/**
 * 产品    
 * @author wzd
 *
 */
public class Product implements Serializable {
private static final long serialVersionUID = -3118882611666749785L;
	
		private String id; //主键id
		private String siteId; //站点id
		private String flag; //站点id
		private String product_TemplateId; //商品模板id
		private String product_TemplateName;//模板名称
		
		private String product_WbUrl; //外部url
		private String product_Summary; //商品概要
		private String product_KeyWords; //商品关键字
		private String created_Time; //创建时间
		private String update_Time; //更新时间
		private String product_Status; //商品状态-暂时没用,改为字体颜色 1:黑色,0:白色
		
		private String productTxt; //内容文本
		
		private String typeId; //商品类型id
		private String [] producttypeids;//商品类型id数组
		private String typeName; //商品类型id
		
		private String product_description;//描述
		private String name;//商品名称
		private String no;//商品编号
		
		private String brandId;//品牌
		private String[] brandIds;//品牌
		private String brandName;
		
		private String imgeId;
		private String imgUrl;//配图
		private String title;
		private String valueText;//属性值
		
		private String typeCode;//类型编码
		
		private String recommend;//标识
		private String hot;//标识
		private String top;//置顶
		
		private String area;//标识
		private String[] fproductids;//标识
		private String recommend_time;
		private String top_time;
		private String hot_time;
		private List<String> columnids;
		private String columId;
		
		private String release_Time;
		private Seo seoInfo;
		private String seo_title;
		private String seo_description;
		private List<String> imageids=new ArrayList<>();
		private List<Image> imageList=new ArrayList<>(); //配图
		private List<Image> timageList=new ArrayList<>(); //配图
		
		private List<Video> videoList=new ArrayList<>();
		private List<String> videoids=new ArrayList<>();
		private List<Video> tvideoList=new ArrayList<>();
		private List<ExtendFiledUtil> fileds=new ArrayList<>();
		//修改提交
		private String[] productRelevantIdList; //相关产品id集合
		private List<Product_Type> productTypeList;
		private List<KeyValuesUtil> objKey_Value;
		private List<ColumConfig> columConfigList;
		private String filedJson;
		private Integer sort;
		private String contentUrlName;
		private String columUrlName;
		
		public String getColumUrlName() {
			return columUrlName;
		}
		public void setColumUrlName(String columUrlName) {
			this.columUrlName = columUrlName;
		}
		public String getContentUrlName() {
			return contentUrlName;
		}
		public void setContentUrlName(String contentUrlName) {
			this.contentUrlName = contentUrlName;
		}
		public Seo getSeoInfo() {
			return seoInfo;
		}

		public void setSeoInfo(Seo seoInfo) {
			this.seoInfo = seoInfo;
		}

		public String getSeo_title() {
			return seo_title;
		}

		public void setSeo_title(String seo_title) {
			this.seo_title = seo_title;
		}

		public String getHot_time() {
			return hot_time;
		}

		public void setHot_time(String hot_time) {
			this.hot_time = hot_time;
		}

		public Integer getSort() {
			return sort;
		}

		public void setSort(Integer sort) {
			this.sort = sort;
		}

		public String[] getFproductids() {
			return fproductids;
		}

		public void setFproductids(String[] fproductids) {
			this.fproductids = fproductids;
		}

		public String getFlag() {
			return flag;
		}

		public void setFlag(String flag) {
			this.flag = flag;
		}

		public String[] getBrandIds() {
			return brandIds;
		}

		public void setBrandIds(String[] brandIds) {
			this.brandIds = brandIds;
		}

		public List<String> getVideoids() {
			return videoids;
		}

		public void setVideoids(List<String> videoids) {
			this.videoids = videoids;
		}

		public List<Video> getVideoList() {
			return videoList;
		}

		public void setVideoList(List<Video> videoList) {
			this.videoList = videoList;
		}

		public List<Video> getTvideoList() {
			return tvideoList;
		}

		public void setTvideoList(List<Video> tvideoList) {
			this.tvideoList = tvideoList;
		}

		public List<String> getColumnids() {
			return columnids;
		}

		public void setColumnids(List<String> columnids) {
			this.columnids = columnids;
		}

		public String getSeo_description() {
			return seo_description;
		}

		public void setSeo_description(String seo_description) {
			this.seo_description = seo_description;
		}
		
		public String getFiledJson() {
			return filedJson;
		}

		public void setFiledJson(String filedJson) {
			this.filedJson = filedJson;
		}

		public List<ExtendFiledUtil> getFileds() {
			return fileds;
		}

		public void setFileds(List<ExtendFiledUtil> fileds) {
			this.fileds = fileds;
		}

		public List<ColumConfig> getColumConfigList() {
			return columConfigList;
		}

		public void setColumConfigList(List<ColumConfig> columConfigList) {
			this.columConfigList = columConfigList;
		}

		public List<KeyValuesUtil> getObjKey_Value() {
			return objKey_Value;
		}

		public void setObjKey_Value(List<KeyValuesUtil> objKey_Value) {
			this.objKey_Value = objKey_Value;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getRecommend_time() {
			return recommend_time;
		}

		public void setRecommend_time(String recommend_time) {
			this.recommend_time = recommend_time;
		}

		public String getTop_time() {
			return top_time;
		}

		public void setTop_time(String top_time) {
			this.top_time = top_time;
		}

		public String getTop() {
			return top;
		}

		public void setTop(String top) {
			this.top = top;
		}

		public List<Product_Type> getProductTypeList() {
			return productTypeList;
		}

		public void setProductTypeList(List<Product_Type> productTypeList) {
			this.productTypeList = productTypeList;
		}

		public String[] getProducttypeids() {
			return producttypeids;
		}

		public void setProducttypeids(String[] producttypeids) {
			this.producttypeids = producttypeids;
		}
		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public String getRelease_Time() {
			return release_Time;
		}

		public void setRelease_Time(String release_Time) {
			this.release_Time = release_Time;
		}

		public String getImgeId() {
			return imgeId;
		}

		public void setImgeId(String imgeId) {
			this.imgeId = imgeId;
		}

		public String getBrandId() {
			return brandId;
		}

		public void setBrandId(String brandId) {
			this.brandId = brandId;
		}

		public String getTypeCode() {
			return typeCode;
		}

		public void setTypeCode(String typeCode) {
			this.typeCode = typeCode;
		}

		public String getRecommend() {
			return recommend;
		}

		public void setRecommend(String recommend) {
			this.recommend = recommend;
		}

		public String getHot() {
			return hot;
		}

		public void setHot(String hot) {
			this.hot = hot;
		}

		public String getValueText() {
			return valueText;
		}

		public void setValueText(String valueText) {
			this.valueText = valueText;
		}

		public String getBrandName() {
			return brandName;
		}

		public void setBrandName(String brandName) {
			this.brandName = brandName;
		}


		public String getProductTxt() {
			return productTxt;
		}

		public void setProductTxt(String productTxt) {
			this.productTxt = productTxt;
		}


		public String getId() {
			return id;
		}

		public String getProduct_TemplateName() {
			return product_TemplateName;
		}

		public void setProduct_TemplateName(String product_TemplateName) {
			this.product_TemplateName = product_TemplateName;
		}

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getSiteId() {
			return siteId;
		}

		public void setSiteId(String siteId) {
			this.siteId = siteId;
		}

		public String getProduct_TemplateId() {
			return product_TemplateId;
		}

		public void setProduct_TemplateId(String product_TemplateId) {
			this.product_TemplateId = product_TemplateId;
		}

		public String getProduct_WbUrl() {
			return product_WbUrl;
		}

		public void setProduct_WbUrl(String product_WbUrl) {
			this.product_WbUrl = product_WbUrl;
		}

		public String getProduct_Summary() {
			return product_Summary;
		}

		public void setProduct_Summary(String product_Summary) {
			this.product_Summary = product_Summary;
		}

		public String getProduct_KeyWords() {
			return product_KeyWords;
		}

		public void setProduct_KeyWords(String product_KeyWords) {
			this.product_KeyWords = product_KeyWords;
		}

		public String getCreated_Time() {
			return created_Time;
		}

		public void setCreated_Time(String created_Time) {
			this.created_Time = created_Time;
		}

		public String getUpdate_Time() {
			return update_Time;
		}

		public void setUpdate_Time(String update_Time) {
			this.update_Time = update_Time;
		}

		public String getProduct_Status() {
			return product_Status;
		}

		public void setProduct_Status(String product_Status) {
			this.product_Status = product_Status;
		}

		public String getTypeId() {
			return typeId;
		}

		public void setTypeId(String typeId) {
			this.typeId = typeId;
		}
		public String getProduct_description() {
			return product_description;
		}

		public void setProduct_description(String product_description) {
			this.product_description = product_description;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getNo() {
			return no;
		}

		public void setNo(String no) {
			this.no = no;
		}

		public String[] getProductRelevantIdList() {
			return productRelevantIdList;
		}

		public void setProductRelevantIdList(String[] productRelevantIdList) {
			this.productRelevantIdList = productRelevantIdList;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		public List<String> getImageids() {
			return imageids;
		}

		public void setImageids(List<String> imageids) {
			this.imageids = imageids;
		}

		public List<Image> getImageList() {
			return imageList;
		}

		public void setImageList(List<Image> imageList) {
			this.imageList = imageList;
		}

		public List<Image> getTimageList() {
			return timageList;
		}
		
		
		public void setTimageList(List<Image> timageList) {
			this.timageList = timageList;
		}

		public String getArea() {
			return area;
		}

		public void setArea(String area) {
			this.area = area;
		}

		public String getColumId() {
			return columId;
		}

		public void setColumId(String columId) {
			this.columId = columId;
		}
		

}
