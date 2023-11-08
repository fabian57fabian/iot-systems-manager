package com.fabian57fabian.app.model.repository;

import static org.assertj.core.api.Assertions.*;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.fabian57fabian.app.model.entities.SensorEntity;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SensorMongoRepositoryTest {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	public static final String DB_NAME = "iotsystems";
	public static final String SENSOR_COLLECTION_NAME = "sensor";

	private MongoClient client;
	private SensorMongoRepository sensorRepository;
	private MongoCollection<Document> sensorCollection;

	@BeforeClass
	public static void setupServer() throws Exception {
		server = new MongoServer(new MemoryBackend());
		// bind on a random local port
		serverAddress = server.bind();
	}

	@AfterClass
	public static void shutdownServer() throws Exception {
		server.shutdown();
	}

	@Before
	public void setUp() throws Exception {
		client = new MongoClient(new ServerAddress(serverAddress));
		sensorRepository = new SensorMongoRepository(client, DB_NAME, SENSOR_COLLECTION_NAME);
		MongoDatabase database = client.getDatabase(DB_NAME);
		// make sure we always start with a clean database
		database.drop();
		sensorCollection = database.getCollection(SENSOR_COLLECTION_NAME);
	}

	@After
	public void tearDown() throws Exception {
		client.close();
	}

	@Test
	public void testRetrieveSensorNamesWhenDatabaseIsEmpty() {
		assertThat(sensorRepository.retrieveSensorsNames()).isEmpty();
	}

	private void addTestSensorToDatabase(Integer id, String name, String description, String unit, double offset,
			double multiplier, int systemId) {
		sensorCollection.insertOne(new Document().append("id", id).append("name", name)
				.append("description", description).append("unit", unit).append("offset", offset)
				.append("multiplier", multiplier).append("systemId", systemId));
	}

	@Test
	public void testRetrieveSensorNamesWhenDatabaseIsNotEmpty() {
		addTestSensorToDatabase(1, "test1", "wow", "mm", 0.1, 0.2, -1);
		addTestSensorToDatabase(2, "test2", "woow", "mm", 0.3, 0.4, -1);
		assertThat(sensorRepository.retrieveSensorsNames()).containsExactly(
				new SensorEntity(1, "test1", "wow", "mm", 0.1, 0.2, -1),
				new SensorEntity(2, "test2", "woow", "mm", 0.3, 0.4, -1));
	}

	@Test
	public void testGetSensorsOfSystemWhenDatabaseIsEmpty() {
		assertThat(sensorRepository.getSensorsOfSystem(0)).isEmpty();
	}

	@Test
	public void testGetSensorsOfSystemWhenDatabaseIsNotEmpty() {
		int systemId = 10;
		addTestSensorToDatabase(1, "test1", "wow", "mm", 0.1, 0.2, systemId);
		addTestSensorToDatabase(2, "test2", "woow", "mm", 0.3, 0.4, systemId);
		addTestSensorToDatabase(3, "test3", "wooow", "mm", 0.5, 0.6, systemId + 1);
		assertThat(sensorRepository.getSensorsOfSystem(systemId)).containsExactly(
				new SensorEntity(1, "test1", "wow", "mm", 0.1, 0.2, systemId),
				new SensorEntity(2, "test2", "woow", "mm", 0.3, 0.4, systemId));
	}

	@Test
	public void testFindByIdNotFound() {
		assertThat(sensorRepository.getSensorById(1)).isNull();
	}

	@Test
	public void testFindByIdFound() {
		addTestSensorToDatabase(1, "test1", "wow", "mm", 0.1, 0.2, -1);
		addTestSensorToDatabase(2, "test2", "woow", "mm", 0.3, 0.4, -1);
		assertThat(sensorRepository.getSensorById(2))
				.isEqualTo(new SensorEntity(2, "test2", "woow", "mm", 0.3, 0.4, -1));
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
						(int) d.getInteger("systemId")))
				.collect(Collectors.toList());
	}

	@Test
	public void testDelete() {
		addTestSensorToDatabase(1, "test1", "wow", "mm", 0.1, 0.2, -1);
		sensorRepository.delete(1);
		assertThat(readAllSensorsFromDatabase()).isEmpty();
	}

}
