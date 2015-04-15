package org.inovout.datastore.cloud.service.entity.tcp.model;

import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;

public interface ModelBuilder {
	Set<Map<String, Object>> build(Map<String,Object> data);

	void init(Node modelConfigNode);
}
