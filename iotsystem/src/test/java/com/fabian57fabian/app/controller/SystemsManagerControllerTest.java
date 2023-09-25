package com.fabian57fabian.app.controller;
import com.fabian57fabian.app.model.DatabaseConnector;
import com.fabian57fabian.app.model.entities.SystemEntity;
import com.fabian57fabian.app.model.entities.SystemHeader;
import com.fabian57fabian.app.view.IotView;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

import static org.mockito.Mockito.*;


public class SystemsManagerControllerTest extends TestCase{
	
	private SystemsManagerController controller;
	
	private DatabaseConnector db_connector;
	private IotView view;
	
	@Before
	public void setUp() throws Exception {
		db_connector = mock(DatabaseConnector.class);
		view = mock(IotView.class)
		controller = new SystemsManagerController(db_connector, view);
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testGetSystemsNames_right() {
		List<SystemHeader> list = new ArrayList<SystemHeader>();
		list.add(new SystemHeader(0, "foo"));
		when(db_connector.RetrieveSystemNames()).thenReturn(list);
		
		controller.viewAllSystems();
		
		verify(view).ShowSystems(list);
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
