package org.inovout.datastore.cloud.service.entity.tcp.model;

import org.inovout.util.ReflectionUtils;
import org.inovout.util.XmlUtils;
import org.w3c.dom.Node;

import com.lmax.disruptor.EventHandler;

public class ModelEventHandler implements EventHandler<ModelEvent> {
	private final Node modelConfigNode;
	private static final String MODEL_BUILDER_CLASS_KEY = "class";
	private final ModelBuilder modelBuilder;

	public ModelEventHandler(Node modelConfigNode) {
		this.modelConfigNode = modelConfigNode;
		modelBuilder = (ModelBuilder) ReflectionUtils.newInstance(XmlUtils
				.getAttribute(this.modelConfigNode, MODEL_BUILDER_CLASS_KEY));
		modelBuilder.init(modelConfigNode);
	}

	public void onEvent(ModelEvent event, long sequence, boolean endOfBatch) {
		System.out.println("Event: " + event);
	}
}