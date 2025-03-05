package org.thinkingstudio.libgui_foxified.events.impl;

import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;

import net.minecraft.client.MinecraftClient;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import org.thinkingstudio.libgui_foxified.events.api.ClientTickEvents;
import org.thinkingstudio.libgui_foxified.events.api.HudRenderCallback;
import org.thinkingstudio.libgui_foxified.network.ModNetwork;

public class FoxifiedEventsImpl {
	public static void registerClientEvents(IEventBus modEventBus) {
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

			ModNetwork.register(registrar);
		});
	}
}
