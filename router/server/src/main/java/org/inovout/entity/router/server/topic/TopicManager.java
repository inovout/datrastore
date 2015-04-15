package org.inovout.entity.router.server.topic;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.inovout.InovoutException;
import org.inovout.cache.AccessType;
import org.inovout.cache.CacheFactory;
import org.inovout.cache.PathCache;
import org.inovout.util.JsonUtils;
import org.inovout.zookeeper.ZooKeeperClientFactory;

public class TopicManager {

	private String[] currentTopics;

	public String[] GetTopics() {
		return currentTopics;
	}

	private List<TopicNotification> topicNotifications = new ArrayList<TopicNotification>();

	private CuratorFramework zookeeperClient;
	private PathChildrenCache zookeeperCache;
	private static final PathCache pathCache = CacheFactory.builderPathCache()
			.setRootPath(DataStoreRegions.DATASTORE_CAHCE_REGION_ROOT_PATH)
			.setRegionName(DataStoreRegions.DOCUMENT)
			.setAccessType(AccessType.READ_ONLY).build();
	
	private String regionPath = DataStoreRegions.DATASTORE_CAHCE_REGION_ROOT_PATH
			+ DataStoreRegions.DOCUMENT;// +"/"+ DataStoreRegions.HANDLERTOPICS;
	public TopicManager(){
		zookeeperClient = ZooKeeperClientFactory.getClient(TopicManager.class);
		String topicsStr = pathCache.getString(DataStoreRegions.KEY);
		currentTopics = topicsStr.split(",");
		Init();
	}

	public String[] GetTopicByHandlerName(String handlerName) {	
		String[] tops = pathCache.get(DataStoreRegions.HANDLERTOPICS).toString().split(",");
		return tops;
	}

	public void AddSubcribers(TopicNotification topicNotification) {
		this.topicNotifications.add(topicNotification);
	}

	public void Fire() {
		for (TopicNotification topicNotify : topicNotifications) {
			topicNotify.Notify(currentTopics);
		}
	}

	// 订阅zookeeper
	public void Init() {
		zookeeperCache = new PathChildrenCache(zookeeperClient, regionPath,
				true);
		zookeeperCache.getListenable().addListener(
				createZooKeeperCacheListener());
		try {
			zookeeperCache.start();
		} catch (Exception e) {
			throw new InovoutException(e);
		}
	}

	private PathChildrenCacheListener createZooKeeperCacheListener() {
		return new PathChildrenCacheListener() {
			public void childEvent(CuratorFramework client,
					PathChildrenCacheEvent event) throws Exception {

				PathChildrenCacheEvent.Type eventType = event.getType();
				System.out.println(eventType);
				switch (eventType) {
				case CONNECTION_RECONNECTED:
					zookeeperCache.rebuild();
					break;
				case CONNECTION_SUSPENDED:
				case CONNECTION_LOST:
					break;
				default:
					currentTopics = ChildDataToString(event.getData().getData());
					Fire();
				}
			}
		};
	}

	private String[] ChildDataToString(byte[] data) {	
		return JsonUtils.getString(data).split(",");
	}

}
