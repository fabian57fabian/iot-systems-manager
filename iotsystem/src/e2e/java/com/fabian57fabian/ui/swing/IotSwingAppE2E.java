package com.fabian57fabian.ui.swing;

import static org.assertj.swing.launcher.ApplicationLauncher.*;

import javax.swing.JFrame;

import static org.assertj.core.api.Assertions.*;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.testcontainers.containers.MongoDBContainer;

import com.mongodb.MongoClient;

@RunWith(GUITestRunner.class)
public class IotSwingAppE2E extends AssertJSwingJUnitTestCase {
	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");

	private static final String DB_NAME = "test-db";

	private static final String SYSTEMS_COLLECTION_NAME = "test-systems";
	private static final String SENSORS_COLLECTION_NAME = "test-sensors";

	private MongoClient mongoClient;

	private FrameFixture window;

	private static final int SYSTEM_FIXTURE_1_ID = 1;
	private static final String SYSTEM_FIXTURE_1_NAME = "first system";
	private static final String SYSTEM_FIXTURE_1_DESC= "sys ok1";

	private static final int SYSTEM_FIXTURE_2_ID = 2;
	private static final String SYSTEM_FIXTURE_2_NAME = "second system";
	private static final String SYSTEM_FIXTURE_2_DESC = "sys ok2";

	@Override
	protected void onSetUp() {
		String containerIpAddress = mongo.getContainerIpAddress();
		Integer mappedPort = mongo.getFirstMappedPort();
		mongoClient = new MongoClient(containerIpAddress, mappedPort);
		// always start with an empty database
		mongoClient.getDatabase(DB_NAME).drop();
		// add some students to the database
		addTestSystemToDatabase(SYSTEM_FIXTURE_1_ID, SYSTEM_FIXTURE_1_NAME, SYSTEM_FIXTURE_1_DESC, true);
		addTestSystemToDatabase(SYSTEM_FIXTURE_2_ID, SYSTEM_FIXTURE_2_NAME, SYSTEM_FIXTURE_2_DESC, true);
		// start the Swing application
		application("com.fabian57fabian.ui.swing.IotSwingApp")
				.withArgs("--mongo-host=" + containerIpAddress, "--mongo-port=" + mappedPort.toString(),
						"--db-name=" + DB_NAME, "--db-collection-systems-name=" + SYSTEMS_COLLECTION_NAME,
						"--db-collection-sensors-name=" + SENSORS_COLLECTION_NAME)
				.start();
		// get a reference of its JFrame
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Iot Systems".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());
	}

	@Override
	protected void onTearDown() {
		mongoClient.close();
	}

	private void addTestSystemToDatabase(Integer id, String name, String description, Boolean active) {
		mongoClient.getDatabase(DB_NAME).getCollection(SYSTEMS_COLLECTION_NAME).insertOne(new Document()
				.append("id", id).append("name", name).append("description", description).append("active", active));
	}

	private String buildSystemToString(int id, String name, String description) {
		return id + " : " + name + " ( " + description + " ) ";
	}
	
	@Test
	@GUITest
	public void testOnStartAllDatabaseElementsAreShown() {
		assertThat(window.list("listSystems").contents())
				.anySatisfy(e -> assertThat(e).contains(buildSystemToString(SYSTEM_FIXTURE_1_ID, SYSTEM_FIXTURE_1_NAME, SYSTEM_FIXTURE_1_DESC)))
				.anySatisfy(e -> assertThat(e).contains(buildSystemToString(SYSTEM_FIXTURE_2_ID, SYSTEM_FIXTURE_2_NAME, SYSTEM_FIXTURE_2_DESC)));
	}
}
