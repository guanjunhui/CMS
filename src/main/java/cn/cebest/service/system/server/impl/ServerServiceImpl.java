package cn.cebest.service.system.server.impl;

import java.io.File;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.springframework.stereotype.Service;

import com.google.common.io.Resources;

import cn.cebest.dao.DaoSupport;
import cn.cebest.entity.bo.UsageBo;
import cn.cebest.entity.vo.ServerInfoVo;
import cn.cebest.service.system.server.ServerService;
import cn.cebest.util.Const;
import cn.cebest.util.Logger;
import cn.cebest.util.Oscheck;
import cn.cebest.util.PageData;

@Service("serverService")
public class ServerServiceImpl implements ServerService{
	private static final Logger logger = Logger.getLogger(ServerServiceImpl.class);
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Override
	public ServerInfoVo getServerInfo() throws Exception {
		ServerInfoVo serverInfo = new ServerInfoVo();
		String ip=StringUtils.EMPTY;//ip
		try{
			ip=getLocalIP();
		}catch(Exception e){
			ip=StringUtils.EMPTY;
			logger.error("dont get the service host address!", e);
		}
		// computer name
		String computerName=StringUtils.EMPTY;
		String userDomain=StringUtils.EMPTY;;
		Map<String, String> sysInfo = System.getenv();
		try{
			computerName = sysInfo.get("COMPUTERNAME");
			// computer domain
			userDomain = sysInfo.get("USERDOMAIN");
		}catch(Exception e){
			computerName=StringUtils.EMPTY;
			userDomain=StringUtils.EMPTY;
			logger.error("does not get the service host computerName||userDomain!", e);
		}
		Sigar sigar = getSigar();
		
		//内存统计start
		UsageBo memory = new UsageBo();
		Mem mem = sigar.getMem();
		//memory total
		long total = mem.getTotal()/1024/1024;
		//memory used
		long used = mem.getUsed()/1024/1024;
		//memory free
		long free = mem.getFree()/1024/1024;
		//memory usedpercent
		double usedPercent = mem.getUsedPercent();
		
		memory.setTotalCount(total);
		memory.setUsageCount(used);
		memory.setFreeCount(free);
		memory.setUsagePercent(format(usedPercent));
		serverInfo.setMemory(memory);
		//内存统计end
		//Cpu start
		//cpu perc
		CpuPerc cpuPerc = sigar.getCpuPerc();  
		String combined = cpuPerc.format(cpuPerc.getCombined());
		UsageBo cpu = new UsageBo();
		cpu.setUsagePercent(combined);
		serverInfo.setCpu(cpu);
		//system file
		String path=getDataBasePath();
		FileSystem fslist[] = sigar.getFileSystemList();
		long systemTotal =0l;
		long systemFree=0l;
		long systemAvail=0l;
		long systemUsed=0l;
		for (int i=0;i<fslist.length;i++) {
			FileSystem fileSystem = fslist[i];
			if (fileSystem.getType()==Const.INT_2) {
				 logger.info("==fileSystem name:"+fileSystem.getDirName());
				 FileSystemUsage usage=sigar.getFileSystemUsage(fileSystem.getDirName());
				 systemTotal += usage.getTotal()/1024;
			     systemFree += usage.getFree()/1024;
				 systemAvail += usage.getAvail()/1024;
				 systemUsed += usage.getUsed()/1024;
			}
		}
		UsageBo disk = new UsageBo();
		disk.setTotalCount(systemTotal);
		disk.setUsageCount(systemUsed);
		disk.setFreeCount(systemFree);
		disk.setAvailCount(systemAvail);
		disk.setUsagePercent(format(systemUsed/systemTotal));
		serverInfo.setDisk(disk);
		
		List<PageData> dateBaseInfo = getDateBaseSize();
		BigDecimal index_size =(BigDecimal)dateBaseInfo.get(0).get("index_size");
		BigDecimal data_size = (BigDecimal)dateBaseInfo.get(0).get("data_size");
		BigDecimal dataBaseUsed=index_size.add(data_size);
		UsageBo dataBase = new UsageBo();
		dataBase.setUsageCount(dataBaseUsed.longValue());
		
	    serverInfo.setIpAddress(ip);
		serverInfo.setName(computerName);
		serverInfo.setDomain(userDomain);
		//Cpu end
		return serverInfo;
	}
	
	private List<PageData> getDateBaseSize() throws Exception{
		//dateBase size
		PageData pd=new PageData();
		List<PageData> list =(List<PageData>) dao.findForList("ServerMapper.dateBaseSize", pd);
		return list;
	}
	
	private String getDataBasePath() throws Exception{
		PageData pd=new PageData();
		PageData resultPath =(PageData) dao.findForObject("ServerMapper.dateBasePath", pd);
		//{Value=/usr/local/mysql/data/, Variable_name=datadir}
		String  path =(String)  resultPath.get("Value");
		return  path;
	}
	
	public Sigar getSigar(){
		try {
            String file = Resources.getResource("sigar/.sigar_shellrc").getFile();
            File classPath = new File(file).getParentFile();

            String path = System.getProperty("java.library.path");
            if (Oscheck.getOperatingSystemType() == Oscheck.OSType.Windows) {
                path += ";" + classPath.getCanonicalPath();
            } else {
                path += ":" + classPath.getCanonicalPath();
            }
            System.setProperty("java.library.path", path);

            return new Sigar();
        } catch (Exception e) {
            return null;
        } 
	}
	
	/**
	  * 获得主机IP
	  *
	  * @return String
	  */
	 public static boolean isWindowsOS(){
	    boolean isWindowsOS = false;
	    String osName = System.getProperty("os.name");
	    if(osName.toLowerCase().indexOf("windows")>-1){
	     isWindowsOS = true;
	    }
	    return isWindowsOS;
	  }

	  /**
	    * 获取本机ip地址，并自动区分Windows还是linux操作系统
	    * @return String
	    */
	  public static String getLocalIP(){
	    String sIP = "";
	    InetAddress ip = null; 
	    try {
	     //如果是Windows操作系统
	     if(isWindowsOS()){
	      ip = InetAddress.getLocalHost();
	     }
	     //如果是Linux操作系统
	     else{
	      boolean bFindIP = false;
	      Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
	        .getNetworkInterfaces();
	      while (netInterfaces.hasMoreElements()) {
	       if(bFindIP){
	        break;
	       }
	       NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
	       //----------特定情况，可以考虑用ni.getName判断
	       //遍历所有ip
	       Enumeration<InetAddress> ips = ni.getInetAddresses();
	       while (ips.hasMoreElements()) {
	        ip = (InetAddress) ips.nextElement();
	        if( ip.isSiteLocalAddress()  
	                   && !ip.isLoopbackAddress()   //127.开头的都是lookback地址
	                   && ip.getHostAddress().indexOf(":")==-1){
	            bFindIP = true;
	               break;  
	           }
	       }

	      }
	     }
	    }
	    catch (Exception e) {
	     e.printStackTrace();
	    }

	    if(null != ip){
	     sIP = ip.getHostAddress();
	    }
	    return sIP;
	  }

	  public String format(Object obj){
			DecimalFormat dFormat=new DecimalFormat(".##");
			String percent = dFormat.format(obj)+"%";
			return percent;
	  }
	  
}
