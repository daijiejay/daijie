package org.daijie.elasticsearch.cloud.controller;

import org.daijie.core.controller.ApiController;
import org.daijie.core.httpResult.ApiResult;
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
	public ApiResult getBlog(){
		return Result.addData(service.listAll().iterator()).build();
	}
	
	@RequestMapping(value="/searchBlog/{id}", method=RequestMethod.GET)
	public ApiResult getBlog(@PathVariable Integer id){
		return Result.addData(service.getById(id)).build();
	}
	
	@RequestMapping(value="/addBlog", method=RequestMethod.POST)
	public ApiResult saveBlog(@RequestBody Blog blog){
		service.save(blog);
		return Result.build();
	}
}
