package org.thinkingstudio.libgui_foxified.events.api;

import org.thinkingstudio.libgui_foxified.base.api.Event;
import org.thinkingstudio.libgui_foxified.base.api.EventFactory;
import net.minecraft.client.gui.DrawContext;

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
	 * @param tickCounter the {@link float} instance
	 */
	void onHudRender(DrawContext drawContext, float tickCounter);
}
