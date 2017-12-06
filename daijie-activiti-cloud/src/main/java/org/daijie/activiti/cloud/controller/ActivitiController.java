package org.daijie.activiti.cloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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


@Api(description = "请假流程案例")
@RestController
public class ActivitiController extends ApiController {
	
	@Autowired
    private TaskService taskService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@ApiOperation(value = "根据审批人ID获取当前待处理的任务")
	@RequestMapping(value = "/getTasks/{checkUserId}", method = RequestMethod.GET)
	public ModelResult<Object> getTasks(
			@ApiParam(value = "审批人ID") @PathVariable Integer checkUserId){
		List<Map<String, Object>> rows = new ArrayList<>();
		PageResult<Map<String, Object>> datas = new PageResult<>();
		TaskQuery query = taskService.createTaskQuery().processVariableValueEquals("checkUserId", checkUserId);
		List<Task> tasks = query.list();
		tasks.forEach(task ->{
			Map<String, Object> runtimeVariables = runtimeService.getVariables(task.getExecutionId());
			Map<String, Object> taskVariables = taskService.getVariables(task.getId());
			Map<String, Object> row = new HashMap<>();
			row.put("taskId", task.getId());
			row.put("taskName", task.getName());
			row.put("assignee", task.getAssignee());
			row.put("category", task.getCategory());
			row.put("createTime", task.getCreateTime());
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
	@ApiOperation(value = "获取所有流程")
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
				Task task = taskService.createTaskQuery().taskId(historicActivityInstance.getTaskId()).singleResult();
				Map<String, Object> runtimeVariables = runtimeService.getVariables(historicActivityInstance.getExecutionId());
				Map<String, Object> taskVariables = taskService.getVariables(task.getId());
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
	@ApiOperation(value = "请假申请")
	@RequestMapping(value = "/applyLeave", method = RequestMethod.POST)
	public ModelResult<Object> applyLeave(
			@ApiParam(value = "请假人") @RequestParam String username, 
			@ApiParam(value = "请假天数") @RequestParam Integer days){
		Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("username", username);
        variables.put("days", days);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave", variables);
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId())
				.singleResult();
		variables = new HashMap<String, Object>();
		//设置下一流程审批人ID
        variables.put("projectLeaderId", 1);
        taskService.complete(task.getId(), variables);
		return Result.build();
	}
	
	/**
	 * 项目组长审批
	 * @param userId
	 * @param checkStatus
	 * @return
	 */
	@ApiOperation(value = "项目组长审批")
	@RequestMapping(value = "/projectLeaderCheck", method = RequestMethod.POST)
	public ModelResult<Object> projectLeaderCheck(
			@ApiParam(value = "流程ID") @RequestParam String processInstanceId, 
			@ApiParam(value = "处理人ID") @RequestParam Integer userId, 
			@ApiParam(value = "审批意见") @RequestParam Boolean checkStatus){
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId)
				.taskAssignee(userId.toString())
				.singleResult();
		if(task == null){
			return Result.build("没有需要审批的订单！", ApiResult.ERROR);
		}
		//执行项目组长审批
		Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("checkStatus", checkStatus);
		//设置下一流程审批人ID
        variables.put("projectManagerId", 2);
        taskService.complete(task.getId(), variables);
		return Result.build();
	}
	
	/**
	 * 项目经理审批
	 * @param userId
	 * @param checkStatus
	 * @return
	 */
	@ApiOperation(value = "项目经理审批")
	@RequestMapping(value = "/projectManagerCheck", method = RequestMethod.POST)
	public ModelResult<Object> projectManagerCheck(
			@ApiParam(value = "流程ID") @RequestParam String processInstanceId, 
			@ApiParam(value = "处理人ID") @RequestParam Integer userId, 
			@ApiParam(value = "审批意见") @RequestParam Boolean checkStatus){
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId)
				.taskAssignee(userId.toString())
				.singleResult();
		if(task == null){
			return Result.build("没有需要审批的订单！", ApiResult.ERROR);
		}
		//执行项目经理审批
		Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("checkStatus", checkStatus);
		//设置下一流程审批人ID
        variables.put("departmentManagerId", 3);
        taskService.complete(task.getId(), variables);
		return Result.build();
	}
	
	/**
	 * 部门经理审批
	 * @param userId
	 * @param checkStatus
	 * @return
	 */
	@ApiOperation(value = "部门经理审批")
	@RequestMapping(value = "/departmentManagerCheck", method = RequestMethod.POST)
	public ModelResult<Object> departmentManagerCheck(
			@ApiParam(value = "流程ID") @RequestParam String processInstanceId, 
			@ApiParam(value = "处理人ID") @RequestParam Integer userId, 
			@ApiParam(value = "审批意见") @RequestParam Boolean checkStatus){
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId)
				.taskAssignee(userId.toString())
				.singleResult();
		if(task == null){
			return Result.build("没有需要审批的订单！", ApiResult.ERROR);
		}
		//执行部门经理审批
		Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("checkStatus", checkStatus);
        taskService.complete(task.getId(), variables);
		return Result.build();
	}
}
