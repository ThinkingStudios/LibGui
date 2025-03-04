package org.thinkingstudio.libgui_foxified;

import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import io.github.cottonmc.cotton.gui.impl.client.LibGuiClient;
import io.github.cottonmc.cotton.gui.impl.modmenu.ModMenuSupport;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.thinkingstudio.libgui_foxified.events.impl.FoxifiedEventsImpl;
import org.thinkingstudio.libgui_foxified.network.ModNetwork;

@Mod(LibGuiCommon.MOD_ID)
public class LibGuiFoxified {
	public LibGuiFoxified(){
		var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModNetwork.register();
		if (FMLLoader.getDist().isClient()){
			FoxifiedEventsImpl.registerClientEvents(modEventBus);
			LibGuiClient.onInitializeClient();
			ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ModMenuSupport().getModConfigScreenFactory());
		}
	}
}
