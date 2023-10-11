package com.fabian57fabian.app.swing;

import java.awt.EventQueue;

import com.fabian57fabian.app.controller.SystemsManagerController;
import com.fabian57fabian.app.model.repository.SystemMongoRepository;
import com.fabian57fabian.app.view.IotSwingView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class IotSwingApp {
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				String mongoHost = "localhost";
				int mongoPort = 27017;
				if (args.length > 0)
					mongoHost = args[0];
				if (args.length > 1)
					mongoPort = Integer.parseInt(args[1]);
				SystemMongoRepository systemRepository = new SystemMongoRepository(
						new MongoClient(new ServerAddress(mongoHost, mongoPort)));
				IotSwingView systemView = new IotSwingView();
				SystemsManagerController systemController = new SystemsManagerController(systemRepository, systemView);
				systemView.setController(systemController);
				systemView.setVisible(true);
				systemController.viewAllSystems();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
