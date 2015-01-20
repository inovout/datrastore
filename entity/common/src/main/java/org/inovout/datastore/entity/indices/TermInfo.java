package org.inovout.datastore.entity.indices;

import java.util.Arrays;

public class TermInfo {
	private String index;
	public String getIndex() {
		return this.index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	private String type;

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
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
