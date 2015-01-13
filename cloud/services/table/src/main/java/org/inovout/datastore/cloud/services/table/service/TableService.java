package org.inovout.datastore.cloud.services.table.service;

import org.inovout.datastore.cloud.services.table.model.TableInfo;
import org.inovout.datastore.entity.metadata.model.Collection;
import org.inovout.datastore.entity.metadata.service.CollectionService;

public class TableService {

	private final CollectionService collectionService;

	public TableService(CollectionService collectionService) {
		this.collectionService = collectionService;
	}

	public TableInfo createCollection(String project, TableInfo table) {
		Collection collection = table.toCollection();
		collection.setDatabase(project);

		return TableInfo.fromCollection(this.collectionService
				.createCollection(collection));
	}
}
