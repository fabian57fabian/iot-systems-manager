package com.fabian57fabian.app.model.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fabian57fabian.app.model.entities.SystemEntity;
import com.fabian57fabian.app.model.repository.SystemRepository;

public class SystemServiceTest {
	SystemService systemService;

	SystemRepository systemRepository;

	@Before
	public void setUp() throws Exception {
		systemRepository = mock(SystemRepository.class);
		systemService = new SystemService(systemRepository);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetSystemNamesWhenIsEmpty() {
		when(systemRepository.retrieveSystemNames()).thenReturn(new ArrayList<SystemEntity>());
		assertThat(systemService.getSystemNames()).isEmpty();
		verify(systemRepository).retrieveSystemNames();
	}

	@Test
	public void testGetSystemNamesWhenIsNotEmpty() {
		List<SystemEntity> systems = new ArrayList<SystemEntity>();
		systems.add(new SystemEntity(0, "foo", "", true));
		systems.add(new SystemEntity(1, "bar", "", false));
		when(systemRepository.retrieveSystemNames()).thenReturn(systems);
		assertThat(systemService.getSystemNames()).containsExactly(systems.get(0), systems.get(1));
		verify(systemRepository).retrieveSystemNames();
	}

	@Test
	public void testFindByIdNotFound() {
		int id = 0;
		when(systemRepository.getSystemById(id)).thenReturn(null);
		assertThat(systemService.getSystemById(id)).isNull();
		verify(systemRepository).getSystemById(id);
	}

	@Test
	public void testFindByIdFound() {
		int id = 0;
		SystemEntity system = new SystemEntity(id, "foo", "", true);
		when(systemRepository.getSystemById(id)).thenReturn(system);
		assertThat(systemService.getSystemById(id)).isEqualTo(system);
		verify(systemRepository).getSystemById(id);
	}

	@Test
	public void testCreateWhenNotExists() {
		when(systemRepository.getSystemById(0)).thenReturn(null);
		SystemEntity system = new SystemEntity(0, "foo", "", true);
		Boolean res = systemService.create(system);
		assertTrue(res);
		verify(systemRepository, times(1)).save(system);
	}

	@Test
	public void testCreateWhenIdAlreadyExists() {
		int id = 3;
		when(systemRepository.getSystemById(id)).thenReturn(new SystemEntity(id, "bar", "d", true));

		SystemEntity system = new SystemEntity(id, "foo", "", true);
		Boolean res = systemService.create(system);
		assertFalse(res);
		verify(systemRepository, never()).save(system);
	}

	@Test
	public void testDelete() {
		int id = 0;
		systemService.delete(id);
		verify(systemRepository, times(1)).delete(id);
	}

}
