package org.thinkingstudio.libgui_foxified;

import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import io.github.cottonmc.cotton.gui.impl.client.LibGuiClient;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import org.thinkingstudio.libgui_foxified.config.ModConfigManager;
import org.thinkingstudio.libgui_foxified.network.ModNetworking;

@Mod(LibGuiCommon.MOD_ID)
public class LibGuiFoxified {
	public LibGuiFoxified(){
		ModNetworking.register();
		if (FMLLoader.getDist().isClient()){
			LibGuiClient.onInitializeClient();
			ModLoadingContext.get().registerExtensionPoint(ModConfigManager.getConfigFactory().getClass(), ModConfigManager::getConfigFactory);
		}
	}
}
