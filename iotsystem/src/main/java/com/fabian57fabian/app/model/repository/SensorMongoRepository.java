package com.fabian57fabian.app.model.repository;

import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.fabian57fabian.app.model.entities.SensorEntity;

public class SensorMongoRepository implements SensorRepository {
	private MongoCollection<Document> sensorsCollection;

	public SensorMongoRepository(MongoClient client, String dbName, String collectionName) {
		sensorsCollection = client.getDatabase(dbName).getCollection(collectionName);
	}

	private SensorEntity fromDocumentToSensor(Document d) {
		return new SensorEntity((int) d.get("id"), "" + d.get("name"), "" + d.get("description"),
				(double) d.get("offset"), (double) d.get("multiplier"));
	}

	@Override
	public List<SensorEntity> retrieveSensorsNames() {
		return StreamSupport.stream(sensorsCollection.find().spliterator(), false).map(this::fromDocumentToSensor)
				.collect(Collectors.toList());
	}

	@Override
	public SensorEntity getSensorById(int id) {
		Document d = sensorsCollection.find(Filters.eq("id", id)).first();
		if (d != null) {
			return fromDocumentToSensor(d);
		}
		return null;
	}

	@Override
	public void save(SensorEntity system) {
		sensorsCollection.insertOne(new Document()
				.append("id", system.getId())
				.append("name", system.getName())
				.append("description", system.getDescription())
				.append("offset", system.getOffset())
				.append("multiplier", system.getMultiplier()));
	}

	@Override
	public void delete(int id) {
		sensorsCollection.deleteOne(Filters.eq("id", id));
	}

}