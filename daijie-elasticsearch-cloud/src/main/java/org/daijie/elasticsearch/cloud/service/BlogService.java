package org.daijie.elasticsearch.cloud.service;

import org.daijie.api.model.Blog;
import org.daijie.elasticsearch.cloud.dao.BlogSearchRepository;
import org.daijie.elasticsearch.cloud.service.base.BaseSearchService;
import org.springframework.stereotype.Service;

@Service
public class BlogService extends BaseSearchService<Blog, Integer, BlogSearchRepository> {

}
