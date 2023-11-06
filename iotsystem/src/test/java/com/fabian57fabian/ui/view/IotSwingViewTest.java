package com.fabian57fabian.ui.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

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
}
