package org.inovout.datastore.entity.indices;

public class PropertyInfo {

	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return null;
	}

	private boolean isPrimaryKey;

	public void setIsPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public boolean getIsPrimaryKey() {
		return isPrimaryKey;
	}
}
