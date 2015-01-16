package org.inovout.datastore.entity.indexer;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.inovout.InovoutException;
import org.inovout.cache.AccessType;
import org.inovout.cache.CacheFactory;
import org.inovout.cache.PathCache;
import org.inovout.datastore.entity.common.DataStoreRegions;
import org.inovout.elasticsearch.ElasticsearchClient;
import org.inovout.elasticsearch.ElasticsearchClientFactory;

public class IndexService {
	private static final Log LOG = LogFactory.getLog(IndexService.class);
	private ElasticsearchClient elasticsearchClient;
	private static final PathCache pathCache = CacheFactory.builderPathCache()
			.setRootPath(DataStoreRegions.DATASTORE_CAHCE_REGION_ROOT_PATH)
			.setRegionName(DataStoreRegions.PERSISTENCE)
			.setAccessType(AccessType.READ_ONLY).build();

	public IndexService() {
		this.elasticsearchClient = ElasticsearchClientFactory
				.getClient(IndexService.class);
	}

	public void createIndex() {
		XContentBuilder builder;
		try {
			builder = XContentFactory.jsonBuilder().startObject();


			builder.endObject();
		} catch (IOException e) {
			throw new InovoutException(e);
		}
		CreateIndexRequest request = new CreateIndexRequest("");

		IndexResponse response = (IndexResponse) elasticsearchClient
				.getIndexRequestBuilder("twitter", "tweet").setSource(builder)
				.execute();
		pathCache.put("", null);
		LOG.info(response);
	}

	private void buildCreateAlias(XContentBuilder builder) throws IOException {
		builder.field("user", "kimchy").field("message",
				"trying out Elasticsearch");
	}
}
