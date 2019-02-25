package cn.cebest.portal.common.resolve.fetchshaoyin;

import java.util.List;

/**
 * Created by lhb on 2018/5/29.
 */
public class ReviewMainVO {
    //全部评论总数
    private String totalReviewCount="";
    //全部评论第一页，星标比例 class="arp-rating-out-of-text"
    private String starCount="";
   //全部评论5星比例 class="a-size-small a-link-normal 5star histogram-review-count"
    private String star5="";
    //全部评论4星比例 class="a-size-small a-link-normal 4star histogram-review-count"
    private String star4="";
    //全部评论3星比例 class="a-size-small a-link-normal 3star histogram-review-count"
    private String star3="";
    //全部评论2星比例 class="a-size-small a-link-normal 2star histogram-review-count"
    private String star2="";
    //全部评论1星比例 class="a-size-small a-link-normal 1star histogram-review-count"
    private String star1="";
    //总页数
    private String pagenum="";
    //评论内容列表
    private List<ReviewVO> reviews;

    public List<ReviewVO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewVO> reviews) {
        this.reviews = reviews;
    }

    public String getTotalReviewCount() {

        return totalReviewCount;
    }

    public void setTotalReviewCount(String totalReviewCount) {
        this.totalReviewCount = totalReviewCount;
    }

    public String getStarCount() {
        return starCount;
    }

    public void setStarCount(String starCount) {
        this.starCount = starCount;
    }

    public String getStar5() {
        return star5;
    }

    public void setStar5(String star5) {
        this.star5 = star5;
    }

    public String getStar4() {
        return star4;
    }

    public void setStar4(String star4) {
        this.star4 = star4;
    }

    public String getStar3() {
        return star3;
    }

    public void setStar3(String star3) {
        this.star3 = star3;
    }

    public String getStar2() {
        return star2;
    }

    public void setStar2(String star2) {
        this.star2 = star2;
    }

    public String getStar1() {
        return star1;
    }

    public void setStar1(String star1) {
        this.star1 = star1;
    }

    public String getPagenum() {
        return pagenum;
    }

    public void setPagenum(String pagenum) {
        this.pagenum = pagenum;
    }


}
