package org.daijie.jdbc;

import org.daijie.common.bean.AbstractFactoryBean;
import org.daijie.jdbc.session.SessionMapperManager;

/**
 * Mapper类构建工厂
 * @author daijie_jay
 * @since 2019年11月18日
 */
public class MapperFactoryBean<T> extends AbstractFactoryBean<T> {

    @Override
    public T getObject() throws Exception {
        return SessionMapperManager.createSessionMapper(this.getInterfaceClass());
    }
}
