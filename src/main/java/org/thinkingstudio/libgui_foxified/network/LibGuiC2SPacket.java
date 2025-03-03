package org.thinkingstudio.libgui_foxified.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import io.github.cottonmc.cotton.gui.impl.ScreenNetworkingImpl;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public record LibGuiC2SPacket(int syncId, Identifier message, PacketByteBuf rest) implements CustomPayload {
	// Packet structure:
	//   syncId: int
	//   message: identifier
	//   rest: buf

	public LibGuiC2SPacket(PacketByteBuf buf) {
		this(buf.readVarInt(), buf.readIdentifier(), buf);
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeVarInt(syncId);
		buf.writeIdentifier(message);
		buf.writeBytes(rest);
	}

	@Override
	public Identifier id() {
		return ScreenNetworkingImpl.SCREEN_MESSAGE_C2S;
	}

	public static void handle(LibGuiC2SPacket packet, PlayPayloadContext context) {
		context.workHandler().execute(() -> {
			ScreenNetworkingImpl.handle(ServerLifecycleHooks.getCurrentServer(), context.player().orElseThrow(), packet.rest);
		});
	}
}
