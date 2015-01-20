package org.inovout.datastore.cloud.service.entity.http;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.inovout.cache.AccessType;
import org.inovout.cache.CacheFactory;
import org.inovout.cache.PathCache;
import org.inovout.datastore.entity.EntityAction;
import org.inovout.datastore.entity.EntityBag;
import org.inovout.datastore.entity.persistence.DataStoreRegions;
import org.inovout.kafka.KafkaProducerFactory;
import org.inovout.util.JsonUtils;
import org.inovout.util.Time;

public class EntityService {
	private static final Log LOG = LogFactory.getLog(EntityService.class);

	private Producer<Long, byte[]> producer;
	private static final PathCache pathCache = CacheFactory.builderPathCache()
			.setRootPath(DataStoreRegions.DATASTORE_CAHCE_REGION_ROOT_PATH)
			.setRegionName(DataStoreRegions.TERM)
			.setAccessType(AccessType.READ_ONLY).build();

	public EntityService() {
		producer = KafkaProducerFactory.getProducer(EntityService.class);

	}

	public void createEntity(String table, Map<String, Object> entity) {

		EntityBag entityBag = new EntityBag();
		entityBag.setCollection(table);
		entityBag.setAction(EntityAction.INSERT);
		Set<Map<String, Object>> entities = new HashSet<Map<String, Object>>();
		entities.add(entity);
		entityBag.setEntitis(entities);
		String topic = pathCache.getString(table);
		producer.send(new KeyedMessage<Long, byte[]>(topic, Time.now(),
				JsonUtils.getBytes(entityBag)));
		LOG.info(entityBag);
	}
}
