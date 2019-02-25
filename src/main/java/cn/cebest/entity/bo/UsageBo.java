package cn.cebest.entity.bo;

public class UsageBo {

	private Long totalCount;//总量
	private Long usageCount;//使用量
	private Long freeCount;//剩余量
	private Long availCount;//可用量
	private String usagePercent;//使用率
	
	public Long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	public Long getUsageCount() {
		return usageCount;
	}
	public void setUsageCount(Long usageCount) {
		this.usageCount = usageCount;
	}
	public Long getFreeCount() {
		return freeCount;
	}
	public void setFreeCount(Long freeCount) {
		this.freeCount = freeCount;
	}
	public String getUsagePercent() {
		return usagePercent;
	}
	public void setUsagePercent(String usagePercent) {
		this.usagePercent = usagePercent;
	}
	public Long getAvailCount() {
		return availCount;
	}
	public void setAvailCount(Long availCount) {
		this.availCount = availCount;
	}
	
}
