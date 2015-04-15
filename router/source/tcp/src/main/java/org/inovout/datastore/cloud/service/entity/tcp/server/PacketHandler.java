package org.inovout.datastore.cloud.service.entity.tcp.server;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.inovout.config.Configuration;
import org.inovout.datastore.cloud.service.entity.tcp.data.DataCollector;
import org.inovout.datastore.cloud.service.entity.tcp.data.DataEvent;
import org.inovout.datastore.cloud.service.entity.tcp.data.DataEventFactory;
import org.inovout.datastore.cloud.service.entity.tcp.data.DataEventHandler;
import org.inovout.datastore.cloud.service.entity.tcp.model.ModelCollector;
import org.inovout.datastore.cloud.service.entity.tcp.model.ModelEvent;
import org.inovout.datastore.cloud.service.entity.tcp.model.ModelEventFactory;
import org.inovout.datastore.cloud.service.entity.tcp.model.ModelEventHandler;
import org.w3c.dom.Node;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class PacketHandler<T> extends SimpleChannelInboundHandler<T> {

	private final DataCollector dataCollector;
	private final Configuration configuration;
	private final String schema;
	public static final String SCHEMA_KEY = "schema";

	public PacketHandler(Configuration configuration, Node dataXmlNode,
			Node modelXmlNode) {
		this.configuration = configuration;
		this.schema = configuration.get(SCHEMA_KEY);
		bufferSize = this.configuration.getInt(BUFFER_SIZE_KEY,
				DEFAULT_BUFFER_SIZE);

		ModelEventHandler modelEventHandler = new ModelEventHandler(
				modelXmlNode);
		ModelCollector modelCollector = getModelCollector(modelEventHandler);

		DataEventHandler dataEventHandler = new DataEventHandler(dataXmlNode,
				modelCollector);
		dataCollector = getDataCollector(dataEventHandler);

	}

	private static final String BUFFER_SIZE_KEY = "buffer.size";
	private static final int DEFAULT_BUFFER_SIZE = 1024;
	private final int bufferSize;

	@SuppressWarnings("unchecked")
	private DataCollector getDataCollector(DataEventHandler dataEventHandler) {
		Executor executor = Executors.newCachedThreadPool();
		DataEventFactory factory = new DataEventFactory();
		Disruptor<DataEvent> disruptor = new Disruptor<DataEvent>(factory,
				bufferSize, executor);
		disruptor.handleEventsWith(dataEventHandler);
		disruptor.start();
		RingBuffer<DataEvent> ringBuffer = disruptor.getRingBuffer();

		return new DataCollector(ringBuffer);
	}

	@SuppressWarnings("unchecked")
	private ModelCollector getModelCollector(ModelEventHandler modelEventHandler) {
		Executor executor = Executors.newCachedThreadPool();
		ModelEventFactory factory = new ModelEventFactory();
		Disruptor<ModelEvent> disruptor = new Disruptor<ModelEvent>(factory,
				bufferSize, executor);
		disruptor.handleEventsWith(modelEventHandler);
		disruptor.start();
		RingBuffer<ModelEvent> ringBuffer = disruptor.getRingBuffer();

		return new ModelCollector(ringBuffer);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		byte[] data;
		if (msg instanceof DatagramPacket) {
			data = ((DatagramPacket) msg).content().array();
		} else {
			data = (byte[]) msg;
		}
		dataCollector.emit(schema, data);

	}

}
