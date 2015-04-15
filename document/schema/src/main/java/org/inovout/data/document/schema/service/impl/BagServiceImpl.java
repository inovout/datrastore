package org.inovout.data.document.schema.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.inovout.InovoutException;
import org.inovout.cache.AccessType;
import org.inovout.cache.CacheFactory;
import org.inovout.cache.PathCache;
import org.inovout.data.document.persistence.CollectionInfo;
import org.inovout.data.document.persistence.DataStoreRegions;
import org.inovout.data.document.schema.model.Bag;
import org.inovout.data.document.schema.model.Element;
import org.inovout.data.document.schema.repository.BagRepository;
import org.inovout.data.document.schema.repository.ElementRepository;
import org.inovout.data.document.schema.service.BagService;
import org.inovout.lock.InterProcessMutex;
import org.inovout.util.Contracts;
import org.inovout.util.JsonUtils;
import org.inovout.util.StringUtils;
import org.inovout.zookeeper.ZooKeeperClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("bagService")
public class BagServiceImpl implements BagService{
	
	@Autowired
	private  BagRepository bagRepository;


	@Autowired
	private  ElementRepository elementRepository;
	
	private static final PathCache pathCache = CacheFactory.builderPathCache()
			.setRootPath(DataStoreRegions.DATASTORE_CAHCE_REGION_ROOT_PATH)
			.setRegionName(DataStoreRegions.DOCUMENT)
			.setAccessType(AccessType.WRITE_ONLY).build();

	@Transactional 
	public Bag createNewBag(Bag bag) {
		String database = bag.getDatabase().trim();
		Contracts.RequiresForString(database, "database is null or empty.");
		String bagName = bag.getName().trim();
		Contracts.RequiresForString(bagName,
				"bag.name is null or empty.");

		String bagPath = StringUtils.combinePathString(
				pathCache.getRegionPath(), bagName);
		InterProcessMutex mutex = new InterProcessMutex(bag.getName());
		try {
			mutex.acquire();
			CuratorFramework zookeeperClient = ZooKeeperClientFactory
					.getClient(BagServiceImpl.class);
			zookeeperClient.start();
			if (zookeeperClient.checkExists().forPath(bagPath) == null) {
				zookeeperClient.create().forPath(bagPath);
			} else {
				throw new InovoutException(bag.getName() + "has exists");
			}
		} catch (Exception e) {
			throw new InovoutException(e);
		} finally {
			mutex.release();
		}
		bag.setDatabase(database);
		bag.setName(bagName);

		bag = bagRepository.save(bag);
		CollectionInfo collectionContext = new CollectionInfo();
		collectionContext.setDatabase(database);
		collectionContext.setCollection(bagName);
		collectionContext.setPrimaryKeys(getPrimaryKeys(bag));
	    String collectionJson = JsonUtils.objectToJson(collectionContext);
		pathCache.put(bagName, collectionJson);
		return bag;
	}

	private String[] getPrimaryKeys(Bag bag) {
		List<String> primarykeys = new ArrayList<String>();
		for (Element property : bag.getElements()) {
			if (property.getIsPrimaryKey()) {
				primarykeys.add(property.getName());
			}
		} 
		return primarykeys.toArray(new String[primarykeys.size()]);
	}
}
