package org.daijie.shiro.web.mq.producer;

import org.daijie.mybatis.model.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProducerTest {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	
	public void send1(){
		User user = new User();
		user.setMobile("18911351016");
		rabbitTemplate.convertAndSend(user);
	}
	
	public void send2(){
		User user = new User();
		user.setMobile("18911351016");
		amqpTemplate.convertAndSend(user);
	}
}
