package org.inovout.datastore.entity.indices;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.inovout.InovoutException;
import org.inovout.cache.AccessType;
import org.inovout.cache.CacheFactory;
import org.inovout.cache.PathCache;
import org.inovout.datastore.entity.persistence.DataStoreRegions;
import org.inovout.datastore.entity.persistence.EntityPackage;
import org.inovout.elasticsearch.ElasticsearchClient;
import org.inovout.elasticsearch.ElasticsearchClientFactory;

public class DocumentService {
	private static final Log LOG = LogFactory.getLog(IndexService.class);
	private ElasticsearchClient elasticsearchClient;
	private static final PathCache pathCache = CacheFactory.builderPathCache()
			.setRootPath(DataStoreRegions.DATASTORE_CAHCE_REGION_ROOT_PATH)
			.setRegionName(DataStoreRegions.TERM)
			.setAccessType(AccessType.READ_ONLY).build();

	public DocumentService() {
		this.elasticsearchClient = ElasticsearchClientFactory
				.getClient(IndexService.class);
	}

	public void storeEntity(EntityPackage entityPackage) {
		TermInfo persistenceInfo = (TermInfo) pathCache
				.get(entityPackage.getCollection());

		switch (entityPackage.getAction()) {
		case SAVE:
			bulkSave(persistenceInfo, entityPackage.getEntitis());
			break;
		case INSERT:
			bulkInsert(persistenceInfo, entityPackage.getEntitis());
			break;
		case UPDATE:
			bulkUpdate(persistenceInfo, entityPackage.getEntitis());
			break;
		case DELETE:
			bulkDelete(persistenceInfo, entityPackage.getEntitis());
			break;
		}
	}


	private void bulkSave(TermInfo persistenceInfo,
			Set<Map<String, Object>> entitis) {
		
	}

	private void bulkUpdate(TermInfo persistenceInfo,
			Set<Map<String, Object>> entitis) {
		
	}

	private void bulkDelete(TermInfo persistenceInfo,
			Set<Map<String, Object>> entitis) {
		
	}

	public void bulkInsert(TermInfo persistenceInfo,
			Set<Map<String, Object>> documents) {
		String index = persistenceInfo.getIndex();
		String type = persistenceInfo.getType();
	
		BulkRequestBuilder bulkRequest = elasticsearchClient.getClient()
				.prepareBulk();
		for (Map<String, Object> document : documents) {

			IndexRequestBuilder indexRequestBuilder = elasticsearchClient
					.getClient().prepareIndex(index, type);
			indexRequestBuilder.setSource(buildDocumentSource(document));
			bulkRequest.add(indexRequestBuilder);
		}

		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		LOG.info(bulkResponse);
	}

	private XContentBuilder buildDocumentSource(Map<String, Object> document) {
		try {
			XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
			xContentBuilder.startObject();
			for (String columnName : document.keySet()) {
				Object columnValue = document.get(columnName);
				xContentBuilder.field(columnName, columnValue);
			}

			xContentBuilder.endObject();

			return xContentBuilder;
		} catch (IOException e) {

			throw new InovoutException(e);
		}
	}
}
