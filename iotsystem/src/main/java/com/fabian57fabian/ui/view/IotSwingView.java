package com.fabian57fabian.ui.view;

import com.fabian57fabian.app.controller.SystemsManagerController;
import com.fabian57fabian.app.model.entities.SensorEntity;
import com.fabian57fabian.app.model.entities.SystemEntity;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JList;

import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JScrollPane;

public class IotSwingView extends JFrame implements IotView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtSystemId;
	private SystemsManagerController systemsManagerController;
	private JLabel lblSystemName;
	private JLabel lblSystemDescription;
	private JTextField txtSystemName;
	private JTextField txtSystemDescription;
	private JButton btnAddSystem;
	private JScrollPane scrollPaneSystem;
	private JButton btnDeleteSystem;
	private JLabel lblSystemErrorMessageLabel;

	private JList<SystemEntity> listSystems;
	private JList<SensorEntity> listSensors;
	private DefaultListModel<SystemEntity> listSystemsModel;
	private DefaultListModel<SensorEntity> listSensorsModel;
	private JLabel lblCurrentSystemDescription;
	private JLabel lblSensorId;
	private JLabel lblSensorName;
	private JLabel lblSensorUnit;
	private JLabel lblSensorOffset;
	private JLabel lblSensorMultiplier;
	private JTextField txtSensorId;
	private JTextField txtSensorName;
	private JTextField txtSensorDescription;
	private JTextField txtSensorUnit;
	private JTextField txtSensorOffset;
	private JTextField txtSensorMultiplier;

	private JButton btnAddSensor;
	private JScrollPane scrollPaneSensor;
	private JButton btnDeleteSensor;
	private JLabel lblSensorDescription;
	private JLabel lblSensorErrorMessageLabel;

	DefaultListModel<SystemEntity> getListSystemsModel() {
		return listSystemsModel;
	}

	DefaultListModel<SensorEntity> getListSensorsModel() {
		return listSensorsModel;
	}

	/**
	 * Create the frame.
	 */
	public IotSwingView() {
		setTitle("Iot Systems");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 303, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 14, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		// All listeners
		KeyAdapter btnSystemAddEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnAddSystem
						.setEnabled(!txtSystemId.getText().trim().isEmpty() && !txtSystemName.getText().trim().isEmpty()
								&& !txtSystemDescription.getText().trim().isEmpty());
			}
		};

		KeyAdapter btnSensorAddEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnAddSensor.setEnabled(!txtSensorId.getText().trim().isEmpty()
						&& !txtSensorName.getText().trim().isEmpty() && !txtSensorDescription.getText().trim().isEmpty()
						&& !txtSensorUnit.getText().trim().isEmpty() && !txtSensorOffset.getText().trim().isEmpty()
						&& !txtSensorMultiplier.getText().trim().isEmpty());
			}
		};

		JLabel lblSystemId = new JLabel("id");
		GridBagConstraints gbc_lblSystemId = new GridBagConstraints();
		gbc_lblSystemId.insets = new Insets(0, 0, 5, 5);
		gbc_lblSystemId.anchor = GridBagConstraints.EAST;
		gbc_lblSystemId.gridx = 0;
		gbc_lblSystemId.gridy = 0;
		lblSystemId.setName("lblSystemId");
		contentPane.add(lblSystemId, gbc_lblSystemId);

		txtSystemId = new JTextField();
		GridBagConstraints gbc_txtSystemId = new GridBagConstraints();
		gbc_txtSystemId.insets = new Insets(0, 0, 5, 5);
		gbc_txtSystemId.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSystemId.gridx = 1;
		gbc_txtSystemId.gridy = 0;
		txtSystemId.setName("txtSystemId");
		contentPane.add(txtSystemId, gbc_txtSystemId);
		txtSystemId.setColumns(10);
		txtSystemId.addKeyListener(btnSystemAddEnabler);

		lblSensorId = new JLabel("id");
		GridBagConstraints gbc_lblSensorId = new GridBagConstraints();
		gbc_lblSensorId.anchor = GridBagConstraints.EAST;
		gbc_lblSensorId.insets = new Insets(0, 0, 5, 5);
		gbc_lblSensorId.gridx = 3;
		gbc_lblSensorId.gridy = 0;
		lblSensorId.setName("lblSensorId");
		contentPane.add(lblSensorId, gbc_lblSensorId);

		txtSensorId = new JTextField();
		GridBagConstraints gbc_txtSensorId = new GridBagConstraints();
		gbc_txtSensorId.insets = new Insets(0, 0, 5, 0);
		gbc_txtSensorId.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSensorId.gridx = 4;
		gbc_txtSensorId.gridy = 0;
		txtSensorId.setName("txtSensorId");
		txtSensorId.setName("txtSensorId");
		contentPane.add(txtSensorId, gbc_txtSensorId);
		txtSensorId.setColumns(10);
		txtSensorId.addKeyListener(btnSensorAddEnabler);

		lblSystemName = new JLabel("name");
		GridBagConstraints gbc_lblSystemName = new GridBagConstraints();
		gbc_lblSystemName.anchor = GridBagConstraints.EAST;
		gbc_lblSystemName.insets = new Insets(0, 0, 5, 5);
		gbc_lblSystemName.gridx = 0;
		gbc_lblSystemName.gridy = 1;
		lblSystemName.setName("lblSystemName");
		contentPane.add(lblSystemName, gbc_lblSystemName);

		txtSystemName = new JTextField();
		GridBagConstraints gbc_txtSystemName = new GridBagConstraints();
		gbc_txtSystemName.insets = new Insets(0, 0, 5, 5);
		gbc_txtSystemName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSystemName.gridx = 1;
		gbc_txtSystemName.gridy = 1;
		txtSystemName.setName("txtSystemName");
		contentPane.add(txtSystemName, gbc_txtSystemName);
		txtSystemName.setColumns(10);
		txtSystemName.addKeyListener(btnSystemAddEnabler);

		lblSensorName = new JLabel("name");
		GridBagConstraints gbc_lblSensorName = new GridBagConstraints();
		gbc_lblSensorName.anchor = GridBagConstraints.EAST;
		gbc_lblSensorName.insets = new Insets(0, 0, 5, 5);
		gbc_lblSensorName.gridx = 3;
		gbc_lblSensorName.gridy = 1;
		lblSensorName.setName("lblSensorName");
		contentPane.add(lblSensorName, gbc_lblSensorName);

		txtSensorName = new JTextField();
		GridBagConstraints gbc_txtSensorName = new GridBagConstraints();
		gbc_txtSensorName.insets = new Insets(0, 0, 5, 0);
		gbc_txtSensorName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSensorName.gridx = 4;
		gbc_txtSensorName.gridy = 1;
		txtSensorName.setName("txtSensorName");
		contentPane.add(txtSensorName, gbc_txtSensorName);
		txtSensorName.setColumns(10);
		txtSensorName.addKeyListener(btnSensorAddEnabler);

		lblSystemDescription = new JLabel("desc");
		GridBagConstraints gbc_lblSystemDescription = new GridBagConstraints();
		gbc_lblSystemDescription.anchor = GridBagConstraints.EAST;
		gbc_lblSystemDescription.insets = new Insets(0, 0, 5, 5);
		gbc_lblSystemDescription.gridx = 0;
		gbc_lblSystemDescription.gridy = 2;
		lblSystemDescription.setName("lblSystemDescription");
		contentPane.add(lblSystemDescription, gbc_lblSystemDescription);

		txtSystemDescription = new JTextField();
		GridBagConstraints gbc_txtSystemDescription = new GridBagConstraints();
		gbc_txtSystemDescription.insets = new Insets(0, 0, 5, 5);
		gbc_txtSystemDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSystemDescription.gridx = 1;
		gbc_txtSystemDescription.gridy = 2;
		txtSystemDescription.setName("txtSystemDescription");
		contentPane.add(txtSystemDescription, gbc_txtSystemDescription);
		txtSystemDescription.setColumns(10);
		txtSystemDescription.addKeyListener(btnSystemAddEnabler);

		lblSensorDescription = new JLabel("description");
		GridBagConstraints gbc_lblSensorDescription = new GridBagConstraints();
		gbc_lblSensorDescription.anchor = GridBagConstraints.EAST;
		gbc_lblSensorDescription.insets = new Insets(0, 0, 5, 5);
		gbc_lblSensorDescription.gridx = 3;
		gbc_lblSensorDescription.gridy = 2;
		lblSensorDescription.setName("lblSensorDescription");
		contentPane.add(lblSensorDescription, gbc_lblSensorDescription);

		txtSensorDescription = new JTextField();
		GridBagConstraints gbc_txtSensorDescription = new GridBagConstraints();
		gbc_txtSensorDescription.insets = new Insets(0, 0, 5, 0);
		gbc_txtSensorDescription.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSensorDescription.gridx = 4;
		gbc_txtSensorDescription.gridy = 2;
		txtSensorDescription.setName("txtSensorDescription");
		contentPane.add(txtSensorDescription, gbc_txtSensorDescription);
		txtSensorDescription.setColumns(10);
		txtSensorDescription.addKeyListener(btnSensorAddEnabler);

		lblSensorUnit = new JLabel("unit");
		GridBagConstraints gbc_lblSensorUnit = new GridBagConstraints();
		gbc_lblSensorUnit.anchor = GridBagConstraints.EAST;
		gbc_lblSensorUnit.insets = new Insets(0, 0, 5, 5);
		gbc_lblSensorUnit.gridx = 3;
		gbc_lblSensorUnit.gridy = 3;
		lblSensorUnit.setName("lblSensorUnit");
		contentPane.add(lblSensorUnit, gbc_lblSensorUnit);

		txtSensorUnit = new JTextField();
		GridBagConstraints gbc_txtSensorUnit = new GridBagConstraints();
		gbc_txtSensorUnit.insets = new Insets(0, 0, 5, 0);
		gbc_txtSensorUnit.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSensorUnit.gridx = 4;
		gbc_txtSensorUnit.gridy = 3;
		txtSensorUnit.setName("txtSensorUnit");
		contentPane.add(txtSensorUnit, gbc_txtSensorUnit);
		txtSensorUnit.setColumns(10);
		txtSensorUnit.addKeyListener(btnSensorAddEnabler);

		lblSensorOffset = new JLabel("offset");
		GridBagConstraints gbc_lblSensorOffset = new GridBagConstraints();
		gbc_lblSensorOffset.anchor = GridBagConstraints.EAST;
		gbc_lblSensorOffset.insets = new Insets(0, 0, 5, 5);
		gbc_lblSensorOffset.gridx = 3;
		gbc_lblSensorOffset.gridy = 4;
		lblSensorOffset.setName("lblSensorOffset");
		contentPane.add(lblSensorOffset, gbc_lblSensorOffset);

		txtSensorOffset = new JTextField();
		GridBagConstraints gbc_txtSensorOffset = new GridBagConstraints();
		gbc_txtSensorOffset.insets = new Insets(0, 0, 5, 0);
		gbc_txtSensorOffset.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSensorOffset.gridx = 4;
		gbc_txtSensorOffset.gridy = 4;
		txtSensorOffset.setName("txtSensorOffset");
		contentPane.add(txtSensorOffset, gbc_txtSensorOffset);
		txtSensorOffset.setColumns(10);
		txtSensorOffset.addKeyListener(btnSensorAddEnabler);

		lblSensorMultiplier = new JLabel("multiplier");
		GridBagConstraints gbc_lblSensorMultiplier = new GridBagConstraints();
		gbc_lblSensorMultiplier.anchor = GridBagConstraints.EAST;
		gbc_lblSensorMultiplier.insets = new Insets(0, 0, 5, 5);
		gbc_lblSensorMultiplier.gridx = 3;
		gbc_lblSensorMultiplier.gridy = 5;
		lblSensorMultiplier.setName("lblSensorMultiplier");
		contentPane.add(lblSensorMultiplier, gbc_lblSensorMultiplier);

		txtSensorMultiplier = new JTextField();
		GridBagConstraints gbc_txtSensorMultiplier = new GridBagConstraints();
		gbc_txtSensorMultiplier.insets = new Insets(0, 0, 5, 0);
		gbc_txtSensorMultiplier.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSensorMultiplier.gridx = 4;
		gbc_txtSensorMultiplier.gridy = 5;
		txtSensorMultiplier.setName("txtSensorMultiplier");
		contentPane.add(txtSensorMultiplier, gbc_txtSensorMultiplier);
		txtSensorMultiplier.setColumns(10);
		txtSensorMultiplier.addKeyListener(btnSensorAddEnabler);

		btnAddSystem = new JButton("add");
		btnAddSystem.setEnabled(false);
		GridBagConstraints gbc_btnAddSystem = new GridBagConstraints();
		gbc_btnAddSystem.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddSystem.gridx = 1;
		gbc_btnAddSystem.gridy = 6;
		btnAddSystem.setName("btnAddSystem");
		contentPane.add(btnAddSystem, gbc_btnAddSystem);

		btnAddSensor = new JButton("add");
		btnAddSensor.setEnabled(false);
		GridBagConstraints gbc_btnAddSensor = new GridBagConstraints();
		gbc_btnAddSensor.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddSensor.gridx = 4;
		gbc_btnAddSensor.gridy = 6;
		btnAddSensor.setName("btnAddSensor");
		contentPane.add(btnAddSensor, gbc_btnAddSensor);

		scrollPaneSystem = new JScrollPane();
		GridBagConstraints gbc_scrollPaneSystem = new GridBagConstraints();
		gbc_scrollPaneSystem.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPaneSystem.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneSystem.gridx = 1;
		gbc_scrollPaneSystem.gridy = 8;
		contentPane.add(scrollPaneSystem, gbc_scrollPaneSystem);

		listSystemsModel = new DefaultListModel<>();
		listSystems = new JList<>(listSystemsModel);
		listSystems.addListSelectionListener(e -> btnDeleteSystem.setEnabled(listSystems.getSelectedIndex() != -1));
		listSystems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listSystems.setName("listSystems");
		scrollPaneSystem.setViewportView(listSystems);

		scrollPaneSensor = new JScrollPane();
		GridBagConstraints gbc_scrollPaneSensor = new GridBagConstraints();
		gbc_scrollPaneSensor.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneSensor.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneSensor.gridx = 4;
		gbc_scrollPaneSensor.gridy = 8;
		contentPane.add(scrollPaneSensor, gbc_scrollPaneSensor);

		listSensorsModel = new DefaultListModel<>();
		listSensors = new JList<>(listSensorsModel);
		listSensors.addListSelectionListener(e -> btnDeleteSensor.setEnabled(listSensors.getSelectedIndex() != -1));
		listSensors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listSensors.setName("listSensors");
		scrollPaneSensor.setViewportView(listSensors);

		lblCurrentSystemDescription = new JLabel(" ");
		GridBagConstraints gbc_lblCurrentSystemDescription = new GridBagConstraints();
		gbc_lblCurrentSystemDescription.insets = new Insets(0, 0, 5, 5);
		gbc_lblCurrentSystemDescription.gridx = 1;
		gbc_lblCurrentSystemDescription.gridy = 9;
		lblCurrentSystemDescription.setName("lblCurrentSystemDescription");
		contentPane.add(lblCurrentSystemDescription, gbc_lblCurrentSystemDescription);

		btnDeleteSystem = new JButton("delete");
		btnDeleteSystem.setEnabled(false);
		GridBagConstraints gbc_btnDeleteSystem = new GridBagConstraints();
		gbc_btnDeleteSystem.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteSystem.gridx = 1;
		gbc_btnDeleteSystem.gridy = 10;
		btnDeleteSystem.setName("btnDeleteSystem");
		contentPane.add(btnDeleteSystem, gbc_btnDeleteSystem);

		btnDeleteSensor = new JButton("delete");
		btnDeleteSensor.setEnabled(false);
		GridBagConstraints gbc_btnDeleteSensor = new GridBagConstraints();
		gbc_btnDeleteSensor.insets = new Insets(0, 0, 5, 0);
		gbc_btnDeleteSensor.gridx = 4;
		gbc_btnDeleteSensor.gridy = 10;
		btnDeleteSensor.setName("btnDeleteSensor");
		contentPane.add(btnDeleteSensor, gbc_btnDeleteSensor);

		lblSystemErrorMessageLabel = new JLabel(" ");
		GridBagConstraints gbc_errorMessageLabel = new GridBagConstraints();
		gbc_errorMessageLabel.insets = new Insets(0, 0, 0, 5);
		gbc_errorMessageLabel.gridx = 1;
		gbc_errorMessageLabel.gridy = 11;
		lblSystemErrorMessageLabel.setName("lblSystemErrorMessageLabel");
		contentPane.add(lblSystemErrorMessageLabel, gbc_errorMessageLabel);

		lblSensorErrorMessageLabel = new JLabel(" ");
		GridBagConstraints gbc_lblSensorErrorMessageLabel = new GridBagConstraints();
		gbc_lblSensorErrorMessageLabel.gridx = 4;
		gbc_lblSensorErrorMessageLabel.gridy = 11;
		lblSensorErrorMessageLabel.setName("lblSensorErrorMessageLabel");
		contentPane.add(lblSensorErrorMessageLabel, gbc_lblSensorErrorMessageLabel);
	}

	@Override
	public void showSystems(List<SystemEntity> systems) {
		systems.stream().forEach(listSystemsModel::addElement);
	}

	@Override
	public void showOneSystem(SystemEntity system) {
		lblCurrentSystemDescription.setText(system.getDescription());
	}

	@Override
	public void showOneSystemError(String message, SystemEntity system) {
		lblSystemErrorMessageLabel.setText(message + ": " + system);

	}

	public void setController(SystemsManagerController controller) {
		this.systemsManagerController = controller;
	}

	@Override
	public void ShowSensorsOfSystem(List<SensorEntity> sensors) {
		sensors.stream().forEach(listSensorsModel::addElement);
	}

	@Override
	public void removeOneSystem(SystemEntity system) {
		listSystemsModel.removeElement(system);
	}

	@Override
	public void onSystemAdded(SystemEntity system) {
		listSystemsModel.addElement(system);
		resetSystemErrorLabel();
	}

	@Override
	public void onSystemRemoved(SystemEntity system) {
		listSystemsModel.removeElement(system);
		resetSystemErrorLabel();
	}

	private void resetSystemErrorLabel() {
		lblSystemErrorMessageLabel.setText(" ");
	}

}
