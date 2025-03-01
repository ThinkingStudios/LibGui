package org.thinkingstudio.libgui_foxified;

import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import io.github.cottonmc.cotton.gui.impl.ScreenNetworkingImpl;
import io.github.cottonmc.cotton.gui.impl.client.LibGuiClient;
import io.github.cottonmc.cotton.gui.impl.modmenu.ModMenuSupport;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.thinkingstudio.libgui_foxified.events.ClientTickEvents;
import org.thinkingstudio.libgui_foxified.events.CoreShaderRegistrationCallback;
import org.thinkingstudio.libgui_foxified.events.HudRenderCallback;

import java.io.IOException;
import java.io.UncheckedIOException;

@Mod(LibGuiCommon.MOD_ID)
public class LibGuiFoxified {
	public LibGuiFoxified(ModContainer modContainer, IEventBus modEventBus) {
		modEventBus.addListener(RegisterPayloadHandlersEvent.class, event -> {
			final PayloadRegistrar registrar = event.registrar("1");

			registrar.playToClient(ScreenNetworkingImpl.ScreenMessage.ID, ScreenNetworkingImpl.ScreenMessage.CODEC, (payload, context) -> {
				ScreenNetworkingImpl.handle(ServerLifecycleHooks.getCurrentServer(), context.player(), payload);
			});
		});

		if (FMLLoader.getDist().isClient()) {
			LibGuiClient.onInitializeClient();
			modContainer.registerExtensionPoint(IConfigScreenFactory.class, new ModMenuSupport().getModConfigScreenFactory());

			modEventBus.addListener(EventPriority.HIGHEST, RegisterShadersEvent.class, event -> {
				try {
					CoreShaderRegistrationCallback.EVENT.invoker().registerShaders((id, vertexFormat, loadCallback) -> {
						event.registerShader(new ShaderProgram(event.getResourceProvider(), id, vertexFormat), loadCallback);
					});
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			});
			modEventBus.addListener(EventPriority.HIGHEST, RenderGuiEvent.Post.class, event -> {
				HudRenderCallback.EVENT.invoker().onHudRender(event.getGuiGraphics(), event.getPartialTick());
			});

			NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, ClientTickEvent.Pre.class, event -> ClientTickEvents.START_CLIENT_TICK.invoker().onStartTick(MinecraftClient.getInstance()));
			NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, ClientTickEvent.Post.class, event -> ClientTickEvents.END_CLIENT_TICK.invoker().onEndTick(MinecraftClient.getInstance()));
		}
	}
}
