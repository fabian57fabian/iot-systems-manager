package com.fabian57fabian.app.model.repository;

import static org.assertj.core.api.Assertions.*;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.fabian57fabian.app.model.entities.SystemEntity;
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

public class SystemMongoRepositoryTest {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	public static final String DB_NAME = "iotsystems";
	public static final String SYSTEM_COLLECTION_NAME = "system";

	private MongoClient client;
	private SystemMongoRepository systemRepository;
	private MongoCollection<Document> systemCollection;

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
		systemRepository = new SystemMongoRepository(client);
		MongoDatabase database = client.getDatabase(DB_NAME);
		// make sure we always start with a clean database
		database.drop();
		systemCollection = database.getCollection(SYSTEM_COLLECTION_NAME);
	}

	@After
	public void tearDown() throws Exception {
		client.close();
	}

	@Test
	public void testRetrieveSystemNamesWhenDatabaseIsEmpty() {
		assertThat(systemRepository.retrieveSystemNames()).isEmpty();
	}

	private void addTestSystemToDatabase(Integer id, String name, String description, Boolean active) {
		systemCollection.insertOne(new Document().append("id", id).append("name", name)
				.append("description", description).append("active", active));
	}

	@Test
	public void testRetrieveSystemNamesWhenDatabaseIsNotEmpty() {
		addTestSystemToDatabase(1, "test1", "wow", false);
		addTestSystemToDatabase(2, "test2", "woow", true);
		assertThat(systemRepository.retrieveSystemNames()).containsExactly(new SystemEntity(1, "test1", "wow", false),
				new SystemEntity(2, "test2", "woow", true));
	}

	@Test
	public void testFindByIdNotFound() {
		assertThat(systemRepository.getSystemById(1)).isNull();
	}

	@Test
	public void testFindByIdFound() {
		addTestSystemToDatabase(1, "test1", "wow", false);
		addTestSystemToDatabase(2, "test2", "woow", true);
		assertThat(systemRepository.getSystemById(2)).isEqualTo(new SystemEntity(2, "test2", "woow", true));
	}

	@Test
	public void testSave() {
		SystemEntity system = new SystemEntity(1, "test1", "wow", false);
		systemRepository.save(system);
		assertThat(readAllSystemsFromDatabase()).containsExactly(system);
	}

	private List<SystemEntity> readAllSystemsFromDatabase() {
		return StreamSupport
				.stream(systemCollection.find().spliterator(), false).map(d -> new SystemEntity((int) d.get("id"),
						"" + d.get("name"), "" + d.get("description"), (Boolean) d.get("active")))
				.collect(Collectors.toList());
	}

	@Test
	public void testDelete() {
		addTestSystemToDatabase(1, "test1", "wow", false);
		systemRepository.delete(1);
		assertThat(readAllSystemsFromDatabase()).isEmpty();
	}

}
