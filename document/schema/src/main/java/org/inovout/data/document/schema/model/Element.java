package org.inovout.data.document.schema.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Element")
public class Element {
	private int elementId;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getElementId() {
		return this.elementId;
	}

	public void setElementId(int elementId) {
		this.elementId = elementId;
	}

	private String name;
	@Column(name = "Name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Bag bag;

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REMOVE })
	@JoinColumn(name = "bagId")
	public Bag getBag() {
		return this.bag;
	}

	public void setBag(Bag bag) {
		this.bag = bag;
	}

	private ElementType type;

	@Enumerated(EnumType.STRING) 
	@Column(name = "ElementType", nullable = true)
	public ElementType Type() {
		return this.type;
	}

	
	public void setType(ElementType type) {
		this.type = type;
	}

	private boolean isPrimaryKey;

	@Column(name = "IsPrimaryKey")
	public boolean getIsPrimaryKey() {
		return isPrimaryKey;
	}

	public void setIsPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Element that = (Element) o;

		if (bag != null ? !bag.equals(that.bag) : that.bag != null) {
			return false;
		}
		if (name != null ? !name.equals(that.name) : that.name != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + (bag != null ? bag.hashCode() : 0);
		return result;
	}
}
