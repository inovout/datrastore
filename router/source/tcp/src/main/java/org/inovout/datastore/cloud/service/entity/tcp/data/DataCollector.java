package org.inovout.datastore.cloud.service.entity.tcp.data;

import com.lmax.disruptor.RingBuffer;

public class DataCollector {

	private final RingBuffer<DataEvent> ringBuffer;

	public DataCollector(RingBuffer<DataEvent> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public void emit(String schema, byte[] packet) {
		long sequence = ringBuffer.next(); // Grab the next sequence
		try {
			DataEvent event = ringBuffer.get(sequence); // Get the entry in the
														// Disruptor
														// for the sequence
			event.setSchema(schema);
			event.setPacket(packet);
		} finally {
			ringBuffer.publish(sequence);
		}
	}
}
