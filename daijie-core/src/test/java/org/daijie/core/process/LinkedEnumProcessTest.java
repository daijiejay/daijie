package org.daijie.core.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.daijie.core.process.factory.LinkedEnumProcessFactory;
import org.junit.Test;

public class LinkedEnumProcessTest {

	@Test
	public void testProcess() {
		LinkedEnumProcess<LinkedTest> linkedEnumProcess = new LinkedEnumProcess<>();
		assertTrue(linkedEnumProcess.isEmpty());
		
		linkedEnumProcess.add(LinkedTest.START);
		linkedEnumProcess.add(LinkedTest.APPLY);
		assertEquals(linkedEnumProcess.next(LinkedTest.START), LinkedTest.APPLY);
		assertEquals(linkedEnumProcess.pre(LinkedTest.APPLY), LinkedTest.START);
		
		linkedEnumProcess.add(LinkedTest.PROJECT_LEADER);
		assertEquals(linkedEnumProcess.next(linkedEnumProcess.next(LinkedTest.START)), LinkedTest.PROJECT_LEADER);
		assertEquals(linkedEnumProcess.pre(linkedEnumProcess.pre(LinkedTest.PROJECT_LEADER)), LinkedTest.START);
		
		linkedEnumProcess.add(new LinkedTest[]{LinkedTest.PROJECT_MANAGER,LinkedTest.DEPARTMENT_MANAGER});
		linkedEnumProcess.add(LinkedTest.PERSONNEL);
		assertEquals(linkedEnumProcess.next(LinkedTest.PROJECT_LEADER), LinkedTest.PROJECT_MANAGER);
		assertEquals(linkedEnumProcess.next(LinkedTest.PROJECT_MANAGER), LinkedTest.DEPARTMENT_MANAGER);
		assertEquals(linkedEnumProcess.pre(LinkedTest.PROJECT_MANAGER), LinkedTest.PROJECT_LEADER);
		assertEquals(linkedEnumProcess.pre(LinkedTest.PERSONNEL), LinkedTest.DEPARTMENT_MANAGER);
		
		linkedEnumProcess.add(LinkedTest.COMPLATE);
		assertEquals(linkedEnumProcess.next(LinkedTest.PERSONNEL), LinkedTest.COMPLATE);
		assertEquals(linkedEnumProcess.pre(LinkedTest.COMPLATE, Process.THROUGH), LinkedTest.PERSONNEL);
		assertFalse(linkedEnumProcess.isEmpty());
		assertEquals(linkedEnumProcess.size(), LinkedTest.values().length);
	}
	
	@Test
	public void testProcessFactory() {
		assertEquals(LinkedTest.START.nextProcess(Process.THROUGH), LinkedTest.APPLY);
		assertEquals(LinkedTest.DEPARTMENT_MANAGER.nextProcess(Process.THROUGH), LinkedTest.PERSONNEL);
		assertEquals(LinkedTest.DEPARTMENT_MANAGER.nextProcess(Process.NOT_THROUGH), LinkedTest.COMPLATE);
	}
	
	public enum LinkedTest implements LinkedEnumProcessFactory<LinkedTest> {
		START(null, "请假申请"),
		
		APPLY(null, "请假申请"),
		
		PROJECT_LEADER("projectLeaderId", "项目组长审批"),
		
		PROJECT_MANAGER("projectManagerId", "项目经理审批"),
		
		DEPARTMENT_MANAGER("departmentManagerId", "部门经理审批"),
		
		PERSONNEL(null, "人事自动审批"),
		
		COMPLATE(null, "流程结束");
		
		
		private String assignee;
		
		private String msg;
		
		LinkedTest(String assignee, String msg){
			this.assignee = assignee;
			this.msg = msg;
		}

		@Override
		public LinkedTest getEnumType() {
			return this;
		}

		@Override
		public LinkedTest[] getEnumTypes() {
			return LinkedTest.values();
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
		public LinkedEnumProcess<LinkedTest> getEnumProcess() {
			LinkedEnumProcess<LinkedTest> linkedTest = new LinkedEnumProcess<>();
			linkedTest.add(LinkedTest.START);
			linkedTest.add(LinkedTest.APPLY);
			linkedTest.add(LinkedTest.PROJECT_LEADER);
			linkedTest.add(LinkedTest.PROJECT_MANAGER);
			linkedTest.add(LinkedTest.DEPARTMENT_MANAGER);
			linkedTest.add(LinkedTest.PERSONNEL);
			linkedTest.add(LinkedTest.COMPLATE);
			return linkedTest;
		}
		
	}
}
