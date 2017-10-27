package org.daijie.rabbit.cloud.controller;

import org.daijie.rabbit.cloud.mq.producer.ProducerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RabbitMQController {

	@Autowired
	ProducerTest producerTest;

	@RequestMapping(value = "/rabbit/send", method = RequestMethod.POST)
	public Object send(){
		producerTest.send();
		return "success";
	}
}
