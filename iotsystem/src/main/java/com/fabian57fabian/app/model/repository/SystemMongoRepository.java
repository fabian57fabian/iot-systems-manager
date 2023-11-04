package com.fabian57fabian.app.model.repository;

import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.fabian57fabian.app.model.entities.SystemEntity;

public class SystemMongoRepository implements SystemRepository {
	private MongoCollection<Document> systemCollection;

	public SystemMongoRepository(MongoClient client, String dbName, String collectionName) {
		systemCollection = client.getDatabase(dbName).getCollection(collectionName);
	}

	private SystemEntity fromDocumentToStudent(Document d) {
		return new SystemEntity((int) d.get("id"), "" + d.get("name"), "" + d.get("description"),
				(Boolean) d.get("active"));
	}

	@Override
	public List<SystemEntity> retrieveSystemNames() {
		return StreamSupport.stream(systemCollection.find().spliterator(), false).map(this::fromDocumentToStudent)
				.collect(Collectors.toList());
	}

	@Override
	public SystemEntity getSystemById(int id) {
		Document d = systemCollection.find(Filters.eq("id", id)).first();
		if (d != null) {
			return fromDocumentToStudent(d);
		}
		return null;
	}

	@Override
	public void save(SystemEntity system) {
		systemCollection.insertOne(new Document().append("id", system.getId()).append("name", system.getName())
				.append("description", system.getDescription()).append("active", system.getAcrive()));
	}

	@Override
	public void delete(int id) {
		systemCollection.deleteOne(Filters.eq("id", id));
	}

}
