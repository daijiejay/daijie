package org.daijie.elasticsearch.cloud.controller;

import java.util.Iterator;

import org.daijie.api.BlogCloud;
import org.daijie.api.model.Blog;
import org.daijie.core.controller.ApiController;
import org.daijie.core.factory.specific.ModelResultInitialFactory.Result;
import org.daijie.core.result.ModelResult;
import org.daijie.elasticsearch.cloud.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogController extends ApiController implements BlogCloud {
	
	@Autowired
	private BlogService service;

	@Override
	public ModelResult<Iterator<Blog>> getBlog(){
		return Result.build(service.listAll().iterator());
	}
	
	@Override
	public ModelResult<Blog> getBlog(@PathVariable Integer id){
		return Result.build(service.getById(id));
	}
	
	@Override
	public ModelResult<Object> saveBlog(@RequestBody Blog blog){
		service.save(blog);
		return Result.build();
	}
}
