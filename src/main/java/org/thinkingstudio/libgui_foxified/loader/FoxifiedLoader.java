package org.thinkingstudio.libgui_foxified.loader;

import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class FoxifiedLoader {
	public static boolean isDevelopmentEnvironment() {
		return !FMLLoader.isProduction();
	}

	public static Path getConfigDir() {
		return FMLPaths.CONFIGDIR.get();
	}
}
