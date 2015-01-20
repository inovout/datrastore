package org.inovout.datastore.entity.indices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.inovout.InovoutException;
import org.inovout.cache.AccessType;
import org.inovout.cache.CacheFactory;
import org.inovout.cache.PathCache;
import org.inovout.datastore.entity.indices.MappingInfo;
import org.inovout.datastore.entity.indices.PropertyInfo;
import org.inovout.datastore.entity.persistence.DataStoreRegions;
import org.inovout.elasticsearch.ElasticsearchClient;
import org.inovout.elasticsearch.ElasticsearchClientFactory;

public class MappingService {
	private static final Log LOG = LogFactory.getLog(IndexService.class);
	private ElasticsearchClient elasticsearchClient;
	private static final PathCache pathCache = CacheFactory.builderPathCache()
			.setRootPath(DataStoreRegions.DATASTORE_CAHCE_REGION_ROOT_PATH)
			.setRegionName(DataStoreRegions.TERM)
			.setAccessType(AccessType.READ_ONLY).build();

	private IndexService indexService;

	public MappingService(IndexService indexService) {
		this.indexService = indexService;
		this.elasticsearchClient = ElasticsearchClientFactory
				.getClient(IndexService.class);
	}

	public void createMapping(MappingInfo mappingInfo) {
		String indexName = indexService.getRealIndex(mappingInfo.getIndex());
		XContentBuilder mappingBuilder;
		try {
			mappingBuilder = XContentFactory.jsonBuilder();
			mappingBuilder.startObject().startObject(indexName)
					.startObject("properties");
			buildProperty(mappingInfo.getProperties(), mappingBuilder);
			mappingBuilder.endObject().endObject().endObject();

		} catch (IOException e) {
			throw new InovoutException(e);
		}
		PutMappingRequest putMappingRequest = new PutMappingRequest(indexName);
		putMappingRequest.type(mappingInfo.getType()).source(mappingBuilder);

		PutMappingResponse putMappingResponse = elasticsearchClient
				.getIndicesAdminClient().putMapping(putMappingRequest)
				.actionGet();
		TermInfo termInfo = new TermInfo();
		termInfo.setIndex(indexName);
		termInfo.setType(mappingInfo.getType());
		termInfo.setPrimaryKeys(getPrimaryKeys(mappingInfo));
		pathCache.put(mappingInfo.getType(), termInfo);
		LOG.info(putMappingResponse);
	}

	private String[] getPrimaryKeys(MappingInfo collection) {
		List<String> primarykeys = new ArrayList<String>();
		for (PropertyInfo property : collection.getProperties()) {
			if (property.getIsPrimaryKey()) {
				primarykeys.add(property.getName());
			}
		}
		return (String[]) primarykeys.toArray();
	}

	private void buildProperty(Set<PropertyInfo> properties,
			XContentBuilder mappingBuilder) throws IOException {
		for (PropertyInfo property : properties) {
			mappingBuilder.startObject(property.getName())
					.field("type", property.getType()).field("store", "yes")
					.endObject();

		}
	}
}
