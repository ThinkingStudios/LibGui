package org.thinkingstudio.libgui_foxified.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.MinecraftClient;

public class ClientTickEvents {
	/**
	 * Called at the start of the client tick.
	 */
	public static final Event<StartTick> START_CLIENT_TICK = EventFactory.createArrayBacked(StartTick.class, callbacks -> client -> {
		for (StartTick event : callbacks) {
			event.onStartTick(client);
		}
	});

	/**
	 * Called at the end of the client tick.
	 */
	public static final Event<EndTick> END_CLIENT_TICK = EventFactory.createArrayBacked(EndTick.class, callbacks -> client -> {
		for (EndTick event : callbacks) {
			event.onEndTick(client);
		}
	});

	@FunctionalInterface
	public interface StartTick {
		void onStartTick(MinecraftClient client);
	}

	@FunctionalInterface
	public interface EndTick {
		void onEndTick(MinecraftClient client);
	}
}
