package org.inovout.datastore.entity.metadata.model;

import java.util.HashSet;
import java.util.Set;

public class Bag {

	private int collectionId;
	public int getCollectionId(){
		return this.collectionId;
	}
	public void setCollectionId(int collectionId){
		this.collectionId=collectionId;
	}
	private String name;
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name=name;
	}
	private String database;
	public String getDatabase(){
		return this.database;
	}
	public void setDatabase(String database){
		this.database=database;
	}
	private Set<Element> properties=new HashSet<Element>();
	public Set<Element> getPropertires(){
		return this.properties;
	}
	public void setProperties(Set<Element> properties){
		this.properties=properties;
	}
	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		Bag that = (Bag) o;

		if ( name != null ? !name.equals( that.name ) : that.name != null ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
}
