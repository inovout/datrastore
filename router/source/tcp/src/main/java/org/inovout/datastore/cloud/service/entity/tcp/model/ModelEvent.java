package org.inovout.datastore.cloud.service.entity.tcp.model;

import java.util.Map;

public class ModelEvent {
	private String schema;

	public String getSchema(){
		return this.schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}

	private Map<String, Object> data;
	public Map<String,Object> getData(){
		return this.data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
