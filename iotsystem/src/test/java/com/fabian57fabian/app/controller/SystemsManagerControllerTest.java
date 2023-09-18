package com.fabian57fabian.app.controller;
import com.fabian57fabian.app.model.DatabaseConnector;
import com.fabian57fabian.app.model.entities.SystemHeader;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

import static org.mockito.Mockito.*;

public class SystemsManagerControllerTest extends TestCase{
	
	private SystemsManagerController controller;
	
	@Before
	public void setUp() throws Exception {
		controller = new SystemsManagerController();
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testGetSystemsNames_notnull() {
		DatabaseConnector db = mock(DatabaseConnector.class);
		
		List<SystemHeader> list = new ArrayList<SystemHeader>();
		list.add(new SystemHeader(0, "foo"));
		when(db.RetrieveSystemNames()).thenReturn(list);
		
		List<SystemHeader> res = controller.GetSystems();
		assertNotNull(res);
	}
}
