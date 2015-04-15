package org.inovout.data.document;

import java.util.Map;
import java.util.Set;

public class EntityPackage {

	private String collection;

	public String getCollection() {
		return this.collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	private EntityAction action;

	public EntityAction getAction() {
		return this.action;
	}

	public void setAction(EntityAction action) {
		this.action = action;
	}

	private Set<Map<String, Object>> entities;

	public Set<Map<String, Object>> getEntitis() {
		return entities;
	}

	public void setEntitis(Set<Map<String, Object>> entities) {
		this.entities = entities;
	}
}
