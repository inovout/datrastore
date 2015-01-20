package org.inovout.datastore.entity.indices;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.inovout.elasticsearch.ElasticsearchClient;
import org.inovout.elasticsearch.ElasticsearchClientFactory;
import org.inovout.util.Time;

public class IndexService {
	private static final Log LOG = LogFactory.getLog(IndexService.class);
	private ElasticsearchClient elasticsearchClient;

	public IndexService() {
		this.elasticsearchClient = ElasticsearchClientFactory
				.getClient(IndexService.class);
	}

	public String getRealIndex(String indexName) {
		String alias = indexName;
		GetAliasesRequest getAliasesRequest = new GetAliasesRequest();
		getAliasesRequest.aliases(alias);
		GetAliasesResponse getAliasesResponse = elasticsearchClient
				.getIndicesAdminClient().getAliases(getAliasesRequest)
				.actionGet();
		ImmutableOpenMap<String, List<AliasMetaData>> aliases = getAliasesResponse
				.getAliases();
		if (aliases.size() == 0) {

			indexName = alias + "_" + Time.now();
			CreateIndexRequest createIndexRequest = Requests
					.createIndexRequest(indexName);
			createIndexRequest.aliases(alias);

			CreateIndexResponse createIndexResponse = elasticsearchClient
					.getIndicesAdminClient().create(createIndexRequest)
					.actionGet();
			LOG.info(createIndexResponse);
		} else {
			indexName = (String) aliases.keys().toArray()[0];
		}
		return indexName;
	}
}
