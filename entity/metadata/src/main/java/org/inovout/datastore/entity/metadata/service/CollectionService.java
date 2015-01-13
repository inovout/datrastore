package org.inovout.datastore.entity.metadata.service;

import java.util.HashMap;

import org.apache.curator.framework.CuratorFramework;
import org.inovout.InovoutException;
import org.inovout.cache.AccessType;
import org.inovout.cache.CacheFactory;
import org.inovout.cache.PachCache;
import org.inovout.datastore.entity.metadata.model.Collection;
import org.inovout.datastore.entity.metadata.repository.CollectionRepository;
import org.inovout.lock.InterProcessMutex;
import org.inovout.util.Contracts;
import org.inovout.zookeeper.ZooKeeperClientFactory;

public class CollectionService {

	private final CollectionRepository collectionRepository;

	public CollectionService(CollectionRepository collectionRepository) {
		this.collectionRepository = collectionRepository;
	}

	private static final String CAHCE_ROOT_PATH = "/3cloud/metadata/";

	public Collection createCollection(Collection collection) {
		String database = collection.getDatabase().trim();
		Contracts.RequiresForString(database, "database is null or empty.");
		PachCache pathCache = CacheFactory.builderPathCache()
				.setRootPath(CAHCE_ROOT_PATH)
				.setRegionName(collection.getName())
				.setAccessType(AccessType.WRITE_ONLY).build();
		InterProcessMutex mutex = new InterProcessMutex(collection.getName());
		try {
			mutex.acquire();
			CuratorFramework zookeeperClient = ZooKeeperClientFactory
					.getClient(CollectionService.class);
			if (zookeeperClient.checkExists().forPath("") == null) {
				zookeeperClient.create().forPath(pathCache.getRegionPath());
			} else {
				throw new InovoutException(collection.getName() + "has exists");
			}
		} catch (Exception e) {
			throw new InovoutException(e);
		} finally {
			mutex.release();
		}
		collection.setDatabase(database);
		HashMap<String, String> collectionMap = new HashMap<String, String>();
		collection = collectionRepository.insertCollection(collection);
		collectionMap.put("database", database);
		pathCache.put("metatable", collectionMap);

		return collection;
	}
}
