package org.thinkingstudio.libgui_foxified.network;

import io.github.cottonmc.cotton.gui.impl.ScreenNetworkingImpl;

import net.minecraft.client.MinecraftClient;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class ModNetwork {
	public static void register(IPayloadRegistrar registrar) {
		registrar.play(LibGuiPacket.ID, LibGuiPacket::new, handler -> {
			handler.client(ModNetwork::c2sHandle);
			handler.server(ModNetwork::s2cHandle);
		});
	}

	private static void c2sHandle(LibGuiPacket payload, IPayloadContext context) {
		context.workHandler().execute(() -> {
			ScreenNetworkingImpl.handle(MinecraftClient.getInstance(), context.player().orElseThrow(), payload.rest());
		});
	}

	private static void s2cHandle(LibGuiPacket payload, IPayloadContext context) {
		context.workHandler().execute(() -> {
			ScreenNetworkingImpl.handle(ServerLifecycleHooks.getCurrentServer(), context.player().orElseThrow(), payload.rest());
		});
	}
}
