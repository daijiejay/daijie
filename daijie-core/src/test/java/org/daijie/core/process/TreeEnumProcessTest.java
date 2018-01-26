package org.daijie.core.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.daijie.core.process.factory.TreeEnumProcessFactory;
import org.junit.Test;

public class TreeEnumProcessTest {

	@Test
	public void testProcess() {
		TreeEnumProcess<TreeTest> treeEnumProcess = new TreeEnumProcess<>();
		assertTrue(treeEnumProcess.isEmpty());
		
		treeEnumProcess.add(TreeTest.START);
		treeEnumProcess.add(TreeTest.APPLY);
		assertEquals(treeEnumProcess.next(TreeTest.START), TreeTest.APPLY);
		assertEquals(treeEnumProcess.pre(TreeTest.APPLY), TreeTest.START);
		
		treeEnumProcess.add(TreeTest.PROJECT_LEADER);
		assertEquals(treeEnumProcess.next(treeEnumProcess.next(TreeTest.START)), TreeTest.PROJECT_LEADER);
		assertEquals(treeEnumProcess.pre(treeEnumProcess.pre(TreeTest.PROJECT_LEADER)), TreeTest.START);
		
		treeEnumProcess.add(new TreeTest[]{TreeTest.PROJECT_MANAGER,TreeTest.DEPARTMENT_MANAGER});
		treeEnumProcess.add(TreeTest.PERSONNEL);
		assertEquals(treeEnumProcess.next(TreeTest.PROJECT_LEADER), TreeTest.PROJECT_MANAGER);
		assertEquals(treeEnumProcess.next(TreeTest.PROJECT_MANAGER), TreeTest.DEPARTMENT_MANAGER);
		assertEquals(treeEnumProcess.pre(TreeTest.PROJECT_MANAGER), TreeTest.PROJECT_LEADER);
		assertEquals(treeEnumProcess.pre(TreeTest.PERSONNEL), TreeTest.DEPARTMENT_MANAGER);
		
		treeEnumProcess.add(TreeTest.DEPARTMENT_MANAGER, TreeTest.REFUSE, Process.NOT_THROUGH);
		assertNull(treeEnumProcess.next(TreeTest.PERSONNEL));
		assertEquals(treeEnumProcess.next(TreeTest.DEPARTMENT_MANAGER), TreeTest.PERSONNEL);
		assertEquals(treeEnumProcess.next(TreeTest.DEPARTMENT_MANAGER, Process.NOT_THROUGH), TreeTest.REFUSE);
		assertEquals(treeEnumProcess.pre(TreeTest.PERSONNEL), TreeTest.DEPARTMENT_MANAGER);
		assertEquals(treeEnumProcess.pre(TreeTest.REFUSE, Process.NOT_THROUGH), TreeTest.DEPARTMENT_MANAGER);
		
		treeEnumProcess.add(TreeTest.COMPLATE);
		assertEquals(treeEnumProcess.next(TreeTest.PERSONNEL), TreeTest.COMPLATE);
		assertEquals(treeEnumProcess.pre(TreeTest.COMPLATE, Process.THROUGH), TreeTest.PERSONNEL);
		assertFalse(treeEnumProcess.isEmpty());
		assertEquals(treeEnumProcess.size(), TreeTest.values().length);
	}
	
	@Test
	public void testProcessFactory() {
		assertEquals(TreeTest.START.nextProcess(Process.THROUGH), TreeTest.APPLY);
		assertEquals(TreeTest.DEPARTMENT_MANAGER.nextProcess(Process.THROUGH), TreeTest.PERSONNEL);
		assertEquals(TreeTest.DEPARTMENT_MANAGER.nextProcess(Process.NOT_THROUGH), TreeTest.REFUSE);
		assertEquals(TreeTest.REFUSE.preProcess(Process.NOT_THROUGH), TreeTest.DEPARTMENT_MANAGER);
	}
	
	public enum TreeTest implements TreeEnumProcessFactory<TreeTest> {
		START(null, "请假申请"),
		
		APPLY(null, "请假申请"),
		
		PROJECT_LEADER("projectLeaderId", "项目组长审批"),
		
		PROJECT_MANAGER("projectManagerId", "项目经理审批"),
		
		DEPARTMENT_MANAGER("departmentManagerId", "部门经理审批"),
		
		PERSONNEL(null, "人事自动审批"),
		
		COMPLATE(null, "请假完成"),
		
		REFUSE(null, "已拒绝");
		
		
		private String assignee;
		
		private String msg;
		
		TreeTest(String assignee, String msg){
			this.assignee = assignee;
			this.msg = msg;
		}

		@Override
		public TreeTest getEnumType() {
			return this;
		}

		@Override
		public TreeTest[] getEnumTypes() {
			return TreeTest.values();
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
		public TreeEnumProcess<TreeTest> getEnumProcess() {
			TreeEnumProcess<TreeTest> treeTest = new TreeEnumProcess<>();
			treeTest.add(TreeTest.START);
			treeTest.add(TreeTest.APPLY);
			treeTest.add(TreeTest.PROJECT_LEADER);
			treeTest.add(TreeTest.PROJECT_MANAGER);
			treeTest.add(TreeTest.DEPARTMENT_MANAGER);
			treeTest.add(TreeTest.PERSONNEL);
			treeTest.add(TreeTest.DEPARTMENT_MANAGER, TreeTest.REFUSE, Process.NOT_THROUGH);
			treeTest.add(TreeTest.PERSONNEL, TreeTest.COMPLATE, Process.THROUGH);
			return treeTest;
		}
		
	}
}
