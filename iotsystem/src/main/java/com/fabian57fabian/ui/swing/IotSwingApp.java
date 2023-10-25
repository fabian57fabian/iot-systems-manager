package com.fabian57fabian.ui.swing;

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fabian57fabian.app.controller.SystemsManagerController;
import com.fabian57fabian.app.model.repository.SystemMongoRepository;
import com.fabian57fabian.app.model.service.SystemService;
import com.fabian57fabian.ui.view.IotSwingView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class IotSwingApp implements Callable<Void> {
	
	@Option(names = { "--mongo-host" }, description = "MongoDB host address")
	private String mongoHost = "localhost";
	
	@Option(names = { "--mongo-port" }, description = "MongoDB host port")
	private int mongoPort = 27017;
	
	public static void main(String[] args) {
		new CommandLine(new IotSwingApp()).execute(args);
	}
	
	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				MongoClient client = new MongoClient(new ServerAddress(mongoHost, mongoPort));
				SystemMongoRepository systemRepository = new SystemMongoRepository(client);
				SystemService systemService = new SystemService(systemRepository);
				IotSwingView iotView = new IotSwingView();
				SystemsManagerController systemController = new SystemsManagerController(systemService, iotView);
				iotView.setController(systemController);
				iotView.setVisible(true);
				systemController.viewAllSystems();
			} catch (Exception e) {
				Logger.getLogger(IotSwingApp.class.getName()).log(Level.SEVERE, "Exception", e);
			}
		});
		return null;
	}
}
