package org.daijie.api.controller;

import org.daijie.core.factory.specific.ModelResultInitialFactory.Result;
import org.daijie.core.lock.Callback;
import org.daijie.core.lock.LockTool;
import org.daijie.core.result.ModelResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分布式锁测试
 * @author daijie_jay
 * @date 2017年11月24日
 */
@RestController
public class LockController {

	@RequestMapping(value = "testLock", method = RequestMethod.GET)
	public ModelResult<Object> testLock(){
		Object result = LockTool.execute("test", 1000, new Callback() {
			
			@Override
			public Object onTimeout() throws InterruptedException {
				return 1;
			}
			
			@Override
			public Object onGetLock() throws InterruptedException {
				return 0;
			}
		});
		return Result.build(result);
	}
}
