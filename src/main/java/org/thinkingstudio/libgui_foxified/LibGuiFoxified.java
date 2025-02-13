package org.thinkingstudio.libgui_foxified;

import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import io.github.cottonmc.cotton.gui.impl.client.LibGuiClient;

import io.github.cottonmc.cotton.gui.impl.modmenu.ModMenuSupport;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(LibGuiCommon.MOD_ID)
public class LibGuiFoxified {
	public LibGuiFoxified(ModContainer modContainer) {
		LibGuiCommon.onInitialize();
		if (FMLLoader.getDist().isClient()) {
			LibGuiClient.onInitializeClient();
			modContainer.registerExtensionPoint(IConfigScreenFactory.class, new ModMenuSupport().getModConfigScreenFactory());
		}
	}
}
