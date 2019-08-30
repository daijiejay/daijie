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
     * @param whereWrapper 查询表对应的映射对象的条件包装类
     * @return 多表关联查询包装类
     */
    public static MultiWrapper newWrapper(Class<?> entityClass, Wrapper whereWrapper) {
        return new MultiWrapper(new MultiWrapper.MultiWrapperBuilder(entityClass, whereWrapper));
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

        protected MultiWrapperBuilder(Class<?> entityClass, Wrapper whereWrapper) {
            if (whereWrapper == null) {
                whereWrapper = Wrapper.newWrapper();
            }
            this.entityClass = entityClass;
            this.wrapping.put(entityClass, whereWrapper);
        }

        protected JoinCondition andJoin(Class<?> entityClass, Wrapper whereWrapper, MultiWrapper multiWrapper) {
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

        protected boolean isCondition() {
            for (Class clz : this.wrapping.keySet()) {
                if (!this.wrapping.get(clz).getWrapperBuilder().getConditions().isEmpty()) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class JoinCondition {
        private JoinType joinType;
        private OnEqual onEqual;
        private JoinWrapper joinWrapper;
        private JoinWayWrapper joinWayWrapper;
        protected JoinCondition(MultiWrapper multiWrapper) {
            this.joinWrapper = new JoinWrapper(multiWrapper);
            this.joinWayWrapper = new JoinWayWrapper(multiWrapper);
            this.onEqual = new OnEqual(this.joinWayWrapper);
        }

        protected JoinType getJoinType() {
            return this.joinType;
        }

        protected OnCondition getOnCondition() {
            return this.onEqual.getOnCondition();
        }

        public JoinWrapper join() {
            this.joinType = JoinType.COMMON;
            return this.joinWrapper;
        }

        public OnEqual leftJoin() {
            this.joinType = JoinType.LEFT;
            return this.onEqual;
        }

        public OnEqual rightJoin() {
            this.joinType = JoinType.RIGHT;
            return this.onEqual;
        }

        public OnEqual innerJoin() {
            this.joinType = JoinType.INNER;
            return this.onEqual;
        }
    }

    public static class OnEqual {
        private OnCondition onCondition;
        protected OnEqual(JoinWayWrapper joinWayWrapper) {
            this.onCondition = new OnCondition(joinWayWrapper);
        }

        protected OnCondition getOnCondition() {
            return this.onCondition;
        }

        public OnCondition onEqual(Class  previousEntityClass, String previousProperty, String property) {
            this.onCondition.andEqual(previousEntityClass, previousProperty, property);
            return this.onCondition;
        }
    }

    public static class OnCondition {
        private Map<Class, List<String>> equaling =  Maps.newHashMap();
        private Wrapper wrapper;
        private JoinWayWrapper joinWayWrapper;
        protected OnCondition(JoinWayWrapper joinWayWrapper) {
            this.joinWayWrapper = joinWayWrapper;
        }

        protected Map<Class, List<String>> getEqualing() {
            return equaling;
        }

        protected Wrapper getWrapper() {
            return wrapper;
        }

        public OnCondition andEqual(Class previousEntityClass, String previousProperty, String property) {
            List<String> equalNames = this.equaling.get(previousEntityClass);
            if (equalNames == null) {
               equalNames = Lists.newArrayList();
            }
            equalNames.add(previousProperty + EQUAL_FIX + property);
            this.equaling.put(previousEntityClass, equalNames);
            return this;
        }

        public JoinWayWrapper endWrapper(Wrapper wrapper) {
            this.wrapper = wrapper;
            return this.joinWayWrapper;
        }

        public JoinWayWrapper endWrapper() {
            this.wrapper = Wrapper.newWrapper();
            return this.joinWayWrapper;
        }
    }

    public static class JoinWrapper {

        private MultiWrapper multiWrapper;
        private AddJoinWrapper addJoinWrapper;
        private Map<Class, Map<Class, List<String>>> equaling = Maps.newHashMap();

        protected JoinWrapper(MultiWrapper multiWrapper) {
            this.multiWrapper = multiWrapper;
            this.addJoinWrapper = new AddJoinWrapper(multiWrapper, this);
        }

        public JoinWrapper andJoin(Class<?> entityClass, Wrapper whereWrapper) {
            this.multiWrapper.entityClasses.add(entityClass);
            this.multiWrapper.wrapperBuilder.andJoin(entityClass, whereWrapper, this.multiWrapper);
            return this;
        }

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

    public static class AddJoinWrapper {

        private MultiWrapper multiWrapper;
        private JoinWrapper joinWrapper;

        protected AddJoinWrapper(MultiWrapper multiWrapper, JoinWrapper joinWrapper) {
            this.multiWrapper = multiWrapper;
            this.joinWrapper = joinWrapper;
        }

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

        public MultiWrapper end() {
            return this.multiWrapper;
        }
    }

    public static class JoinWayWrapper {
        private MultiWrapper multiWrapper;

        protected JoinWayWrapper(MultiWrapper multiWrapper) {
            this.multiWrapper = multiWrapper;
        }

        public OnEqual andLeftJoin(Class<?> entityClass, Wrapper whereWrapper) {
            return this.multiWrapper.andLeftJoin(entityClass, whereWrapper);
        }

        public OnEqual andRightJoin(Class<?> entityClass, Wrapper whereWrapper) {
            return this.multiWrapper.andRightJoin(entityClass, whereWrapper);
        }

        public OnEqual andInnerJoin(Class<?> entityClass, Wrapper whereWrapper) {
            return this.multiWrapper.andInnerJoin(entityClass, whereWrapper);
        }

        public MultiWrapper end() {
            return this.multiWrapper;
        }
    }

    protected enum JoinType {
        COMMON,
        LEFT,
        RIGHT,
        INNER
    }

}
