package org.inovout.datastore.cloud.services.table.service;

import org.inovout.datastore.cloud.services.table.model.TableInfo;
import org.inovout.datastore.entity.metadata.model.Bag;
import org.inovout.datastore.entity.metadata.service.BagService;

public class TableService {

	private final BagService collectionService;

	public TableService(BagService collectionService) {
		this.collectionService = collectionService;
	}

	public TableInfo createCollection(String project, TableInfo table) {
		Bag collection = table.toCollection();
		collection.setDatabase(project);

		return TableInfo.fromCollection(this.collectionService
				.createCollection(collection));
	}
}
