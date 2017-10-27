package org.daijie.elasticsearch.cloud.controller;

import java.util.Iterator;

import org.daijie.core.controller.ApiController;
import org.daijie.core.factory.specific.ModelResultInitialFactory.Result;
import org.daijie.core.httpResult.ModelResult;
import org.daijie.elasticsearch.cloud.pojo.Blog;
import org.daijie.elasticsearch.cloud.service.BlogService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogController extends ApiController<BlogService, Exception> {

	@RequestMapping(value="/searchBlogs", method=RequestMethod.GET)
	public ModelResult<Iterator<Blog>> getBlog(){
		return Result.build(service.listAll().iterator());
	}
	
	@RequestMapping(value="/searchBlog/{id}", method=RequestMethod.GET)
	public ModelResult<Blog> getBlog(@PathVariable Integer id){
		return Result.build(service.getById(id));
	}
	
	@RequestMapping(value="/addBlog", method=RequestMethod.POST)
	public ModelResult<Object> saveBlog(@RequestBody Blog blog){
		service.save(blog);
		return Result.build();
	}
}
