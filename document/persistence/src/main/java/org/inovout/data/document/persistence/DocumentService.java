package org.inovout.data.document.persistence;

import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.inovout.InovoutException;
import org.inovout.cache.AccessType;
import org.inovout.cache.CacheFactory;
import org.inovout.cache.PathCache;
import org.inovout.data.document.persistence.CollectionInfo;
import org.inovout.data.document.persistence.DataStoreRegions;
import org.inovout.data.document.EntityPackage;
import org.inovout.mongodb.MongoDbClient;
import org.inovout.mongodb.MongoDbClientFactory;
import org.inovout.util.JsonUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteResult;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class DocumentService {
	private static final Log LOG = LogFactory.getLog(DocumentService.class);
	private MongoDbClient mongodbClient;
	private static final PathCache pathCache = CacheFactory.builderPathCache()
			.setRootPath(DataStoreRegions.DATASTORE_CAHCE_REGION_ROOT_PATH)
			.setRegionName(DataStoreRegions.DOCUMENT)
			.setAccessType(AccessType.READ_ONLY).build();

	public DocumentService() {
		this.mongodbClient = MongoDbClientFactory
				.getClient(DocumentService.class);
	}

	public void storeEntity(EntityPackage entityPackage) {
		 String jsonCollection= pathCache.getString(entityPackage.getCollection());
		 CollectionInfo persistenceInfo =(CollectionInfo) JsonUtils.jsonToObject(jsonCollection, CollectionInfo.class);
		if(persistenceInfo==null){
			persistenceInfo=new CollectionInfo();
		}
		
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

	private void bulkDelete(CollectionInfo persistenceInfo,
			Set<Map<String, Object>> entities) {

		BulkWriteOperation bulkOperation = getMongoDbCollection(persistenceInfo)
				.initializeUnorderedBulkOperation();
		for (Map<String, Object> entity : entities) {
			if (entity.containsKey(MONGODB_COLLECTION_PRIMARY_KEY)) {
				throw new InovoutException("Entity can not has "
						+ MONGODB_COLLECTION_PRIMARY_KEY + " Key when update");
			}
			DBObject idObject = new BasicDBObject(
					MONGODB_COLLECTION_PRIMARY_KEY, getPrimaryKeyValue(
							persistenceInfo.getPrimaryKeys(), entity));

			bulkOperation.find(idObject).remove();
		}
		BulkWriteResult result = bulkOperation.execute();
		LOG.info(result);
	}

	private void bulkUpdate(CollectionInfo persistenceInfo,
			Set<Map<String, Object>> entities) {

		BulkWriteOperation bulkOperation = getMongoDbCollection(persistenceInfo)
				.initializeUnorderedBulkOperation();
		for (Map<String, Object> entity : entities) {
			if (entity.containsKey(MONGODB_COLLECTION_PRIMARY_KEY)) {
				throw new InovoutException("Entity can not has "
						+ MONGODB_COLLECTION_PRIMARY_KEY + " Key when update");
			}
			DBObject idObject = new BasicDBObject(
					MONGODB_COLLECTION_PRIMARY_KEY, getPrimaryKeyValue(
							persistenceInfo.getPrimaryKeys(), entity));
			DBObject updateObject = new BasicDBObject("$set", entity);

			bulkOperation.find(idObject).update(updateObject);
		}
		bulkOperation.execute();
	}

	private void bulkSave(CollectionInfo persistenceInfo,
			Set<Map<String, Object>> entities) {

		BulkWriteOperation bulkOperation = getMongoDbCollection(persistenceInfo)
				.initializeUnorderedBulkOperation();
		for (Map<String, Object> entity : entities) {
			if (entity.containsKey(MONGODB_COLLECTION_PRIMARY_KEY)) {
				throw new InovoutException("Entity can not has "
						+ MONGODB_COLLECTION_PRIMARY_KEY + " Key when update");
			}
			DBObject idObject = new BasicDBObject(
					MONGODB_COLLECTION_PRIMARY_KEY, getPrimaryKeyValue(
							persistenceInfo.getPrimaryKeys(), entity));
			// DBObject updateObject = new BasicDBObject("$set", entity);
			DBObject insertObject = new BasicDBObject(entity);
			insertObject.putAll(idObject);

			// bulkOperation.find(idObject).upsert().update(updateObject);
			bulkOperation.find(idObject).upsert().replaceOne(insertObject);
		}
		bulkOperation.execute();
	}

	private void bulkInsert(CollectionInfo persistenceInfo,
			Set<Map<String, Object>> entities) {

		BulkWriteOperation bulkOperation = getMongoDbCollection(persistenceInfo)
				.initializeUnorderedBulkOperation();
		for (Map<String, Object> entity : entities) {
			if (entity.containsKey(MONGODB_COLLECTION_PRIMARY_KEY)) {
				throw new InovoutException("Entity can not has "
						+ MONGODB_COLLECTION_PRIMARY_KEY + " Key when update");
			}
			DBObject insertObject = new BasicDBObject(entity);
			insertObject
					.put(MONGODB_COLLECTION_PRIMARY_KEY,
							getPrimaryKeyValue(
									persistenceInfo.getPrimaryKeys(), entity));
			bulkOperation.insert(insertObject);
			;
		}
		bulkOperation.execute();
	}

	private DBCollection getMongoDbCollection(CollectionInfo persistenceInfo) {
		return mongodbClient.getCollection(persistenceInfo.getDatabase(),
				persistenceInfo.getCollection());
	}

	private static final String MONGODB_COLLECTION_PRIMARY_KEY = "_id";

	private Object getPrimaryKeyValue(String[] primaryKeys,
			Map<String, Object> entity) {
		if (primaryKeys.length == 1) {
			return entity.get(primaryKeys[0]);
		} else {
			String primaryKeyValue = (String) entity.get(primaryKeys[0]);
			for (Integer i = 1; i < primaryKeys.length; i++) {
				primaryKeyValue += (String) entity.get(primaryKeys[i]);
			}
			return primaryKeyValue;
		}
	}

}
