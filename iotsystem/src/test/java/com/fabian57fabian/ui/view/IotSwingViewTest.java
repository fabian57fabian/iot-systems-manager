package com.fabian57fabian.ui.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import javax.swing.DefaultListModel;

import com.fabian57fabian.app.controller.SystemsManagerController;
import com.fabian57fabian.app.model.entities.SensorEntity;
import com.fabian57fabian.app.model.entities.SystemEntity;

import org.junit.Test;

import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.core.matcher.JTextComponentMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;

@RunWith(GUITestRunner.class)
public class IotSwingViewTest extends AssertJSwingJUnitTestCase {

	private AutoCloseable closeable;

	private FrameFixture window;

	private IotSwingView iotSwingView;

	@Mock
	private SystemsManagerController controller;

	@Override
	protected void onSetUp() {
		closeable = MockitoAnnotations.openMocks(this);
		GuiActionRunner.execute(() -> {
			iotSwingView = new IotSwingView();
			iotSwingView.setController(controller);
			return iotSwingView;
		});
		window = new FrameFixture(robot(), iotSwingView);
		window.show(); // shows the frame to test
	}

	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

	@Test
	@GUITest
	public void testControlsInitialStates() {
		window.label(JLabelMatcher.withName("lblSystemId")).requireText("id");
		window.label(JLabelMatcher.withName("lblSystemName")).requireText("name");
		window.label(JLabelMatcher.withName("lblSystemDescription")).requireText("desc");
		window.textBox(JTextComponentMatcher.withName("txtSystemName")).requireEnabled();
		window.textBox(JTextComponentMatcher.withName("txtSystemId")).requireEnabled();
		window.textBox(JTextComponentMatcher.withName("txtSystemDescription")).requireEnabled();
		window.button(JButtonMatcher.withName("btnAddSystem")).requireDisabled();
		window.button(JButtonMatcher.withName("btnDeleteSystem")).requireDisabled();
		window.label(JLabelMatcher.withName("lblSystemErrorMessageLabel")).requireText(" ");
		window.label(JLabelMatcher.withName("lblCurrentSystemDescription")).requireText(" ");
		window.list("listSystems");

		window.label(JLabelMatcher.withName("lblSensorId")).requireText("id");
		window.label(JLabelMatcher.withName("lblSensorName")).requireText("name");
		window.label(JLabelMatcher.withName("lblSensorDescription")).requireText("description");
		window.label(JLabelMatcher.withName("lblSensorUnit")).requireText("unit");
		window.label(JLabelMatcher.withName("lblSensorOffset")).requireText("offset");
		window.label(JLabelMatcher.withName("lblSensorMultiplier")).requireText("multiplier");
		window.textBox(JTextComponentMatcher.withName("txtSensorId")).requireEnabled();
		window.textBox(JTextComponentMatcher.withName("txtSensorName")).requireEnabled();
		window.textBox(JTextComponentMatcher.withName("txtSensorDescription")).requireEnabled();
		window.textBox(JTextComponentMatcher.withName("txtSensorUnit")).requireEnabled();
		window.textBox(JTextComponentMatcher.withName("txtSensorOffset")).requireEnabled();
		window.textBox(JTextComponentMatcher.withName("txtSensorMultiplier")).requireEnabled();
		window.button(JButtonMatcher.withName("btnAddSensor")).requireDisabled();
		window.button(JButtonMatcher.withName("btnDeleteSensor")).requireDisabled();
		window.label(JLabelMatcher.withName("lblSensorErrorMessageLabel")).requireText(" ");
		window.list("listSensors");
	}

	@Test
	public void testSystemDeleteButtonShouldBeEnabledOnlyWhenASystemIsSelected() {
		GuiActionRunner.execute(() -> iotSwingView.getListSystemsModel()
				.addElement(new SystemEntity(0, "bar", "Description of 'bar' ", false)));
		window.list("listSystems").selectItem(0);
		JButtonFixture deleteButton = window.button(JButtonMatcher.withName("btnDeleteSystem"));
		deleteButton.requireEnabled();
		window.list("listSystems").clearSelection();
		deleteButton.requireDisabled();
	}

	@Test
	public void testSensorDeleteButtonShouldBeEnabledOnlyWhenASensorIsSelected() {
		GuiActionRunner.execute(() -> iotSwingView.getListSensorsModel()
				.addElement(new SensorEntity(0, "bar", "Description of 'bar' ", 0.1, 0.2, 1)));
		window.list("listSensors").selectItem(0);
		JButtonFixture deleteButton = window.button(JButtonMatcher.withName("btnDeleteSensor"));
		deleteButton.requireEnabled();
		window.list("listSensors").clearSelection();
		deleteButton.requireDisabled();
	}

	@Test
	public void testsShowAllSystemsShouldAddSystemsDescriptionsToTheListAndLabel() {
		SystemEntity system1 = new SystemEntity(0, "bar", "Description of 'bar' ", false);
		SystemEntity system2 = new SystemEntity(1, "foo", "Description of 'foo' ", true);
		GuiActionRunner.execute(() -> iotSwingView.showSystems(Arrays.asList(system1, system2)));
		String[] listContents = window.list("listSystems").contents();
		assertThat(listContents).containsExactly(system1.toString(), system2.toString());
	}

	@Test
	public void testsShowSensorsOfSystemShouldAddSensorsToTheList() {
		SensorEntity sensor1 = new SensorEntity(0, "bar", "Description of 'bar' ", 0.1, 0.2, 10);
		SensorEntity sensor2 = new SensorEntity(1, "foo", "Description of 'foo' ", 0.3, 0.4, 10);
		GuiActionRunner.execute(() -> iotSwingView.ShowSensorsOfSystem(Arrays.asList(sensor1, sensor2)));
		String[] listContents = window.list("listSensors").contents();
		assertThat(listContents).containsExactly(sensor1.toString(), sensor2.toString());
	}

	@Test
	public void testsShowOneSystemShouldShowDescriptionToLabel() {
		SystemEntity system1 = new SystemEntity(0, "bar", "Description of bar ", false);
		GuiActionRunner.execute(() -> iotSwingView.showOneSystem(system1));
		window.label(JLabelMatcher.withName("lblCurrentSystemDescription")).requireText(system1.getDescription());
	}

	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
		SystemEntity system1 = new SystemEntity(0, "bar", "Description of 'bar' ", false);
		GuiActionRunner.execute(() -> iotSwingView.showOneSystemError("error message", system1));
		window.label(JLabelMatcher.withName("lblSystemErrorMessageLabel")).requireText("error message: " + system1);
	}

	@Test
	public void testSystemAddedShouldAddTheStudentToTheListAndResetTheErrorLabel() {
		SystemEntity system1 = new SystemEntity(0, "bar", "Description of bar ", false);
		GuiActionRunner
				.execute(() -> iotSwingView.onSystemAdded(new SystemEntity(0, "bar", "Description of bar ", false)));
		String[] listContents = window.list("listSystems").contents();
		assertThat(listContents).containsExactly(system1.toString());
		window.label("lblSystemErrorMessageLabel").requireText(" ");
	}

	@Test
	public void testStudentRemovedShouldRemoveTheStudentFromTheListAndResetTheErrorLabel() {
		// setup
		SystemEntity system1 = new SystemEntity(0, "bar", "Description of 'bar' ", false);
		SystemEntity system2 = new SystemEntity(1, "foo", "Description of 'foo' ", true);
		GuiActionRunner.execute(() -> {
			DefaultListModel<SystemEntity> listSystemsModel = iotSwingView.getListSystemsModel();
			listSystemsModel.addElement(system1);
			listSystemsModel.addElement(system2);
		});
		// execute
		GuiActionRunner
				.execute(() -> iotSwingView.onSystemRemoved(new SystemEntity(1, "foo", "Description of 'foo' ", true)));
		// verify
		String[] listContents = window.list("listSystems").contents();
		assertThat(listContents).containsExactly(system1.toString());
		window.label("lblSystemErrorMessageLabel").requireText(" ");
	}

	@Test
	public void testSystemWhenEitherIdOrNameOrDescriptionAreBlankThenAddButtonShouldBeDisabled() {
		JTextComponentFixture idTextBox = window.textBox(JTextComponentMatcher.withName("txtSystemId"));
		JTextComponentFixture nameTextBox = window.textBox(JTextComponentMatcher.withName("txtSystemName"));
		JTextComponentFixture descriptionTextBox = window
				.textBox(JTextComponentMatcher.withName("txtSystemDescription"));

		idTextBox.enterText("1");
		nameTextBox.enterText("");
		descriptionTextBox.enterText("s");
		window.button(JButtonMatcher.withName("btnAddSystem")).requireDisabled();

		// reset
		idTextBox.setText("");
		nameTextBox.setText("");
		descriptionTextBox.setText("");

		idTextBox.enterText("");
		nameTextBox.enterText("");
		descriptionTextBox.enterText("");
		window.button(JButtonMatcher.withName("btnAddSystem")).requireDisabled();

		// reset
		idTextBox.setText("");
		nameTextBox.setText("");
		descriptionTextBox.setText("");

		idTextBox.enterText(" ");
		nameTextBox.enterText(" ");
		descriptionTextBox.enterText("s2");
		window.button(JButtonMatcher.withName("btnAddSystem")).requireDisabled();

		// reset
		idTextBox.setText("");
		nameTextBox.setText("");
		descriptionTextBox.setText("");

		idTextBox.enterText(" ");
		nameTextBox.enterText(" ");
		descriptionTextBox.enterText(" ");
		window.button(JButtonMatcher.withName("btnAddSystem")).requireDisabled();
	}

	@Test
	public void testSystemWhenAllAreFilledThenAddButtonShouldBeEnabled() {
		JTextComponentFixture idTextBox = window.textBox(JTextComponentMatcher.withName("txtSystemId"));
		JTextComponentFixture nameTextBox = window.textBox(JTextComponentMatcher.withName("txtSystemName"));
		JTextComponentFixture descriptionTextBox = window
				.textBox(JTextComponentMatcher.withName("txtSystemDescription"));

		idTextBox.enterText("10");
		nameTextBox.enterText("n");
		descriptionTextBox.enterText("n");
		window.button(JButtonMatcher.withName("btnAddSystem")).requireEnabled();
	}

	@Test
	public void testSensorWhenEitherAllAreBlankThenAddButtonShouldBeDisabled() {
		JTextComponentFixture idTextBox = window.textBox(JTextComponentMatcher.withName("txtSensorId"));
		JTextComponentFixture nameTextBox = window.textBox(JTextComponentMatcher.withName("txtSensorName"));
		JTextComponentFixture descriptionTextBox = window
				.textBox(JTextComponentMatcher.withName("txtSensorDescription"));
		JTextComponentFixture unitTextBox = window.textBox(JTextComponentMatcher.withName("txtSensorUnit"));
		JTextComponentFixture offsetTextBox = window.textBox(JTextComponentMatcher.withName("txtSensorOffset"));
		JTextComponentFixture multiplierTextBox = window.textBox(JTextComponentMatcher.withName("txtSensorMultiplier"));

		// One empty
		idTextBox.enterText("1");
		nameTextBox.enterText("");
		descriptionTextBox.enterText("s");
		unitTextBox.enterText("s");
		offsetTextBox.enterText("s");
		multiplierTextBox.enterText("s");
		window.button(JButtonMatcher.withName("btnAddSensor")).requireDisabled();

		// reset
		idTextBox.setText("");
		nameTextBox.setText("");
		descriptionTextBox.setText("");
		unitTextBox.setText("");
		offsetTextBox.setText("");
		multiplierTextBox.setText("");

		// all empty
		idTextBox.enterText("");
		nameTextBox.enterText("");
		descriptionTextBox.enterText("");
		unitTextBox.enterText("");
		offsetTextBox.enterText("");
		multiplierTextBox.enterText("");
		window.button(JButtonMatcher.withName("btnAddSensor")).requireDisabled();

		// reset
		idTextBox.setText("");
		nameTextBox.setText("");
		descriptionTextBox.setText("");
		unitTextBox.setText("");
		offsetTextBox.setText("");
		multiplierTextBox.setText("");

		idTextBox.enterText(" ");
		nameTextBox.enterText(" ");
		descriptionTextBox.enterText("s2");
		unitTextBox.enterText(" ");
		offsetTextBox.enterText(" s3");
		multiplierTextBox.enterText(" ");
		window.button(JButtonMatcher.withName("btnAddSensor")).requireDisabled();

		// reset
		idTextBox.setText("");
		nameTextBox.setText("");
		descriptionTextBox.setText("");
		unitTextBox.setText("");
		offsetTextBox.setText("");
		multiplierTextBox.setText("");

		// all spaces
		idTextBox.enterText(" ");
		nameTextBox.enterText(" ");
		descriptionTextBox.enterText(" ");
		unitTextBox.enterText(" ");
		offsetTextBox.enterText(" ");
		multiplierTextBox.enterText(" ");
		window.button(JButtonMatcher.withName("btnAddSensor")).requireDisabled();
	}

	@Test
	public void testSensorWhenAllAreFilledThenAddButtonShouldBeEnabled() {
		JTextComponentFixture idTextBox = window.textBox(JTextComponentMatcher.withName("txtSensorId"));
		JTextComponentFixture nameTextBox = window.textBox(JTextComponentMatcher.withName("txtSensorName"));
		JTextComponentFixture descriptionTextBox = window
				.textBox(JTextComponentMatcher.withName("txtSensorDescription"));
		JTextComponentFixture unitTextBox = window.textBox(JTextComponentMatcher.withName("txtSensorUnit"));
		JTextComponentFixture offsetTextBox = window.textBox(JTextComponentMatcher.withName("txtSensorOffset"));
		JTextComponentFixture multiplierTextBox = window.textBox(JTextComponentMatcher.withName("txtSensorMultiplier"));

		// One empty
		idTextBox.enterText("1");
		nameTextBox.enterText("n");
		descriptionTextBox.enterText("d");
		unitTextBox.enterText("u");
		offsetTextBox.enterText("0.1");
		multiplierTextBox.enterText("0.2");
		window.button(JButtonMatcher.withName("btnAddSensor")).requireEnabled();
	}

	@Test
	public void testAddSystemButtonShouldDelegateToSystemsControllerNewSystem() {
		window.textBox(JTextComponentMatcher.withName("txtSystemId")).enterText("10");
		window.textBox(JTextComponentMatcher.withName("txtSystemName")).enterText("n");
		window.textBox(JTextComponentMatcher.withName("txtSystemDescription")).enterText("d");
		window.button(JButtonMatcher.withName("btnAddSystem")).click();
		verify(controller).addSystem(new SystemEntity(10, "n", "d", false));
	}

	@Test
	public void testAddSystemButtonOnNotIntIdShouldShowAnError() {
		window.textBox(JTextComponentMatcher.withName("txtSystemId")).enterText("aaa");
		window.textBox(JTextComponentMatcher.withName("txtSystemName")).enterText("n");
		window.textBox(JTextComponentMatcher.withName("txtSystemDescription")).enterText("d");
		window.button(JButtonMatcher.withName("btnAddSystem")).click();
		window.label(JLabelMatcher.withName("lblSystemErrorMessageLabel")).requireText("Id not int!");
	}

	@Test
	public void testDeleteButtonShouldDelegateToSchoolControllerDeleteStudent() {
		SystemEntity system1 = new SystemEntity(0, "bar", "Description of 'bar' ", false);
		SystemEntity system2 = new SystemEntity(1, "foo", "Description of 'foo' ", true);
		GuiActionRunner.execute(() -> {
			DefaultListModel<SystemEntity> listSystemsModel = iotSwingView.getListSystemsModel();
			listSystemsModel.addElement(system1);
			listSystemsModel.addElement(system2);
		});
		window.list("listSystems").selectItem(1);
		window.button(JButtonMatcher.withName("btnDeleteSystem")).click();
		verify(controller).removeSystem(system2);
	}
	
	@Test
	public void testAddSystemButtonShouldDelegateToSystemsControllerNewSensor() {
		// Set a system
		int systemId = 9;
		SystemEntity system1 = new SystemEntity(systemId, "bar", "Description of 'bar' ", false);
		GuiActionRunner.execute(() -> {
			DefaultListModel<SystemEntity> listSystemsModel = iotSwingView.getListSystemsModel();
			listSystemsModel.addElement(system1);
		});
		//Select system
		window.list("listSystems").selectItem(0);
		
		// Set sensor fields		
		window.textBox(JTextComponentMatcher.withName("txtSensorId")).enterText("10");
		window.textBox(JTextComponentMatcher.withName("txtSensorName")).enterText("n");
		window.textBox(JTextComponentMatcher.withName("txtSensorDescription")).enterText("d");
		window.textBox(JTextComponentMatcher.withName("txtSensorUnit")).enterText("u");
		window.textBox(JTextComponentMatcher.withName("txtSensorOffset")).enterText("0.1");
		window.textBox(JTextComponentMatcher.withName("txtSensorMultiplier")).enterText("0.2");
		
		window.button(JButtonMatcher.withName("btnAddSensor")).click();
		
		verify(controller).addSensor(new SensorEntity(10, "n", "d", 0.1, 0.2, systemId));
	}

	@Test
	public void testAddSensorButtonOnNotIntIdShouldShowAnError() {
		// Set a system
		int systemId = 9;
		SystemEntity system1 = new SystemEntity(systemId, "bar", "Description of 'bar' ", false);
		GuiActionRunner.execute(() -> {
			DefaultListModel<SystemEntity> listSystemsModel = iotSwingView.getListSystemsModel();
			listSystemsModel.addElement(system1);
		});
		//Select system
		window.list("listSystems").selectItem(0);
		//Set fields
		window.textBox(JTextComponentMatcher.withName("txtSensorId")).enterText("aaa");
		window.textBox(JTextComponentMatcher.withName("txtSensorName")).enterText("n");
		window.textBox(JTextComponentMatcher.withName("txtSensorDescription")).enterText("d");
		window.textBox(JTextComponentMatcher.withName("txtSensorUnit")).enterText("u");
		window.textBox(JTextComponentMatcher.withName("txtSensorOffset")).enterText("0.1");
		window.textBox(JTextComponentMatcher.withName("txtSensorMultiplier")).enterText("0.2");
		
		window.button(JButtonMatcher.withName("btnAddSensor")).click();
		window.label(JLabelMatcher.withName("lblSensorErrorMessageLabel")).requireText("Id not int!");
	}
	
	@Test
	public void testAddSensorButtonOnNotDoubleOffsetShouldShowAnError() {
		// Set a system
		int systemId = 9;
		SystemEntity system1 = new SystemEntity(systemId, "bar", "Description of 'bar' ", false);
		GuiActionRunner.execute(() -> {
			DefaultListModel<SystemEntity> listSystemsModel = iotSwingView.getListSystemsModel();
			listSystemsModel.addElement(system1);
		});
		//Select system
		window.list("listSystems").selectItem(0);
		//Set fields
		window.textBox(JTextComponentMatcher.withName("txtSensorId")).enterText("1");
		window.textBox(JTextComponentMatcher.withName("txtSensorName")).enterText("n");
		window.textBox(JTextComponentMatcher.withName("txtSensorDescription")).enterText("d");
		window.textBox(JTextComponentMatcher.withName("txtSensorUnit")).enterText("u");
		window.textBox(JTextComponentMatcher.withName("txtSensorOffset")).enterText("aaa");
		window.textBox(JTextComponentMatcher.withName("txtSensorMultiplier")).enterText("0.2");
		
		window.button(JButtonMatcher.withName("btnAddSensor")).click();
		window.label(JLabelMatcher.withName("lblSensorErrorMessageLabel")).requireText("Offset not float!");
	}
	
	@Test
	public void testAddSensorButtonOnNotDoubleMultiplierShouldShowAnError() {
		// Set a system
		int systemId = 9;
		SystemEntity system1 = new SystemEntity(systemId, "bar", "Description of 'bar' ", false);
		GuiActionRunner.execute(() -> {
			DefaultListModel<SystemEntity> listSystemsModel = iotSwingView.getListSystemsModel();
			listSystemsModel.addElement(system1);
		});
		//Select system
		window.list("listSystems").selectItem(0);
		//Set fields
		window.textBox(JTextComponentMatcher.withName("txtSensorId")).enterText("1");
		window.textBox(JTextComponentMatcher.withName("txtSensorName")).enterText("n");
		window.textBox(JTextComponentMatcher.withName("txtSensorDescription")).enterText("d");
		window.textBox(JTextComponentMatcher.withName("txtSensorUnit")).enterText("u");
		window.textBox(JTextComponentMatcher.withName("txtSensorOffset")).enterText("0.1");
		window.textBox(JTextComponentMatcher.withName("txtSensorMultiplier")).enterText("bbb");
		
		window.button(JButtonMatcher.withName("btnAddSensor")).click();
		window.label(JLabelMatcher.withName("lblSensorErrorMessageLabel")).requireText("Multiplier not float!");
	}
	
	@Test
	public void testAddSensorButtonOnSystemNotSelectedShouldShowAnError() {
		//Set fields
		window.textBox(JTextComponentMatcher.withName("txtSensorId")).enterText("1");
		window.textBox(JTextComponentMatcher.withName("txtSensorName")).enterText("n");
		window.textBox(JTextComponentMatcher.withName("txtSensorDescription")).enterText("d");
		window.textBox(JTextComponentMatcher.withName("txtSensorUnit")).enterText("u");
		window.textBox(JTextComponentMatcher.withName("txtSensorOffset")).enterText("0.1");
		window.textBox(JTextComponentMatcher.withName("txtSensorMultiplier")).enterText("0.2");
		
		window.button(JButtonMatcher.withName("btnAddSensor")).click();
		window.label(JLabelMatcher.withName("lblSensorErrorMessageLabel")).requireText("No system selected!");
	}
	
	@Test
	public void testDeleteSensorButtonShouldDelegateToSystemsControllerDeleteSensor() {
		SensorEntity sensor1 = new SensorEntity(0, "foo", "Description of 'foo' ", 0.1, 0.2, 10);
		SensorEntity sensor2 = new SensorEntity(1, "bar", "Description of 'bar' ", 0.3, 0.4, 10);
		GuiActionRunner.execute(() -> {
			DefaultListModel<SensorEntity> listSensorsModel = iotSwingView.getListSensorsModel();
			listSensorsModel.addElement(sensor1);
			listSensorsModel.addElement(sensor2);
		});
		window.list("listSensors").selectItem(1);
		window.button(JButtonMatcher.withName("btnDeleteSensor")).click();
		verify(controller).removeSensor(sensor2);
	}
}
