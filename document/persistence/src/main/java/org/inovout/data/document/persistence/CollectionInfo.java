package org.inovout.data.document.persistence;

import java.util.Arrays;

public class CollectionInfo {
	private String database;

	public String getDatabase() {
		return this.database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	private String collection;

	public String getCollection() {
		return this.collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}
	
	private String[] primaryKeys;
	public String[] getPrimaryKeys(){
		return this.primaryKeys;
	}
	public void setPrimaryKeys(String[] primaryKeys){
		Arrays.sort(primaryKeys);
		this.primaryKeys=primaryKeys;
	}
}
