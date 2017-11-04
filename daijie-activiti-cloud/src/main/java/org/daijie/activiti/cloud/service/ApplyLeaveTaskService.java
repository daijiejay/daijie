package org.daijie.activiti.cloud.service;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

@SuppressWarnings("serial")
public class ApplyLeaveTaskService implements TaskListener {

	@Override
	public void notify(DelegateTask delegateTask) {
		System.out.println(this.getClass().getName() + "申请请假！");
		//设置下一流程审批人ID
		ProcessEngines.getDefaultProcessEngine().getTaskService()
			.setVariable(delegateTask.getId(), "checkUserId", 1);
	}
}
