package org.inovout.datastore.cloud.service.entity.tcp.model;

import java.util.Map;

import com.lmax.disruptor.RingBuffer;

public class ModelCollector {

	private final RingBuffer<ModelEvent> ringBuffer;

	public ModelCollector(RingBuffer<ModelEvent> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public void emit(String schema, Map<String, Object> data) {
		long sequence = ringBuffer.next(); // Grab the next sequence
		try {
			ModelEvent event = ringBuffer.get(sequence); // Get the entry in the
															// Disruptor
															// for the sequence
			event.setSchema(schema);
			event.setData(data);
		} finally {
			ringBuffer.publish(sequence);
		}
	}
}
