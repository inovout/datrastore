package org.inovout.datastore.cloud.service.entity.tcp.data;

import com.lmax.disruptor.EventFactory;

public class DataEventFactory implements EventFactory<DataEvent> {
	public DataEvent newInstance() {
		return new DataEvent();
	}
}
