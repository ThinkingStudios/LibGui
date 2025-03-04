package org.thinkingstudio.libgui_foxified.loader.gui;

import net.minecraftforge.client.ConfigScreenHandler;

public interface ModConfigScreenInitializer {
	ConfigScreenHandler.ConfigScreenFactory getModConfigScreenFactory();
}
