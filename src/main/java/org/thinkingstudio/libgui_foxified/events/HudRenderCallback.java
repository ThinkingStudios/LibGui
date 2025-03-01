package org.thinkingstudio.libgui_foxified.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public interface HudRenderCallback {
	Event<HudRenderCallback> EVENT = EventFactory.createArrayBacked(HudRenderCallback.class, (listeners) -> (matrixStack, delta) -> {
		for (HudRenderCallback event : listeners) {
			event.onHudRender(matrixStack, delta);
		}
	});

	/**
	 * Called after rendering the whole hud, which is displayed in game, in a world.
	 *
	 * @param drawContext the {@link DrawContext} instance
	 * @param tickCounter the {@link RenderTickCounter} instance
	 */
	void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter);
}
