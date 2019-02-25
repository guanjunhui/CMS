package cn.cebest.entity.vo;

public class PvInfo {

	private String date;//日期
	private Long pm;//浏览量
	private Long uv;//访客数
	private Long ip;//IP
	
	public Long getPm() {
		return pm;
	}
	public void setPm(Long pm) {
		this.pm = pm;
	}
	public Long getUv() {
		return uv;
	}
	public void setUv(Long uv) {
		this.uv = uv;
	}
	public Long getIp() {
		return ip;
	}
	public void setIp(Long ip) {
		this.ip = ip;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
