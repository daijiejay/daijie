package org.daijie.common.bean;

import org.springframework.beans.factory.FactoryBean;

/**
 * 代理类构建工厂
 * @author daijie_jay
 * @since 2019年11月18日
 */
public abstract class AbstractFactoryBean<T> implements FactoryBean<T> {

    private Class<T> interfaceClass;

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    @Override
    public Class<?> getObjectType() {
        return this.interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
