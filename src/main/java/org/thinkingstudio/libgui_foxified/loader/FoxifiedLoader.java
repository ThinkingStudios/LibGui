package org.thinkingstudio.libgui_foxified.loader;

import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class FoxifiedLoader {
	public static boolean isDevelopmentEnvironment() {
		return !FMLLoader.isProduction();
	}

	public static Path getConfigDir() {
		return FMLPaths.CONFIGDIR.get();
	}
}
