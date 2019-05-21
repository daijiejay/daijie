package org.daijie.lock;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * AOP锁配置类
 * @author daijie
 * @since 2019/5/17
 */
@Import(LockAspect.class)
@Configuration
public class AspectLockAutoConfiguration {

//    @Bean
//    @ConditionalOnMissingBean
//    public LockHardler lockHardler(){
//        return new LockHardler();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public LockAdvisorAutoProxyCreator lockAdvisorAutoProxyCreator(){
//        return new LockAdvisorAutoProxyCreator();
//    }

//    @Bean
//    public JdkRegexpMethodPointcut jdkRegexpMethodPointcut() {
//        JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
//        pointcut.setPattern("@annotation(Lock)");
//        return pointcut;
//    }

//    @Bean
//    public LockAdvice lockAdvice() {
//        return new LockAdvice();
//    }
//
//    @Bean("regexpMethodPointcutAdvisor")
//    public RegexpMethodPointcutAdvisor regexpMethodPointcutAdvisor(LockAdvice lockAdvice) {
//        RegexpMethodPointcutAdvisor advisor = new RegexpMethodPointcutAdvisor();
//        advisor.setAdvice(lockAdvice);
//        advisor.setPatterns(".anontationLock.*");
//        return advisor;
//    }

//    @Bean
//    public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
//        BeanNameAutoProxyCreator proxyCreator = new BeanNameAutoProxyCreator();
//        proxyCreator.setInterceptorNames("regexpMethodPointcutAdvisor");
//        return proxyCreator;
//    }

//    @Bean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//        return new DefaultAdvisorAutoProxyCreator();
//    }

//    @Bean
//    public ProxyFactory proxyFactory(LockAdvice lockAdvice) {
//        ProxyFactory proxyFactory = new ProxyFactory();
//        proxyFactory.setTarget();
//        proxyFactory.addAdvice();
//        return proxyFactory;
//    }
}
