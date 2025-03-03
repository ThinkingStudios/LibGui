package org.thinkingstudio.libgui_foxified.event;

import com.mojang.blaze3d.platform.Window;

import io.github.cottonmc.cotton.gui.client.CottonHud;
import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import io.github.cottonmc.cotton.gui.widget.WWidget;

import net.minecraft.client.Minecraft;

import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = LibGuiCommon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvent {
	@SubscribeEvent
	public static void hudRender(RenderGuiOverlayEvent event){
		Window window = event.getWindow();
		int hudWidth = window.getGuiScaledWidth();
		int hudHeight = window.getGuiScaledHeight();
		for (WWidget widget : CottonHud.getWidgets()) {
			CottonHud.Positioner positioner = CottonHud.getPositioners().get(widget);
			if (positioner != null) {
				positioner.reposition(widget, hudWidth, hudHeight);
			}

			widget.paint(event.getGuiGraphics(), widget.getX(), widget.getY(), -1, -1);
		}
	}

	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event){
		if (event.phase == TickEvent.Phase.END){
			for (WWidget widget : CottonHud.getWidgets()) {
				widget.tick();
			}
		}
	}
}
