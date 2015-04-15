package org.inovout.datastore.cloud.service.entity.tcp.data;

import java.util.Map;

import org.w3c.dom.Node;

public interface DataParser {

	Map<String,Object> parse(byte[] packet);

	void init(Node dataConfigNode);
}
