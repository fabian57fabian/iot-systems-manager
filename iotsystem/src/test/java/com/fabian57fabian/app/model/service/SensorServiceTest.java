package com.fabian57fabian.app.model.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fabian57fabian.app.model.entities.SensorEntity;
import com.fabian57fabian.app.model.repository.SensorRepository;

public class SensorServiceTest {
	SensorService sensorService;

	SensorRepository sensorRepository;

	@Before
	public void setUp() throws Exception {
		sensorRepository = mock(SensorRepository.class);
		sensorService = new SensorService(sensorRepository);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetSensorNamesWhenIsEmpty() {
		when(sensorRepository.retrieveSensorsNames()).thenReturn(new ArrayList<SensorEntity>());
		assertThat(sensorService.getSensorNames()).isEmpty();
		verify(sensorRepository).retrieveSensorsNames();
	}

	@Test
	public void testGetSensorNamesWhenIsNotEmpty() {
		List<SensorEntity> sensors = new ArrayList<SensorEntity>();
		sensors.add(new SensorEntity(0, "foo", "", 0.1, 0.2, -1));
		sensors.add(new SensorEntity(1, "bar", "", 0.3, 0.4, -1));
		when(sensorRepository.retrieveSensorsNames()).thenReturn(sensors);
		assertThat(sensorService.getSensorNames()).containsExactly(sensors.get(0), sensors.get(1));
		verify(sensorRepository).retrieveSensorsNames();
	}

	@Test
	public void testGgetSensorsOfSystemWhenIsEmpty() {
		int id = 10;
		when(sensorRepository.getSensorsOfSystem(id)).thenReturn(new ArrayList<SensorEntity>());
		assertThat(sensorService.getSensorsOfSystem(id)).isEmpty();
		verify(sensorRepository).getSensorsOfSystem(id);
	}

	@Test
	public void testGgetSensorsOfSystemWhenIsNotEmpty() {
		int system_id = 10;
		List<SensorEntity> sensors = new ArrayList<SensorEntity>();
		sensors.add(new SensorEntity(0, "foo", "", 0.1, 0.2, system_id));
		sensors.add(new SensorEntity(1, "bar", "", 0.3, 0.4, system_id));
		when(sensorRepository.getSensorsOfSystem(system_id)).thenReturn(sensors);
		assertThat(sensorService.getSensorsOfSystem(system_id)).containsExactly(sensors.get(0), sensors.get(1));
		verify(sensorRepository).getSensorsOfSystem(system_id);
	}

	@Test
	public void testFindByIdNotFound() {
		int id = 0;
		when(sensorRepository.getSensorById(id)).thenReturn(null);
		assertThat(sensorService.getSensorById(id)).isNull();
		verify(sensorRepository).getSensorById(id);
	}

	@Test
	public void testFindByIdFound() {
		int id = 0;
		SensorEntity sensor = new SensorEntity(id, "foo", "", 0.1, 0.2, -1);
		when(sensorRepository.getSensorById(id)).thenReturn(sensor);
		assertThat(sensorService.getSensorById(id)).isEqualTo(sensor);
		verify(sensorRepository).getSensorById(id);
	}

	@Test
	public void testCreate() {
		SensorEntity sensor = new SensorEntity(0, "foo", "", 0.1, 0.2, -1);
		sensorService.create(sensor);
		verify(sensorRepository, times(1)).save(sensor);
	}

	@Test
	public void testDelete() {
		int id = 0;
		sensorService.delete(id);
		verify(sensorRepository, times(1)).delete(id);
	}

}
