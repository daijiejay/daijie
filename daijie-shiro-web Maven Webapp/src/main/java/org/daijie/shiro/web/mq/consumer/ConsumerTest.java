package org.daijie.shiro.web.mq.consumer;

import org.daijie.mybatis.model.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class ConsumerTest {

	@RabbitListener
	public void process(Object message){
		User user = (User) message;
		System.out.println(user.getMobile());
	}
}
