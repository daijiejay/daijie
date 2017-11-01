package org.daijie.activiti.cloud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.daijie.core.controller.ApiController;
import org.daijie.core.factory.specific.ModelResultInitialFactory.Result;
import org.daijie.core.result.ApiResult;
import org.daijie.core.result.ModelResult;
import org.daijie.core.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivitiController extends ApiController<TaskService, Exception> {
	
	@Autowired
    private RuntimeService runtimeService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@RequestMapping(value = "/getTasks/{checkUserId}", method = RequestMethod.GET)
	public ModelResult<Object> getTasks(@PathVariable Integer checkUserId){
		List<Map<String, Object>> rows = new ArrayList<>();
		PageResult<Map<String, Object>> datas = new PageResult<>();
		TaskQuery query = service.createTaskQuery().processVariableValueEquals("checkUserId", checkUserId);
		List<Task> tasks = query.list();
		tasks.forEach(task ->{
			Map<String, Object> runtimeVariables = runtimeService.getVariables(task.getExecutionId());
			Map<String, Object> taskVariables = service.getVariables(task.getId());
			Map<String, Object> row = new HashMap<>();
			row.put("taskId", task.getId());
			row.put("taskName", task.getName());
			row.put("assignee", task.getAssignee());
			row.put("category", task.getCategory());
			row.put("claimTime", task.getClaimTime());
			row.put("delegationState", task.getDelegationState());
			row.put("description", task.getDescription());
			row.put("dueDate", task.getDueDate());
			row.put("executionId", task.getExecutionId());
			row.put("formKey", task.getFormKey());
			row.put("owner", task.getOwner());
			row.put("parentTaskId", task.getParentTaskId());
			row.put("priority", task.getPriority());
			row.put("processDefinitionId", task.getProcessDefinitionId());
			row.put("processInstanceId", task.getProcessInstanceId());
			row.put("taskDefinitionKey", task.getTaskDefinitionKey());
			row.put("tenantId", task.getTenantId());
			row.put("taskLocalVariables", task.getTaskLocalVariables());
			row.put("username", runtimeVariables.get("username"));
			row.put("days", runtimeVariables.get("days"));
			row.put("checkUserId", taskVariables.get("checkUserId"));
			rows.add(row);
		});
		datas.setTotal(query.count());
		datas.setRows(rows);
		return Result.build(datas);
	}

	/**
	 * 查询所有申请订单流程
	 * @return
	 */
	@RequestMapping(value = "/getActivities", method = RequestMethod.GET)
	public ModelResult<Object> getActivities(){
		List<Map<String, Object>> rows = new ArrayList<>();
		PageResult<Map<String, Object>> datas = new PageResult<>();
		ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery();
		List<ProcessInstance> processInstances = query.list();
		processInstances.forEach(processInstance ->{
			HistoricActivityInstance historicActivityInstance = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstance.getId())
				.unfinished().singleResult();
			Map<String, Object> row = new HashMap<>();
			row.put("processInstanceId", processInstance.getId());
			if(historicActivityInstance != null){
				Task task = service.createTaskQuery().taskId(historicActivityInstance.getTaskId()).singleResult();
				Map<String, Object> runtimeVariables = runtimeService.getVariables(historicActivityInstance.getExecutionId());
				Map<String, Object> taskVariables = service.getVariables(task.getId());
				row.put("activityName", historicActivityInstance.getActivityName());
				row.put("taskId", historicActivityInstance.getTaskId());
				row.put("createTime", task.getCreateTime());
				row.put("username", runtimeVariables.get("username"));
				row.put("days", runtimeVariables.get("days"));
				row.put("checkUserId", taskVariables.get("checkUserId"));
			}else{
				row.put("activityName", "流程结束");
			}
			rows.add(row);
		});
		datas.setTotal(query.count());
		datas.setRows(rows);
		return Result.build(datas);
	}

	/**
	 * 请假申请
	 * @param username
	 * @param days
	 * @return
	 */
	@RequestMapping(value = "/applyLeave", method = RequestMethod.POST)
	public ModelResult<Object> applyLeave(@RequestParam String username, @RequestParam Integer days){
		Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("username", username);
        variables.put("days", days);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("dome", variables);
		Task task = service.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		//设置下一流程审核人ID
		service.setVariable(task.getId(), "checkUserId", 1);
		return Result.build();
	}
	
	/**
	 * 项目组长审核
	 * @param userId
	 * @param checkStatus
	 * @return
	 */
	@RequestMapping(value = "/projectLeaderCheck", method = RequestMethod.POST)
	public ModelResult<Object> projectLeaderCheck(@RequestParam String processInstanceId, @RequestParam Integer userId, 
			@RequestParam Boolean checkStatus){
		Task task = service.createTaskQuery().processInstanceId(processInstanceId)
				.processVariableValueEquals("checkUserId", userId)
				.singleResult();
		if(task == null){
			return Result.build("没有需要审批的订单！", ApiResult.ERROR);
		}
		Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("checkStatus", checkStatus);
        variables.put("userId", userId);
		service.complete(task.getId(), variables);
		//设置下一流程审核人ID
		task = service.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		service.setVariable(task.getId(), "checkUserId", 2);
		return Result.build();
	}
	
	/**
	 * 项目经理审核
	 * @param userId
	 * @param checkStatus
	 * @return
	 */
	@RequestMapping(value = "/projectManagerCheck", method = RequestMethod.POST)
	public ModelResult<Object> projectManagerCheck(@RequestParam String processInstanceId, @RequestParam Integer userId, 
			@RequestParam Boolean checkStatus){
		Task task = service.createTaskQuery().processInstanceId(processInstanceId)
				.processVariableValueEquals("checkUserId", userId)
				.singleResult();
		if(task == null){
			return Result.build("没有需要审批的订单！", ApiResult.ERROR);
		}
		Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("checkStatus", checkStatus);
        variables.put("userId", userId);
		service.complete(task.getId(), variables);
		//设置下一流程审核人ID
		task = service.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		service.setVariable(task.getId(), "checkUserId", 3);
		return Result.build();
	}
	
	/**
	 * 部门经理审核
	 * @param userId
	 * @param checkStatus
	 * @return
	 */
	@RequestMapping(value = "/departmentManagerCheck", method = RequestMethod.POST)
	public ModelResult<Object> departmentManagerCheck(@RequestParam String processInstanceId, @RequestParam Integer userId, 
			@RequestParam Boolean checkStatus){
		Task task = service.createTaskQuery().processInstanceId(processInstanceId)
				.processVariableValueEquals("checkUserId", userId)
				.singleResult();
		if(task == null){
			return Result.build("没有需要审批的订单！", ApiResult.ERROR);
		}
		Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("checkStatus", checkStatus);
        variables.put("userId", userId);
		service.complete(task.getId(), variables);
		return Result.build();
	}
}
