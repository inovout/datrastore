package org.inovout.data.lamx;

import org.inovout.data.document.EntityBag;
import org.inovout.data.lamx.EntityBagEvent;

import com.lmax.disruptor.RingBuffer;

public class EntityBagEventProducer {
	private final RingBuffer<EntityBagEvent> ringBuffer;

	public EntityBagEventProducer(RingBuffer<EntityBagEvent> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public void onData(EntityBag entityBag) {
		long sequence = ringBuffer.next();
		try {
			EntityBagEvent event = ringBuffer.get(sequence);
			event.setEntityBag(entityBag);
		} finally {
			ringBuffer.publish(sequence);
		}
	}

}
