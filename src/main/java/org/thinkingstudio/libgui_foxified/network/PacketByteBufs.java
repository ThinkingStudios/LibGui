package org.thinkingstudio.libgui_foxified.network;

import io.netty.buffer.Unpooled;

import net.minecraft.network.PacketByteBuf;

public class PacketByteBufs {
	/**
	 * Returns a new heap memory-backed instance of packet byte buf.
	 *
	 * @return a new buf
	 */
	public static PacketByteBuf create() {
		return new PacketByteBuf(Unpooled.buffer());
	}
}
