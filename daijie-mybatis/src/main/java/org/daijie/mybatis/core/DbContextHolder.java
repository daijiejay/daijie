package org.daijie.mybatis.core;


/**
 * 主从设置
 * @author daijie_jay
 * @date 2017年11月20日
 */
public class DbContextHolder {

    public enum DbType{
        MASTER,SLAVE
    }

    private static final ThreadLocal<DbType> contextHolder = new ThreadLocal<>();

    public static void setDbType(DbType dbType){
        if(dbType==null)throw new NullPointerException();
        contextHolder.set(dbType);
    }

    public static DbType getDbType(){
        return contextHolder.get()==null?DbType.MASTER:contextHolder.get();
    }

    public static void clearDbType(){
        contextHolder.remove();
    }

}
