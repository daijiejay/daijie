package org.daijie.shiro.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.kisso.common.util.EnvUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取request的IP、MAC和城市地址
 * @author daijie
 * @since 2017年6月5日
 */
public class MacAddressUtil {
	public static String callCmd(String[] cmd) {
		String result = "";
		String line = "";
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
			InputStreamReader is = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(is);
			while ((line = br.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param cmd     第一个命令
	 * @param another 第二个命令
	 * @return 第二个命令的执行结果
	 */
	public static String callCmd(String[] cmd, String[] another) {
		String result = "";
		String line = "";
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(cmd);
			proc.waitFor(); //已经执行完第一个命令，准备执行第二个命令
			proc = rt.exec(another);
			InputStreamReader is = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(is);
			while ((line = br.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return result;
	}


	/**
	 * @param ip           目标ip,一般在局域网内
	 * @param sourceString 命令处理的结果字符串
	 * @param macSeparator mac分隔符号
	 * @return mac地址，用上面的分隔符号表示
	 */
	public static String filterMacAddress(final String ip, final String sourceString, final String macSeparator) {
		String result = "";
		String regExp = "((([0-9,A-F,a-f]{1,2}" + macSeparator + "){1,5})[0-9,A-F,a-f]{1,2})";
		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(sourceString);
		while (matcher.find()) {
			result = matcher.group(1);
			if (sourceString.indexOf(ip) <= sourceString.lastIndexOf(matcher.group(1))) {
				break; //如果有多个IP,只匹配本IP对应的Mac.
			}
		}

		return result;
	}

	/**
	 * @param ip 目标ip
	 * @return Mac Address
	 */
	public static String getMacInWindows(final String ip) {
		String result = "";
		String[] cmd = {
				"cmd",
				"/c",
				"ping " + ip
		};
		String[] another = {
				"cmd",
				"/c",
				"arp -a"
		};

		String cmdResult = callCmd(cmd, another);
		result = filterMacAddress(ip, cmdResult, "-");

		return result;
	}

	/**
	 * @param ip 目标ip
	 * @return Mac Address
	 */
	public static String getMacInLinux(final String ip) {
		String result = "";
		String[] cmd = {
				"/bin/sh",
				"-c",
				"ping " + ip + " -c 2 && arp -a"
		};
		String cmdResult = callCmd(cmd);
		result = filterMacAddress(ip, cmdResult, ":");

		return result;
	}

	/**
	 * 获取MAC地址
	 *
	 * @param ip ip地址
	 * @return 返回MAC地址
	 */
	public static String getMacAddress(String ip) {
		String macAddress = "";
		if (EnvUtil.isLinux()) {
			macAddress = getMacInLinux(ip).trim();
		}
		if (macAddress == null || "".equals(macAddress)) {
			macAddress = getMacInWindows(ip).trim();
		}
		return macAddress;
	}
	
	/**
	 * 通过HttpServletRequest返回IP地址
	 * @param request HttpServletRequest
	 * @return ip String
	 * @throws Exception 抛出异常
	 */
	public static String getIpAddr(HttpServletRequest request) throws Exception {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
	    if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
	    	ipAddress = request.getHeader("HTTP_CLIENT_IP");
	    }
	    if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
	    	ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
	    }
		
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if(ipAddress.startsWith("0")){
				ipAddress = "127.0.0.1";
			}
			if (ipAddress.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}

		}

		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
	     return ipAddress;    
	}

	/**
	 * 将IP地址转为 省市区
	 * @param ip Ip地址
	 * @return String
	 */
	public static String ip2Location(String ip) {
	        String result = "";
	        try {
	        	
	            URLConnection connection = new URL("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=" + ip).openConnection();
	            connection.getInputStream();
	            connection.setConnectTimeout(5000);
	            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
	                String s;
	                while ((s = br.readLine()) != null) {
	                    result += s;
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        String json = result.substring(result.indexOf("=") + 1, result.length() - 1);
	        JSONObject jsonObject = JSONUtil.parseObj(json);
	        StringBuilder location = new StringBuilder();
	        if (jsonObject.getStr("country") != null) {
	            location.append(jsonObject.getStr("country"));
	        }
	        if (jsonObject.getStr("province") != null) {
	            location.append(jsonObject.getStr("province"));
	        }
	        if (jsonObject.getStr("city") != null) {
	            location.append(jsonObject.getStr("city"));
	        }
	        return location.toString();
	 }      

	 /**
	 * 通过IP地址获取MAC地址
	 * @param ip String,127.0.0.1格式
	 * @return mac String
	 * @throws Exception 抛出异常
	 */
	public static String getMACAddress1(String ip) throws Exception {
	    String line = "";
	    String macAddress = "";
	    final String MAC_ADDRESS_PREFIX = "MAC Address = ";
	    final String LOOPBACK_ADDRESS = "127.0.0.1";
	    //如果为127.0.0.1,则获取本地MAC地址。
	    if (LOOPBACK_ADDRESS.equals(ip)) {
	        InetAddress inetAddress = InetAddress.getLocalHost();
	        //貌似此方法需要JDK1.6。
	        byte[] mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
	        //下面代码是把mac地址拼装成String
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < mac.length; i++) {
	            if (i != 0) {
	                sb.append("-");
	            }
	            //mac[i] & 0xFF 是为了把byte转化为正整数
	            String s = Integer.toHexString(mac[i] & 0xFF);
	            sb.append(s.length() == 1 ? 0 + s : s);
	        }
	        //把字符串所有小写字母改为大写成为正规的mac地址并返回
	        macAddress = sb.toString().trim().toUpperCase();
	        return macAddress;
	    }
	    //获取非本地IP的MAC地址
	    try {
	        Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
	        InputStreamReader isr = new InputStreamReader(p.getInputStream());
	        BufferedReader br = new BufferedReader(isr);
	        while ((line = br.readLine()) != null) {
	            if (line != null) {
	                int index = line.indexOf(MAC_ADDRESS_PREFIX);
	                if (index != -1) {
	                    macAddress = line.substring(index + MAC_ADDRESS_PREFIX.length()).trim().toUpperCase();
	                }
	            }
	        }
	        br.close();
	    } catch (IOException e) {
	        e.printStackTrace(System.out);
	    }
	    return macAddress;
	}

}