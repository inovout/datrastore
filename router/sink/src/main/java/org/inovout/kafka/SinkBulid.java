package org.inovout.kafka;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.inovout.data.handler.EntityBagHandler;
import org.inovout.data.lamx.EntityBagEvent;
import org.inovout.data.lamx.EntityBagEventFactory;
import org.inovout.data.lamx.EntityBagEventHandler;

import com.lmax.disruptor.dsl.Disruptor;

public class SinkBulid {

	private KafkaSink kafkaSink;
	private Executor executor = Executors.newCachedThreadPool();
	private EntityBagEventFactory factory;
	private int bufferSize = 1024;
	private Disruptor<EntityBagEvent> disruptor;
	private EntityBagEventHandler entityBagEventHandler;
	private String handlerName;
	@SuppressWarnings("unchecked")
	public SinkBulid(EntityBagHandler handler, String handlerName) {
		this.handlerName=handlerName;
		factory = new EntityBagEventFactory();
		disruptor = new Disruptor<>(factory, bufferSize, executor);
		entityBagEventHandler=new EntityBagEventHandler(handler);
		disruptor.handleEventsWith(entityBagEventHandler);
		disruptor.start();
	}
	public void Start() {
		kafkaSink = new KafkaSink(handlerName,disruptor);
		kafkaSink.SendData();
	}

}
