package org.inovout.data.lamx;

import org.inovout.data.document.EntityBag;

public class EntityBagEvent {
	private EntityBag entityBag;
	public void setEntityBag(EntityBag entityBag){
		this.entityBag=entityBag;
	}
	public EntityBag getEntityBag(){
		return this.entityBag;
	}
}
