package com.fabian57fabian.app.controller;
import com.fabian57fabian.app.model.entities.SystemEntity;
import com.fabian57fabian.app.model.entities.SystemHeader;
import com.fabian57fabian.app.model.repository.SystemRepository;
import com.fabian57fabian.app.model.service.SystemService;
import com.fabian57fabian.app.view.IotView;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import junit.framework.TestCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.VerificationCollector;


public class SystemsManagerControllerTest extends TestCase{
	
	private SystemsManagerController controller;
	
	private SystemRepository db_connector;
	private SystemService systemService;
	private IotView view;
	
	@Rule
	public VerificationCollector collector = MockitoJUnit.collector();
	
	@Before
	public void setUp() throws Exception {
		db_connector = mock(SystemRepository.class);
		systemService = mock(SystemService.class);
		view = mock(IotView.class);
		controller = new SystemsManagerController(db_connector, systemService, view);
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testGetSystemsNames_one() {
		List<SystemHeader> list = new ArrayList<SystemHeader>();
		list.add(new SystemHeader(0, "foo"));
		when(db_connector.RetrieveSystemNames()).thenReturn(list);
		
		controller.viewAllSystems();
		
		verify(view).ShowSystems(list);
		
		// Verify systemService not called?
		//assertThat(systemService.toString()).isEqualTo(0);
	}

	@Test
	public void testGetOneSystem_right() {
		int id = 1;
		SystemEntity sys = new SystemEntity(id, "bar", "Description of 'bar' ", false);
		when(db_connector.GetSystemById(id)).thenReturn(sys);
		
		controller.ExpandOneSystem(id);
		verify(view).ShowOneSystem(sys);
	}

}
