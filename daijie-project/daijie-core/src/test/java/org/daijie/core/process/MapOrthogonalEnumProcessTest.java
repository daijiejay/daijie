package org.daijie.core.process;

import org.daijie.core.process.factory.MapOrthogonalEnumProcessFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapOrthogonalEnumProcessTest {

	@Test
	public void testProcess() {
		MapOrthogonalEnumProcess<MapOrthogonalTest> mapOrthogonalEnumProcess = new MapOrthogonalEnumProcess<>(MapOrthogonalTest.values().length);
		assertTrue(mapOrthogonalEnumProcess.isEmpty());
		
		mapOrthogonalEnumProcess.add(MapOrthogonalTest.START);
		mapOrthogonalEnumProcess.add(MapOrthogonalTest.APPLY);
		assertEquals(mapOrthogonalEnumProcess.next(MapOrthogonalTest.START), MapOrthogonalTest.APPLY);
		assertEquals(mapOrthogonalEnumProcess.pre(MapOrthogonalTest.APPLY), MapOrthogonalTest.START);
		
		mapOrthogonalEnumProcess.add(MapOrthogonalTest.PROJECT_LEADER);
		assertEquals(mapOrthogonalEnumProcess.next(mapOrthogonalEnumProcess.next(MapOrthogonalTest.START)), MapOrthogonalTest.PROJECT_LEADER);
		assertEquals(mapOrthogonalEnumProcess.pre(mapOrthogonalEnumProcess.pre(MapOrthogonalTest.PROJECT_LEADER)), MapOrthogonalTest.START);
		
		mapOrthogonalEnumProcess.add(new MapOrthogonalTest[]{MapOrthogonalTest.PROJECT_MANAGER, MapOrthogonalTest.DEPARTMENT_MANAGER});
		mapOrthogonalEnumProcess.add(MapOrthogonalTest.PERSONNEL);
		assertEquals(mapOrthogonalEnumProcess.next(MapOrthogonalTest.PROJECT_LEADER), MapOrthogonalTest.PROJECT_MANAGER);
		assertEquals(mapOrthogonalEnumProcess.next(MapOrthogonalTest.PROJECT_MANAGER), MapOrthogonalTest.DEPARTMENT_MANAGER);
		assertEquals(mapOrthogonalEnumProcess.pre(MapOrthogonalTest.PROJECT_MANAGER), MapOrthogonalTest.PROJECT_LEADER);
		assertEquals(mapOrthogonalEnumProcess.pre(MapOrthogonalTest.PERSONNEL), MapOrthogonalTest.DEPARTMENT_MANAGER);
		
		mapOrthogonalEnumProcess.add(MapOrthogonalTest.DEPARTMENT_MANAGER, MapOrthogonalTest.REFUSE, Process.NOT_THROUGH);
		assertNull(mapOrthogonalEnumProcess.next(MapOrthogonalTest.PERSONNEL));
		assertEquals(mapOrthogonalEnumProcess.next(MapOrthogonalTest.DEPARTMENT_MANAGER), MapOrthogonalTest.PERSONNEL);
		assertEquals(mapOrthogonalEnumProcess.next(MapOrthogonalTest.DEPARTMENT_MANAGER, Process.NOT_THROUGH), MapOrthogonalTest.REFUSE);
		assertEquals(mapOrthogonalEnumProcess.pre(MapOrthogonalTest.PERSONNEL), MapOrthogonalTest.DEPARTMENT_MANAGER);
		assertEquals(mapOrthogonalEnumProcess.pre(MapOrthogonalTest.REFUSE, Process.NOT_THROUGH), MapOrthogonalTest.DEPARTMENT_MANAGER);

		mapOrthogonalEnumProcess.add(MapOrthogonalTest.PERSONNEL, MapOrthogonalTest.COMPLATE, Process.THROUGH);
		assertEquals(mapOrthogonalEnumProcess.next(MapOrthogonalTest.PERSONNEL), MapOrthogonalTest.COMPLATE);
		assertEquals(mapOrthogonalEnumProcess.pre(MapOrthogonalTest.COMPLATE, Process.THROUGH), MapOrthogonalTest.PERSONNEL);
		assertFalse(mapOrthogonalEnumProcess.isEmpty());
		assertEquals(mapOrthogonalEnumProcess.size(), MapOrthogonalTest.values().length);
	}
	
	@Test
	public void testProcessFactory() {
		assertEquals(MapOrthogonalTest.START.nextProcess(Process.THROUGH), MapOrthogonalTest.APPLY);
		assertEquals(MapOrthogonalTest.DEPARTMENT_MANAGER.nextProcess(Process.THROUGH), MapOrthogonalTest.PERSONNEL);
		assertEquals(MapOrthogonalTest.DEPARTMENT_MANAGER.nextProcess(Process.NOT_THROUGH), MapOrthogonalTest.REFUSE);
		assertEquals(MapOrthogonalTest.REFUSE.preProcess(Process.NOT_THROUGH), MapOrthogonalTest.DEPARTMENT_MANAGER);
	}
	
	public enum MapOrthogonalTest implements MapOrthogonalEnumProcessFactory<MapOrthogonalTest> {
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
		
		MapOrthogonalTest(String assignee, String msg){
			this.assignee = assignee;
			this.msg = msg;
		}

		@Override
		public MapOrthogonalTest getEnumType() {
			return this;
		}

		@Override
		public MapOrthogonalTest[] getEnumTypes() {
			return MapOrthogonalTest.values();
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
		public MapOrthogonalEnumProcess<MapOrthogonalTest> getEnumProcess() {
			MapOrthogonalEnumProcess<MapOrthogonalTest> mapOrthogonalEnumProcess = new MapOrthogonalEnumProcess<>(MapOrthogonalTest.values().length);
			mapOrthogonalEnumProcess.add(MapOrthogonalTest.START);
			mapOrthogonalEnumProcess.add(MapOrthogonalTest.APPLY);
			mapOrthogonalEnumProcess.add(MapOrthogonalTest.PROJECT_LEADER);
			mapOrthogonalEnumProcess.add(MapOrthogonalTest.PROJECT_MANAGER);
			mapOrthogonalEnumProcess.add(MapOrthogonalTest.DEPARTMENT_MANAGER);
			mapOrthogonalEnumProcess.add(MapOrthogonalTest.PERSONNEL);
			mapOrthogonalEnumProcess.add(MapOrthogonalTest.DEPARTMENT_MANAGER, MapOrthogonalTest.REFUSE, Process.NOT_THROUGH);
			mapOrthogonalEnumProcess.add(MapOrthogonalTest.PERSONNEL, MapOrthogonalTest.COMPLATE, Process.THROUGH);
			return mapOrthogonalEnumProcess;
		}
		
	}
}
