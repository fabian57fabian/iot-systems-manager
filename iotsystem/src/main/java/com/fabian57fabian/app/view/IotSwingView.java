package com.fabian57fabian.app.view;

import com.fabian57fabian.app.controller.SystemsManagerController;
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
import javax.swing.JButton;
import javax.swing.JScrollPane;

public class IotSwingView extends JFrame implements IotView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtId;
	private SystemsManagerController systemsManagerController;
	private JLabel lblName;
	private JLabel lblDescription;
	private JTextField txtName;
	private JTextField txtDesc;
	private JButton btnAdd;
	private JScrollPane scrollPaneSystem;
	private JButton btnDelete;
	private JLabel errorMessageLabel;

	private JList<SystemEntity> listSystems;
	private DefaultListModel<SystemEntity> listSystemsModel;
	private JLabel lblSystemDescription;

	DefaultListModel<SystemEntity> getListSystemsModel() {
		return listSystemsModel;
	}

	/**
	 * Create the frame.
	 */
	public IotSwingView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblId = new JLabel("id");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.insets = new Insets(0, 0, 5, 5);
		gbc_lblId.anchor = GridBagConstraints.EAST;
		gbc_lblId.gridx = 0;
		gbc_lblId.gridy = 0;
		contentPane.add(lblId, gbc_lblId);

		txtId = new JTextField();
		GridBagConstraints gbc_txtId = new GridBagConstraints();
		gbc_txtId.insets = new Insets(0, 0, 5, 0);
		gbc_txtId.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtId.gridx = 1;
		gbc_txtId.gridy = 0;
		txtId.setName("txtId");
		contentPane.add(txtId, gbc_txtId);
		txtId.setColumns(10);

		lblName = new JLabel("name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 1;
		contentPane.add(lblName, gbc_lblName);

		txtName = new JTextField();
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.insets = new Insets(0, 0, 5, 0);
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.gridx = 1;
		gbc_txtName.gridy = 1;
		txtName.setName("nameTextBox");
		contentPane.add(txtName, gbc_txtName);
		txtName.setColumns(10);

		lblDescription = new JLabel("desc");
		GridBagConstraints gbc_lblDescription = new GridBagConstraints();
		gbc_lblDescription.anchor = GridBagConstraints.EAST;
		gbc_lblDescription.insets = new Insets(0, 0, 5, 5);
		gbc_lblDescription.gridx = 0;
		gbc_lblDescription.gridy = 2;
		contentPane.add(lblDescription, gbc_lblDescription);

		txtDesc = new JTextField();
		GridBagConstraints gbc_txtDesc = new GridBagConstraints();
		gbc_txtDesc.insets = new Insets(0, 0, 5, 0);
		gbc_txtDesc.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDesc.gridx = 1;
		gbc_txtDesc.gridy = 2;
		txtDesc.setName("descTextBox");
		contentPane.add(txtDesc, gbc_txtDesc);
		txtDesc.setColumns(10);

		btnAdd = new JButton("add");
		btnAdd.setEnabled(false);
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
		gbc_btnAdd.gridx = 1;
		gbc_btnAdd.gridy = 3;
		contentPane.add(btnAdd, gbc_btnAdd);

		scrollPaneSystem = new JScrollPane();
		GridBagConstraints gbc_scrollPaneSystem = new GridBagConstraints();
		gbc_scrollPaneSystem.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneSystem.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneSystem.gridx = 1;
		gbc_scrollPaneSystem.gridy = 4;
		contentPane.add(scrollPaneSystem, gbc_scrollPaneSystem);

		listSystemsModel = new DefaultListModel<>();
		listSystems = new JList<>(listSystemsModel);
		listSystems.addListSelectionListener(e -> btnDelete.setEnabled(listSystems.getSelectedIndex() != -1));
		listSystems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listSystems.setName("listSystems");
		scrollPaneSystem.setViewportView(listSystems);
		
		lblSystemDescription = new JLabel(" ");
		GridBagConstraints gbc_lblSystemDescription = new GridBagConstraints();
		gbc_lblSystemDescription.insets = new Insets(0, 0, 5, 0);
		gbc_lblSystemDescription.gridx = 1;
		gbc_lblSystemDescription.gridy = 5;
		lblSystemDescription.setName("lblSystemDescription"); 
		contentPane.add(lblSystemDescription, gbc_lblSystemDescription);

		btnDelete = new JButton("delete");
		btnDelete.setEnabled(false);
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.insets = new Insets(0, 0, 5, 0);
		gbc_btnDelete.gridx = 1;
		gbc_btnDelete.gridy = 6;
		btnDelete.setName("btnDelete");
		contentPane.add(btnDelete, gbc_btnDelete);

		errorMessageLabel = new JLabel(" ");
		GridBagConstraints gbc_errorMessageLabel = new GridBagConstraints();
		gbc_errorMessageLabel.gridx = 1;
		gbc_errorMessageLabel.gridy = 7;
		errorMessageLabel.setName("errorMessageLabel");
		contentPane.add(errorMessageLabel, gbc_errorMessageLabel);
	}

	@Override
	public void showSystems(List<SystemEntity> systems) {
		systems.stream().forEach(listSystemsModel::addElement);
	}

	@Override
	public void showOneSystem(SystemEntity system) {
		lblSystemDescription.setText(system.getDescription());
	}

	@Override
	public void showOneSystemError(String message, SystemEntity system) {
		errorMessageLabel.setText(message + ": " + system);

	}

	public void setController(SystemsManagerController controller) {
		this.systemsManagerController = controller;
	}

}
