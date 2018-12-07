package engine.network;

import java.nio.ByteBuffer;

import com.esotericsoftware.kryo.Kryo;

public class Packet {

	public static class Packet1allHostsIP_ID {
		public String[] IPs;
		public Integer[] IDs;
	}

	public static class Packet2Message {
		public String message;
	}

	public static class Packet3ByteBuffer {
		public ByteBuffer byteBuffer;
	}

	public static class Packet4Function {
		public int functionID;
		public Object[] objects;
	}

	public static class Packet5RandomSeed {
		public long seed;
	}

	public static void RegisterAll(Kryo kryo) {
		kryo.register(Packet1allHostsIP_ID.class);
		kryo.register(Packet2Message.class);
		kryo.register(Packet4Function.class);
		kryo.register(Packet5RandomSeed.class);
		kryo.register(long.class);
		kryo.register(Boolean.class);
		kryo.register(Integer[].class);
		kryo.register(String[].class);
		kryo.register(byte[].class);
		kryo.register(String.class);
		kryo.register(Object[].class);
		kryo.register(org.joml.Vector2f.class);
	}
}
