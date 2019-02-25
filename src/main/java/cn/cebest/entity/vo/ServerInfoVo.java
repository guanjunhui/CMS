package cn.cebest.entity.vo;

import cn.cebest.entity.bo.UsageBo;

/**
 * 
* 类名称：服务器信息
* 类描述： 
* @author qichangxin
* 作者单位： 
* 联系方式：qichangxin@300.cn
* 创建时间：2017年12月13日
* @version 1.0
 */
public class ServerInfoVo {
	
	private String name;
	private String ipAddress;
	private String domain;
	private int port;
	
	private UsageBo cpu;
	private UsageBo memory;
	private UsageBo dataBase;
	private UsageBo disk;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public UsageBo getCpu() {
		return cpu;
	}
	public void setCpu(UsageBo cpu) {
		this.cpu = cpu;
	}
	public UsageBo getMemory() {
		return memory;
	}
	public void setMemory(UsageBo memory) {
		this.memory = memory;
	}
	public UsageBo getDataBase() {
		return dataBase;
	}
	public void setDataBase(UsageBo dataBase) {
		this.dataBase = dataBase;
	}
	public UsageBo getDisk() {
		return disk;
	}
	public void setDisk(UsageBo disk) {
		this.disk = disk;
	}
}
