package org.inovout.datastore.cloud.service.entity.tcp.packet;

import org.w3c.dom.Node;

import io.netty.buffer.ByteBuf;

public interface PacketReader {
	void init(Node packetNode);

	void beginReader(ByteBuf in);

	public boolean endRead();

	public byte[] read();


}
