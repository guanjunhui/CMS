package cn.cebest.entity.system;

import java.io.Serializable;
import java.util.Date;

public class AppUserExt implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId;//会员ID

    private String memRealName;//姓名

    private String memNick;//昵称

    private String memSex;//性别(enum: 1男 2女 3不详)

    private Date memBirthday;//出生日期

    private String memIdcard;//身份证号码

    private String memCity;//所在城市

    private String memAddr;//详细地址

    private String memTel;//联系电话

    private String memImgPath;//头像

    private String memSign;//个性签名

    private String memIntro;//自我介绍

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getMemRealName() {
        return memRealName;
    }

    public void setMemRealName(String memRealName) {
        this.memRealName = memRealName == null ? null : memRealName.trim();
    }

    public String getMemNick() {
        return memNick;
    }

    public void setMemNick(String memNick) {
        this.memNick = memNick == null ? null : memNick.trim();
    }

    public String getMemSex() {
        return memSex;
    }

    public void setMemSex(String memSex) {
        this.memSex = memSex == null ? null : memSex.trim();
    }

    public Date getMemBirthday() {
        return memBirthday;
    }

    public void setMemBirthday(Date memBirthday) {
        this.memBirthday = memBirthday;
    }

    public String getMemIdcard() {
        return memIdcard;
    }

    public void setMemIdcard(String memIdcard) {
        this.memIdcard = memIdcard == null ? null : memIdcard.trim();
    }

    public String getMemCity() {
        return memCity;
    }

    public void setMemCity(String memCity) {
        this.memCity = memCity == null ? null : memCity.trim();
    }

    public String getMemAddr() {
        return memAddr;
    }

    public void setMemAddr(String memAddr) {
        this.memAddr = memAddr == null ? null : memAddr.trim();
    }

    public String getMemTel() {
        return memTel;
    }

    public void setMemTel(String memTel) {
        this.memTel = memTel == null ? null : memTel.trim();
    }

    public String getMemImgPath() {
        return memImgPath;
    }

    public void setMemImgPath(String memImgPath) {
        this.memImgPath = memImgPath == null ? null : memImgPath.trim();
    }

    public String getMemSign() {
        return memSign;
    }

    public void setMemSign(String memSign) {
        this.memSign = memSign == null ? null : memSign.trim();
    }

    public String getMemIntro() {
        return memIntro;
    }

    public void setMemIntro(String memIntro) {
        this.memIntro = memIntro == null ? null : memIntro.trim();
    }
}