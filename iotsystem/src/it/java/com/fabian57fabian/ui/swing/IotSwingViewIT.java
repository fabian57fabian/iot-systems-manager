package com.fabian57fabian.ui.swing;

import static org.assertj.core.api.Assertions.*;

import java.net.InetSocketAddress;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.core.matcher.JTextComponentMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fabian57fabian.app.controller.SystemsManagerController;
import com.fabian57fabian.app.model.entities.SensorEntity;
import com.fabian57fabian.app.model.entities.SystemEntity;
import com.fabian57fabian.app.model.repository.SensorMongoRepository;
import com.fabian57fabian.app.model.repository.SystemMongoRepository;
import com.fabian57fabian.app.model.service.SensorService;
import com.fabian57fabian.app.model.service.SystemService;
import com.fabian57fabian.ui.view.IotSwingView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

@RunWith(GUITestRunner.class)
public class IotSwingViewIT extends AssertJSwingJUnitTestCase {

	private MongoClient mongo;
	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private FrameFixture window;

	private IotSwingView iotView;
	private SensorService sensorService;
	private SystemService systemService;
	private SystemsManagerController controller;

	public static final String DB_NAME = "test-iot-systems";
	public static final String SYSTEM_COLLECTION_NAME = "system";
	public static final String SENSOR_COLLECTION_NAME = "sensor";

	@BeforeClass
	public static void setupServer() {
		server = new MongoServer(new MemoryBackend());
		// bind on a random local port
		serverAddress = server.bind();
	}

	@AfterClass
	public static void shutdownServer() {
		server.shutdown();
	}

	@Override
	protected void onSetUp() {
		mongo = new MongoClient(new ServerAddress(serverAddress));
		GuiActionRunner.execute(() -> {
			SystemMongoRepository systemRepository = new SystemMongoRepository(mongo, DB_NAME, SYSTEM_COLLECTION_NAME);
			systemService = new SystemService(systemRepository);
			for (SystemEntity sys : systemService.getSystemNames()) {
				systemService.delete(sys.getId());
			}
			SensorMongoRepository sensorRepository = new SensorMongoRepository(mongo, DB_NAME, SENSOR_COLLECTION_NAME);
			sensorService = new SensorService(sensorRepository);
			for (SensorEntity sen : sensorService.getSensorNames()) {
				systemService.delete(sen.getId());
			}
			iotView = new IotSwingView();
			controller = new SystemsManagerController(systemService, sensorService, iotView);
			iotView.setController(controller);
			return iotView;
		});
		window = new FrameFixture(robot(), iotView);
		window.show(); // shows the frame to test
	}

	@Override
	protected void onTearDown() {
		mongo.close();
	}

	@Test
	public void testViewAllSystems() {
		SystemEntity system = new SystemEntity(0, "s1", "d1", false);
		systemService.create(system);
		GuiActionRunner.execute(() -> controller.viewAllSystems());
		assertThat(window.list("listSystems").contents()).containsExactly(system.toString());
	}

	@Test
	public void testExpandOneSystemSuccess() {
		SystemEntity system = new SystemEntity(10, "s1", "d1", false);
		systemService.create(system);
		SensorEntity sensor = new SensorEntity(0, "foo", "description of foo", "mm", 0.1, 0.2, system.getId());
		sensorService.create(sensor);
		GuiActionRunner.execute(() -> controller.expandOneSystem(sensor.getSystemId()));
		assertThat(window.list("listSensors").contents()).containsExactly(sensor.toString());
	}

	@Test
	public void testExpandOneSystemFailedNoSystem() {
		SensorEntity sensor = new SensorEntity(0, "foo", "description of foo", "mm", 0.1, 0.2, 10);
		sensorService.create(sensor);
		GuiActionRunner.execute(() -> controller.expandOneSystem(sensor.getSystemId()));
		assertThat(window.label(JLabelMatcher.withName("lblSystemErrorMessageLabel")).text()).contains("System",
				"found");
	}

	@Test
	public void testDeleteSensor() {
		SensorEntity sensor = new SensorEntity(0, "foo", "description of foo", "mm", 0.1, 0.2, 10);
		sensorService.create(sensor);
		GuiActionRunner.execute(() -> iotView.onSensorAdded(sensor));
		window.list("listSensors").selectItem(0);
		window.button(JButtonMatcher.withName("btnDeleteSensor")).click();
		assertThat(window.list("listSensors").contents()).isEmpty();
	}

	@Test
	public void testDeleteSystem() {
		SystemEntity system = new SystemEntity(10, "s1", "d1", false);
		systemService.create(system);
		GuiActionRunner.execute(() -> iotView.onSystemAdded(system));
		window.list("listSystems").selectItem(0);
		window.button(JButtonMatcher.withName("btnDeleteSystem")).click();
		assertThat(window.list("listSystems").contents()).isEmpty();
	}

	@Test
	public void testAddSystemSuccess() {
		window.textBox(JTextComponentMatcher.withName("txtSystemId")).enterText("10");
		window.textBox(JTextComponentMatcher.withName("txtSystemName")).enterText("s1");
		window.textBox(JTextComponentMatcher.withName("txtSystemDescription")).enterText("d1");
		window.button(JButtonMatcher.withName("btnAddSystem")).click();
		assertThat(window.list("listSystems").contents())
				.containsExactly(new SystemEntity(10, "s1", "d1", false).toString());
	}

	@Test
	public void testAddSystemFailedBecauseAlreadyExists() {
		SystemEntity system = new SystemEntity(10, "s1", "d1", false);
		systemService.create(system);
		window.textBox(JTextComponentMatcher.withName("txtSystemId")).enterText("10");
		window.textBox(JTextComponentMatcher.withName("txtSystemName")).enterText("s1");
		window.textBox(JTextComponentMatcher.withName("txtSystemDescription")).enterText("d1");
		window.button(JButtonMatcher.withName("btnAddSystem")).click();
		assertThat(window.label(JLabelMatcher.withName("lblSystemErrorMessageLabel")).text()).contains("System",
				"already", "exists");
	}

	@Test
	public void testAddSystemFailedBecauseIdNotInt() {
		window.textBox(JTextComponentMatcher.withName("txtSystemId")).enterText("aaa");
		window.textBox(JTextComponentMatcher.withName("txtSystemName")).enterText("s1");
		window.textBox(JTextComponentMatcher.withName("txtSystemDescription")).enterText("d1");
		window.button(JButtonMatcher.withName("btnAddSystem")).click();
		assertThat(window.label(JLabelMatcher.withName("lblSystemErrorMessageLabel")).text()).contains("Id", "int");
	}

	@Test
	public void testAddSensorSuccess() {
		SystemEntity system = new SystemEntity(10, "s1", "d1", false);
		systemService.create(system);
		GuiActionRunner.execute(() -> iotView.onSystemAdded(system));
		window.list("listSystems").selectItem(0);
		window.textBox(JTextComponentMatcher.withName("txtSensorId")).enterText("10");
		window.textBox(JTextComponentMatcher.withName("txtSensorName")).enterText("n");
		window.textBox(JTextComponentMatcher.withName("txtSensorDescription")).enterText("d");
		window.textBox(JTextComponentMatcher.withName("txtSensorUnit")).enterText("mm");
		window.textBox(JTextComponentMatcher.withName("txtSensorOffset")).enterText("0.1");
		window.textBox(JTextComponentMatcher.withName("txtSensorMultiplier")).enterText("0.2");
		window.button(JButtonMatcher.withName("btnAddSensor")).click();
		assertThat(window.list("listSensors").contents())
				.containsExactly(new SensorEntity(10, "n", "d", "mm", 0.1, 0.2, 10).toString());
	}

	@Test
	public void testAddSensorFailedIdNotInt() {
		SystemEntity system = new SystemEntity(10, "s1", "d1", false);
		systemService.create(system);
		GuiActionRunner.execute(() -> iotView.onSystemAdded(system));
		window.list("listSystems").selectItem(0);
		window.textBox(JTextComponentMatcher.withName("txtSensorId")).enterText("aaaa");
		window.textBox(JTextComponentMatcher.withName("txtSensorName")).enterText("n");
		window.textBox(JTextComponentMatcher.withName("txtSensorDescription")).enterText("d");
		window.textBox(JTextComponentMatcher.withName("txtSensorUnit")).enterText("mm");
		window.textBox(JTextComponentMatcher.withName("txtSensorOffset")).enterText("0.1");
		window.textBox(JTextComponentMatcher.withName("txtSensorMultiplier")).enterText("0.2");
		window.button(JButtonMatcher.withName("btnAddSensor")).click();
		assertThat(window.label(JLabelMatcher.withName("lblSensorErrorMessageLabel")).text()).contains("Id", "int");
	}

	@Test
	public void testAddSensorFailedOffsetNotInt() {
		SystemEntity system = new SystemEntity(10, "s1", "d1", false);
		systemService.create(system);
		GuiActionRunner.execute(() -> iotView.onSystemAdded(system));
		window.list("listSystems").selectItem(0);
		window.textBox(JTextComponentMatcher.withName("txtSensorId")).enterText("10");
		window.textBox(JTextComponentMatcher.withName("txtSensorName")).enterText("n");
		window.textBox(JTextComponentMatcher.withName("txtSensorDescription")).enterText("d");
		window.textBox(JTextComponentMatcher.withName("txtSensorUnit")).enterText("mm");
		window.textBox(JTextComponentMatcher.withName("txtSensorOffset")).enterText("ddddd");
		window.textBox(JTextComponentMatcher.withName("txtSensorMultiplier")).enterText("0.2");
		window.button(JButtonMatcher.withName("btnAddSensor")).click();
		assertThat(window.label(JLabelMatcher.withName("lblSensorErrorMessageLabel")).text()).contains("Offset",
				"float");
	}

	@Test
	public void testAddSensorFailedMultiplierNotInt() {
		SystemEntity system = new SystemEntity(10, "s1", "d1", false);
		systemService.create(system);
		GuiActionRunner.execute(() -> iotView.onSystemAdded(system));
		window.list("listSystems").selectItem(0);
		window.textBox(JTextComponentMatcher.withName("txtSensorId")).enterText("10");
		window.textBox(JTextComponentMatcher.withName("txtSensorName")).enterText("n");
		window.textBox(JTextComponentMatcher.withName("txtSensorDescription")).enterText("d");
		window.textBox(JTextComponentMatcher.withName("txtSensorUnit")).enterText("mm");
		window.textBox(JTextComponentMatcher.withName("txtSensorOffset")).enterText("0.1");
		window.textBox(JTextComponentMatcher.withName("txtSensorMultiplier")).enterText("aaa");
		window.button(JButtonMatcher.withName("btnAddSensor")).click();
		assertThat(window.label(JLabelMatcher.withName("lblSensorErrorMessageLabel")).text()).contains("Multiplier",
				"float");
	}

	@Test
	public void testAddSensorFailedSystemNotSelected() {
		window.textBox(JTextComponentMatcher.withName("txtSensorId")).enterText("10");
		window.textBox(JTextComponentMatcher.withName("txtSensorName")).enterText("n");
		window.textBox(JTextComponentMatcher.withName("txtSensorDescription")).enterText("d");
		window.textBox(JTextComponentMatcher.withName("txtSensorUnit")).enterText("mm");
		window.textBox(JTextComponentMatcher.withName("txtSensorOffset")).enterText("0.1");
		window.textBox(JTextComponentMatcher.withName("txtSensorMultiplier")).enterText("0.2");
		window.button(JButtonMatcher.withName("btnAddSensor")).click();
		assertThat(window.label(JLabelMatcher.withName("lblSensorErrorMessageLabel")).text()).contains("system",
				"selected");
	}
	
	@Test
	public void testCalibrateSensorSuccess() {
		SystemEntity system = new SystemEntity(10, "s1", "d1", false);
		systemService.create(system);
		GuiActionRunner.execute(() -> iotView.onSystemAdded(system));
		window.list("listSystems").selectItem(0);
		SensorEntity sensor = new SensorEntity(0, "foo", "description of foo", "mm", 0.1, 0.2, system.getId());
		sensorService.create(sensor);
		GuiActionRunner.execute(() -> iotView.onSensorAdded(sensor));
		window.list("listSensors").selectItem(0);
		window.textBox(JTextComponentMatcher.withName("txtSensorNewOffset")).enterText("1");
		window.textBox(JTextComponentMatcher.withName("txtSensorNewMultiplier")).enterText("2");
		window.button(JButtonMatcher.withName("btnCalibrateSensor")).click();
		assertThat(window.list("listSensors").contents()).containsExactly(
				new SensorEntity(0, "foo", "description of foo", "mm", 0.11, 0.22, system.getId()).toString());
	}

	@Test
	public void testCalibrateSensorFailOffsetNotFloat() {
		SystemEntity system = new SystemEntity(10, "s1", "d1", false);
		systemService.create(system);
		GuiActionRunner.execute(() -> iotView.onSystemAdded(system));
		window.list("listSystems").selectItem(0);
		SensorEntity sensor = new SensorEntity(0, "foo", "description of foo", "mm", 0.1, 0.2, system.getId());
		sensorService.create(sensor);
		GuiActionRunner.execute(() -> iotView.onSensorAdded(sensor));
		window.list("listSensors").selectItem(0);
		window.textBox(JTextComponentMatcher.withName("txtSensorNewOffset")).enterText("aaa");
		window.textBox(JTextComponentMatcher.withName("txtSensorNewMultiplier")).enterText("2");
		window.button(JButtonMatcher.withName("btnCalibrateSensor")).click();
		assertThat(window.label(JLabelMatcher.withName("lblSensorErrorMessageLabel")).text()).contains("Offset",
				"float");
	}
	
	@Test
	public void testCalibrateSensorFailMultiplierNotFloat() {
		SystemEntity system = new SystemEntity(10, "s1", "d1", false);
		systemService.create(system);
		GuiActionRunner.execute(() -> iotView.onSystemAdded(system));
		window.list("listSystems").selectItem(0);
		SensorEntity sensor = new SensorEntity(0, "foo", "description of foo", "mm", 0.1, 0.2, system.getId());
		sensorService.create(sensor);
		GuiActionRunner.execute(() -> iotView.onSensorAdded(sensor));
		window.list("listSensors").selectItem(0);
		window.textBox(JTextComponentMatcher.withName("txtSensorNewOffset")).enterText("1");
		window.textBox(JTextComponentMatcher.withName("txtSensorNewMultiplier")).enterText("aaaa");
		window.button(JButtonMatcher.withName("btnCalibrateSensor")).click();
		assertThat(window.label(JLabelMatcher.withName("lblSensorErrorMessageLabel")).text()).contains("Multiplier",
				"float");
	}
}
