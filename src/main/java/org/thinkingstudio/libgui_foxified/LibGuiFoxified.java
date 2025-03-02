package org.thinkingstudio.libgui_foxified;

import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import io.github.cottonmc.cotton.gui.impl.client.LibGuiClient;
import io.github.cottonmc.cotton.gui.impl.modmenu.ModMenuSupport;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.thinkingstudio.libgui_foxified.events.impl.FoxifiedEventsImpl;

@Mod(LibGuiCommon.MOD_ID)
public class LibGuiFoxified {
	public LibGuiFoxified(ModContainer modContainer, IEventBus modEventBus) {
		FoxifiedEventsImpl.registerCommonEvents(modEventBus);
		if (FMLLoader.getDist().isClient()) {
			FoxifiedEventsImpl.registerClientEvents(modEventBus);
			LibGuiClient.onInitializeClient();
			modContainer.registerExtensionPoint(IConfigScreenFactory.class, new ModMenuSupport().getModConfigScreenFactory());
		}
	}
}
