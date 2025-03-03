package org.thinkingstudio.libgui_foxified.events.impl;

import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import io.github.cottonmc.cotton.gui.impl.ScreenNetworkingImpl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

import org.thinkingstudio.libgui_foxified.events.api.ClientTickEvents;
import org.thinkingstudio.libgui_foxified.events.api.CoreShaderRegistrationCallback;
import org.thinkingstudio.libgui_foxified.events.api.HudRenderCallback;
import org.thinkingstudio.libgui_foxified.network.LibGuiC2SPacket;
import org.thinkingstudio.libgui_foxified.network.LibGuiS2CPacket;

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
		NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, TickEvent.ClientTickEvent.class, event -> {
			if (event.phase == TickEvent.Phase.START) {
				ClientTickEvents.START_CLIENT_TICK.invoker().onStartTick(MinecraftClient.getInstance());
			} else if (event.phase == TickEvent.Phase.END) {
				ClientTickEvents.END_CLIENT_TICK.invoker().onEndTick(MinecraftClient.getInstance());
			}
		});
	}

	public static void registerCommonEvents(IEventBus modEventBus) {
		modEventBus.addListener(RegisterPayloadHandlerEvent.class, event -> {
			final IPayloadRegistrar registrar = event.registrar(LibGuiCommon.MOD_ID).versioned(LibGuiCommon.MOD_ID);

			registrar.play(ScreenNetworkingImpl.SCREEN_MESSAGE_C2S, LibGuiC2SPacket::new, handler -> {
				handler.server(LibGuiC2SPacket::handle);
			});
			registrar.play(ScreenNetworkingImpl.SCREEN_MESSAGE_S2C, LibGuiS2CPacket::new, handler -> {
				handler.client(LibGuiS2CPacket::handle);
			});
		});
	}
}
