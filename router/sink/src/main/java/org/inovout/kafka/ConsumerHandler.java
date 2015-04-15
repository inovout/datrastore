package org.inovout.kafka;

import java.util.HashSet;
import java.util.Set;

import org.inovout.data.document.EntityBag;
import org.inovout.data.document.EntityPackage;
import org.inovout.data.lamx.EntityBagEventProducer;
import org.inovout.util.JsonUtils;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

public class ConsumerHandler implements Runnable {
	@SuppressWarnings("rawtypes")
	private KafkaStream m_stream;
	private EntityBagEventProducer entityBagEventProducer; 
	private EntityBag entityBag =new EntityBag();
	private EntityPackage 	entityPackage; 
	private Set<EntityPackage> entityPackages;
	//private Set<Map<String,Object>> entities;
	public ConsumerHandler(@SuppressWarnings("rawtypes") KafkaStream a_stream, EntityBagEventProducer entityBagEventProducer) {
		m_stream = a_stream;
		this.entityBagEventProducer=entityBagEventProducer;
		entityBag.setSchema("schema-01");
		
	}

	public void run() {
	        @SuppressWarnings("unchecked")
			ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
	        while (it.hasNext()){
	        	String message= new String(it.next().message());	   
	        	entityPackage=(EntityPackage)JsonUtils.jsonToObject(message,EntityPackage.class);
	        	entityPackages=new HashSet<EntityPackage>();
	        	entityPackages.add(entityPackage);
	        	entityBag.setPackages(entityPackages);
	        	this.entityBagEventProducer.onData(entityBag);
	        }
	 }
}
