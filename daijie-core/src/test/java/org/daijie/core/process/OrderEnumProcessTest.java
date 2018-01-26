package org.daijie.core.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.daijie.core.process.factory.OrderEnumProcessFactory;
import org.junit.Test;

public class OrderEnumProcessTest {

	@Test
	public void testProcess() {
		OrderEnumProcess<OrderTest> orderEnumProcess = new OrderEnumProcess<>();
		assertTrue(orderEnumProcess.isEmpty());
		
		orderEnumProcess.add(OrderTest.START);
		orderEnumProcess.add(OrderTest.APPLY);
		assertEquals(orderEnumProcess.next(OrderTest.START), OrderTest.APPLY);
		assertEquals(orderEnumProcess.pre(OrderTest.APPLY), OrderTest.START);
		
		orderEnumProcess.add(OrderTest.PROJECT_LEADER);
		assertEquals(orderEnumProcess.next(orderEnumProcess.next(OrderTest.START)), OrderTest.PROJECT_LEADER);
		assertEquals(orderEnumProcess.pre(orderEnumProcess.pre(OrderTest.PROJECT_LEADER)), OrderTest.START);
		
		orderEnumProcess.add(new OrderTest[]{OrderTest.PROJECT_MANAGER,OrderTest.DEPARTMENT_MANAGER});
		orderEnumProcess.add(OrderTest.PERSONNEL);
		assertEquals(orderEnumProcess.next(OrderTest.PROJECT_LEADER), OrderTest.PROJECT_MANAGER);
		assertEquals(orderEnumProcess.next(OrderTest.PROJECT_MANAGER), OrderTest.DEPARTMENT_MANAGER);
		assertEquals(orderEnumProcess.pre(OrderTest.PROJECT_MANAGER), OrderTest.PROJECT_LEADER);
		assertEquals(orderEnumProcess.pre(OrderTest.PERSONNEL), OrderTest.DEPARTMENT_MANAGER);
		
		orderEnumProcess.add(OrderTest.COMPLATE);
		assertEquals(orderEnumProcess.next(OrderTest.PERSONNEL), OrderTest.COMPLATE);
		assertEquals(orderEnumProcess.pre(OrderTest.COMPLATE, Process.THROUGH), OrderTest.PERSONNEL);
		assertFalse(orderEnumProcess.isEmpty());
		assertEquals(orderEnumProcess.size(), OrderTest.values().length);
	}
	
	@Test
	public void testProcessFactory() {
		assertEquals(OrderTest.START.nextProcess(Process.THROUGH), OrderTest.APPLY);
		assertEquals(OrderTest.DEPARTMENT_MANAGER.nextProcess(Process.THROUGH), OrderTest.PERSONNEL);
		assertEquals(OrderTest.DEPARTMENT_MANAGER.nextProcess(Process.NOT_THROUGH), OrderTest.COMPLATE);
	}
	
	public enum OrderTest implements OrderEnumProcessFactory<OrderTest> {
		START(1, null, "请假申请"),
		
		APPLY(2, null, "请假申请"),
		
		PROJECT_LEADER(3, "projectLeaderId", "项目组长审批"),
		
		PROJECT_MANAGER(4, "projectManagerId", "项目经理审批"),
		
		DEPARTMENT_MANAGER(5, "departmentManagerId", "部门经理审批"),
		
		PERSONNEL(6, null, "人事自动审批"),
		
		COMPLATE(7, null, "流程结束");
		
		private int status;
		
		private String assignee;
		
		private String msg;
		
		OrderTest(int status, String assignee, String msg){
			this.status = status;
			this.assignee = assignee;
			this.msg = msg;
		}

		@Override
		public OrderTest getEnumType() {
			return this;
		}

		@Override
		public OrderTest[] getEnumTypes() {
			return OrderTest.values();
		}

		@Override
		public String getAssignee() {
			return assignee;
		}

		@Override
		public String getMsg() {
			return msg;
		}

		@Override
		public Integer getStatus() {
			return status;
		}

		@Override
		public OrderEnumProcess<OrderTest> getEnumProcess() {
			OrderEnumProcess<OrderTest> orderEnumProcess = new OrderEnumProcess<>();
			orderEnumProcess.add(OrderTest.START);
			orderEnumProcess.add(OrderTest.APPLY);
			orderEnumProcess.add(OrderTest.PROJECT_LEADER);
			orderEnumProcess.add(OrderTest.PROJECT_MANAGER);
			orderEnumProcess.add(OrderTest.DEPARTMENT_MANAGER);
			orderEnumProcess.add(OrderTest.PERSONNEL);
			orderEnumProcess.add(OrderTest.COMPLATE);
			return orderEnumProcess;
		}
		
	}
}
