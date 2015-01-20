package org.inovout.datastore.entity.indices;

import java.util.HashSet;
import java.util.Set;

public class MappingInfo {
	private String type;

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private IndexAction action;

	public IndexAction getAction() {
		return this.action;
	}

	public void setAction(IndexAction action) {
		this.action = action;
	}

	private Set<PropertyInfo> properties = new HashSet<PropertyInfo>();

	public Set<PropertyInfo> getProperties() {
		return this.properties;
	}

	public void setProperties(Set<PropertyInfo> properties) {
		this.properties = properties;
	}
	private String index;
	public String getIndex() {
		return index;
	}
	public void setIndex(String index){
		this.index=index;
	}
}
