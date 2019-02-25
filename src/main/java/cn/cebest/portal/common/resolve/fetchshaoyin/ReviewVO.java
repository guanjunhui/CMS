package cn.cebest.portal.common.resolve.fetchshaoyin;



/**
 * Created by lhb on 2018/5/29.
 */
public class ReviewVO {
    //评论概要a-size-base a-link-normal review-title a-color-base a-text-bold
    private String gaiyao="";
    //评论星标 a-icon-alt
    private String stars="";
    //评论作者 a-size-base a-link-normal author
    private String author="";
    //评论日期 a-size-base a-color-secondary review-date
    private String reviewDate="";
    //评论日期格式化成中文日期，排序使用
    private String reviewDateCh="";
	//购买颜色 a-size-mini a-link-normal a-color-secondary
    private String reviewColour="";
    //颜色说明 a-size-mini a-color-state a-text-bold
    private String textColour="";
    //评论主体 a-size-base review-text
    private String reviewText="";
    //评论有用数量 a-size-base a-color-secondary cr-vote-text
    private String voteText="";

    public String getGaiyao() {
        return gaiyao;
    }

    public void setGaiyao(String gaiyao) {
        this.gaiyao = gaiyao;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }
    
    public String getReviewDateCh() {
		return reviewDateCh;
	}

	public void setReviewDateCh(String reviewDateCh) {
		this.reviewDateCh = reviewDateCh;
	}

    public String getReviewColour() {
        return reviewColour;
    }

    public void setReviewColour(String reviewColour) {
        this.reviewColour = reviewColour;
    }

    public String getTextColour() {
        return textColour;
    }

    public void setTextColour(String textColour) {
        this.textColour = textColour;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getVoteText() {
        return voteText;
    }

    public void setVoteText(String voteText) {
        this.voteText = voteText;
    }



}
