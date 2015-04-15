package org.inovout.data.lamx;

import org.inovout.data.handler.EntityBagHandler;
import org.inovout.data.lamx.EntityBagEvent;

import com.lmax.disruptor.EventHandler;

public class EntityBagEventHandler implements EventHandler<EntityBagEvent> {

	private EntityBagHandler entityBagHandler;

	public EntityBagEventHandler(EntityBagHandler entityBagHandler) {
		this.entityBagHandler = entityBagHandler;
	}

	@Override
	public void onEvent(EntityBagEvent event, long sequence, boolean endOfBatch) {
		this.entityBagHandler.Handle(event.getEntityBag());
	}
}
