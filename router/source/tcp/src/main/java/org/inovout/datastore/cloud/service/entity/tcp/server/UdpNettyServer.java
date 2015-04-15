package org.inovout.datastore.cloud.service.entity.tcp.server;

import org.inovout.config.Configuration;
import org.w3c.dom.Document;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UdpNettyServer extends AbstractNettyServer<Bootstrap> {
	public UdpNettyServer(Configuration configuration, Document xmlDocumnet) {
		super(configuration, new Bootstrap(), xmlDocumnet);
	}

	@Override
	protected void configChannel(Bootstrap bootstrap) {
		bootstrap.channel(NioDatagramChannel.class);
	}

	@Override
	protected void configChanncelOption(Bootstrap bootstrap) {
		bootstrap.option(ChannelOption.SO_BROADCAST, true);

	}

	@Override
	protected void configGroup(Bootstrap bootstrap) {
		int serverWorkerThreadSize = 1;
		EventLoopGroup workerGroup = new NioEventLoopGroup(
				serverWorkerThreadSize);
		bootstrap.group(workerGroup);

	}

	@Override
	protected void configHandler(Bootstrap bootstrap) {
		bootstrap.handler(new PacketHandler<DatagramPacket>(getConfiguration(),
				getDataConfigNode(), getModelConfigNode()));

	}

}
