package com.fabian57fabian.app.controller;
import com.fabian57fabian.app.model.entities.SystemEntity;
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
	private IotView view;
	
	@Rule
	public VerificationCollector collector = MockitoJUnit.collector();
	
	@Before
	public void setUp() throws Exception {
		systemService = mock(ISystemService.class);
		view = mock(IotView.class);
		controller = new SystemsManagerController(systemService, view);
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
		int id = 1;
		SystemEntity sys = new SystemEntity(id, "bar", "Description of 'bar' ", false);
		when(systemService.getSystemById(id)).thenReturn(sys);
		
		controller.expandOneSystem(id);
		verify(view).showOneSystem(sys);
	}

	@Test
	public void testGetOneSystem_noexist() {
		int id = 1;
		String error_msg = "System not found.";
		when(systemService.getSystemById(id)).thenReturn(null);
		
		controller.expandOneSystem(id);
		verify(view, never()).showOneSystem(null);
		verify(view).showOneSystemError(error_msg, null);
	}

}
