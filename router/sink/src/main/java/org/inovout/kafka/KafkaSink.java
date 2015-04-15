package org.inovout.kafka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.inovout.data.lamx.EntityBagEvent;
import org.inovout.data.lamx.EntityBagEventProducer;
import org.inovout.entity.router.server.topic.TopicManager;
import org.inovout.entity.router.server.topic.TopicNotification;

import com.lmax.disruptor.dsl.Disruptor;

import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class KafkaSink implements TopicNotification {
	private String[] topics;
	private ConsumerConnector consumer;
	private TopicManager topicManager = new TopicManager();
	private EntityBagEventProducer entityBagEventProducer;
	private List<KafkaStream<byte[], byte[]>> streams = new ArrayList<KafkaStream<byte[], byte[]>>();
	public KafkaSink(String handlerName, Disruptor<EntityBagEvent> disruptor) {
		this.topics = topicManager.GetTopicByHandlerName(handlerName);
		this.entityBagEventProducer = new EntityBagEventProducer(
				disruptor.getRingBuffer());
		this.consumer = KafkaConsumerFactory.getConsumer(KafkaSink.class);

	}

	public void SendData() {
		SubscribeTopics();
	}

	public void Notify(String[] topics) {
		this.topics = topics;
		CancelSubTopics();
		SubscribeTopics();
	}

	private void SubscribeTopics() {
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		int a_numThreads = topics.length;
		for (int i = 0; i < topics.length; i++) {
			topicCountMap.put(topics[i], new Integer(a_numThreads));
		}
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer
				.createMessageStreams(topicCountMap);
		
		for (int i = 0; i < topics.length; i++) {
			streams.addAll(consumerMap.get(topics[i]));
		}

		Executor executor = Executors.newFixedThreadPool(a_numThreads);
		for (@SuppressWarnings("rawtypes") final KafkaStream stream : streams) {
			executor.execute(new ConsumerHandler(stream,
					this.entityBagEventProducer));
		}

	}

	private void CancelSubTopics() {
		streams.clear();
	}

}
