package org.inovout.test;

import org.inovout.cache.AccessType;
import org.inovout.cache.CacheFactory;
import org.inovout.cache.PathCache;
import org.inovout.entity.router.server.topic.DataStoreRegions;
import org.inovout.entity.router.server.topic.TopicManager;
import org.inovout.entity.router.server.topic.TopicNotification;
import org.junit.Test;

public class TestTopicManager {
		
	private static String rootPath = "/3cloud/datastore/";
	private static String regionName = "document";
	private static final PathCache writeablePathCache;
	//private static final PathCache readablePathCache;
	static {
		/*readablePathCache = CacheFactory.builderPathCache()
				.setRootPath(rootPath).setRegionName(regionName)
				.setAccessType(AccessType.READ_ONLY).build();*/

		writeablePathCache = CacheFactory.builderPathCache()
				.setRootPath(rootPath).setRegionName(regionName)
				.setAccessType(AccessType.WRITE_ONLY).build();
	}
	
	class TestTopicNotification implements TopicNotification{
		String[] topics;
		public void Notify(String[] topics) {
			this.topics = topics;
			System.out.println(pringChars(topics));
		}
	}
	
	
	@Test
	public void testSub() throws InterruptedException{
		TestTopicNotification testTopicNotification=new TestTopicNotification();
		TopicManager topicManager=new TopicManager();
		topicManager.AddSubcribers(testTopicNotification);
		writeablePathCache.put(DataStoreRegions.HANDLERTOPICS, "p2,p3");
		System.out.println("当前主题："+pringChars(topicManager.GetTopicByHandlerName("dd")));
		Thread.sleep(5000);
		writeablePathCache.put(DataStoreRegions.HANDLERTOPICS, "p4,p5,p6");
		System.out.println("当前主题："+pringChars(topicManager.GetTopicByHandlerName("dd")));
		/*
		readablePathCache.get("d");*/
	}
	
	
	private String pringChars(String[] str){
		String result="";
		for(String a : str){
			result+=a;
		}
		return result;
	}
}
