package org.inovout.datastore.entity.metadata.model;

public class Element {
	private int propertyId;
	public int getPropertyId() {
		return this.propertyId;
	}
	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Bag collection;

	public Bag getCollection() {
		return this.collection;
	}

	public void setCollection(Bag collection) {
		this.collection = collection;
	}
	private ElementType type;
	public ElementType Type(){
		return this.type;
	}
	public void setType(ElementType type){
		this.type=type;
	}
	private boolean isPrimaryKey;
	public boolean getIsPrimaryKey(){
		return isPrimaryKey;
	}
	public void setIsPrimaryKey(boolean isPrimaryKey){
		this.isPrimaryKey=isPrimaryKey;
	}
	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		Element that = (Element) o;

		if ( collection != null ? ! collection.equals( that.collection ) : that.collection != null ) {
			return false;
		}
		if ( name != null ? !name.equals( that.name ) : that.name != null ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + ( collection != null ? collection.hashCode() : 0 );
		return result;	}
}
