package com.fabian57fabian.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.ClassRule;
import org.testcontainers.containers.GenericContainer;

import com.fabian57fabian.app.model.entities.SystemEntity;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.fabian57fabian.app.model.entities.SystemEntity;
import com.fabian57fabian.app.model.repository.SystemMongoRepository;
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

public class SystemMongoRepositoryTestcontainersIT {
	@SuppressWarnings("rawtypes")
	@ClassRule
	public static final GenericContainer mongo = new GenericContainer("mongo:4.4.3").withExposedPorts(27017);

	public static final String DB_NAME = "iotsystems";
	public static final String SYSTEM_COLLECTION_NAME = "system";

	private MongoClient client;
	private SystemMongoRepository systemRepository;
	private MongoCollection<Document> systemCollection;

	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(mongo.getContainerIpAddress(), mongo.getMappedPort(27017)));
		systemRepository = new SystemMongoRepository(client);
		MongoDatabase database = client.getDatabase(DB_NAME);
		// make sure we always start with a clean database
		database.drop();
		systemCollection = database.getCollection(SYSTEM_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void test() {
		// just to check that we can connect to the container
	}

	private void addTestSystemToDatabase(Integer id, String name, String description, Boolean active) {
		systemCollection.insertOne(new Document().append("id", id).append("name", name)
				.append("description", description).append("active", active));
	}

	@Test
	public void testFindAll() {
		addTestSystemToDatabase(1, "test1", "wow", false);
		addTestSystemToDatabase(2, "test2", "woow", true);
		assertThat(systemRepository.RetrieveSystemNames()).containsExactly(new SystemEntity(1, "test1", "wow", false),
				new SystemEntity(2, "test2", "woow", true));
	}

	@Test
	public void testFindById() {
		addTestSystemToDatabase(1, "test1", "wow", false);
		addTestSystemToDatabase(2, "test2", "woow", true);
		assertThat(systemRepository.GetSystemById(2)).isEqualTo(new SystemEntity(2, "test2", "woow", true));
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
