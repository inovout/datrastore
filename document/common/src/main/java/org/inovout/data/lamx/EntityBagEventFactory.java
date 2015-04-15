package org.inovout.data.lamx;

import org.inovout.data.lamx.EntityBagEvent;

import com.lmax.disruptor.EventFactory;

public class EntityBagEventFactory implements EventFactory<EntityBagEvent> {
	public EntityBagEvent newInstance() {
		return new EntityBagEvent();
	}
}
