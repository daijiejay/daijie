package org.daijie.elasticsearch.cloud.controller;

import org.daijie.core.controller.ApiController;
import org.daijie.core.httpResult.ApiResult;
import org.daijie.elasticsearch.cloud.service.BlogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogController extends ApiController<BlogService, Exception> {

	@RequestMapping(value="/getBlog", method=RequestMethod.GET)
	public ApiResult getBlog(){
		return Result.addData(service.getByKey("Android", "Android")).build();
	}
}
