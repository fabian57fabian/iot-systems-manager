package com.fabian57fabian.app;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.ClassRule;
import org.testcontainers.containers.GenericContainer;

import com.fabian57fabian.app.model.entities.SensorEntity;

import org.junit.Test;

import com.fabian57fabian.app.model.repository.SensorMongoRepository;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.junit.After;
import org.junit.Before;

public class SensorMongoRepositoryTestcontainersIT {
	@SuppressWarnings("rawtypes")
	@ClassRule
	public static final GenericContainer mongo = new GenericContainer("mongo:4.4.3").withExposedPorts(27017);

	public static final String DB_NAME = "iotsystems";
	public static final String SENSOR_COLLECTION_NAME = "sensor";

	private MongoClient client;
	private SensorMongoRepository sensorRepository;
	private MongoCollection<Document> sensorCollection;

	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(mongo.getContainerIpAddress(), mongo.getMappedPort(27017)));
		sensorRepository = new SensorMongoRepository(client, DB_NAME, SENSOR_COLLECTION_NAME);
		MongoDatabase database = client.getDatabase(DB_NAME);
		// make sure we always start with a clean database
		database.drop();
		sensorCollection = database.getCollection(SENSOR_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		client.close();
	}

	private void addTestSensorToDatabase(Integer id, String name, String description, String unit, double offset,
			double multiplier, int systemId) {
		sensorCollection.insertOne(new Document().append("id", id).append("name", name)
				.append("description", description).append("unit", unit).append("offset", offset)
				.append("multiplier", multiplier).append("systemId", systemId));
	}

	@Test
	public void testRetrieveSensorsNames() {
		addTestSensorToDatabase(1, "test1", "wow", "mm", 0.1, 0.2, -1);
		addTestSensorToDatabase(2, "test2", "woow", "mm", 0.3, 0.4, -1);
		assertThat(sensorRepository.retrieveSensorsNames()).containsExactly(
				new SensorEntity(1, "test1", "wow", "mm", 0.1, 0.2, -1),
				new SensorEntity(2, "test2", "woow", "mm", 0.3, 0.4, -1));
	}

	@Test
	public void testFindById() {
		addTestSensorToDatabase(1, "test1", "wow", "mm", 0.1, 0.2, -1);
		addTestSensorToDatabase(2, "test2", "woow", "mm", 0.3, 0.4, -1);
		assertThat(sensorRepository.getSensorById(2))
				.isEqualTo(new SensorEntity(2, "test2", "woow", "mm", 0.3, 0.4, -1));
	}
	
	@Test
	public void testGetSensorsOfSystem() {
		int systemId = 10;
		addTestSensorToDatabase(1, "test1", "wow", "mm", 0.1, 0.2, 5);
		addTestSensorToDatabase(2, "test2", "woow", "mm", 0.3, 0.4, systemId);
		assertThat(sensorRepository.getSensorsOfSystem(systemId))
				.containsExactly(new SensorEntity(2, "test2", "woow", "mm", 0.3, 0.4, systemId));
	}

	@Test
	public void testSave() {
		SensorEntity sensor = new SensorEntity(1, "test1", "wow", "mm", 0.1, 0.2, -1);
		sensorRepository.save(sensor);
		assertThat(readAllSensorsFromDatabase()).containsExactly(sensor);
	}

	private List<SensorEntity> readAllSensorsFromDatabase() {
		return StreamSupport.stream(sensorCollection.find().spliterator(), false)
				.map(d -> new SensorEntity((int) d.get("id"), "" + d.get("name"), "" + d.get("description"),
						"" + d.get("unit"), (double) d.get("offset"), (double) d.get("multiplier"),
						(int) d.get("systemId")))
				.collect(Collectors.toList());
	}

	@Test
	public void testDelete() {
		addTestSensorToDatabase(1, "test1", "wow", "mm", 0.1, 0.2, -1);
		sensorRepository.delete(1);
		assertThat(readAllSensorsFromDatabase()).isEmpty();
	}

	@Test
	public void testUpdate() {
		addTestSensorToDatabase(1, "test1", "wow", "mm", 0.1, 0.2, -1);
		SensorEntity sensor = new SensorEntity(1, "test1", "wow", "mm", 2.6, 5.9, -1);
		sensorRepository.update(1, sensor);
		assertThat(readAllSensorsFromDatabase()).containsExactly(sensor);
	}

}
