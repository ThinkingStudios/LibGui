package org.thinkingstudio.libgui_foxified.config;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.impl.modmenu.ConfigGui;

import net.minecraft.client.gui.screens.Screen;

import net.minecraft.network.chat.Component;

import net.minecraftforge.client.ConfigScreenHandler;

public class ModConfigManager {
	private static final ConfigScreenHandler.ConfigScreenFactory FACTORY = new ConfigScreenHandler.ConfigScreenFactory(ModConfigManager::getConfigScreen);
	private static Screen getConfigScreen(Screen parent){
		return new CottonClientScreen(Component.translatable("options.libgui.libgui_settings"), new ConfigGui(parent)) {
			@Override
			public void onClose() {
				this.minecraft.setScreen(parent);
			}
		};
	}

	public static ConfigScreenHandler.ConfigScreenFactory getConfigFactory() {
		return FACTORY;
	}
}
