package org.inovout.datastore.cloud.service.entity.tcp.model;

import com.lmax.disruptor.EventFactory;

public class ModelEventFactory implements EventFactory<ModelEvent> {
	public ModelEvent newInstance() {
		return new ModelEvent();
	}
}
