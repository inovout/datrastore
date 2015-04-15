package org.inovout.datastore.cloud.service.entity.tcp.data;

import java.util.Map;

import org.inovout.datastore.cloud.service.entity.tcp.model.ModelCollector;
import org.inovout.util.ReflectionUtils;
import org.inovout.util.XmlUtils;
import org.w3c.dom.Node;

import com.lmax.disruptor.EventHandler;

public class DataEventHandler implements EventHandler<DataEvent> {
	private final Node dataConfigNode;
	private final ModelCollector modelCollector;
	private static final String DATA_PARSER_CLASS_KEY = "class";
	private final DataParser dataParser;
	public DataEventHandler(Node dataConfigNode, ModelCollector modelCollector) {
		this.dataConfigNode = dataConfigNode;
		this.modelCollector = modelCollector;
		dataParser = (DataParser) ReflectionUtils.newInstance(XmlUtils
				.getAttribute(this.dataConfigNode, DATA_PARSER_CLASS_KEY));
		dataParser.init(dataConfigNode);

	}

	public void onEvent(DataEvent event, long sequence, boolean endOfBatch) {
		Map<String,Object> data=dataParser.parse(event.getPacket());
		modelCollector.emit(event.getSchema(), data);
	}
}