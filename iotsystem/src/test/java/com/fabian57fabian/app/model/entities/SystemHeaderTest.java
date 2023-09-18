package com.fabian57fabian.app.model.entities;

import junit.framework.TestCase;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SystemHeaderTest extends TestCase {
	
	private SystemHeader system_header;
	
	@Before
	public void setUp() throws Exception {
		system_header = new SystemHeader(0, "foo");
	}

	@Test
	public void testEquals_true() {
		SystemHeader new_sh = new SystemHeader(system_header.GetId(), system_header.GetName());
		assertTrue(system_header.equals(new_sh));
	}

}
