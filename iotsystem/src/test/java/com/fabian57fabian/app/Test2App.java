package com.fabian57fabian.app;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Test2App {
	private App app;
	
	@Before
	public void setup(){
		app = new App();
	}
	
	@Test
	public void testsayHello() {
		assertEquals("Hallo", app.sayHello());
	}

}
