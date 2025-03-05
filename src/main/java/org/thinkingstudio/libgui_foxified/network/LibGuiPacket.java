package org.thinkingstudio.libgui_foxified.network;

import io.github.cottonmc.cotton.gui.impl.ScreenNetworkingImpl;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.function.Supplier;

public record LibGuiPacket(int syncId, Identifier message, PacketByteBuf rest) {

	public LibGuiPacket(PacketByteBuf buf) {
		this(buf.readVarInt(), buf.readIdentifier(), buf);
	}

	public static void write(LibGuiPacket packet, PacketByteBuf buf){
		buf.writeVarInt(packet.syncId);
		buf.writeIdentifier(packet.message);
		buf.writeBytes(packet.rest);
	}

	public static boolean handle(LibGuiPacket packet, Supplier<NetworkEvent.Context> ctx){
		ctx.get().enqueueWork(()-> ScreenNetworkingImpl.handle(ServerLifecycleHooks.getCurrentServer(), ctx.get().getSender(), packet.rest));
		return true;
	}
}
