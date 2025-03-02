package org.thinkingstudio.libgui_foxified.events.impl;

import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import io.github.cottonmc.cotton.gui.impl.ScreenNetworkingImpl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import org.thinkingstudio.libgui_foxified.events.api.ClientTickEvents;
import org.thinkingstudio.libgui_foxified.events.api.CoreShaderRegistrationCallback;
import org.thinkingstudio.libgui_foxified.events.api.HudRenderCallback;

import java.io.IOException;
import java.io.UncheckedIOException;

public class FoxifiedEventsImpl {
	public static void registerClientEvents(IEventBus modEventBus) {
		modEventBus.addListener(EventPriority.HIGHEST, RegisterShadersEvent.class, event -> {
			try {
				CoreShaderRegistrationCallback.EVENT.invoker().registerShaders((id, vertexFormat, loadCallback) -> {
					event.registerShader(new ShaderProgram(event.getResourceProvider(), id, vertexFormat), loadCallback);
				});
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});

		NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, RenderGuiEvent.Post.class, event -> {
			HudRenderCallback.EVENT.invoker().onHudRender(event.getGuiGraphics(), event.getPartialTick());
		});
		NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, ClientTickEvent.Pre.class, event -> {
			ClientTickEvents.START_CLIENT_TICK.invoker().onStartTick(MinecraftClient.getInstance());
		});
		NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, ClientTickEvent.Post.class, event -> {
			ClientTickEvents.END_CLIENT_TICK.invoker().onEndTick(MinecraftClient.getInstance());
		});
	}

	public static void registerCommonEvents(IEventBus modEventBus) {
		modEventBus.addListener(RegisterPayloadHandlersEvent.class, event -> {
			final PayloadRegistrar registrar = event.registrar(LibGuiCommon.MOD_ID);

			registrar.playBidirectional(ScreenNetworkingImpl.ScreenMessage.ID, ScreenNetworkingImpl.ScreenMessage.CODEC, (payload, context) -> {
				ScreenNetworkingImpl.handle(ServerLifecycleHooks.getCurrentServer(), context.player(), payload);
			});
		});
	}
}
