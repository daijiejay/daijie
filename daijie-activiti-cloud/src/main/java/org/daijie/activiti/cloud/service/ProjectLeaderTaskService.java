package org.daijie.activiti.cloud.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class ProjectLeaderTaskService implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) {
		System.out.println(this.getClass().getName() + "审批通过！");
	}

	
}
