package org.daijie.jdbc.scripting;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 查询条件包装，可以设置多表关联查询
 * @author daijie
 * @since 2019/6/27
 */
public class MultiWrapper {

    /**
     * 多表关联查询时，两个表关联字段的分隔符
     */
    protected static final String EQUAL_FIX = ":";

    /**
     * 多表关联查询包装类构建类
     */
    private MultiWrapperBuilder wrapperBuilder;

    /**
     * 多表关联查询对应的映射对象集合
     */
    private Set<Class> entityClasses = Sets.newHashSet();

    /**
     * 构建多表关联查询包装类
     * @param wrapperBuilder 多表关联查询包装类构建类
     */
    public MultiWrapper(MultiWrapperBuilder wrapperBuilder) {
        this.wrapperBuilder = wrapperBuilder;
        this.entityClasses.add(this.wrapperBuilder.getEntityClass());
    }

    /**
     * 实例多表关联查询包装类
     * @param entityClass 查询表对应的映射对象
     * @return 多表关联查询包装类
     */
    public static MultiWrapper newWrapper(Class<?> entityClass) {
        return newWrapper(entityClass, null);
    }

    /**
     * 实例多表关联查询包装类
     * @param entityClass 查询表对应的映射对象
     * @param whereWrapper 查询表对应的映射对象的条件包装类
     * @return 多表关联查询包装类
     */
    public static MultiWrapper newWrapper(Class<?> entityClass, Wrapper whereWrapper) {
        return new MultiWrapper(new MultiWrapper.MultiWrapperBuilder(entityClass, whereWrapper));
    }

    /**
     * 添加关联查询映射对象
     * @param entityClass 查询表对应的映射对象
     * @return 关联查询属性设置
     */
    public JoinWrapper andJoin(Class<?> entityClass) {
        return andJoin(entityClass, null);
    }

    /**
     * 添加关联查询映射对象
     * @param entityClass 查询表对应的映射对象
     * @param whereWrapper 查询表对应的映射对象的条件包装类
     * @return 关联查询属性设置
     */
    public JoinWrapper andJoin(Class<?> entityClass, Wrapper whereWrapper) {
        this.entityClasses.add(entityClass);
        return this.wrapperBuilder.andJoin(entityClass, whereWrapper, this).join();
    }

    /**
     * 添加左连接关联查询映射对象
     * @param entityClass 查询表对应的映射对象
     * @return 关联查询ON条件语句属性设置
     */
    public OnEqual andLeftJoin(Class<?> entityClass) {
        return andLeftJoin(entityClass, null);
    }

    /**
     * 添加左连接关联查询映射对象
     * @param entityClass 查询表对应的映射对象
     * @param whereWrapper 查询表对应的映射对象的条件包装类
     * @return 关联查询ON条件语句属性设置
     */
    public OnEqual andLeftJoin(Class<?> entityClass, Wrapper whereWrapper) {
        this.entityClasses.add(entityClass);
        return this.wrapperBuilder.andJoin(entityClass, whereWrapper, this).leftJoin();
    }

    /**
     * 添加右连接关联查询映射对象
     * @param entityClass 查询表对应的映射对象
     * @return 关联查询ON条件语句属性设置
     */
    public OnEqual andRightJoin(Class<?> entityClass) {
        return andRightJoin(entityClass, null);
    }

    /**
     * 添加右连接关联查询映射对象
     * @param entityClass 查询表对应的映射对象
     * @param whereWrapper 查询表对应的映射对象的条件包装类
     * @return 关联查询ON条件语句属性设置
     */
    public OnEqual andRightJoin(Class<?> entityClass, Wrapper whereWrapper) {
        this.entityClasses.add(entityClass);
        return this.wrapperBuilder.andJoin(entityClass, whereWrapper, this).rightJoin();
    }

    /**
     * 添加内连接关联查询映射对象
     * @param entityClass 查询表对应的映射对象
     * @return 关联查询ON条件语句属性设置
     */
    public OnEqual andInnerJoin(Class<?> entityClass) {
        return andInnerJoin(entityClass, null);
    }

    /**
     * 添加内连接关联查询映射对象
     * @param entityClass 查询表对应的映射对象
     * @param whereWrapper 查询表对应的映射对象的条件包装类
     * @return 关联查询ON条件语句属性设置
     */
    public OnEqual andInnerJoin(Class<?> entityClass, Wrapper whereWrapper) {
        this.entityClasses.add(entityClass);
        return this.wrapperBuilder.andJoin(entityClass, whereWrapper, this).innerJoin();
    }

    /**
     * 获取多表关联查询包装类构建类
     * @return 多表关联查询包装类构建类
     */
    protected MultiWrapperBuilder getWrapperBuilder() {
        return wrapperBuilder;
    }

    /**
     * 获取多表关联查询对应的映射对象集合
     * @return 多表关联查询对应的映射对象集合
     */
    public Set<Class> getEntityClasses() {
        return entityClasses;
    }

    /**
     * 多表关联查询包装类构建类，封装连接查询方式、查询条件、排序、分组、分页的相关信息
     */
    public static class MultiWrapperBuilder {

        /**
         * 主表对应的映射对象
         */
        private Class entityClass;

        /**
         * 每个表映射对象对应的连接方式及关联字段设置集合
         */
        private Map<Class, JoinCondition> joining = Maps.newHashMap();

        /**
         * 每个表映射对象对应的关联条件集合
         */
        private Map<Class, Wrapper> wrapping =  Maps.newHashMap();

        /**
         * 构建多表关联查询的包装构建器
         * @param entityClass 主表对应的映射对象
         * @param whereWrapper 主表对应的条件包装
         */
        protected MultiWrapperBuilder(Class<?> entityClass, Wrapper whereWrapper) {
            if (whereWrapper == null) {
                whereWrapper = Wrapper.newWrapper();
            }
            this.entityClass = entityClass;
            this.wrapping.put(entityClass, whereWrapper);
        }

        /**
         *  添加多表关联查询的单个表映射对象及这个表对应的条件
         * @param entityClass 表对应的映射对象
         * @param whereWrapper 表对应的条件包装
         * @param multiWrapper 多表关联查询包装类，用于把装包装类传递到下个类方法中设置更多的条件
         * @return
         */
        protected JoinCondition andJoin(Class<?> entityClass, Wrapper whereWrapper, MultiWrapper multiWrapper) {
            if (whereWrapper == null) {
                whereWrapper = Wrapper.newWrapper();
            }
            JoinCondition joinCondition = new JoinCondition(multiWrapper);
            this.joining.put(entityClass, joinCondition);
            this.wrapping.put(entityClass, whereWrapper);
            return joinCondition;
        }

        protected Class getEntityClass() {
            return entityClass;
        }

        protected Map<Class, JoinCondition> getJoining() {
            return joining;
        }

        protected Map<Class, Wrapper> getWrapping() {
            return wrapping;
        }

        /**
         * 这个语句中是否有配置多表的查询条件
         * @return 布尔值
         */
        protected boolean isCondition() {
            for (Class clz : this.wrapping.keySet()) {
                if (!this.wrapping.get(clz).getWrapperBuilder().getConditions().isEmpty()) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 多表关联查询条件信息封装类
     */
    public static class JoinCondition {

        /**
         * 关联查询类型
         */
        private JoinType joinType;

        /**
         * 用于左，右，内连接后的ON相等条件封装类
         */
        private OnEqual onEqual;

        /**
         * 用于普通多表关联查询配置参数的包装类
         */
        private JoinWrapper joinWrapper;

        /**
         * 用于左，右，内连接多表关联查询配置参数的包装类
         */
        private JoinWayWrapper joinWayWrapper;

        /**
         * 构建多表关联查询条件信息封装类
         * @param multiWrapper 多表关联查询包装类，用于把装包装类传递到下个类方法中设置更多的条件
         */
        protected JoinCondition(MultiWrapper multiWrapper) {
            this.joinWrapper = new JoinWrapper(multiWrapper);
            this.joinWayWrapper = new JoinWayWrapper(multiWrapper);
            this.onEqual = new OnEqual(this.joinWayWrapper);
        }

        /**
         * 获取当前映射表对应关联查询类型
         * @return 当前映射表对应关联查询类型
         */
        protected JoinType getJoinType() {
            return this.joinType;
        }

        /**
         * 获取当前映射表对应ON条件的参数封装对象
         * @return 当前映射表对应ON条件的参数封装对象
         */
        protected OnCondition getOnCondition() {
            return this.onEqual.getOnCondition();
        }

        /**
         * 设置当前映射表对应的普通连接类型
         * @return 多表连接配置类
         */
        protected JoinWrapper join() {
            this.joinType = JoinType.COMMON;
            return this.joinWrapper;
        }

        /**
         * 设置当前映射表对应的左连接类型
         * @return 多表连接配置类
         */
        protected OnEqual leftJoin() {
            this.joinType = JoinType.LEFT;
            return this.onEqual;
        }

        /**
         * 设置当前映射表对应的右连接类型
         * @return 多表连接配置类
         */
        protected OnEqual rightJoin() {
            this.joinType = JoinType.RIGHT;
            return this.onEqual;
        }

        /**
         * 设置当前映射表对应的内连接类型
         * @return 多表连接配置类
         */
        protected OnEqual innerJoin() {
            this.joinType = JoinType.INNER;
            return this.onEqual;
        }
    }

    /**
     * 多表关联查询ON相等条件配置类
     */
    public static class OnEqual {

        /**
         * 多表关联查询ON条件参数包装类
         */
        private OnCondition onCondition;

        /**
         * 构建多表关联查询ON相等条件配置类
         * @param joinWayWrapper 左、右、内连接配置类
         */
        protected OnEqual(JoinWayWrapper joinWayWrapper) {
            this.onCondition = new OnCondition(joinWayWrapper);
        }

        /**
         * 获取多表关联查询ON条件参数包装类
         * @return 多表关联查询ON条件参数包装类
         */
        protected OnCondition getOnCondition() {
            return this.onCondition;
        }

        /**
         * 配置多表关联查询ON条件相等字段
         * @param previousEntityClass 之前已配置的表映射对象
         * @param previousProperty 之前已配置的表映射对象的字段名
         * @param property 当前配置的表映射对象的字段名
         * @return 多表关联查询ON条件参数包装类
         */
        public OnCondition onEqual(Class  previousEntityClass, String previousProperty, String property) {
            this.onCondition.andEqual(previousEntityClass, previousProperty, property);
            return this.onCondition;
        }
    }

    /**
     * 多表关联查询ON条件参数包装类
     */
    public static class OnCondition {

        /**
         * 当前表映射对象ON条件对应多个表映射对象相等字段名
         */
        private Map<Class, List<String>> equaling =  Maps.newHashMap();

        /**
         * 当前表映射对象ON条件包装
         */
        private Wrapper wrapper;

        /**
         * joinWayWrapper 左、右、内连接配置类
         */
        private JoinWayWrapper joinWayWrapper;

        /**
         * 构建多表关联查询ON条件参数包装类
         * @param joinWayWrapper 左、右、内连接配置类
         */
        protected OnCondition(JoinWayWrapper joinWayWrapper) {
            this.joinWayWrapper = joinWayWrapper;
        }

        protected Map<Class, List<String>> getEqualing() {
            return equaling;
        }

        protected Wrapper getWrapper() {
            return wrapper;
        }

        /**
         * 配置多表关联查询ON条件相等字段
         * @param previousEntityClass 之前已配置的表映射对象
         * @param previousProperty 之前已配置的表映射对象的字段名
         * @param property 当前配置的表映射对象的字段名
         * @return 多表关联查询ON条件参数包装类
         */
        public OnCondition andEqual(Class previousEntityClass, String previousProperty, String property) {
            List<String> equalNames = this.equaling.get(previousEntityClass);
            if (equalNames == null) {
               equalNames = Lists.newArrayList();
            }
            equalNames.add(previousProperty + EQUAL_FIX + property);
            this.equaling.put(previousEntityClass, equalNames);
            return this;
        }

        /**
         * 当前表映射对象ON条件配置及条件结束
         * @param wrapper 条件包装
         * @return 左、右、内连接配置类
         */
        public JoinWayWrapper endWrapper(Wrapper wrapper) {
            this.wrapper = wrapper;
            return this.joinWayWrapper;
        }

        /**
         * 当前表映射对象ON条件结束
         * @return 左、右、内连接配置类
         */
        public JoinWayWrapper endWrapper() {
            this.wrapper = Wrapper.newWrapper();
            return this.joinWayWrapper;
        }
    }

    /**
     * 普通连接包装类
     */
    public static class JoinWrapper {

        /**
         * 多表关联查询包装类
         */
        private MultiWrapper multiWrapper;

        /**
         * 多表关联查询相等字段条件配置类
         */
        private AddJoinWrapper addJoinWrapper;

        /**
         * 多表关联查询相关字段条件参数
         */
        private Map<Class, Map<Class, List<String>>> equaling = Maps.newHashMap();

        /**
         * 构建普通连接包装类
         * @param multiWrapper 多表关联查询包装类
         */
        protected JoinWrapper(MultiWrapper multiWrapper) {
            this.multiWrapper = multiWrapper;
            this.addJoinWrapper = new AddJoinWrapper(multiWrapper, this);
        }

        /**
         * 多表关联查询添加普通关联表映射对象
         * @param entityClass 表映射对象
         * @param whereWrapper 表映射对象的附加条件
         * @return 普通连接包装类
         */
        public JoinWrapper andJoin(Class<?> entityClass, Wrapper whereWrapper) {
            this.multiWrapper.entityClasses.add(entityClass);
            this.multiWrapper.wrapperBuilder.andJoin(entityClass, whereWrapper, this.multiWrapper);
            return this;
        }

        /**
         * 多表关联查询相等字段配置
         * @param entityClass1 表映射对象1
         * @param entityClass2 表映射对象2
         * @param property1 表映射对象1对应的字段名
         * @param property2 表映射对象2对应的字段名
         * @return 再次添加多表关联查询相等字段配置类
         */
        public AddJoinWrapper andEqual(Class entityClass1, Class entityClass2, String property1, String property2) {
            Map<Class, List<String>> map = this.equaling.get(entityClass1);
            if (map == null) {
                map = Maps.newHashMap();
            }
            List<String> equalNames = map.get(entityClass2);
            if (equalNames == null) {
                equalNames = Lists.newArrayList();
            }
            equalNames.add(property1 + EQUAL_FIX + property2);
            map.put(entityClass2, equalNames);
            this.equaling.put(entityClass1, map);
            return this.addJoinWrapper;
        }
    }

    /**
     * 再次添加多表关联查询相等字段配置类
     */
    public static class AddJoinWrapper {

        /**
         * 多表关联查询包装类
         */
        private MultiWrapper multiWrapper;

        /**
         * 普通连接包装类
         */
        private JoinWrapper joinWrapper;

        /**
         * 再次添加多表关联查询相等字段配置类
         * @param multiWrapper 多表关联查询包装类
         * @param joinWrapper 普通连接包装类
         */
        protected AddJoinWrapper(MultiWrapper multiWrapper, JoinWrapper joinWrapper) {
            this.multiWrapper = multiWrapper;
            this.joinWrapper = joinWrapper;
        }

        /**
         * 多表关联查询相等字段配置
         * @param entityClass1 表映射对象1
         * @param entityClass2 表映射对象2
         * @param property1 表映射对象1对应的字段名
         * @param property2 表映射对象2对应的字段名
         * @return 再次添加多表关联查询相等字段配置类
         */
        public AddJoinWrapper andEqual(Class entityClass1, Class entityClass2, String property1, String property2) {
            Map<Class, List<String>> map = this.joinWrapper.equaling.get(entityClass1);
            if (map == null) {
                map = Maps.newHashMap();
            }
            List<String> equalNames = map.get(entityClass2);
            if (equalNames == null) {
                equalNames = Lists.newArrayList();
            }
            equalNames.add(property1 + EQUAL_FIX + property2);
            map.put(entityClass2, equalNames);
            this.joinWrapper.equaling.put(entityClass1, map);
            return this;
        }

        /**
         * 多表普通关联查询配置结束
         * @return 多表关联查询包装类
         */
        public MultiWrapper end() {
            return this.multiWrapper;
        }
    }

    /**
     * 左、右、内连接配置类
     */
    public static class JoinWayWrapper {

        /**
         * 多表关联查询包装类
         */
        private MultiWrapper multiWrapper;

        /**
         * 构建左、右、内连接配置类
         * @param multiWrapper 多表关联查询包装类
         */
        protected JoinWayWrapper(MultiWrapper multiWrapper) {
            this.multiWrapper = multiWrapper;
        }

        /**
         * 再次添加左连接关联查询映射对象
         * @param entityClass 查询表对应的映射对象
         * @param whereWrapper 查询表对应的映射对象的条件包装类
         * @return 关联查询ON条件语句属性设置
         */
        public OnEqual andLeftJoin(Class<?> entityClass, Wrapper whereWrapper) {
            return this.multiWrapper.andLeftJoin(entityClass, whereWrapper);
        }

        /**
         * 再次添加右连接关联查询映射对象
         * @param entityClass 查询表对应的映射对象
         * @param whereWrapper 查询表对应的映射对象的条件包装类
         * @return 关联查询ON条件语句属性设置
         */
        public OnEqual andRightJoin(Class<?> entityClass, Wrapper whereWrapper) {
            return this.multiWrapper.andRightJoin(entityClass, whereWrapper);
        }

        /**
         * 再次添加内连接关联查询映射对象
         * @param entityClass 查询表对应的映射对象
         * @param whereWrapper 查询表对应的映射对象的条件包装类
         * @return 关联查询ON条件语句属性设置
         */
        public OnEqual andInnerJoin(Class<?> entityClass, Wrapper whereWrapper) {
            return this.multiWrapper.andInnerJoin(entityClass, whereWrapper);
        }

        /**
         * 添加左、右、内连接映射对象配置结束
         * @return 多表关联查询包装类
         */
        public MultiWrapper end() {
            return this.multiWrapper;
        }
    }

    /**
     * 连接类型
     */
    protected enum JoinType {

        /**
         * 普通类型
         */
        COMMON,

        /**
         * 左连接类型
         */
        LEFT,

        /**
         * 右连接类型
         */
        RIGHT,

        /**
         * 内连接类型
         */
        INNER
    }

}
