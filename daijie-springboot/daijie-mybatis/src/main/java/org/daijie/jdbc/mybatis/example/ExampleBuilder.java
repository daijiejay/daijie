package org.daijie.jdbc.mybatis.example;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

public abstract class ExampleBuilder {
	
	private static ThreadLocal<Example> threadExample = new ThreadLocal<Example>();

	public static Builder create(Class<?> className) {
		Builder builder = new Builder();
		return builder.createCriteria(className);
	}

	@SuppressWarnings({"rawtypes"})
	public static class Builder {
		
		private ThreadLocal<Criteria> threadCriteria = new ThreadLocal<Criteria>();
		
		public Builder createCriteria(Class<?> className) {
			Example example = new Example(className);
			threadExample.set(example);
			threadCriteria.set(example.createCriteria());
			return this;
		}
		
		public Example build() {
			return threadExample.get();
		}
		
		public Builder orderByDesc(String property) {
			threadExample.get().orderBy(property).desc();
			return this;
		}
		
		public Builder orderByAsc(String property) {
			threadExample.get().orderBy(property).asc();
			return this;
		}
		
		public Builder andIsNull(String property) {
			threadCriteria.get().andIsNull(property);
            return this;
        }

        public Builder andIsNotNull(String property) {
			threadCriteria.get().andIsNotNull(property);
            return this;
        }

        public Builder andEqualTo(String property, Object value) {
			threadCriteria.get().andEqualTo(property, value);
            return this;
        }

        public Builder andNotEqualTo(String property, Object value) {
			threadCriteria.get().andNotEqualTo(property, value);
            return this;
        }

        public Builder andGreaterThan(String property, Object value) {
			threadCriteria.get().andGreaterThan(property, value);
            return this;
        }

        public Builder andGreaterThanOrEqualTo(String property, Object value) {
			threadCriteria.get().andGreaterThanOrEqualTo(property, value);
            return this;
        }

        public Builder andLessThan(String property, Object value) {
			threadCriteria.get().andLessThan(property, value);
            return this;
        }

        public Builder andLessThanOrEqualTo(String property, Object value) {
			threadCriteria.get().andLessThanOrEqualTo(property, value);
            return this;
        }

		public Builder andIn(String property, Iterable values) {
			threadCriteria.get().andIn(property, values);
            return this;
        }

        public Builder andNotIn(String property, Iterable values) {
			threadCriteria.get().andNotIn(property, values);
            return this;
        }

        public Builder andBetween(String property, Object value1, Object value2) {
			threadCriteria.get().andBetween(property, value1, value2);
            return this;
        }

        public Builder andNotBetween(String property, Object value1, Object value2) {
			threadCriteria.get().andNotBetween(property, value1, value2);
            return this;
        }

        public Builder andLike(String property, String value) {
			threadCriteria.get().andLike(property, value);
            return this;
        }

        public Builder andNotLike(String property, String value) {
			threadCriteria.get().andNotLike(property, value);
            return this;
        }

        public Builder andCondition(String condition) {
			threadCriteria.get().andCondition(condition);
            return this;
        }

        public Builder andCondition(String condition, Object value) {
			threadCriteria.get().andCondition(condition, value);
            return this;
        }

        public Builder andEqualTo(Object param) {
			threadCriteria.get().andEqualTo(param);
            return this;
        }

        public Builder andAllEqualTo(Object param) {
			threadCriteria.get().andAllEqualTo(param);
            return this;
        }

        public Builder orIsNull(String property) {
			threadCriteria.get().orIsNull(property);
            return this;
        }

        public Builder orIsNotNull(String property) {
			threadCriteria.get().orIsNotNull(property);
            return this;
        }

        public Builder orEqualTo(String property, Object value) {
			threadCriteria.get().orEqualTo(property, value);
            return this;
        }

        public Builder orNotEqualTo(String property, Object value) {
			threadCriteria.get().orNotEqualTo(property, value);
            return this;
        }

        public Builder orGreaterThan(String property, Object value) {
			threadCriteria.get().orGreaterThan(property, value);
            return this;
        }

        public Builder orGreaterThanOrEqualTo(String property, Object value) {
			threadCriteria.get().orGreaterThanOrEqualTo(property, value);
            return this;
        }

        public Builder orLessThan(String property, Object value) {
			threadCriteria.get().orLessThan(property, value);
            return this;
        }

        public Builder orLessThanOrEqualTo(String property, Object value) {
			threadCriteria.get().orLessThanOrEqualTo(property, value);
            return this;
        }

        public Builder orIn(String property, Iterable values) {
			threadCriteria.get().orIn(property, values);
            return this;
        }

        public Builder orNotIn(String property, Iterable values) {
			threadCriteria.get().orNotIn(property, values);
            return this;
        }

        public Builder orBetween(String property, Object value1, Object value2) {
			threadCriteria.get().orBetween(property, value1, value2);
            return this;
        }

        public Builder orNotBetween(String property, Object value1, Object value2) {
			threadCriteria.get().orNotBetween(property, value1, value2);
            return this;
        }

        public Builder orLike(String property, String value) {
			threadCriteria.get().orLike(property, value);
            return this;
        }

        public Builder orNotLike(String property, String value) {
			threadCriteria.get().orNotLike(property, value);
            return this;
        }

        public Builder orCondition(String condition) {
			threadCriteria.get().orCondition(condition);
            return this;
        }

        public Builder orCondition(String condition, Object value) {
			threadCriteria.get().orCondition(condition, value);
            return this;
        }

        public Builder orEqualTo(Object param) {
			threadCriteria.get().orEqualTo(param);
            return this;
        }

        public Builder orAllEqualTo(Object param) {
			threadCriteria.get().orAllEqualTo(param);
            return this;
        }
	}
}
