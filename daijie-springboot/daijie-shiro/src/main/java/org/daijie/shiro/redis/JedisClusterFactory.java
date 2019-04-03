package org.daijie.shiro.redis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * 集群redis实例工厂
 * @author daijie
 * @since 2017年6月22日
 */
public class JedisClusterFactory extends JedisCluster implements InitializingBean {

	private Resource addressConfig;
	private String addressKeyPrefix;
	private JedisCluster jedisCluster;
	private String password;
	private Integer timeout;
	private Integer connectionTimeout;
	private GenericObjectPoolConfig genericObjectPoolConfig;
	private Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");
	
	public JedisClusterFactory(HostAndPort node) {
		super(node);
	}

	public JedisCluster getObject() throws Exception {
		return this.jedisCluster;
	}

	public Class<? extends JedisCluster> getObjectType() {
		return this.jedisCluster != null?this.jedisCluster.getClass():JedisCluster.class;
	}

	public boolean isSingleton() {
		return true;
	}

	private Set<HostAndPort> parseHostAndPort() throws Exception {
		try {
			Properties ex = new Properties();
			ex.load(this.addressConfig.getInputStream());
			HashSet<HostAndPort> haps = new HashSet<HostAndPort>();
			Iterator<?> i$ = ex.keySet().iterator();

			while(i$.hasNext()) {
				Object key = i$.next();
				if(((String)key).startsWith(this.addressKeyPrefix)) {
					String val = (String)ex.get(key);
					boolean isIpPort = this.p.matcher(val).matches();
					if(!isIpPort) {
						throw new IllegalArgumentException("ip 或 port 不合法");
					}

					String[] ipAndPort = val.split(":");
					HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
					haps.add(hap);
				}
			}

			return haps;
		} catch (IllegalArgumentException var9) {
			throw var9;
		} catch (Exception var10) {
			throw new Exception("解析 jedis 配置文件失败", var10);
		}
	}

	public void afterPropertiesSet() throws Exception {
		Set<HostAndPort> haps = this.parseHostAndPort();
		this.jedisCluster = new JedisCluster(haps, connectionTimeout, this.timeout.intValue(), maxAttempts, password, this.genericObjectPoolConfig);
	}

	public void setAddressConfig(Resource addressConfig) {
		this.addressConfig = addressConfig;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setTimeout(int timeout) {
		this.timeout = Integer.valueOf(timeout);
	}

	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public void setAddressKeyPrefix(String addressKeyPrefix) {
		this.addressKeyPrefix = addressKeyPrefix;
	}

	public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
		this.genericObjectPoolConfig = genericObjectPoolConfig;
	}

}
