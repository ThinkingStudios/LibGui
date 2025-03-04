package org.thinkingstudio.libgui_foxified.events.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;

import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import org.thinkingstudio.libgui_foxified.events.api.ClientTickEvents;
import org.thinkingstudio.libgui_foxified.events.api.CoreShaderRegistrationCallback;
import org.thinkingstudio.libgui_foxified.events.api.HudRenderCallback;

import java.io.IOException;
import java.io.UncheckedIOException;

public class FoxifiedEventsImpl {
	public static void registerClientEvents(IEventBus modEventBus) {
		modEventBus.<RegisterShadersEvent>addListener(EventPriority.HIGHEST, event -> {
			try {
				CoreShaderRegistrationCallback.RegistrationContext context = (id, vertexFormat, loadCallback) -> {
					ShaderProgram program = new ShaderProgram(event.getResourceProvider(), id, vertexFormat);
					event.registerShader(program, loadCallback);
				};
				CoreShaderRegistrationCallback.EVENT.invoker().registerShaders(context);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});

		MinecraftForge.EVENT_BUS.<RenderGuiEvent.Post>addListener(EventPriority.HIGHEST, event -> {
			HudRenderCallback.EVENT.invoker().onHudRender(event.getGuiGraphics(), event.getPartialTick());
		});
		MinecraftForge.EVENT_BUS.<TickEvent.ClientTickEvent>addListener(EventPriority.HIGHEST, event -> {
			if (event.phase == TickEvent.Phase.START) {
				ClientTickEvents.START_CLIENT_TICK.invoker().onStartTick(MinecraftClient.getInstance());
			} else if (event.phase == TickEvent.Phase.END) {
				ClientTickEvents.END_CLIENT_TICK.invoker().onEndTick(MinecraftClient.getInstance());
			}
		});
	}
}
