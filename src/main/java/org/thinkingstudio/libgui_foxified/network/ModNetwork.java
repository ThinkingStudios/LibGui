package org.thinkingstudio.libgui_foxified.network;

import io.github.cottonmc.cotton.gui.impl.ScreenNetworkingImpl;

import net.minecraft.client.MinecraftClient;

import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class ModNetwork {
	public static void register(PayloadRegistrar registrar) {
		registrar.playBidirectional(
			ScreenNetworkingImpl.ScreenMessage.ID,
			ScreenNetworkingImpl.ScreenMessage.CODEC,
			new DirectionalPayloadHandler<>(ModNetwork::c2sHandle, ModNetwork::s2cHandle)
		);
	}

	private static void c2sHandle(ScreenNetworkingImpl.ScreenMessage payload, IPayloadContext context) {
		ScreenNetworkingImpl.handle(MinecraftClient.getInstance(), context.player(), payload);
	}

	private static void s2cHandle(ScreenNetworkingImpl.ScreenMessage payload, IPayloadContext context) {
		ScreenNetworkingImpl.handle(ServerLifecycleHooks.getCurrentServer(), context.player(), payload);
	}
}
