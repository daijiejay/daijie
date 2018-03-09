package org.daijie.core.lock;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.daijie.core.factory.RegisterBeanFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.annotation.AspectJAdvisorFactory;
import org.springframework.aop.aspectj.annotation.BeanFactoryAspectInstanceFactory;
import org.springframework.aop.aspectj.annotation.MetadataAwareAspectInstanceFactory;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * 注册代理锁
 * @author daijie_jay
 * @since 2018年3月9日
 */
public class LockAdvisorAutoProxyCreator extends AbstractAdvisorAutoProxyCreator implements RegisterBeanFactory {

	private static final long serialVersionUID = 4432491876357640679L;

	private AspectJAdvisorFactory aspectJAdvisorFactory;

	private final Set<String> targetSourcedBeans =
			Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(16));

	private final Set<Object> earlyProxyReferences =
			Collections.newSetFromMap(new ConcurrentHashMap<Object, Boolean>(16));

	private final Map<Object, Class<?>> proxyTypes = new ConcurrentHashMap<Object, Class<?>>(16);

	private final Map<Object, Boolean> advisedBeans = new ConcurrentHashMap<Object, Boolean>(256);
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean != null) {
			Object cacheKey = getCacheKey(bean.getClass(), beanName);
			if (!this.earlyProxyReferences.contains(cacheKey)) {
				return proxy(bean, beanName, cacheKey);
			}
			return super.postProcessAfterInitialization(bean, beanName);
		}
		return bean;
	}
	
	public Object proxy(Object bean, String beanName, Object cacheKey){
		if (beanName != null && this.targetSourcedBeans.contains(beanName)) {
			return bean;
		}
		if (Boolean.FALSE.equals(this.advisedBeans.get(cacheKey))) {
			return bean;
		}
		if (isInfrastructureClass(bean.getClass()) || shouldSkip(bean.getClass(), beanName)) {
			this.advisedBeans.put(cacheKey, Boolean.FALSE);
			return bean;
		}
		MetadataAwareAspectInstanceFactory factory =
				new BeanFactoryAspectInstanceFactory(this.getBeanFactory(), beanName);
		List<Advisor> advisors = new LinkedList<Advisor>();
		for (Method method : bean.getClass().getMethods()) {
			Advisor advisor = aspectJAdvisorFactory.getAdvisor(method, factory, advisors.size(), "lockHardler");
			if (advisor != null) {
				advisors.add(advisor);
			}
		}
		Object[] specificInterceptors = advisors.toArray();
		if (specificInterceptors != DO_NOT_PROXY) {
			this.advisedBeans.put(cacheKey, Boolean.TRUE);
			Object proxy = createProxy(
					bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));
			this.proxyTypes.put(cacheKey, proxy.getClass());
			return proxy;
		}

		this.advisedBeans.put(cacheKey, Boolean.FALSE);
		return bean;
	}

	public void setAspectJAdvisorFactory(AspectJAdvisorFactory aspectJAdvisorFactory) {
		Assert.notNull(aspectJAdvisorFactory, "AspectJAdvisorFactory must not be null");
		this.aspectJAdvisorFactory = aspectJAdvisorFactory;
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(LockHardler.class);
		registerBean("lockHardler", builder, registry);
	}
}
