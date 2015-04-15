package org.inovout.datastore.cloud.service.entity.tcp.server;

import org.inovout.config.Configuration;
import org.w3c.dom.Document;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class TcpNettyServer extends AbstractNettyServer<ServerBootstrap> {
	public TcpNettyServer(Configuration configuration,Document xmlDocumnet) {
		super(configuration,new ServerBootstrap(), xmlDocumnet);

	}

	@Override
	protected void configGroup(ServerBootstrap bootstrap) {
		int serverBossThreadSize = 1;
		int serverWorkerThreadSize = 1;
		EventLoopGroup bossGroup = new NioEventLoopGroup(serverBossThreadSize);
		EventLoopGroup workerGroup = new NioEventLoopGroup(
				serverWorkerThreadSize);
		bootstrap.group(bossGroup, workerGroup);
	}

	@Override
	protected void configChanncelOption(ServerBootstrap bootstrap) {
		int backlogValue = 128;

		bootstrap.option(ChannelOption.SO_BACKLOG, backlogValue);
		bootstrap.childOption(ChannelOption.SO_REUSEADDR, true);
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.childOption(ChannelOption.ALLOCATOR,
				new PooledByteBufAllocator(false));// heap buf 's better
		bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
		bootstrap.childOption(ChannelOption.SO_RCVBUF, 1048576);
		bootstrap.childOption(ChannelOption.SO_SNDBUF, 1048576);

	}

	@Override
	protected void configChannel(ServerBootstrap bootstrap) {
		bootstrap.channel(NioServerSocketChannel.class);

	}

	@Override
	protected void configHandler(ServerBootstrap bootstrap) {
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("decoder", new PacketDecoder(
						getPacketConfigNode()));
				pipeline.addLast("encoder", new StringEncoder());

				int eventExecutorSize = 32;

				EventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(
						eventExecutorSize);

				pipeline.addLast(eventExecutorGroup, new PacketHandler<byte[]>(
						getConfiguration(),
						getDataConfigNode(), getModelConfigNode()));
			}
		});
	}
}
