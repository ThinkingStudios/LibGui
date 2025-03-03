package org.thinkingstudio.libgui_foxified.network;

import io.github.cottonmc.cotton.gui.impl.ScreenNetworkingImpl;

import net.minecraft.network.FriendlyByteBuf;

import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.function.Supplier;

public class LibGuiPacket {
	private int syncId;
	private ResourceLocation message;
	private FriendlyByteBuf rest;
	public LibGuiPacket(int syncId, ResourceLocation message, FriendlyByteBuf rest) {
		this.syncId = syncId;
		this.message = message;
		this.rest = rest;
	}
	public static void encode(LibGuiPacket packet, FriendlyByteBuf friendlyByteBuf){
		friendlyByteBuf.writeVarInt(packet.syncId);
		friendlyByteBuf.writeResourceLocation(packet.message);
		friendlyByteBuf.writeBytes(packet.rest);
	}
	public static LibGuiPacket decode(FriendlyByteBuf friendlyByteBuf){
		return new LibGuiPacket(friendlyByteBuf.readVarInt(), friendlyByteBuf.readResourceLocation(), friendlyByteBuf);
	}
	public static boolean handle(LibGuiPacket packet, Supplier<NetworkEvent.Context> ctx){
		ctx.get().enqueueWork(()-> ScreenNetworkingImpl.handle(ServerLifecycleHooks.getCurrentServer(), ctx.get().getSender(), packet.rest));
		return true;
	}
}
