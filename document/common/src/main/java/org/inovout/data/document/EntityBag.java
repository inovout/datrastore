package org.inovout.data.document;

import java.util.Set;

public class EntityBag {

	private String schema;
	public String getSchema() {
		return this.schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	private Set<EntityPackage> entityPackages;
	public Set<EntityPackage> getPackages() {
		return entityPackages;
	}
	public void setPackages(Set<EntityPackage> entityPackages) {
		this.entityPackages = entityPackages;
	}
}
