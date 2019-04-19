package org.daijie.jdbc.jpa;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.config.DefaultRepositoryBaseClass;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;

import java.lang.annotation.*;

/**
 * 重写EnableJpaRepositories注解
 * 将导入JpaRepositoriesRegistrar改成JpaMultipleRepositoriesRegistrar
 * 基本属性与JpaRepositoriesRegistrar作用一样
 * @author daijie_jay
 * @since 2018年5月25日
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(JpaMultipleRepositoriesRegistrar.class)
@EnableAutoConfiguration
public @interface EnableJpaMultipleRepositories {

	String[] value() default {};

	String[] basePackages() default {};

	Class<?>[] basePackageClasses() default {};

	Filter[] includeFilters() default {};

	Filter[] excludeFilters() default {};

	String repositoryImplementationPostfix() default "Impl";

	String namedQueriesLocation() default "";

	Key queryLookupStrategy() default Key.CREATE_IF_NOT_FOUND;

	Class<?> repositoryFactoryBeanClass() default JpaRepositoryFactoryBean.class;

	Class<?> repositoryBaseClass() default DefaultRepositoryBaseClass.class;

	String entityManagerFactoryRef() default "entityManagerFactory";

	String transactionManagerRef() default "transactionManager";

	boolean considerNestedRepositories() default false;

	boolean enableDefaultTransactions() default true;
}
