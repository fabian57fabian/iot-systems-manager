package com.fabian57fabian.app.controller;
import com.fabian57fabian.app.model.entities.SystemEntity;
import com.fabian57fabian.app.model.entities.SystemHeader;
import com.fabian57fabian.app.model.service.SystemService;
import com.fabian57fabian.app.view.IotView;

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
	
	private SystemService systemService;
	private IotView view;
	
	@Rule
	public VerificationCollector collector = MockitoJUnit.collector();
	
	@Before
	public void setUp() throws Exception {
		systemService = mock(SystemService.class);
		view = mock(IotView.class);
		controller = new SystemsManagerController(systemService, view);
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testGetSystemsNames_one() {
		List<SystemHeader> list = new ArrayList<SystemHeader>();
		list.add(new SystemHeader(0, "foo"));
		when(systemService.getAllSystems()).thenReturn(list);
		
		controller.viewAllSystems();
		
		verify(view).showSystems(list);
		
		// Verify systemService not called?
		//assertThat(systemService.toString()).isEqualTo(0);
	}

	@Test
	public void testGetOneSystem_right() {
		int id = 1;
		SystemEntity sys = new SystemEntity(id, "bar", "Description of 'bar' ", false);
		when(systemService.getSystem(id)).thenReturn(sys);
		
		controller.expandOneSystem(id);
		verify(view).showOneSystem(sys);
	}

	@Test
	public void testGetOneSystem_noexist() {
		int id = 1;
		String error_msg = "System not found.";
		when(systemService.getSystem(id)).thenReturn(null);
		
		controller.expandOneSystem(id);
		verify(view, never()).showOneSystem(null);
		verify(view).showOneSystemError(error_msg);
	}

}
