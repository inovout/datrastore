package org.inovout.datastore.cloud.services.table.model;

public class ColumnInfo {
	private String name;
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name=name;
	}
	private ColumnType type;
	public ColumnType Type(){
		return this.type;
	}
	public void setType(ColumnType type){
		this.type=type;
	}
	private boolean isPrimaryKey;
	public boolean getIsPrimaryKey(){
		return isPrimaryKey;
	}
	public void setIsPrimaryKey(boolean isPrimaryKey){
		this.isPrimaryKey=isPrimaryKey;
	}
}
