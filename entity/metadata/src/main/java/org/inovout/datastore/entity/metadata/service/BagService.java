package org.inovout.datastore.entity.metadata.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.inovout.InovoutException;
import org.inovout.cache.AccessType;
import org.inovout.cache.CacheFactory;
import org.inovout.cache.PathCache;
import org.inovout.datastore.entity.metadata.model.Bag;
import org.inovout.datastore.entity.metadata.model.Element;
import org.inovout.datastore.entity.metadata.repository.BagRepository;
import org.inovout.datastore.entity.persistence.CollectionInfo;
import org.inovout.datastore.entity.persistence.DataStoreRegions;
import org.inovout.lock.InterProcessMutex;
import org.inovout.util.Contracts;
import org.inovout.util.StringUtils;
import org.inovout.zookeeper.ZooKeeperClientFactory;

public class BagService {

	private final BagRepository collectionRepository;

	public BagService(BagRepository collectionRepository) {
		this.collectionRepository = collectionRepository;
	}

	private static final PathCache pathCache = CacheFactory.builderPathCache()
			.setRootPath(DataStoreRegions.DATASTORE_CAHCE_REGION_ROOT_PATH)
			.setRegionName(DataStoreRegions.DOCUMENT)
			.setAccessType(AccessType.WRITE_ONLY).build();

	public Bag createCollection(Bag collection) {
		String database = collection.getDatabase().trim();
		Contracts.RequiresForString(database, "database is null or empty.");
		String collectionName = collection.getName().trim();
		Contracts.RequiresForString(collectionName,
				"collection.name is null or empty.");

		String collectionPath = StringUtils.combinePathString(
				pathCache.getRegionPath(), collectionName);
		InterProcessMutex mutex = new InterProcessMutex(collection.getName());
		try {

			mutex.acquire();
			CuratorFramework zookeeperClient = ZooKeeperClientFactory
					.getClient(BagService.class);
			if (zookeeperClient.checkExists().forPath(collectionPath) == null) {
				zookeeperClient.create().forPath(collectionPath);
			} else {
				throw new InovoutException(collection.getName() + "has exists");
			}
		} catch (Exception e) {
			throw new InovoutException(e);
		} finally {
			mutex.release();
		}
		collection.setDatabase(database);
		collection.setName(collectionName);

		collection = collectionRepository.insertCollection(collection);
		CollectionInfo collectionContext = new CollectionInfo();
		collectionContext.setDatabase(database);
		collectionContext.setCollection(collectionName);
		collectionContext.setPrimaryKeys(getPrimaryKeys(collection));
		pathCache.put(collectionName, collectionContext);

		return collection;
	}

	private String[] getPrimaryKeys(Bag collection) {
		List<String> primarykeys = new ArrayList<String>();
		for (Element property : collection.getPropertires()) {
			if (property.getIsPrimaryKey()) {
				primarykeys.add(property.getName());
			}
		}
		return (String[]) primarykeys.toArray();
	}
}
