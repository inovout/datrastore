package org.inovout.datastore.entity;

import java.util.Map;
import java.util.Set;

public class EntityBag {

	private String schema;
	public String getCollection() {
		return this.schema;
	}
	public void setCollection(String schema) {
		this.schema = schema;
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
