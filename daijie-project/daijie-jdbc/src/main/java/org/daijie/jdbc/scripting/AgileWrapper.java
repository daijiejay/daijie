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
public class AgileWrapper {

    protected static final String EQUAL_FIX = ":";
    private AgileWrapperBuilder wrapperBuilder;
    private Set<Class> entityClasses = Sets.newHashSet();

    public AgileWrapper(AgileWrapperBuilder wrapperBuilder) {
        this.wrapperBuilder = wrapperBuilder;
        this.entityClasses.add(this.wrapperBuilder.getEntityClass());
    }

    /**
     * 实例包装条件构建
     * @return 条件包装
     */
    public static AgileWrapper newWrapper(Class<?> entityClass, Wrapper whereWrapper) {
        return new AgileWrapper(new AgileWrapper.AgileWrapperBuilder(entityClass, whereWrapper));
    }

    public JoinCondition and(Class<?> entityClass, Wrapper whereWrapper) {
        this.entityClasses.add(entityClass);
        return this.wrapperBuilder.andJoin(entityClass, whereWrapper, this);
    }

    protected AgileWrapperBuilder getWrapperBuilder() {
        return wrapperBuilder;
    }

    public Set<Class> getEntityClasses() {
        return entityClasses;
    }

    /**
     * 条件包装构建类，封装查询条件、排序、分组、分页的相关信息
     */
    public static class AgileWrapperBuilder {

        private Class entityClass;
        private Map<Class, JoinCondition> joining = Maps.newHashMap();
        private Map<Class, Wrapper> wrapping =  Maps.newHashMap();

        protected AgileWrapperBuilder(Class<?> entityClass, Wrapper whereWrapper) {
            this.entityClass = entityClass;
            this.wrapping.put(entityClass, whereWrapper);
        }

        protected JoinCondition andJoin(Class<?> entityClass, Wrapper whereWrapper, AgileWrapper agileWrapper) {
            JoinCondition joinCondition = new JoinCondition(agileWrapper);
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
    }

    public static class JoinCondition {
        private JoinType joinType;
        private AgileWrapper agileWrapper;
        private OnEqual onEqual;
        protected JoinCondition(AgileWrapper agileWrapper) {
            this.agileWrapper = agileWrapper;
            this.onEqual = new OnEqual(agileWrapper);
        }

        protected JoinType getJoinType() {
            return this.joinType;
        }

        protected OnCondition getOnCondition() {
            return this.onEqual.getOnCondition();
        }

        public AgileWrapper join() {
            this.joinType = JoinType.COMMON;
            return this.agileWrapper;
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
        protected OnEqual(AgileWrapper agileWrapper) {
            this.onCondition = new OnCondition(agileWrapper);
        }

        protected OnCondition getOnCondition() {
            return this.onCondition;
        }

        public OnCondition on(Class  previousEntityClass, String previousProperty, String property) {
            this.onCondition.andEqual(previousEntityClass, previousProperty, property);
            return this.onCondition;
        }
    }

    public static class OnCondition {
        private Map<Class, List<String>> equaling =  Maps.newHashMap();
        private Wrapper wrapper;
        private AgileWrapper agileWrapper;
        protected OnCondition(AgileWrapper agileWrapper) {
            this.agileWrapper = agileWrapper;
        }

        protected Map<Class, List<String>> getEqualing() {
            return equaling;
        }

        protected Wrapper getWrapper() {
            return wrapper;
        }

        public OnCondition andEqual(Class  previousEntityClass, String previousProperty, String property) {
            List<String> equalNames = this.equaling.get(previousEntityClass);
            if (equalNames == null) {
               equalNames = Lists.newArrayList();
            }
            equalNames.add(previousProperty + EQUAL_FIX + property);
            this.equaling.put(previousEntityClass, equalNames);
            return this;
        }

        public AgileWrapper end(Wrapper wrapper) {
            this.wrapper = wrapper;
            return this.agileWrapper;
        }

        public AgileWrapper end () {
            return this.agileWrapper;
        }
    }

    protected enum JoinType {
        COMMON,
        LEFT,
        RIGHT,
        INNER
    }

}
