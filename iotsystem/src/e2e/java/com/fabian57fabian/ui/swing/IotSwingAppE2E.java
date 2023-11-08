package com.fabian57fabian.ui.swing;

import static org.assertj.swing.launcher.ApplicationLauncher.*;

import java.util.regex.Pattern;

import javax.swing.JFrame;

import static org.assertj.core.api.Assertions.*;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.core.matcher.JTextComponentMatcher;
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
	private static final String SYSTEM_FIXTURE_1_DESC = "sys ok1";

	private static final int SYSTEM_FIXTURE_2_ID = 2;
	private static final String SYSTEM_FIXTURE_2_NAME = "second system";
	private static final String SYSTEM_FIXTURE_2_DESC = "sys ok2";

	private static final int SENSOR_FIXTURE_1_OF_SYS_1_ID = 1;
	private static final String SENSOR_FIXTURE_1_OF_SYS_1_NAME = "first sensor";
	private static final String SENSOR_FIXTURE_1_OF_SYS_1_DESC = "mm";
	private static final Double SENSOR_FIXTURE_1_OF_SYS_1_OFFSET = 0.1;
	private static final Double SENSOR_FIXTURE_1_OF_SYS_1_MULT = 0.2;

	private static final int SENSOR_FIXTURE_2_OF_SYS_1_ID = 2;
	private static final String SENSOR_FIXTURE_2_OF_SYS_1_NAME = "second sensor";
	private static final String SENSOR_FIXTURE_2_OF_SYS_1_DESC = "m/s";
	private static final Double SENSOR_FIXTURE_2_OF_SYS_1_OFFSET = 0.3;
	private static final Double SENSOR_FIXTURE_2_OF_SYS_1_MULT = 0.4;

	private static final int SENSOR_FIXTURE_3_OF_SYS_2_ID = 3;;
	private static final String SENSOR_FIXTURE_3_OF_SYS_2_NAME = "third sensor";
	private static final String SENSOR_FIXTURE_3_OF_SYS_2_DESC = "km/h";
	private static final Double SENSOR_FIXTURE_3_OF_SYS_2_OFFSET = 0.5;
	private static final Double SENSOR_FIXTURE_3_OF_SYS_2_MULT = 0.6;

	@Override
	protected void onSetUp() {
		String containerIpAddress = mongo.getContainerIpAddress();
		Integer mappedPort = mongo.getFirstMappedPort();
		mongoClient = new MongoClient(containerIpAddress, mappedPort);
		// always start with an empty database
		mongoClient.getDatabase(DB_NAME).drop();
		// add some students to the database
		addTestSystemToDatabase(SYSTEM_FIXTURE_1_ID, SYSTEM_FIXTURE_1_NAME, SYSTEM_FIXTURE_1_DESC, true);
		addTestSensorToDatabase(SENSOR_FIXTURE_1_OF_SYS_1_ID, SENSOR_FIXTURE_1_OF_SYS_1_NAME,
				SENSOR_FIXTURE_1_OF_SYS_1_DESC, SENSOR_FIXTURE_1_OF_SYS_1_OFFSET, SENSOR_FIXTURE_1_OF_SYS_1_MULT,
				SYSTEM_FIXTURE_1_ID);
		addTestSensorToDatabase(SENSOR_FIXTURE_2_OF_SYS_1_ID, SENSOR_FIXTURE_2_OF_SYS_1_NAME,
				SENSOR_FIXTURE_2_OF_SYS_1_DESC, SENSOR_FIXTURE_2_OF_SYS_1_OFFSET, SENSOR_FIXTURE_2_OF_SYS_1_MULT,
				SYSTEM_FIXTURE_1_ID);
		addTestSystemToDatabase(SYSTEM_FIXTURE_2_ID, SYSTEM_FIXTURE_2_NAME, SYSTEM_FIXTURE_2_DESC, true);
		addTestSensorToDatabase(SENSOR_FIXTURE_3_OF_SYS_2_ID, SENSOR_FIXTURE_3_OF_SYS_2_NAME,
				SENSOR_FIXTURE_3_OF_SYS_2_DESC, SENSOR_FIXTURE_3_OF_SYS_2_OFFSET, SENSOR_FIXTURE_3_OF_SYS_2_MULT,
				SYSTEM_FIXTURE_2_ID);
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

	private void addTestSensorToDatabase(Integer id, String name, String description, double offset, double multiplier,
			int systemId) {
		mongoClient.getDatabase(DB_NAME).getCollection(SENSORS_COLLECTION_NAME)
				.insertOne(new Document().append("id", id).append("name", name).append("description", description)
						.append("offset", offset).append("multiplier", multiplier).append("systemId", systemId));
	}

	private String buildSystemToString(int id, String name, String description) {
		return id + " : " + name + " ( " + description + " ) ";
	}

	@Test
	@GUITest
	public void testOnStartAllDatabaseElementsAreShown() {
		assertThat(window.list("listSystems").contents())
				.anySatisfy(e -> assertThat(e).contains(
						buildSystemToString(SYSTEM_FIXTURE_1_ID, SYSTEM_FIXTURE_1_NAME, SYSTEM_FIXTURE_1_DESC)))
				.anySatisfy(e -> assertThat(e).contains(
						buildSystemToString(SYSTEM_FIXTURE_2_ID, SYSTEM_FIXTURE_2_NAME, SYSTEM_FIXTURE_2_DESC)));
	}

	@Test
	@GUITest
	public void testAddSystemSuccess() {
		String id = "11";
		String name = "n";
		String desc = "d";
		window.textBox(JTextComponentMatcher.withName("txtSystemId")).enterText(id);
		window.textBox(JTextComponentMatcher.withName("txtSystemName")).enterText(name);
		window.textBox(JTextComponentMatcher.withName("txtSystemDescription")).enterText(desc);
		window.button(JButtonMatcher.withName("btnAddSystem")).click();
		assertThat(window.list("listSystems").contents()).anySatisfy(e -> assertThat(e).contains(id, name, desc));
	}

	@Test
	@GUITest
	public void testAddSystemFailOnIdNotInteger() {
		String id = "aaa";
		String name = "n";
		String desc = "d";
		window.textBox(JTextComponentMatcher.withName("txtSystemId")).enterText(id);
		window.textBox(JTextComponentMatcher.withName("txtSystemName")).enterText(name);
		window.textBox(JTextComponentMatcher.withName("txtSystemDescription")).enterText(desc);
		window.button(JButtonMatcher.withName("btnAddSystem")).click();
		assertThat(window.label(JLabelMatcher.withName("lblSystemErrorMessageLabel")).text()).contains("Id", "int",
				"!");
	}

	@Test
	@GUITest
	public void testAddSensorSuccess() {
		String id = "12";
		String name = "n";
		String desc = "d";
		String unit = "m";
		String offset = "0.1";
		String multiplier = "0.2";
		window.list("listSystems").selectItem(0);
		window.textBox(JTextComponentMatcher.withName("txtSensorId")).enterText(id);
		window.textBox(JTextComponentMatcher.withName("txtSensorName")).enterText(name);
		window.textBox(JTextComponentMatcher.withName("txtSensorDescription")).enterText(desc);
		window.textBox(JTextComponentMatcher.withName("txtSensorUnit")).enterText(unit);
		window.textBox(JTextComponentMatcher.withName("txtSensorOffset")).enterText(offset);
		window.textBox(JTextComponentMatcher.withName("txtSensorMultiplier")).enterText(multiplier);
		window.button(JButtonMatcher.withName("btnAddSensor")).click();
		assertThat(window.list("listSensors").contents()).anySatisfy(e -> assertThat(e).contains(id, name, desc));
	}

	@Test
	@GUITest
	public void testAddSensorFailWhenMultiplierIsNotDouble() {
		String id = "12";
		String name = "n";
		String desc = "d";
		String unit = "m";
		String offset = "0.1";
		String multiplier = "aaa";
		window.list("listSystems").selectItem(0);
		window.textBox(JTextComponentMatcher.withName("txtSensorId")).enterText(id);
		window.textBox(JTextComponentMatcher.withName("txtSensorName")).enterText(name);
		window.textBox(JTextComponentMatcher.withName("txtSensorDescription")).enterText(desc);
		window.textBox(JTextComponentMatcher.withName("txtSensorUnit")).enterText(unit);
		window.textBox(JTextComponentMatcher.withName("txtSensorOffset")).enterText(offset);
		window.textBox(JTextComponentMatcher.withName("txtSensorMultiplier")).enterText(multiplier);
		window.button(JButtonMatcher.withName("btnAddSensor")).click();
		assertThat(window.label(JLabelMatcher.withName("lblSensorErrorMessageLabel")).text()).contains("Multiplier",
				"float", "!");
	}

	@Test
	@GUITest
	public void testAddSensorFailWhenOffsetIsNotDouble() {
		String id = "12";
		String name = "n";
		String desc = "d";
		String unit = "m";
		String offset = "bbb";
		String multiplier = "0.2";
		window.list("listSystems").selectItem(0);
		window.textBox(JTextComponentMatcher.withName("txtSensorId")).enterText(id);
		window.textBox(JTextComponentMatcher.withName("txtSensorName")).enterText(name);
		window.textBox(JTextComponentMatcher.withName("txtSensorDescription")).enterText(desc);
		window.textBox(JTextComponentMatcher.withName("txtSensorUnit")).enterText(unit);
		window.textBox(JTextComponentMatcher.withName("txtSensorOffset")).enterText(offset);
		window.textBox(JTextComponentMatcher.withName("txtSensorMultiplier")).enterText(multiplier);
		window.button(JButtonMatcher.withName("btnAddSensor")).click();
		assertThat(window.label(JLabelMatcher.withName("lblSensorErrorMessageLabel")).text()).contains("Offset",
				"float", "!");
	}

	@Test
	@GUITest
	public void testDeleteSystemSuccess() {
		window.list("listSystems").selectItem(Pattern.compile(".*" + SYSTEM_FIXTURE_1_NAME + ".*"));
		window.button(JButtonMatcher.withName("btnDeleteSystem")).click();
		assertThat(window.list("listSystems").contents()).noneMatch(e -> e.contains(SYSTEM_FIXTURE_1_NAME));
	}

	@Test
	@GUITest
	public void testDeleteSensorSuccess() {
		window.list("listSystems").selectItem(0);

		window.list("listSensors").selectItem(Pattern.compile(".*" + SENSOR_FIXTURE_2_OF_SYS_1_NAME + ".*"));
		window.button(JButtonMatcher.withName("btnDeleteSensor")).click();
		assertThat(window.list("listSensors").contents()).noneMatch(e -> e.contains(SENSOR_FIXTURE_2_OF_SYS_1_NAME));
	}
}
