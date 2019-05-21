package org.daijie.common.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring容器bean工具类
 * 
 * @author daijie
 */
public class ApplicationContextHolder implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;     

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextHolder.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取对象
	 *
	 * @param name bean注册名
	 * @return Object 一个以所给名字注册的bean的实例
	 * @throws BeansException 抛出异常
	 */
	public static Object getBean(String name) throws BeansException {
		return applicationContext.getBean(name);
	}

	/**
	 * 获取类型为requiredType的对象
	 * 如果bean不能被类型转换，相应的异常将会被抛出（BeanNotOfRequiredTypeException）
	 *
	 * @param name         bean注册名
	 * @param requiredType 返回对象类型
	 * @return Object 返回requiredType类型对象
	 * @throws BeansException 抛出异常
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object getBean(String name, Class requiredType) throws BeansException {
		return applicationContext.getBean(name, requiredType);
	}

	/**
	 * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
	 *
	 * @param name bean注册名
	 * @return boolean 是否匹配成功
	 */
	public static boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}

	/**
	 * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
	 * 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
	 *
	 * @param name bean注册名
	 * @return boolean 是否单例
	 * @throws NoSuchBeanDefinitionException 抛出异常
	 */
	public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		return applicationContext.isSingleton(name);
	}

	/**
	 * @param name bean注册名
	 * @return Class 注册对象的类型
	 * @throws NoSuchBeanDefinitionException 抛出异常
	 */
	@SuppressWarnings("rawtypes")
	public static Class getType(String name) throws NoSuchBeanDefinitionException {
		return applicationContext.getType(name);
	}

	/**
	 * 如果给定的bean名字在bean定义中有别名，则返回这些别名
	 *
	 * @param name bean注册名
	 * @return String[] 别名
	 * @throws NoSuchBeanDefinitionException 抛出异常
	 */
	public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
		return applicationContext.getAliases(name);
	}
}