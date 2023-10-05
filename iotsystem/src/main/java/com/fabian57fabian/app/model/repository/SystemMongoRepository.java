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

	public static final String DB_NAME = "iotsystems";
	public static final String SYSTEM_COLLECTION_NAME = "system";
	private MongoCollection<Document> systemCollection;
	
	public SystemMongoRepository(MongoClient client) {
		systemCollection = client
				.getDatabase(DB_NAME)
				.getCollection(SYSTEM_COLLECTION_NAME);
	}
	
	private SystemEntity fromDocumentToStudent(Document d) {
		return new SystemEntity((int)d.get("id"), ""+d.get("name"), ""+d.get("description"), (Boolean)d.get("active"));
	}
	
	@Override
	public List<SystemEntity> RetrieveSystemNames() {
		return StreamSupport.
				stream(systemCollection.find().spliterator(), false)
				.map(d -> fromDocumentToStudent(d))
				.collect(Collectors.toList());
	}

	@Override
	public SystemEntity GetSystemById(int id) {
		Document d = systemCollection.find(Filters.eq("id", id)).first();
		if (d != null)
		return fromDocumentToStudent(d);
		return null;
	}

}
