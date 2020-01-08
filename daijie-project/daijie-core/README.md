# 工程简介
* 主要提供一些基础类和一些常用工具类。
* 流程枚举存储容器，枚举成员节点提供线性数据结构的有序序列存储和双向链表存储、树形数据结构的树形链表存储、图形数据结构的十字链表存储。
# 使用说明
## maven依赖
```
<dependency>
	<groupId>org.daijie</groupId>
	<artifactId>daijie-core</artifactId>
	<version>2.0.0</version>
</dependency>
```
## 流程枚举存储容器
### 线性枚举成员节点的有序序列存储
* 有序序列存储需要实现`OrderEnumProcessFactory`接口，并实现它的方法
* 举例，请假流程为“请假申请”->"项目组长审批"->"项目经理审批"->"部门经理审批"->"人事自动审批"->"请假完成"->"流程结束"，其中领导审批有一个不同意都是直接流程结束。
```java
public enum LeaveStatus implements OrderEnumProcessFactory<LeaveStatus> {
	APPLY(1, null, "请假申请"),	
	PROJECT_LEADER(2, "projectLeaderId", "项目组长审批"),	
	PROJECT_MANAGER(3, "projectManagerId", "项目经理审批"),	
	DEPARTMENT_MANAGER(4, "departmentManagerId", "部门经理审批"),	
	PERSONNEL(5, null, "人事自动审批"),	
	COMPLATE(6, null, "请假完成"),	
	REFUSE(7, null, "流程结束");
	
	private Integer status;	
	private String assignee;	
	private String msg;
	
	LeaveStatus(Integer status, String assignee, String msg){
		this.status = status;
		this.assignee = assignee;
		this.msg = msg;
	}

	@Override
	public Integer getStatus() {
		return status;
	}
	@Override
	public String getMsg() {
		return msg;
	}
	@Override
	public String getAssignee() {
		return assignee;
	}
	@Override
	public LeaveStatus getEnumType() {
		return this;
	}
	@Override
	public LeaveStatus[] getEnumTypes() {
		return LeaveStatus.values();
	}
}
```
* 容器使用
```java
//获取下一个流程节点
LeaveStatus.APPLY.nextProcess(Process.THROUGH);
//获取上一个流程节点
LeaveStatus.PROJECT_LEADER.preProcess(Process.THROUGH);
//获取流程结束节点
LeaveStatus.PROJECT_MANAGER.nextProcess(Process.NOT_THROUGH);
```
### 树形枚举成员节点的链表树状存储
* 链表树状存储需要实现`TreeEnumProcessFactory`接口，并实现它的方法
* 举例，文物备案主线流程为“申请文物备案”->“用户支付”->“初审”->“复审”->“预约实物线下终审”->“客户到场确认”->“终审”->“备案入库”->“备案完成”，例举其中一个分支支线任务，“初审”->“重新提交”或者“初审”->“退款”，在流程当中一般只有四种流程走向（并行和串行走也都是同一个流程），这里定义了枚举类`Process`，分别对应为“通过”、“不通过”、“拒绝”、“退回”四个操作，流转时根据业务自由定义。
```java
public enum RelicStatus implements TreeEnumProcessFactory<RelicStatus> {
	APPLY("username", "申请文物备案"),
	PAY("username", "用户支付"),
	TRIAL("auditor", "初审"),
	REVIWE(null, "复审"),
	APPOINTENT("customerService", "预约实物线下终审"),
	ARRIVE("customerService", "客户到场确认"),
	LAST_TRIAL(null, "终审"),
	RECORD("operator", "备案入库"),
	SUBMIT("username", "重新提交"),	
	REFUND(null, "退款"),
	FAIL(null, "备案失败"),
	CANEL(null, "备案取消"),
	COMPLATE(null, "备案完成");
	
	private String assignee;
	private String msg;

	RelicStatus(String assignee, String msg){
		this.assignee = assignee;
		this.msg = msg;
	}

	@Override
	public String getMsg() {
		return msg;
	}
	@Override
	public String getAssignee() {
		return assignee;
	}
	@Override
	public RelicStatus getEnumType() {
		return this;
	}	
	@Override
	public RelicStatus[] getEnumTypes() {
		return RelicStatus.values();
	}
	@Override
	public TreeEnumProcess<RelicStatus> getEnumProcess() {
		TreeEnumProcess<RelicStatus> process = new TreeEnumProcess<>();
		//初始化主线流程，这里会从上往下关连流程，或者是这样this.process.add(new RelicStatus[]{APPLY, PAY, ...});
		process.add(APPLY);//申请备案
		process.add(PAY);//支付
		process.add(TRIAL);//初审
		process.add(REVIWE);//复审
		process.add(APPOINTENT);//预约线下审核
		process.add(ARRIVE);//用户到场确认
		process.add(LAST_TRIAL);//终审
		process.add(RECORD);//备案入库
		process.add(COMPLATE);//备案完成
		//设置支线流程，关连其它节点
		process.setBranch(PAY, CANEL, Process.NOT_THROUGH);//支付->备案取消，条件为不通过（如用户取消订单操作）
		process.setBranch(TRIAL, SUBMIT, TRIAL, Process.REJECT);//初审->重新提交->初审，条件为拒绝（如初审为图片不清晰）
		process.setBranch(TRIAL, REFUND, CANEL);//初审->退款->备案取消，下节点只有一个
		process.setBranch(REVIWE, FAIL, Process.NOT_THROUGH);//复审->备案失败，条件为不通过（如文物没有价值意义）
		process.setBranch(APPOINTENT, CANEL, Process.NOT_THROUGH);//预约线下审核->备案取消，条件为不通过（如客服在电话预约中用户取消备案）
		process.setBranch(ARRIVE, APPOINTENT, Process.RETURN);//用户到场确认->预约线下审核，条件为退回（如用户约定当天未到场）
		process.setBranch(LAST_TRIAL, FAIL, Process.NOT_THROUGH);//终审->备案失败，条件为不通过（如文物仿造）
		return process;
	}
}
```
* 容器使用
```java
//获取主线流程的下一个流程节点（节点“APPLY（申请）”申请成功，得到下一个节点为“PAY（支付）”）
LeaveStatus.APPLY.nextProcess(Process.THROUGH);
//获取主线流程的上一个流程节点（得到上一个节点为“REVIWE（复审）”）
LeaveStatus.APPOINTENT.preProcess(Process.THROUGH);
//获取支线流程的下个流程节点（节点“TRIAL（初审）”审批为拒绝，得到下一个节点为“SUBMIT（重新提交）”）
LeaveStatus.TRIAL.nextProcess(Process.REJECT);
//获取支线流程的下个流程节点（节点“TRIAL（初审）”审批为不通过，得到下一个节点为“REFUND（退款）”）
LeaveStatus.TRIAL.nextProcess(Process.NOT_THROUGH);
```