package org.inovout.datastore.cloud.service.entity.tcp.data;

public class DataEvent {
	private String schema;

	public void setSchema(String schema) {
		this.schema = schema;
	}

	private byte[] packet;

	public void setPacket(byte[] packet) {
		this.packet = packet;
	}

	public String getSchema() {
		return this.schema;
	}

	public byte[] getPacket() {
		return this.packet;
	}
}
