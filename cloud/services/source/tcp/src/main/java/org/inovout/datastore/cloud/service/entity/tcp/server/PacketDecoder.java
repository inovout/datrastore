package org.inovout.datastore.cloud.service.entity.tcp.server;

import java.util.List;

import org.inovout.datastore.cloud.service.entity.tcp.packet.PacketReader;
import org.inovout.util.ReflectionUtils;
import org.inovout.util.XmlUtils;
import org.w3c.dom.Node;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder extends ByteToMessageDecoder {

	private static final String PACKET_READER_CLASS_KEY = "class";
	private final PacketReader packetReader;

	public PacketDecoder(Node packetNode) {
		packetReader = (PacketReader) ReflectionUtils.newInstance(XmlUtils
				.getAttribute(packetNode, PACKET_READER_CLASS_KEY));
		packetReader.init(packetNode);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		packetReader.beginReader(in);
		if (packetReader.endRead()) {
			out.add(packetReader.read());
	}
	
	}

}
