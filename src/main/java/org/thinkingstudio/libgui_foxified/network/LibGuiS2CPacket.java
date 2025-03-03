package org.thinkingstudio.libgui_foxified.network;

import io.github.cottonmc.cotton.gui.impl.ScreenNetworkingImpl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public record LibGuiS2CPacket(int syncId, Identifier message, PacketByteBuf rest) implements CustomPayload {
	// Packet structure:
	//   syncId: int
	//   message: identifier
	//   rest: buf

	public LibGuiS2CPacket(PacketByteBuf buf) {
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
		return ScreenNetworkingImpl.SCREEN_MESSAGE_S2C;
	}

	public static void handle(LibGuiS2CPacket packet, PlayPayloadContext context) {
		context.workHandler().execute(() -> {
			ScreenNetworkingImpl.handle(MinecraftClient.getInstance(), context.player().orElseThrow(), packet.rest);
		});
	}
}
