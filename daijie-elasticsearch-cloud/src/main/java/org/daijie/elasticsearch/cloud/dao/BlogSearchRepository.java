package org.daijie.elasticsearch.cloud.dao;

import org.daijie.elasticsearch.cloud.pojo.Blog;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BlogSearchRepository extends PagingAndSortingRepository<Blog, Integer>{

}
