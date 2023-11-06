package com.fabian57fabian.app.controller;
import com.fabian57fabian.app.model.entities.SensorEntity;
import com.fabian57fabian.app.model.entities.SystemEntity;
import com.fabian57fabian.app.model.service.ISensorService;
import com.fabian57fabian.app.model.service.ISystemService;
import com.fabian57fabian.ui.view.IotView;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import junit.framework.TestCase;

import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.VerificationCollector;


public class SystemsManagerControllerTest extends TestCase{
	
	private SystemsManagerController controller;
	
	private ISystemService systemService;
	private ISensorService sensorService;
	private IotView view;
	
	@Rule
	public VerificationCollector collector = MockitoJUnit.collector();
	
	@Before
	public void setUp() throws Exception {
		systemService = mock(ISystemService.class);
		sensorService = mock(ISensorService.class);
		view = mock(IotView.class);
		controller = new SystemsManagerController(systemService, sensorService, view);
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testGetSystemsNames_one() {
		List<SystemEntity> list = new ArrayList<SystemEntity>();
		list.add(new SystemEntity(0, "bar", "Description of 'bar' ", false));
		when(systemService.getSystemNames()).thenReturn(list);
		
		controller.viewAllSystems();
		
		verify(view).showSystems(list);
		
		// Verify systemService not called?
		//assertThat(systemService.toString()).isEqualTo(0);
	}

	@Test
	public void testGetOneSystem_right() {
		int systemId = 10;
		SystemEntity sys = new SystemEntity(systemId, "bar", "Description of 'bar' ", false);
		when(systemService.getSystemById(systemId)).thenReturn(sys);
		
		List<SensorEntity> sensors = new ArrayList<SensorEntity>();
		sensors.add(new SensorEntity(2,  "foo2",  "wow2",  0.1, 0.2, systemId));
		sensors.add(new SensorEntity(3,  "foo3",  "wow3",  0.1, 0.2, systemId));
		when(sensorService.getSensorsOfSystem(systemId)).thenReturn(sensors);
		
		controller.expandOneSystem(systemId);
		verify(view).showOneSystem(sys);
		verify(view).ShowSensorsOfSystem(sensors);
	}

	@Test
	public void testGetOneSystem_noexist() {
		int id = 1;
		String error_msg = "System not found.";
		when(systemService.getSystemById(id)).thenReturn(null);
		
		controller.expandOneSystem(id);
		verify(view, never()).showOneSystem(null);
		verify(view, never()).ShowSensorsOfSystem(null);
		verify(view).showOneSystemError(error_msg, null);
	}
	
	@Test
	public void testaddSystem() {
		SystemEntity system = new SystemEntity(10, "bar", "Description of 'bar' ", false);
		controller.addSystem(system);
		verify(systemService).create(system);
		verify(view).onSystemAdded(system);
	}
	
	@Test
	public void testremoveSystem() {
		SystemEntity system = new SystemEntity(10, "bar", "Description of 'bar' ", false);
		controller.removeSystem(system);
		verify(systemService).delete(system.getId());
		verify(view).onSystemRemoved(system);
	}
	
	@Test
	public void testAddSensor() {
		SensorEntity sensor =new SensorEntity(0,  "foo", "description of foo",  0.1,  0.2, 10);
		controller.addSensor(sensor);
		verify(sensorService).create(sensor);
		verify(view).onSensorAdded(sensor);
	}
	
	@Test
	public void testRemoveSensor() {
		SensorEntity sensor =new SensorEntity(0,  "foo", "description of foo",  0.1,  0.2, 10);
		controller.removeSensor(sensor);
		verify(sensorService).delete(sensor.getId());
		verify(view).onSensorRemoved(sensor);
	}
}
