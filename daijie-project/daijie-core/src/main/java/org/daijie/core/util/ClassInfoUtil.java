package org.daijie.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 获取类信息
 * @author daijie
 * @since 2019/5/30
 */
public class ClassInfoUtil {

    /***
     * 获取指定类的第一个泛型对象类型
     * @param clazz 指定类
     * @return Class
     */
    public static Class getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 获取指定类的第几个泛型对象类型
     * @param clazz 指定类
     * @param index 第几个
     * @return Class
     * @throws IndexOutOfBoundsException
     */
    public static Class getSuperClassGenricType(Class clazz, int index)
            throws IndexOutOfBoundsException {
        Type[] types = clazz.getGenericInterfaces();
        if (types.length > 0) {
            Type type = types[0];
            return getSuperClassGenricType(type, index);
        }
        return Object.class;
    }

    /***
     * 获取指定类的第一个泛型对象类型
     * @param type 指定类的类型
     * @return Class
     */
    public static Class getSuperClassGenricType(Type type) {
        return getSuperClassGenricType(type, 0);
    }

    /**
     * 获取指定类的第几个泛型对象类型
     * @param type 指定类的类型
     * @param index 第几个
     * @return Class
     * @throws IndexOutOfBoundsException
     */
    public static Class getSuperClassGenricType(Type type, int index)
            throws IndexOutOfBoundsException {
        if (type instanceof ParameterizedType) {
            Type[] params = ((ParameterizedType) type).getActualTypeArguments();
            if (params.length > 0) {
                if (params[index] instanceof Class) {
                    return (Class) params[index];
                }
            }
        }
        return Object.class;
    }
}
