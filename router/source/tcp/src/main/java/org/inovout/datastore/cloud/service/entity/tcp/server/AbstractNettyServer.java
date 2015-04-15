package org.inovout.datastore.cloud.service.entity.tcp.server;

import org.inovout.InovoutException;
import org.inovout.config.Configuration;
import org.inovout.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.ChannelFuture;

public abstract class AbstractNettyServer<B extends AbstractBootstrap<?, ?>>
/*
 * AbstractBootstrap<B extends AbstractBootstrap<B, C>, C extends Channel>> >
 */{
	private final B bootstrap;
	private final Document xmlDocumnet;
	private final Configuration configuration;

	protected AbstractNettyServer(Configuration configuration, B bootstrap,
			Document xmlDocumnet) {
		this.configuration = configuration;
		this.bootstrap = bootstrap;
		this.xmlDocumnet = xmlDocumnet;
	}

	protected Configuration getConfiguration() {
		return configuration;
	}

	protected abstract void configGroup(B bootstrap);

	protected abstract void configChanncelOption(B bootstrap);

	protected abstract void configChannel(B bootstrap);

	protected abstract void configHandler(B bootstrap);

	private static final String PACKET_NODE_NAME = "packet";
	private static final String DATA_NODE_NAME = "data";
	private static final String MODEL_NODE_NAME = "data";

	protected Node getPacketConfigNode() {
		return XmlUtils.getNode(xmlDocumnet, PACKET_NODE_NAME);
	}

	protected Node getDataConfigNode() {
		return XmlUtils.getNode(xmlDocumnet, DATA_NODE_NAME);
	}

	protected Node getModelConfigNode() {
		return XmlUtils.getNode(xmlDocumnet, MODEL_NODE_NAME);
	}

	private static final String PORT_KEY = "port";
	private static final int DEFULT_PORT = 8080;

	public void start() {
		configChannel(bootstrap);
		configChanncelOption(bootstrap);
		configHandler(bootstrap);
		int port = this.configuration.getInt(PORT_KEY, DEFULT_PORT);
		try {

			ChannelFuture channelFuture = bootstrap.bind(port).sync();
			channelFuture.channel().closeFuture().sync();

		} catch (InterruptedException e) {
			throw new InovoutException(e);
		}

	}
}
