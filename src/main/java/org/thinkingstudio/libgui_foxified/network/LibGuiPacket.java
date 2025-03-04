package org.thinkingstudio.libgui_foxified.network;

import io.github.cottonmc.cotton.gui.impl.ScreenNetworkingImpl;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.function.Supplier;

public class LibGuiPacket {
	private int syncId;
	private Identifier message;
	private PacketByteBuf rest;
	public LibGuiPacket(int syncId, Identifier message, PacketByteBuf rest) {
		this.syncId = syncId;
		this.message = message;
		this.rest = rest;
	}
	public static void encode(LibGuiPacket packet, PacketByteBuf friendlyByteBuf){
		friendlyByteBuf.writeVarInt(packet.syncId);
		friendlyByteBuf.writeIdentifier(packet.message);
		friendlyByteBuf.writeBytes(packet.rest);
	}
	public static LibGuiPacket decode(PacketByteBuf friendlyByteBuf){
		return new LibGuiPacket(friendlyByteBuf.readVarInt(), friendlyByteBuf.readIdentifier(), friendlyByteBuf);
	}
	public static boolean handle(LibGuiPacket packet, Supplier<NetworkEvent.Context> ctx){
		ctx.get().enqueueWork(()-> ScreenNetworkingImpl.handle(ServerLifecycleHooks.getCurrentServer(), ctx.get().getSender(), packet.rest));
		return true;
	}
}
