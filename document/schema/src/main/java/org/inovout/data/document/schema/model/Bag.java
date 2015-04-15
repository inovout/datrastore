package org.inovout.data.document.schema.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Bag")
public class Bag {

	private int bagId;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getBagId(){
		return this.bagId;
	}
	public void setBagId(int bagId){
		this.bagId=bagId;
	}
	
	
	private String name;
	@Column(name = "Name")
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name=name;
	}
	
	private String database;
	@Column(name = "DatabaseName")
	public String getDatabase(){
		return this.database;
	}
	public void setDatabase(String database){
		this.database=database;
	}
	private Set<Element> elements=new HashSet<Element>();
	
	@OneToMany(cascade={CascadeType.MERGE,CascadeType.REMOVE},mappedBy = "bag")
	public Set<Element> getElements(){
		return this.elements;
	}
	public void setElements(Set<Element> elements){
		this.elements=elements;
	}
	
	public void addElement(Element element){
		this.elements.add(element);
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
